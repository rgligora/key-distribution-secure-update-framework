package hr.fer.kdsuf.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hr.fer.kdsuf.exception.exceptions.*;
import hr.fer.kdsuf.mapper.SoftwarePackageMapper;
import hr.fer.kdsuf.model.domain.*;
import hr.fer.kdsuf.model.dto.EncryptedDto;
import hr.fer.kdsuf.model.dto.SoftwareDto;
import hr.fer.kdsuf.model.dto.SoftwarePackageDto;
import hr.fer.kdsuf.model.dto.UpdateHistoryDto;
import hr.fer.kdsuf.model.request.*;
import hr.fer.kdsuf.repository.*;
import hr.fer.kdsuf.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private SoftwarePackageRepository softwarePackageRepository;

    @Autowired
    private SoftwareRepository softwareRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private SoftwarePackageMapper softwarePackageMapper;

    @Autowired
    private UpdateHistoryServiceImpl updateHistoryService;

    @Autowired
    private VaultSecretServiceImpl vaultSecretService;

    @Override
    public EncryptedDto checkForUpdates(EncryptedDto request) {
        String devicePublicKeyBase64 = request.getDevicePublicKey();
        String keyNameSuffix = getKeyNameSuffix(devicePublicKeyBase64);
        String decryptedCheckUpdateRequest = vaultSecretService.decryptData("aes-key-" + keyNameSuffix, request.getEncryptedData());

        CheckUpdateRequest checkUpdateRequest = parseDecryptedCheckUpdateRequest(decryptedCheckUpdateRequest);

        String deviceId = checkUpdateRequest.getDeviceId();

        Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new DeviceNotFoundException(deviceId));
        if(device.getStatus() != DeviceStatus.UPDATE_PENDING){
            return createEncryptedUpdateInfoResponse(keyNameSuffix, request.getDevicePublicKey(), false, null);
        }
        List<SoftwarePackage> softwarePackages = softwarePackageRepository.findSoftwarePackageByModelsContaining(device.getModel());

        List<String> availableSoftwarePackageIds = softwarePackages.stream()
                .filter(softwarePackage -> softwarePackage.getStatus() == PackageStatus.AVAILABLE)
                .filter(softwarePackage -> {
                    List<UpdateHistoryDto> updateHistories = updateHistoryService.retrieveUpdateHistories(deviceId, softwarePackage.getSoftwarePackageId());
                    return updateHistories.stream().noneMatch(updateHistory -> updateHistory.getStatus() == UpdateStatus.SUCCESS);
                })
                .map(SoftwarePackage::getSoftwarePackageId)
                .collect(Collectors.toList());


        if (availableSoftwarePackageIds.isEmpty()) {
            return createEncryptedUpdateInfoResponse(keyNameSuffix, request.getDevicePublicKey(), false, null);
        }
        return createEncryptedUpdateInfoResponse(keyNameSuffix, request.getDevicePublicKey(), true, availableSoftwarePackageIds);

    }

    @Override
    public EncryptedDto downloadUpdate(EncryptedDto request) {
        String devicePublicKeyBase64 = request.getDevicePublicKey();
        String keyNameSuffix = getKeyNameSuffix(devicePublicKeyBase64);
        String decryptedUpdateDeviceRequest = vaultSecretService.decryptData("aes-key-" + keyNameSuffix, request.getEncryptedData());

        UpdateDeviceRequest updateDeviceRequest = parseDecryptedUpdateDeviceRequest(decryptedUpdateDeviceRequest);

        String deviceId = updateDeviceRequest.getDeviceId();
        Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new DeviceNotFoundException(deviceId));
        device.setStatus(DeviceStatus.UPDATING);
        deviceRepository.save(device);
        String softwarePackageId = updateDeviceRequest.getSoftwarePackageId();
        SoftwarePackage softwarePackage = softwarePackageRepository.findById(softwarePackageId).orElseThrow(() -> new SoftwarePackageNotFoundException(softwarePackageId));

        String signature = vaultSecretService.retrieveSignature(softwarePackageId);
        SoftwarePackageDto softwarePackageDto = softwarePackageMapper.modelToDto(softwarePackage);
        softwarePackageDto.setSignature(signature);


        return createEncryptedSoftwarePackageResponse(keyNameSuffix, request.getDevicePublicKey(), softwarePackageDto);
    }

    @Override
    public EncryptedDto verifyUpdate(EncryptedDto encryptedVerifyUpdateRequest) {
        String devicePublicKeyBase64 = encryptedVerifyUpdateRequest.getDevicePublicKey();
        String keyNameSuffix = getKeyNameSuffix(devicePublicKeyBase64);
        String decryptedVerifyUpdateRequest = vaultSecretService.decryptData("aes-key-" + keyNameSuffix, encryptedVerifyUpdateRequest.getEncryptedData());

        VerifyUpdateRequest verifyUpdateRequest = parseDecryptedVerifyUpdateRequest(decryptedVerifyUpdateRequest);

        SoftwarePackageDto softwarePackageDto = verifyUpdateRequest.getSoftwarePackageDto();
        softwarePackageDto.setSignature(null);
        String signature = verifyUpdateRequest.getSignature();

        Device device = deviceRepository.findById(verifyUpdateRequest.getDeviceId()).orElseThrow(() -> new DeviceNotFoundException(verifyUpdateRequest.getDeviceId()));

        byte[] dataBytes = softwarePackageDto.toString().getBytes(StandardCharsets.UTF_8);

        boolean valid = vaultSecretService.verifyData("software-signing-key", dataBytes, signature);

        if (!valid){
            device.setStatus(DeviceStatus.UPDATE_PENDING);
        }

        String validInfoJson;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> validInfoMap = new HashMap<>();
            validInfoMap.put("valid", valid);
            validInfoJson = objectMapper.writeValueAsString(validInfoMap);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert UpdateInfo to JSON", e);
        }

        String encryptedValidInfoJson = vaultSecretService.encryptData("aes-key-" + keyNameSuffix, validInfoJson);

        return new EncryptedDto(encryptedVerifyUpdateRequest.getDevicePublicKey(), encryptedValidInfoJson);
    }


    @Override
    public String flashing(FlashingSuccess flashingSuccess) {
        Device device = deviceRepository.findById(flashingSuccess.getDeviceId()).orElseThrow(() -> new DeviceNotFoundException(flashingSuccess.getDeviceId()));

        String status;
        UpdateStatus updateStatus;

        if(flashingSuccess.isSuccess()){
            device.setStatus(DeviceStatus.ACTIVE);
            device.setLastUpdated(LocalDateTime.now());
            status = DeviceStatus.ACTIVE.toString();
            updateStatus = UpdateStatus.SUCCESS;
        }else {
            device.setStatus(DeviceStatus.INACTIVE);
            status = DeviceStatus.INACTIVE.toString();
            updateStatus = UpdateStatus.FAILED;
        }

        flashingSuccess.getSoftwarePackageIds().forEach(softwarePackageId ->
                updateHistoryService.createUpdateHistory(
                        new CreateUpdateHistoryRequest(updateStatus, flashingSuccess.getDeviceId(), softwarePackageId)
                )
        );

        deviceRepository.save(device);
        return status;
    }

    private String getKeyNameSuffix(String devicePublicKeyBase64) {
        return devicePublicKeyBase64.length() > 20 ? devicePublicKeyBase64.substring(10, 20) : devicePublicKeyBase64;
    }

    private CheckUpdateRequest parseDecryptedCheckUpdateRequest(String decryptedRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(decryptedRequest, CheckUpdateRequest.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to parse decrypted CheckUpdateRequest", e);
        }
    }
    private UpdateDeviceRequest parseDecryptedUpdateDeviceRequest(String decryptedRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(decryptedRequest, UpdateDeviceRequest.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to parse decrypted UpdateDeviceRequest", e);
        }
    }
    private VerifyUpdateRequest parseDecryptedVerifyUpdateRequest(String decryptedRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            return objectMapper.readValue(decryptedRequest, VerifyUpdateRequest.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to parse decrypted VerifyUpdateRequest", e);
        }
    }

    private EncryptedDto createEncryptedUpdateInfoResponse(String keyNameSuffix, String devicePublicKey, boolean isAvailable, List<String> softwarePackageIds) {
        UpdateInfo updateInfo = new UpdateInfo(isAvailable, softwarePackageIds);
        String updateInfoJson = convertUpdateInfoToJson(updateInfo);

        String encryptedUpdateInfoJson = vaultSecretService.encryptData("aes-key-" + keyNameSuffix, updateInfoJson);
        return new EncryptedDto(devicePublicKey, encryptedUpdateInfoJson);
    }

    private EncryptedDto createEncryptedSoftwarePackageResponse(String keyNameSuffix, String devicePublicKey, SoftwarePackageDto softwarePackageDto) {
        String softwarePackageJson = convertSoftwarePackageToJson(softwarePackageDto);
        String encryptedUpdateInfoJson = vaultSecretService.encryptData("aes-key-" + keyNameSuffix, softwarePackageJson);
        return new EncryptedDto(devicePublicKey, encryptedUpdateInfoJson);
    }

    private String convertUpdateInfoToJson(UpdateInfo updateInfo) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> updateInfoMap = new HashMap<>();
            updateInfoMap.put("available", updateInfo.isAvailable());
            updateInfoMap.put("softwarePackageIds", updateInfo.getSoftwarePackageIds());
            return objectMapper.writeValueAsString(updateInfoMap);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert UpdateInfo to JSON", e);
        }
    }

    private String convertSoftwarePackageToJson(SoftwarePackageDto softwarePackageDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            Map<String, Object> softwarePackageMap = new HashMap<>();
            softwarePackageMap.put("softwarePackageId", softwarePackageDto.getSoftwarePackageId());
            softwarePackageMap.put("name", softwarePackageDto.getName());
            softwarePackageMap.put("creationDate", softwarePackageDto.getCreationDate());
            softwarePackageMap.put("description", softwarePackageDto.getDescription());
            softwarePackageMap.put("status", softwarePackageDto.getStatus());
            softwarePackageMap.put("signature", softwarePackageDto.getSignature());
            softwarePackageMap.put("includedSoftware", softwarePackageDto.getIncludedSoftware());
            softwarePackageMap.put("modelIds", softwarePackageDto.getModelIds());
            softwarePackageMap.put("companyId", softwarePackageDto.getCompanyId());

            return objectMapper.writeValueAsString(softwarePackageMap);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert SoftwarePackageDto to JSON", e);
        }
    }
}

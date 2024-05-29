package hr.fer.kdsuf.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.fer.kdsuf.exception.exceptions.*;
import hr.fer.kdsuf.mapper.SoftwarePackageMapper;
import hr.fer.kdsuf.model.domain.*;
import hr.fer.kdsuf.model.dto.SoftwarePackageDto;
import hr.fer.kdsuf.model.dto.UpdateHistoryDto;
import hr.fer.kdsuf.model.request.CreateUpdateHistoryRequest;
import hr.fer.kdsuf.model.request.FlashingSuccess;
import hr.fer.kdsuf.model.request.UpdateDeviceRequest;
import hr.fer.kdsuf.model.request.VerifyUpdateRequest;
import hr.fer.kdsuf.repository.*;
import hr.fer.kdsuf.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    public UpdateInfo checkForUpdates(String deviceId) {
        Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new DeviceNotFoundException(deviceId));
        if(device.getStatus() != DeviceStatus.UPDATE_PENDING){
            return new UpdateInfo(false, null);
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
            return new UpdateInfo(false, null);
        }
        return new UpdateInfo(true, availableSoftwarePackageIds);
    }

    @Override
    public SoftwarePackageDto downloadUpdate(UpdateDeviceRequest updateDeviceRequest) {
        String deviceId = updateDeviceRequest.getDeviceId();
        Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new DeviceNotFoundException(deviceId));
        device.setStatus(DeviceStatus.UPDATING);
        deviceRepository.save(device);
        String softwarePackageId = updateDeviceRequest.getSoftwarePackageId();
        SoftwarePackage softwarePackage = softwarePackageRepository.findById(softwarePackageId).orElseThrow(() -> new SoftwarePackageNotFoundException(softwarePackageId));

        String signature = vaultSecretService.retrieveSignature(softwarePackageId);
        SoftwarePackageDto softwarePackageDto = softwarePackageMapper.modelToDto(softwarePackage);
        softwarePackageDto.setSignature(signature);
        return softwarePackageDto;
    }

    @Override
    public Map<String, Boolean> verifyUpdate(VerifyUpdateRequest payload) {
        SoftwarePackageDto softwarePackageDto = payload.getSoftwarePackageDto();
        softwarePackageDto.setSignature(null);
        String signature = payload.getSignature();

        byte[] dataBytes = softwarePackageDto.toString().getBytes(StandardCharsets.UTF_8);

        boolean valid = vaultSecretService.verifyData("software-signing-key", dataBytes, signature);

        return Map.of("valid", valid);
    }


    @Override
    public String flashing(FlashingSuccess flashingSuccess) {
        Device device = deviceRepository.findById(flashingSuccess.getDeviceId()).orElseThrow(() -> new DeviceNotFoundException(flashingSuccess.getDeviceId()));

        String status;
        UpdateStatus updateStatus;

        if(flashingSuccess.isSuccess()){
            device.setStatus(DeviceStatus.ACTIVE);
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
}

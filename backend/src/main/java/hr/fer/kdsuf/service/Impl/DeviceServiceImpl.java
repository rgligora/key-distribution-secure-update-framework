package hr.fer.kdsuf.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.fer.kdsuf.exception.exceptions.DeviceNotFoundException;
import hr.fer.kdsuf.mapper.DeviceMapper;
import hr.fer.kdsuf.model.domain.Company;
import hr.fer.kdsuf.model.domain.Device;
import hr.fer.kdsuf.model.domain.DeviceStatus;
import hr.fer.kdsuf.model.domain.Model;
import hr.fer.kdsuf.model.dto.DeviceDto;
import hr.fer.kdsuf.model.dto.EncryptedDto;
import hr.fer.kdsuf.model.request.CreateDeviceRequest;
import hr.fer.kdsuf.model.request.RegisterDeviceRequest;
import hr.fer.kdsuf.repository.CompanyRepository;
import hr.fer.kdsuf.repository.DeviceRepository;
import hr.fer.kdsuf.repository.ModelRepository;
import hr.fer.kdsuf.repository.SoftwarePackageRepository;
import hr.fer.kdsuf.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private SoftwarePackageRepository softwarePackageRepository;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private VaultSecretServiceImpl vaultService;

    @Override
    public EncryptedDto registerDevice(EncryptedDto request) {
        String devicePublicKeyBase64 = request.getDevicePublicKey();
        String keyNameSuffix = devicePublicKeyBase64.length() > 20 ? devicePublicKeyBase64.substring(10, 20) : devicePublicKeyBase64;
        System.out.println(request.getEncryptedData());
        String decryptedRegisterDeviceRequest = vaultService.decryptData("aes-key-" + keyNameSuffix, request.getEncryptedData());

        ObjectMapper objectMapper = new ObjectMapper();
        RegisterDeviceRequest registerDeviceRequest;
        try {
            registerDeviceRequest = objectMapper.readValue(decryptedRegisterDeviceRequest, RegisterDeviceRequest.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to parse decrypted RegisterDeviceRequest", e);
        }

        Optional<Device> existingDevice = deviceRepository.findDeviceBySerialNo(registerDeviceRequest.getSerialNo());
        if (existingDevice.isPresent()) {
            throw new IllegalArgumentException("Device with serial number: '" + registerDeviceRequest.getSerialNo() + "' is already registered!");
        }

        Model foundModel = null;
        List<Model> allModels = modelRepository.findAll();
        for (Model model : allModels) {
            List<String> serialNos = vaultService.retrieveSerialNos(model.getModelId());
            if (serialNos.contains(registerDeviceRequest.getSerialNo())) {
                foundModel = model;
                break;
            }
        }

        if (foundModel == null) {
            throw new IllegalArgumentException("Device with serial number: '" + registerDeviceRequest.getSerialNo() + "' cannot be registered!");
        }

        Device device = new Device();
        device.setDeviceId(java.util.UUID.randomUUID().toString());
        device.setSerialNo(registerDeviceRequest.getSerialNo());
        device.setName(foundModel.getName());
        device.setStatus(DeviceStatus.REGISTERED);
        device.setRegistrationDate(LocalDate.now());
        device.setLastUpdated(LocalDateTime.now());
        device.setPublicKey(registerDeviceRequest.getPublicKey());
        device.setModel(foundModel);

        Model finalFoundModel = foundModel;
        Company company = companyRepository.findById(foundModel.getCompany().getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company with id: '" + finalFoundModel.getCompany().getCompanyId() + "' does not exist!"));


        device.setCompany(company);
        company.addDevice(device);
        companyRepository.save(company);

        DeviceDto deviceDto = deviceMapper.modelToDto(device);
        String deviceDtoJson;
        try {
            Map<String, Object> deviceDtoMap = new HashMap<>();
            deviceDtoMap.put("deviceId", deviceDto.getDeviceId());
            deviceDtoMap.put("name", deviceDto.getName());
            deviceDtoMap.put("registrationDate", deviceDto.getRegistrationDate().toString());
            deviceDtoMap.put("lastUpdated", deviceDto.getLastUpdated().toString());
            deviceDtoMap.put("companyId", deviceDto.getCompanyId());
            deviceDtoMap.put("modelId", deviceDto.getModelId());
            deviceDtoMap.put("status", deviceDto.getStatus());

            deviceDtoJson = objectMapper.writeValueAsString(deviceDtoMap);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert DeviceDto to JSON", e);
        }
        String encryptedDeviceDtoJson = vaultService.encryptData("aes-key-" + keyNameSuffix, deviceDtoJson);

        EncryptedDto response = new EncryptedDto(request.getDevicePublicKey(), encryptedDeviceDtoJson);
        return response;
    }

    @Override
    public DeviceDto confirmDevice(String deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException(deviceId));
        Model model = device.getModel();
        boolean hasSoftwarePackagePending = softwarePackageRepository.existsByModelsContaining(model);
        if(hasSoftwarePackagePending) {
            device.setStatus(DeviceStatus.UPDATE_PENDING);
        }else{
            device.setStatus(DeviceStatus.ACTIVE);
        }
        return deviceMapper.modelToDto(deviceRepository.save(device));
    }

    @Override
    public DeviceDto createDevice(CreateDeviceRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company with id: '" + request.getCompanyId() + "' does not exist!"));
        Device device = deviceMapper.requestToModel(request);

        device.setCompany(company);

        company.addDevice(device);
        companyRepository.save(company);

        return deviceMapper.modelToDto(device);
    }

    @Override
    public DeviceDto retrieveDevice(String id) {
        Device device = deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));
        return deviceMapper.modelToDto(device);
    }

    @Override
    public List<DeviceDto> retrieveDevices(String companyId) {
        List<Device> devices;
        if (companyId != null) {
            devices = deviceRepository.findDevicesByCompanyCompanyId(companyId);
        } else {
            devices = deviceRepository.findAll();
        }
        return deviceMapper.modelToDtos(devices);
    }

    @Override
    public void deleteDevice(String id) {
        boolean deviceExists = deviceRepository.existsById(id);
        if (!deviceExists) {
            throw new DeviceNotFoundException(id);
        }
        deviceRepository.deleteById(id);
    }
}
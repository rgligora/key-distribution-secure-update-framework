package hr.fer.kdsuf.service.Impl;

import hr.fer.kdsuf.exception.exceptions.DeviceNotFoundException;
import hr.fer.kdsuf.exception.exceptions.ModelNotFoundException;
import hr.fer.kdsuf.mapper.DeviceMapper;
import hr.fer.kdsuf.model.domain.Company;
import hr.fer.kdsuf.model.domain.Device;
import hr.fer.kdsuf.model.domain.DeviceStatus;
import hr.fer.kdsuf.model.domain.Model;
import hr.fer.kdsuf.model.dto.DeviceDto;
import hr.fer.kdsuf.model.request.CreateDeviceRequest;
import hr.fer.kdsuf.model.request.RegisterDeviceRequest;
import hr.fer.kdsuf.repository.CompanyRepository;
import hr.fer.kdsuf.repository.DeviceRepository;
import hr.fer.kdsuf.repository.ModelRepository;
import hr.fer.kdsuf.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private VaultSecretServiceImpl vaultService;

    @Override
    public DeviceDto registerDevice(RegisterDeviceRequest request) {
        Optional<Device> existingDevice = deviceRepository.findDeviceBySerialNo(request.getSerialNo());
        if (existingDevice.isPresent()) {
            throw new IllegalArgumentException("Device with serial number: '" + request.getSerialNo() + "' is already registered!");
        }

        Model model = modelRepository.findModelBySerialNosContaining(request.getSerialNo())
                .orElseThrow(() -> new IllegalArgumentException("Device with serial number: '" + request.getSerialNo() + "' cannot be registered!"));

        List<String> serialNos = vaultService.retrieveSerialNos(model.getModelId());

        if (!serialNos.contains(request.getSerialNo())) {
            throw new IllegalArgumentException("Device with serial number: '" + request.getSerialNo() + "' not found in serial numbers for modelId: " + model.getModelId());
        }

        Device device = new Device();
        device.setDeviceId(java.util.UUID.randomUUID().toString());
        device.setSerialNo(request.getSerialNo());
        device.setName(model.getName());
        device.setStatus(DeviceStatus.REGISTERED);
        device.setRegistrationDate(LocalDate.now());
        device.setLastUpdated(LocalDateTime.now());
        device.setModel(model);

        Company company = companyRepository.findById(model.getCompany().getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company with id: '" + model.getCompany().getCompanyId() + "' does not exist!"));

        device.setCompany(company);
        company.addDevice(device);
        companyRepository.save(company);
        return deviceMapper.modelToDto(device);
    }

    @Override
    public DeviceDto confirmDevice(String deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException(deviceId));
        device.setStatus(DeviceStatus.ACTIVE);
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
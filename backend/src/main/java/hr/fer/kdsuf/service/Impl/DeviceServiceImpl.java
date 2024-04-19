package hr.fer.kdsuf.service.Impl;

import hr.fer.kdsuf.exception.exceptions.DeviceNotFoundException;
import hr.fer.kdsuf.mapper.DeviceMapper;
import hr.fer.kdsuf.model.domain.Company;
import hr.fer.kdsuf.model.domain.Device;
import hr.fer.kdsuf.model.dto.DeviceDto;
import hr.fer.kdsuf.model.request.CreateDeviceRequest;
import hr.fer.kdsuf.repository.CompanyRepository;
import hr.fer.kdsuf.repository.DeviceRepository;
import hr.fer.kdsuf.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public DeviceDto createDevice(CreateDeviceRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company with id: '" + request.getCompanyId() + "' does not exist!"));
        Device device = deviceMapper.requestToModel(request);

        device.setCompany(company);
        deviceRepository.save(device);
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
    public void deleteDevice(String id) {
        boolean deviceExists = deviceRepository.existsById(id);
        if(!deviceExists){
            throw new DeviceNotFoundException(id);
        }
        deviceRepository.deleteById(id);
    }
}

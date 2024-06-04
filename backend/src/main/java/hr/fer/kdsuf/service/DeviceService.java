package hr.fer.kdsuf.service;

import hr.fer.kdsuf.model.dto.DeviceDto;
import hr.fer.kdsuf.model.dto.EncryptedDto;
import hr.fer.kdsuf.model.request.CreateDeviceRequest;
import hr.fer.kdsuf.model.request.RegisterDeviceRequest;

import java.util.List;

public interface DeviceService {

    EncryptedDto registerDevice(EncryptedDto request);

    DeviceDto confirmDevice(String deviceId);
    DeviceDto createDevice(CreateDeviceRequest request);

    DeviceDto retrieveDevice(String id);

    List<DeviceDto> retrieveDevices(String companyId);

    void deleteDevice(String id);
}

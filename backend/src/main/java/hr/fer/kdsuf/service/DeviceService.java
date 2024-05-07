package hr.fer.kdsuf.service;

import hr.fer.kdsuf.model.dto.DeviceDto;
import hr.fer.kdsuf.model.request.CreateDeviceRequest;

import java.util.List;

public interface DeviceService {

    DeviceDto registerDevice(CreateDeviceRequest request);

    DeviceDto confirmDevice(String deviceId);
    DeviceDto createDevice(CreateDeviceRequest request);

    DeviceDto retrieveDevice(String id);

    List<DeviceDto> retrieveDevices(String companyId);

    void deleteDevice(String id);
}

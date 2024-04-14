package hr.fer.kdsuf.service;

import hr.fer.kdsuf.model.dto.DeviceDto;
import hr.fer.kdsuf.model.request.CreateDeviceRequest;

public interface DeviceService {
    DeviceDto createDevice(CreateDeviceRequest request);

    DeviceDto retrieveDevice(String id);

    void deleteDevice(String id);
}

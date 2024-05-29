package hr.fer.kdsuf.service;

import hr.fer.kdsuf.model.domain.UpdateInfo;
import hr.fer.kdsuf.model.dto.SoftwarePackageDto;
import hr.fer.kdsuf.model.request.FlashingSuccess;
import hr.fer.kdsuf.model.request.UpdateDeviceRequest;
import hr.fer.kdsuf.model.request.VerifyUpdateRequest;

import java.util.Map;

public interface UpdateService {
    UpdateInfo checkForUpdates(String deviceId);

    SoftwarePackageDto downloadUpdate(UpdateDeviceRequest updateDeviceRequest);

    Map<String, Boolean> verifyUpdate(VerifyUpdateRequest payload);
    String flashing(FlashingSuccess flashingSuccess);
}

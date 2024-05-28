package hr.fer.kdsuf.service;

import hr.fer.kdsuf.model.domain.UpdateInfo;
import hr.fer.kdsuf.model.dto.SoftwarePackageDto;
import hr.fer.kdsuf.model.request.FlashingSuccess;
import hr.fer.kdsuf.model.request.UpdateDeviceRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface UpdateService {
    UpdateInfo checkForUpdates(String deviceId);

    SoftwarePackageDto downloadUpdate(UpdateDeviceRequest updateDeviceRequest);

    String flashing(FlashingSuccess flashingSuccess);
}

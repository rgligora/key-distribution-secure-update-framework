package hr.fer.kdsuf.service;

import hr.fer.kdsuf.model.domain.UpdateInfo;
import hr.fer.kdsuf.model.dto.EncryptedDto;
import hr.fer.kdsuf.model.dto.SoftwarePackageDto;
import hr.fer.kdsuf.model.request.FlashingSuccess;
import hr.fer.kdsuf.model.request.UpdateDeviceRequest;
import hr.fer.kdsuf.model.request.VerifyUpdateRequest;

import java.util.Map;

public interface UpdateService {
    EncryptedDto checkForUpdates(EncryptedDto request);

    EncryptedDto downloadUpdate(EncryptedDto updateDeviceRequest);

    EncryptedDto verifyUpdate(EncryptedDto payload);
    EncryptedDto flashing(EncryptedDto flashingSuccess);
}

package hr.fer.kdsuf.service;

import hr.fer.kdsuf.model.domain.UpdateInfo;
import hr.fer.kdsuf.model.dto.SoftwarePackageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface UpdateService {
    UpdateInfo checkForUpdates(String serialNo);

    SoftwarePackageDto downloadUpdate(String serialNo);
}

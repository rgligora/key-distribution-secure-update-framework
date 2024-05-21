package hr.fer.kdsuf.service.Impl;

import hr.fer.kdsuf.model.domain.UpdateInfo;
import hr.fer.kdsuf.model.dto.SoftwarePackageDto;
import hr.fer.kdsuf.service.UpdateService;
import org.springframework.stereotype.Service;

@Service
public class UpdateServiceImpl implements UpdateService {
    @Override
    public UpdateInfo checkForUpdates(String serialNo) {
        return null;
    }

    @Override
    public SoftwarePackageDto downloadUpdate(String serialNo) {
        return null;
    }
}

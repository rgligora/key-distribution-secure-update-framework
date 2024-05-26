package hr.fer.kdsuf.service.Impl;

import hr.fer.kdsuf.exception.exceptions.DeviceNotFoundException;
import hr.fer.kdsuf.model.domain.*;
import hr.fer.kdsuf.model.dto.SoftwarePackageDto;
import hr.fer.kdsuf.repository.DeviceRepository;
import hr.fer.kdsuf.repository.ModelRepository;
import hr.fer.kdsuf.repository.SoftwarePackageRepository;
import hr.fer.kdsuf.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private SoftwarePackageRepository softwarePackageRepository;

    @Override
    public UpdateInfo checkForUpdates(String deviceId) {
        Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new DeviceNotFoundException(deviceId));
        if(device.getStatus() != DeviceStatus.UPDATE_PENDING){
            throw new IllegalArgumentException("Device with device id: '" + deviceId + "' does not have an update pending!");
        }
        List<SoftwarePackage> softwarePackages = softwarePackageRepository.findSoftwarePackageByModelsContaining(device.getModel().getModelId());

        List<String> availableSoftwarePackageIds = softwarePackages.stream()
                .filter(softwarePackage -> softwarePackage.getStatus() == PackageStatus.AVAILABLE)
                .map(SoftwarePackage::getSoftwarePackageId)
                .collect(Collectors.toList());


        return new UpdateInfo(true, availableSoftwarePackageIds);
    }

    @Override
    public SoftwarePackageDto downloadUpdate(String softwarePackageId) {
        return null;
    }
}

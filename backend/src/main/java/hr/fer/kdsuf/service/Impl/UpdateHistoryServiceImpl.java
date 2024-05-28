package hr.fer.kdsuf.service.Impl;

import hr.fer.kdsuf.exception.exceptions.UpdateHistoryNotFoundException;
import hr.fer.kdsuf.mapper.UpdateHistoryMapper;
import hr.fer.kdsuf.model.domain.Company;
import hr.fer.kdsuf.model.domain.Device;
import hr.fer.kdsuf.model.domain.SoftwarePackage;
import hr.fer.kdsuf.model.domain.UpdateHistory;
import hr.fer.kdsuf.model.dto.UpdateHistoryDto;
import hr.fer.kdsuf.model.request.CreateUpdateHistoryRequest;
import hr.fer.kdsuf.repository.DeviceRepository;
import hr.fer.kdsuf.repository.SoftwarePackageRepository;
import hr.fer.kdsuf.repository.UpdateHistoryRepository;
import hr.fer.kdsuf.service.UpdateHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UpdateHistoryServiceImpl implements UpdateHistoryService {

    @Autowired
    private UpdateHistoryRepository updateHistoryRepository;

    @Autowired
    private UpdateHistoryMapper updateHistoryMapper;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private SoftwarePackageRepository softwarePackageRepository;

    @Override
    public UpdateHistoryDto createUpdateHistory(CreateUpdateHistoryRequest request) {
        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new IllegalArgumentException("Device with id: '" + request.getDeviceId() + "' does not exist!"));
        SoftwarePackage softwarePackage = softwarePackageRepository.findById(request.getSoftwarePackageId())
                .orElseThrow(() -> new IllegalArgumentException("Software package with id: '" + request.getSoftwarePackageId() + "' does not exist!"));

        UpdateHistory updateHistory = updateHistoryMapper.requestToModel(request);
        updateHistory.setDevice(device);
        updateHistory.setSoftwarePackage(softwarePackage);
        updateHistory.setUpdateDate(LocalDateTime.now());

        updateHistoryRepository.save(updateHistory);
        return updateHistoryMapper.modelToDto(updateHistory);
    }

    @Override
    public UpdateHistoryDto retrieveUpdateHistory(String id) {
        UpdateHistory updateHistory = updateHistoryRepository.findById(id).orElseThrow(() -> new UpdateHistoryNotFoundException(id));
        return updateHistoryMapper.modelToDto(updateHistory);
    }

    @Override
    public List<UpdateHistoryDto> retrieveUpdateHistories(String deviceId, String packageId) {
        List<UpdateHistory> updateHistories;
        if(packageId != null){
            updateHistories = updateHistoryRepository.findUpdateHistoriesBySoftwarePackageSoftwarePackageId(packageId);
        }else{
            updateHistories = updateHistoryRepository.findByDeviceDeviceId(deviceId);
        }
        return updateHistoryMapper.modelToDtos(updateHistories);
    }
}

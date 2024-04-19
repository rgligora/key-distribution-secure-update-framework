package hr.fer.kdsuf.service.Impl;

import hr.fer.kdsuf.exception.exceptions.SoftwareNotFoundException;
import hr.fer.kdsuf.exception.exceptions.SoftwarePackageNotFoundException;
import hr.fer.kdsuf.mapper.SoftwarePackageMapper;
import hr.fer.kdsuf.model.domain.Software;
import hr.fer.kdsuf.model.domain.SoftwarePackage;
import hr.fer.kdsuf.model.dto.SoftwarePackageDto;
import hr.fer.kdsuf.model.request.CreateSoftwarePackageRequest;
import hr.fer.kdsuf.repository.SoftwarePackageRepository;
import hr.fer.kdsuf.repository.SoftwareRepository;
import hr.fer.kdsuf.service.SoftwarePackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SoftwarePackageServiceImpl implements SoftwarePackageService{
    @Autowired
    private SoftwarePackageRepository softwarePackageRepository;

    @Autowired
    private SoftwarePackageMapper softwarePackageMapper;

    @Autowired
    private SoftwareRepository softwareRepository;
    @Override
    public SoftwarePackageDto createSoftwarePackage(CreateSoftwarePackageRequest request) {
        SoftwarePackage softwarePackage = softwarePackageMapper.requestToModel(request);

        List<Software> includedSoftware = request.getIncludedSoftwareIds().stream()
                .map(id -> softwareRepository.findById(id).orElse(null))
                .collect(Collectors.toList());

        softwarePackage.setIncludedSoftware(includedSoftware);
        softwarePackageRepository.save(softwarePackage);
        return softwarePackageMapper.modelToDto(softwarePackage);
    }

    @Override
    public SoftwarePackageDto retrieveSoftwarePackage(String id) {
        SoftwarePackage softwarePackage = softwarePackageRepository.findById(id).orElseThrow(() -> new SoftwarePackageNotFoundException(id));
        return softwarePackageMapper.modelToDto(softwarePackage);
    }

    @Override
    public void deleteSoftwarePackage(String id) {
        boolean softwarePackageExists = softwarePackageRepository.existsById(id);
        if (!softwarePackageExists){
            throw new SoftwareNotFoundException(id);
        }
        softwarePackageRepository.deleteById(id);
    }
}

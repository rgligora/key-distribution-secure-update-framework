package hr.fer.kdsuf.service;

import hr.fer.kdsuf.model.dto.SoftwarePackageDto;
import hr.fer.kdsuf.model.request.CreateSoftwarePackageRequest;

import java.util.List;

public interface SoftwarePackageService {

    SoftwarePackageDto createSoftwarePackage(CreateSoftwarePackageRequest request);

    SoftwarePackageDto retrieveSoftwarePackage(String id);
    
    List<SoftwarePackageDto> retrieveSoftwarePackages(String companyId);

    void deleteSoftwarePackage(String id);
}

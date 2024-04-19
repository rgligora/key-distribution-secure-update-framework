package hr.fer.kdsuf.service;

import hr.fer.kdsuf.model.dto.SoftwarePackageDto;
import hr.fer.kdsuf.model.request.CreateSoftwarePackageRequest;

public interface SoftwarePackageService {

    SoftwarePackageDto createSoftwarePackage(CreateSoftwarePackageRequest request);

    SoftwarePackageDto retrieveSoftwarePackage(String id);

    void deleteSoftwarePackage(String id);
}

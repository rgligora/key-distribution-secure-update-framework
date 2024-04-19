package hr.fer.kdsuf.service;

import hr.fer.kdsuf.model.dto.SoftwareDto;
import hr.fer.kdsuf.model.request.CreateSoftwareRequest;

public interface SoftwareService {

    SoftwareDto createSoftware(CreateSoftwareRequest request);

    SoftwareDto retrieveSoftware(String id);

    void deleteSoftware(String id);
}

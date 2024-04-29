package hr.fer.kdsuf.service;

import hr.fer.kdsuf.model.dto.SoftwareDto;
import hr.fer.kdsuf.model.request.CreateSoftwareRequest;

import java.util.List;

public interface SoftwareService {

    SoftwareDto createSoftware(CreateSoftwareRequest request);

    SoftwareDto retrieveSoftware(String id);

    List<SoftwareDto> retrieveAllSoftware();

    void deleteSoftware(String id);
}

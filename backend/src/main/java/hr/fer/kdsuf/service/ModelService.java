package hr.fer.kdsuf.service;

import hr.fer.kdsuf.model.dto.ModelDto;
import hr.fer.kdsuf.model.request.CreateModelRequest;

import java.util.List;

public interface ModelService {

    ModelDto createModel(CreateModelRequest request);

    ModelDto retrieveModel(String id);

    List<ModelDto> retrieveModels(String companyId);

    void deleteModel(String id);
}

package hr.fer.kdsuf.service.Impl;

import hr.fer.kdsuf.exception.exceptions.ModelNotFoundException;
import hr.fer.kdsuf.mapper.ModelMapper;
import hr.fer.kdsuf.model.domain.Company;
import hr.fer.kdsuf.model.domain.Device;
import hr.fer.kdsuf.model.domain.Model;
import hr.fer.kdsuf.model.dto.ModelDto;
import hr.fer.kdsuf.model.request.CreateModelRequest;
import hr.fer.kdsuf.repository.CompanyRepository;
import hr.fer.kdsuf.repository.ModelRepository;
import hr.fer.kdsuf.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelServiceImpl implements ModelService {

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private VaultSecretServiceImpl vaultService;

    @Override
    public ModelDto createModel(CreateModelRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company with id: '" + request.getCompanyId() + "' does not exist!"));
        Model model = modelMapper.requestToModel(request);

        model.setCompany(company);
        modelRepository.save(model);
        vaultService.storeSerialNos(model.getModelId(), model.getSerialNos());

        return modelMapper.modelToDto(model);
    }

    @Override
    public ModelDto retrieveModel(String id) {
        Model model = modelRepository.findById(id).orElseThrow(() -> new ModelNotFoundException(id));

        List<String> serialNos = vaultService.retrieveSerialNos(model.getModelId());
        model.setSerialNos(serialNos);

        return modelMapper.modelToDto(model);
    }

    @Override
    public List<ModelDto> retrieveModels(String companyId) {
        List<Model> models;
        if (companyId != null) {
            models = modelRepository.findModelsByCompanyCompanyId(companyId);
        } else {
            models = modelRepository.findAll();
        }
        return modelMapper.modelToDtos(models);
    }

    @Override
    public void deleteModel(String id) {
        boolean modelExists = modelRepository.existsById(id);
        if (!modelExists) {
            throw new ModelNotFoundException(id);
        }
        modelRepository.deleteById(id);
    }
}
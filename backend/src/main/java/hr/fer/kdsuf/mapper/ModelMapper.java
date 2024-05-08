package hr.fer.kdsuf.mapper;

import hr.fer.kdsuf.model.domain.Model;
import hr.fer.kdsuf.model.dto.ModelDto;
import hr.fer.kdsuf.model.request.CreateModelRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface ModelMapper {

    @Mapping(target = "modelId", expression = "java(java.util.UUID.randomUUID().toString())")
    Model requestToModel(CreateModelRequest request);

    @Mapping(target = "companyId", source = "company.companyId")
    ModelDto modelToDto(Model model);

    List<ModelDto> modelToDtos(List<Model> models);
}

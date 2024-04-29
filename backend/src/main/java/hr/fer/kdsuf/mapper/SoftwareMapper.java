package hr.fer.kdsuf.mapper;

import hr.fer.kdsuf.model.domain.Software;
import hr.fer.kdsuf.model.dto.SoftwareDto;
import hr.fer.kdsuf.model.request.CreateSoftwareRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface SoftwareMapper {

    @Mapping(target = "softwareId", expression = "java(java.util.UUID.randomUUID().toString())")
    Software requestToModel(CreateSoftwareRequest request);

    @Mapping(target = "companyId", source = "company.companyId")
    SoftwareDto modelToDto(Software software);

    List<SoftwareDto> modelToDtos(List<Software> softwares);
}

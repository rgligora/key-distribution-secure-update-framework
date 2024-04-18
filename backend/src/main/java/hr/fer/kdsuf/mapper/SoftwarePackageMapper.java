package hr.fer.kdsuf.mapper;

import hr.fer.kdsuf.model.domain.SoftwarePackage;
import hr.fer.kdsuf.model.dto.SoftwarePackageDto;
import hr.fer.kdsuf.model.request.CreateSoftwarePackageRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface SoftwarePackageMapper {

    @Mapping(target = "softwarePackageId", expression = "java(java.util.UUID.randomUUID().toString())")
    SoftwarePackage requestToModel(CreateSoftwarePackageRequest request);

    SoftwarePackageDto modelToDto(SoftwarePackage softwarePackage);

    List<SoftwarePackageDto> modelToDtos(List<SoftwarePackage> softwarePackages);

}

package hr.fer.kdsuf.mapper;

import hr.fer.kdsuf.model.domain.Model;
import hr.fer.kdsuf.model.domain.SoftwarePackage;
import hr.fer.kdsuf.model.dto.SoftwarePackageDto;
import hr.fer.kdsuf.model.request.CreateSoftwarePackageRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "Spring")
public interface SoftwarePackageMapper {

    @Mapping(target = "softwarePackageId", expression = "java(java.util.UUID.randomUUID().toString())")
    SoftwarePackage requestToModel(CreateSoftwarePackageRequest request);

    @Mapping(target = "companyId", source = "company.companyId")
    @Mapping(target = "modelIds", expression = "java(packageToModelIds(softwarePackage))")
    SoftwarePackageDto modelToDto(SoftwarePackage softwarePackage);

    List<SoftwarePackageDto> modelToDtos(List<SoftwarePackage> softwarePackages);

    default List<String> packageToModelIds(SoftwarePackage softwarePackage) {
        return softwarePackage.getModels() != null ? softwarePackage.getModels().stream()
                .map(Model::getModelId)
                .collect(Collectors.toList()) : null;
    }
}

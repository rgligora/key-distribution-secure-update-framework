package hr.fer.kdsuf.mapper;

import hr.fer.kdsuf.model.domain.Company;
import hr.fer.kdsuf.model.dto.CompanyDto;
import hr.fer.kdsuf.model.request.CreateCompanyRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "Spring", uses = {AdminUserMapper.class, DeviceMapper.class, SoftwareMapper.class, SoftwarePackageMapper.class})
public interface CompanyMapper {

    @Mapping(target = "companyId", expression = "java(java.util.UUID.randomUUID().toString())")
    Company requestToModel(CreateCompanyRequest request);



    @Mapping(target = "devices", source = "devices")
    @Mapping(target = "softwares", source = "softwares")
    @Mapping(target = "softwarePackages", source = "softwarePackages")
    @Mapping(target = "adminUsers", source = "adminUsers")
    CompanyDto modelToDto(Company company);

    List<CompanyDto> modelToDtos(List<Company> companies);

}

package hr.fer.kdsuf.mapper;

import hr.fer.kdsuf.model.domain.Company;
import hr.fer.kdsuf.model.dto.CompanyDto;
import hr.fer.kdsuf.model.request.CreateCompanyRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface CompanyMapper {

    @Mapping(target = "companyId", expression = "java(java.util.UUID.randomUUID().toString())")
    Company requestToModel(CreateCompanyRequest request);

    CompanyDto modelToDto(Company company);

    List<CompanyDto> modelToDtos(List<Company> companies);

}

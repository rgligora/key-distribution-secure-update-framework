package hr.fer.kdsuf.service;

import hr.fer.kdsuf.model.dto.CompanyDto;
import hr.fer.kdsuf.model.request.CreateCompanyRequest;

public interface CompanyService {

    CompanyDto createCompany(CreateCompanyRequest request);

    CompanyDto retrieveCompany(String id);

    void deleteCompany(String id);
}

package hr.fer.kdsuf.service.Impl;

import hr.fer.kdsuf.exception.exceptions.CompanyNotFoundException;
import hr.fer.kdsuf.mapper.CompanyMapper;
import hr.fer.kdsuf.model.domain.Company;
import hr.fer.kdsuf.model.dto.CompanyDto;
import hr.fer.kdsuf.model.request.CreateCompanyRequest;
import hr.fer.kdsuf.repository.CompanyRepository;
import hr.fer.kdsuf.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public CompanyDto createCompany(CreateCompanyRequest request) {
        Company company = companyMapper.requestToModel(request);
        companyRepository.save(company);
        return companyMapper.modelToDto(company);
    }

    @Override
    public CompanyDto retrieveCompany(String id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new CompanyNotFoundException(id));
        return companyMapper.modelToDto(company);
    }

    @Override
    public void deleteCompany(String id) {
        boolean companyExists = companyRepository.existsById(id);
        if(!companyExists){
            throw new CompanyNotFoundException((id));
        }
        companyRepository.deleteById(id);
    }
}

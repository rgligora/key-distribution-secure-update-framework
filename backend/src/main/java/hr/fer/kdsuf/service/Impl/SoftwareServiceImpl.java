package hr.fer.kdsuf.service.Impl;

import hr.fer.kdsuf.exception.exceptions.SoftwareNotFoundException;
import hr.fer.kdsuf.mapper.SoftwareMapper;
import hr.fer.kdsuf.model.domain.Company;
import hr.fer.kdsuf.model.domain.Device;
import hr.fer.kdsuf.model.domain.Software;
import hr.fer.kdsuf.model.dto.DeviceDto;
import hr.fer.kdsuf.model.dto.SoftwareDto;
import hr.fer.kdsuf.model.request.CreateSoftwareRequest;
import hr.fer.kdsuf.repository.CompanyRepository;
import hr.fer.kdsuf.repository.SoftwareRepository;
import hr.fer.kdsuf.service.SoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SoftwareServiceImpl implements SoftwareService {

    @Autowired
    SoftwareMapper softwareMapper;

    @Autowired
    SoftwareRepository softwareRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public SoftwareDto createSoftware(CreateSoftwareRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company with id: '" + request.getCompanyId() + "' does not exist!"));
        Software software = softwareMapper.requestToModel(request);

        software.setCompany(company);
        software.setUploadDate(LocalDate.now());

        company.addSoftware(software);
        companyRepository.save(company);

        return softwareMapper.modelToDto(software);
    }

    @Override
    public SoftwareDto retrieveSoftware(String id) {
        Software software = softwareRepository.findById(id).orElseThrow(() -> new SoftwareNotFoundException(id));
        return softwareMapper.modelToDto(software);
    }

    @Override
    public List<SoftwareDto> retrieveSoftwares(String companyId) {
        List<Software> softwares;
        if(companyId != null){
            softwares = softwareRepository.findSoftwaresByCompanyCompanyId(companyId);
        }else{
            softwares = softwareRepository.findAll();
        }
        return softwareMapper.modelToDtos(softwares);
    }

    @Override
    public void deleteSoftware(String id) {
        boolean softwareExists = softwareRepository.existsById(id);
        if (!softwareExists){
            throw new SoftwareNotFoundException(id);
        }
        softwareRepository.deleteById(id);
    }
}

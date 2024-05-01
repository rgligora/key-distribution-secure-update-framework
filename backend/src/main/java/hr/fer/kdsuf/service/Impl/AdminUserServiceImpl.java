package hr.fer.kdsuf.service.Impl;

import hr.fer.kdsuf.exception.exceptions.AdminNotFoundException;
import hr.fer.kdsuf.mapper.AdminUserMapper;
import hr.fer.kdsuf.mapper.DeviceMapper;
import hr.fer.kdsuf.model.domain.AdminUser;
import hr.fer.kdsuf.model.domain.Company;
import hr.fer.kdsuf.model.domain.Software;
import hr.fer.kdsuf.model.dto.AdminUserDto;
import hr.fer.kdsuf.model.request.CreateAdminUserRequest;
import hr.fer.kdsuf.repository.AdminUserRepository;
import hr.fer.kdsuf.repository.CompanyRepository;
import hr.fer.kdsuf.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public AdminUserDto createAdminUser(CreateAdminUserRequest request) {
        List<Company> companies = request.getCompanies().stream()
                .map(id -> companyRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Company with id: '" + id + "' does not exist!")))
                .collect(Collectors.toList());

        AdminUser adminUser = adminUserMapper.requestToModel(request);
        adminUser.setCompanies(companies);
        companies.forEach(company -> company.addAdminUser(adminUser));
        companyRepository.saveAll(companies);
        return adminUserMapper.modelToDto(adminUser);
    }

    @Override
    public AdminUserDto retrieveAdminUser(String id) {
        AdminUser adminUser = adminUserRepository.findById(id).orElseThrow(() -> new AdminNotFoundException(id));
        return adminUserMapper.modelToDto(adminUser);
    }

    @Override
    public void deleteAdminUser(String id) {
        boolean adminUserExists = adminUserRepository.existsById(id);
        if (!adminUserExists){
            throw new AdminNotFoundException(id);
        }
        adminUserRepository.deleteById(id);
    }
}

package hr.fer.kdsuf.service;

import hr.fer.kdsuf.model.dto.AdminUserDto;
import hr.fer.kdsuf.model.request.CreateAdminUserRequest;

public interface AdminUserService {

    AdminUserDto createAdminUser(CreateAdminUserRequest request);

    AdminUserDto retrieveAdminUser(String id);

    void deleteAdminUser(String id);
}

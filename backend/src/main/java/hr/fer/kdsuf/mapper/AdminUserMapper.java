package hr.fer.kdsuf.mapper;

import hr.fer.kdsuf.model.domain.AdminUser;
import hr.fer.kdsuf.model.dto.AdminUserDto;
import hr.fer.kdsuf.model.request.CreateAdminUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface AdminUserMapper {
    @Mapping(target = "userId", expression = "java(java.util.UUID.randomUUID().toString())")
    AdminUser requestToModel(CreateAdminUserRequest request);

    AdminUserDto modelToDto(AdminUser adminUser);

    List<AdminUserDto> modelToDtos(List<AdminUser> adminUsers);
}

package hr.fer.kdsuf.model.dto;

import hr.fer.kdsuf.model.domain.AdminUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {
    private String companyId;

    private String name;

    private List<AdminUserDto> adminUsers;
}

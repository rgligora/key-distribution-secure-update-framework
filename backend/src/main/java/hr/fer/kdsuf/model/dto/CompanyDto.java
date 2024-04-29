package hr.fer.kdsuf.model.dto;

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

    private List<DeviceDto> devices;

    private List<SoftwareDto> softwares;
}

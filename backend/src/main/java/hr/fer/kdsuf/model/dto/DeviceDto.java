package hr.fer.kdsuf.model.dto;

import hr.fer.kdsuf.model.domain.DeviceStatus;
import hr.fer.kdsuf.model.domain.Software;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto {

    private String deviceId;

    private String name;

    private LocalDate registrationDate;

    private LocalDateTime lastUpdated;

    private String firmwareVersion;

    private String companyId;

    private DeviceStatus status;

}

package hr.fer.kdsuf.model.request;

import hr.fer.kdsuf.model.domain.DeviceStatus;
import hr.fer.kdsuf.model.domain.Software;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeviceRequest {

    private String name;

    private LocalDate registrationDate;

    private LocalDateTime lastUpdated;

    private String companyId;

    private DeviceStatus status;
}

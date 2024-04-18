package hr.fer.kdsuf.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Device {
    @Id
    private String deviceId;

    private String name;

    private LocalDate registrationDate;

    private LocalDateTime lastUpdated;

    private String firmwareVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId")
    private Company companyId;

    @Enumerated(EnumType.STRING)
    private DeviceStatus status;
}
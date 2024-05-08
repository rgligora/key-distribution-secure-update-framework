package hr.fer.kdsuf.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "companyId")
    private Company company;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "modelId")
    private Model model;

    @Enumerated(EnumType.STRING)
    private DeviceStatus status;
}
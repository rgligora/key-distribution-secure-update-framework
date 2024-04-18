package hr.fer.kdsuf.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHistory {
    @Id
    private String historyId;

    private LocalDateTime updateDate;

    @Enumerated(EnumType.STRING)
    private UpdateStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deviceId")
    private Device device;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packageId")
    private SoftwarePackage softwarePackage;

}

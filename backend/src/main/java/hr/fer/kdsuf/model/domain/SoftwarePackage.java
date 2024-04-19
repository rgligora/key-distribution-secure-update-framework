package hr.fer.kdsuf.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoftwarePackage {
    @Id
    private String softwarePackageId;

    private String name;

    private LocalDate creationDate;

    private String description;

    @Enumerated(EnumType.STRING)
    private PackageStatus status;

    @ManyToMany
    @JoinTable(
            name = "software_in_package",
            joinColumns = @JoinColumn(name = "packageId"),
            inverseJoinColumns = @JoinColumn(name = "softwareId")
    )
    private List<Software> includedSoftware;

}

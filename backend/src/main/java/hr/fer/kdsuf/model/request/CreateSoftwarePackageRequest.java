package hr.fer.kdsuf.model.request;

import hr.fer.kdsuf.model.domain.PackageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSoftwarePackageRequest {
    private String name;

    private String description;

    private PackageStatus status;
}

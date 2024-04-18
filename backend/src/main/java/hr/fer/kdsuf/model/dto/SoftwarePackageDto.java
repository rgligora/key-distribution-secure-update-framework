package hr.fer.kdsuf.model.dto;

import hr.fer.kdsuf.model.domain.PackageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SoftwarePackageDto {

    private String softwarePackageId;

    private String name;

    private LocalDate creationDate;

    private String description;

    private PackageStatus status;

    private List<SoftwareDto> softwareIncluded;
}

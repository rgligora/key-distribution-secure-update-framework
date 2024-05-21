package hr.fer.kdsuf.model.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class UpdateInfo {

    boolean available;

    String softwarePackageId;
}

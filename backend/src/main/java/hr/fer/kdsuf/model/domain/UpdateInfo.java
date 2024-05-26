package hr.fer.kdsuf.model.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class UpdateInfo {

    boolean available;

    List<String> softwarePackageId;
}

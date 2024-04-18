package hr.fer.kdsuf.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Software {
    @Id
    private String softwareId;

    private String name;

    private String version;

    private LocalDate uploadDate;

    private String fileLocation;

    private Long fileSize;

    private String checksum;
}

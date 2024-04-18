package hr.fer.kdsuf.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SoftwareDto {
    private String softwareId;

    private String name;

    private String version;

    private LocalDate uploadDate;

    private String fileLocation;

    private Long fileSize;

    private String checksum;
}

package hr.fer.kdsuf.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSoftwareRequest {

    private String name;

    private String version;

    private LocalDate uploadDate;

    private String fileLocation;

}

package hr.fer.kdsuf.model.dto;

import hr.fer.kdsuf.model.domain.Software;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto {

    private String id;

    private String model;

    private String manufacturer;

    private String currentSoftware;

    private String fallbackSoftware;

}

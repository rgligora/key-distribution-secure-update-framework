package hr.fer.kdsuf.model.request;

import hr.fer.kdsuf.model.domain.Software;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeviceRequest {

    private String model;

    private String manufacturer;

    private String currentSoftware;

    private String fallbackSoftware;
}

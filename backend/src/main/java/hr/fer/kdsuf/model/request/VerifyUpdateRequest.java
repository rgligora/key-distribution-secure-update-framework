package hr.fer.kdsuf.model.request;

import hr.fer.kdsuf.model.dto.SoftwarePackageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyUpdateRequest {

    String deviceId;

    SoftwarePackageDto softwarePackageDto;

    String signature;
}

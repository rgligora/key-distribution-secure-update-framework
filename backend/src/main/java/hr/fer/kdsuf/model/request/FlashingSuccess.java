package hr.fer.kdsuf.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashingSuccess {
    String deviceId;

    List<String> softwarePackageIds;

    boolean success;
}

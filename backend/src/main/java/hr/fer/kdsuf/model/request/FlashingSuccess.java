package hr.fer.kdsuf.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashingSuccess {
    String deviceId;

    boolean success;
}

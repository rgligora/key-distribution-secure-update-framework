package hr.fer.kdsuf.model.request;

import hr.fer.kdsuf.model.domain.UpdateStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateHistoryRequest {

    private UpdateStatus status;

    private String deviceId;

    private String packageId;
}

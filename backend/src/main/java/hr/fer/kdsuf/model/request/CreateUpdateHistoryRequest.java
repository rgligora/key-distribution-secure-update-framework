package hr.fer.kdsuf.model.request;

import hr.fer.kdsuf.model.domain.UpdateStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateHistoryRequest {

    private LocalDateTime updateDate;

    private UpdateStatus status;

    private String deviceId;

    private String softwarePackageId;
}

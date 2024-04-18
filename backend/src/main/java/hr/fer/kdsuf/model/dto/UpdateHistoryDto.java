package hr.fer.kdsuf.model.dto;

import hr.fer.kdsuf.model.domain.UpdateStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateHistoryDto {
    private String historyId;

    private LocalDateTime updateDate;

    private UpdateStatus status;

    private String deviceId;

    private String packageId;
}

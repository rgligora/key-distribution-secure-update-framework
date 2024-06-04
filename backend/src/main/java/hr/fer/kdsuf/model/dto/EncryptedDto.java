package hr.fer.kdsuf.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncryptedDto {

    private String devicePublicKey;

    private String encryptedData;
}

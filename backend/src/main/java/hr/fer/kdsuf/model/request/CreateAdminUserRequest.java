package hr.fer.kdsuf.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdminUserRequest {
    private String username;

    private String email;

    private String companyId;
}

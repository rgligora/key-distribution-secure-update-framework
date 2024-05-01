package hr.fer.kdsuf.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdminUserRequest {
    private String username;

    private String email;

    private List<String> companies;
}

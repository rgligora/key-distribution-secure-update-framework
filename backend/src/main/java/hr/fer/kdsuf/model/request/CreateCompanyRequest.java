package hr.fer.kdsuf.model.request;

import hr.fer.kdsuf.model.dto.AdminUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCompanyRequest {

    private String name;

}

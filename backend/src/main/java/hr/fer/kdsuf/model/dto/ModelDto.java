package hr.fer.kdsuf.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelDto {

    private String modelId;

    private String name;

    private String companyId;

    private List<String> serialNos;

}

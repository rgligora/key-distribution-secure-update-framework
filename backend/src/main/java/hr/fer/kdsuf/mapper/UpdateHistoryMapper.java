package hr.fer.kdsuf.mapper;

import hr.fer.kdsuf.model.domain.UpdateHistory;
import hr.fer.kdsuf.model.dto.UpdateHistoryDto;
import hr.fer.kdsuf.model.request.CreateUpdateHistoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface UpdateHistoryMapper {

    @Mapping(target = "historyId", expression = "java(java.util.UUID.randomUUID().toString())")
    UpdateHistory requestToModel(CreateUpdateHistoryRequest request);

    UpdateHistoryDto modelToDto(UpdateHistory updateHistory);

    List<UpdateHistoryDto> modelToDtos(List<UpdateHistory> updateHistories);

}

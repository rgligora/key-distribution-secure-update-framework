package hr.fer.kdsuf.service;

import hr.fer.kdsuf.model.dto.UpdateHistoryDto;
import hr.fer.kdsuf.model.request.CreateUpdateHistoryRequest;

import java.util.List;

public interface UpdateHistoryService {

    UpdateHistoryDto createUpdateHistory(CreateUpdateHistoryRequest request);

    UpdateHistoryDto retrieveUpdateHistory(String id);

    List<UpdateHistoryDto> retrieveUpdateHistories(String deviceId, String packageId);

}

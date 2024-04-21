package hr.fer.kdsuf.controller;

import hr.fer.kdsuf.mapper.UpdateHistoryMapper;
import hr.fer.kdsuf.model.domain.UpdateHistory;
import hr.fer.kdsuf.model.dto.UpdateHistoryDto;
import hr.fer.kdsuf.model.request.CreateUpdateHistoryRequest;
import hr.fer.kdsuf.repository.UpdateHistoryRepository;
import hr.fer.kdsuf.service.Impl.UpdateHistoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/update-history")
@CrossOrigin
public class UpdateHistoryController {
    @Autowired
    UpdateHistoryServiceImpl service;

    @PostMapping
    public ResponseEntity<UpdateHistoryDto> createUpdateHistory(CreateUpdateHistoryRequest request){
         return ResponseEntity.ok(service.createUpdateHistory(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UpdateHistoryDto> retrieveUpdateHistory(@PathVariable("id") String id){
        return ResponseEntity.ok(service.retrieveUpdateHistory(id));
    }

    @GetMapping
    public ResponseEntity<List<UpdateHistoryDto>> retrieveUpdateHistories(
            @RequestParam(required = false) String deviceId,
            @RequestParam(required = false) String softwarePackageId
    ){
        return ResponseEntity.ok(service.retrieveUpdateHistories(deviceId, softwarePackageId));
    }


}

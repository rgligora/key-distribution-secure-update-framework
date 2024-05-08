package hr.fer.kdsuf.controller;

import hr.fer.kdsuf.model.dto.DeviceDto;
import hr.fer.kdsuf.model.dto.ModelDto;
import hr.fer.kdsuf.model.request.CreateModelRequest;
import hr.fer.kdsuf.service.Impl.ModelServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/models")
@CrossOrigin

public class ModelController {
    @Autowired
    private ModelServiceImpl service;

    @PostMapping
    public ResponseEntity<ModelDto> createModel(@RequestBody CreateModelRequest request){
        return ResponseEntity.ok(service.createModel(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelDto> retrieveModel(@PathVariable("id") String id){
        return ResponseEntity.ok(service.retrieveModel(id));
    }

    @GetMapping
    public ResponseEntity<List<ModelDto>> retrieveModels(
            @RequestParam(required = false) String companyId
    ){
        return ResponseEntity.ok(service.retrieveModels(companyId));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteModel(@PathVariable("id") String id){
        service.deleteModel(id);
        return ResponseEntity.ok("Successfully deleted!");
    }
}

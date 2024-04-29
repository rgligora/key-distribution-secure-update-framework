package hr.fer.kdsuf.controller;

import hr.fer.kdsuf.model.dto.DeviceDto;
import hr.fer.kdsuf.model.dto.SoftwareDto;
import hr.fer.kdsuf.model.request.CreateSoftwareRequest;
import hr.fer.kdsuf.service.Impl.SoftwareServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/software")
@CrossOrigin
public class SoftwareController {

    @Autowired
    private SoftwareServiceImpl service;

    @PostMapping
    public ResponseEntity<SoftwareDto> createSoftware(@RequestBody CreateSoftwareRequest request){
        return ResponseEntity.ok(service.createSoftware(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SoftwareDto> retrieveSoftware(@PathVariable("id") String id){
        return ResponseEntity.ok(service.retrieveSoftware(id));
    }

    @GetMapping
    public ResponseEntity<List<SoftwareDto>> retrieveDevices(){
        return ResponseEntity.ok(service.retrieveAllSoftware());
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteSoftware(@PathVariable("id") String id){
        service.deleteSoftware(id);
        return ResponseEntity.ok("Successfully deleted!");
    }
}

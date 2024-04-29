package hr.fer.kdsuf.controller;

import hr.fer.kdsuf.model.dto.DeviceDto;
import hr.fer.kdsuf.model.dto.UpdateHistoryDto;
import hr.fer.kdsuf.model.request.CreateDeviceRequest;
import hr.fer.kdsuf.repository.DeviceRepository;
import hr.fer.kdsuf.service.Impl.DeviceServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@CrossOrigin

public class DeviceController {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceServiceImpl service;

    @PostMapping
    public ResponseEntity<DeviceDto> createDevice(@RequestBody CreateDeviceRequest request) {
        return ResponseEntity.ok(service.createDevice(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto> retrieveDevice(@PathVariable("id") String id){
        return ResponseEntity.ok(service.retrieveDevice(id));
    }

    @GetMapping
    public ResponseEntity<List<DeviceDto>> retrieveDevices(
            @RequestParam(required = false) String companyId
    ){
        return ResponseEntity.ok(service.retrieveDevices(companyId));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteDevice(@PathVariable("id") String id){
        service.deleteDevice(id);
        return ResponseEntity.ok("Successfully deleted!");
    }

}

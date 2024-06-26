package hr.fer.kdsuf.controller;

import hr.fer.kdsuf.model.dto.DeviceDto;
import hr.fer.kdsuf.model.dto.EncryptedDto;
import hr.fer.kdsuf.model.request.CreateDeviceRequest;
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
    private DeviceServiceImpl service;

    @PostMapping("/register")
    public ResponseEntity<EncryptedDto> registerDevice(@RequestBody EncryptedDto request) {
        return ResponseEntity.ok(service.registerDevice(request));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<DeviceDto> confirmDevice(@PathVariable String id) {
        return ResponseEntity.ok(service.confirmDevice(id));
    }

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

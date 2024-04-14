package hr.fer.kdsuf.controller;

import hr.fer.kdsuf.model.domain.Device;
import hr.fer.kdsuf.model.dto.DeviceDto;
import hr.fer.kdsuf.model.request.CreateDeviceRequest;
import hr.fer.kdsuf.repository.DeviceRepository;
import hr.fer.kdsuf.service.Impl.DeviceServiceImpl;
import hr.fer.kdsuf.service.Impl.VaultSecretServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/devices")
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

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteDevice(@PathVariable("id") String id){
        service.deleteDevice(id);
        return ResponseEntity.ok("Successfully deleted!");
    }

}

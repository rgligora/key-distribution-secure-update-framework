package hr.fer.kdsuf.controller;

import hr.fer.kdsuf.exception.exceptions.DeviceNotFoundException;
import hr.fer.kdsuf.model.domain.Device;
import hr.fer.kdsuf.repository.DeviceRepository;
import hr.fer.kdsuf.service.Impl.VaultSecretServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
@Service
public class SecretsController {

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    VaultSecretServiceImpl vaultSecretService;

    @PostMapping("/{deviceId}/secret")
    public ResponseEntity<?> addSecretToDevice(@PathVariable String deviceId) {
        vaultSecretService.createSecretForDevice(deviceId);
        return ResponseEntity.ok(200);
    }
    @GetMapping("/{deviceId}/secret")
    public ResponseEntity<String> getSecretForDevice(@PathVariable String deviceId) {
        String secret = vaultSecretService.getSecretForDevice(deviceId);
        if (secret == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(secret);
    }//
}

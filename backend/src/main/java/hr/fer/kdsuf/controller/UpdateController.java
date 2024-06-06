package hr.fer.kdsuf.controller;

import hr.fer.kdsuf.model.domain.UpdateInfo;
import hr.fer.kdsuf.model.dto.EncryptedDto;
import hr.fer.kdsuf.model.dto.SoftwarePackageDto;
import hr.fer.kdsuf.model.request.FlashingSuccess;
import hr.fer.kdsuf.model.request.UpdateDeviceRequest;
import hr.fer.kdsuf.model.request.VerifyUpdateRequest;
import hr.fer.kdsuf.service.Impl.UpdateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/updates")
public class UpdateController {

    @Autowired
    private UpdateServiceImpl updateService;

    @PostMapping("/check")
    public ResponseEntity<EncryptedDto> checkForUpdates(@RequestBody EncryptedDto request) {
        return ResponseEntity.ok(updateService.checkForUpdates(request));
    }

    @PostMapping("/download")
    public ResponseEntity<EncryptedDto> downloadUpdate(@RequestBody EncryptedDto updateDeviceRequest) {
        return ResponseEntity.ok(updateService.downloadUpdate(updateDeviceRequest));
    }

    @PostMapping("/verify")
    public ResponseEntity<EncryptedDto> verifyUpdate(@RequestBody EncryptedDto request){
        return ResponseEntity.ok(updateService.verifyUpdate(request));
    }

    @PostMapping("/flashing")
    public ResponseEntity flashing(@RequestBody FlashingSuccess flashingSuccess) {
        return ResponseEntity.ok(updateService.flashing(flashingSuccess));
    }
}
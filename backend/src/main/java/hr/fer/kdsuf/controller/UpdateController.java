package hr.fer.kdsuf.controller;

import hr.fer.kdsuf.model.domain.UpdateInfo;
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

    @GetMapping("/check/{deviceId}")
    public ResponseEntity<UpdateInfo> checkForUpdates(@PathVariable String deviceId) {
        return ResponseEntity.ok(updateService.checkForUpdates(deviceId));
    }

    @PostMapping("/download")
    public ResponseEntity<SoftwarePackageDto> downloadUpdate(@RequestBody UpdateDeviceRequest updateDeviceRequest) {
        return ResponseEntity.ok(updateService.downloadUpdate(updateDeviceRequest));
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Boolean>> verifyUpdate(@RequestBody VerifyUpdateRequest payload){
        return ResponseEntity.ok(updateService.verifyUpdate(payload));
    }

    @PostMapping("/flashing")
    public ResponseEntity flashing(@RequestBody FlashingSuccess flashingSuccess) {
        return ResponseEntity.ok(updateService.flashing(flashingSuccess));
    }
}
package hr.fer.kdsuf.controller;

import hr.fer.kdsuf.model.domain.UpdateInfo;
import hr.fer.kdsuf.model.dto.SoftwarePackageDto;
import hr.fer.kdsuf.service.Impl.UpdateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/updates")
public class UpdateController {

    @Autowired
    private UpdateServiceImpl updateService;

    @GetMapping("/check/{deviceId}")
    public ResponseEntity<UpdateInfo> checkForUpdates(@PathVariable String deviceId) {
        UpdateInfo updateInfo = updateService.checkForUpdates(deviceId);
        return ResponseEntity.ok(updateInfo);
    }

    @GetMapping("/download/{updateId}")
    public ResponseEntity<SoftwarePackageDto> downloadUpdate(@PathVariable String updateId) {
        SoftwarePackageDto update = updateService.downloadUpdate(updateId);
        return ResponseEntity.ok(update);
    }
}
package hr.fer.kdsuf.controller;

import hr.fer.kdsuf.service.Impl.VaultSecretServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/keys")
@CrossOrigin
public class KeyController {

    @Autowired
    private VaultSecretServiceImpl vaultSecretService;

    @GetMapping("/backend")
    public ResponseEntity<String> checkAndCreateKey() {
        if (!vaultSecretService.isKeyPresent("backend")) {
            vaultSecretService.generateRSAKeyPair("backend");
        }
        String publicKey = vaultSecretService.retrievePublicKey("backend");
        return ResponseEntity.ok(publicKey);
    }
}

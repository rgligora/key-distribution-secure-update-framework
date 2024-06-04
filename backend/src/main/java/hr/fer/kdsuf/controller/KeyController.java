package hr.fer.kdsuf.controller;

import hr.fer.kdsuf.service.Impl.VaultSecretServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

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

    @PostMapping("/sessionKey")
    public ResponseEntity<String> createSessionKey(@RequestBody Map<String, String> payload) {
        try {
            String devicePublicKeyBase64 = payload.get("devicePublicKey");
            byte[] devicePublicKeyBytes = Base64.getDecoder().decode(devicePublicKeyBase64);
            PublicKey devicePublicKey = getPublicKeyFromBytes(devicePublicKeyBytes);

            byte[] javaPublicKeyBytes = devicePublicKey.getEncoded();
            String keyNameSuffix = devicePublicKeyBase64.length() > 20 ? devicePublicKeyBase64.substring(10, 20) : devicePublicKeyBase64;
            String keyName = "aes-key-" + keyNameSuffix;
            byte[] aesKey = vaultSecretService.generateAESKey(keyName);

            String encryptedAESKey = encryptAESKeyWithPublicKey(aesKey, devicePublicKey);

            return ResponseEntity.ok(encryptedAESKey);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to create session key: " + e.getMessage());
        }
    }

    private PublicKey getPublicKeyFromBytes(byte[] keyBytes) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    private String encryptAESKeyWithPublicKey(byte[] aesKey, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedAESKey = cipher.doFinal(aesKey);
        return Base64.getEncoder().encodeToString(encryptedAESKey);
    }
}

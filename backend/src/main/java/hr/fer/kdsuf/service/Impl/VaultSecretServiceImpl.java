package hr.fer.kdsuf.service.Impl;

import hr.fer.kdsuf.service.VaultSecretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.util.*;

@Service
public class VaultSecretServiceImpl implements VaultSecretService {

    @Autowired
    private VaultTemplate vaultTemplate;

    public void storeSerialNos(String modelId, List<String> serialNos) {
        Map<String, Object> data = new HashMap<>();
        data.put("serialNos", serialNos);
        Map<String, Object> payload = Collections.singletonMap("data", data);
        vaultTemplate.write("secret/data/model/" + modelId, payload);
    }

    public List<String> retrieveSerialNos(String modelId) {
        VaultResponse response = vaultTemplate.read("secret/data/model/" + modelId);
        if (response != null && response.getData() != null) {
            Map<String, Object> data = (Map<String, Object>) response.getData().get("data");
            if (data != null) {
                return (List<String>) data.get("serialNos");
            }
        }
        throw new IllegalArgumentException("Serial numbers not found in Vault for modelId: " + modelId);
    }
    public boolean isKeyPresent(String keyName) {
        VaultResponse response = vaultTemplate.read(String.format("transit/keys/%s", keyName));
        return response != null && response.getData() != null;
    }

    public String retrievePublicKey(String keyName) {
        VaultResponse response = vaultTemplate.read(String.format("transit/keys/%s", keyName));
        if (response != null && response.getData() != null) {
            Map<String, Object> data = (Map<String, Object>) response.getData().get("keys");
            if (data != null && data.containsKey("1")) {
                Map<String, Object> keyVersion = (Map<String, Object>) data.get("1");
                if (keyVersion != null) {
                    String publicKeyPem = (String) keyVersion.get("public_key");
                    String publicKeyDer = convertPemToDer(publicKeyPem);
                    return publicKeyDer;
                }
            }
        }
        throw new IllegalStateException("Failed to retrieve public key from Vault");
    }

    private String convertPemToDer(String pem) {
        String base64Encoded = pem.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] der = Base64.getDecoder().decode(base64Encoded);
        return Base64.getEncoder().encodeToString(der);
    }

    public void generateRSAKeyPair(String keyName) {
        Map<String, String> request = new HashMap<>();
        request.put("type", "rsa-2048");
        vaultTemplate.write(String.format("transit/keys/%s", keyName), request);
    }

    public byte[] generateAESKey(String keyName) {
        Map<String, String> request = new HashMap<>();
        request.put("type", "aes256-gcm96");
        request.put("exportable", "true");
        vaultTemplate.write(String.format("transit/keys/%s", keyName), request);
        String path = String.format("transit/export/encryption-key/%s", keyName);
        VaultResponse response = vaultTemplate.read(path);

        if (response != null && response.getData() != null) {
            Map<String, Object> data = response.getData();
            if (data.containsKey("keys")) {
                Map<String, Object> keys = (Map<String, Object>) data.get("keys");
                String keyVersion = keys.keySet().iterator().next();
                String aesKeyBase64 = (String) keys.get(keyVersion);
                return Base64.getDecoder().decode(aesKeyBase64);
            }
        }

        throw new RuntimeException("Failed to generate AES key");
    }

    public String encryptData(String keyName, String plaintext) {
        String encodedData = Base64.getEncoder().encodeToString(plaintext.getBytes());
        Map<String, String> request = new HashMap<>();
        request.put("plaintext", encodedData);

        VaultResponse response = vaultTemplate.write(String.format("transit/encrypt/%s", keyName), request);
        if (response != null && response.getData() != null) {
            return (String) response.getData().get("ciphertext");
        }
        throw new IllegalStateException("Failed to encrypt data with Vault");
    }

    public String decryptData(String keyName, String ciphertext) {
        Map<String, String> request = new HashMap<>();
        request.put("ciphertext", ciphertext);

        VaultResponse response = vaultTemplate.write(String.format("transit/decrypt/%s", keyName), request);
        if (response != null && response.getData() != null) {
            String decryptedBase64 = (String) response.getData().get("plaintext");
            return new String(Base64.getDecoder().decode(decryptedBase64));
        }
        throw new IllegalStateException("Failed to decrypt data with Vault");
    }

    public String signData(String keyName, byte[] data) {
        String encodedData = Base64.getEncoder().encodeToString(data);
        Map<String, Object> request = new HashMap<>();
        request.put("input", encodedData);

        VaultResponse response = vaultTemplate.write(String.format("transit/sign/%s", keyName), request);
        if (response != null && response.getData() != null) {
            return (String) response.getData().get("signature");
        }
        throw new IllegalStateException("Failed to sign data with Vault");
    }

    public boolean verifyData(String keyName, byte[] data, String signature) {
        String encodedData = Base64.getEncoder().encodeToString(data);
        Map<String, Object> request = new HashMap<>();
        request.put("input", encodedData);
        request.put("signature", signature);

        VaultResponse response = vaultTemplate.write(String.format("transit/verify/%s", keyName), request);
        if (response != null && response.getData() != null) {
            return (Boolean) response.getData().get("valid");
        }
        throw new IllegalStateException("Failed to verify data with Vault");
    }

    public void storeSignature(String packageId, String signature) {
        Map<String, Object> data = new HashMap<>();
        data.put("signature", signature);
        Map<String, Object> payload = Collections.singletonMap("data", data);
        vaultTemplate.write("secret/data/signatures/" + packageId, payload);
    }

    public String retrieveSignature(String packageId) {
        VaultResponse response = vaultTemplate.read("secret/data/signatures/" + packageId);
        if (response != null && response.getData() != null) {
            Map<String, Object> data = (Map<String, Object>) response.getData().get("data");
            if (data != null) {
                return (String) data.get("signature");
            }
        }
        throw new IllegalArgumentException("Signature not found in Vault for packageId: " + packageId);
    }

    public void deleteSignature(String packageId) {
        vaultTemplate.delete("secret/data/signatures/" + packageId);
    }
}

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

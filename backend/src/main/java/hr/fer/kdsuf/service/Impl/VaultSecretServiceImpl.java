package hr.fer.kdsuf.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.util.Collections;
import java.util.Map;

@Service
public class VaultSecretServiceImpl {

    @Autowired
    private VaultTemplate vaultTemplate;

    public void storeModelId(String modelId) {
        Map<String, String> data = Collections.singletonMap("modelId", modelId);
        vaultTemplate.write("secret/data/model/" + modelId, data);
    }

    public String retrieveModelId(String modelId) {
        VaultResponse response = vaultTemplate.read("secret/data/model/" + modelId);
        if (response != null && response.getData() != null) {
            Map<String, Object> data = (Map<String, Object>) response.getData().get("data");
            return (String) data.get("modelId");
        }
        throw new IllegalArgumentException("ModelId not found in Vault: " + modelId);
    }
}

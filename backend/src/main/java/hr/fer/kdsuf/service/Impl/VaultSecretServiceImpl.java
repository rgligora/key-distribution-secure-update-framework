package hr.fer.kdsuf.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class VaultSecretServiceImpl {

    @Autowired
    private VaultTemplate vaultTemplate;

    public void storeSerialNos(String modelId, List<String> serialNos) {
        Map<String, List<String>> data = Collections.singletonMap("serialNos", serialNos);
        vaultTemplate.write("secret/data/model/" + modelId, data);
    }

    public List<String> retrieveSerialNos(String modelId) {
        VaultResponse response = vaultTemplate.read("secret/data/model/" + modelId);
        if (response != null && response.getData() != null) {
            Map<String, Object> data = (Map<String, Object>) response.getData().get("data");
            return (List<String>) data.get("serialNos");
        }
        throw new IllegalArgumentException("Serial numbers not found in Vault for modelId: " + modelId);
    }
}

package hr.fer.kdsuf.service.Impl;

import hr.fer.kdsuf.service.VaultSecretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.support.VaultResponseSupport;

import java.util.Collections;
import java.util.Map;

@Service
public class VaultSecretServiceImpl implements VaultSecretService {

    @Autowired
    private VaultOperations vaultOperations;
    @Override
    public void createSecretForDevice(String deviceId) {
        vaultOperations.write("secret/data/device/" + deviceId);

    }

    @Override
    public String getSecretForDevice(String deviceId) {
        VaultResponseSupport<Map> response = vaultOperations.read("secret/data/device/" + deviceId, Map.class);
        if (response != null && response.getData() != null) {
            return response.getData().get("data").toString();
        }
        return null;

    }
}

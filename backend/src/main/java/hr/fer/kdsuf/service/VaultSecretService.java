package hr.fer.kdsuf.service;

import java.util.List;

public interface VaultSecretService {
    void storeSerialNos(String modelId, List<String> serialNos);

    public List<String> retrieveSerialNos(String modelId);
}

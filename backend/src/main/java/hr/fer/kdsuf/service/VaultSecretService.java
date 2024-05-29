package hr.fer.kdsuf.service;

import java.util.List;

public interface VaultSecretService {
    void storeSerialNos(String modelId, List<String> serialNos);

    List<String> retrieveSerialNos(String modelId);

    String signData(String keyName, byte[] data);

    boolean verifyData(String keyName, byte[] data, String signature);

    void storeSignature(String packageId, String signature);

    String retrieveSignature(String packageId);
}

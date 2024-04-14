package hr.fer.kdsuf.service;

public interface VaultSecretService {
    void createSecretForDevice(String deviceId);

    String getSecretForDevice(String deviceId);
}

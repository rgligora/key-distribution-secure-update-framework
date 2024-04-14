package hr.fer.kdsuf.repository;

import hr.fer.kdsuf.model.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, String> {
}

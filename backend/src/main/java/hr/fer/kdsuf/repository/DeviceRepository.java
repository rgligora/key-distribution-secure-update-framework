package hr.fer.kdsuf.repository;

import hr.fer.kdsuf.model.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String>, QuerydslPredicateExecutor<Device> {
}

package hr.fer.kdsuf.repository;

import hr.fer.kdsuf.model.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String>, QuerydslPredicateExecutor<Device> {

    List<Device> findDevicesByCompanyCompanyId(String id);
}

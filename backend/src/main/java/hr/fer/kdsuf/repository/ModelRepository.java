package hr.fer.kdsuf.repository;

import hr.fer.kdsuf.model.domain.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model, String>, QuerydslPredicateExecutor<Model> {
    Model findModelByDeviceIdsContaining(String deviceId);

    List<Model> findModelsByCompanyCompanyId(String id);
}


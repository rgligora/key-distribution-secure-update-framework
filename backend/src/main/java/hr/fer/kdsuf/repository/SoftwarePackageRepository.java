package hr.fer.kdsuf.repository;

import hr.fer.kdsuf.model.domain.Model;
import hr.fer.kdsuf.model.domain.SoftwarePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoftwarePackageRepository extends JpaRepository<SoftwarePackage, String>, QuerydslPredicateExecutor<SoftwarePackage> {
    List<SoftwarePackage> findSoftwarePackageByCompanyCompanyId(String id);

    List<SoftwarePackage> findSoftwarePackageByModelsContaining(Model model);

    boolean existsByModelsContaining(Model model);
}

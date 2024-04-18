package hr.fer.kdsuf.repository;

import hr.fer.kdsuf.model.domain.Software;
import hr.fer.kdsuf.model.domain.SoftwarePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SoftwareRepository extends JpaRepository<Software, String>, QuerydslPredicateExecutor<SoftwarePackage> {
}

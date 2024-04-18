package hr.fer.kdsuf.repository;

import hr.fer.kdsuf.model.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String>, QuerydslPredicateExecutor<Company> {
}
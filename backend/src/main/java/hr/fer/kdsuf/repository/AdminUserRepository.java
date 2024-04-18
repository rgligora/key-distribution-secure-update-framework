package hr.fer.kdsuf.repository;

import hr.fer.kdsuf.model.domain.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, String>, QuerydslPredicateExecutor<AdminUser> {
}

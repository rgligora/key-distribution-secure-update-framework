package hr.fer.kdsuf.repository;

import hr.fer.kdsuf.model.domain.UpdateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdateHistoryRepository extends JpaRepository<UpdateHistory, String>, QuerydslPredicateExecutor<UpdateHistory> {
}

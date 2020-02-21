package app.repos;

import app.domain.ApplicationStatusHistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationStatusHistoryRepo extends JpaRepository<ApplicationStatusHistory, Long> {
}
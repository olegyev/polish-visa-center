package app.repos;

import app.domain.ClientVisa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VisaRepo extends JpaRepository<ClientVisa, Long>, JpaSpecificationExecutor<ClientVisa> {
}
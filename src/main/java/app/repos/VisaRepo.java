package app.repos;

import app.domain.Client;
import app.domain.ClientVisa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VisaRepo extends JpaRepository<ClientVisa, Long>, JpaSpecificationExecutor<ClientVisa> {

    ClientVisa findByIdAndClient(long id, Client client);

}
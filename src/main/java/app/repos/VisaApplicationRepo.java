package app.repos;

import app.domain.Client;
import app.domain.VisaApplication;
import app.domain.enums.City;
import app.domain.enums.VisaApplicationStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VisaApplicationRepo extends JpaRepository<VisaApplication, Long>, JpaSpecificationExecutor<VisaApplication> {

    @Query("FROM VisaApplication WHERE client = :client" +
            " AND appointmentDate IN (SELECT max(appointmentDate) FROM VisaApplication WHERE client = :client)")
    VisaApplication findLastVisaApplicationByClient(Client client);

    VisaApplication findByCityAndAppointmentDateAndAppointmentTime(City city, Date date, String time);

    VisaApplication findByIdAndClient(long id, Client client);

    Page<VisaApplication> findByCityAndVisaApplicationStatus(City city, VisaApplicationStatus status, Pageable pageable);

    List<VisaApplication> findByAppointmentDateAndVisaApplicationStatus(Date date, VisaApplicationStatus status);

}
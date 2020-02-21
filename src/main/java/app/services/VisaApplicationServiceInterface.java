package app.services;

import app.domain.ApplicationStatusHistory;
import app.domain.Client;
import app.domain.VisaApplication;
import app.domain.enums.City;
import app.domain.enums.VisaApplicationStatus;
import app.domain.enums.VisaType;
import app.dto.DisabledTimeAndDatesDto;
import app.dto.VisaApplicationDtoRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;

public interface VisaApplicationServiceInterface extends CrudServiceInterface<VisaApplication> {

    Page<VisaApplication> readAll(UserDetails userDetails, VisaType requiredVisaType, City appointmentCity,
                                  String appointmentDate, String appointmentTime, VisaApplicationStatus visaApplicationStatus,
                                  String lastName, String passportId, String email, String phoneNumber, Pageable pageable);

    VisaApplication readByClientIdAndApplicationId(UserDetails userDetails, long clientId, long applicationId);

    VisaApplication create(VisaApplicationDtoRequest visaApplication, Client loggedClient);

    VisaApplication update(VisaApplicationDtoRequest newVisaApplication, Client loggedClient);

    VisaApplication update(UserDetails userDetails, long clientId, long applicationId, VisaApplication newVisaApplication);

    void delete(Client loggedClient);

    void delete(UserDetails userDetails, long clientId, long applicationId);

    VisaApplication readLastVisaApplicationByClient(Client client);

    VisaApplication readByCityAndDateAndTime(City city, Date date, String time);

    Page<VisaApplication> readByCityAndStatus(City city, VisaApplicationStatus status, Pageable pageable);

    List<VisaApplication> readByAppointmentDateAndStatus(Date date, VisaApplicationStatus status);

    List<ApplicationStatusHistory> readVisaApplicationHistory(UserDetails userDetails, VisaApplication visaApplication);

    DisabledTimeAndDatesDto findTimeAndDatesToDisable(City city, int workDayBegin, int workDayEnd, int stepInMinutes);

}
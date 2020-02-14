package app.services;

import app.domain.Client;
import app.domain.VisaApplication;
import app.domain.enums.City;
import app.domain.enums.VisaApplicationStatus;
import app.dto.DisabledTimeAndDatesDto;
import app.dto.VisaApplicationDtoRequest;

import java.util.Date;
import java.util.List;

public interface VisaApplicationServiceInterface extends CrudServiceInterface<VisaApplication> {

    VisaApplication create(VisaApplicationDtoRequest visaApplication, Client loggedClient);

    VisaApplication update(VisaApplicationDtoRequest newVisaApplication, Client loggedClient);

    void delete(Client loggedClient);

    VisaApplication readLastVisaApplicationByClient(Client client);

    VisaApplication readByCityAndDateAndTime(City city, Date date, String time);

    List<VisaApplication> readByCityAndStatus(City city, VisaApplicationStatus status);

    DisabledTimeAndDatesDto findTimeAndDatesToDisable(City city, int workDayBegin, int workDayEnd, int stepInMinutes);

}
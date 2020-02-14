package app.services.impl;

import app.domain.Client;
import app.domain.VisaApplication;
import app.domain.enums.City;
import app.domain.enums.EmployeePosition;
import app.domain.enums.VisaApplicationStatus;
import app.dto.DisabledTimeAndDatesDto;
import app.dto.VisaApplicationDtoRequest;
import app.exceptions.BadRequestException;
import app.repos.VisaApplicationRepo;
import app.services.EmployeeServiceInterface;
import app.services.VisaApplicationServiceInterface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VisaApplicationServiceImpl implements VisaApplicationServiceInterface {

    private final static Logger log = LogManager.getLogger(VisaApplicationServiceImpl.class);

    private final VisaApplicationRepo visaApplicationRepo;
    private final EmployeeServiceInterface employeeService;

    @Autowired
    public VisaApplicationServiceImpl(final VisaApplicationRepo visaApplicationRepo,
                                      final EmployeeServiceInterface employeeService) {
        this.visaApplicationRepo = visaApplicationRepo;
        this.employeeService = employeeService;
    }

    @Override
    public VisaApplication create(VisaApplicationDtoRequest visaApplication, Client loggedClient) {
        VisaApplication lastVisaApplication = readLastVisaApplicationByClient(loggedClient);
        VisaApplicationStatus status = null;

        if (lastVisaApplication != null) {
            status = lastVisaApplication.getVisaApplicationStatus();
        }

        VisaApplication createdVisaApplication;

        if (lastVisaApplication == null
                || (status == VisaApplicationStatus.DID_NOT_COME
                || status == VisaApplicationStatus.DOCS_INCOMPLETE
                || status == VisaApplicationStatus.ISSUED)) {

            VisaApplication anotherVisaApplication = readByCityAndDateAndTime(
                    visaApplication.getCity(),
                    visaApplication.getAppointmentDate(),
                    visaApplication.getAppointmentTime()
            );

            if (!bodyIsOk(visaApplication)) {
                log.error("Attempt to add new visa application failed due to the incorrect form filling.");
                throw new BadRequestException("The form filled incorrectly.");
            } else if (anotherVisaApplication != null) {
                throw new BadRequestException("There are another appointment already booked in " + visaApplication.getCity().toString() +
                        " on " + new SimpleDateFormat("yyyy-MM-dd").format(visaApplication.getAppointmentDate()) +
                        " at " + visaApplication.getAppointmentTime() + ".");
            } else {
                createdVisaApplication = new VisaApplication();

                createdVisaApplication.setClient(loggedClient);
                createdVisaApplication.setRequiredVisaType(visaApplication.getRequiredVisaType());
                createdVisaApplication.setCity(visaApplication.getCity());
                createdVisaApplication.setAppointmentDate(visaApplication.getAppointmentDate());
                createdVisaApplication.setAppointmentTime(visaApplication.getAppointmentTime());
                createdVisaApplication.setVisaApplicationStatus(VisaApplicationStatus.BOOKED);

                createdVisaApplication = create(createdVisaApplication);

                log.info("Client with ID = {} created a visa application with ID = {}.",
                        loggedClient.getId(), createdVisaApplication.getId());
            }

        } else {
            throw new BadRequestException("You already have visa application in processing.");
        }

        return createdVisaApplication;
    }

    @Override
    public VisaApplication create(VisaApplication visaApplication) {
        return visaApplicationRepo.save(visaApplication);
    }

    @Override
    public Page<VisaApplication> readAll(Specification<VisaApplication> spec, Pageable pageable) {
        return null;
    }

    @Override
    public VisaApplication readById(long id) {
        return visaApplicationRepo.findById(id).orElse(null);
    }

    @Override
    public VisaApplication readByCityAndDateAndTime(City city, Date date, String time) {
        return visaApplicationRepo.findByCityAndAppointmentDateAndAppointmentTime(city, date, time);
    }

    @Override
    public VisaApplication update(VisaApplicationDtoRequest newVisaApplication, Client loggedClient) {
        VisaApplication lastVisaApplication = readLastVisaApplicationByClient(loggedClient);
        VisaApplicationStatus status = null;

        if (lastVisaApplication != null) {
            status = lastVisaApplication.getVisaApplicationStatus();
        }

        VisaApplication updatedVisaApplication;

        if (status == VisaApplicationStatus.BOOKED) {

            VisaApplication anotherVisaApplication = readByCityAndDateAndTime(
                    newVisaApplication.getCity(),
                    newVisaApplication.getAppointmentDate(),
                    newVisaApplication.getAppointmentTime()
            );

            if (!bodyIsOk(newVisaApplication)) {
                log.error("Attempt to add new visa application failed due to the incorrect form filling.");
                throw new BadRequestException("The form filled incorrectly.");
            } else if (anotherVisaApplication != null) {
                throw new BadRequestException("There are another appointment already booked in " + newVisaApplication.getCity().toString() +
                        " on " + new SimpleDateFormat("yyyy-MM-dd").format(newVisaApplication.getAppointmentDate()) +
                        " at " + newVisaApplication.getAppointmentTime() + ".");
            } else {
                BeanUtils.copyProperties(newVisaApplication, lastVisaApplication, "id", "visaApplicationStatus");
                updatedVisaApplication = update(lastVisaApplication.getId(), lastVisaApplication);

                log.info("Client with ID = {} updated a visa application with ID = {}.",
                        loggedClient.getId(), updatedVisaApplication.getId());
            }

        } else {
            throw new BadRequestException("You have no booked visa applications.");
        }

        return updatedVisaApplication;
    }

    @Override
    public VisaApplication update(long id, VisaApplication visaApplication) {
        return visaApplicationRepo.save(visaApplication);
    }

    @Override
    public void delete(Client loggedClient) {
        VisaApplication lastVisaApplication = readLastVisaApplicationByClient(loggedClient);
        VisaApplicationStatus status = null;

        if (lastVisaApplication != null) {
            status = lastVisaApplication.getVisaApplicationStatus();
        }

        if (status == VisaApplicationStatus.BOOKED) {
            log.info("Client with ID = {} deleted a visa application with ID = {}.",
                    loggedClient.getId(), lastVisaApplication.getId());

            delete(lastVisaApplication.getId());

        } else {
            throw new BadRequestException("You have no booked visa applications.");
        }
    }

    @Override
    public void delete(long id) {
        visaApplicationRepo.deleteById(id);
    }

    @Override
    public VisaApplication readLastVisaApplicationByClient(Client client) {
        return visaApplicationRepo.findLastVisaApplicationByClient(client);
    }

    @Override
    public List<VisaApplication> readByCityAndStatus(City city, VisaApplicationStatus status) {
        return visaApplicationRepo.readByCityAndVisaApplicationStatus(city, status);
    }

    @Override
    public DisabledTimeAndDatesDto findTimeAndDatesToDisable(City city, int workDayBegin, int workDayEnd, int stepInMinutes) {
        Map<String, String[]> disabledTime = findTimeToDisable(city);
        String[] disabledDates = findDatesToDisable(disabledTime, city, workDayBegin, workDayEnd, stepInMinutes);

        DisabledTimeAndDatesDto result = new DisabledTimeAndDatesDto();
        result.setCity(city.toString());
        result.setDisabledTimeByDate(disabledTime);
        result.setDisabledDates(disabledDates);

        return result;
    }

    private Map<String, String[]> findTimeToDisable(City city) {
        List<VisaApplication> bookedAppsInCity = readByCityAndStatus(city, VisaApplicationStatus.BOOKED);

        Set<Date> datesInDb = bookedAppsInCity.stream()
                .map(VisaApplication::getAppointmentDate)
                .collect(Collectors.toSet());

        List<String> timeToDisable = new ArrayList<>();

        Map<String, String[]> disabledTimeByCityAndDate = new HashMap<>();

        for (Date dateInDb : datesInDb) {
            List<String> timeInDb = findTimeByCityAndDate(bookedAppsInCity, dateInDb);

            if (employeeService.countAdminsByCityAndPosition(city, EmployeePosition.OPERATOR) == 1) {
                timeToDisable.addAll(timeInDb);
            } else {
                int timeCounter = 1;
                for (int i = 0; (i + 1) < timeInDb.size(); i++) {
                    if (timeInDb.get(i).equals(timeInDb.get(i + 1))) {
                        timeCounter++;
                    }

                    if (timeCounter == employeeService.countAdminsByCityAndPosition(city, EmployeePosition.OPERATOR)) {
                        timeToDisable.add(timeInDb.get(i));
                        timeCounter = 1;
                    }
                }
            }

            disabledTimeByCityAndDate.put(dateInDb.toString(), timeToDisable.toArray(new String[0]));

            timeToDisable.clear();
        }

        return disabledTimeByCityAndDate;
    }

    private String[] findDatesToDisable(Map<String, String[]> disabledTime, City city,
                                        int workDayBegin, int workDayEnd, int stepInMinutes) {
        long operatorsNum = employeeService.countAdminsByCityAndPosition(city, EmployeePosition.OPERATOR);
        long maxApplicationsNum = (workDayEnd - workDayBegin) * (60 / stepInMinutes) * operatorsNum;

        List<String> disabledDates = new ArrayList<>();

        for (Map.Entry<String, String[]> entry : disabledTime.entrySet()) {
            String date = entry.getKey();
            String[] time = entry.getValue();

            if (time.length == maxApplicationsNum) {
                disabledDates.add(date);
            }
        }

        return disabledDates.toArray(new String[0]);
    }

    private List<String> findTimeByCityAndDate(List<VisaApplication> bookedAppsInCity, Date date) {
        return bookedAppsInCity.stream()
                .filter(list -> list.getAppointmentDate() == date)
                .map(VisaApplication::getAppointmentTime)
                .collect(Collectors.toList());
    }

    private boolean bodyIsOk(VisaApplicationDtoRequest body) {
        return body.getRequiredVisaType() != null
                && body.getCity() != null
                && body.getAppointmentDate() != null
                && body.getAppointmentTime() != null;
    }

}
package app.services.impl;

import app.domain.ApplicationStatusHistory;
import app.domain.Client;
import app.domain.Employee;
import app.domain.VisaApplication;
import app.domain.enums.City;
import app.domain.enums.EmployeePosition;
import app.domain.enums.VisaApplicationStatus;
import app.domain.enums.VisaType;
import app.dto.DisabledTimeAndDatesDto;
import app.dto.VisaApplicationDtoRequest;
import app.exceptions.BadRequestException;
import app.exceptions.NotFoundException;
import app.repos.ApplicationStatusHistoryRepo;
import app.repos.VisaApplicationRepo;
import app.repos.specs.VisaApplicationJpaSpecification;
import app.services.ClientServiceInterface;
import app.services.EmployeeServiceInterface;
import app.services.VisaApplicationServiceInterface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VisaApplicationServiceImpl implements VisaApplicationServiceInterface {

    private final static Logger log = LogManager.getLogger(VisaApplicationServiceImpl.class);

    private final VisaApplicationRepo visaApplicationRepo;
    private final ApplicationStatusHistoryRepo historyRepo;
    private final EmployeeServiceInterface employeeService;
    private final ClientServiceInterface clientService;

    @Autowired
    public VisaApplicationServiceImpl(final VisaApplicationRepo visaApplicationRepo,
                                      final ApplicationStatusHistoryRepo historyRepo,
                                      final EmployeeServiceInterface employeeService,
                                      final ClientServiceInterface clientService) {
        this.visaApplicationRepo = visaApplicationRepo;
        this.historyRepo = historyRepo;
        this.employeeService = employeeService;
        this.clientService = clientService;
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
    public Page<VisaApplication> readAll(UserDetails userDetails, VisaType requiredVisaType, City appointmentCity, String appointmentDate,
                                         String appointmentTime, VisaApplicationStatus visaApplicationStatus, String lastName,
                                         String passportId, String email, String phoneNumber, Pageable pageable) {
        Employee loggedEmployee = employeeService.readByEmail(userDetails.getUsername());
        Page<VisaApplication> visaApplications;

        if (requiredVisaType == null && appointmentCity == null && appointmentDate == null && appointmentTime == null
                && visaApplicationStatus == null && lastName == null && passportId == null && email == null && phoneNumber == null) {

            visaApplications = readByCityAndStatus(loggedEmployee.getCity(), VisaApplicationStatus.BOOKED, pageable);

        } else {

            Specification<VisaApplication> spec = Specification
                    .where(requiredVisaType == null ? null : VisaApplicationJpaSpecification.requiredVisaTypeContains(requiredVisaType))
                    .and(appointmentCity == null ? null : VisaApplicationJpaSpecification.appointmentCityContains(appointmentCity))
                    .and(appointmentDate == null ? null : VisaApplicationJpaSpecification.appointmentDateContains(appointmentDate))
                    .and(appointmentTime == null ? null : VisaApplicationJpaSpecification.appointmentTimeContains(appointmentTime))
                    .and(visaApplicationStatus == null ? null : VisaApplicationJpaSpecification.visaApplicationStatusContains(visaApplicationStatus))
                    .and(lastName == null ? null : VisaApplicationJpaSpecification.lastNameContains(lastName.toUpperCase()))
                    .and(passportId == null ? null : VisaApplicationJpaSpecification.passportIdContains(passportId))
                    .and(email == null ? null : VisaApplicationJpaSpecification.emailContains(email))
                    .and(phoneNumber == null ? null : VisaApplicationJpaSpecification.phoneNumberContains(phoneNumber));

            visaApplications = readAll(spec, pageable);
        }

        if (visaApplications.isEmpty()) {
            NotFoundException exception = new NotFoundException("Cannot find visa applications.");
            log.error(exception);
            throw exception;
        } else {
            log.info("Found visa applications by employee with ID = {}.", loggedEmployee.getId());
        }

        return visaApplications;
    }

    @Override
    public Page<VisaApplication> readAll(Specification<VisaApplication> spec, Pageable pageable) {
        return visaApplicationRepo.findAll(spec, pageable);
    }

    @Override
    public VisaApplication readById(long id) {
        return visaApplicationRepo.findById(id).orElse(null);
    }

    @Override
    public VisaApplication readByClientIdAndApplicationId(UserDetails userDetails, long clientId, long applicationId) {
        Employee loggedEmployee = employeeService.readByEmail(userDetails.getUsername());
        VisaApplication visaApplication = visaApplicationRepo.findByIdAndClient(applicationId, clientService.readById(clientId));

        if (visaApplication == null) {
            NotFoundException exception = new NotFoundException("Cannot find visa application with ID = " + applicationId +
                    " for client with ID = " + clientId + ".");
            log.error(exception);
            throw exception;
        } else {
            log.info("Found visa application with ID = {} by employee with ID = {}.", applicationId, loggedEmployee.getId());
        }

        return visaApplication;
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
                log.error("Attempt to update visa application with ID = {} by client with ID = {} failed due to the incorrect form filling.",
                        lastVisaApplication.getId(),
                        loggedClient.getId());
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
    public VisaApplication update(UserDetails userDetails, long clientId, long applicationId, VisaApplication newVisaApplication) {
        Employee loggedOperator = employeeService.readByEmail(userDetails.getUsername());
        VisaApplication visaApplicationFromDb = readByClientIdAndApplicationId(userDetails, clientId, applicationId);
        VisaApplicationStatus status = visaApplicationFromDb.getVisaApplicationStatus();

        if (!bodyIsOk(newVisaApplication)) {
            log.error("Attempt to update visa application with ID = {} by operator with ID = {} failed due to the incorrect form filling.",
                    applicationId, loggedOperator.getId());
            throw new BadRequestException("The form filled incorrectly.");
        } else if (visaApplicationFromDb.getCity() != loggedOperator.getCity()) {
            log.error("Attempt to update visa application with ID = {} by operator with ID = {} failed due to the cities' mismatch.",
                    applicationId, loggedOperator.getId());
            throw new BadRequestException("You can update visa applications only in " + loggedOperator.getCity() + ".");
        }

        VisaApplication updatedVisaApplication;

        if (status == VisaApplicationStatus.BOOKED) {

            VisaApplication anotherVisaApplication = readByCityAndDateAndTime(
                    newVisaApplication.getCity(),
                    newVisaApplication.getAppointmentDate(),
                    newVisaApplication.getAppointmentTime()
            );

            if (anotherVisaApplication != null
                    && anotherVisaApplication.getId() != visaApplicationFromDb.getId()
                    && newVisaApplication.getVisaApplicationStatus() == VisaApplicationStatus.BOOKED) {
                throw new BadRequestException("There is another appointment already booked in " + newVisaApplication.getCity().toString() +
                        " on " + new SimpleDateFormat("yyyy-MM-dd").format(newVisaApplication.getAppointmentDate()) +
                        " at " + newVisaApplication.getAppointmentTime() + ".");
            } else if ((newVisaApplication.getVisaApplicationStatus() != VisaApplicationStatus.BOOKED
                    || isAppointmentDateInPast(visaApplicationFromDb))
                    && (newVisaApplication.getRequiredVisaType() != visaApplicationFromDb.getRequiredVisaType()
                    || newVisaApplication.getCity() != visaApplicationFromDb.getCity()
                    || !(new SimpleDateFormat("yyyy-MM-dd").format(newVisaApplication.getAppointmentDate())).equals(visaApplicationFromDb.getAppointmentDate().toString())
                    || !newVisaApplication.getAppointmentTime().equals(visaApplicationFromDb.getAppointmentTime()))) {
                throw new BadRequestException("You cannot change booked visa application's" +
                        " required visa type, appointment city, date or time if you are going to change its status" +
                        " or if it is in past but not yet automatically set to DID_NOT_COME status.");
            } else if (!isAppointmentDateInPast(visaApplicationFromDb) && newVisaApplication.getVisaApplicationStatus() != VisaApplicationStatus.BOOKED) {
                throw new BadRequestException("Status of the visa application with ID = " + applicationId +
                        " cannot be updated because its appointment date has not come yet.");
            }

            BeanUtils.copyProperties(newVisaApplication, visaApplicationFromDb, "id", "client", "applicationStatusHistory");

            if (newVisaApplication.getVisaApplicationStatus() != VisaApplicationStatus.BOOKED) {
                setStatusHistory(loggedOperator, newVisaApplication, visaApplicationFromDb);
            }

            updatedVisaApplication = update(visaApplicationFromDb.getId(), visaApplicationFromDb);

            log.info("Operator with ID = {} updated the visa application with ID = {}.",
                    loggedOperator.getId(), updatedVisaApplication.getId());

        } else if (status == VisaApplicationStatus.DOCS_RECEIVED
                || status == VisaApplicationStatus.PENDING
                || status == VisaApplicationStatus.CONFIRMED
                || status == VisaApplicationStatus.DENIED) {

            if (newVisaApplication.getVisaApplicationStatus() == VisaApplicationStatus.BOOKED
                    || newVisaApplication.getVisaApplicationStatus() == VisaApplicationStatus.DOCS_INCOMPLETE) {
                throw new BadRequestException("Status " + newVisaApplication.getVisaApplicationStatus()
                        + " cannot be set to the visa application with ID = "
                        + applicationId + " because its current status is " + status + " already.");
            }

            visaApplicationFromDb.setVisaApplicationStatus(newVisaApplication.getVisaApplicationStatus());
            setStatusHistory(loggedOperator, newVisaApplication, visaApplicationFromDb);
            updatedVisaApplication = update(visaApplicationFromDb.getId(), visaApplicationFromDb);

            log.info("Operator with ID = {} set status = {} to the visa application with ID = {}.",
                    loggedOperator.getId(), newVisaApplication.getVisaApplicationStatus().toString(), updatedVisaApplication.getId());

        } else if (status == VisaApplicationStatus.DOCS_INCOMPLETE) {

            if (isAppointmentDateInPast(visaApplicationFromDb)) {
                throw new BadRequestException("Visa application with ID = " + applicationId +
                        " cannot be updated due to the expiration of the appointment date considering its status " + status.toString() + ".");
            } else {
                visaApplicationFromDb.setVisaApplicationStatus(newVisaApplication.getVisaApplicationStatus());
                setStatusHistory(loggedOperator, newVisaApplication, visaApplicationFromDb);
                updatedVisaApplication = update(visaApplicationFromDb.getId(), visaApplicationFromDb);
            }

        } else { // DID_NOT_COME or ISSUED

            throw new BadRequestException("Visa application with ID = " + applicationId +
                    " cannot be updated due to its status " + status.toString() + ".");

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
            log.info("Client with ID = {} deleted the visa application with ID = {}.",
                    loggedClient.getId(), lastVisaApplication.getId());

            delete(lastVisaApplication.getId());

        } else {
            throw new BadRequestException("You have no booked visa applications.");
        }
    }

    @Override
    public void delete(UserDetails userDetails, long clientId, long applicationId) {
        Employee loggedOperator = employeeService.readByEmail(userDetails.getUsername());
        VisaApplication visaApplication = readByClientIdAndApplicationId(userDetails, clientId, applicationId);
        VisaApplicationStatus status = visaApplication.getVisaApplicationStatus();

        if (status == VisaApplicationStatus.BOOKED
                && !isAppointmentDateInPast(visaApplication)
                && visaApplication.getCity() == loggedOperator.getCity()) {
            log.info("Operator with ID = {} deleted the visa application with ID = {}.",
                    loggedOperator.getId(), visaApplication.getId());

            delete(visaApplication.getId());

        } else {
            throw new BadRequestException("You can delete visa application only if its appointment city is " + loggedOperator.getCity() +
                    " and if its status is BOOKED or if its appointment date and time are in future.");
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
    public VisaApplication readByCityAndDateAndTime(City city, Date date, String time) {
        return visaApplicationRepo.findByCityAndAppointmentDateAndAppointmentTime(city, date, time);
    }

    @Override
    public Page<VisaApplication> readByCityAndStatus(City city, VisaApplicationStatus status, Pageable pageable) {
        return visaApplicationRepo.findByCityAndVisaApplicationStatus(city, status, pageable);
    }

    @Override
    public List<VisaApplication> readByAppointmentDateAndStatus(Date date, VisaApplicationStatus status) {
        return visaApplicationRepo.findByAppointmentDateAndVisaApplicationStatus(date, status);
    }

    @Override
    public List<ApplicationStatusHistory> readVisaApplicationHistory(UserDetails userDetails, VisaApplication visaApplication) {
        Employee loggedEmployee = employeeService.readByEmail(userDetails.getUsername());
        log.info("Found status' changes history for visa application with ID = {} by employee with ID = {}.", visaApplication.getId(), loggedEmployee.getId());
        List<ApplicationStatusHistory> history = visaApplication.getApplicationStatusHistory();

        Collections.sort(history, new Comparator<ApplicationStatusHistory>() {
            public int compare(ApplicationStatusHistory o1, ApplicationStatusHistory o2) {
                return o1.getSettingDate().compareTo(o2.getSettingDate());
            }
        });

        return history;
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
        List<VisaApplication> bookedAppsInCity = readByCityAndStatus(city, VisaApplicationStatus.BOOKED, null).getContent();

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

    private boolean bodyIsOk(VisaApplication body) {
        return body.getRequiredVisaType() != null
                && body.getCity() != null
                && body.getAppointmentDate() != null
                && body.getAppointmentTime() != null
                && body.getVisaApplicationStatus() != null;
    }

    private boolean isAppointmentDateInPast(VisaApplication visaApplication) {
        boolean isAppointmentInPast = false;

        Date today = new Date();
        String concatAppointmentDateAndTime = visaApplication.getAppointmentDate() + " " + visaApplication.getAppointmentTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date appointmentDateAndTime = null;
        try {
            appointmentDateAndTime = format.parse(concatAppointmentDateAndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (appointmentDateAndTime != null) {
            long todayInMillis = today.getTime();
            long appointmentInMillis = appointmentDateAndTime.getTime();
            long difference = todayInMillis - appointmentInMillis;

            if (difference > 0) {
                isAppointmentInPast = true;
            }
        }

        return isAppointmentInPast;
    }

    private void setStatusHistory(Employee loggedOperator, VisaApplication newVisaApplication, VisaApplication visaApplicationFromDb) {
        ApplicationStatusHistory history = new ApplicationStatusHistory();
        history.setApplicationStatus(newVisaApplication.getVisaApplicationStatus());
        history.setSettingDate(new Date());
        history.setOperatorId(loggedOperator.getId());
        history.setApplication(visaApplicationFromDb);
        historyRepo.save(history);
    }

}
package app.services.impl;

import app.domain.VisaApplication;
import app.domain.enums.VisaApplicationStatus;
import app.services.VisaApplicationServiceInterface;
import app.services.VisitCheckerInterface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class VisitCheckerImpl extends Thread implements VisitCheckerInterface {

    private final static Logger log = LogManager.getLogger(ClientServiceImpl.class);

    private final VisaApplicationServiceInterface visaApplicationService;

    @Autowired
    public VisitCheckerImpl(final VisaApplicationServiceInterface visaApplicationService) {
        this.visaApplicationService = visaApplicationService;
    }

    @Value("${visit-checker.start-time}")
    private String timeToStart;

    @Override
    public void init() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
        SimpleDateFormat formatOnlyDay = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        Date dateToStart = format.parse(formatOnlyDay.format(now) + " at " + timeToStart);
        long difference = dateToStart.getTime() - now.getTime();

        if (difference < 0) {
            Date tomorrow = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tomorrow);
            calendar.add(Calendar.DATE, 1);
            tomorrow = calendar.getTime();
            dateToStart = format.parse(formatOnlyDay.format(tomorrow) + " at " + timeToStart);
            difference = dateToStart.getTime() - now.getTime();
        }

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this, TimeUnit.MILLISECONDS.toSeconds(difference), 24 * 60 * 60, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        List<VisaApplication> didNotComeVisaApplications = visaApplicationService.readByAppointmentDateAndStatus(new Date(), VisaApplicationStatus.BOOKED);

        didNotComeVisaApplications.forEach(u -> {
            u.setVisaApplicationStatus(VisaApplicationStatus.DID_NOT_COME);
            visaApplicationService.create(u);
            log.info("Status DID_NOT_COME set on {} for application with ID = {}.", new Date(), u.getId());
        });
    }

}
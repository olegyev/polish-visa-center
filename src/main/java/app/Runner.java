package app;

import app.services.VisitCheckerInterface;
import app.services.impl.VisaApplicationServiceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.ParseException;

@SpringBootApplication
public class Runner {

    private final static Logger log = LogManager.getLogger(VisaApplicationServiceImpl.class);

    private static VisitCheckerInterface visitCheckerImpl;

    @Autowired
    public Runner(VisitCheckerInterface visitCheckerImpl) {
        Runner.visitCheckerImpl = visitCheckerImpl;
    }

    public static void main(String[] args) {
        SpringApplication.run(Runner.class);

        try {
            visitCheckerImpl.init();
        } catch (ParseException e) {
            log.error(e.getStackTrace());
        }
    }

}
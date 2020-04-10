package app;

import app.services.VisitCheckerInterface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.ParseException;

@SpringBootApplication
public class Runner {

    private final static Logger log = LogManager.getLogger(Runner.class);

    private static VisitCheckerInterface visitChecker;

    @Autowired
    public Runner(VisitCheckerInterface visitChecker) {
        Runner.visitChecker = visitChecker;
    }

    public static void main(String[] args) {
        SpringApplication.run(Runner.class);

        try {
            visitChecker.init();
        } catch (ParseException e) {
            log.error(e.getStackTrace());
        }
    }

}
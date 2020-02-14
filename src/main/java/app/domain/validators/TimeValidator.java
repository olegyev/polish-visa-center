package app.domain.validators;

import app.domain.validators.annotations.TimeLimit;
import app.exceptions.BadRequestException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class TimeValidator implements ConstraintValidator<TimeLimit, String> {

    private final static Logger log = LogManager.getLogger(TimeValidator.class);

    String lower;
    String upper;
    int step;

    @Override
    public void initialize(TimeLimit constraintAnnotation) {
        lower = constraintAnnotation.lower();
        upper = constraintAnnotation.upper();
        step = constraintAnnotation.step();
    }

    @Override
    public boolean isValid(String time, ConstraintValidatorContext constraintValidatorcontext) {
        if (time == null || time.isEmpty()) {
            BadRequestException exception = new BadRequestException("Time is not entered.");
            log.error(exception);
            throw exception;
        }

        List<String> result = new ArrayList<>();
        String initialLower = lower;

        int lowerLimit = Integer.parseInt(lower.substring(0, 2));
        int upperLimit = Integer.parseInt(upper.substring(0, 2));

        double iterationsNum = (upperLimit - lowerLimit) * (60d / step);
        for (int i = 0; i <= (int) iterationsNum; i++) {
            if (Integer.parseInt(lower.substring(0, 2)) >= upperLimit) {
                break;
            }

            result.add(lower);
            lower = addStep(lower);
        }
        lower = initialLower;

        return result.contains(time);
    }

    private String addStep(String time) {
        String[] splitTime = time.split(":");
        int hours = Integer.parseInt(splitTime[0]);
        int minutes = Integer.parseInt(splitTime[1]);

        if (minutes + step >= 60) {
            hours = hours + 1;
            minutes = step - (60 - minutes);
        } else {
            minutes = minutes + step;
        }

        if (hours < 10 && minutes < 10) {
            return "0" + hours + ":0" + minutes;
        } else if (hours >= 10 && minutes < 10) {
            return hours + ":0" + minutes;
        } else if (hours < 10) {
            return "0" + hours + ":" + minutes;
        } else {
            return hours + ":" + minutes;
        }
    }

}
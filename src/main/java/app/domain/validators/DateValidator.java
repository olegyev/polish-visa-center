package app.domain.validators;

import app.domain.validators.annotations.DateLimit;
import app.exceptions.BadRequestException;

import lombok.SneakyThrows;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValidator implements ConstraintValidator<DateLimit, Date> {

    private final static Logger log = LogManager.getLogger(DateValidator.class);

    String lower;
    String upper;
    int range;

    @Override
    public void initialize(DateLimit constraintAnnotation) {
        lower = constraintAnnotation.lower();
        upper = constraintAnnotation.upper();
        range = constraintAnnotation.range();
    }

    @Override
    @SneakyThrows
    public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid;

        if (date == null) {
            BadRequestException exception = new BadRequestException("Date is not entered.");
            log.error(exception);
            throw exception;

        }

        if (!lower.isEmpty() && upper.isEmpty() && range == 0) {
            Date lowerLimit = new SimpleDateFormat("yyyy-MM-dd").parse(lower);
            isValid = date.after(lowerLimit);

        } else if (lower.isEmpty() && !upper.isEmpty() && range == 0) {
            Date upperLimit = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(upper).getTime()
                    + calcMillisFromDays(1));
            isValid = date.before(upperLimit);

        } else if (!lower.isEmpty() && !upper.isEmpty() && range == 0) {
            Date lowerLimit = new SimpleDateFormat("yyyy-MM-dd").parse(lower);
            Date upperLimit = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(upper).getTime()
                    + calcMillisFromDays(1));
            isValid = date.after(lowerLimit) && date.before(upperLimit);

        } else if (!lower.isEmpty() && upper.isEmpty() && range != 0) {
            Date lowerLimit = new SimpleDateFormat("yyyy-MM-dd").parse(lower);
            Date upperLimit = new Date(lowerLimit.getTime() + calcMillisFromDays(range + 1));
            isValid = date.after(lowerLimit) && date.before(upperLimit);

        } else if (lower.isEmpty() && !upper.isEmpty() && range != 0) {
            Date upperLimit = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(upper).getTime()
                    + calcMillisFromDays(1));
            Date lowerLimit = new Date(upperLimit.getTime() - calcMillisFromDays(range + 1));
            isValid = date.after(lowerLimit) && date.before(upperLimit);

        } else if (lower.isEmpty() && upper.isEmpty() && range != 0) {
            Date upperLimitFromToday = new Date(new Date().getTime() + calcMillisFromDays(range));
            isValid = !(date.before(new Date(new Date().getTime() - calcMillisFromDays(1))) || date.after(upperLimitFromToday));

        } else {
            return false;
        }

        return isValid;
    }

    private long calcMillisFromDays(Integer range) {
        return ((long) range * 24 * 60 * 60 * 1000);
    }

}
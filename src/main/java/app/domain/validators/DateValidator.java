package app.domain.validators;

import app.domain.validators.annotations.ValidDate;
import app.exceptions.BadRequestException;

import lombok.SneakyThrows;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValidator implements ConstraintValidator<ValidDate, Date> {

    private final static Logger log = LogManager.getLogger(DateValidator.class);

    String dateLimit;

    @Override
    public void initialize(ValidDate constraintAnnotation) {
        dateLimit = constraintAnnotation.limit();
    }

    @Override
    @SneakyThrows
    public boolean isValid(Date date, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid;

        if (date == null) {
            BadRequestException exception = new BadRequestException("Date is not entered.");
            log.error(exception);
            throw exception;
        } else {
            isValid = date.after(new SimpleDateFormat("yyyy-MM-dd").parse(dateLimit));
        }

        return isValid;
    }

}
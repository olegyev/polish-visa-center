package app.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ValidationException;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class CustomExceptionHandler {

    private final static Logger log = LogManager.getLogger(CustomExceptionHandler.class);

    private final MessageSource messageSource;

    @Autowired
    public CustomExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseBody
    @ExceptionHandler({
            BadRequestException.class,
            BadCredentialsException.class,
            NumberFormatException.class,
            InvalidFormatException.class,
            IllegalArgumentException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            ParseException.class,
            ValidationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String exceptionHandlerBadRequest(RuntimeException e) {
        log.error("Exception occurred: {}.", e.getClass());
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler({
            NotFoundException.class,
            UsernameNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String exceptionHandlerNotFound(RuntimeException e) {
        log.error("Exception occurred: {}.", e.getClass());
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler({HttpServerErrorException.InternalServerError.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandlerInternalServerError(RuntimeException e) {
        log.error("Exception occurred: {}.", e.getClass());
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ValidationError getValidationError(MethodArgumentNotValidException e) {
        log.error("Exception occurred: {}.", e.getClass());
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

    private ValidationError processFieldErrors(List<FieldError> fieldErrors) {
        ValidationError error = new ValidationError();
        for (FieldError fieldError : fieldErrors) {
            String message = resolveErrorMessage(fieldError);
            error.addFieldError(fieldError.getField(), message);
        }
        return error;
    }

    private String resolveErrorMessage(FieldError fieldError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(fieldError, currentLocale);
    }

}
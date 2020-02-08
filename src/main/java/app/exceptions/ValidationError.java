package app.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError {

    private final List<FieldErrorDto> fieldErrors = new ArrayList<>();

    public ValidationError() {
    }

    public void addFieldError(String field, String message) {
        FieldErrorDto error = new FieldErrorDto(field, message);
        fieldErrors.add(error);
    }

    public List<FieldErrorDto> getFieldErrors() {
        return fieldErrors;
    }

}
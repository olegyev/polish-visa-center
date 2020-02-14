package app.domain.validators.annotations;

import app.domain.validators.TimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = TimeValidator.class)
public @interface TimeLimit {

    String message() default "Invalid time.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String lower();

    String upper();

    /* In minutes. */
    int step();

}
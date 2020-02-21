package app.domain.validators.annotations;

import app.domain.validators.DateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = DateValidator.class)
public @interface DateLimit {

    String message() default "Invalid date.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /* Format as "yyyy-MM-dd". Including provided date. */
    String lower() default "";

    /* Format as "yyyy-MM-dd". Including provided date. */
    String upper() default "";

    /* In days. Including upper and lower limits, e.g.
     * lower = '1900-01-01' + range = 2 -> from 1900-01-01 to 1900-01-03 including;
     * upper = '1900-01-10' - range = 2 -> from 1900-01-08 to 1900-01-10 including;
     * and including today if is given without explicit lower and upper limits,
     * e.g. today is 1900-01-01 + range = 2 -> valid dates are 1900-01-01, 1900-01-02 and 1900-01-03.
     * !!! But in last case time can be selected before current time within today's date !!!
     * !!! Such possibility should be excluded on the front-end !!! */
    int range() default 0;

}
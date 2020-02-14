package app.dto;

import app.domain.enums.City;
import app.domain.enums.VisaType;
import app.domain.validators.annotations.DateLimit;
import app.domain.validators.annotations.TimeLimit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VisaApplicationDtoRequest extends AbstractDto {

    private VisaType requiredVisaType;

    private City city;

    @DateLimit(range = 180, message = "Appointment date should be selected in range of 180 days from today.")
    private Date appointmentDate;

    @Pattern(regexp = "^([01]\\d|2[0-3]):?([0-5]\\d)$", message = "Time should be given as HH:mm between 00:00 and 23:59.")
    @TimeLimit(lower = "09:00", upper = "17:00", step = 15, message = "Appointment time should be between 09:00 and 17:00 with step of 15 minutes.")
    private String appointmentTime;

}
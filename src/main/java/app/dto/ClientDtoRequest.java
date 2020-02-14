package app.dto;

import app.domain.enums.ClientOccupation;
import app.domain.validators.annotations.DateLimit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ClientDtoRequest extends AbstractDto {

    @NotBlank(message = "Field must be filled.")
    @Size(min = 1, max = 50, message = "Length must be between 1 and 50.")
    @Pattern(regexp = "^[A-Z -]+$", message = "Only uppercase latin letters, spaces and dashes are allowed.")
    private String firstName;

    @NotBlank(message = "Field must be filled.")
    @Size(min = 1, max = 50, message = "Length must be between 1 and 50.")
    @Pattern(regexp = "^[A-Z -]+$", message = "Only uppercase latin letters, spaces and dashes are allowed.")
    private String lastName;

    @NotBlank(message = "Field must be filled.")
    @Size(min = 3, max = 255, message = "Length must be between 3 and 255.")
    @Email(message = "Email should contain @.")
    private String email;

    @NotBlank(message = "Field must be filled.")
    @Size(min = 11, max = 50, message = "Length must be between 11 and 50.")
    @Pattern(regexp = "^[0-9]+$", message = "Phone number should contain code and number itself, e.g. 375291001010.")
    private String phoneNumber;

    @Past(message = "Date of birth should be in past.")
    @DateLimit(lower = "1900-01-01", message = "Date of birth should be between 1900-01-01 and today.")
    private Date dateOfBirth;

    @Size(min = 3, max = 255, message = "Length must be between 3 and 255.")
    private String passportId;

    private ClientOccupation occupation;

}
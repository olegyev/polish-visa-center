package app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@MappedSuperclass
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class User extends Entity {

    @NonNull
    @Column(name = "first_name")
    @NotBlank(message = "Field must be filled.")
    @Size(min = 1, max = 50, message = "Length must be between 1 and 50.")
    @Pattern(regexp = "^[A-Z -]+$", message = "Only uppercase latin letters, spaces and dashes are allowed.")
    protected String firstName;

    @NonNull
    @Column(name = "last_name")
    @NotBlank(message = "Field must be filled.")
    @Size(min = 1, max = 50, message = "Length must be between 1 and 50.")
    @Pattern(regexp = "^[A-Z -]+$", message = "Only uppercase latin letters, spaces and dashes are allowed.")
    protected String lastName;

    @NonNull
    @NotBlank(message = "Field must be filled.")
    @Size(min = 3, max = 255, message = "Length must be between 3 and 255.")
    @Email(message = "Email should contain @.")
    protected String email;

    @NonNull
    @NotBlank(message = "Field must be filled.")
    @Column(name = "phone_number")
    @Size(min = 11, max = 50, message = "Length must be between 11 and 50.")
    @Pattern(regexp = "^\\+[0-9]+$", message = "Phone number should match a sample: +375291001010.")
    protected String phoneNumber;

    @NonNull
    @NotBlank(message = "Field must be filled.")
    @Size(min = 8, max = 255, message = "Password should be between 8 and 255 characters.")
    protected String password;

}
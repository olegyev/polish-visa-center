package app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class User extends Entity {

    @NonNull
    @Column(name = "first_name")
    protected String firstName;

    @NonNull
    @Column(name = "last_name")
    protected String lastName;

    @NonNull
    protected String email;

    @NonNull
    @Column(name = "phone_number")
    protected String phoneNumber;

    @NonNull
    protected String password;

}
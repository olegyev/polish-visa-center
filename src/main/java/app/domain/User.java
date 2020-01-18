package app.domain;

import app.domain.views.UserViews;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;

@MappedSuperclass
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class User extends Entity {

    @NonNull
    @Column(name = "first_name")
    @JsonView(UserViews.AllExceptIdAndPassword.class)
    protected String firstName;

    @NonNull
    @Column(name = "last_name")
    @JsonView(UserViews.AllExceptIdAndPassword.class)
    protected String lastName;

    @NonNull
    @JsonView(UserViews.AllExceptIdAndPassword.class)
    protected String email;

    @NonNull
    @Column(name = "phone_number")
    @JsonView(UserViews.AllExceptIdAndPassword.class)
    protected String phoneNumber;

    @NonNull
    @JsonView(UserViews.IncludingPassword.class)
    protected String password;

}
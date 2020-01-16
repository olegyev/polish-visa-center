package app.first;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "admins")
public class Admin extends User {
    @Enumerated(EnumType.STRING)
    private AdminPosition position;

    @Enumerated(EnumType.STRING)
    private City city;

    public Admin(String firstName, String lastName, AdminPosition position, City city, String email, String phoneNumber, String password) {
        super(firstName, lastName, email, phoneNumber, password);
        this.position = position;
        this.city = city;
    }
}
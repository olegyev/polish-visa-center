package app.entities;

import app.entities.enums.AdminPosition;
import app.entities.enums.City;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;

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
package app.domain;

import app.domain.enums.AdminPosition;
import app.domain.enums.City;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "admins")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Admin extends User {

    @NonNull
    @Enumerated(EnumType.STRING)
    private AdminPosition position;

    @NonNull
    @Enumerated(EnumType.STRING)
    private City city;

}
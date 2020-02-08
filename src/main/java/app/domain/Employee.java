package app.domain;

import app.domain.enums.City;
import app.domain.enums.EmployeePosition;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
@Table(name = "employees")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Employee extends User {

    @NonNull
    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private EmployeePosition position;

    @NonNull
    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private City city;

}
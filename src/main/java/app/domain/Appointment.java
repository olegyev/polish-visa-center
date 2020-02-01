package app.domain;

import app.domain.enums.City;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Appointment extends app.domain.Entity {

    @NonNull
    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @NonNull
    @Enumerated(EnumType.STRING)
    private City city;

    @NonNull
    @Column(name = "appointment_date")
    @Temporal(TemporalType.DATE)
    private Date appointmentDate;

    @NonNull
    @Column(name = "appointment_time")
    private String appointmentTime;

}
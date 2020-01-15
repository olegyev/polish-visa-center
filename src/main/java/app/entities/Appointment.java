package app.entities;

import app.entities.enums.City;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "appointments")
public class Appointment extends app.entities.Entity {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @OneToOne/*(fetch = FetchType.LAZY)*/
    @JoinColumn(name = "client_id", referencedColumnName="id")
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
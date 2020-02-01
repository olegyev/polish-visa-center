package app.domain;

import app.domain.enums.ClientOccupation;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "clients")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Client extends User {

    @Getter
    @Setter
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private ClientOccupation occupation;

    @Getter
    @Setter
    @Column(name = "personal_data_process_agreement")
    private boolean personalDataProcAgreement;

    @Getter
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private Appointment appointment;

}
package app.entities;

import app.entities.enums.ClientOccupation;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;

import java.util.Date;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "clients")
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

    public Client(String firstName, String lastName, Date dateOfBirth, ClientOccupation occupation, String email,
                  String phoneNumber, String password, boolean personalDataProcAgreement) {
        super(firstName, lastName, email, phoneNumber, password);
        this.dateOfBirth = dateOfBirth;
        this.occupation = occupation;
        this.personalDataProcAgreement = personalDataProcAgreement;
    }
}
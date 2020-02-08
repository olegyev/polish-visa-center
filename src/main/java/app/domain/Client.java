package app.domain;

import app.domain.enums.ClientOccupation;
import app.domain.validators.annotations.ValidDate;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "clients")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Client extends User {

    @NonNull
    @Getter
    @Setter
    @Column(name = "date_of_birth", nullable = false)
    @Temporal(TemporalType.DATE)
    @Past(message = "Date of birth must be less than today.")
    @ValidDate(limit = "1900-01-01", message = "Date of birth should be between 1900-01-01 and yesterday.")
    private Date dateOfBirth;

    @NonNull
    @Getter
    @Setter
    @Column(name = "passport_id", unique = true, nullable = false)
    @Size(min = 3, max = 255, message = "Length must be between 3 and 255.")
    private String passportId;

    @NonNull
    @Getter
    @Setter
    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private ClientOccupation occupation;

    @Getter
    @Setter
    @Column(name = "personal_data_process_agreement", nullable = false)
    @AssertTrue(message = "Should be selected.")
    private boolean personalDataProcAgreement;

    @Getter
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VisaApplication> applications;

    @Getter
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClientVisa> visas;

}
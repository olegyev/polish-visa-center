package app.domain;

import app.domain.enums.City;
import app.domain.enums.VisaApplicationStatus;
import app.domain.enums.VisaType;
import app.domain.validators.annotations.DateLimit;
import app.domain.validators.annotations.TimeLimit;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.Pattern;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "visa_applications")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VisaApplication extends app.domain.Entity {

    @Column(name = "required_visa_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private VisaType requiredVisaType;

    @NonNull
    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private City city;

    @NonNull
    @Column(name = "appointment_date", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateLimit(range = 180, message = "Appointment date should be selected in range of 180 days from today.")
    private Date appointmentDate;

    @NonNull
    @Column(name = "appointment_time", nullable = false, length = 10)
    @Pattern(regexp = "^([01]\\d|2[0-3]):?([0-5]\\d)$", message = "Time should be given as HH:mm between 00:00 and 23:59.")
    @TimeLimit(lower = "09:00", upper = "17:00", step = 15, message = "Appointment time should be between 09:00 and 17:00 with step of 15 minutes.")
    private String appointmentTime;

    @NonNull
    @Column(name = "application_status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private VisaApplicationStatus visaApplicationStatus;

    @Getter
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicationStatusHistory> applicationStatusHistory;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private Client client;

}
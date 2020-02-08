package app.domain;

import app.domain.enums.ApplicationStatus;
import app.domain.enums.City;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "visa_applications")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VisaApplication extends app.domain.Entity {

    @NonNull
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private Client client;

    @Getter
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicationStatusHistory> applicationStatusHistories;

    @Column(name = "required_visa_type", nullable = false)
    private char requiredVisaType;

    @NonNull
    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private City city;

    @NonNull
    @Column(name = "appointment_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date appointmentDate;

    @NonNull
    @Column(name = "appointment_time", nullable = false, length = 10)
    private String appointmentTime;

    @NonNull
    @Column(name = "application_status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

}
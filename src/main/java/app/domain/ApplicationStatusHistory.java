package app.domain;

import app.domain.enums.VisaApplicationStatus;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "applications_status_history")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ApplicationStatusHistory extends app.domain.Entity {

    @NonNull
    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private VisaApplicationStatus applicationStatus;

    @NonNull
    @Column(name = "setting_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date settingDate;

    @Column(name = "operator_id", nullable = false)
    private long operatorId;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", referencedColumnName = "id", nullable = false)
    private VisaApplication application;

}
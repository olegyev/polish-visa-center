package app.domain;

import app.domain.enums.VisaType;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "client_visas")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ClientVisa extends app.domain.Entity {

    @NonNull
    @Column(name = "visa_num", nullable = false, length = 50)
    @Size(min = 3, max = 50, message = "Length must be between 3 and 50.")
    private String visaNumber;

    @Column(name = "visa_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private VisaType visaType;

    @NonNull
    @Column(name = "issue_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date issueDate;

    @NonNull
    @Column(name = "expiry_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date expiryDate;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private Client client;

}
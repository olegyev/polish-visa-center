package app.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "client_visas")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ClientVisa extends app.domain.Entity {

    @NonNull
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private Client client;

    @NonNull
    @Column(name = "visa_num", nullable = false, length = 50)
    private String visaNumber;

    @Column(name = "visa_type", nullable = false)
    private char visaType;

    @NonNull
    @Column(name = "issue_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date issueDate;

    @NonNull
    @Column(name = "expiry_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date expiryDate;

}
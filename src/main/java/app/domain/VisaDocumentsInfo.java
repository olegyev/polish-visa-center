package app.domain;

import app.domain.enums.ClientOccupation;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
@Table(name = "visa_documents")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VisaDocumentsInfo extends app.domain.Entity {

    @NonNull
    @Column(name = "client_occupation", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private ClientOccupation occupation;

    @Column(name = "visa_type", nullable = false)
    private char visaType;

    @NonNull
    @Column(name = "document_description", nullable = false)
    private String docDescription;

}
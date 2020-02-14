package app.domain;

import app.domain.enums.ClientOccupation;
import app.domain.enums.VisaType;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "visa_documents")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VisaDocumentsInfo extends app.domain.Entity {

    @NonNull
    @Column(name = "visa_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private VisaType visaType;

    @NonNull
    @Column(name = "client_occupation", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private ClientOccupation occupation;

    @NonNull
    @Column(name = "document_description", nullable = false)
    @Size(min = 1, max = 255, message = "Length must be between 1 and 255.")
    private String docDescription;

}
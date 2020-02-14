package app.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.hateoas.server.core.Relation;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "application", collectionRelation = "applications")
public class VisaApplicationDtoResponse extends AbstractDto {

    private String requiredVisaType;
    private String city;
    private String appointmentDate;
    private String appointmentTime;
    private String visaApplicationStatus;

}
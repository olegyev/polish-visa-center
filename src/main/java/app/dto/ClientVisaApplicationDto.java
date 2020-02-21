package app.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.hateoas.server.core.Relation;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "application", collectionRelation = "applications")
public class ClientVisaApplicationDto extends ClientDto {

    private VisaApplicationDto visaApplication;

}
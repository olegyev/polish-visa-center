package app.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.hateoas.server.core.Relation;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "visaDocumentsInfo")
public class VisaDocumentsInfoDto extends AbstractDto {

    private String visaType;
    private String occupation;
    private String docDescription;

}
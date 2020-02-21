package app.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "application", collectionRelation = "applications")
public class VisaApplicationWithDocInfoDto extends VisaApplicationDto {

    private List<String> requiredDocs;

}
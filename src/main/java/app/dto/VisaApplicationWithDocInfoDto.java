package app.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import lombok.ToString;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Relation(itemRelation = "application", collectionRelation = "applications")
public class VisaApplicationWithDocInfoDto extends VisaApplicationDto {

    private List<String> requiredDocs;

}
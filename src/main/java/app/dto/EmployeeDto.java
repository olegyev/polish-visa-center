package app.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.hateoas.server.core.Relation;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "employee", collectionRelation = "employees")
public class EmployeeDto extends AbstractDto {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String position;
    private String city;

}
package app.dto.assembler;

import app.controllers.DirectorController;
import app.domain.Admin;
import app.dto.EmployeeDto;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmployeeDtoAssembler extends RepresentationModelAssemblerSupport<Admin, EmployeeDto> {

    public EmployeeDtoAssembler() {
        super(DirectorController.class, EmployeeDto.class);
    }

    public EmployeeDto toModel(Admin employee) {
        EmployeeDto dto = createModelWithId(employee.getId(), employee);

        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setPosition(employee.getPosition().toString());
        dto.setCity(employee.getCity().toString());

        dto.add(linkTo(methodOn(DirectorController.class).getEmployees(null)).withSelfRel());
        return dto;
    }

    public CollectionModel<EmployeeDto> toCollectionModel(List<Admin> employees) {
        CollectionModel<EmployeeDto> dtos = super.toCollectionModel(employees);
        dtos.add(linkTo(methodOn(DirectorController.class).getEmployees(null)).withSelfRel());
        return dtos;
    }

}
package app.dto.assemblers.impl;

import app.controllers.EmployeeController;
import app.domain.Employee;
import app.dto.EmployeeDto;
import app.dto.assemblers.DtoAssemblerInterface;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EmployeeDtoAssembler
        extends RepresentationModelAssemblerSupport<Employee, EmployeeDto>
        implements DtoAssemblerInterface<Employee, EmployeeDto> {

    public EmployeeDtoAssembler() {
        super(EmployeeController.class, EmployeeDto.class);
    }

    @Override
    public EmployeeDto toModel(Employee employee) {
        EmployeeDto dto = createModelWithId(employee.getId(), employee);

        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setPosition(employee.getPosition().toString());
        dto.setCity(employee.getCity().toString());

        dto.add(linkTo(methodOn(EmployeeController.class).getEmployees(null, null, null)).withRel("employees"));

        return dto;
    }

    @Override
    public CollectionModel<EmployeeDto> toCollectionModel(List<Employee> employees) {
        CollectionModel<EmployeeDto> dtos = super.toCollectionModel(employees);
        dtos.add(linkTo(methodOn(EmployeeController.class).getEmployees(null, null, null)).withSelfRel());
        return dtos;
    }

}
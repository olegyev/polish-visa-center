package app.controllers;

import app.domain.Employee;
import app.domain.enums.City;
import app.domain.enums.EmployeePosition;
import app.dto.EmployeeDto;
import app.dto.assemblers.DtoAssemblerInterface;
import app.services.EmployeeServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import java.util.List;

/* ROLE_DIRECTOR operates all employees.
 * ROLE_MANAGER operates only operators from his/her city. */

@RestController
@RequestMapping("employees")
@RolesAllowed({"ROLE_DIRECTOR", "ROLE_MANAGER"})
public class EmployeeController {

    private final EmployeeServiceInterface employeeService;
    private final DtoAssemblerInterface<Employee, EmployeeDto> assembler;

    @Autowired
    public EmployeeController(final EmployeeServiceInterface employeeService,
                              final DtoAssemblerInterface<Employee, EmployeeDto> assembler) {
        this.employeeService = employeeService;
        this.assembler = assembler;
    }

    /* !!! Parameters 'city' and 'position' should be given in uppercase => exact search !!! */
    /* !!! Parameters 'size', 'page' and 'sort' are used only for JSON representation - by default (implicitly) they are available in Pageable interface !!! */
    @GetMapping
    @Transactional
    public ResponseEntity<PagedModel<EmployeeDto>> getEmployees(@RequestParam(required = false) City city,
                                                                @RequestParam(required = false) EmployeePosition position,
                                                                @RequestParam(required = false) String lastName,
                                                                @RequestParam(required = false) Integer page,
                                                                @RequestParam(required = false) Integer size,
                                                                @RequestParam(required = false) String sort,
                                                                @PageableDefault(sort = {"lastName"}, direction = Sort.Direction.ASC) Pageable defaultPageable,
                                                                PagedResourcesAssembler<Employee> pagedResourcesAssembler) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pageable pageable;
        if (size == null || page == null || sort == null) {
            pageable = defaultPageable;
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc(sort)));
        }

        List<Employee> employees = employeeService.readAll(userDetails, city, position, lastName, pageable);
        Page<Employee> employeesPage = new PageImpl<>(employees, pageable, employees.size());
        PagedModel<EmployeeDto> dtos = pagedResourcesAssembler.toModel(employeesPage, assembler);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("{id}")
    @Transactional
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable long id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Employee employee = employeeService.readById(id, userDetails);
        EmployeeDto dto = assembler.toModel(employee);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<EmployeeDto> addEmployee(@Valid @RequestBody Employee employee, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Employee createdEmployee = employeeService.create(employee, userDetails);
        EmployeeDto dto = assembler.toModel(createdEmployee);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable long id, @Valid @RequestBody Employee newEmployee,
                                                      Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Employee updatedEmployee = employeeService.update(id, newEmployee, userDetails);
        EmployeeDto dto = assembler.toModel(updatedEmployee);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable long id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        employeeService.delete(id, userDetails);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
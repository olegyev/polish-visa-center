package app.controllers;

import app.domain.Client;
import app.domain.enums.City;
import app.domain.enums.VisaApplicationStatus;
import app.domain.enums.VisaType;
import app.dto.ClientDtoRequest;
import app.dto.ClientDtoResponse;
import app.dto.assemblers.DtoAssemblerInterface;
import app.services.ClientServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

@RestController
@RequestMapping("clients")
@RolesAllowed("ROLE_OPERATOR")
public class ClientController {

    private final ClientServiceInterface clientService;
    private final DtoAssemblerInterface<Client, ClientDtoResponse> assembler;

    @Autowired
    public ClientController(final ClientServiceInterface clientService,
                            final DtoAssemblerInterface<Client, ClientDtoResponse> assembler) {
        this.clientService = clientService;
        this.assembler = assembler;
    }

    /* !!! Client's registration (create-functionality) is in app.security.controllers.RegisterLoginController !!! */

    /* !!! Parameters 'requiredVisaType', 'appointmentCity', 'appointmentDate', 'visaApplicationStatus',
     * 'visaType', 'issueDate' and 'expiryDate' should be given in uppercase => exact search !!! */
    /* !!! Parameters 'size', 'page' and 'sort' are used only for JSON representation - by default (implicitly) they are available in Pageable interface !!! */
    @GetMapping
    @Transactional
    public ResponseEntity<PagedModel<ClientDtoResponse>> getClients(@RequestParam(required = false) String lastName,
                                                                    @RequestParam(required = false) String passportId,
                                                                    @RequestParam(required = false) String email,
                                                                    @RequestParam(required = false) String phoneNumber,
                                                                    @RequestParam(required = false) VisaType requiredVisaType,
                                                                    @RequestParam(required = false) City appointmentCity,
                                                                    @RequestParam(required = false) String appointmentDate,
                                                                    @RequestParam(required = false) String appointmentTime,
                                                                    @RequestParam(required = false) VisaApplicationStatus visaApplicationStatus,
                                                                    @RequestParam(required = false) String visaNumber,
                                                                    @RequestParam(required = false) VisaType issuedVisaType,
                                                                    @RequestParam(required = false) String issueDate,
                                                                    @RequestParam(required = false) String expiryDate,
                                                                    @RequestParam(required = false) Integer page,
                                                                    @RequestParam(required = false) Integer size,
                                                                    @RequestParam(required = false) String sort,
                                                                    @PageableDefault(sort = {"lastName"}, direction = Sort.Direction.ASC) Pageable defaultPageable,
                                                                    PagedResourcesAssembler<Client> pagedResourcesAssembler) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pageable pageable;
        if (page == null || size == null || sort == null) {
            pageable = defaultPageable;
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc(sort)));
        }

        Page<Client> clients = clientService.readAll(
                userDetails, lastName, passportId, email, phoneNumber,
                requiredVisaType, appointmentCity, appointmentDate, appointmentTime, visaApplicationStatus,
                visaNumber, issuedVisaType, issueDate, expiryDate, pageable
        );
        PagedModel<ClientDtoResponse> dto = pagedResourcesAssembler.toModel(clients, assembler);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("{id}")
    @Transactional
    public ResponseEntity<ClientDtoResponse> getClientById(@PathVariable long id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Client client = clientService.readById(id, userDetails);
        ClientDtoResponse dto = assembler.toModel(client);
        return ResponseEntity.ok(dto);
    }

    /* !!! Update all fields except password and agreement !!! */
    @PutMapping("{id}")
    @Transactional
    public ResponseEntity<ClientDtoResponse> updateClient(@PathVariable long id, @Valid @RequestBody ClientDtoRequest newClient,
                                                          Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Client updatedClient = clientService.update(id, newClient, userDetails);
        ClientDtoResponse dto = assembler.toModel(updatedClient);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable long id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        clientService.delete(id, userDetails);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
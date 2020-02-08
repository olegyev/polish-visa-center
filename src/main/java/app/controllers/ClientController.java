package app.controllers;

import app.domain.Client;
import app.dto.ClientDtoRequest;
import app.dto.ClientDtoResponse;
import app.dto.assemblers.DtoAssemblerInterface;
import app.services.ClientServiceInterface;

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

    @GetMapping
    @Transactional
    public ResponseEntity<PagedModel<ClientDtoResponse>> getClients(@RequestParam(required = false) String lastName,
                                                                    @RequestParam(required = false) String passportId,
                                                                    @RequestParam(required = false) String email,
                                                                    @RequestParam(required = false) String phoneNumber,
                                                                    @RequestParam(required = false) Integer page,
                                                                    @RequestParam(required = false) Integer size,
                                                                    @RequestParam(required = false) String sort,
                                                                    @PageableDefault(sort = {"lastName"}, direction = Sort.Direction.ASC) Pageable defaultPageable,
                                                                    PagedResourcesAssembler<Client> pagedResourcesAssembler) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pageable pageable;
        if (size == null || page == null || sort == null) {
            pageable = defaultPageable;
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc(sort)));
        }

        List<Client> clients = clientService.readAll(userDetails, lastName, passportId, email, phoneNumber, pageable);
        Page<Client> clientsPage = new PageImpl<>(clients, pageable, clients.size());
        PagedModel<ClientDtoResponse> dtos = pagedResourcesAssembler.toModel(clientsPage, assembler);
        return ResponseEntity.ok(dtos);
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
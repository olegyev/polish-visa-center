package app.controllers;

import app.domain.Client;
import app.dto.ClientDtoRequest;
import app.dto.ClientDtoResponse;
import app.dto.assemblers.ClientProfileDtoAssemblerInterface;
import app.services.ClientServiceInterface;
import app.services.impl.ClientServiceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import java.security.Principal;

@RestController
@RolesAllowed("ROLE_CLIENT")
public class ClientPageController {

    private final static Logger log = LogManager.getLogger(ClientServiceImpl.class);

    private final ClientServiceInterface clientService;
    private final ClientProfileDtoAssemblerInterface<Client, ClientDtoResponse> assembler;

    @Autowired
    public ClientPageController(ClientServiceInterface clientService,
                                ClientProfileDtoAssemblerInterface<Client, ClientDtoResponse> assembler) {
        this.clientService = clientService;
        this.assembler = assembler;
    }

    @GetMapping("my-profile")
    @Transactional
    public ResponseEntity<ClientDtoResponse> getLoggedClient(Principal principal) {
        User loggedUserInfo = (User) ((Authentication) principal).getPrincipal();
        Client loggedClient = clientService.readByEmail(loggedUserInfo.getUsername());
        ClientDtoResponse dto = assembler.toModel(loggedClient);
        return ResponseEntity.ok(dto);
    }

    /* !!! Update all fields except password and agreement !!! */
    @PutMapping("my-profile")
    @Transactional
    public ResponseEntity<ClientDtoResponse> updateLoggedClient(@Valid @RequestBody ClientDtoRequest newClient, Principal principal) {
        User loggedUserInfo = (User) ((Authentication) principal).getPrincipal();
        Client loggedClient = clientService.readByEmail(loggedUserInfo.getUsername());
        Client updatedClient = clientService.update(loggedClient.getId(), newClient);
        ClientDtoResponse dto = assembler.toModel(updatedClient);

        log.info("Client with ID = {} updated him-/herself.", loggedClient.getId());

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

}
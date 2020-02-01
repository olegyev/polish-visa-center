package app.controllers;

import app.services.ClientServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
public class ClientController {

    private final ClientServiceInterface clientService;

    @Autowired
    public ClientController(final ClientServiceInterface clientService) {
        this.clientService = clientService;
    }

    @GetMapping("client")
    @RolesAllowed("ROLE_CLIENT")
    public String greeting(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        long clientId = clientService.readByEmail(userDetails.getUsername()).getId();
        return "Hello! You're on client's page! Your ID = " + clientId;
    }

}
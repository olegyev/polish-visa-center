package app.security.controllers;

import app.domain.Client;
import app.security.UserDetailsServiceImpl;
import app.security.models.LoginRequest;
import app.security.models.LoginResponse;
import app.security.utils.JwtUtil;
import app.services.ClientServiceInterface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import java.net.URI;

@RestController
public class RegisterLoginController {

    private final static Logger log = LogManager.getLogger(RegisterLoginController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final ClientServiceInterface clientService;

    @Autowired
    public RegisterLoginController(final AuthenticationManager authenticationManager,
                                   final JwtUtil jwtUtil,
                                   final UserDetailsServiceImpl userDetailsService,
                                   final ClientServiceInterface clientService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.clientService = clientService;
    }

    /* !!! Login is made right after the successful registration !!! */
    @PostMapping("registration")
    public ResponseEntity<HttpHeaders> register(@Valid @RequestBody Client client, LoginRequest loginRequest) {
        loginRequest.setUsername(client.getEmail());
        loginRequest.setPassword(client.getPassword());
        clientService.create(client);
        return new ResponseEntity<>(createToken(loginRequest), HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<HttpHeaders> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(createToken(loginRequest), HttpStatus.OK);
    }

    private HttpHeaders createToken(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String roles = userDetails.getAuthorities().toString();
        String path;

        if (roles.contains("ROLE_CLIENT")) {
            path = "/my-visa-application";
        } else {
            if (roles.contains("ROLE_DIRECTOR")) {
                path = "/employees";
            } else if (roles.contains("ROLE_MANAGER")) {
                path = "/employees";
            } else if (roles.contains("ROLE_OPERATOR")) {
                path = "/applications";
            } else {
                path = "/";
            }
        }

        String jwt = jwtUtil.generateToken(userDetails);
        LoginResponse loginResponse = new LoginResponse(jwt, path);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path(loginResponse.getRedirectPath()).build().toUri();

        log.info("Token created for user {} with role {}.", userDetails.getUsername(), userDetails.getAuthorities().toString());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, uri.toString());
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer_" + loginResponse.getJwt());

        return headers;
    }

}
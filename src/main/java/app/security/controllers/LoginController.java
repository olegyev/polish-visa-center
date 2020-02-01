package app.security.controllers;

import app.exceptions.BadRequestException;
import app.security.UserDetailsServiceImpl;
import app.security.models.LoginRequest;
import app.security.models.LoginResponse;
import app.security.utils.JwtUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("login")
public class LoginController {

    private final static Logger log = LogManager.getLogger(LoginController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public LoginController(final AuthenticationManager authenticationManager,
                           final JwtUtil jwtUtil,
                           final UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> createToken(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Incorrect username or password.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);
        String roles = userDetails.getAuthorities().toString();
        String path;

        if (roles.contains("ROLE_CLIENT")) {
            path = "/client";
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

        LoginResponse loginResponse = new LoginResponse(jwt);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path(path).build().toUri();

        log.info("Token created for user {} with role {}.", userDetails.getUsername(), userDetails.getAuthorities().toString());

        return ResponseEntity.ok().location(uri).body(loginResponse);
    }

}
package app.security.controllers;

import app.exceptions.BadRequestException;
import app.security.UserDetailsServiceImpl;
import app.security.models.LoginRequest;
import app.security.models.LoginResponse;
import app.security.utils.JwtUtil;

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

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtTokenUtil;
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, JwtUtil jwtTokenUtil, UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
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
        String jwt = jwtTokenUtil.generateToken(userDetails);
        String roles = userDetails.getAuthorities().toString();
        String path;

        if (roles.contains("ROLE_CLIENT")) {
            path = "/client";
        } else {
            if (roles.contains("ROLE_DIRECTOR")) {
                path = "/employees";
            } else if (roles.contains("ROLE_MANAGER")) {
                path = "/operators";
            } else if (roles.contains("ROLE_OPERATOR")) {
                path = "/applications";
            } else {
                path = "/";
            }
        }

        LoginResponse loginResponse = new LoginResponse(jwt);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path(path).build().toUri();
        return ResponseEntity.ok().location(uri).body(loginResponse);
    }

}
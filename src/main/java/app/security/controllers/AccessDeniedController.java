package app.security.controllers;

import app.security.utils.WebSecurityUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AccessDeniedController {

    @GetMapping(value = "/403")
    public ResponseEntity<String> accessDenied(Principal principal) {
        String userInfo = null;

        if (principal != null) {
            User loggedUser = (User) ((Authentication) principal).getPrincipal();
            userInfo = WebSecurityUtil.toString(loggedUser);
        }

        return new ResponseEntity<>("HTTP Status 403 - Access denied for user " + userInfo, HttpStatus.FORBIDDEN);
    }

}
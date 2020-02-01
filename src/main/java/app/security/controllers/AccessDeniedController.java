package app.security.controllers;

import app.security.utils.WebSecurityUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AccessDeniedController {

    private final static Logger log = LogManager.getLogger(AccessDeniedController.class);

    @GetMapping(value = "/403")
    public ResponseEntity<String> accessDenied(Principal principal) {
        String userInfo = null;

        if (principal != null) {
            User loggedUser = (User) ((Authentication) principal).getPrincipal();
            userInfo = WebSecurityUtil.toString(loggedUser);
            log.error("Access denied for {}.", userInfo);
        }

        return new ResponseEntity<>("HTTP Status 403 - Access denied for user " + userInfo, HttpStatus.FORBIDDEN);
    }

}
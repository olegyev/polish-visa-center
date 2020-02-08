package app.security.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;

@RestController
public class AccessDeniedController {

    private final static Logger log = LogManager.getLogger(AccessDeniedController.class);

    @GetMapping(value = "/403")
    public ResponseEntity<String> forGet(Principal principal) {
        return accessDenied(principal);
    }

    @PostMapping(value = "/403")
    public ResponseEntity<String> forPost(Principal principal) {
        return accessDenied(principal);
    }

    @PutMapping(value = "/403")
    public ResponseEntity<String> forPut(Principal principal) {
        return accessDenied(principal);
    }

    @DeleteMapping(value = "/403")
    public ResponseEntity<String> forDelete(Principal principal) {
        return accessDenied(principal);
    }

    private ResponseEntity<String> accessDenied(Principal principal) {
        String userInfo = null;

        if (principal != null) {
            User loggedUser = (User) ((Authentication) principal).getPrincipal();
            userInfo = toString(loggedUser);
            log.error("Access denied for {}.", userInfo);
        }

        return new ResponseEntity<>("HTTP Status 403 - Access denied for user " + userInfo, HttpStatus.FORBIDDEN);
    }

    private String toString(User user) {
        StringBuilder sb = new StringBuilder();

        sb.append(user.getUsername());

        Collection<GrantedAuthority> authorities = user.getAuthorities();
        if (authorities != null && !authorities.isEmpty()) {
            sb.append(" (");
            boolean first = true;
            for (GrantedAuthority a : authorities) {
                if (first) {
                    sb.append(a.getAuthority());
                    first = false;
                } else {
                    sb.append(", ").append(a.getAuthority());
                }
            }
            sb.append(")");
        }

        return sb.toString();
    }

}
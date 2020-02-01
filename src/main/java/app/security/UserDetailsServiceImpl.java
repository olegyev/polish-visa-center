package app.security;

import app.domain.Employee;
import app.services.ClientServiceInterface;
import app.services.EmployeeServiceInterface;
import app.services.impl.ClientServiceImpl;
import app.services.impl.EmployeeServiceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final static Logger log = LogManager.getLogger(UserDetailsServiceImpl.class);

    private final EmployeeServiceInterface employeeService;
    private final ClientServiceInterface clientService;

    @Autowired
    public UserDetailsServiceImpl(final EmployeeServiceImpl employeeService, final ClientServiceImpl clientService) {
        this.employeeService = employeeService;
        this.clientService = clientService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        app.domain.User user = null;
        List<String> roleNames = new ArrayList<>();

        try {
            user = employeeService.readByEmail(email);
            if (user != null) {
                roleNames.add(((Employee) user).getPosition().toString());
                log.info("Found employee by login: {}", user.getEmail());
            } else {
                user = clientService.readByEmail(email);
                if (user != null) {
                    roleNames.add("CLIENT");
                    log.info("Found client by login: {}", user.getEmail());
                }
            }
        } catch (NullPointerException ignored) {
        }

        if (user == null) {
            UsernameNotFoundException exception = new UsernameNotFoundException("User with username '" + email
                    + "' was not found in the database by login attempt.");
            log.error(exception);
            throw exception;
        }

        List<GrantedAuthority> grantList = new ArrayList<>();
        for (String role : roleNames) {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
            grantList.add(authority);
        }

        log.info("User {} was granted with roles {}.", user.getEmail(), grantList.toString());

        return new User(user.getEmail(), user.getPassword(), grantList);
    }

}
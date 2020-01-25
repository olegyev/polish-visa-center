package app.security;

import app.domain.Admin;
import app.services.impl.AdminServiceImpl;
import app.services.impl.ClientServiceImpl;

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

    private final AdminServiceImpl adminService;
    private final ClientServiceImpl clientService;

    @Autowired
    public UserDetailsServiceImpl(AdminServiceImpl adminService, ClientServiceImpl clientService) {
        this.adminService = adminService;
        this.clientService = clientService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        app.domain.User user = null;
        List<String> roleNames = new ArrayList<>();

        try {
            user = adminService.readByEmail(email);
            if (user != null) {
                roleNames.add(((Admin) user).getPosition().toString());
            } else {
                user = clientService.readByEmail(email);
                if (user != null) {
                    roleNames.add("CLIENT");
                }
            }
        } catch (NullPointerException ignored) {
        }

        if (user != null) {
            System.out.println("Found user by login: " + user); // log
        } else {
            System.out.println("User not found by login! " + email); // log
            throw new UsernameNotFoundException("User with e-mail '" + email + "' was not found in the database by login.");
        }

        List<GrantedAuthority> grantList = new ArrayList<>();
        for (String role : roleNames) {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
            grantList.add(authority);
        }

        return new User(user.getEmail(), user.getPassword(), grantList);
    }

}
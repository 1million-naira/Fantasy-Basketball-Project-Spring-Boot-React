package org.example.newtopsquadproject.Security;

import org.example.newtopsquadproject.Service.MyUserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final MyUserService myUserService;

    public CustomUserDetailsService(MyUserService myUserService) {
        this.myUserService = myUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = myUserService.findByEmail(username).orElseThrow();

        return UserPrincipal.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(user.getRole())))
                .build();
    }
}

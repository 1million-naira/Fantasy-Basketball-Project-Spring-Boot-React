package org.example.newtopsquadproject.Service;

import org.example.newtopsquadproject.Model.Login.LoginResponse;
import org.example.newtopsquadproject.Model.Login.RegistrationRequestDto;
import org.example.newtopsquadproject.Model.Login.RegistrationResponse;
import org.example.newtopsquadproject.Model.Players.MyUser;
import org.example.newtopsquadproject.Security.JwtIssuer;
import org.example.newtopsquadproject.Security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtIssuer jwtIssuer;

    private final AuthenticationManager authenticationManager;

    private final MyUserService myUserService;

    public AuthService(JwtIssuer jwtIssuer, AuthenticationManager authenticationManager, MyUserService myUserService) {
        this.jwtIssuer = jwtIssuer;
        this.authenticationManager = authenticationManager;
        this.myUserService = myUserService;
    }


    public RegistrationResponse registerUser(RegistrationRequestDto registrationRequestDto){
        myUserService.registerUser(registrationRequestDto);
        return new RegistrationResponse(registrationRequestDto.getUsername(), registrationRequestDto.getEmail());
    }


    public LoginResponse loginUser(String email, String password){
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        var principal = (UserPrincipal) authentication.getPrincipal();

        var roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        var token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);
        MyUser myUser = myUserService.findById(principal.getUserId());
        boolean admin = roles.contains("ROLE_ADMIN");

        return new LoginResponse(token, myUser.getId(), myUser.getUsername(), myUser.getBudget(), admin);
    }
}

package org.example.newtopsquadproject.Controller;

import org.example.newtopsquadproject.Model.Login.LoginRequest;
import org.example.newtopsquadproject.Model.Login.LoginResponse;
import org.example.newtopsquadproject.Model.Login.RegistrationRequestDto;
import org.example.newtopsquadproject.Model.Login.RegistrationResponse;
import org.example.newtopsquadproject.Model.Players.MyUser;
import org.example.newtopsquadproject.Security.JwtIssuer;
import org.example.newtopsquadproject.Security.UserPrincipal;
import org.example.newtopsquadproject.Service.AuthService;
import org.example.newtopsquadproject.Service.MyUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public RegistrationResponse registerUser(@RequestBody RegistrationRequestDto registrationRequestDto){
        return authService.registerUser(registrationRequestDto);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest loginRequest){
        return authService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
    }
}

package org.example.newtopsquadproject.Controller;

import org.example.newtopsquadproject.Security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/secured")
    public String secured(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return "If you see this, you are logged in as user: " + userPrincipal.getEmail() +
                " With Id: " + userPrincipal.getUserId();
    }
}

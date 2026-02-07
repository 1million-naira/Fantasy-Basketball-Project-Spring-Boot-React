package org.example.newtopsquadproject.Controller;

import org.example.newtopsquadproject.Model.DTO.UserDTO;
import org.example.newtopsquadproject.Model.Players.MyUser;
import org.example.newtopsquadproject.Security.UserPrincipal;
import org.example.newtopsquadproject.Service.MyUserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class MyUserRestController {
    private final MyUserService myUserService;



    public MyUserRestController(MyUserService myUserService){
        this.myUserService = myUserService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MyUser> getUser(@PathVariable("id") int id){
        MyUser myUser = myUserService.findById(id);
        return new ResponseEntity<>(myUser, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<UserDTO> getCurrentlyAuthenticatedUser(){
        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MyUser myUser = myUserService.findById(principal.getUserId());
        UserDTO userDTO = myUserService.userToDto(myUser);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }





}

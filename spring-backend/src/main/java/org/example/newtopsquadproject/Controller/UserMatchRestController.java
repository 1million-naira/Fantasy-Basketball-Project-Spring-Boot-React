package org.example.newtopsquadproject.Controller;

import org.example.newtopsquadproject.Model.DTO.UserMatchDTO;
import org.example.newtopsquadproject.Security.UserPrincipal;
import org.example.newtopsquadproject.Service.UserMatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/fantasy/matches")
public class UserMatchRestController {

    public UserMatchRestController(UserMatchService userMatchService) {
        this.userMatchService = userMatchService;
    }
    private final UserMatchService userMatchService;

    @GetMapping("/user")
    public ResponseEntity<List<UserMatchDTO>> getMatchesForCurrentlyAuthenticated(){
        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UserMatchDTO> userMatchDTOList = userMatchService.getAllMatchesForUser(principal.getUserId());
        return new ResponseEntity<>(userMatchDTOList, HttpStatus.OK);
    }

    @GetMapping("/user/league/{id}")
    public ResponseEntity<List<UserMatchDTO>> getMatchesInLeagueForCurrentlyAuthenticated(@PathVariable("id") int leagueId){
        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UserMatchDTO> userMatchDTOList = userMatchService.getAllMatchesForUserInLeague(principal.getUserId(), leagueId);
        return new ResponseEntity<>(userMatchDTOList, HttpStatus.OK);
    }
}

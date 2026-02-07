package org.example.newtopsquadproject.Controller;

import org.example.newtopsquadproject.Model.DTO.*;
import org.example.newtopsquadproject.Model.Enums.HeadToHeadLeagueType;
import org.example.newtopsquadproject.Model.FantasyLeagues.HeadToHeadUserLeague;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeague;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeagueStatus;
import org.example.newtopsquadproject.Model.Players.MyUser;
import org.example.newtopsquadproject.Security.UserPrincipal;
import org.example.newtopsquadproject.Service.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/fantasy/leagues")
public class FantasyLeagueRestController {

    private final HeadToHeadLeagueService headToHeadLeagueService;
    private final RotoLeagueService rotoLeagueService;

    private final UserLeagueService userLeagueService;

    private final UserMatchService userMatchService;

    private final MyUserService myUserService;


    public FantasyLeagueRestController(HeadToHeadLeagueService headToHeadLeagueService, RotoLeagueService rotoLeagueService, UserLeagueService userLeagueService, UserMatchService userMatchService, MyUserService myUserService) {
        this.headToHeadLeagueService = headToHeadLeagueService;
        this.rotoLeagueService = rotoLeagueService;
        this.userLeagueService = userLeagueService;
        this.userMatchService = userMatchService;
        this.myUserService = myUserService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FantasyLeagueDTO> getLeague(@PathVariable("id") int id){
        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserLeague userLeague = userLeagueService.findLeagueById(id);
        FantasyLeagueDTO fantasyLeagueDTO = userLeagueService.fantasyLeagueToDto(userLeague, principal.getUserId());
        return new ResponseEntity<>(fantasyLeagueDTO, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<FantasyLeagueDTO>> getLeaguesForCurrentlyAuthenticatedUser(){
        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<FantasyLeagueDTO> fantasyLeagueDTOList = userLeagueService.getAllLeaguesForUser(principal.getUserId());
        return new ResponseEntity<>(fantasyLeagueDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<UserLeagueStatusDTO>> getLeagueMembers(@PathVariable("id") int id){
        UserLeague userLeague = userLeagueService.findLeagueById(id);
        List<UserLeagueStatusDTO> leagueUsers = userLeagueService.getLeagueUsers(userLeague);
        return new ResponseEntity<>(leagueUsers, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<FantasyLeagueDTO>> getLeagueByCode(@RequestParam String code){
        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<FantasyLeagueDTO> fantasyLeagueDTOList = userLeagueService.findByCode(code, principal.getUserId());
        if(fantasyLeagueDTOList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(fantasyLeagueDTOList, HttpStatus.OK);
    }

    @GetMapping("/h2h/{id}/matches")
    public ResponseEntity<List<UserMatchDTO>> getMatchesInLeague(@PathVariable("id") int leagueId){
        List<UserMatchDTO> userMatchDTOList = userMatchService.getAllMatchesInLeague(leagueId);
        return new ResponseEntity<>(userMatchDTOList, HttpStatus.OK);
    }


    @GetMapping("/h2h/user/matches")
    public ResponseEntity<List<UserMatchDTO>> getMatchesForCurrentlyAuthenticated(){
        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UserMatchDTO> userMatchDTOList = userMatchService.getAllMatchesForUser(principal.getUserId());
        return new ResponseEntity<>(userMatchDTOList, HttpStatus.OK);
    }

    @GetMapping("/h2h/{id}/user/matches")
    public ResponseEntity<List<UserMatchDTO>> getMatchesForCurrentlyAuthenticatedInLeague(@PathVariable("id") int leagueId){
        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UserMatchDTO> userMatchDTOList = userMatchService.getAllMatchesForUserInLeague(principal.getUserId(), leagueId);
        return new ResponseEntity<>(userMatchDTOList, HttpStatus.OK);
    }


    @GetMapping("/h2h/user/{id}/matches")
    public ResponseEntity<List<UserMatchDTO>> getMatchesForUser(@PathVariable("id") int userId){
        List<UserMatchDTO> userMatchDTOList = userMatchService.getAllMatchesForUser(userId);
        return new ResponseEntity<>(userMatchDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<LeagueMessageDTO>> getMessagesInLeagueForCurrentlyAuthenticated(@PathVariable("id") int leagueId){
        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<LeagueMessageDTO> leagueMessageDTOList = userLeagueService.getAllMessagesInLeagueForUser(leagueId, principal.getUserId());
        return new ResponseEntity<>(leagueMessageDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}/topPlayers")
    public ResponseEntity<List<PlayerDTO>> getTopPlayersInLeague(@PathVariable("id") int leagueId){
        return new ResponseEntity<>(userLeagueService.getTopPlayersInLeague(leagueId), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<FantasyLeagueDTO> createLeague(@RequestBody FantasyLeagueDTO fantasyLeagueDTO){
        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MyUser myUser = myUserService.findById(principal.getUserId());
        UserLeague createdLeague = userLeagueService.createLeague(fantasyLeagueDTO, myUser);
        FantasyLeagueDTO createdLeagueDTO = userLeagueService.fantasyLeagueToDto(createdLeague, principal.getUserId());
        return new ResponseEntity<>(createdLeagueDTO, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/member")
    public ResponseEntity<UserLeagueStatusDTO> joinLeague(@PathVariable("id") int id){
        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserLeagueStatus userLeagueStatus = userLeagueService.joinLeague(principal.getUserId(), id);
        UserLeagueStatusDTO userLeagueStatusDTO = userLeagueService.userLeagueStatusToDto(userLeagueStatus);
        return new ResponseEntity<>(userLeagueStatusDTO, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/message")
    public ResponseEntity<LeagueMessageDTO> createLeagueMessageForCurrentlyAuthenticated(@PathVariable("id") int leagueId, @RequestBody Map<String, String> messageDto){
        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String message = messageDto.get("message");
        LeagueMessageDTO leagueMessageDTO = userLeagueService.createLeagueMessage(leagueId, principal.getUserId(), message);
        return new ResponseEntity<>(leagueMessageDTO, HttpStatus.CREATED);
    }


}

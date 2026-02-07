package org.example.newtopsquadproject.Controller;

import org.example.newtopsquadproject.Model.DTO.FantasyTeamDTO;
import org.example.newtopsquadproject.Model.FantasyLeagues.FantasyTeam;
import org.example.newtopsquadproject.Security.UserPrincipal;
import org.example.newtopsquadproject.Service.FantasyTeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/fantasy/team")
public class FantasyTeamRestController {

    private final FantasyTeamService fantasyTeamService;

    public FantasyTeamRestController(FantasyTeamService fantasyTeamService) {
        this.fantasyTeamService = fantasyTeamService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FantasyTeamDTO> getFantasyTeam(@PathVariable("id") int id){
        FantasyTeamDTO fantasyTeamDTO = fantasyTeamService.fantasyTeamToDto(id);
        return  new ResponseEntity<>(fantasyTeamDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<FantasyTeamDTO> getFantasyTeamForCurrentlyAuthenticated(){
        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int id = fantasyTeamService.findByUserId(principal.getUserId()).getId();

        FantasyTeamDTO fantasyTeamDTO = fantasyTeamService.fantasyTeamToDto(id);
        return new ResponseEntity<>(fantasyTeamDTO, HttpStatus.OK);
    }



    @PostMapping
    public ResponseEntity<?> createFantasyTeam(@RequestBody Map<String, Object> teamDto){
        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        FantasyTeam fantasyTeam = fantasyTeamService.createFantasyTeam(principal.getUserId(), (String) teamDto.get("name"));
        List<Integer> ids = (List<Integer>) teamDto.get("players");
        fantasyTeamService.addPlayersToTeam(fantasyTeam.getId(), ids);
        return new ResponseEntity<>("Team with id: " + fantasyTeam.getId() + " created", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateFantasyTeam(@RequestBody Map<String, Object> players){
        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FantasyTeam fantasyTeam = fantasyTeamService.findByUserId(principal.getUserId());
        List<Integer> ids = (List<Integer>) players.get("players");
        fantasyTeamService.updateTeam(fantasyTeam, ids);
        return new ResponseEntity<>("Team with id: " + fantasyTeam.getId() + " updated", HttpStatus.CREATED);
    }
    
    
    @PutMapping("/substitution")
    public ResponseEntity<?> substitutePlayerTeamStatuses(@RequestParam("starter") int starterId, @RequestParam("bench") int benchId){
        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FantasyTeam fantasyTeam = fantasyTeamService.findByUserId(principal.getUserId());

        if(starterId == benchId){
            return new ResponseEntity<>("A player can not be substituted with themselves.", HttpStatus.BAD_REQUEST);
        }

        fantasyTeamService.substitutePlayersOnTeam(fantasyTeam.getId(), starterId, benchId);
        return new ResponseEntity<>("Substitution successful for team with ID: " + fantasyTeam.getId(), HttpStatus.OK);
    }

    
    
    
    





}

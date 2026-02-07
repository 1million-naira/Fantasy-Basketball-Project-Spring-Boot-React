package org.example.newtopsquadproject.Controller;

import jakarta.persistence.metamodel.ListAttribute;
import org.example.newtopsquadproject.Model.DTO.PlayerDTO;
import org.example.newtopsquadproject.Model.Players.Player;
import org.example.newtopsquadproject.Model.ProLeagues.ProTeam;
import org.example.newtopsquadproject.Service.PlayerService;
import org.example.newtopsquadproject.Service.ProTeamService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/players")
public class PlayerRestController {

    private final PlayerService playerService;

    private final ProTeamService proTeamService;

    public PlayerRestController(PlayerService playerService, ProTeamService proTeamService){
        this.playerService = playerService;
        this.proTeamService = proTeamService;
    }
    @GetMapping
    public ResponseEntity<Page<PlayerDTO>> getPlayers(
            @RequestParam(name="page", defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "cba") String league,
            @RequestParam(defaultValue = "asc") String direction){
        Page<PlayerDTO> players = playerService.findAllByLeague(page, size, sortBy, direction, league);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable("id") int id){
        Player player = playerService.findById(id);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @GetMapping("/team/{id}")
    public ResponseEntity<List<Player>> getPlayersByTeam(@PathVariable("id") int teamId){
        ProTeam proTeam = proTeamService.findById(teamId);
        List<Player> playersByTeam = playerService.findByProTeam(proTeam);
        return new ResponseEntity<>(playersByTeam, HttpStatus.OK);
    }

    @GetMapping("/cba")
    public ResponseEntity<Page<Player>> getCbaPlayers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ){
        Page<Player> cbaPlayersPage = playerService.getCbaPlayers(page, size, sortBy, direction);
        return new ResponseEntity<>(cbaPlayersPage, HttpStatus.OK);
    }

}

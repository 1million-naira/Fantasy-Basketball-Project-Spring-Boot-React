package org.example.newtopsquadproject.Controller;

import org.example.newtopsquadproject.Model.DTO.PlayerDTO;
import org.example.newtopsquadproject.Model.FantasyLeagues.FantasyTeam;
import org.example.newtopsquadproject.Model.Players.Player;
import org.example.newtopsquadproject.Security.UserPrincipal;
import org.example.newtopsquadproject.Service.FantasyTeamService;
import org.example.newtopsquadproject.Service.PlayerService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/transfers")
public class TransferRestController {

    private final FantasyTeamService fantasyTeamService;
    private final PlayerService playerService;

    public TransferRestController(FantasyTeamService fantasyTeamService, PlayerService playerService) {
        this.fantasyTeamService = fantasyTeamService;
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<Page<PlayerDTO>> getAvailablePlayersForTransfer(
            @RequestParam(name="page", defaultValue = "0") int page,
            @RequestParam(name="position", defaultValue = "pg") String position,
            @RequestParam(name="league", defaultValue = "cba") String league,
            @RequestParam(name="search", required = false) String name,
            @RequestParam(name="sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name="direction", defaultValue = "asc") String direction
    ){

//        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        FantasyTeam fantasyTeam = fantasyTeamService.findByUserId(principal.getUserId());
        Page<PlayerDTO> playerPage = playerService.findAvailablePlayersForTransfer(page, position, league, name, sortBy, direction);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Total-Count", String.valueOf(playerPage.getTotalElements()));

        return new ResponseEntity<>(playerPage, headers, HttpStatus.OK);
    }
}

package org.example.newtopsquadproject.Service;

import org.apache.commons.collections4.CollectionUtils;
import org.example.newtopsquadproject.Exceptions.ResourceNotFoundException;
import org.example.newtopsquadproject.Exceptions.ValidationException;
import org.example.newtopsquadproject.Model.DTO.FantasyTeamDTO;
import org.example.newtopsquadproject.Model.DTO.PlayerDTO;
import org.example.newtopsquadproject.Model.Enums.StarterBench;
import org.example.newtopsquadproject.Model.FantasyLeagues.FantasyTeam;
import org.example.newtopsquadproject.Model.Players.MyUser;
import org.example.newtopsquadproject.Model.Players.Player;
import org.example.newtopsquadproject.Model.Enums.PlayerRoleEnum;
import org.example.newtopsquadproject.Model.Players.PlayerTeamStatus;
import org.example.newtopsquadproject.Repository.FantasyLeagues.FantasyTeamRepo;
import org.example.newtopsquadproject.Repository.MyUserRepo;
import org.example.newtopsquadproject.Repository.Players.PlayerRepo;
import org.example.newtopsquadproject.Repository.Players.PlayerTeamStatusRepo;
import org.example.newtopsquadproject.Repository.Settings.AppSettingsRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FantasyTeamService {
    private final FantasyTeamRepo fantasyTeamRepo;


    private final PlayerTeamStatusRepo playerTeamStatusRepo;

    private final MyUserRepo myUserRepo;

    private final PlayerRepo playerRepo;

    private final PlayerService playerService;

    private final AppSettingsService appSettingsService;


    public FantasyTeamService(FantasyTeamRepo fantasyTeamRepo, PlayerTeamStatusRepo playerTeamStatusRepo, MyUserRepo myUserRepo, PlayerRepo playerRepo, PlayerService playerService, AppSettingsRepo appSettingsRepo, AppSettingsService appSettingsService) {
        this.fantasyTeamRepo = fantasyTeamRepo;
        this.playerTeamStatusRepo = playerTeamStatusRepo;
        this.myUserRepo = myUserRepo;
        this.playerRepo = playerRepo;
        this.playerService = playerService;
        this.appSettingsService = appSettingsService;
    }

    public FantasyTeam findById(int id){
        return fantasyTeamRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Fantasy Team not found with ID: "+id));
    }

    public FantasyTeam findByUserId(int userId){
        return fantasyTeamRepo.findByMyUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Fantasy Team not found for user with ID: "+userId));
    }


    public FantasyTeam createFantasyTeam(int userId, String name){
        MyUser myUser = myUserRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: "+userId));

        if(fantasyTeamRepo.existsByMyUserId(userId)){
            throw new ValidationException("User: " + myUser.getUsername() + " has already created team. A user can only have one team");
        }

        FantasyTeam fantasyTeam = new FantasyTeam(myUser);
        fantasyTeam.setName(name);
        fantasyTeamRepo.save(fantasyTeam);

        myUser.setFantasyTeam(fantasyTeam);
        myUserRepo.save(myUser);

        return fantasyTeam;
    }

    public FantasyTeamDTO fantasyTeamToDto(int id){
        Optional<FantasyTeam> fantasyTeamOptional = fantasyTeamRepo.findByIdWithPlayerTeamStatuses(id);

        if(fantasyTeamOptional.isEmpty()){
            throw new ResourceNotFoundException("Fantasy Team not found with ID: " +id);
        }
        FantasyTeam fantasyTeam = fantasyTeamOptional.get();

        FantasyTeamDTO fantasyTeamDTO = new FantasyTeamDTO();
        fantasyTeamDTO.setId(fantasyTeam.getId());
        fantasyTeamDTO.setName(fantasyTeam.getName());
        fantasyTeamDTO.setTotalPoints(fantasyTeam.getFantasyPoints());

        List<PlayerDTO> players = new ArrayList<>();

        for(PlayerTeamStatus p : fantasyTeam.getPlayerTeamStatuses()){
            PlayerDTO playerDTO = playerService.playerToDto(p.getPlayer());
            if(p.getStarterBench() == StarterBench.STARTER){
                playerDTO.setBench(false);
            } else if(p.getStarterBench() == StarterBench.BENCH){
                playerDTO.setBench(true);
            }
            players.add(playerDTO);
        }

        fantasyTeamDTO.setPlayers(players);
        return fantasyTeamDTO;
    }

    public FantasyTeam updateTeam(FantasyTeam fantasyTeam, List<Integer> playerIds){

        int value = 0;
        MyUser myUser = fantasyTeam.getMyUser();
        if(playerIds.size() != 8){
            throw new ResourceNotFoundException("A team must have 8 players");
        }
        List<PlayerTeamStatus> existingPlayerTeamStatuses = playerTeamStatusRepo.findAllByFantasyTeamId(fantasyTeam.getId());

        List<Integer> existingIds = existingPlayerTeamStatuses.stream().map(p -> p.getPlayer().getId()).toList();

        if(!CollectionUtils.isEqualCollection(playerIds, existingIds) && !appSettingsService.getSettingState("transferMarketStatus").equalsIgnoreCase("open")){
            throw new ValidationException("The transfer market is closed, no transfers can be made");
        }

        playerTeamStatusRepo.deleteAll(existingPlayerTeamStatuses);

        List<Player> players = new ArrayList<>();

        for(int i : playerIds){
            Player player = playerRepo.findById(i).orElseThrow(() -> new ResourceNotFoundException("A player was not found in team creation"));
            players.add(player);
            value += player.getValue();
        }

        if(players.size() != playerIds.size()){
            throw new ResourceNotFoundException("Some players could not be found by ID");
        }

        if(value > 100){
            throw new ValidationException("Team Value must not exceed 100,000,000");
        }
        myUser.setBudget(100 - value);

        int count = players.size();
        List<PlayerTeamStatus> playerTeamStatuses = new ArrayList<>();

        for(Player p : players){
            PlayerTeamStatus playerTeamStatus = new PlayerTeamStatus(p, fantasyTeam);
            if(count>3){
                playerTeamStatus.setStarterBench(StarterBench.STARTER);
            } else{
                playerTeamStatus.setStarterBench(StarterBench.BENCH);
            }

            playerTeamStatuses.add(playerTeamStatus);
            count=count-1;
        }

        fantasyTeam.setPlayerTeamStatuses(playerTeamStatuses);
        playerTeamStatusRepo.saveAll(playerTeamStatuses);
        fantasyTeamRepo.save(fantasyTeam);
        myUserRepo.save(myUser);
        playerRepo.saveAll(players);
        return fantasyTeam;

    }

    public FantasyTeam addPlayersToTeam(int teamId, List<Integer> playerIds){
        if(playerIds.size() != 8){
            throw new ResourceNotFoundException("A team must have 8 players");
        }

        FantasyTeam fantasyTeam = fantasyTeamRepo.findById(teamId).orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: "+teamId));
        MyUser myUser = fantasyTeam.getMyUser();
        List<Player> players = new ArrayList<>();
        int value = 0;

        for(int i : playerIds){
            Player player = playerRepo.findById(i).orElseThrow(() -> new ResourceNotFoundException("A player was not found in team creation"));
            players.add(player);
            value += player.getValue();
        }


        if(players.size() != playerIds.size()){
            throw new ResourceNotFoundException("Some players could not be found by ID");
        }

        if(value > 100){
            throw new ValidationException("Team Value must not exceed 100,000,000");
        }
        myUser.setBudget(myUser.getBudget() - value);



        int count = players.size();
        List<PlayerTeamStatus> playerTeamStatuses = new ArrayList<>();

        for(Player p : players){
            PlayerTeamStatus playerTeamStatus = new PlayerTeamStatus(p, fantasyTeam);
            if(count>3){
                playerTeamStatus.setStarterBench(StarterBench.STARTER);
            } else{
                playerTeamStatus.setStarterBench(StarterBench.BENCH);
            }

            playerTeamStatuses.add(playerTeamStatus);
            count=count-1;
        }

        fantasyTeam.setPlayerTeamStatuses(playerTeamStatuses);
        playerTeamStatusRepo.saveAll(playerTeamStatuses);
        fantasyTeamRepo.save(fantasyTeam);
        myUserRepo.save(myUser);
        playerRepo.saveAll(players);
        return fantasyTeam;
    }

    public void substitutePlayersOnTeam(int teamId, int starterId, int benchId){
        FantasyTeam fantasyTeam = fantasyTeamRepo.findById(teamId).orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: "+teamId));
        Optional<PlayerTeamStatus> starterOptional = playerTeamStatusRepo.findByFantasyTeamIdAndPlayerId(teamId, starterId);
        Optional<PlayerTeamStatus> benchOptional = playerTeamStatusRepo.findByFantasyTeamIdAndPlayerId(teamId, benchId);

        if(starterOptional.isEmpty() || benchOptional.isEmpty()){
            throw new RuntimeException("One or both of the players in the substitution could not be found in team.");
        }

        PlayerTeamStatus starterToBench = starterOptional.get();
        PlayerTeamStatus benchToStarter = benchOptional.get();

        if(starterToBench.getStarterBench() == StarterBench.STARTER){
            starterToBench.setStarterBench(StarterBench.BENCH);
        }

        if(benchToStarter.getStarterBench() == StarterBench.BENCH){
            benchToStarter.setStarterBench(StarterBench.STARTER);
        }

        playerTeamStatusRepo.saveAll(List.of(starterToBench, benchToStarter));
        fantasyTeamRepo.save(fantasyTeam);
    }

    public void setCaptain(int teamId, int playerTeamStatusId){
        FantasyTeam fantasyTeam = fantasyTeamRepo.findById(teamId).orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: "+teamId));
        PlayerTeamStatus playerTeamStatus = playerTeamStatusRepo.findById(playerTeamStatusId).orElseThrow(
                () -> new ResourceNotFoundException("Player Team Status not found with ID: "+playerTeamStatusId));


        List<PlayerTeamStatus> playerTeamStatusesByTeam =  findByFantasyTeamId(teamId);
        Optional<PlayerTeamStatus> currentCaptainOptional = playerTeamStatusRepo.findByFantasyTeamIdAndPlayerRoleEnum(teamId, PlayerRoleEnum.CAPTAIN);

        if(currentCaptainOptional.isEmpty()){
            playerTeamStatus.setPlayerRoleEnum(PlayerRoleEnum.CAPTAIN);
        } else{
            currentCaptainOptional.get().setPlayerRoleEnum(PlayerRoleEnum.NORMAL);
            playerTeamStatus.setPlayerRoleEnum(PlayerRoleEnum.CAPTAIN);
        }

        playerTeamStatusRepo.save(playerTeamStatus);
        fantasyTeamRepo.save(fantasyTeam);
    }

    public List<PlayerTeamStatus> findByPlayerId(int id){
        return playerTeamStatusRepo.findAllByPlayerId(id);
    }

    public List<PlayerTeamStatus> findByFantasyTeamId(int id){
        return playerTeamStatusRepo.findAllByFantasyTeamId(id);
    }
}

package org.example.newtopsquadproject.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.newtopsquadproject.Model.Enums.ProLeagueEnum;
import org.example.newtopsquadproject.Model.Players.Player;
import org.example.newtopsquadproject.Model.Players.PlayerImage;
import org.example.newtopsquadproject.Model.Players.PlayerInfo;
import org.example.newtopsquadproject.Model.ProLeagues.ProTeam;
import org.example.newtopsquadproject.Service.PlayerImageService;
import org.example.newtopsquadproject.Service.PlayerInfoService;
import org.example.newtopsquadproject.Service.PlayerService;
import org.example.newtopsquadproject.Service.ProTeamService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Order(1)
public class DataLoader implements CommandLineRunner {
    private final PlayerService playerService;
    private final PlayerInfoService playerInfoService;
    private final ProTeamService proTeamService;

    private final PlayerImageService playerImageService;

    public DataLoader(PlayerService playerService, PlayerInfoService playerInfoService,
                      ProTeamService proTeamService, PlayerImageService playerImageService){
        this.playerService = playerService;
        this.playerInfoService = playerInfoService;
        this.proTeamService = proTeamService;
        this.playerImageService = playerImageService;
    }
    @Override
    public void run(String... args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<ProTeam>> cbaTeamsTypeReference = new TypeReference<List<ProTeam>>() {};
        try(InputStream inputStream = TypeReference.class.getResourceAsStream("/JSON/cbaTeams.json")){
            List<ProTeam> cbaTeams = objectMapper.readValue(inputStream, cbaTeamsTypeReference);
            proTeamService.saveAll(cbaTeams);
            System.out.println("CBA Teams saved");
        } catch(Exception e){
            System.out.println("Unable to save CBA teams: " + e.getMessage());
        }

        TypeReference<List<Player>> playerTypeReference = new TypeReference<List<Player>>(){};
        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/JSON/cbaPlayers.json")) {
            List<Player> players = objectMapper.readValue(inputStream, playerTypeReference);
            Map<String, ProTeam> proTeamsByName = proTeamService.findAll().stream().collect(Collectors.toMap(ProTeam::getClubName
                    , Function.identity()));

            for(Player p : players){
                ProTeam proTeam = proTeamsByName.get(p.getTeam_name());
                if(proTeam == null){
                    System.out.println(p.getName() + " Team not found: " + p.getTeam_name());
                    continue;
                }
                proTeam.getPlayerList().add(p);
                p.setProLeague(ProLeagueEnum.CBA);
                p.setProTeam(proTeam);
            }
            proTeamService.saveAll(new ArrayList<>(proTeamsByName.values()));
            playerService.saveAll(players);

            System.out.println("Players saved");
        } catch(Exception e){
            System.out.println("Unable to save players: " + e.getMessage());
        }


        TypeReference<List<PlayerInfo>> playerInfoTypeReference = new TypeReference<List<PlayerInfo>>() {};
        try(InputStream inputStream = TypeReference.class.getResourceAsStream("/JSON/cbaPlayerInfo.json")){
            List<PlayerInfo> playerInfoList = objectMapper.readValue(inputStream, playerInfoTypeReference);
            Map<String, Player> playersByName = playerService.findAll().stream().collect(Collectors.toMap(Player::getName, Function.identity()));
            for(PlayerInfo p : playerInfoList){
                try{
                    Player player = playersByName.get(p.getPlayer_name());
                    if(player == null){
                        System.out.println(p.getPlayer_name() + " was not found");
                        continue;
                    }

                    p.setPlayer(player);
                    player.setPlayerInfo(p);
                } catch(Exception e){
                    System.out.println(p.getPlayer_name() + ": " + e.getMessage());
                }

            }
            playerInfoService.saveAll(playerInfoList);
            playerService.saveAll(new ArrayList<>(playersByName.values()));
            System.out.println("Player Info saved");
        }catch(Exception e){
            System.out.println("Unable to save player info: " + e.getMessage());
        }


        TypeReference<List<PlayerImage>> playerImageTypeReference = new TypeReference<List<PlayerImage>>() {};
        try(InputStream inputStream = TypeReference.class.getResourceAsStream("/JSON/cbaPlayerImages.json")){
            List<PlayerImage> playerImageList = objectMapper.readValue(inputStream, playerImageTypeReference);
            Map<String, Player> playersByName = playerService.findAll().stream().collect(Collectors.toMap(Player::getName, Function.identity()));


            for(PlayerImage p : playerImageList){

                try{
                    Player player = playersByName.get(p.getPlayerName());
                    if(player == null){
                        System.out.println(p.getPlayerName() + " was not found");
                        continue;
                    }
                    p.setPlayer(player);
                    player.setPlayerImage(p);
                } catch(Exception e){
                    System.out.println(p.getPlayerName() + ": " + e.getMessage());
                }
            }
            playerImageService.saveAll(playerImageList);
            playerService.saveAll(playersByName.values().stream().toList());
            System.out.println("Player Images saved");


        } catch(Exception e){
            System.out.println("Unable to save player images: " + e.getMessage());
        }
    }
}

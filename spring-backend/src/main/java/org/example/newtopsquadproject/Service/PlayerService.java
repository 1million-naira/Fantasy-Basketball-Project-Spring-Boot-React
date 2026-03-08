package org.example.newtopsquadproject.Service;

import jakarta.persistence.criteria.Path;
import org.example.newtopsquadproject.Exceptions.ResourceNotFoundException;
import org.example.newtopsquadproject.Model.DTO.PlayerDTO;
import org.example.newtopsquadproject.Model.Enums.ProLeagueEnum;
import org.example.newtopsquadproject.Model.FantasyLeagues.FantasyTeam;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeagueStatus;
import org.example.newtopsquadproject.Model.Players.Player;
import org.example.newtopsquadproject.Model.Players.PlayerTeamStatus;
import org.example.newtopsquadproject.Model.ProLeagues.ProTeam;
import org.example.newtopsquadproject.Repository.Players.PlayerRepo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    private final PlayerRepo playerRepo;

    public PlayerService(PlayerRepo playerRepo){
        this.playerRepo = playerRepo;
    }
    public List<Player> findAll(){
        return playerRepo.findAll();
    }

    public void saveAll (List<Player> playerStats){
        playerRepo.saveAll(playerStats);
    }

    public Optional<Player> findByName(String name){
        return playerRepo.findByName(name);
    }

    public Player findById(int id){
        Optional<Player> playerOptional = playerRepo.findById(id);
        if(playerOptional.isEmpty()){
            throw new ResourceNotFoundException("Player not found with ID: " + id);
        }
        return playerOptional.get();
    }

    public void save(Player player){
        playerRepo.save(player);
    }
    
    
    public PlayerDTO playerToDto(Player player){
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(player.getId());

        String image = player.getPlayerImage() == null ? "https://www.eurobasket.com/photos/Not_Available.jpg" : player.getPlayerImage().getPlayerImage();
        playerDTO.setImage(image);


        playerDTO.setName(player.getName());
        String pos = player.getPlayerInfo() == null ? "N/A" : player.getPlayerInfo().getPosition();
        playerDTO.setPos(pos);
        playerDTO.setPoints(player.getFantasyPoints());
        playerDTO.setValue(player.getValue());

        return playerDTO;

    }

    public List<Integer> selectRandomPlayers(int n){
        Random rand = new Random();
        List<Integer> ids = new ArrayList<>();
        List<Player> players = playerRepo.findAll();
        List<Player> copy = new ArrayList<>(players);

        for(int i=0; i < n+1; i++){
            Player player = selectAndRemoveRandomPlayer(copy, rand);
            ids.add(player.getId());
        }

        return ids;
    }

    private Player selectAndRemoveRandomPlayer(List<Player> copy, Random rand){
        int randomIndex = rand.nextInt(copy.size());
        Player player = copy.get(randomIndex);

        Player temp = copy.get(copy.size()-1);
        copy.set(copy.size()-1, player);
        copy.set(randomIndex, temp);

        copy.remove(copy.size()-1);
        return player;
    }


    public List<Player> findByProTeam(ProTeam proTeam){
        return playerRepo.findByProTeam(proTeam);
    }



    public Page<Player> getCbaPlayers(int page, int size, String sortBy, String direction){

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return playerRepo.findByProLeague(pageRequest, ProLeagueEnum.CBA);
    }

    public Page<PlayerDTO> findAllByLeague(int page, int size, String sortBy, String direction, String league){
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                :Sort.by(sortBy).ascending();


        ProLeagueEnum proLeagueEnum = switch (league) {
            case "nba" -> ProLeagueEnum.NBA;
            case "wnba" -> ProLeagueEnum.WNBA;
            case "euro" -> ProLeagueEnum.EURO;
            default -> ProLeagueEnum.CBA;
        };

        Pageable pageable = PageRequest.of(page, size, sort);
        return playerRepo.findByProLeague(pageable, proLeagueEnum).map(this::playerToDto);
    }


    public Page<PlayerDTO> findAvailablePlayersForTransfer(int page, String position, String league, String name, String sortBy, String direction){
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                :Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, 10, sort);

        ProLeagueEnum proLeagueEnum = switch (league) {
            case "nba" -> ProLeagueEnum.NBA;
            case "wnba" -> ProLeagueEnum.WNBA;
            case "euro" -> ProLeagueEnum.EURO;
            default -> ProLeagueEnum.CBA;
        };

        if(!(name == null)){
            if(!name.isBlank()){
                Page<Player> searchedPlayerPage = playerRepo.findPlayersByNameIncludes(pageable, name, proLeagueEnum);
                return searchedPlayerPage.map(this::playerToDto);
            }
        }

//        List<Player> players = fantasyTeam.getPlayerTeamStatuses().stream().map(PlayerTeamStatus::getPlayer).collect(Collectors.toList());

        Page<Player> playerPage = playerRepo.findAvailablePlayersForTransfer(pageable, proLeagueEnum, position);
        return playerPage.map(this::playerToDto);
    }


}

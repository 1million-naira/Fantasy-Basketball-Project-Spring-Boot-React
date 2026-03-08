package org.example.newtopsquadproject.Service;

import org.example.newtopsquadproject.Exceptions.ResourceNotFoundException;
import org.example.newtopsquadproject.Model.Players.Player;
import org.example.newtopsquadproject.Model.ProLeagues.ProTeam;
import org.example.newtopsquadproject.Repository.ProLeagues.ProTeamRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProTeamService {

    private final ProTeamRepo proTeamRepo;

    public ProTeamService(ProTeamRepo proTeamRepo){
        this.proTeamRepo = proTeamRepo;
    }

    public ProTeam findById(int teamId){
        return proTeamRepo.findById(teamId).orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + teamId));
    }

    public void save(ProTeam proTeam){
        proTeamRepo.save(proTeam);
    }

    public void saveAll(List<ProTeam> proTeamList){
        proTeamRepo.saveAll(proTeamList);
    }

    public Optional<ProTeam> findByClubName(String club_name){
        return proTeamRepo.findByClubName(club_name);
    }

    public List<ProTeam> findAll(){
        return proTeamRepo.findAll();
    }

//    public Optional<ProTeam> findByClubNameWithPlayers(String club_name){
//        return proTeamRepo.findByClubNameAndFetchPlayersEagerly(club_name);
//    }
}

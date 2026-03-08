package org.example.newtopsquadproject.Service;

import org.example.newtopsquadproject.Model.ProLeagues.ProLeague;
import org.example.newtopsquadproject.Repository.ProLeagues.ProLeagueRepo;
import org.springframework.stereotype.Service;

@Service
public class ProLeagueService {
    private final ProLeagueRepo proLeagueRepo;

    public ProLeagueService(ProLeagueRepo proLeagueRepo){
        this.proLeagueRepo = proLeagueRepo;
    }
    public void save(ProLeague proLeague){
        proLeagueRepo.save(proLeague);
    }



}

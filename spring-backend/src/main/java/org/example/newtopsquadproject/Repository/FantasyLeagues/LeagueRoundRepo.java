package org.example.newtopsquadproject.Repository.FantasyLeagues;

import org.example.newtopsquadproject.Model.FantasyLeagues.LeagueRound;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LeagueRoundRepo extends CrudRepository<LeagueRound, Integer> {
    List<LeagueRound> findAllByUserLeagueId(int userLeagueId);
}

package org.example.newtopsquadproject.Repository.FantasyLeagues;

import org.example.newtopsquadproject.Model.FantasyLeagues.RotoUserLeague;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeague;
import org.example.newtopsquadproject.Repository.FantasyLeagues.UserLeagueRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RotoUserLeagueRepo extends UserLeagueRepo {
    Optional<RotoUserLeague> findById(int id);

    List<RotoUserLeague> findAllByCode(String code);
}

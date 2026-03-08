package org.example.newtopsquadproject.Repository.FantasyLeagues;

import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeague;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeagueStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserLeagueStatusRepo extends CrudRepository<UserLeagueStatus, Integer> {
    List<UserLeagueStatus> findAllByUserLeague(UserLeague userLeague);

    List<UserLeagueStatus> findAllByMyUserId(int id);

    Optional<UserLeagueStatus> findByUserLeagueIdAndMyUserId(int leagueId, int userId);

    Boolean existsByUserLeagueIdAndMyUserId(int leagueId, int userId);

    void deleteAllByMyUserId(int id);
}

package org.example.newtopsquadproject.Repository.FantasyLeagues;

import org.example.newtopsquadproject.Model.FantasyLeagues.HeadToHeadUserLeague;
import org.example.newtopsquadproject.Model.FantasyLeagues.LeagueRound;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserMatch;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface UserMatchRepo extends CrudRepository<UserMatch, Integer> {
    List<UserMatch> findAllByHeadToHeadUserLeagueId(int id);
    List<UserMatch> findAllByLeagueRound(LeagueRound leagueRound);

    @Query("SELECT u from UserMatch u WHERE u.ongoing = true AND (u.homeTeam.id = :teamId OR u.awayTeam.id = :teamId)")
    List<UserMatch> findAllOngoingContainingFantasyTeamId(@Param("teamId") int teamId);

    @Query("SELECT u from UserMatch u WHERE u.homeTeam.id = :teamId OR u.awayTeam.id = :teamId")
    List<UserMatch> findAllContainingFantasyTeamId(@Param("teamId") int teamId);

    @Query("SELECT u from UserMatch u WHERE u.homeUserLeagueStatus.id = :id OR u.awayUserLeagueStatus.id = :id")
    List<UserMatch> findAllByUserLeagueStatusId(@Param("id") int id);

    List<UserMatch> findAllByLeagueRoundUserLeagueId(int id);

    @Query("SELECT u from UserMatch u WHERE (u.homeUserLeagueStatus.id = :userLeagueStatusId OR u.awayUserLeagueStatus.id = :userLeagueStatusId) AND u.ongoing = true AND u.leagueRound.userLeague.id = :leagueId AND u.leagueRound.current = true")
    Optional<UserMatch> findCurrentMatchOfUserInLeague(@Param("leagueId") int leagueId, @Param("userLeagueStatusId") int userLeagueStatusId);


    @NonNull
    @Override
    List<UserMatch> findAll();
}

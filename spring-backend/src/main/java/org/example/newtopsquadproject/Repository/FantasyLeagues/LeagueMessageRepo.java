package org.example.newtopsquadproject.Repository.FantasyLeagues;

import org.example.newtopsquadproject.Model.FantasyLeagues.LeagueMessage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LeagueMessageRepo extends CrudRepository<LeagueMessage, Integer> {

    public List<LeagueMessage> findAllByUserLeagueStatusId(int userLeagueStatusId);
    public List<LeagueMessage> findAllByUserLeagueStatus_UserLeague_Id(int leagueId);

    public List<LeagueMessage> findAllByUserLeagueStatus_MyUser_Id(int userId);
}

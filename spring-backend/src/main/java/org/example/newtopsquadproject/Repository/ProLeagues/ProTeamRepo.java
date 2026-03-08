package org.example.newtopsquadproject.Repository.ProLeagues;

import org.example.newtopsquadproject.Model.ProLeagues.ProTeam;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ProTeamRepo extends CrudRepository<ProTeam, Integer> {

//    Optional<ProTeam> findByClubName(String club_name);

    Optional<ProTeam> findByClubNameAndIdIsNotNull(String clubName);

    @EntityGraph(
            value="ProTeam.withPlayers",
            type= EntityGraph.EntityGraphType.FETCH
    )
    Optional<ProTeam> findByClubName(String clubName);

    @NonNull
    @EntityGraph(
            value="ProTeam.withPlayers",
            type= EntityGraph.EntityGraphType.FETCH
    )
    List<ProTeam> findAll();
}

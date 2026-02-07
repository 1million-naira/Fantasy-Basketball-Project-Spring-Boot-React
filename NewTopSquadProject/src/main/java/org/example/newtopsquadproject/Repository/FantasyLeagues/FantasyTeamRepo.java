package org.example.newtopsquadproject.Repository.FantasyLeagues;

import org.example.newtopsquadproject.Model.FantasyLeagues.FantasyTeam;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface FantasyTeamRepo extends CrudRepository<FantasyTeam, Integer> {
    @NonNull
    List<FantasyTeam> findAll();

    @Query(value="SELECT DISTINCT ft from FantasyTeam ft LEFT JOIN FETCH ft.playerTeamStatuses")
    List<FantasyTeam> findAllWithPlayerTeamStatuses();

    @Query(value="SELECT ft FROM FantasyTeam ft LEFT JOIN FETCH ft.playerTeamStatuses WHERE ft.id = :id")
    Optional<FantasyTeam> findByIdWithPlayerTeamStatuses(@Param("id") int id);

    @Modifying
    @Query("DELETE FantasyTeam ft WHERE ft.myUser.id = :id")
    void deleteByUserIdWithJPQL(@Param("id") int id);

    Optional<FantasyTeam> findByMyUserId(int userId);

    Boolean existsByMyUserId(int id);

}

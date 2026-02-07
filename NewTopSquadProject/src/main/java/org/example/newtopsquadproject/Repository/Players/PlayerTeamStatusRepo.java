package org.example.newtopsquadproject.Repository.Players;

import org.example.newtopsquadproject.Model.Players.Player;
import org.example.newtopsquadproject.Model.Enums.PlayerRoleEnum;
import org.example.newtopsquadproject.Model.Players.PlayerTeamStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface PlayerTeamStatusRepo extends CrudRepository<PlayerTeamStatus, Integer> {

    @NonNull
    List<PlayerTeamStatus> findAll();

    List<PlayerTeamStatus> findAllByPlayerId(int id);

    List<PlayerTeamStatus> findAllByFantasyTeamId(int id);

    List<PlayerTeamStatus> findAllByPlayerIn(List<Player> players);

    Optional<PlayerTeamStatus> findByFantasyTeamIdAndPlayerRoleEnum(int teamId, PlayerRoleEnum playerRoleEnum);

    Optional<PlayerTeamStatus> findByFantasyTeamIdAndPlayerId(int teamId, int playerId);

    @Modifying
    @Query("DELETE PlayerTeamStatus p WHERE p.fantasyTeam.id = :id")
    void deleteByFantasyTeamIdWithJPQL(@Param("id") int id);
}

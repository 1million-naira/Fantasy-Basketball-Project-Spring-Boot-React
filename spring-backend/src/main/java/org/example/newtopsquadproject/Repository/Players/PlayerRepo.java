package org.example.newtopsquadproject.Repository.Players;

import org.example.newtopsquadproject.Model.Enums.ProLeagueEnum;
import org.example.newtopsquadproject.Model.FantasyLeagues.FantasyTeam;
import org.example.newtopsquadproject.Model.Players.Player;
import org.example.newtopsquadproject.Model.ProLeagues.ProTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface PlayerRepo extends CrudRepository<Player, Integer>, PagingAndSortingRepository<Player, Integer> {
    Optional<Player> findByName(String name);
    @NonNull
    List<Player> findAll();

    List<Player> findByIdIn(List<Integer> ids);

    List<Player> findByProTeam(ProTeam proTeam);

    @Query("SELECT p FROM Player p WHERE p.proLeague = :league")
    Page<Player> findByProLeague(Pageable pageable, @Param("league") ProLeagueEnum proLeagueEnum);

    @NonNull
    @Override
    Page<Player> findAll(@NonNull Pageable pageable);

    @Query("SELECT p FROM Player p WHERE p.proLeague = :league AND p.playerInfo IS NOT null AND lower(p.playerInfo.position) = lower(:position)")
    Page<Player> findAvailablePlayersForTransfer(Pageable pageable, @Param("league") ProLeagueEnum proLeagueEnum, @Param("position") String position);

    @Query("SELECT p FROM Player p WHERE lower(p.name) LIKE lower(CONCAT('%', :name, '%')) AND p.proLeague = :league")
    Page<Player> findPlayersByNameIncludes(Pageable pageable, @Param("name") String name, @Param("league") ProLeagueEnum proLeagueEnum);



}

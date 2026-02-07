package org.example.newtopsquadproject.Repository.FantasyLeagues;

import org.example.newtopsquadproject.Model.Enums.HeadToHeadLeagueType;
import org.example.newtopsquadproject.Model.FantasyLeagues.HeadToHeadUserLeague;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeague;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeadToHeadUserLeagueRepo extends UserLeagueRepo {
    Optional<HeadToHeadUserLeague> findById(int id);

    List<HeadToHeadUserLeague> findAllByCode(String code);

    @Query("SELECT league FROM HeadToHeadUserLeague league WHERE league.headToHeadLeagueType = :type")
    List<HeadToHeadUserLeague> findAllByHeadToHeadLeagueType(@Param("type") HeadToHeadLeagueType headToHeadLeagueType);

    @NonNull
    Page<UserLeague> findAll(@NonNull Pageable pageable);

    @Query("SELECT l from UserLeague l WHERE l.id = :id")
    Page<UserLeague> findById(@NonNull Pageable pageable, @Param("id") int searchId);

    @Modifying
    @Query("UPDATE UserLeague l SET l.createdBy = null WHERE l.createdBy.id = :id")
    void setNullToLeagueByMyUserId(@Param("id") int id);


    @Query("SELECT l from UserLeague l WHERE l.id = :id")
    Optional<UserLeague> findUserLeagueById(@Param("id") int id);

    @Query("SELECT u from UserLeague u")
    List<UserLeague> findAllUserLeagues();

    @Modifying
    @Query("DELETE UserLeague l WHERE l.id = :id")
    void deleteUserLeagueByIdWithJPQL(@Param("id") int id);

}


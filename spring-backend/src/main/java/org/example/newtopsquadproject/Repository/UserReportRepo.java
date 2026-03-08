package org.example.newtopsquadproject.Repository;

import org.example.newtopsquadproject.Model.Players.MyUser;
import org.example.newtopsquadproject.Model.UserReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;

public interface UserReportRepo extends CrudRepository<UserReport, Integer>,
        PagingAndSortingRepository<UserReport, Integer> {

    @NonNull
    @Override
    List<UserReport> findAll();

    List<UserReport> findAllByCreatedById(int id);
    List<UserReport> findAllByReportedId(int id);

    @Query("SELECT r FROM UserReport r WHERE r.reported.id = :id OR r.createdBy.id = :id")
    Page<UserReport> findAllByUserId(Pageable pageable, @Param("id") int userId);

    @Query("SELECT r FROM UserReport r WHERE r.resolved = :status")
    Page<UserReport> findAllByResolvedStatus(Pageable pageable, @Param("status") boolean status);


}

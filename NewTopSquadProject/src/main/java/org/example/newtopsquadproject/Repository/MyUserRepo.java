package org.example.newtopsquadproject.Repository;

import org.example.newtopsquadproject.Model.Players.MyUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface MyUserRepo extends CrudRepository<MyUser, Integer>,
        PagingAndSortingRepository<MyUser, Integer> {
    @NonNull
    @Override
    List<MyUser> findAll();

    Optional<MyUser> findByEmail(String email);

    Optional<MyUser> findByUsername(String username);

    @NonNull
    Page<MyUser> findAll(@NonNull Pageable pageable);

    @Query("SELECT u FROM MyUser u WHERE u.id = :id")
    Page<MyUser> findById(@NonNull Pageable pageable, @Param("id") int searchId);
}

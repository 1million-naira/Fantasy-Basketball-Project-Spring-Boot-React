package org.example.newtopsquadproject.Repository.Players;

import org.example.newtopsquadproject.Model.Players.PlayerImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface PlayerImageRepo extends CrudRepository<PlayerImage, Integer> {
    Optional<PlayerImage> findByPlayerId(int id);

    @NonNull
    List<PlayerImage> findAll();
 }

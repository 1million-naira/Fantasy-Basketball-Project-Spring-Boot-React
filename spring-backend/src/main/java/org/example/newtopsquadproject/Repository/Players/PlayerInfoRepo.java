package org.example.newtopsquadproject.Repository.Players;

import org.example.newtopsquadproject.Model.Players.PlayerInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface PlayerInfoRepo extends CrudRepository<PlayerInfo, Integer> {
    @NonNull
    List<PlayerInfo> findAll();
}

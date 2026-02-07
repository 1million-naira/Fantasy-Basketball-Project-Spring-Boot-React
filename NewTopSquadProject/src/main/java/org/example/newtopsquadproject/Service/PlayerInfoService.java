package org.example.newtopsquadproject.Service;

import org.example.newtopsquadproject.Model.Players.PlayerInfo;
import org.example.newtopsquadproject.Repository.Players.PlayerInfoRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerInfoService {
    private final PlayerInfoRepo playerInfoRepo;

    public PlayerInfoService(PlayerInfoRepo playerInfoRepo){
        this.playerInfoRepo = playerInfoRepo;
    }

    public void save(PlayerInfo playerInfo){
        playerInfoRepo.save(playerInfo);
    }

    public void saveAll(List<PlayerInfo> playerInfoList){
        playerInfoRepo.saveAll(playerInfoList);
    }

    public Iterable<PlayerInfo> findAll(){
        return playerInfoRepo.findAll();
    }

}

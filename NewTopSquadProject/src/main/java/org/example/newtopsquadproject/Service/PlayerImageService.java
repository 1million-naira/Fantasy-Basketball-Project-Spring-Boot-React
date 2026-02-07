package org.example.newtopsquadproject.Service;

import org.example.newtopsquadproject.Exceptions.ResourceNotFoundException;
import org.example.newtopsquadproject.Model.Players.PlayerImage;
import org.example.newtopsquadproject.Repository.Players.PlayerImageRepo;
import org.example.newtopsquadproject.Repository.Players.PlayerRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerImageService {

    private final PlayerImageRepo playerImageRepo;
    private final PlayerRepo playerRepo;

    public PlayerImageService(PlayerImageRepo playerImageRepo, PlayerRepo playerRepo) {
        this.playerImageRepo = playerImageRepo;
        this.playerRepo = playerRepo;
    }



    public PlayerImage findByPlayerId(int playerId){

        if(playerRepo.findById(playerId).isEmpty()){
            throw new ResourceNotFoundException("Player not found with ID: " + playerId);
        }

        Optional<PlayerImage> playerImageOptional = playerImageRepo.findByPlayerId(playerId);
        if(playerImageOptional.isEmpty()){
            throw new ResourceNotFoundException("Image not found for player with ID: " + playerId);
        }
        return playerImageOptional.get();
    }

    public void saveAll(List<PlayerImage> playerImageList){
        playerImageRepo.saveAll(playerImageList);
    }
}

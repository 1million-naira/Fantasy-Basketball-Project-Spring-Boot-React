package org.example.newtopsquadproject.Model.Players;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
public class PlayerImage {
    @Id
    @GeneratedValue
    private int id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @JsonProperty("player_name")
    private String playerName;


    @JsonProperty("player_img")
    private String playerImage;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerImage() {
        return playerImage;
    }

    public void setPlayerImage(String playerImage) {
        this.playerImage = playerImage;
    }
}

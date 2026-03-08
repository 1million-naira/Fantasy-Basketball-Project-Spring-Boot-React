package org.example.newtopsquadproject.Model.Players;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.example.newtopsquadproject.Model.ProLeagues.PlayerBoxScore;

import java.util.List;

@Entity
public class PlayerInfo {
    @Id
    @GeneratedValue
    private int id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name="player_id")
    Player player;

    private String player_name;
    private String league;
    private String nationality;

    private String age;

    private String height;

    private String position;


    public Player getPlayer() {
        return player;
    }

    @JsonIgnore
    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}

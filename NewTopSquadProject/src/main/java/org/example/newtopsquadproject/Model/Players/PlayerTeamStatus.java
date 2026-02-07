package org.example.newtopsquadproject.Model.Players;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.example.newtopsquadproject.Model.Enums.PlayerRoleEnum;
import org.example.newtopsquadproject.Model.Enums.StarterBench;
import org.example.newtopsquadproject.Model.FantasyLeagues.FantasyTeam;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class PlayerTeamStatus {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @Cascade(CascadeType.DETACH)
    @JoinColumn(name="player_id")
    private Player player;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="team_id")
    private FantasyTeam fantasyTeam;

    @Enumerated(EnumType.STRING)
    private PlayerRoleEnum playerRoleEnum;

    @Enumerated(EnumType.STRING)
    private StarterBench starterBench;

    private boolean captain;


    public PlayerTeamStatus() {}
    public PlayerTeamStatus(Player player, FantasyTeam fantasyTeam) {
        this.player = player;
        this.fantasyTeam = fantasyTeam;
    }

    public int getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isCaptain() {
        return captain;
    }

    public void setCaptain(boolean captain) {
        this.captain = captain;
    }

    public FantasyTeam getFantasyTeam() {
        return fantasyTeam;
    }

    public void setFantasyTeam(FantasyTeam fantasyTeam) {
        this.fantasyTeam = fantasyTeam;
    }

    public PlayerRoleEnum getPlayerRoleEnum() {
        return playerRoleEnum;
    }

    public void setPlayerRoleEnum(PlayerRoleEnum playerRoleEnum) {
        this.playerRoleEnum = playerRoleEnum;
    }

    public StarterBench getStarterBench() {
        return starterBench;
    }

    public void setStarterBench(StarterBench starterBench) {
        this.starterBench = starterBench;
    }
}

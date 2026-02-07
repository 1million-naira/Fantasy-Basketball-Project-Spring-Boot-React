package org.example.newtopsquadproject.Model.FantasyLeagues;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.example.newtopsquadproject.Model.Players.MyUser;
import org.example.newtopsquadproject.Model.Players.PlayerTeamStatus;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
public class FantasyTeam {

    public FantasyTeam(){}

    public FantasyTeam(MyUser myUser) {
        this.myUser = myUser;
    }

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    @JoinColumn(name="user_id")
    private MyUser myUser;

    private String name;
    private int fantasyPoints;

    @OneToMany(mappedBy = "fantasyTeam", fetch = FetchType.LAZY)
    private List<PlayerTeamStatus> playerTeamStatuses;


    public int getId() {
        return id;
    }

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public List<PlayerTeamStatus> getPlayerTeamStatuses() {
        return playerTeamStatuses;
    }

    public void setPlayerTeamStatuses(List<PlayerTeamStatus> playerTeamStatuses) {
        this.playerTeamStatuses = playerTeamStatuses;
    }

    public void addPlayerTeamStatus(PlayerTeamStatus playerTeamStatus){
        this.playerTeamStatuses.add(playerTeamStatus);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFantasyPoints() {
        return fantasyPoints;
    }

    public void addFantasyPoints(int fantasyPoints) {
        this.fantasyPoints += fantasyPoints;
    }
}

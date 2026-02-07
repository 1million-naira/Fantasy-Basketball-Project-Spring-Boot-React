package org.example.newtopsquadproject.Model.FantasyLeagues;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.example.newtopsquadproject.Model.Players.MyUser;

import java.util.List;

@Entity
public class UserLeagueStatus {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private MyUser myUser;

    @ManyToOne
    @JoinColumn(name="league_id")
    private UserLeague userLeague;

//    @JsonIgnore
//    @OneToMany(fetch = FetchType.EAGER)
//    private List<UserMatch> schedule;

    private int wins;
    private int losses;
    private int draws;

    public UserLeagueStatus() {}

    public UserLeagueStatus(MyUser myUser, UserLeague userLeague) {
        this.myUser = myUser;
        this.userLeague = userLeague;
    }

    private int points;

    private int gamesPlayed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public UserLeague getLeague() {
        return userLeague;
    }

    public void setLeague(UserLeague userLeague) {
        this.userLeague = userLeague;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

//    public List<UserMatch> getSchedule() {
//        return schedule;
//    }
//
//    public void setSchedule(List<UserMatch> schedule) {
//        this.schedule = schedule;
//    }
//
//    public void addMatchToSchedule(UserMatch userMatch){
//        this.schedule.add(userMatch);
//    }

    public int getWins() {
        return wins;
    }

    public void addWins() {
        this.wins += 1;
    }

    public int getLosses() {
        return losses;
    }

    public void addLosses() {
        this.losses += 1;
    }

    public int getDraws() {
        return draws;
    }

    public void addDraws() {
        this.draws += 1;
    }
}

package org.example.newtopsquadproject.Model.Players;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeague;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeagueStatus;
import org.example.newtopsquadproject.Model.FantasyLeagues.FantasyTeam;
import org.hibernate.annotations.SQLDelete;

import java.util.List;

@Entity
public class MyUser {
    @Id
    @GeneratedValue
    private int id;

    @JsonIgnore
    @OneToOne(mappedBy = "myUser", cascade = CascadeType.ALL)
    private FantasyTeam fantasyTeam;

    @JsonIgnore
    @OneToMany(mappedBy = "myUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLeagueStatus> userLeagueStatuses;

    @JsonIgnore
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLeague> userLeagues;

    private String email;

    private String username;

    private int budget = 100;
    @JsonIgnore
    private String password;

    private String role;

    private int warnings;

    private int gameRoundScore;
    private int overallScore;


    public int getId() {
        return id;
    }



    public FantasyTeam getFantasyTeam() {
        return fantasyTeam;
    }

    public void setFantasyTeam(FantasyTeam fantasyTeam) {
        this.fantasyTeam = fantasyTeam;
    }

    public List<UserLeagueStatus> getUserLeagueStatuses() {
        return userLeagueStatuses;
    }

    public void setUserLeagueStatuses(List<UserLeagueStatus> userLeagueStatuses) {
        this.userLeagueStatuses = userLeagueStatuses;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public List<UserLeague> getUserLeagues() {
        return userLeagues;
    }

    public void setUserLeagues(List<UserLeague> userLeagues) {
        this.userLeagues = userLeagues;
    }

    public void addWarnings(){
        this.warnings += 1;
    }
    public int getWarnings(){
        return warnings;
    }

}

package org.example.newtopsquadproject.Model.ProLeagues;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class BoxScore {
    @Id
    @GeneratedValue
    private int id;

    @OneToMany(mappedBy = "boxScore", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<PlayerBoxScore> awayTeamPlayers;

    @OneToMany(mappedBy = "boxScore", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<PlayerBoxScore> homeTeamPlayers;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private TeamBoxScore awayTeam;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private TeamBoxScore homeTeam;

    public List<PlayerBoxScore> getAwayTeamPlayers() {
        return awayTeamPlayers;
    }

    public void setAwayTeamPlayers(List<PlayerBoxScore> awayTeamPlayers) {
        this.awayTeamPlayers = awayTeamPlayers;
    }

    public List<PlayerBoxScore> getHomeTeamPlayers() {
        return homeTeamPlayers;
    }

    public void setHomeTeamPlayers(List<PlayerBoxScore> homeTeamPlayers) {
        this.homeTeamPlayers = homeTeamPlayers;
    }

    public TeamBoxScore getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(TeamBoxScore awayTeam) {
        this.awayTeam = awayTeam;
    }

    public TeamBoxScore getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(TeamBoxScore homeTeam) {
        this.homeTeam = homeTeam;
    }
}

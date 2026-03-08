package org.example.newtopsquadproject.Model.FantasyLeagues;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class UserMatch {
    @Id
    @GeneratedValue
    private int id;

    public UserMatch() {}
    public UserMatch(HeadToHeadUserLeague headToHeadUserLeague, UserLeagueStatus homeUserLeagueStatus, UserLeagueStatus awayUserLeagueStatus, LeagueRound leagueRound) {
        this.headToHeadUserLeague = headToHeadUserLeague;
        this.homeUserLeagueStatus = homeUserLeagueStatus;
        this.awayUserLeagueStatus = awayUserLeagueStatus;
        this.homeTeam = homeUserLeagueStatus.getMyUser().getFantasyTeam();
        this.awayTeam = awayUserLeagueStatus.getMyUser().getFantasyTeam();
        this.leagueRound = leagueRound;
        this.ongoing = leagueRound.isCurrent();
    }

    @ManyToOne
    @JoinColumn(name="league_id")
    private HeadToHeadUserLeague headToHeadUserLeague;

    @ManyToOne
    @JoinColumn(name="round_id")
    private LeagueRound leagueRound;

    @ManyToOne
    @JoinColumn(name="home_user_league_status_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserLeagueStatus homeUserLeagueStatus;
    @ManyToOne
    @JoinColumn(name="away_user_league_status_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserLeagueStatus awayUserLeagueStatus;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="home_fantasy_team_id")
    private FantasyTeam homeTeam;


    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="away_fantasy_team_id")
    private FantasyTeam awayTeam;

    private int homePoints;

    private int awayPoints;

    private boolean complete = false;

    private boolean ongoing = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }

    public HeadToHeadUserLeague getHeadToHeadUserLeague() {
        return headToHeadUserLeague;
    }

    public void setHeadToHeadUserLeague(HeadToHeadUserLeague headToHeadUserLeague) {
        this.headToHeadUserLeague = headToHeadUserLeague;
    }

    public UserLeagueStatus getHomeUserLeagueStatus() {
        return homeUserLeagueStatus;
    }

    public void setHomeUserLeagueStatus(UserLeagueStatus homeUserLeagueStatus) {
        this.homeUserLeagueStatus = homeUserLeagueStatus;
    }

    public UserLeagueStatus getAwayUserLeagueStatus() {
        return awayUserLeagueStatus;
    }

    public void setAwayUserLeagueStatus(UserLeagueStatus awayUserLeagueStatus) {
        this.awayUserLeagueStatus = awayUserLeagueStatus;
    }

    public FantasyTeam getHomeTeam() {
        return homeTeam;
    }

    public FantasyTeam getAwayTeam() {
        return awayTeam;
    }

    public int getHomePoints() {
        return homePoints;
    }

    public void addHomePoints(int homePoints) {
        this.homePoints += homePoints;
    }

    public int getAwayPoints() {
        return awayPoints;
    }

    public void addAwayPoints(int awayPoints) {
        this.awayPoints += awayPoints;
    }

    public LeagueRound getLeagueRound() {
        return leagueRound;
    }

    public void setLeagueRound(LeagueRound leagueRound) {
        this.leagueRound = leagueRound;
    }
}

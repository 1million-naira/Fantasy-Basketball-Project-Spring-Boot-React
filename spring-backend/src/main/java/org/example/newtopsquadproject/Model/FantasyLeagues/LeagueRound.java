package org.example.newtopsquadproject.Model.FantasyLeagues;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.example.newtopsquadproject.Model.Enums.RoundStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class LeagueRound {

    public LeagueRound() {}

    public LeagueRound(int roundNumber, UserLeague userLeague) {
        this.roundNumber = roundNumber;
        this.userLeague = userLeague;
        this.startedAt = LocalDateTime.now();
    }


    @Id
    @GeneratedValue
    private int id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="league_id")
    private UserLeague userLeague;

    @OneToMany(mappedBy = "leagueRound", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserMatch> userMatches;

    @Enumerated(EnumType.STRING)
    private RoundStatusEnum roundStatusEnum;

    private int roundNumber;

    private LocalDateTime startedAt;

    private boolean current;


    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public UserLeague getUserLeague() {
        return userLeague;
    }

    public void setUserLeague(UserLeague userLeague) {
        this.userLeague = userLeague;
    }

    public RoundStatusEnum getRoundStatusEnum() {
        return roundStatusEnum;
    }

    public void setRoundStatusEnum(RoundStatusEnum roundStatusEnum) {
        this.roundStatusEnum = roundStatusEnum;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public List<UserMatch> getUserMatches() {
        return userMatches;
    }

    public void setUserMatches(List<UserMatch> userMatches) {
        this.userMatches = userMatches;
    }
}

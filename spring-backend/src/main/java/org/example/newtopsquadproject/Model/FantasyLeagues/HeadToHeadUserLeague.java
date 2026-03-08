package org.example.newtopsquadproject.Model.FantasyLeagues;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.example.newtopsquadproject.Model.Enums.HeadToHeadLeagueType;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeague;

import java.util.List;

@Entity
public class HeadToHeadUserLeague extends UserLeague {
    public HeadToHeadUserLeague() {}
    public HeadToHeadUserLeague(String name) {
        super(name);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "headToHeadUserLeague", fetch = FetchType.EAGER)
    private List<UserMatch> userMatches;
    @OneToOne
    private LeagueRound currentRound;
    @Enumerated(EnumType.STRING)
    private HeadToHeadLeagueType headToHeadLeagueType;

    public List<UserMatch> getUserMatches() {
        return userMatches;
    }

    public void setUserMatches(List<UserMatch> userMatches) {
        this.userMatches = userMatches;
    }

    public void addMatches(List<UserMatch> userMatches){
        this.userMatches.addAll(userMatches);
    }


    public LeagueRound getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(LeagueRound currentRound) {
        this.currentRound = currentRound;
    }

    public HeadToHeadLeagueType getHeadToHeadLeagueType() {
        return headToHeadLeagueType;
    }

    public void setHeadToHeadLeagueType(HeadToHeadLeagueType headToHeadLeagueType) {
        this.headToHeadLeagueType = headToHeadLeagueType;
    }
}

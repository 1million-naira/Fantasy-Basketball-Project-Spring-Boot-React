package org.example.newtopsquadproject.Model;

import jakarta.persistence.*;
import org.example.newtopsquadproject.Model.FantasyLeagues.LeagueMessage;
import org.example.newtopsquadproject.Model.Players.MyUser;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class UserReport {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "creatted_by_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MyUser createdBy;

    @ManyToOne
    @JoinColumn(name = "reported_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MyUser reported;

    @ManyToOne
    @JoinColumn(name="message_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private LeagueMessage leagueMessage;

    private String description;

    private boolean resolved;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MyUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(MyUser createdBy) {
        this.createdBy = createdBy;
    }

    public MyUser getReported() {
        return reported;
    }

    public void setReported(MyUser reported) {
        this.reported = reported;
    }

    public LeagueMessage getLeagueMessage() {
        return leagueMessage;
    }

    public void setLeagueMessage(LeagueMessage leagueMessage) {
        this.leagueMessage = leagueMessage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
}

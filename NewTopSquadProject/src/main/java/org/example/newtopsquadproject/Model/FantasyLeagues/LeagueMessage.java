package org.example.newtopsquadproject.Model.FantasyLeagues;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class LeagueMessage {

    public LeagueMessage() {
        this.timeSent = LocalDateTime.now();
    }

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private UserLeagueStatus userLeagueStatus;

    private String message;

    private String senderName;

    private LocalDateTime timeSent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserLeagueStatus getUserLeagueStatus() {
        return userLeagueStatus;
    }

    public void setUserLeagueStatus(UserLeagueStatus userLeagueStatus) {
        this.userLeagueStatus = userLeagueStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }
}

package org.example.newtopsquadproject.Model.DTO;

import java.util.List;

public class FantasyLeagueDTO {


    public FantasyLeagueDTO() {
    }

    public FantasyLeagueDTO(String name, String type, String matchmaking, String access) {
        this.name = name;
        this.type = type;
        this.matchmaking = matchmaking;
        this.access = access;
    }

    public FantasyLeagueDTO(String name, String type, String access) {
        this.name = name;
        this.type = type;
        this.access = access;
    }

    private int id;

    private String name;
    private String type;
    private String matchmaking;

    private String access;
    private String code;
    private int users;

    private boolean created;

    private String timeUntilNextRound;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public String getMatchmaking() {
        return matchmaking;
    }

    public void setMatchmaking(String matchmaking) {
        this.matchmaking = matchmaking;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTimeUntilNextRound() {
        return timeUntilNextRound;
    }

    public void setTimeUntilNextRound(String timeUntilNextRound) {
        this.timeUntilNextRound = timeUntilNextRound;
    }
}

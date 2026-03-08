package org.example.newtopsquadproject.Model.DTO;

public class UserDTO {
    private String username;
    private int budget;
    private int points;
    private int teamValue;
    private int globalRank;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTeamValue() {
        return teamValue;
    }

    public void setTeamValue(int teamValue) {
        this.teamValue = teamValue;
    }

    public int getGlobalRank() {
        return globalRank;
    }

    public void setGlobalRank(int globalRank) {
        this.globalRank = globalRank;
    }
}

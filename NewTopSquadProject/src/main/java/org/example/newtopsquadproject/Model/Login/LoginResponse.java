package org.example.newtopsquadproject.Model.Login;

public class LoginResponse {
    private final String accessToken;

    private final int userId;
    private final String username;
    private final int budget;

    private final boolean admin;

    public LoginResponse(String accessToken, int userId, String username, int budget, boolean admin) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.username = username;
        this.budget = budget;
        this.admin = admin;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public String getUsername() {
        return username;
    }

    public int getBudget() {
        return budget;
    }

    public boolean isAdmin() {
        return admin;
    }

    public int getUserId() {
        return userId;
    }
}

package org.example.newtopsquadproject.Model.Login;

public class RegistrationResponse {
    private final String username;
    private final String email;
    private final String status;

    public RegistrationResponse(String username, String email) {
        this.username = username;
        this.email = email;
        this.status = "Registration success";
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }
}

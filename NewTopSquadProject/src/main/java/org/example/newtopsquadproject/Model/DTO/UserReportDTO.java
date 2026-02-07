package org.example.newtopsquadproject.Model.DTO;

public class UserReportDTO {
    private int id;

    private int userId;

    private String username;

    private String description;

    private int againstId;

    private String againstName;

    private String message;

    private boolean resolved;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAgainstId() {
        return againstId;
    }

    public void setAgainstId(int againstId) {
        this.againstId = againstId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public String getAgainstName() {
        return againstName;
    }

    public void setAgainstName(String againstName) {
        this.againstName = againstName;
    }
}

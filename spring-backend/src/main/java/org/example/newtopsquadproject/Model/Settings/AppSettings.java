package org.example.newtopsquadproject.Model.Settings;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class AppSettings {

    public AppSettings() {
    }

    public AppSettings(String setting, String state) {
        this.setting = setting;
        this.state = state;
        setTime();
    }

    @Id
    private String setting;
    private String state;

    private LocalDateTime time;

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime() {
        this.time = LocalDateTime.now();
    }
}

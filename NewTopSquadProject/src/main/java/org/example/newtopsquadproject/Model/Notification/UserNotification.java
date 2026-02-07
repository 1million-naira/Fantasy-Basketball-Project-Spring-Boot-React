package org.example.newtopsquadproject.Model.Notification;

import jakarta.persistence.*;
import org.example.newtopsquadproject.Model.Players.MyUser;

import java.time.LocalDateTime;

@Entity
public class UserNotification {

    public UserNotification() {
        this.timeCreated = LocalDateTime.now();
    }

    public UserNotification(MyUser myUser, NotificationType type, String message) {
        this.myUser = myUser;
        this.type = type;
        this.message = message;
    }

    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    @JoinColumn(name="user_id")
    private MyUser myUser;
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String message;

    private LocalDateTime timeCreated;

    private boolean delivered;
    private boolean notificationRead;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public boolean isNotificationRead() {
        return notificationRead;
    }

    public void setNotificationRead(boolean notificationRead) {
        this.notificationRead = notificationRead;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }
}

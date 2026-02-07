package org.example.newtopsquadproject.Repository;

import org.example.newtopsquadproject.Model.Notification.UserNotification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepo extends CrudRepository<UserNotification, Integer> {
    List<UserNotification> findAllByMyUserId(int id);
//    List<UserNotification> findAllByRead(boolean read);
}

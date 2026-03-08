package org.example.newtopsquadproject.Service;

import org.example.newtopsquadproject.Model.Notification.NotificationDTO;
import org.example.newtopsquadproject.Model.Notification.UserNotification;
import org.example.newtopsquadproject.Model.Players.MyUser;
import org.example.newtopsquadproject.Repository.NotificationRepo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepo notificationRepo;

    private final SimpMessagingTemplate simpMessagingTemplate;



    public NotificationService(NotificationRepo notificationRepo, SimpMessagingTemplate simpMessagingTemplate) {
        this.notificationRepo = notificationRepo;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void createNotification(UserNotification userNotification){
        notificationRepo.save(userNotification);
    }

    public NotificationDTO notificationToDto(UserNotification userNotification){
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(userNotification.getId());
        notificationDTO.setUsername(userNotification.getMyUser().getUsername());
        notificationDTO.setMessage(userNotification.getMessage());
        String time = userNotification.getTimeCreated().format(DateTimeFormatter.ofPattern("MM-dd HH:mm:ss"));
        notificationDTO.setTime(time);
        return notificationDTO;
    }


    public List<NotificationDTO> getNotificationsForUser(int userId){
        List<UserNotification> userNotifications = notificationRepo.findAllByMyUserId(userId);
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for(UserNotification u : userNotifications){
            notificationDTOList.add(notificationToDto(u));
        }
        return notificationDTOList;
    }
}

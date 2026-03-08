package org.example.newtopsquadproject.Controller;

import org.example.newtopsquadproject.Model.Notification.NotificationDTO;
import org.example.newtopsquadproject.Model.Players.MyUser;
import org.example.newtopsquadproject.Security.UserPrincipal;
import org.example.newtopsquadproject.Service.MyUserService;
import org.example.newtopsquadproject.Service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/notifications")
public class NotificationController {

    private final MyUserService myUserService;
    private final NotificationService notificationService;

    public NotificationController(MyUserService myUserService, NotificationService notificationService) {
        this.myUserService = myUserService;
        this.notificationService = notificationService;
    }

    @GetMapping("/user")
    public ResponseEntity<List<NotificationDTO>> getNotificationsForCurrentlyAuthenticated(){
        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<NotificationDTO> notificationDTOList = notificationService.getNotificationsForUser(principal.getUserId());

        return new ResponseEntity<>(notificationDTOList, HttpStatus.OK);
    }


}

package org.example.newtopsquadproject.Controller;

import org.example.newtopsquadproject.Component.SocketUser;
import org.example.newtopsquadproject.Model.DTO.FantasyLeagueDTO;
import org.example.newtopsquadproject.Model.DTO.LeagueMessageDTO;
import org.example.newtopsquadproject.Model.DTO.UserReportDTO;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeague;
import org.example.newtopsquadproject.Model.Notification.NotificationDTO;
import org.example.newtopsquadproject.Model.Notification.UserNotification;
import org.example.newtopsquadproject.Model.Players.MyUser;
import org.example.newtopsquadproject.Service.MyUserService;
import org.example.newtopsquadproject.Service.NotificationService;
import org.example.newtopsquadproject.Service.ReportService;
import org.example.newtopsquadproject.Service.UserLeagueService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class MessageController {

    private final NotificationService notificationService;
    private final MyUserService myUserService;

    private final ReportService reportService;
    private final UserLeagueService userLeagueService;

    public MessageController(NotificationService notificationService, MyUserService myUserService, ReportService reportService, UserLeagueService userLeagueService) {
        this.notificationService = notificationService;
        this.myUserService = myUserService;
        this.reportService = reportService;
        this.userLeagueService = userLeagueService;
    }

    @MessageMapping("/league-created")
    @SendToUser("/api/notifications/league")
    public NotificationDTO leagueCreated(@Payload FantasyLeagueDTO fantasyLeagueDTO, Principal principalUser){

        SocketUser socketUser = (SocketUser) principalUser;
        MyUser myUser = myUserService.findById(socketUser.getId());

        UserLeague userLeague = userLeagueService.findLeagueById(fantasyLeagueDTO.getId());
        String code = "";
        if(userLeague.getCode() == null){
            code = "There is no code as this is a public league";
        } else{
            code = userLeague.getCode();
        }

        UserNotification userNotification = new UserNotification();
        userNotification.setMyUser(myUser);
        userNotification.setMessage("You created a new league with name: " + fantasyLeagueDTO.getName() + " and code: " + code);
        notificationService.createNotification(userNotification);

        return notificationService.notificationToDto(userNotification);
    }

    @MessageMapping("/league-chat/{id}")
    @SendTo("/api/chat/{id}")
    public LeagueMessageDTO leagueChat(@DestinationVariable("id") int id, @Payload LeagueMessageDTO leagueMessageDTO, Principal principalUser){
        SocketUser socketUser = (SocketUser) principalUser;
        MyUser myUser = myUserService.findById(socketUser.getId());
        return leagueMessageDTO;
    }


    @MessageMapping("/warnUser/{id}")
    @SendTo("/api/general/{id}")
    public NotificationDTO notifyUserWarning(@DestinationVariable("id") int userId, Principal principalUser){
        SocketUser socketUser = (SocketUser) principalUser;

        MyUser warnedUser = myUserService.findById(userId);
        reportService.warnUser(userId);

        UserNotification userNotification = new UserNotification();
        userNotification.setMyUser(warnedUser);
        userNotification.setMessage("You have received a warning for bad behaviour, you currently have " + warnedUser.getWarnings() + " warnings");
        notificationService.createNotification(userNotification);

        return notificationService.notificationToDto(userNotification);
    }

    @MessageMapping("/admin/report")
    @SendTo("/api/admin")
    public UserReportDTO receiveReport(@Payload UserReportDTO userReportDTO){
        return userReportDTO;
    }
}

package org.example.newtopsquadproject.Service;

import org.example.newtopsquadproject.Exceptions.ResourceNotFoundException;
import org.example.newtopsquadproject.Exceptions.ValidationException;
import org.example.newtopsquadproject.Model.DTO.FantasyLeagueDTO;
import org.example.newtopsquadproject.Model.DTO.UserLeagueStatusDTO;
import org.example.newtopsquadproject.Model.Enums.HeadToHeadLeagueType;
import org.example.newtopsquadproject.Model.FantasyLeagues.*;
import org.example.newtopsquadproject.Model.Notification.UserNotification;
import org.example.newtopsquadproject.Model.Players.MyUser;
import org.example.newtopsquadproject.Repository.FantasyLeagues.HeadToHeadUserLeagueRepo;
import org.example.newtopsquadproject.Repository.FantasyLeagues.LeagueRoundRepo;
import org.example.newtopsquadproject.Repository.FantasyLeagues.UserLeagueStatusRepo;
import org.example.newtopsquadproject.Repository.FantasyLeagues.UserMatchRepo;
import org.example.newtopsquadproject.Repository.MyUserRepo;
import org.example.newtopsquadproject.Utils.LeagueDuration;
import org.example.newtopsquadproject.Utils.TimeEnum;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HeadToHeadLeagueService {

    private final HeadToHeadUserLeagueRepo headToHeadUserLeagueRepo;
    private final MyUserRepo myUserRepo;

    private final LeagueRoundRepo leagueRoundRepo;

    private final UserLeagueStatusRepo userLeagueStatusRepo;

    private final UserMatchRepo userMatchRepo;

    private final NotificationService notificationService;

    private final SimpMessagingTemplate simpMessagingTemplate;




    public HeadToHeadLeagueService(HeadToHeadUserLeagueRepo headToHeadUserLeagueRepo, MyUserRepo myUserRepo, LeagueRoundRepo leagueRoundRepo, UserLeagueStatusRepo userLeagueStatusRepo, UserMatchRepo userMatchRepo, NotificationService notificationService, SimpMessagingTemplate simpMessagingTemplate) {
        this.headToHeadUserLeagueRepo = headToHeadUserLeagueRepo;
        this.myUserRepo = myUserRepo;
        this.leagueRoundRepo = leagueRoundRepo;
        this.userLeagueStatusRepo = userLeagueStatusRepo;
        this.userMatchRepo = userMatchRepo;
        this.notificationService = notificationService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public HeadToHeadUserLeague findById(int id){
        return headToHeadUserLeagueRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("League not found with Id: " + id));
    }

    public HeadToHeadUserLeague save(HeadToHeadUserLeague userLeague){
        return headToHeadUserLeagueRepo.save(userLeague);
    }


//    public HeadToHeadUserLeague createHeadtoHeadUserLeague(HeadToHeadUserLeague userLeague, HeadToHeadLeagueType headToHeadLeagueType, int userId){
//        MyUser myUser = myUserRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with Id: " + userId));
//        userLeague.setCreatedBy(myUser);
//        userLeague.setCreatedAt();
//        userLeague.setHeadToHeadLeagueType(headToHeadLeagueType);
//        userLeague.generateCode();
//        System.out.println("User League created with code: " + userLeague.getCode() + " at: " + userLeague.getCreatedAt());
//
//        headToHeadUserLeagueRepo.save(userLeague);
//        myUserRepo.save(myUser);
//
//        return userLeague;
//    }
//
//
//    public UserLeagueStatus createHeadToHeadUserLeagueStatus(int userId, int leagueId){
//        MyUser myUser = myUserRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with Id: " + userId));
//        HeadToHeadUserLeague headToHeadUserLeague = headToHeadUserLeagueRepo.findById(leagueId).orElseThrow(() -> new ResourceNotFoundException("League not found with Id: " + leagueId));
//
//        if(userLeagueStatusRepo.existsByUserLeagueIdAndMyUserId(leagueId, userId)){
//            throw new ValidationException("User: " + myUser.getUsername() + " has already joined this league");
//        }
//        UserLeagueStatus userLeagueStatus = new UserLeagueStatus(myUser, headToHeadUserLeague);
//        myUserRepo.save(myUser);
//        headToHeadUserLeagueRepo.save(headToHeadUserLeague);
//        return userLeagueStatusRepo.save(userLeagueStatus);
//    }

    public LeagueRound createLeagueRound(HeadToHeadUserLeague headToHeadUserLeague){
        if(checkIfCurrentRoundIsFinished(headToHeadUserLeague)){
            List<LeagueRound> leagueRounds = leagueRoundRepo.findAllByUserLeagueId(headToHeadUserLeague.getId());

            if(leagueRounds.isEmpty()){
                LeagueRound leagueRound = new LeagueRound(1, headToHeadUserLeague);
                leagueRounds.add(leagueRound);

                headToHeadUserLeague.setCurrentRound(leagueRound);
                leagueRound.setCurrent(true);

                leagueRoundRepo.saveAll(leagueRounds);
                headToHeadUserLeagueRepo.save(headToHeadUserLeague);

                return leagueRound;

            } else{
                leagueRounds.get(leagueRounds.size()-1).setCurrent(false);

                List<UserMatch> userMatches = userMatchRepo.findAllByHeadToHeadUserLeagueId(headToHeadUserLeague.getId());
                decideUserMatchResults(userMatches);

                LeagueRound leagueRound = new LeagueRound(leagueRounds.size()+1, headToHeadUserLeague);
                leagueRounds.add(leagueRound);

                headToHeadUserLeague.setCurrentRound(leagueRound);
                leagueRound.setCurrent(true);

                leagueRoundRepo.saveAll(leagueRounds);
                headToHeadUserLeagueRepo.save(headToHeadUserLeague);

                return leagueRound;

            }
        }
        return null;
    }

    public void decideUserMatchResults(List<UserMatch> userMatches){
        String homeResult = "";
        String awayResult = "";

        for(UserMatch userMatch : userMatches){
            UserLeagueStatus homeUserLeagueStatus = userMatch.getHomeUserLeagueStatus();
            UserLeagueStatus awayUserLeagueStatus = userMatch.getAwayUserLeagueStatus();

            if(userMatch.getHomePoints() > userMatch.getAwayPoints()){
                homeUserLeagueStatus.addWins();
                awayUserLeagueStatus.addLosses();
                homeResult = "Congratulations! You won a head to head against " + awayUserLeagueStatus.getMyUser().getUsername() + " in league: " + userMatch.getHeadToHeadUserLeague().getName();
                awayResult = "Better luck next time! You lost a head to head against " + homeUserLeagueStatus.getMyUser().getUsername() + " in league: " + userMatch.getHeadToHeadUserLeague().getName();
            } else if(userMatch.getHomePoints() < userMatch.getAwayPoints()){
                awayUserLeagueStatus.addWins();
                homeUserLeagueStatus.addLosses();
                homeResult = "Better luck next time! You lost a head to head against " + awayUserLeagueStatus.getMyUser().getUsername() + " in league: " + userMatch.getHeadToHeadUserLeague().getName();
                awayResult = "Congratulations! You won a head to head against " + homeUserLeagueStatus.getMyUser().getUsername() + " in league: " + userMatch.getHeadToHeadUserLeague().getName();
            } else{
                homeUserLeagueStatus.addDraws();
                awayUserLeagueStatus.addDraws();
                homeResult = "Not bad! You drew a head to head against " + awayUserLeagueStatus.getMyUser().getUsername() + " in league: " + userMatch.getHeadToHeadUserLeague().getName();
                awayResult = "Not bad! You drew a head to head against " + homeUserLeagueStatus.getMyUser().getUsername() + " in league: " + userMatch.getHeadToHeadUserLeague().getName();
            }

            UserNotification homeNotification = new UserNotification();
            homeNotification.setMessage(homeResult);
            homeNotification.setMyUser(homeUserLeagueStatus.getMyUser());
            notificationService.createNotification(homeNotification);

            UserNotification awayNotification = new UserNotification();
            awayNotification.setMessage(awayResult);
            awayNotification.setMyUser(awayUserLeagueStatus.getMyUser());
            notificationService.createNotification(awayNotification);

            simpMessagingTemplate.convertAndSend("/api/general/" + homeUserLeagueStatus.getMyUser().getId(), notificationService.notificationToDto(homeNotification));
            simpMessagingTemplate.convertAndSend("/api/general/" + awayUserLeagueStatus.getMyUser().getId(), notificationService.notificationToDto(awayNotification));
            userMatch.setComplete(true);
            userMatch.setOngoing(false);
        }
        userMatchRepo.saveAll(userMatches);
    }


    public boolean checkIfCurrentRoundIsFinished(HeadToHeadUserLeague headToHeadUserLeague){
        LocalDateTime currentDate = LocalDateTime.now();

        List<LeagueRound> leagueRounds = leagueRoundRepo.findAllByUserLeagueId(headToHeadUserLeague.getId());

        if(leagueRounds.isEmpty()){
            System.out.println("Head to head League with ID: " + headToHeadUserLeague.getId() + " has no rounds yet");
            return true;
        }

        Duration duration = Duration.between(headToHeadUserLeague.getCurrentRound().getStartedAt(), currentDate);
        LeagueDuration leagueDuration = LeagueDuration.getInstance();
        long durationToLong = 0;

        switch (leagueDuration.getTimeEnum()){
            case HOURS -> durationToLong = duration.toHours();

            case DAYS -> durationToLong = duration.toDays();

            default -> durationToLong = duration.toMinutes();
        }

        if(durationToLong >= leagueDuration.getUnit()){
            System.out.println("Current round of Head to head League with ID: " + headToHeadUserLeague.getId() + " finished");
            return true;
        } else{
            System.out.println("Current round of Head to head League with ID: " + headToHeadUserLeague.getId() + " not yet finished");
            return false;
        }
    }


}

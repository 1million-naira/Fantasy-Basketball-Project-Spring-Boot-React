package org.example.newtopsquadproject.Service;

import org.example.newtopsquadproject.Exceptions.ResourceNotFoundException;
import org.example.newtopsquadproject.Exceptions.ValidationException;
import org.example.newtopsquadproject.Model.DTO.FantasyLeagueDTO;
import org.example.newtopsquadproject.Model.DTO.LeagueMessageDTO;
import org.example.newtopsquadproject.Model.DTO.PlayerDTO;
import org.example.newtopsquadproject.Model.DTO.UserLeagueStatusDTO;
import org.example.newtopsquadproject.Model.Enums.HeadToHeadLeagueType;
import org.example.newtopsquadproject.Model.Enums.LeagueTypeEnum;
import org.example.newtopsquadproject.Model.FantasyLeagues.*;
import org.example.newtopsquadproject.Model.Notification.UserNotification;
import org.example.newtopsquadproject.Model.Notification.NotificationType;
import org.example.newtopsquadproject.Model.Players.MyUser;
import org.example.newtopsquadproject.Model.Players.Player;
import org.example.newtopsquadproject.Model.Players.PlayerTeamStatus;
import org.example.newtopsquadproject.Repository.FantasyLeagues.*;
import org.example.newtopsquadproject.Repository.MyUserRepo;
import org.example.newtopsquadproject.Repository.Players.PlayerTeamStatusRepo;
import org.example.newtopsquadproject.Utils.LeagueDuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserLeagueService {
    private final HeadToHeadUserLeagueRepo headToHeadUserLeagueRepo;
    private final RotoUserLeagueRepo rotoUserLeagueRepo;
    private final UserLeagueStatusRepo userLeagueStatusRepo;

    private final LeagueMessageRepo leagueMessageRepo;

    private final UserMatchRepo userMatchRepo;

    private final MyUserRepo myUserRepo;

    private final NotificationService notificationService;

    private final LeagueRoundRepo leagueRoundRepo;

    private final PlayerTeamStatusRepo playerTeamStatusRepo;

    private final PlayerService playerService;

    public UserLeagueService(HeadToHeadUserLeagueRepo headToHeadUserLeagueRepo, RotoUserLeagueRepo rotoUserLeagueRepo, UserLeagueStatusRepo userLeagueStatusRepo, LeagueMessageRepo leagueMessageRepo, UserMatchRepo userMatchRepo, MyUserRepo myUserRepo, NotificationService notificationService, LeagueRoundRepo leagueRoundRepo, PlayerTeamStatusRepo playerTeamStatusRepo, PlayerService playerService) {
        this.headToHeadUserLeagueRepo = headToHeadUserLeagueRepo;
        this.rotoUserLeagueRepo = rotoUserLeagueRepo;
        this.userLeagueStatusRepo = userLeagueStatusRepo;
        this.leagueMessageRepo = leagueMessageRepo;
        this.userMatchRepo = userMatchRepo;
        this.myUserRepo = myUserRepo;
        this.notificationService = notificationService;
        this.leagueRoundRepo = leagueRoundRepo;
        this.playerTeamStatusRepo = playerTeamStatusRepo;
        this.playerService = playerService;
    }

    public List<UserLeague> findAll(){
        return headToHeadUserLeagueRepo.findAllUserLeagues();
    }
    public UserLeague findLeagueById(int leagueId){
        Optional<HeadToHeadUserLeague> headToHeadUserLeagueOptional = headToHeadUserLeagueRepo.findById(leagueId);
        Optional<RotoUserLeague> rotoUserLeagueOptional = rotoUserLeagueRepo.findById(leagueId);

        if(headToHeadUserLeagueOptional.isPresent()){
            return headToHeadUserLeagueOptional.get();
        } else if(rotoUserLeagueOptional.isPresent()){
            return rotoUserLeagueOptional.get();
        } else{
            throw new ResourceNotFoundException("League not present with Id: "+leagueId);
        }
    }


    public UserLeague createLeague(FantasyLeagueDTO fantasyLeagueDTO, MyUser myUser){
        if(fantasyLeagueDTO.getType().trim().equalsIgnoreCase("h2h")){
            HeadToHeadUserLeague headToHeadUserLeague = new HeadToHeadUserLeague();
            headToHeadUserLeague.setLeagueTypeEnum(LeagueTypeEnum.HEAD_TO_HEAD);
            if(fantasyLeagueDTO.getMatchmaking().trim().equalsIgnoreCase("rr")){
                headToHeadUserLeague.setHeadToHeadLeagueType(HeadToHeadLeagueType.ROUND_ROBIN);
            } else if(fantasyLeagueDTO.getMatchmaking().trim().equalsIgnoreCase("random")){
                headToHeadUserLeague.setHeadToHeadLeagueType(HeadToHeadLeagueType.RANDOM);
            }

            if(fantasyLeagueDTO.getAccess().trim().equalsIgnoreCase("private")){
                headToHeadUserLeague.generateCode();
            }
            headToHeadUserLeague.setName(fantasyLeagueDTO.getName().trim());
            headToHeadUserLeague.setCreatedAt();
            headToHeadUserLeague.setCreatedBy(myUser);
            headToHeadUserLeagueRepo.save(headToHeadUserLeague);
            joinLeague(myUser.getId(), headToHeadUserLeague.getId());
            return headToHeadUserLeague;
        } else if(fantasyLeagueDTO.getType().trim().equalsIgnoreCase("roto")){
            RotoUserLeague rotoUserLeague = new RotoUserLeague();
            rotoUserLeague.setLeagueTypeEnum(LeagueTypeEnum.ROTO);
            rotoUserLeague.setName(fantasyLeagueDTO.getName().trim());

            if(fantasyLeagueDTO.getAccess().trim().equalsIgnoreCase("private")){
                rotoUserLeague.generateCode();
            }
            rotoUserLeague.setCreatedAt();
            rotoUserLeague.setCreatedBy(myUser);
            rotoUserLeagueRepo.save(rotoUserLeague);
            joinLeague(myUser.getId(), rotoUserLeague.getId());
            return rotoUserLeague;
        } else{
            throw new ValidationException("League could not be created");
        }
    }


    public UserLeagueStatus joinLeague(int userId, int leagueId){
        MyUser myUser = myUserRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with Id: " + userId));
        UserLeague userLeague = findLeagueById(leagueId);

        if(userLeagueStatusRepo.existsByUserLeagueIdAndMyUserId(leagueId, userId)){
            throw new ValidationException("User: " + myUser.getUsername() + " has already joined this league");
        }
        UserLeagueStatus userLeagueStatus = new UserLeagueStatus(myUser, userLeague);

        myUserRepo.save(myUser);
        if(userLeague.getLeagueTypeEnum() == LeagueTypeEnum.HEAD_TO_HEAD){
            headToHeadUserLeagueRepo.save((HeadToHeadUserLeague) userLeague);
        } else if(userLeague.getLeagueTypeEnum() == LeagueTypeEnum.ROTO){
            rotoUserLeagueRepo.save((RotoUserLeague) userLeague);
        }
        return userLeagueStatusRepo.save(userLeagueStatus);
    }

    public UserLeagueStatusDTO userLeagueStatusToDto(UserLeagueStatus userLeagueStatus){
        UserLeagueStatusDTO userLeagueStatusDTO = new UserLeagueStatusDTO();
        userLeagueStatusDTO.setId(userLeagueStatus.getMyUser().getId());
        userLeagueStatusDTO.setTeam(userLeagueStatus.getMyUser().getFantasyTeam().getName());
        userLeagueStatusDTO.setName(userLeagueStatus.getMyUser().getUsername());
        userLeagueStatusDTO.setTotal(userLeagueStatus.getMyUser().getFantasyTeam().getFantasyPoints());
        userLeagueStatusDTO.setTeamId(userLeagueStatus.getMyUser().getFantasyTeam().getId());

        if(userLeagueStatus.getLeague().getLeagueTypeEnum() == LeagueTypeEnum.HEAD_TO_HEAD){
            userLeagueStatusDTO.setWins(userLeagueStatus.getWins());
            userLeagueStatusDTO.setLosses(userLeagueStatus.getLosses());
            userLeagueStatusDTO.setDraws(userLeagueStatus.getDraws());
        }
        int round = 0;
        Optional<UserMatch> currentUserMatchOptional = userMatchRepo.findCurrentMatchOfUserInLeague(userLeagueStatus.getLeague().getId(), userLeagueStatus.getId());
        if(currentUserMatchOptional.isPresent()){
            UserMatch userMatch = currentUserMatchOptional.get();
            if(userLeagueStatus.getId() == userMatch.getHomeUserLeagueStatus().getId()){
                round = userMatch.getHomePoints();
            } else if(userLeagueStatus.getId() == userMatch.getAwayUserLeagueStatus().getId()){
                round = userMatch.getAwayPoints();
            }
        }
        userLeagueStatusDTO.setRound(round);

        return userLeagueStatusDTO;
    }

    public List<UserLeagueStatusDTO> getLeagueUsers(UserLeague userLeague){
        List<UserLeagueStatus> userLeagueStatuses = userLeague.getUserLeagueStatuses();
        List<UserLeagueStatusDTO> userLeagueStatusDTOList = new ArrayList<>();

        for(UserLeagueStatus u: userLeagueStatuses){
            userLeagueStatusDTOList.add(userLeagueStatusToDto(u));
        }

        if(userLeague.getLeagueTypeEnum() == LeagueTypeEnum.HEAD_TO_HEAD){
            userLeagueStatusDTOList.sort(Comparator.comparing(UserLeagueStatusDTO::getWins).reversed()); //Sort list in descending order of total wins
        } else if(userLeague.getLeagueTypeEnum() == LeagueTypeEnum.ROTO){
            userLeagueStatusDTOList.sort(Comparator.comparing(UserLeagueStatusDTO::getTotal).reversed()); //Sort list in descending order of total points
        }

        int pos = 1;
        for(UserLeagueStatusDTO u : userLeagueStatusDTOList){
            u.setPos(pos);
            pos+=1;
        } //Assign positions in league;

        return userLeagueStatusDTOList;
    }

    public List<FantasyLeagueDTO> findByCode(String code, int userId){
        List<UserLeague> userLeagues = new ArrayList<>();
        userLeagues.addAll(headToHeadUserLeagueRepo.findAllByCode(code));
        userLeagues.addAll(rotoUserLeagueRepo.findAllByCode(code));

        List<FantasyLeagueDTO> fantasyLeagueDTOList = new ArrayList<>();

        for(UserLeague u : userLeagues){
            fantasyLeagueDTOList.add(fantasyLeagueToDto(u, userId));
        }
        return fantasyLeagueDTOList;
    }

    public List<LeagueMessageDTO> getAllMessagesByUserInLeague(int userId, int leagueId){

        List<LeagueMessageDTO> leagueMessageDTOList = new ArrayList<>();

        Optional<UserLeagueStatus> userLeagueStatusOptional = userLeagueStatusRepo.findByUserLeagueIdAndMyUserId(leagueId, userId);
        if(userLeagueStatusOptional.isEmpty()){
            throw new ResourceNotFoundException("User with Id: " + userId + " not present in league with Id: " + leagueId);
        }
        UserLeagueStatus userLeagueStatus = userLeagueStatusOptional.get();

        List<LeagueMessage> leagueMessages = leagueMessageRepo.findAllByUserLeagueStatusId(userLeagueStatus.getId());
        if(leagueMessages.isEmpty()){
            return leagueMessageDTOList; //Return empty list
        }

        for(LeagueMessage leagueMessage : leagueMessages){
            LeagueMessageDTO leagueMessageDTO = leagueMessageToDto(leagueMessage);
            leagueMessageDTO.setUser(true);
            leagueMessageDTOList.add(leagueMessageDTO);
        }

        return leagueMessageDTOList;
    }

    public List<LeagueMessageDTO> getAllMessagesInLeagueForUser(int leagueId, int userId){
        List<LeagueMessageDTO> leagueMessageDTOList = new ArrayList<>();
        UserLeague userLeague = headToHeadUserLeagueRepo.findUserLeagueById(leagueId).orElseThrow(() -> new ResourceNotFoundException("League not present with Id: "+leagueId));

//        int id = 0;
//        Optional<HeadToHeadUserLeague> headToHeadUserLeagueOptional = headToHeadUserLeagueRepo.findById(leagueId);
//        Optional<RotoUserLeague> rotoUserLeagueOptional = rotoUserLeagueRepo.findById(leagueId);
//
//        if(headToHeadUserLeagueOptional.isPresent()){
//            id = headToHeadUserLeagueOptional.get().getId();
//        } else if(rotoUserLeagueOptional.isPresent()){
//            id = rotoUserLeagueOptional.get().getId();
//        } else{
//            throw new ResourceNotFoundException("League not present with Id: "+leagueId);
//        }

        List<LeagueMessage> leagueMessages = leagueMessageRepo.findAllByUserLeagueStatus_UserLeague_Id(leagueId);
        if(leagueMessages.isEmpty()){
            return leagueMessageDTOList; //Return empty list
        }

        for(LeagueMessage leagueMessage : leagueMessages){
            LeagueMessageDTO leagueMessageDTO = leagueMessageToDto(leagueMessage);
            leagueMessageDTO.setUser(leagueMessage.getUserLeagueStatus().getMyUser().getId() == userId);
            leagueMessageDTOList.add(leagueMessageDTO);
        }

        return leagueMessageDTOList;
    }



    public LeagueMessageDTO createLeagueMessage(int leagueId, int userId, String message){

        Optional<UserLeagueStatus> userLeagueStatusOptional = userLeagueStatusRepo.findByUserLeagueIdAndMyUserId(leagueId, userId);
        if(userLeagueStatusOptional.isEmpty()){
            throw new ResourceNotFoundException("User with Id: " + userId + " not present in league with Id: " + leagueId);
        }

        UserLeagueStatus userLeagueStatus = userLeagueStatusOptional.get();
        LeagueMessage leagueMessage = new LeagueMessage();
        leagueMessage.setMessage(message);
        leagueMessage.setUserLeagueStatus(userLeagueStatus);
        leagueMessage.setSenderName(userLeagueStatus.getMyUser().getUsername());

        leagueMessageRepo.save(leagueMessage);

        LeagueMessageDTO leagueMessageDTO = leagueMessageToDto(leagueMessage);
        leagueMessageDTO.setUser(true);
        return leagueMessageDTO;
    }



    public LeagueMessageDTO leagueMessageToDto(LeagueMessage leagueMessage){
        LeagueMessageDTO leagueMessageDTO = new LeagueMessageDTO();

        leagueMessageDTO.setId(leagueMessage.getId());
        leagueMessageDTO.setUserId(leagueMessage.getUserLeagueStatus().getMyUser().getId());
        leagueMessageDTO.setLeagueId(leagueMessage.getUserLeagueStatus().getLeague().getId());
        leagueMessageDTO.setName(leagueMessage.getSenderName());
        leagueMessageDTO.setMessage(leagueMessage.getMessage());

        return leagueMessageDTO;
    }

    public FantasyLeagueDTO fantasyLeagueToDto(UserLeague userLeague, int userId){
        MyUser myUser = myUserRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        FantasyLeagueDTO fantasyLeagueDTO = new FantasyLeagueDTO();
        fantasyLeagueDTO.setId(userLeague.getId());
        fantasyLeagueDTO.setName(userLeague.getName());

        fantasyLeagueDTO.setCreated(userLeague.getCreatedBy().getId() == userId);

        if(userLeague.getCode() == null){
            fantasyLeagueDTO.setAccess("public");
        } else{
            if(fantasyLeagueDTO.isCreated() || myUser.getRole().equals("ROLE_ADMIN")){
                fantasyLeagueDTO.setCode(userLeague.getCode());
            }
            fantasyLeagueDTO.setAccess("private");
        }

        String type = "";
        String matchmaking = "";

        if(userLeague.getLeagueTypeEnum() == LeagueTypeEnum.HEAD_TO_HEAD){
            type="H2H";
            HeadToHeadUserLeague headToHeadUserLeague = (HeadToHeadUserLeague) userLeague;

            if(headToHeadUserLeague.getHeadToHeadLeagueType() == HeadToHeadLeagueType.ROUND_ROBIN){
                matchmaking = "Round Robin";
            } else if(headToHeadUserLeague.getHeadToHeadLeagueType() == HeadToHeadLeagueType.RANDOM){
                matchmaking = "Random";
            }
            fantasyLeagueDTO.setMatchmaking(matchmaking);


            if(((HeadToHeadUserLeague) userLeague).getCurrentRound() != null){
                LocalDateTime startOfCurrentRound = ((HeadToHeadUserLeague) userLeague).getCurrentRound().getStartedAt();
                Duration timeRemaining;
                LeagueDuration leagueDuration = LeagueDuration.getInstance();
                switch (leagueDuration.getTimeEnum()){
                    case DAYS -> timeRemaining = Duration.between(LocalDateTime.now(), startOfCurrentRound.plusDays(leagueDuration.getUnit()));

                    case HOURS -> timeRemaining = Duration.between(LocalDateTime.now(), startOfCurrentRound.plusHours(leagueDuration.getUnit()));

                    default -> timeRemaining = Duration.between(LocalDateTime.now(), startOfCurrentRound.plusMinutes(leagueDuration.getUnit()));
                }

                long HH = timeRemaining.toHours();
                long MM = timeRemaining.toMinutesPart();
                long SS = timeRemaining.toSecondsPart();

                if(SS<0){
                    fantasyLeagueDTO.setTimeUntilNextRound("00:00:00");
                } else{
                    String time = String.format("%02d:%02d:%02d", HH, MM, SS);
                    fantasyLeagueDTO.setTimeUntilNextRound(time);
                }
            } else{
                fantasyLeagueDTO.setTimeUntilNextRound("N/A");
            }



        } else if(userLeague.getLeagueTypeEnum() == LeagueTypeEnum.ROTO){
            type="Roto";
        }
        fantasyLeagueDTO.setType(type);
        if(userLeague.getUserLeagueStatuses() == null){
            fantasyLeagueDTO.setUsers(0);
        } else{
            fantasyLeagueDTO.setUsers(userLeague.getUserLeagueStatuses().size());
        }

        return fantasyLeagueDTO;
    }

    public List<FantasyLeagueDTO> getAllLeaguesForUser(int id){
        List<FantasyLeagueDTO> fantasyLeagueDTOList = new ArrayList<>();
        List<UserLeagueStatus> userLeagueStatuses = userLeagueStatusRepo.findAllByMyUserId(id);
        for(UserLeagueStatus u : userLeagueStatuses){
            FantasyLeagueDTO fantasyLeagueDTO = fantasyLeagueToDto(u.getLeague(), id);
            fantasyLeagueDTOList.add(fantasyLeagueDTO);
        }
        return fantasyLeagueDTOList;
    }

    public void deleteLeague(int id){
//        HeadToHeadUserLeague headToHeadUserLeague = headToHeadUserLeagueRepo.findById(id).orElseThrow();
        UserLeague userLeague = headToHeadUserLeagueRepo.findUserLeagueById(id).orElseThrow(() -> new ResourceNotFoundException("League not present with Id: "+id));

        List<UserLeagueStatus> userLeagueStatuses = userLeagueStatusRepo.findAllByUserLeague(userLeague);
        for(UserLeagueStatus u : userLeagueStatuses){
            u.setLeague(null);
        }
        userLeagueStatusRepo.saveAll(userLeagueStatuses);
        userLeagueStatusRepo.deleteAll(userLeagueStatuses);

        if(userLeague.getLeagueTypeEnum() == LeagueTypeEnum.HEAD_TO_HEAD){
            List<UserMatch> userMatches = userMatchRepo.findAllByHeadToHeadUserLeagueId(id);
            for(UserMatch userMatch : userMatches){
                userMatch.setLeagueRound(null);
            }
            userMatchRepo.saveAll(userMatches);
            userMatchRepo.deleteAll(userMatches);
        }

        List<LeagueRound> leagueRounds = leagueRoundRepo.findAllByUserLeagueId(id);
        for(LeagueRound l : leagueRounds){
            l.setUserLeague(null);
        }
        leagueRoundRepo.saveAll(leagueRounds);
        leagueRoundRepo.deleteAll(leagueRounds);

        headToHeadUserLeagueRepo.delete(userLeague);
    }


    public Page<FantasyLeagueDTO> getHeadToHeadLeaguePage(int page, int size, String sortBy, String direction, int userId, String search){


        Sort sort = direction.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);


        if(!(search==null)){
            if(!search.isBlank()){
                int searchId = 0;
                try{
                    searchId = Integer.parseInt(search.trim());
                    Page<UserLeague> userLeaguePage = headToHeadUserLeagueRepo.findById(pageRequest, searchId);
                    return userLeaguePage.map(userLeague -> fantasyLeagueToDto(userLeague, userId));
                } catch(Exception e){
                    return Page.empty();
                }
            }
        }


        Page<UserLeague> userLeaguePage = headToHeadUserLeagueRepo.findAll(pageRequest);
        return userLeaguePage.map(userLeague -> fantasyLeagueToDto(userLeague, userId));
    }

    public List<PlayerDTO> getTopPlayersInLeague(int userLeagueId){
        UserLeague userLeague = headToHeadUserLeagueRepo.findUserLeagueById(userLeagueId).orElseThrow(() -> new ResourceNotFoundException("League not present with Id: "+userLeagueId));
        List<UserLeagueStatus> userLeagueStatusList = userLeagueStatusRepo.findAllByUserLeague(userLeague);
        Set<Player> players =  new LinkedHashSet<>();

        for(UserLeagueStatus u: userLeagueStatusList){

            if(u.getMyUser().getFantasyTeam() == null){
                continue;
            }

            FantasyTeam fantasyTeam = u.getMyUser().getFantasyTeam();
            List<PlayerTeamStatus> playerTeamStatusList = playerTeamStatusRepo.findAllByFantasyTeamId(fantasyTeam.getId());
            players.addAll(playerTeamStatusList.stream().map(PlayerTeamStatus::getPlayer).collect(Collectors.toSet())); //Stream all, convert to type player collection and add to set
        }


        List<Player> sortedPlayerList = players.stream().sorted(Comparator.comparing(Player::getFantasyPoints).reversed()).limit(10).toList();

        return sortedPlayerList.stream().map(playerService::playerToDto).toList();

    }




}

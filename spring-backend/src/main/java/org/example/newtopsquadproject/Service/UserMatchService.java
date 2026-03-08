package org.example.newtopsquadproject.Service;


import org.example.newtopsquadproject.Exceptions.ResourceNotFoundException;
import org.example.newtopsquadproject.Model.DTO.UserMatchDTO;
import org.example.newtopsquadproject.Model.Enums.HeadToHeadLeagueType;
import org.example.newtopsquadproject.Model.FantasyLeagues.HeadToHeadUserLeague;
import org.example.newtopsquadproject.Model.FantasyLeagues.LeagueRound;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeagueStatus;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserMatch;
import org.example.newtopsquadproject.Model.Players.MyUser;
import org.example.newtopsquadproject.Repository.FantasyLeagues.HeadToHeadUserLeagueRepo;
import org.example.newtopsquadproject.Repository.FantasyLeagues.LeagueRoundRepo;
import org.example.newtopsquadproject.Repository.FantasyLeagues.UserLeagueStatusRepo;
import org.example.newtopsquadproject.Repository.FantasyLeagues.UserMatchRepo;
import org.example.newtopsquadproject.Repository.MyUserRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service

public class UserMatchService {
    private final UserLeagueStatusRepo userLeagueStatusRepo;
    private final UserMatchRepo userMatchRepo;
    private final HeadToHeadUserLeagueRepo headToHeadUserLeagueRepo;
    private final HeadToHeadLeagueService headToHeadLeagueService;

    private final LeagueRoundRepo leagueRoundRepo;

    private final MyUserRepo myUserRepo;

    public UserMatchService(UserLeagueStatusRepo userLeagueStatusRepo, UserMatchRepo userMatchRepo, HeadToHeadUserLeagueRepo headToHeadUserLeagueRepo, HeadToHeadLeagueService headToHeadLeagueService, LeagueRoundRepo leagueRoundRepo, MyUserRepo myUserRepo) {
        this.userLeagueStatusRepo = userLeagueStatusRepo;
        this.userMatchRepo = userMatchRepo;
        this.headToHeadUserLeagueRepo = headToHeadUserLeagueRepo;
        this.headToHeadLeagueService = headToHeadLeagueService;
        this.leagueRoundRepo = leagueRoundRepo;
        this.myUserRepo = myUserRepo;
    }

    public List<UserMatchDTO> decidedUserInMatches(List<UserMatch> userMatches, int userId){
        List<UserMatchDTO> userMatchDTOList = new ArrayList<>();
        for(UserMatch u : userMatches){
            UserMatchDTO userMatchDTO = userMatchToDto(u);
            if(u.getHomeUserLeagueStatus().getMyUser().getId() == userId){
                userMatchDTO.setUserHome(true);
            } else if(u.getAwayUserLeagueStatus().getMyUser().getId() == userId){
                userMatchDTO.setUserHome(false);
            }
            userMatchDTOList.add(userMatchDTO);
        }

        return userMatchDTOList;
    }

    public List<UserMatchDTO> getAllMatchesInLeague(int leagueId){
        headToHeadUserLeagueRepo.findById(leagueId).orElseThrow(() -> new ResourceNotFoundException("League not found with ID: " +leagueId));
        List<UserMatch> userMatches = userMatchRepo.findAllByLeagueRoundUserLeagueId(leagueId);
        List<UserMatchDTO> userMatchDTOList = new ArrayList<>();
        if(userMatches.isEmpty()){
            return userMatchDTOList;
        }

        for(UserMatch u : userMatches){
            UserMatchDTO userMatchDTO = userMatchToDto(u);
            userMatchDTOList.add(userMatchDTO);
        }

        return userMatchDTOList;
    }

    public List<UserMatchDTO> getAllMatchesForUser(int userId){
        MyUser myUser = myUserRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: "+userId));
        List<UserMatch> userMatches = userMatchRepo.findAllContainingFantasyTeamId(myUser.getFantasyTeam().getId());
        List<UserMatchDTO> userMatchDTOList = new ArrayList<>();

        if(!userMatches.isEmpty()){
            userMatchDTOList = decidedUserInMatches(userMatches, userId);
        }
        return userMatchDTOList;
    }

    public List<UserMatchDTO> getAllMatchesForUserInLeague(int userId, int leagueId){
        MyUser myUser = myUserRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: "+userId));
        HeadToHeadUserLeague userLeague = headToHeadUserLeagueRepo.findById(leagueId).orElseThrow(() -> new ResourceNotFoundException("League not found with ID: " +leagueId));
        Optional<UserLeagueStatus> userLeagueStatusOptional = userLeagueStatusRepo.findByUserLeagueIdAndMyUserId(leagueId, userId);
        List<UserMatchDTO> userMatchDTOList = new ArrayList<>();

        if(userLeagueStatusOptional.isPresent()){
            int id = userLeagueStatusOptional.get().getId();
            List<UserMatch> userMatches = userMatchRepo.findAllByUserLeagueStatusId(id);
            userMatchDTOList = decidedUserInMatches(userMatches, userId);
        }
        return userMatchDTOList;
    }

    public UserMatchDTO userMatchToDto(UserMatch userMatch){
        UserMatchDTO userMatchDTO = new UserMatchDTO();
        userMatchDTO.setId(userMatch.getId());
        userMatchDTO.setHome(userMatch.getHomeUserLeagueStatus().getMyUser().getUsername());
        userMatchDTO.setAway(userMatch.getAwayUserLeagueStatus().getMyUser().getUsername());
        userMatchDTO.setHomeScore(userMatch.getHomePoints());
        userMatchDTO.setAwayScore(userMatch.getAwayPoints());
        userMatchDTO.setLeague(userMatch.getHeadToHeadUserLeague().getName());
        userMatchDTO.setOngoing(userMatch.isOngoing());
        userMatchDTO.setCompleted(userMatch.isComplete());
        return userMatchDTO;
    }

    public void allocateUserMatchesRoundRobin(int leagueId){
        HeadToHeadUserLeague league;
        league =  headToHeadUserLeagueRepo.findById(leagueId).orElseThrow(() -> new ResourceNotFoundException("League not found with ID: " +leagueId));
        List<UserMatch> userMatches = new ArrayList<>();
        List<UserLeagueStatus> userLeagueStatuses = league.getUserLeagueStatuses(); //May cause issue due to lazy fetching

        List<LeagueRound> leagueRounds = new ArrayList<>();

        for(int i=0; i<userLeagueStatuses.size()-1; i++){
            LeagueRound leagueRound = new LeagueRound(i+1, league);
            leagueRound.setCurrent(i+1==1);
            leagueRounds.add(leagueRound);
        }
        
        league.setCurrentRound(leagueRounds.get(0));

        if(userLeagueStatuses.size()%2 != 0){
            userLeagueStatuses.add(null);
        }

        List<UserLeagueStatus> rotatingList = new ArrayList<>(userLeagueStatuses);
        rotatingList.remove(0);

        for(int round=1; round<userLeagueStatuses.size(); round++){
            List<UserLeagueStatus> fixedList = new ArrayList<>(List.of(userLeagueStatuses.get(0)));
            fixedList.addAll(rotatingList);

            for(int i=0; i<userLeagueStatuses.size()/2; i++){
                UserMatch userMatch = new UserMatch(league, fixedList.get(i), fixedList.get(userLeagueStatuses.size()-1-i), leagueRounds.get(round-1));
                userMatch.setOngoing(round==1);
                userMatches.add(userMatch);
            }
            Collections.rotate(rotatingList, +1);
        }

        league.addMatches(userMatches);
        leagueRoundRepo.saveAll(leagueRounds);
        headToHeadUserLeagueRepo.save(league);
        userMatchRepo.saveAll(userMatches);
        userLeagueStatusRepo.saveAll(userLeagueStatuses);
    }


    public void updateAllRandomLeagues(){
        List<HeadToHeadUserLeague> randomLeagues = headToHeadUserLeagueRepo.findAllByHeadToHeadLeagueType(HeadToHeadLeagueType.RANDOM);
        for(HeadToHeadUserLeague league : randomLeagues){
            if(league.getUserLeagueStatuses().size() <= 1){
                continue;
            } else{
                allocateUserMatchesRandomly(league);
            }
        }
    }



    public void allocateUserMatchesRandomly(HeadToHeadUserLeague league){
        if(league.getHeadToHeadLeagueType() != HeadToHeadLeagueType.RANDOM){
            throw new RuntimeException("Head to head League with ID: "+ league.getId() + " is not round robin league");
        }
        LeagueRound leagueRound = headToHeadLeagueService.createLeagueRound(league);

        if(leagueRound == null){
            System.out.println("Can not create new round for league with ID: " + league.getId());
            return;
        }

        List<UserMatch> userMatches = new ArrayList<>();

        List<UserLeagueStatus> userLeagueStatuses = league.getUserLeagueStatuses(); //May cause issue due to lazy fetching
        List<UserLeagueStatus> copy = new ArrayList<>(userLeagueStatuses);
        Collections.shuffle(copy);
        Random rand = new Random();

        while(copy.size() > 1){
            UserLeagueStatus awayUser = selectAndRemoveRandomUser(copy, rand);
            UserLeagueStatus homeUser = selectAndRemoveRandomUser(copy, rand);
            UserMatch userMatch = new UserMatch(league, homeUser, awayUser, leagueRound);
            userMatches.add(userMatch);
        }

        if(!copy.isEmpty()){
            UserLeagueStatus byeUser = copy.get(0);
            userMatches.add(new UserMatch(league, byeUser, null, leagueRound)); //As they can't play against anybody
        }
        league.addMatches(userMatches);
        leagueRoundRepo.save(leagueRound);
        headToHeadUserLeagueRepo.save(league);
        userMatchRepo.saveAll(userMatches);
    }

    private UserLeagueStatus selectAndRemoveRandomUser(List<UserLeagueStatus> copy, Random rand){
        int randomIndex = rand.nextInt(copy.size());
        UserLeagueStatus selectedUser = copy.get(randomIndex);

        UserLeagueStatus temp = copy.get(copy.size()-1);
        copy.set(copy.size()-1, selectedUser);
        copy.set(randomIndex, temp);

        copy.remove(copy.size()-1);
        return selectedUser;
    }

    public void updateCurrentRoundForRoundRobinLeague(HeadToHeadUserLeague headToHeadUserLeague){
        if(headToHeadUserLeague.getHeadToHeadLeagueType() != HeadToHeadLeagueType.ROUND_ROBIN){
            throw new RuntimeException("Head to head League with ID: "+ headToHeadUserLeague.getId() + " is not round robin league");
        }

        List<LeagueRound> leagueRounds = leagueRoundRepo.findAllByUserLeagueId(headToHeadUserLeague.getId());


        if(leagueRounds.isEmpty()){
            allocateUserMatchesRoundRobin(headToHeadUserLeague.getId());
            return;
        }


        if(headToHeadLeagueService.checkIfCurrentRoundIsFinished(headToHeadUserLeague)){

            int pos = leagueRounds.indexOf(headToHeadUserLeague.getCurrentRound());

            if(pos == leagueRounds.size()-1){ //If the current round is the last
                System.out.println("No more round robin rounds for head to head league: " + headToHeadUserLeague.getId());
                headToHeadUserLeague.setHeadToHeadLeagueType(HeadToHeadLeagueType.RANDOM); //No more round-robin
            }

            LeagueRound nextRound = leagueRounds.get(pos+1);

            //Calculate results of user matches
            List<UserMatch> userMatches = userMatchRepo.findAllByLeagueRound(headToHeadUserLeague.getCurrentRound());
            headToHeadLeagueService.decideUserMatchResults(userMatches);

            nextRound.setStartedAt(LocalDateTime.now());
            List<UserMatch> userMatchesNextRound = userMatchRepo.findAllByLeagueRound(nextRound);
            for(UserMatch u : userMatchesNextRound){
                u.setOngoing(true);
            }
            userMatchRepo.saveAll(userMatchesNextRound);

            headToHeadUserLeague.setCurrentRound(nextRound);
            leagueRoundRepo.saveAll(leagueRounds);
            headToHeadUserLeagueRepo.save(headToHeadUserLeague);
        }
    }

    public void updateAllRoundRobinLeagues(){
        List<HeadToHeadUserLeague> roundRobinLeagues = headToHeadUserLeagueRepo.findAllByHeadToHeadLeagueType(HeadToHeadLeagueType.ROUND_ROBIN);
        for(HeadToHeadUserLeague league : roundRobinLeagues){
            if(league.getUserLeagueStatuses().size() <= 1){
                continue;
            } else{
                updateCurrentRoundForRoundRobinLeague(league);
            }
        }
    }
}

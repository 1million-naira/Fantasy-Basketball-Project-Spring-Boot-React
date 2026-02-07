package org.example.newtopsquadproject.Component;

import org.example.newtopsquadproject.Model.DTO.FantasyLeagueDTO;
import org.example.newtopsquadproject.Model.Enums.HeadToHeadLeagueType;
import org.example.newtopsquadproject.Model.Enums.LeagueTypeEnum;
import org.example.newtopsquadproject.Model.FantasyLeagues.FantasyTeam;
import org.example.newtopsquadproject.Model.FantasyLeagues.HeadToHeadUserLeague;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeague;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeagueStatus;
import org.example.newtopsquadproject.Model.Login.RegistrationRequestDto;
import org.example.newtopsquadproject.Model.Players.MyUser;
import org.example.newtopsquadproject.Service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(2)
public class ApplicationFlow implements CommandLineRunner {
    private final UserMatchService userMatchService;

    private final FantasyTeamService fantasyTeamService;

    private final PlayerService playerService;

    private final MyUserService myUserService;

    private final BoxScoreService boxScoreService;

    private final HeadToHeadLeagueService headToHeadLeagueService;

    private final UserLeagueService userLeagueService;

    private final AppSettingsService appSettingsService;

    public ApplicationFlow(UserMatchService userMatchService, FantasyTeamService fantasyTeamService, PlayerService playerService, MyUserService myUserService, BoxScoreService boxScoreService, HeadToHeadLeagueService headToHeadLeagueService, UserLeagueService userLeagueService, AppSettingsService appSettingsService) {
        this.userMatchService = userMatchService;
        this.fantasyTeamService = fantasyTeamService;
        this.playerService = playerService;
        this.myUserService = myUserService;
        this.boxScoreService = boxScoreService;
        this.headToHeadLeagueService = headToHeadLeagueService;
        this.userLeagueService = userLeagueService;
        this.appSettingsService = appSettingsService;
    }

    @Override
    public void run(String... args) throws Exception {

        appSettingsService.createAppSetting("transferMarketStatus", "open");
        System.out.println("Transfer Market opened");


        MyUser user1 = myUserService.registerUser(new RegistrationRequestDto("first", "Zoro@gmail.com", "st56Th7*aj"));
        MyUser user2 = myUserService.registerUser(new RegistrationRequestDto("second", "Sanji@gmail.com", "tyd7896!."));
        MyUser user3 = myUserService.registerUser(new RegistrationRequestDto("third", "Jinbei@gmail.com", "tJin8sr7bei345*."));
        MyUser user4 = myUserService.registerUser(new RegistrationRequestDto("fourth", "Brook@gmail.com", "bJ7ns8ei345!!!."));
        MyUser user5 = myUserService.registerUser(new RegistrationRequestDto("fifth", "Franky@gmail.com", "fR8bs9Jl741!!?"));
        MyUser user6 = myUserService.registerUser(new RegistrationRequestDto("sixth", "Chopper@gmail.com", "Ch8bs9nR941!;?"));
        MyUser user7 = myUserService.registerUser(new RegistrationRequestDto("seventh", "Nami@gmail.com", "Nami76s9yRr571!;"));
        MyUser admin = myUserService.registerAdmin(new RegistrationRequestDto("admin", "admin@gmail.com", "admin452hdf!:kha"));

        HeadToHeadUserLeague testHeadToHead = new HeadToHeadUserLeague();
        testHeadToHead.setCode("x56B4l");
        testHeadToHead.setName("testLeague");
        testHeadToHead.setLeagueTypeEnum(LeagueTypeEnum.ROTO);
        testHeadToHead.setCreatedAt();
        testHeadToHead.setCreatedBy(user2);

        headToHeadLeagueService.save(testHeadToHead);
        myUserService.save(user2);


        UserLeague headToHead = userLeagueService.createLeague(new FantasyLeagueDTO("BEST", "H2H", "RR", "private"), user1);
        UserLeague testH2HLeague = userLeagueService.createLeague(new FantasyLeagueDTO("Best2", "H2H", "rr", "Private"), user3);
        UserLeague testRotoLeague = userLeagueService.createLeague(new FantasyLeagueDTO("Best3", "ROTO", "Public"), user4);

        FantasyTeam fantasyTeam = fantasyTeamService.createFantasyTeam(user1.getId(), "Team1");
        fantasyTeam = fantasyTeamService.addPlayersToTeam(fantasyTeam.getId(), playerService.selectRandomPlayers(7));

        FantasyTeam fantasyTeam2 = fantasyTeamService.createFantasyTeam(user2.getId(), "Team2");
        fantasyTeam2 = fantasyTeamService.addPlayersToTeam(fantasyTeam2.getId(), List.of(332, 243, 71, 117, 110, 41, 329, 270));

        FantasyTeam fantasyTeam3 = fantasyTeamService.createFantasyTeam(user3.getId(), "Team3");
        fantasyTeam3 = fantasyTeamService.addPlayersToTeam(fantasyTeam3.getId(), List.of(75, 68, 88, 117, 329, 41, 110, 260));

        FantasyTeam fantasyTeam4 = fantasyTeamService.createFantasyTeam(user4.getId(), "Team4");
        fantasyTeam4 = fantasyTeamService.addPlayersToTeam(fantasyTeam4.getId(), playerService.selectRandomPlayers(7));

        FantasyTeam fantasyTeam5 = fantasyTeamService.createFantasyTeam(user5.getId(), "Team5");
        fantasyTeam5 = fantasyTeamService.addPlayersToTeam(fantasyTeam5.getId(), playerService.selectRandomPlayers(7));

        FantasyTeam fantasyTeam6 = fantasyTeamService.createFantasyTeam(user6.getId(), "Team6");
        fantasyTeam6 = fantasyTeamService.addPlayersToTeam(fantasyTeam6.getId(), playerService.selectRandomPlayers(7));

        UserLeagueStatus userLeagueStatus = userLeagueService.joinLeague(user2.getId(), headToHead.getId());
        UserLeagueStatus userLeagueStatus2 = userLeagueService.joinLeague(user3.getId(), headToHead.getId());
        UserLeagueStatus userLeagueStatus3 = userLeagueService.joinLeague(user4.getId(), headToHead.getId());
        UserLeagueStatus userLeagueStatus4 = userLeagueService.joinLeague(user5.getId(), headToHead.getId());
        UserLeagueStatus userLeagueStatus5 = userLeagueService.joinLeague(user6.getId(), headToHead.getId());

        userMatchService.allocateUserMatchesRoundRobin(headToHead.getId()); //Fix Eager Fetching
    }
}

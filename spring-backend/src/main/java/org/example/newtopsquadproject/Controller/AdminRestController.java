package org.example.newtopsquadproject.Controller;

import org.example.newtopsquadproject.Model.DTO.FantasyLeagueDTO;
import org.example.newtopsquadproject.Model.DTO.UserReportDTO;
import org.example.newtopsquadproject.Model.Players.MyUser;
import org.example.newtopsquadproject.Security.UserPrincipal;
import org.example.newtopsquadproject.Service.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/admin")
public class AdminRestController {

    private final BoxScoreService boxScoreService;

    private final HeadToHeadLeagueService headToHeadLeagueService;

    private final UserMatchService userMatchService;

    private final MyUserService myUserService;

    private final UserLeagueService userLeagueService;

    private final ReportService reportService;

    private final AppSettingsService appSettingsService;

    public AdminRestController(BoxScoreService boxScoreService, HeadToHeadLeagueService headToHeadLeagueService, UserMatchService userMatchService, MyUserService myUserService, UserLeagueService userLeagueService, ReportService reportService, AppSettingsService appSettingsService) {
        this.boxScoreService = boxScoreService;
        this.headToHeadLeagueService = headToHeadLeagueService;
        this.userMatchService = userMatchService;
        this.myUserService = myUserService;
        this.userLeagueService = userLeagueService;
        this.reportService = reportService;
        this.appSettingsService = appSettingsService;
    }

    @PostMapping("/boxscores")
    public ResponseEntity<?> readBoxScores(){
        boxScoreService.readBoxScore();
        return new ResponseEntity<>("Box scores have been read", HttpStatus.CREATED);
    }

    @PutMapping("/leagues")
    public ResponseEntity<?> updateLeagues(){
        userMatchService.updateAllRoundRobinLeagues();
        userMatchService.updateAllRandomLeagues();
        return new ResponseEntity<>("Leagues have been updated", HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<MyUser>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String search
    ){
        Page<MyUser> myUserPage = myUserService.findAll(page, size, sortBy, direction, search);
        return new ResponseEntity<>(myUserPage, HttpStatus.OK);
    }

    @GetMapping("/leagues")
    public ResponseEntity<Page<FantasyLeagueDTO>> getHeadToHeadLeaguePage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String search
    ){
        var principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<FantasyLeagueDTO> fantasyLeagueDTOPage = userLeagueService.getHeadToHeadLeaguePage(page, size, sortBy, direction, principal.getUserId(), search);
        return new ResponseEntity<>(fantasyLeagueDTOPage, HttpStatus.OK);
    }

    @GetMapping("/reports")
    public ResponseEntity<Page<UserReportDTO>> getUserReportPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "pending") String reportStatus,
            @RequestParam(required = false) String search
    ){
        Page<UserReportDTO> userReportDTOPage = reportService.getUserReportPage(page, size, sortBy, direction, reportStatus, search);
        return new ResponseEntity<>(userReportDTOPage, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getAppStatus(){
        Map<String, Object> status = new HashMap<>();
        status.put("userCount", myUserService.findAll().size());
        status.put("leagueCount", userLeagueService.findAll().size());
        status.put("reportsCount", reportService.findAll().size());
        return new ResponseEntity<>(status, HttpStatus.OK);
    }


    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int userId){
        String name = myUserService.deleteUser(userId);
        return new ResponseEntity<>("User with email: " + name + " has been deleted", HttpStatus.OK);
    }

    @DeleteMapping("/league/{id}")
    public ResponseEntity<?> deleteLeague(@PathVariable("id") int leagueId){
        userLeagueService.deleteLeague(leagueId);
        return new ResponseEntity<>("League with id: " + leagueId + " has been deleted", HttpStatus.OK);
    }

    @PutMapping("/message/resolution/{id}")
    public ResponseEntity<String> resolveMessage(@PathVariable("id") int reportId){
        return new ResponseEntity<>(reportService.resolveReport(reportId), HttpStatus.OK);
    }

    @PutMapping("/user/{id}/warning")
    public ResponseEntity<String> warnUser(@PathVariable("id") int userId){
        return new ResponseEntity<>(reportService.warnUser(userId), HttpStatus.OK);
    }

    @PutMapping("/transferMarket")
    public ResponseEntity<String> moderateTransferMarket(@RequestParam String status){
        if(status.equalsIgnoreCase("open") || status.equalsIgnoreCase("closed")){
            appSettingsService.updateAppSetting("transferMarketStatus", status.toLowerCase());

            return new ResponseEntity<>("Transfer market is successfully " + status, HttpStatus.OK);
        }
        return new ResponseEntity<>("There is no state: " + status + " for the transfer market", HttpStatus.BAD_REQUEST);
    }
}

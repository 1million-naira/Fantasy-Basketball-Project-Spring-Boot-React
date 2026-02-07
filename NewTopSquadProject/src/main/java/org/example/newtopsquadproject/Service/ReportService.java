package org.example.newtopsquadproject.Service;

import org.example.newtopsquadproject.Exceptions.ResourceNotFoundException;
import org.example.newtopsquadproject.Exceptions.ValidationException;
import org.example.newtopsquadproject.Model.DTO.UserReportDTO;
import org.example.newtopsquadproject.Model.FantasyLeagues.LeagueMessage;
import org.example.newtopsquadproject.Model.Players.MyUser;
import org.example.newtopsquadproject.Model.UserReport;
import org.example.newtopsquadproject.Repository.FantasyLeagues.LeagueMessageRepo;
import org.example.newtopsquadproject.Repository.MyUserRepo;
import org.example.newtopsquadproject.Repository.UserReportRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {
    private final UserReportRepo userReportRepo;

    private final LeagueMessageRepo leagueMessageRepo;

    private final MyUserRepo myUserRepo;

    public ReportService(UserReportRepo userReportRepo, LeagueMessageRepo leagueMessageRepo, MyUserRepo myUserRepo) {
        this.userReportRepo = userReportRepo;
        this.leagueMessageRepo = leagueMessageRepo;
        this.myUserRepo = myUserRepo;
    }

    public List<UserReport> findAll(){
        return userReportRepo.findAll();
    }


    public UserReportDTO createMessageReport(int messageId, int userReportedById, String description){
        LeagueMessage leagueMessage = leagueMessageRepo.findById(messageId).orElseThrow(() -> new ResourceNotFoundException("Message with ID: " + messageId + " not found"));

        if(leagueMessage.getUserLeagueStatus().getMyUser().getId() == userReportedById){
            throw new ValidationException("A user can not report themselves");
        }

        int reportedUserId = leagueMessage.getUserLeagueStatus().getMyUser().getId();
        MyUser myUser = myUserRepo.findById(userReportedById).orElseThrow(() -> new ResourceNotFoundException("User not found with Id: " + userReportedById));
        MyUser reportedUser = myUserRepo.findById(reportedUserId).orElseThrow(() -> new ResourceNotFoundException("User not found with Id: " + reportedUserId));

        UserReport userReport = new UserReport();
        userReport.setCreatedBy(myUser);
        userReport.setReported(reportedUser);
        userReport.setLeagueMessage(leagueMessage);
        userReport.setDescription(description);

        userReportRepo.save(userReport);

        return userReportToDto(userReport);
    }

    public String resolveReport(int reportId){
        UserReport userReport = userReportRepo.findById(reportId).orElseThrow(() -> new ResourceNotFoundException("Report with ID: " + reportId + " not found"));
        userReport.setResolved(true);
        userReportRepo.save(userReport);
        return "Report with ID: " + reportId + " has been resolved";
    }

    public String warnUser(int userId){
        MyUser myUser = myUserRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with Id: " + userId));
        myUser.addWarnings();
        myUserRepo.save(myUser);

        return "User with ID: " + userId + " and username: " + myUser.getUsername() + " now has " + myUser.getWarnings() + " warnings";

    }


    public UserReportDTO userReportToDto(UserReport userReport){
        UserReportDTO userReportDTO = new UserReportDTO();
        userReportDTO.setId(userReport.getId());
        userReportDTO.setUserId(userReport.getCreatedBy().getId());
        userReportDTO.setUsername(userReport.getCreatedBy().getUsername());
        userReportDTO.setAgainstId(userReport.getReported().getId());
        userReportDTO.setAgainstName(userReport.getReported().getUsername());

        if(userReport.getLeagueMessage().getMessage() != null){
            userReportDTO.setMessage(userReport.getLeagueMessage().getMessage());
        } else{
            userReportDTO.setMessage("");
        }

        userReportDTO.setDescription(userReport.getDescription());

        return userReportDTO;
    }


    public Page<UserReportDTO> getUserReportPage(int page, int size, String sortBy, String direction, String reportStatus, String search){
        Sort sort = direction.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        if(!(search==null)){
            if(!search.isBlank()){
                int searchId = 0;
                try{
                    searchId = Integer.parseInt(search.trim());
                    Page<UserReport> userReportPage = userReportRepo.findAllByUserId(pageRequest, searchId);
                    return userReportPage.map(this::userReportToDto); //Lambda method reference
                } catch(Exception e){
                    return Page.empty();
                }
            }
        }

        boolean resolved = reportStatus.equals("Resolved");
        Page<UserReport> userReportPage = userReportRepo.findAllByResolvedStatus(pageRequest, resolved);
        return userReportPage.map(this::userReportToDto); //Lambda method reference
    }



}

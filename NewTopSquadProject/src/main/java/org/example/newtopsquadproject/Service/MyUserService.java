package org.example.newtopsquadproject.Service;

import org.example.newtopsquadproject.Exceptions.ResourceNotFoundException;
import org.example.newtopsquadproject.Exceptions.ValidationException;
import org.example.newtopsquadproject.Model.DTO.UserDTO;
import org.example.newtopsquadproject.Model.FantasyLeagues.FantasyTeam;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeague;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeagueStatus;
import org.example.newtopsquadproject.Model.Login.RegistrationRequestDto;
import org.example.newtopsquadproject.Model.Players.MyUser;
import org.example.newtopsquadproject.Model.Players.PlayerTeamStatus;
import org.example.newtopsquadproject.Repository.FantasyLeagues.FantasyTeamRepo;
import org.example.newtopsquadproject.Repository.FantasyLeagues.HeadToHeadUserLeagueRepo;
import org.example.newtopsquadproject.Repository.FantasyLeagues.UserLeagueStatusRepo;
import org.example.newtopsquadproject.Repository.MyUserRepo;
import org.example.newtopsquadproject.Repository.Players.PlayerTeamStatusRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MyUserService {

    private final MyUserRepo myUserRepo;
    private final FantasyTeamRepo fantasyTeamRepo;

    private final PlayerTeamStatusRepo playerTeamStatusRepo;

    private final UserLeagueStatusRepo userLeagueStatusRepo;

    private final HeadToHeadUserLeagueRepo headToHeadUserLeagueRepo;
    private final PasswordEncoder passwordEncoder;

    public MyUserService(MyUserRepo myUserRepo, FantasyTeamRepo fantasyTeamRepo, PlayerTeamStatusRepo playerTeamStatusRepo, UserLeagueStatusRepo userLeagueStatusRepo, HeadToHeadUserLeagueRepo headToHeadUserLeagueRepo, PasswordEncoder passwordEncoder) {
        this.myUserRepo = myUserRepo;
        this.fantasyTeamRepo = fantasyTeamRepo;
        this.playerTeamStatusRepo = playerTeamStatusRepo;
        this.userLeagueStatusRepo = userLeagueStatusRepo;
        this.headToHeadUserLeagueRepo = headToHeadUserLeagueRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public MyUser registerUser(RegistrationRequestDto registrationRequestDto){

        String email = registrationRequestDto.getEmail();

        if(myUserRepo.findByEmail(email).isPresent()){
            throw new ValidationException("Email: " + email + "already exists");
        }

        MyUser myUser = new MyUser();
        myUser.setUsername(registrationRequestDto.getUsername());
        myUser.setEmail(registrationRequestDto.getEmail());
        myUser.setPassword(passwordEncoder.encode(registrationRequestDto.getPassword()));
        myUser.setRole("ROLE_USER");
        return myUserRepo.save(myUser);
    }


    public MyUser registerAdmin(RegistrationRequestDto registrationRequestDto){
        String email = registrationRequestDto.getEmail();

        if(myUserRepo.findByEmail(email).isPresent()){
            throw new ValidationException("Email: " + email + "already exists");
        }

        MyUser admin = new MyUser();
        admin.setUsername(registrationRequestDto.getUsername());
        admin.setEmail(registrationRequestDto.getEmail());
        admin.setPassword(passwordEncoder.encode(registrationRequestDto.getPassword()));
        admin.setRole("ROLE_ADMIN");
        return myUserRepo.save(admin);

    }

    public MyUser findById(int id){
        return myUserRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with Id: " + id));
    }

    public Optional<MyUser> findByEmail(String email){
        return myUserRepo.findByEmail(email);
    }

    public List<MyUser> findAll(){
        return myUserRepo.findAll();
    }


    public Page<MyUser> findAll(int page, int size, String sortBy, String direction, String search){
        Sort sort = direction.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        if(!(search==null)){
            if(!search.isBlank()){
                int searchId = 0;
                try{
                    searchId = Integer.parseInt(search.trim());
                    return myUserRepo.findById(pageRequest, searchId);
                } catch(Exception e){
                    return Page.empty();
                }
            }
        }

        return myUserRepo.findAll(pageRequest);
    }

    public MyUser save(MyUser myUser){
        return myUserRepo.save(myUser);
    }


    public UserDTO userToDto(MyUser myUser){
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(myUser.getUsername());
        userDTO.setBudget(myUser.getBudget());

        return userDTO;
    }

    @Transactional
    public String deleteUser(int userId){

        MyUser myUser = myUserRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with Id: " + userId));
        String name = myUser.getEmail();

        List<UserLeagueStatus> userLeagueStatuses = userLeagueStatusRepo.findAllByMyUserId(userId);
        for(UserLeagueStatus u : userLeagueStatuses){
            u.setMyUser(null);
            u.setLeague(null);
        }
        userLeagueStatusRepo.saveAll(userLeagueStatuses);
        userLeagueStatusRepo.deleteAllByMyUserId(myUser.getId());


        Integer fantasyTeamId = myUser.getFantasyTeam() != null ? myUser.getFantasyTeam().getId() : null;
        if(fantasyTeamId != null){
            playerTeamStatusRepo.deleteByFantasyTeamIdWithJPQL(fantasyTeamId);
        }

        if(fantasyTeamId != null){
            playerTeamStatusRepo.deleteByFantasyTeamIdWithJPQL(fantasyTeamId);

            FantasyTeam fantasyTeam = fantasyTeamRepo.findById(fantasyTeamId).orElseThrow(() -> new ResourceNotFoundException("Fantasy Team not found with ID: " + fantasyTeamId));
            fantasyTeam.setMyUser(null);
            fantasyTeamRepo.save(fantasyTeam);
            myUser.setFantasyTeam(null);
            myUserRepo.save(myUser);
            fantasyTeamRepo.deleteByUserIdWithJPQL(userId);
        }
//        headToHeadUserLeagueRepo.setNullToLeagueByMyUserId(userId);
        myUser.getUserLeagues().clear();
        myUserRepo.save(myUser);
        myUserRepo.delete(myUser);

        return name;
    }

}

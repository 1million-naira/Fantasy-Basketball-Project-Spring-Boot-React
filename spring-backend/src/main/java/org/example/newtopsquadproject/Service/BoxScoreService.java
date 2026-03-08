package org.example.newtopsquadproject.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.newtopsquadproject.Exceptions.ResourceNotFoundException;
import org.example.newtopsquadproject.Model.FantasyLeagues.FantasyTeam;
import org.example.newtopsquadproject.Model.FantasyLeagues.PlayerScoring;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserMatch;
import org.example.newtopsquadproject.Model.Players.Player;
import org.example.newtopsquadproject.Model.Players.PlayerTeamStatus;
import org.example.newtopsquadproject.Model.ProLeagues.BoxScore;
import org.example.newtopsquadproject.Model.ProLeagues.PlayerBoxScore;
import org.example.newtopsquadproject.Model.ProLeagues.ProTeam;
import org.example.newtopsquadproject.Model.ProLeagues.TeamBoxScore;
import org.example.newtopsquadproject.Repository.FantasyLeagues.FantasyTeamRepo;
import org.example.newtopsquadproject.Repository.FantasyLeagues.PlayerScoringRepo;
import org.example.newtopsquadproject.Repository.FantasyLeagues.UserMatchRepo;
import org.example.newtopsquadproject.Repository.Players.PlayerRepo;
import org.example.newtopsquadproject.Repository.Players.PlayerTeamStatusRepo;
import org.example.newtopsquadproject.Repository.ProLeagues.BoxScoreRepo;
import org.example.newtopsquadproject.Repository.ProLeagues.ProTeamRepo;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BoxScoreService {
    private final BoxScoreRepo boxScoreRepo;

    private final PlayerRepo playerRepo;

    private final ProTeamRepo proTeamRepo;

    private final PlayerScoringRepo playerScoringRepo;

    private final PlayerTeamStatusRepo playerTeamStatusRepo;

    private final FantasyTeamRepo fantasyTeamRepo;

    private final UserMatchRepo userMatchRepo;

    public BoxScoreService(BoxScoreRepo boxScoreRepo, PlayerRepo playerRepo, ProTeamRepo proTeamRepo, PlayerScoringRepo playerScoringRepo, PlayerTeamStatusRepo playerTeamStatusRepo, FantasyTeamRepo fantasyTeamRepo, UserMatchRepo userMatchRepo) {
        this.boxScoreRepo = boxScoreRepo;
        this.playerRepo = playerRepo;
        this.proTeamRepo = proTeamRepo;
        this.playerScoringRepo = playerScoringRepo;
        this.playerTeamStatusRepo = playerTeamStatusRepo;
        this.fantasyTeamRepo = fantasyTeamRepo;
        this.userMatchRepo = userMatchRepo;
    }

    public void readPlayerBoxScore(List<PlayerBoxScore> playerBoxScores, BoxScore boxScore){
        for(PlayerBoxScore playerBoxScore : playerBoxScores){
            String name = playerBoxScore.getPlayer_name();
            Optional<Player> playerOptional = playerRepo.findByName(name);
            if(playerOptional.isEmpty()){
                System.out.println(name+ " was not found in database");
                continue;
            }
            playerBoxScore.setPlayer(playerOptional.get());
            playerBoxScore.setBoxScore(boxScore);
        }

    }

    public void readBoxScore(){
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<BoxScore> boxScoreTypeReference = new TypeReference<BoxScore>() {};

        try(InputStream inputStream = TypeReference.class.getResourceAsStream("/JSON/cbaTestBoxScore.json")){
            BoxScore boxScore = objectMapper.readValue(inputStream, boxScoreTypeReference);

            readPlayerBoxScore(boxScore.getAwayTeamPlayers(), boxScore);
            readPlayerBoxScore(boxScore.getHomeTeamPlayers(), boxScore);

            TeamBoxScore awayTeamBoxScore = boxScore.getAwayTeam();
            TeamBoxScore homeTeamBoxScore = boxScore.getHomeTeam();

            String team_name = awayTeamBoxScore.getTeam_name();
            Optional<ProTeam> awayTeamOptional= proTeamRepo.findByClubNameAndIdIsNotNull(team_name);
            if(awayTeamOptional.isEmpty()){
                throw new ResourceNotFoundException(team_name+ "was not found in team table");
            }


            team_name = homeTeamBoxScore.getTeam_name();
            Optional<ProTeam> homeTeamOptional= proTeamRepo.findByClubNameAndIdIsNotNull(team_name);
            if(homeTeamOptional.isEmpty()){
                throw new ResourceNotFoundException(team_name+ "was not found in team table");
            }

            boxScore.setAwayTeam(awayTeamBoxScore);
            awayTeamBoxScore.setProTeam(awayTeamOptional.get());

            boxScore.setHomeTeam(homeTeamBoxScore);
            homeTeamBoxScore.setProTeam(homeTeamOptional.get());

            List<PlayerBoxScore> playerBoxScores = new ArrayList<>(Stream.concat(boxScore.getAwayTeamPlayers().stream(), boxScore.getHomeTeamPlayers().stream()).toList());
            calculateFantasyPoints(playerBoxScores);

            boxScoreRepo.save(boxScore);
            System.out.println("Box score saved successfully");
        } catch(Exception e){
            System.out.println("Unable to read box score: " + e.getMessage());
        }
    }

    public void calculateFantasyPoints(List<PlayerBoxScore> playerBoxScores){
        List<Player> players = playerBoxScores.stream().map(PlayerBoxScore::getPlayer).toList();
        List<PlayerScoring> playerScoringList = new ArrayList<>();
        for(PlayerBoxScore p : playerBoxScores){
            PlayerScoring playerScoring = new PlayerScoring(p.getPlayer());
            playerScoring.setPtsScore((int) Math.round(0.5 * p.getPts()));
            playerScoring.setAstScore(2*p.getAst());
            playerScoring.setTrbScore(p.getTrb());
            playerScoring.setStlScore(4*p.getStl());
            playerScoring.setBlkScore(4*p.getBlk());
            playerScoring.setThreePointersScore(p.getFg3m());
            playerScoring.setFgmScore(2*p.getFgm());
            playerScoring.setFtmScore(p.getFtm());
            playerScoring.setFgaPenalty(p.getFga());
            playerScoring.setFtaPenalty(2*p.getFta());
            playerScoring.setTovPenalty(2*p.getTov());

            playerScoring.calcTotalFantasyScore();
            p.getPlayer().addFantasyPoints(playerScoring.getTotalScore());

            List<FantasyTeam> teams = playerTeamStatusRepo.findAllByPlayerId(p.getPlayer().getId()).stream().map(PlayerTeamStatus::getFantasyTeam).toList();
            for(FantasyTeam ft : teams){
                ft.addFantasyPoints(playerScoring.getTotalScore());
                List<UserMatch> userMatches = userMatchRepo.findAllOngoingContainingFantasyTeamId(ft.getId());

                for(UserMatch u : userMatches){
                    if(u.getHomeTeam().getId() == ft.getId()){
                        u.addHomePoints(playerScoring.getTotalScore());
                    }
                    else if (u.getAwayTeam().getId() == ft.getId()){
                        u.addAwayPoints(playerScoring.getTotalScore());
                    }
                }
                userMatchRepo.saveAll(userMatches);
            }
            fantasyTeamRepo.saveAll(teams);
            playerScoringList.add(playerScoring);
        }
        playerScoringRepo.saveAll(playerScoringList);
        playerRepo.saveAll(players);
    }
}

package org.example.newtopsquadproject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.newtopsquadproject.Model.FantasyLeagues.HeadToHeadUserLeague;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeague;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeagueStatus;
import org.example.newtopsquadproject.Model.Players.MyUser;
import org.example.newtopsquadproject.Model.Players.Player;
import org.example.newtopsquadproject.Model.Players.PlayerInfo;
import org.example.newtopsquadproject.Model.ProLeagues.ProTeam;
import org.example.newtopsquadproject.Repository.*;
import org.example.newtopsquadproject.Repository.FantasyLeagues.HeadToHeadUserLeagueRepo;
import org.example.newtopsquadproject.Repository.FantasyLeagues.UserLeagueStatusRepo;
import org.example.newtopsquadproject.Service.PlayerService;
import org.example.newtopsquadproject.Service.PlayerInfoService;
import org.example.newtopsquadproject.Service.ProTeamService;
import org.example.newtopsquadproject.Service.UserMatchService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootApplication
public class NewTopSquadProjectApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NewTopSquadProjectApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

}

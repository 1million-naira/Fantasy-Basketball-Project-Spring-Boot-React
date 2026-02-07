package org.example.newtopsquadproject.DBTests;

import org.example.newtopsquadproject.Model.FantasyLeagues.FantasyTeam;
import org.example.newtopsquadproject.Model.Enums.PlayerRoleEnum;
import org.example.newtopsquadproject.Model.Players.PlayerTeamStatus;
import org.example.newtopsquadproject.Repository.FantasyLeagues.FantasyTeamRepo;
import org.example.newtopsquadproject.Repository.Players.PlayerTeamStatusRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class FantasyTeamRepoTest {

    @Autowired
    private FantasyTeamRepo fantasyTeamRepo;

    @Autowired
    private PlayerTeamStatusRepo playerTeamStatusRepo;


    @BeforeEach
    public void setUp(){
        FantasyTeam fantasyTeam = new FantasyTeam();

        PlayerTeamStatus status = new PlayerTeamStatus();
        status.setFantasyTeam(fantasyTeam);
        status.setPlayerRoleEnum(PlayerRoleEnum.NORMAL);

        PlayerTeamStatus status2 = new PlayerTeamStatus();
        status2.setFantasyTeam(fantasyTeam);
        status.setPlayerRoleEnum(PlayerRoleEnum.NORMAL);

        PlayerTeamStatus status3 = new PlayerTeamStatus();
        status3.setFantasyTeam(fantasyTeam);
        status.setPlayerRoleEnum(PlayerRoleEnum.CAPTAIN);

        fantasyTeam.setPlayerTeamStatuses(List.of(status, status2, status3));

        FantasyTeam team = fantasyTeamRepo.save(fantasyTeam);
    }

    @Test
    public void testFindAll(){
        List<FantasyTeam> teams = fantasyTeamRepo.findAll();
        List<PlayerTeamStatus> playerTeamStatuses = playerTeamStatusRepo.findAll();

        assertThat(teams).isNotEmpty();
        assertThat(playerTeamStatuses).isNotEmpty();
    }

    @Test
    public void testFindAllWithPlayerTeamStatuses(){
        //Test to see if left join fetch query works due to lazy fetching
        List<FantasyTeam> teamsWithPlayerTeamStatuses = fantasyTeamRepo.findAllWithPlayerTeamStatuses();
        assertThat(teamsWithPlayerTeamStatuses).isNotEmpty();

        List<PlayerTeamStatus> playerTeamStatuses = teamsWithPlayerTeamStatuses.get(0).getPlayerTeamStatuses();
        assertThat(playerTeamStatuses).isNotEmpty();
    }


    @Test
    public void testFindByIdWithPlayerTeamStatuses(){
        List<FantasyTeam> teams = fantasyTeamRepo.findAll();
        Optional<FantasyTeam> fantasyTeamOptional = fantasyTeamRepo.findByIdWithPlayerTeamStatuses(teams.get(0).getId());
        assertThat(fantasyTeamOptional).isPresent();

        assert(fantasyTeamOptional.isPresent());
        FantasyTeam fantasyTeam = fantasyTeamOptional.get();

        List<PlayerTeamStatus> playerTeamStatuses = fantasyTeam.getPlayerTeamStatuses();
        assertThat(playerTeamStatuses).isNotEmpty();
    }
}

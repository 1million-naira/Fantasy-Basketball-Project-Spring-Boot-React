package org.example.newtopsquadproject.Model.ProLeagues;

import jakarta.persistence.*;
import org.example.newtopsquadproject.Model.Enums.ProLeagueEnum;

import java.util.List;

@Entity
public class ProLeague {
    @Id
    @GeneratedValue
    private int id;


    @OneToMany(mappedBy = "proLeague", fetch = FetchType.LAZY)
    private List<ProTeam> teamList;

    @OneToMany(mappedBy = "proLeague",  fetch = FetchType.LAZY)
    private List<ProGame> proGames;


//    @ManyToMany(mappedBy = "proLeagues",  fetch = FetchType.LAZY)
//    private List<UserLeague> userLeagues;

    private String name;

    @Enumerated(EnumType.STRING)
    private ProLeagueEnum proLeagueEnum;

}

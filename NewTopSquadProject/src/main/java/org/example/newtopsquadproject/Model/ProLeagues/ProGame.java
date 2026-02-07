package org.example.newtopsquadproject.Model.ProLeagues;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class ProGame {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name="pro_league_id")
    private ProLeague proLeague;

    @ManyToOne
    @JoinColumn(name="home_team_id")
    private ProTeam homeTeam;

    @ManyToOne
    @JoinColumn(name="away_team_id")
    private ProTeam awayTeam;


    private LocalDate date;
}

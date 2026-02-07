package org.example.newtopsquadproject.Model.ProLeagues;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Standings {
    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private ProLeague proLeague;

    @OneToMany
    private List<ProTeam> proTeams;


    public ProLeague getProLeague() {
        return proLeague;
    }

    public void setProLeague(ProLeague proLeague) {
        this.proLeague = proLeague;
    }

    public List<ProTeam> getProTeams() {
        return proTeams;
    }

    public void setProTeams(List<ProTeam> proTeams) {
        this.proTeams = proTeams;
    }
}

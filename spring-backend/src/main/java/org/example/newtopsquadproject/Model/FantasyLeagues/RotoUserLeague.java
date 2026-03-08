package org.example.newtopsquadproject.Model.FantasyLeagues;

import jakarta.persistence.Entity;
import org.example.newtopsquadproject.Model.FantasyLeagues.UserLeague;

@Entity
public class RotoUserLeague extends UserLeague {
    public RotoUserLeague() {}

    public RotoUserLeague(String name) {
        super(name);
    }


}

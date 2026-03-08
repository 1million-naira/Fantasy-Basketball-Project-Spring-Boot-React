package org.example.newtopsquadproject.Model.FantasyLeagues;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.example.newtopsquadproject.Model.Enums.LeagueTypeEnum;
import org.example.newtopsquadproject.Model.Players.MyUser;
import org.example.newtopsquadproject.Model.Enums.ProLeagueEnum;
import org.example.newtopsquadproject.Utils.RandomAlphaNumericString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserLeague {
    public UserLeague() {}
    public UserLeague(String name) {
        this.name = name;
        generateCode();
        setCreatedAt();
    }
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    @JoinColumn(name="user_id")
    private MyUser createdBy;
    @JsonIgnore
    @OneToMany(mappedBy = "userLeague", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<UserLeagueStatus> userLeagueStatuses;

    @OneToMany(mappedBy = "userLeague", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LeagueRound> leagueRounds;
    @Enumerated(EnumType.STRING)
    private LeagueTypeEnum leagueTypeEnum;
    private LocalDateTime createdAt;
    private String name;
    private String code;


    private int rounds;

    @ElementCollection(targetClass = ProLeagueEnum.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name="pro_leagues", joinColumns = @JoinColumn(name = "league_id"))
    @Column(name="pro_league", nullable = false)
    private List<ProLeagueEnum> proLeagueEnums;

    public List<UserLeagueStatus> getUserLeagueStatuses() {
        return userLeagueStatuses;
    }

    public void setUserLeagueStatuses(List<UserLeagueStatus> userLeagueStatuses) {
        this.userLeagueStatuses = userLeagueStatuses;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void generateCode(){
        this.code = RandomAlphaNumericString.generateString(6);
    }

    public MyUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(MyUser createdBy) {
        this.createdBy = createdBy;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public LeagueTypeEnum getLeagueTypeEnum() {
        return leagueTypeEnum;
    }

    public void setLeagueTypeEnum(LeagueTypeEnum leagueTypeEnum) {
        this.leagueTypeEnum = leagueTypeEnum;
    }

    public List<LeagueRound> getLeagueRounds() {
        return leagueRounds;
    }

    public void setLeagueRounds(List<LeagueRound> leagueRounds) {
        this.leagueRounds = leagueRounds;
    }
}

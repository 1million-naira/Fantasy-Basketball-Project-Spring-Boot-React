package org.example.newtopsquadproject.Model.Players;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.example.newtopsquadproject.Model.Enums.ProLeagueEnum;
import org.example.newtopsquadproject.Model.ProLeagues.PlayerBoxScore;
import org.example.newtopsquadproject.Model.ProLeagues.ProTeam;

import java.util.List;

@Entity
public class Player {
    @Id
    @GeneratedValue
    int id;

    @OneToOne(mappedBy = "player")
    PlayerInfo playerInfo;

    @OneToOne(mappedBy="player")
    PlayerImage playerImage;

    private String name;

    private int fantasyPoints;
    private String team_name;
    private int games_played;
    private float mp_per_g;
    private float fgm_per_g;
    private float fga_per_g;
    private float fg_pct;
    private float fg3m_per_g;
    private float fg3a_per_g;
    private float fg3_pct;
    private float ftm_per_g;
    private float fta_per_g;
    private float ft_pct;
    private float orb_per_g;
    private float drb_per_g;
    private float trb_per_g;
    private float ast_per_g;
    private float stl_per_g;
    private float blk_per_g;
    private float tov_per_g;
    private float fouls_per_g;
    private float pts_per_g;
    private int value;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="team_id")
    private ProTeam proTeam;

    @JsonIgnore
    @OneToMany(mappedBy = "player")
    private List<PlayerBoxScore> playerBoxScores;

    @Enumerated(EnumType.STRING)
    private ProLeagueEnum proLeague;

    public List<PlayerBoxScore> getPlayerBoxScores() {
        return playerBoxScores;
    }

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayerTeamStatus> playerTeamStatuses;

    public void setPlayerBoxScores(List<PlayerBoxScore> playerBoxScores) {
        this.playerBoxScores = playerBoxScores;
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

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

//    public List<PlayerTeamStatus> getPlayerTeamStatuses() {
//        return playerTeamStatuses;
//    }
//
//    public void setPlayerTeamStatuses(List<PlayerTeamStatus> playerTeamStatuses) {
//        this.playerTeamStatuses = playerTeamStatuses;
//    }
//
//    public void addPlayerTeamStatus(PlayerTeamStatus playerTeamStatus){
//        this.playerTeamStatuses.add(playerTeamStatus);
//    }

    public int getGames_played() {
        return games_played;
    }

    public void setGames_played(int games_played) {
        this.games_played = games_played;
    }

    public float getMp_per_g() {
        return mp_per_g;
    }

    public void setMp_per_g(float mp_per_g) {
        this.mp_per_g = mp_per_g;
    }

    public float getFgm_per_g() {
        return fgm_per_g;
    }

    public void setFgm_per_g(float fgm_per_g) {
        this.fgm_per_g = fgm_per_g;
    }

    public float getFga_per_g() {
        return fga_per_g;
    }

    public void setFga_per_g(float fga_per_g) {
        this.fga_per_g = fga_per_g;
    }

    public float getFg_pct() {
        return fg_pct;
    }

    public void setFg_pct(float fg_pct) {
        this.fg_pct = fg_pct;
    }

    public float getFg3m_per_g() {
        return fg3m_per_g;
    }

    public void setFg3m_per_g(float fg3m_per_g) {
        this.fg3m_per_g = fg3m_per_g;
    }

    public float getFg3a_per_g() {
        return fg3a_per_g;
    }

    public void setFg3a_per_g(float fg3a_per_g) {
        this.fg3a_per_g = fg3a_per_g;
    }

    public float getFg3_pct() {
        return fg3_pct;
    }

    public void setFg3_pct(float fg3_pct) {
        this.fg3_pct = fg3_pct;
    }

    public float getFtm_per_g() {
        return ftm_per_g;
    }

    public void setFtm_per_g(float ftm_per_g) {
        this.ftm_per_g = ftm_per_g;
    }

    public float getFta_per_g() {
        return fta_per_g;
    }

    public void setFta_per_g(float fta_per_g) {
        this.fta_per_g = fta_per_g;
    }

    public float getFt_pct() {
        return ft_pct;
    }

    public void setFt_pct(float ft_pct) {
        this.ft_pct = ft_pct;
    }

    public float getOrb_per_g() {
        return orb_per_g;
    }

    public void setOrb_per_g(float orb_per_g) {
        this.orb_per_g = orb_per_g;
    }

    public float getDrb_per_g() {
        return drb_per_g;
    }

    public void setDrb_per_g(float drb_per_g) {
        this.drb_per_g = drb_per_g;
    }

    public float getTrb_per_g() {
        return trb_per_g;
    }

    public void setTrb_per_g(float trb_per_g) {
        this.trb_per_g = trb_per_g;
    }

    public float getAst_per_g() {
        return ast_per_g;
    }

    public void setAst_per_g(float ast_per_g) {
        this.ast_per_g = ast_per_g;
    }

    public float getStl_per_g() {
        return stl_per_g;
    }

    public void setStl_per_g(float stl_per_g) {
        this.stl_per_g = stl_per_g;
    }

    public float getBlk_per_g() {
        return blk_per_g;
    }

    public void setBlk_per_g(float blk_per_g) {
        this.blk_per_g = blk_per_g;
    }

    public float getTov_per_g() {
        return tov_per_g;
    }

    public void setTov_per_g(float tov_per_g) {
        this.tov_per_g = tov_per_g;
    }

    public float getFouls_per_g() {
        return fouls_per_g;
    }

    public void setFouls_per_g(float fouls_per_g) {
        this.fouls_per_g = fouls_per_g;
    }

    public float getPts_per_g() {
        return pts_per_g;
    }

    public void setPts_per_g(float pts_per_g) {
        this.pts_per_g = pts_per_g;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public ProTeam getProTeam() {
        return proTeam;
    }

    public void setProTeam(ProTeam proTeam) {
        this.proTeam = proTeam;
    }


    public ProLeagueEnum getProLeague() {
        return proLeague;
    }

    public void setProLeague(ProLeagueEnum proLeague) {
        this.proLeague = proLeague;
    }

    public PlayerImage getPlayerImage() {
        return playerImage;
    }

    public void setPlayerImage(PlayerImage playerImage) {
        this.playerImage = playerImage;
    }

    public int getFantasyPoints() {
        return fantasyPoints;
    }

    public void addFantasyPoints(int fantasyPoints) {
        this.fantasyPoints += fantasyPoints;
    }
}

package org.example.newtopsquadproject.Model.ProLeagues;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.example.newtopsquadproject.Model.Players.Player;

import java.util.List;

@Entity
@NamedEntityGraph(
        name="ProTeam.withPlayers",
        attributeNodes = @NamedAttributeNode("playerList")
)

@JsonIgnoreProperties({"playerList"})
public class ProTeam {
    @Id
    @GeneratedValue
    private int id;

    @JsonIgnoreProperties({"teamList", "proGames"})
    @ManyToOne
    @JoinColumn(name="league_id")
    private ProLeague proLeague;

    @OneToMany(mappedBy = "proTeam", fetch = FetchType.LAZY)
    private List<Player> playerList;

    @JsonProperty("club_name")
    private String clubName;

    private int games_played;
    private float fgm_per_g;
    private float fga_per_g;
    private float fg_pct;
    private float fg3m_per_g;
    private float fg3a_per_g;
    private float fg3_pct;
    private float fg2m_per_g;
    private float fg2a_per_g;
    private float fg2_pct;
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


    public ProLeague getProLeague() {
        return proLeague;
    }

    @JsonIgnore
    public void setProLeague(ProLeague proLeague) {
        this.proLeague = proLeague;
    }


    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public int getGames_played() {
        return games_played;
    }

    public void setGames_played(int games_played) {
        this.games_played = games_played;
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

    public float getFg2m_per_g() {
        return fg2m_per_g;
    }

    public void setFg2m_per_g(float fg2m_per_g) {
        this.fg2m_per_g = fg2m_per_g;
    }

    public float getFg2a_per_g() {
        return fg2a_per_g;
    }

    public void setFg2a_per_g(float fg2a_per_g) {
        this.fg2a_per_g = fg2a_per_g;
    }

    public float getFg2_pct() {
        return fg2_pct;
    }

    public void setFg2_pct(float fg2_pct) {
        this.fg2_pct = fg2_pct;
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



    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public void addPlayer(Player player){
        playerList.add(player);
    }
}

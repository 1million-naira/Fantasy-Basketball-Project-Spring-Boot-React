package org.example.newtopsquadproject.Model.ProLeagues;

import jakarta.persistence.*;
import org.example.newtopsquadproject.Model.Players.Player;
import org.example.newtopsquadproject.Model.Players.PlayerInfo;

@Entity
public class PlayerBoxScore {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name="box_score_id")
    private BoxScore boxScore;

    @ManyToOne
    @JoinColumn(name="player_id")
    private Player player;

    private String player_name;

    private String mp;

    private int fgm;

    private int fga;

    private float fg_pct;

    private int fg3m;

    private int fg3a;

    private float fg3_pct;

    private int ftm;
    private int fta;
    private float ft_pct;

    private int orb;
    private int drb;
    private int trb;
    private int ast;
    private int stl;
    private int blk;
    private int tov;
    private int fouls;
    private int pts;

    public BoxScore getBoxScore() {
        return boxScore;
    }

    public void setBoxScore(BoxScore boxScore) {
        this.boxScore = boxScore;
    }



    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public String getMp() {
        return mp;
    }

    public void setMp(String mp) {
        this.mp = mp;
    }

    public int getFgm() {
        return fgm;
    }

    public void setFgm(int fgm) {
        this.fgm = fgm;
    }

    public int getFga() {
        return fga;
    }

    public void setFga(int fga) {
        this.fga = fga;
    }

    public float getFg_pct() {
        return fg_pct;
    }

    public void setFg_pct(float fg_pct) {
        this.fg_pct = fg_pct;
    }

    public int getFg3m() {
        return fg3m;
    }

    public void setFg3m(int fg3m) {
        this.fg3m = fg3m;
    }

    public int getFg3a() {
        return fg3a;
    }

    public void setFg3a(int fg3a) {
        this.fg3a = fg3a;
    }

    public float getFg3_pct() {
        return fg3_pct;
    }

    public void setFg3_pct(float fg3_pct) {
        this.fg3_pct = fg3_pct;
    }

    public int getFtm() {
        return ftm;
    }

    public void setFtm(int ftm) {
        this.ftm = ftm;
    }

    public int getFta() {
        return fta;
    }

    public void setFta(int fta) {
        this.fta = fta;
    }

    public float getFt_pct() {
        return ft_pct;
    }

    public void setFt_pct(float ft_pct) {
        this.ft_pct = ft_pct;
    }

    public int getOrb() {
        return orb;
    }

    public void setOrb(int orb) {
        this.orb = orb;
    }

    public int getDrb() {
        return drb;
    }

    public void setDrb(int drb) {
        this.drb = drb;
    }

    public int getTrb() {
        return trb;
    }

    public void setTrb(int trb) {
        this.trb = trb;
    }

    public int getAst() {
        return ast;
    }

    public void setAst(int ast) {
        this.ast = ast;
    }

    public int getStl() {
        return stl;
    }

    public void setStl(int stl) {
        this.stl = stl;
    }

    public int getBlk() {
        return blk;
    }

    public void setBlk(int blk) {
        this.blk = blk;
    }

    public int getTov() {
        return tov;
    }

    public void setTov(int tov) {
        this.tov = tov;
    }

    public int getFouls() {
        return fouls;
    }

    public void setFouls(int fouls) {
        this.fouls = fouls;
    }

    public int getPts() {
        return pts;
    }

    public void setPts(int pts) {
        this.pts = pts;
    }
}

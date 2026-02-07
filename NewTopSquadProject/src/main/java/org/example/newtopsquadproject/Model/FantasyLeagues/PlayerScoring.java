package org.example.newtopsquadproject.Model.FantasyLeagues;

import jakarta.persistence.*;
import org.example.newtopsquadproject.Model.Players.Player;

@Entity
public class PlayerScoring {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name="player_id")
    private Player player;

    public PlayerScoring() {}

    public PlayerScoring(Player player) {
        this.player = player;
    }

    private int ptsScore;
    private int threePointersScore;
    private int fgmScore;
    private int ftmScore;
    private int trbScore;
    private int astScore;
    private int stlScore;
    private int blkScore;
    private int fgaPenalty;
    private int ftaPenalty;
    private int tovPenalty;

    private int totalScore;


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getPtsScore() {
        return ptsScore;
    }

    public void setPtsScore(int ptsScore) {
        this.ptsScore = ptsScore;
    }

    public int getThreePointersScore() {
        return threePointersScore;
    }

    public void setThreePointersScore(int threePointersScore) {
        this.threePointersScore = threePointersScore;
    }

    public int getFgmScore() {
        return fgmScore;
    }

    public void setFgmScore(int fgmScore) {
        this.fgmScore = fgmScore;
    }

    public int getFtmScore() {
        return ftmScore;
    }

    public void setFtmScore(int ftmScore) {
        this.ftmScore = ftmScore;
    }

    public int getTrbScore() {
        return trbScore;
    }

    public void setTrbScore(int trbScore) {
        this.trbScore = trbScore;
    }

    public int getAstScore() {
        return astScore;
    }

    public void setAstScore(int astScore) {
        this.astScore = astScore;
    }

    public int getStlScore() {
        return stlScore;
    }

    public void setStlScore(int stlScore) {
        this.stlScore = stlScore;
    }

    public int getBlkScore() {
        return blkScore;
    }

    public void setBlkScore(int blkScore) {
        this.blkScore = blkScore;
    }

    public int getFgaPenalty() {
        return fgaPenalty;
    }

    public void setFgaPenalty(int fgaPenalty) {
        this.fgaPenalty = fgaPenalty;
    }

    public int getFtaPenalty() {
        return ftaPenalty;
    }

    public void setFtaPenalty(int ftaPenalty) {
        this.ftaPenalty = ftaPenalty;
    }

    public int getTovPenalty() {
        return tovPenalty;
    }

    public void setTovPenalty(int tovPenalty) {
        this.tovPenalty = tovPenalty;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public void calcTotalFantasyScore(){
        this.totalScore = ptsScore +
                threePointersScore +
                fgmScore +
                ftmScore +
                trbScore +
                astScore +
                stlScore +
                blkScore -
                fgaPenalty -
                ftaPenalty -
                tovPenalty;
    }
}

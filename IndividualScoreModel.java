package com.example.user.quickscorer;

/**
 * Created by user on 9/8/2016.
 */
public class IndividualScoreModel {
    private String batsmanName;
    private int runsPerBall;
    private int batsmanScore;
    private int ballFaced;
    private int totalFours;
    private int totalSixes;
    private double strikeRate;
    private String status;

    public IndividualScoreModel(String batsmanName, int runsPerBall, int batsmanScore, int ballFaced, int totalFours, int totalSixes, double strikeRate) {
        this.batsmanName = batsmanName;
        this.runsPerBall = runsPerBall;
        this.batsmanScore = batsmanScore;
        this.ballFaced = ballFaced;
        this.totalFours = totalFours;
        this.totalSixes = totalSixes;
        this.strikeRate = strikeRate;
    }

    public IndividualScoreModel(String batsmanName, int runsPerBall, int batsmanScore, int ballFaced, int totalFours, int totalSixes, double strikeRate, String status) {
        this.batsmanName = batsmanName;
        this.runsPerBall = runsPerBall;
        this.batsmanScore = batsmanScore;
        this.ballFaced = ballFaced;
        this.totalFours = totalFours;
        this.totalSixes = totalSixes;
        this.strikeRate = strikeRate;
        this.status = status;
    }

    public IndividualScoreModel() {
    }

    public String getBatsmanName() {
        return batsmanName;
    }

    public int getRunsPerBall() {
        return runsPerBall;
    }
    public void setRunsPerBall(int runsPerBall) {
        this.runsPerBall = runsPerBall;
    }

    public int getBatsmanScore() {
        return batsmanScore;
    }

    public void setBallFaced(int ballFaced) {
        this.ballFaced = ballFaced;
    }

    public int getBallFaced() {
        return ballFaced;
    }

    public int getTotalFours() {
        return totalFours;
    }

    public int getTotalSixes() {
        return totalSixes;
    }

    public double getStrikeRate() {
        return strikeRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void processRun(int runsPerBall){
        batsmanScore += runsPerBall;
        ballFaced += 1;
        if(runsPerBall == 4){
            totalFours += 1;
        }
        if(runsPerBall == 6){
            totalSixes += 1;
        }
        if(ballFaced == 0){
            strikeRate = 0.0;
        }else{
            strikeRate = (double)batsmanScore * 100 / ballFaced;
        }
    }
}

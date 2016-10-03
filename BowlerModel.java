package com.example.user.quickscorer;

/**
 * Created by user on 9/15/2016.
 */
public class BowlerModel {
    private String bowlerName;
    private int maidenOver;
    private int overCount;
    private int bowlerRun;
    private int bowlerWicket;
    private double bowlerEcon;
    private int bowlerFours;
    private int bowlerSixes;
    private int noOfWideBalls;
    private int noOfNoBalls;
    private int ballCount;
    private boolean maiden = false;

    public BowlerModel(String bowlerName, int maidenOver, int overCount, int bowlerRun, int bowlerWicket, double bowlerEcon, int bowlerFours, int bowlerSixes) {
        this.bowlerName = bowlerName;
        this.maidenOver = maidenOver;
        this.overCount = overCount;
        this.bowlerRun = bowlerRun;
        this.bowlerWicket = bowlerWicket;
        this.bowlerEcon = bowlerEcon;
        this.bowlerFours = bowlerFours;
        this.bowlerSixes = bowlerSixes;
        this.ballCount = 0;
        this.noOfWideBalls = 0;
        this.noOfNoBalls = 0;
    }

    public String getBowlerName() {
        return bowlerName;
    }

    public int getMaidenOver() {
        return maidenOver;
    }

    public int getOverCount() {
        return overCount;
    }

    public int getBowlerRun() {
        return bowlerRun;
    }

    public int getBowlerWicket() {
        return bowlerWicket;
    }

    public double getBowlerEcon() {
        return bowlerEcon;
    }

    public int getBowlerFours() {
        return bowlerFours;
    }

    public int getBowlerSixes() {
        return bowlerSixes;
    }

    public int getNoOfWideBalls() {
        return noOfWideBalls;
    }

    public int getNoOfNoBalls() {
        return noOfNoBalls;
    }

    public int getBallCount() {
        return ballCount;
    }

    public boolean isMaiden() {
        return maiden;
    }

    public void processOver(int runsPerBall, String extra, int ballCount){
        if(runsPerBall == 0 && (extra.equals("") || extra.equals(Constants.OUT))){
            maiden = true;
        }else{
            maiden = false;
        }
        this.ballCount = ballCount;
        if(ballCount == 6){
            overCount += 1;
            this.ballCount = 0;
            if(maiden){
                maidenOver += 1;
            }
        }
        if(runsPerBall > 0){
            bowlerRun += runsPerBall;
        }
        if(runsPerBall == 4){
            bowlerFours += 1;
        }else if(runsPerBall == 6){
            bowlerSixes += 1;
        }
        if(extra.equals(Constants.WIDE_BALL)){
            noOfWideBalls += 1;
            bowlerRun += Constants.ILLEGAL_DELIVERY_SCORE;
        }else if(extra.equals(Constants.NO_BALL)){
            noOfNoBalls += 1;
            bowlerRun += Constants.ILLEGAL_DELIVERY_SCORE;
        }else if(extra.equals(Constants.OUT)){
            bowlerWicket += 1;
        }
        String overString = String.valueOf(new StringBuilder().append(overCount).append(".").append(this.ballCount));
        double over = Double.parseDouble(overString);
        bowlerEcon = (double)bowlerRun / over;
    }
    public void processOverAlt(int runsPerBall, String illegalDelivery, String byeDelivery, String wicketDelivery, int ballCount){
        this.ballCount = ballCount;
        if(runsPerBall == 0 && (illegalDelivery.length() == 0 || wicketDelivery.length() > 0)){
            maiden = true;
        }else {
            maiden = false;
        }
        if(ballCount == 6){
            overCount += 1;
            this.ballCount = 0;
            if(maiden){
                maidenOver += 1;
            }
        }
        if(runsPerBall > 0 && byeDelivery.length() == 0){
            bowlerRun += runsPerBall;
        }
        if(runsPerBall == 4 && byeDelivery.length() == 0){
            bowlerFours += 1;
        }else if(runsPerBall == 6 && byeDelivery.length() == 0){
            bowlerSixes += 1;
        }
        if(illegalDelivery.equals(Constants.WIDE_BALL)){
            noOfWideBalls += 1;
            if(byeDelivery.length() > 0){
                bowlerRun += runsPerBall + Constants.ILLEGAL_DELIVERY_SCORE;
            }else{
                bowlerRun += Constants.ILLEGAL_DELIVERY_SCORE;
            }
        }else if(illegalDelivery.equals(Constants.NO_BALL)){
            noOfNoBalls += 1;
            if(byeDelivery.length() > 0){
                bowlerRun += runsPerBall + Constants.ILLEGAL_DELIVERY_SCORE;
            }else{
                bowlerRun += Constants.ILLEGAL_DELIVERY_SCORE;
            }
        }else if(wicketDelivery.equals(Constants.WICKET_BOLD) || wicketDelivery.equals(Constants.WICKET_STUMMPED) || wicketDelivery.equals(Constants.WICKET_LBW)
                || wicketDelivery.equals(Constants.WICKET_CATCH) || wicketDelivery.equals(Constants.WICKET_HITOUT)){
            bowlerWicket += 1;
        }
        String overString = String.valueOf(new StringBuilder().append(overCount).append(".").append(this.ballCount));
        double over = Double.parseDouble(overString);
        bowlerEcon = (double)bowlerRun / over;
    }
}

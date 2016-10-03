package com.example.user.quickscorer;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by user on 9/29/2016.
 */

public class Scorecard {
    private static ArrayList<IndividualScoreModel> firstInningsBatsmanModels;
    private static ArrayList<IndividualScoreModel> secondInningsBatsmanModels;
    private static ArrayList<BowlerModel> firstInningsBowlerModels;
    private static ArrayList<BowlerModel> secondInningsBowlerModels;
    private static String currentInnings;

    public Scorecard() {
        firstInningsBatsmanModels = new ArrayList<>();
        firstInningsBowlerModels = new ArrayList<>();
        secondInningsBatsmanModels = new ArrayList<>();
        secondInningsBowlerModels = new ArrayList<>();
        currentInnings = null;
    }

    public static String getCurrentInnings() {
        return currentInnings;
    }

    public static void setCurrentInnings(String currentInnings) {
        Scorecard.currentInnings = currentInnings;
    }

    public ArrayList<IndividualScoreModel> getFirstInningsBatsmanModels() {
        return firstInningsBatsmanModels;
    }

    public void setFirstInningsBatsmanModels(ArrayList<IndividualScoreModel> firstInningsBatsmanModels) {
        this.firstInningsBatsmanModels = firstInningsBatsmanModels;
    }

    public ArrayList<BowlerModel> getFirstInningsBowlerModels() {
        return firstInningsBowlerModels;
    }

    public void setFirstInningsBowlerModels(ArrayList<BowlerModel> firstInningsBowlerModels) {
        this.firstInningsBowlerModels = firstInningsBowlerModels;
    }

    public ArrayList<IndividualScoreModel> getSecondInningsBatsmanModels() {
        return secondInningsBatsmanModels;
    }

    public void setSecondInningsBatsmanModels(ArrayList<IndividualScoreModel> secondInningsBatsmanModels) {
        this.secondInningsBatsmanModels = secondInningsBatsmanModels;
    }

    public ArrayList<BowlerModel> getSecondInningsBowlerModels() {
        return secondInningsBowlerModels;
    }

    public void setSecondInningsBowlerModels(ArrayList<BowlerModel> secondInningsBowlerModels) {
        this.secondInningsBowlerModels = secondInningsBowlerModels;
    }
}

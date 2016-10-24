package com.example.user.quickscorer;


import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.quickscorer.Database.PlayerDatabaseSource;
import com.example.user.quickscorer.Database.PlayerModel;
import com.example.user.quickscorer.Interface.ScorecardListener;
import com.example.user.quickscorer.Preference.GamePreference;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;

import static com.example.user.quickscorer.Constants.OUT;


public class MainScoreFragment extends Fragment {
    private ArrayList<IndividualScoreModel> firstInningsBattingScoreCard;
    private ArrayList<IndividualScoreModel> secondInningsBattingScoreCard;
    private ArrayList<BowlerModel> firstInningsBowlingScorecard;
    private ArrayList<BowlerModel> secondInningsBowlingScorecard;
    private String[]batsmenPair = new String[2];
    private int runPerBall,ballCount,currentOver,totalScore,fallOfWicket,targetScore,strikePointer;
    private double currentRunRate,reqRunRate;
    private TextView vsTV,totalOverLabelTV,teamNameTV,scoreTV,overTV,runRateTV,reqRunRateTV,targetTV,inningsLabel,inputDisplayTV, ballByBallStatTV,batsManOneTV, batsManOneRunTV, batsManOneBallTV, batsManTwoTV, batsManTwoRunTV, batsManTwoBallTV, extraScoreTV;
    private TextView bowlerNameTV,bowlerOverTV;
    private String inputDisplayRunText = "";
    private String inputDisplayExtraText = "";
    private String inputDisplayExtraTextTwo = "";
    private boolean batsmanIsOut;
    private FloatingActionMenu menuExtra;
    private FloatingActionMenu menuWicket;
    private FloatingActionButton extraWide;
    private FloatingActionButton extraNo;
    private FloatingActionButton extraBye;
    private FloatingActionButton extraLegbye;
    private FloatingActionButton wkBold;
    private FloatingActionButton wkCatch;
    private FloatingActionButton wkLbw;
    private FloatingActionButton wkHitWicket;
    private FloatingActionButton wkRunOut;
    private FloatingActionButton wkStummped;
    private Button zeroBtn,oneBtn,twoBtn,threeBtn,fourBtn,fiveBtn,sixBtn;
    private Button strikeBtn,changeBatsmanBtn,changeBowlerBtn,playBtn,addScoreBtn,setupInningsBtn,clearDisplayBtn;
    private int runBtnColor, runBtnColorPressed,controlBtnColor,controlBtnColorPressed;
    private String currentInnings,tossWinner,electsTo,firstInningsTeam,secondInningsTeam;
    private int totalOver,teamOneId,teamTwoId,dismissedBatsmanIndex,firstInningsExtraRuns,secondInningsExtraRuns,inningsExtra;
    private IndividualScoreModel model1;
    private IndividualScoreModel model2;
    private BowlerModel bowlerObj;
    private PlayerDatabaseSource playerDatabaseSource;
    private ArrayList<PlayerModel>firstInningsTeamPlayers;
    private ArrayList<PlayerModel>secondInningsTeamPlayers;
    private AlertDialog.Builder alert;
    private BowlerCustomAdapter bowlerCustomAdapter;
    private BatsmanCustomAdapter batsmanCustomAdapter;
    private String typeOfWicketFall,ballStats="",fielderName = "someone";
    private ScorecardListener scorecardListener;
    private Scorecard scorecard;
    private String illegalDeliveryText = "";
    private String byeDeliveryText = "";
    private String wicketDeliveryText = "";
    private String previousBowler;
    private GamePreference gamePreference;

    public MainScoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        runBtnColor = getResources().getColor(R.color.textAltBackground);
        runBtnColorPressed = getResources().getColor(R.color.colorPrimaryDark);
        controlBtnColor = getResources().getColor(R.color.btnBackground);
        controlBtnColorPressed = getResources().getColor(R.color.textAltBackground);
        menuExtra = (FloatingActionMenu) view.findViewById(R.id.menuExtra);
        menuWicket = (FloatingActionMenu) view.findViewById(R.id.menuWicket);
        alert = new AlertDialog.Builder(getContext());
        gamePreference = new GamePreference(getContext());
        firstInningsBowlingScorecard = new ArrayList<>();
        secondInningsBowlingScorecard = new ArrayList<>();
        firstInningsBattingScoreCard = new ArrayList<>();
        secondInningsBattingScoreCard = new ArrayList<>();
        scorecard = new Scorecard();
        /*TRIGGER DATABASE*/
        new DatabaseTask().execute();
        initializeButtons(view);
        initializeTextViews(view);
        disableOptionButtons();
        disableScoreButtons();
        playBtn.setEnabled(false);
        playBtn.setAlpha(0.5f);
        addScoreBtn.setEnabled(false);
        addScoreBtn.setAlpha(0.5f);
        inputDisplayTV.setText(tossWinner+" won the toss and elected to "+electsTo+" first");
    }
    private class DatabaseTask extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            playerDatabaseSource = new PlayerDatabaseSource(getContext());
            firstInningsTeamPlayers = new ArrayList<>();
            secondInningsTeamPlayers = new ArrayList<>();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            firstInningsTeamPlayers = playerDatabaseSource.getTeamPlayers(teamOneId);
            secondInningsTeamPlayers = playerDatabaseSource.getTeamPlayers(teamTwoId);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            vsTV.setText(firstInningsTeam+" Vs "+secondInningsTeam);
            totalOverLabelTV.setText(Integer.toString(totalOver)+" Overs Match");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firstInningsTeam = getArguments().getString(Constants.FIRST_INNINGS_TEAM);
        secondInningsTeam = getArguments().getString(Constants.SECOND_INNINGS_TEAM);
        tossWinner = getArguments().getString(Constants.TOSS_WINNER);
        electsTo = getArguments().getString(Constants.ELECTS_TO);
        totalOver = getArguments().getInt(Constants.TOTAL_OVER);
        teamOneId = getArguments().getInt(Constants.TEAM_ONE_ID);
        teamTwoId = getArguments().getInt(Constants.TEAM_TWO_ID);
        return inflater.inflate(R.layout.fragment_main_score, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        extraWide.setOnClickListener(clickListener);
        extraNo.setOnClickListener(clickListener);
        extraBye.setOnClickListener(clickListener);
        extraLegbye.setOnClickListener(clickListener);
        wkBold.setOnClickListener(clickListener);
        wkCatch.setOnClickListener(clickListener);
        wkLbw.setOnClickListener(clickListener);
        wkRunOut.setOnClickListener(clickListener);
        wkStummped.setOnClickListener(clickListener);
        zeroBtn.setOnClickListener(runBtnClickListener);
        oneBtn.setOnClickListener(runBtnClickListener);
        twoBtn.setOnClickListener(runBtnClickListener);
        threeBtn.setOnClickListener(runBtnClickListener);
        fourBtn.setOnClickListener(runBtnClickListener);
        fiveBtn.setOnClickListener(runBtnClickListener);
        sixBtn.setOnClickListener(runBtnClickListener);
        strikeBtn.setOnClickListener(controlButtonListener);
        changeBatsmanBtn.setOnClickListener(controlButtonListener);
        changeBowlerBtn.setOnClickListener(controlButtonListener);
        playBtn.setOnClickListener(controlButtonListener);
        addScoreBtn.setOnClickListener(controlButtonListener);
        setupInningsBtn.setOnClickListener(controlButtonListener);
        clearDisplayBtn.setOnClickListener(controlButtonListener);
    }
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FloatingActionButton v = (FloatingActionButton) view;
            switch (view.getId()){
                case R.id.extraWide:
                    illegalDeliveryText = Constants.WIDE_BALL;
                    inputDisplayTV.setText(inputDisplayRunText+" "+illegalDeliveryText+" "+byeDeliveryText+" "+wicketDeliveryText);
                    break;
                case R.id.extraNo:
                    illegalDeliveryText = Constants.NO_BALL;
                    inputDisplayTV.setText(inputDisplayRunText+" "+illegalDeliveryText+" "+byeDeliveryText+" "+wicketDeliveryText);
                    break;
                case R.id.extraBye:
                    byeDeliveryText = Constants.BYE;
                    inputDisplayTV.setText(inputDisplayRunText+" "+illegalDeliveryText+" "+byeDeliveryText+" "+wicketDeliveryText);
                    break;
                case R.id.extraLegbye:
                    byeDeliveryText = Constants.LEG_BYE;
                    inputDisplayTV.setText(inputDisplayRunText+" "+illegalDeliveryText+" "+byeDeliveryText+" "+wicketDeliveryText);
                    break;
                case R.id.wkBold:
                case R.id.wkCatch:
                case R.id.wkLbw:
                case R.id.wkRunOut:
                case R.id.wkStummped:
                case R.id.wkHitWicket:
                    typeOfWicketFall = v.getLabelText();
                    wicketDeliveryText = typeOfWicketFall;
                    inputDisplayTV.setText(inputDisplayRunText+" "+illegalDeliveryText+" "+byeDeliveryText+" "+wicketDeliveryText);
                    break;
            }
        }
    };
    private View.OnClickListener runBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button b= (Button) view;
            switch (view.getId()){
                case R.id.runBtnZero:
                case R.id.runBtnOne:
                case R.id.runBtnTwo:
                case R.id.runBtnThree:
                case R.id.runBtnFour:
                case R.id.runBtnFive:
                case R.id.runBtnSix:
                    runPerBall=Integer.parseInt(b.getText().toString());
                    inputDisplayRunText=b.getText().toString()+" run(s)";
                    inputDisplayTV.setText(inputDisplayRunText+" "+illegalDeliveryText+" "+byeDeliveryText+" "+wicketDeliveryText);
            }
        }
    };
    private View.OnClickListener controlButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.changeStrikeBtn:
                    changeStrike();
                    break;
                case R.id.changeBatsmanBtn:
                    selectBatsman();
                    break;
                case R.id.changeBowlerBtn:
                    nextBowler();
                    break;
                case R.id.playBtn:
                    play();
                    break;
                case R.id.addScoreBtn:
                    addToScore();
                    break;
                case R.id.setupInningsBtn:
                    setupInnings();
                    break;
                case R.id.clearDisplayBtn:
                    inputDisplayTV.setText("");
                    illegalDeliveryText = "";
                    byeDeliveryText = "";
                    wicketDeliveryText = "";
                    inputDisplayRunText = "";
                    runPerBall = 0;
            }
        }
    };
    private View.OnClickListener selectBatsmanListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final TextView batsmanTV = (TextView) view;
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            final View layout = layoutInflater.inflate(R.layout.batsman_list_layout,null);
            final ListView batsmanLV = (ListView) layout.findViewById(R.id.batsmanListView);
            if(currentInnings.equals(Constants.FIRST_INNINGS)){
                batsmanCustomAdapter = new BatsmanCustomAdapter(getContext(),firstInningsTeamPlayers);
            }else if(currentInnings.equals(Constants.SECOND_INNINGS)){
                batsmanCustomAdapter = new BatsmanCustomAdapter(getContext(),secondInningsTeamPlayers);
            }
            batsmanLV.setAdapter(batsmanCustomAdapter);
            alert.setTitle("Select Batsman");
            alert.setView(layout);
            alert.setNegativeButton("Ok",null);
            alert.show();
            batsmanLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final String name;
                    if(currentInnings.equals(Constants.FIRST_INNINGS)){
                        name = firstInningsTeamPlayers.get(i).getPlayerName();
                    }else if(currentInnings.equals(Constants.SECOND_INNINGS)){
                        name = secondInningsTeamPlayers.get(i).getPlayerName();
                    }
                    switch (batsmanTV.getId()){
                        case R.id.batsmanOne:

                            break;
                        case R.id.batsmanTwo:
                            break;
                    }
                }
            });
        }
    };
    private void addToScore(){
        if((inputDisplayRunText.length() > 0 || illegalDeliveryText.length() > 0 || byeDeliveryText.length() > 0 || wicketDeliveryText.length() > 0)
                && (batsManOneTV.length() > 0 && batsManTwoTV.length() > 0 && bowlerNameTV.length() > 0)){
            finalizeScore();
        }else{
            inputDisplayTV.setText("Either your Batsmen or Bowler fields are Empty Or you haven't entered Runs on board!");
        }
    }
    private void createBallStatString(){
        String extra = "";
        if(illegalDeliveryText.equals(Constants.WIDE_BALL)){
            extra = "wd";
        }else if(illegalDeliveryText.equals(Constants.NO_BALL)){
            extra = "n";
        }else if(wicketDeliveryText.length() > 0){
            extra = "wk";
        }else if(byeDeliveryText.equals(Constants.BYE)){
            extra = "b";
        }else if(byeDeliveryText.equals(Constants.LEG_BYE)){
            extra = "lb";
        }
        ballStats += String.format("%d%s ",runPerBall,extra);
        ballByBallStatTV.setText(ballStats);
    }
    private void checkEndOfInnings(){
        if(currentInnings == Constants.FIRST_INNINGS){
            if(fallOfWicket == Constants.TOTAL_WICKETS || currentOver == totalOver){
                firstInningsExtraRuns = inningsExtra;
                targetScore = totalScore + 1;
                reqRunRate = (double)targetScore / totalOver;
                addScoreBtn.setEnabled(false);
                addScoreBtn.setAlpha(0.5f);
                setupInningsBtn.setEnabled(true);
                setupInningsBtn.setAlpha(1.0f);
                disableScoreButtons();
                disableOptionButtons();
                clearBatsmanOneTextFields();
                clearBatsmanTwoTextFields();
                clearBowlerTextFields();
            }
        }else if(currentInnings == Constants.SECOND_INNINGS){
            if(totalScore >= targetScore && currentOver <= totalOver && fallOfWicket < Constants.TOTAL_WICKETS){
                secondInningsExtraRuns = inningsExtra;
                inputDisplayTV.setText(secondInningsTeam+" wins by "+Integer.toString(Constants.TOTAL_WICKETS - fallOfWicket)+" wickets!");
                addScoreBtn.setEnabled(false);
                addScoreBtn.setAlpha(0.5f);
                disableScoreButtons();
                disableOptionButtons();
                gamePreference.setGameStatus(getString(R.string.game_status_off));
            }else if(totalScore == targetScore - 1 && (currentOver == totalOver || fallOfWicket == Constants.TOTAL_WICKETS)){
                secondInningsExtraRuns = inningsExtra;
                inputDisplayTV.setText("Match drawn!!!");
                addScoreBtn.setEnabled(false);
                addScoreBtn.setAlpha(0.5f);
                disableScoreButtons();
                disableOptionButtons();
                gamePreference.setGameStatus(getString(R.string.game_status_off));
            }else if(totalScore < targetScore - 1 && (currentOver == totalOver || fallOfWicket == Constants.TOTAL_WICKETS)){
                secondInningsExtraRuns = inningsExtra;
                inputDisplayTV.setText(firstInningsTeam+" wins by "+Integer.toString((targetScore - 1) - totalScore)+" run(s)");
                addScoreBtn.setEnabled(false);
                addScoreBtn.setAlpha(0.5f);
                disableScoreButtons();
                disableOptionButtons();
                gamePreference.setGameStatus(getString(R.string.game_status_off));
            }
        }
    }
    private void setupInnings() {
        enableOptionButtons();
        playBtn.setEnabled(true);
        playBtn.setAlpha(1.0f);
        totalScore = 0;
        runPerBall = 0;
        currentOver = 0;
        ballCount = 0;
        fallOfWicket = 0;
        currentRunRate = 0.0;
        strikePointer = 0;
        firstInningsExtraRuns = 0;
        secondInningsExtraRuns = 0;
        inningsExtra = 0;
        batsmanIsOut = false;
        batsManOneTV.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.appFontBlack));
        batsManTwoTV.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.appFontBlack));
        if(currentInnings == null){
            currentInnings = Constants.FIRST_INNINGS;
            teamNameTV.setText(firstInningsTeam);
            reqRunRate = 0.0;
            batsmenPair[0] = "empty";
            batsmenPair[1] = "empty";
            batsManOneTV.setText(batsmenPair[0]);
            batsManTwoTV.setText(batsmenPair[1]);
            batsManOneTV.setTextColor(Color.GREEN);
        }else{
            currentInnings = Constants.SECOND_INNINGS;
            teamNameTV.setText(secondInningsTeam);
            targetTV.setText(Integer.toString(targetScore));
            reqRunRateTV.setText(String.format("%.2f",reqRunRate));
            batsmenPair[0] = "empty";
            batsmenPair[1] = "empty";
            batsManOneTV.setText(batsmenPair[0]);
            batsManTwoTV.setText(batsmenPair[1]);
            batsManOneTV.setTextColor(Color.GREEN);
        }

        setupInningsBtn.setEnabled(false);
        setupInningsBtn.setAlpha(0.5f);
        inningsLabel.setText(currentInnings);
        scoreTV.setText(Integer.toString(totalScore)+"/"+Integer.toString(fallOfWicket));
        overTV.setText(Integer.toString(currentOver)+"."+Integer.toString(ballCount)+" overs");
        runRateTV.setText(Double.toString(currentRunRate));
        inputDisplayTV.setText("");
        ballByBallStatTV.setText("");
        enableOptionButtons();
    }
    private void play(){
        if(model1 == null || model2 == null || bowlerObj == null){
            inputDisplayTV.setText("Please select both batsmen and bowler");
        }else{
            inputDisplayTV.setText("");
            addScoreBtn.setEnabled(true);
            addScoreBtn.setAlpha(1.0f);
            setupInningsBtn.setEnabled(false);
            setupInningsBtn.setAlpha(0.5f);
            playBtn.setEnabled(false);
            playBtn.setAlpha(0.5f);
            enableScoreButtons();
            if(currentInnings.equals(Constants.FIRST_INNINGS)){
                firstInningsBattingScoreCard.add(model1);
                firstInningsBattingScoreCard.add(model2);
                scorecard.setCurrentInnings(currentInnings);
                scorecard.setFirstInningsBatsmanModels(firstInningsBattingScoreCard);
            }else if(currentInnings.equals(Constants.SECOND_INNINGS)){
                secondInningsBattingScoreCard.add(model1);
                secondInningsBattingScoreCard.add(model2);
                scorecard.setCurrentInnings(currentInnings);
                scorecard.setSecondInningsBatsmanModels(secondInningsBattingScoreCard);
            }
        }
    }
    private void changeStrike(){
        switch (strikePointer){
            case 0:
                strikePointer = 1;
                batsManOneTV.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.appFontBlack));
                batsManTwoTV.setTextColor(Color.GREEN);
                break;
            case 1:
                strikePointer = 0;
                batsManOneTV.setTextColor(Color.GREEN);
                batsManTwoTV.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.appFontBlack));
                break;
        }
    }
    private void selectBatsman(){
        if(!batsmanIsOut){
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            final View layout = layoutInflater.inflate(R.layout.batsman_list_layout,null);
            final ListView batsmanLV = (ListView) layout.findViewById(R.id.batsmanListView);
            if(currentInnings.equals(Constants.FIRST_INNINGS)){
                batsmanCustomAdapter = new BatsmanCustomAdapter(getContext(),firstInningsTeamPlayers);
            }else if(currentInnings.equals(Constants.SECOND_INNINGS)){
                batsmanCustomAdapter = new BatsmanCustomAdapter(getContext(),secondInningsTeamPlayers);
            }
            batsmanLV.setAdapter(batsmanCustomAdapter);
            alert.setTitle("Select Batsman");
            alert.setView(layout);
            alert.setNegativeButton("Ok",null);
            alert.show();
            batsmanLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(currentInnings.equals(Constants.FIRST_INNINGS)){
                        final String name = firstInningsTeamPlayers.get(i).getPlayerName();
                        if(strikePointer == 0){
                            batsmenPair[0] = name;
                            batsManOneTV.setText(batsmenPair[0]);
                            model1 = new IndividualScoreModel(name,0,0,0,0,0,0.0,Constants.NOTOUT);
                            setBatsmanOneScore();
                        }else if(strikePointer == 1){
                            batsmenPair[1] = name;
                            batsManTwoTV.setText(batsmenPair[1]);
                            model2 = new IndividualScoreModel(name,0,0,0,0,0,0.0,Constants.NOTOUT);
                            setBatsmanTwoScore();
                        }
                    }else if(currentInnings.equals(Constants.SECOND_INNINGS)){
                        final String name = secondInningsTeamPlayers.get(i).getPlayerName();
                        if(strikePointer == 0){
                            batsmenPair[0] = name;
                            batsManOneTV.setText(batsmenPair[0]);
                            model1 = new IndividualScoreModel(name,0,0,0,0,0,0.0,Constants.NOTOUT);
                            setBatsmanOneScore();
                        }else if(strikePointer == 1){
                            batsmenPair[1] = name;
                            batsManTwoTV.setText(batsmenPair[1]);
                            model2 = new IndividualScoreModel(name,0,0,0,0,0,0.0,Constants.NOTOUT);
                            setBatsmanTwoScore();
                        }
                    }
                }
            });
        }else{
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            final View layout = layoutInflater.inflate(R.layout.batsman_list_layout,null);
            final ListView batsmanLV = (ListView) layout.findViewById(R.id.batsmanListView);
            if(currentInnings.equals(Constants.FIRST_INNINGS)){
                batsmanCustomAdapter = new BatsmanCustomAdapter(getContext(),firstInningsTeamPlayers);
            }else if(currentInnings.equals(Constants.SECOND_INNINGS)){
                batsmanCustomAdapter = new BatsmanCustomAdapter(getContext(),secondInningsTeamPlayers);
            }
            batsmanLV.setAdapter(batsmanCustomAdapter);
            alert.setTitle("Select Batsman");
            alert.setView(layout);
            alert.setNegativeButton("Ok",null);
            alert.show();
            batsmanLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(currentInnings.equals(Constants.FIRST_INNINGS)){
                        boolean exist = false;
                        final String name = firstInningsTeamPlayers.get(i).getPlayerName();
                        for(IndividualScoreModel b:firstInningsBattingScoreCard){
                            if(b.getBatsmanName().equals(name)){
                                Toast.makeText(getActivity().getApplicationContext(), "Select another", Toast.LENGTH_SHORT).show();
                                exist = true;
                            }
                        }
                        if(!exist){
                            if(dismissedBatsmanIndex == 0){
                                batsmenPair[0] = name;
                                batsManOneTV.setText(name);
                                model1 = new IndividualScoreModel(name,0,0,0,0,0,0.0,Constants.NOTOUT);
                                firstInningsBattingScoreCard.add(model1);
                                setBatsmanOneScore();
                                scorecard.setFirstInningsBatsmanModels(firstInningsBattingScoreCard);
                            }else if(dismissedBatsmanIndex == 1){
                                batsmenPair[1] = name;
                                batsManTwoTV.setText(name);
                                model2 = new IndividualScoreModel(name,0,0,0,0,0,0.0,Constants.NOTOUT);
                                firstInningsBattingScoreCard.add(model2);
                                setBatsmanTwoScore();
                                scorecard.setFirstInningsBatsmanModels(firstInningsBattingScoreCard);
                            }
                        }
                    }else if(currentInnings.equals(Constants.SECOND_INNINGS)){
                        boolean exist = false;
                        final String name = secondInningsTeamPlayers.get(i).getPlayerName();
                        for(IndividualScoreModel b:secondInningsBattingScoreCard){
                            if(b.getBatsmanName().equals(name)){
                                Toast.makeText(getActivity().getApplicationContext(), "Select another", Toast.LENGTH_SHORT).show();
                                exist = true;
                            }
                        }
                        if(!exist){
                            if(dismissedBatsmanIndex == 0){
                                batsmenPair[0] = name;
                                batsManOneTV.setText(name);
                                model1 = new IndividualScoreModel(name,0,0,0,0,0,0.0,Constants.NOTOUT);
                                secondInningsBattingScoreCard.add(model1);
                                setBatsmanOneScore();
                                scorecard.setSecondInningsBatsmanModels(secondInningsBattingScoreCard);
                            }else if(dismissedBatsmanIndex == 1){
                                batsmenPair[1] = name;
                                batsManTwoTV.setText(name);
                                model2 = new IndividualScoreModel(name,0,0,0,0,0,0.0,Constants.NOTOUT);
                                secondInningsBattingScoreCard.add(model2);
                                setBatsmanTwoScore();
                                scorecard.setSecondInningsBatsmanModels(secondInningsBattingScoreCard);
                            }
                        }
                    }
                }
            });
            addScoreBtn.setEnabled(true);
            addScoreBtn.setAlpha(1.0f);
            strikeBtn.setEnabled(true);
            strikeBtn.setAlpha(1.0f);
        }
    }
    private void nextBowler(){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View layout = layoutInflater.inflate(R.layout.bowler_list_layout,null);
        final ListView bowlerLV = (ListView) layout.findViewById(R.id.bowlerListView);
        if(currentInnings == Constants.FIRST_INNINGS){
            bowlerCustomAdapter = new BowlerCustomAdapter(getContext(),secondInningsTeamPlayers);
        }else if(currentInnings == Constants.SECOND_INNINGS){
            bowlerCustomAdapter = new BowlerCustomAdapter(getContext(),firstInningsTeamPlayers);
        }
        bowlerLV.setAdapter(bowlerCustomAdapter);
        alert.setTitle("Select a Bowler");
        alert.setView(layout);
        alert.setNegativeButton("Ok",null);
        alert.show();
        bowlerLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(currentInnings == Constants.FIRST_INNINGS){
                    boolean exist = false;
                    final String b = secondInningsTeamPlayers.get(i).getPlayerName();
                    bowlerNameTV.setText(b);
                    for(BowlerModel bm:firstInningsBowlingScorecard){
                        if(bm.getBowlerName().equals(b)){
                            exist = true;
                            if(previousBowler == null || !previousBowler.equals(b)){
                                bowlerObj = bm;
                            }else{
                                Toast.makeText(getActivity().getApplicationContext(), "Select another", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    if(!exist){
                        bowlerObj = new BowlerModel(bowlerNameTV.getText().toString(),0,0,0,0,0.0,0,0);
                        firstInningsBowlingScorecard.add(bowlerObj);
                        scorecard.setFirstInningsBowlerModels(firstInningsBowlingScorecard);
                    }
                    setBowlerStats();
                    previousBowler = b;
                }else if(currentInnings == Constants.SECOND_INNINGS){
                    boolean exist = false;
                    final String b = firstInningsTeamPlayers.get(i).getPlayerName();
                    bowlerNameTV.setText(b);
                    for(BowlerModel bm:secondInningsBowlingScorecard){
                        if(bm.getBowlerName().equals(b)){
                            exist = true;
                            if(previousBowler == null || !previousBowler.equals(b)){
                                bowlerObj = bm;
                            }else{
                                Toast.makeText(getActivity().getApplicationContext(), "Select another", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    if(!exist){
                        bowlerObj = new BowlerModel(bowlerNameTV.getText().toString(),0,0,0,0,0.0,0,0);
                        secondInningsBowlingScorecard.add(bowlerObj);
                        scorecard.setSecondInningsBowlerModels(secondInningsBowlingScorecard);
                    }
                    setBowlerStats();
                    previousBowler = b;
                }
            }
        });
    }
    private void autoChangeStrike(){
        switch (strikePointer){
            case 0:
                strikePointer = 1;
                batsManOneTV.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.appFontBlack));
                batsManTwoTV.setTextColor(Color.GREEN);
                break;
            case 1:
                strikePointer = 0;
                batsManOneTV.setTextColor(Color.GREEN);
                batsManTwoTV.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.appFontBlack));
                break;
        }
    }
    private void addIndividualScore() {
        if(strikePointer == 0){
            if(inputDisplayExtraText.length() == 0 || inputDisplayExtraText.equals(Constants.NO_BALL) || inputDisplayExtraText.equals(OUT)){
                model1.processRun(runPerBall);
            }else if(inputDisplayExtraText.equals(Constants.BYE) || inputDisplayExtraText.equals(Constants.LEG_BYE)){
                model1.processRun(0);
            }
        }else if(strikePointer == 1){
            if(inputDisplayExtraText.length() == 0 || inputDisplayExtraText.equals(Constants.NO_BALL) || inputDisplayExtraText.equals(OUT)){
                model2.processRun(runPerBall);
            }else if(inputDisplayExtraText.equals(Constants.BYE) || inputDisplayExtraText.equals(Constants.LEG_BYE)){
                model2.processRun(0);
            }
        }
    }
    private void setBatsmanOneScore(){
        batsManOneRunTV.setText(Integer.toString(model1.getBatsmanScore()));
        batsManOneBallTV.setText("("+Integer.toString(model1.getBallFaced())+")");

    }
    private void setBatsmanTwoScore(){
        batsManTwoRunTV.setText(Integer.toString(model2.getBatsmanScore()));
        batsManTwoBallTV.setText("("+Integer.toString(model2.getBallFaced())+")");
    }
    private void setBowlerStats() {
        bowlerNameTV.setText(bowlerObj.getBowlerName());
        bowlerOverTV.setText(Integer.toString(bowlerObj.getOverCount())+"."+Integer.toString(bowlerObj.getBallCount()));
    }
    private void whenTheBatsmanIsOut() {
        String bowlerName = bowlerObj.getBowlerName();
        String status= "";
        if(typeOfWicketFall.equals(Constants.WICKET_BOLD)){
            status = "b "+bowlerName;
        }else if(typeOfWicketFall.equals(Constants.WICKET_CATCH)){
            status = "c "+fielderName+" b "+bowlerName;
        }else if(typeOfWicketFall.equals(Constants.WICKET_RUNOUT)){
            status = Constants.WICKET_RUNOUT+"("+fielderName+")";
        }else if(typeOfWicketFall.equals(Constants.WICKET_STUMMPED)){
            status = "st "+fielderName+" b "+bowlerName;
        }else if(typeOfWicketFall.equals(Constants.WICKET_LBW)){
            status = "lbw "+bowlerName;
        }else if(typeOfWicketFall.equals(Constants.WICKET_HITOUT)){
            status = "htwk "+bowlerName;
        }
        switch (strikePointer){
            case 0:
                model1.setStatus(status);
                batsmenPair[dismissedBatsmanIndex] = "";
                batsManOneTV.setText(batsmenPair[dismissedBatsmanIndex]);
                batsManOneRunTV.setText("");
                batsManOneBallTV.setText("");
                break;
            case 1:
                model2.setStatus(status);
                batsmenPair[dismissedBatsmanIndex] = "";
                batsManTwoTV.setText(batsmenPair[dismissedBatsmanIndex]);
                batsManTwoRunTV.setText("");
                batsManTwoBallTV.setText("");
                break;
        }
    }
    public void clearBatsmanOneTextFields(){
        batsManOneTV.setText("");
        batsManOneRunTV.setText("");
        batsManOneBallTV.setText("");
    }
    public void clearBatsmanTwoTextFields(){
        batsManTwoTV.setText("");
        batsManTwoRunTV.setText("");
        batsManTwoBallTV.setText("");
    }
    public void clearBowlerTextFields(){
        bowlerNameTV.setText("");
        bowlerOverTV.setText("");
    }
    private void startControlBtnAnimation(View view) {
        ObjectAnimator ctrlBtnAnimator = ObjectAnimator.ofInt(view,"backgroundColor", controlBtnColorPressed, controlBtnColor).setDuration(500);
        ctrlBtnAnimator.setEvaluator(new ArgbEvaluator());
        ctrlBtnAnimator.start();
    }
    private void startRunBtnAnimation(Button b) {
        ObjectAnimator btnAnimator = ObjectAnimator.ofInt(b,"backgroundColor", runBtnColorPressed, runBtnColor).setDuration(500);
        btnAnimator.setEvaluator(new ArgbEvaluator());
        btnAnimator.start();
    }
    private void initializeButtons(View view){
        extraWide = (FloatingActionButton) view.findViewById(R.id.extraWide);
        extraNo = (FloatingActionButton) view.findViewById(R.id.extraNo);
        extraBye = (FloatingActionButton) view.findViewById(R.id.extraBye);
        extraLegbye = (FloatingActionButton) view.findViewById(R.id.extraLegbye);
        wkBold = (FloatingActionButton) view.findViewById(R.id.wkBold);
        wkCatch = (FloatingActionButton) view.findViewById(R.id.wkCatch);
        wkLbw = (FloatingActionButton) view.findViewById(R.id.wkLbw);
        wkRunOut = (FloatingActionButton) view.findViewById(R.id.wkRunOut);
        wkStummped = (FloatingActionButton) view.findViewById(R.id.wkStummped);
        wkHitWicket = (FloatingActionButton) view.findViewById(R.id.wkHitWicket);
        zeroBtn = (Button) view.findViewById(R.id.runBtnZero);
        oneBtn = (Button) view.findViewById(R.id.runBtnOne);
        twoBtn = (Button) view.findViewById(R.id.runBtnTwo);
        threeBtn = (Button) view.findViewById(R.id.runBtnThree);
        fourBtn = (Button) view.findViewById(R.id.runBtnFour);
        fiveBtn = (Button) view.findViewById(R.id.runBtnFive);
        sixBtn = (Button) view.findViewById(R.id.runBtnSix);
        strikeBtn = (Button) view.findViewById(R.id.changeStrikeBtn);
        changeBatsmanBtn = (Button) view.findViewById(R.id.changeBatsmanBtn);
        changeBowlerBtn = (Button) view.findViewById(R.id.changeBowlerBtn);
        playBtn = (Button) view.findViewById(R.id.playBtn);
        addScoreBtn = (Button) view.findViewById(R.id.addScoreBtn);
        setupInningsBtn = (Button) view.findViewById(R.id.setupInningsBtn);
        clearDisplayBtn = (Button) view.findViewById(R.id.clearDisplayBtn);
    }
    private void initializeTextViews(View view) {
        vsTV = (TextView) view.findViewById(R.id.vsText);
        totalOverLabelTV = (TextView) view.findViewById(R.id.totalOverLabel);
        teamNameTV = (TextView) view.findViewById(R.id.teamName);
        scoreTV = (TextView) view.findViewById(R.id.teamTotalScore);
        overTV = (TextView) view.findViewById(R.id.over);
        runRateTV = (TextView) view.findViewById(R.id.runRate);
        reqRunRateTV = (TextView) view.findViewById(R.id.reqRunRate);
        targetTV = (TextView) view.findViewById(R.id.targetScore);
        extraScoreTV = (TextView) view.findViewById(R.id.extraScore);
        inningsLabel = (TextView) view.findViewById(R.id.inningsLabel);
        inputDisplayTV = (TextView) view.findViewById(R.id.inputDisplay);
        ballByBallStatTV = (TextView) view.findViewById(R.id.ballByBallStats);
        batsManOneTV = (TextView) view.findViewById(R.id.batsmanOne);
        batsManOneRunTV = (TextView) view.findViewById(R.id.batsmanOneRun);
        batsManOneBallTV = (TextView) view.findViewById(R.id.batsmanOneBall);
        batsManTwoTV = (TextView) view.findViewById(R.id.batsmanTwo);
        batsManTwoRunTV = (TextView) view.findViewById(R.id.batsmanTwoRun);
        batsManTwoBallTV = (TextView) view.findViewById(R.id.batsmanTwoBall);
        bowlerNameTV = (TextView) view.findViewById(R.id.bowler);
        bowlerOverTV = (TextView) view.findViewById(R.id.bowloerOver);
        /*batsManOneTV.setOnClickListener(selectBatsmanListener);
        batsManTwoTV.setOnClickListener(selectBatsmanListener);*/
    }
    private void enableOptionButtons(){
        strikeBtn.setEnabled(true);
        strikeBtn.setAlpha(1.0f);
        changeBatsmanBtn.setEnabled(true);
        changeBatsmanBtn.setAlpha(1.0f);
        changeBowlerBtn.setEnabled(true);
        changeBowlerBtn.setAlpha(1.0f);
    }
    private void disableOptionButtons(){
        strikeBtn.setEnabled(false);
        strikeBtn.setAlpha(0.5f);
        changeBatsmanBtn.setEnabled(false);
        changeBatsmanBtn.setAlpha(0.5f);
        changeBowlerBtn.setEnabled(false);
        changeBowlerBtn.setAlpha(0.5f);
    }
    private void enableScoreButtons(){
        zeroBtn.setEnabled(true);
        oneBtn.setEnabled(true);
        twoBtn.setEnabled(true);
        threeBtn.setEnabled(true);
        fourBtn.setEnabled(true);
        fiveBtn.setEnabled(true);
        sixBtn.setEnabled(true);
        extraWide.setEnabled(true);
        extraNo.setEnabled(true);
        extraBye.setEnabled(true);
        extraLegbye.setEnabled(true);
        wkBold.setEnabled(true);
        wkCatch.setEnabled(true);
        wkLbw.setEnabled(true);
        wkRunOut.setEnabled(true);
        wkStummped.setEnabled(true);
        zeroBtn.setAlpha(1.0f);
        oneBtn.setAlpha(1.0f);
        twoBtn.setAlpha(1.0f);
        threeBtn.setAlpha(1.0f);
        fourBtn.setAlpha(1.0f);
        fiveBtn.setAlpha(1.0f);
        sixBtn.setAlpha(1.0f);
        extraWide.setAlpha(1.0f);
        extraNo.setAlpha(1.0f);
        extraBye.setAlpha(1.0f);
        extraLegbye.setAlpha(1.0f);
        wkBold.setAlpha(1.0f);
        wkCatch.setAlpha(1.0f);
        wkLbw.setAlpha(1.0f);
        wkRunOut.setAlpha(1.0f);
        wkStummped.setAlpha(1.0f);
    }
    private void disableScoreButtons(){
        zeroBtn.setEnabled(false);
        oneBtn.setEnabled(false);
        twoBtn.setEnabled(false);
        threeBtn.setEnabled(false);
        fourBtn.setEnabled(false);
        fiveBtn.setEnabled(false);
        sixBtn.setEnabled(false);
        extraWide.setEnabled(false);
        extraNo.setEnabled(false);
        extraBye.setEnabled(false);
        extraLegbye.setEnabled(false);
        wkBold.setEnabled(false);
        wkCatch.setEnabled(false);
        wkLbw.setEnabled(false);
        wkRunOut.setEnabled(false);
        wkStummped.setEnabled(false);
        zeroBtn.setAlpha(0.5f);
        oneBtn.setAlpha(0.5f);
        twoBtn.setAlpha(0.5f);
        threeBtn.setAlpha(0.5f);
        fourBtn.setAlpha(0.5f);
        fiveBtn.setAlpha(0.5f);
        sixBtn.setAlpha(0.5f);
        extraWide.setAlpha(0.5f);
        extraNo.setAlpha(0.5f);
        extraBye.setAlpha(0.5f);
        extraLegbye.setAlpha(0.5f);
        wkBold.setAlpha(0.5f);
        wkCatch.setAlpha(0.5f);
        wkLbw.setAlpha(0.5f);
        wkRunOut.setAlpha(0.5f);
        wkStummped.setAlpha(0.5f);
    }
    private double calculateRequiredRunRate(int totalScore, int targetScore, int currentOver, int ballCount) {
        String overString = String.valueOf(new StringBuilder().append(currentOver).append(".").append(ballCount));
        double over = Double.parseDouble(overString);
        double remainingOvers = (double)totalOver - over;
        return (targetScore - totalScore) / remainingOvers;
    }
    private double calculateCurrentRunRate(int totalScore, int currentOver, int ballCount) {
        if(currentOver < 1){
            return (totalScore * 6) / ballCount;
        }else{
            String overString= String.valueOf(new StringBuilder().append(currentOver).append(".").append(ballCount));
            return totalScore / Double.parseDouble(overString);
        }
    }

    private void finalizeScore(){
        if(illegalDeliveryText.length() == 0 && wicketDeliveryText.length() == 0){
            totalScore += runPerBall;
            ballCount += 1;
            addIndividualScoreAlt();
        }
        else if((illegalDeliveryText.equals(Constants.WIDE_BALL) || illegalDeliveryText.equals(Constants.NO_BALL))
                && wicketDeliveryText.length() == 0){
            totalScore += runPerBall + Constants.ILLEGAL_DELIVERY_SCORE;
            if(illegalDeliveryText.equals(Constants.NO_BALL)){
                addIndividualScoreAlt();
            }
        }
        else if(wicketDeliveryText.length() > 0){
            if(illegalDeliveryText.length() == 0){
                totalScore += runPerBall;
                ballCount += 1;
                addIndividualScoreAlt();
            }else if(illegalDeliveryText.equals(Constants.WIDE_BALL)){
                totalScore += runPerBall + Constants.ILLEGAL_DELIVERY_SCORE;
            }else if(illegalDeliveryText.equals(Constants.NO_BALL)){
                totalScore += runPerBall + Constants.ILLEGAL_DELIVERY_SCORE;
                addIndividualScoreAlt();
            }
            whenTheBatsmanIsOut();
            batsmanIsOut = true;
            if(fallOfWicket < Constants.TOTAL_WICKETS){
                fallOfWicket += 1;
            }
            dismissedBatsmanIndex = strikePointer;
            addScoreBtn.setEnabled(false);
            addScoreBtn.setAlpha(0.5f);
            strikeBtn.setEnabled(false);
            strikeBtn.setAlpha(0.5f);
        }
        calculateExtraRuns();
        createBallStatString();
        bowlerObj.processOverAlt(runPerBall,illegalDeliveryText,byeDeliveryText,wicketDeliveryText,ballCount);
        setBowlerStats();
        if (strikePointer == 0) {
            setBatsmanOneScore();
        } else {
            setBatsmanTwoScore();
        }
        if (runPerBall % 2 != 0 && ballCount < 6) {
            autoChangeStrike();
        }
        if (ballCount == 6) {
            currentOver += 1;
            ballCount = 0;
            if (runPerBall % 2 == 0) {
                autoChangeStrike();
            }
            clearBowlerTextFields();
            ballStats = "";
        }
        runPerBall = 0;
        inputDisplayRunText = "";
        illegalDeliveryText = "";
        byeDeliveryText = "";
        wicketDeliveryText = "";
        inputDisplayTV.setText("");
        currentRunRate = calculateCurrentRunRate(totalScore, currentOver, ballCount);
        if (currentInnings == Constants.SECOND_INNINGS) {
            reqRunRate = calculateRequiredRunRate(totalScore, targetScore, currentOver, ballCount);
            reqRunRateTV.setText(String.format("%.2f", reqRunRate));
            processRemainingScores();
        }
        scoreTV.setText(Integer.toString(totalScore) + "/" + Integer.toString(fallOfWicket));
        overTV.setText(Integer.toString(currentOver) + "." + Integer.toString(ballCount) + " overs");
        runRateTV.setText(String.format("%.2f", currentRunRate));
        extraScoreTV.setText(String.format("%d",inningsExtra));
        checkEndOfInnings();
    }

    private void calculateExtraRuns() {
        if(illegalDeliveryText.length() > 0){
            inningsExtra += Constants.ILLEGAL_DELIVERY_SCORE;
        }
        if(byeDeliveryText.length() > 0){
            inningsExtra += runPerBall;
        }
    }

    private void addIndividualScoreAlt(){
        if(strikePointer == 0){
            if(byeDeliveryText.length() > 0){
                model1.processRun(0);
            }else{
                model1.processRun(runPerBall);
            }
        }else if(strikePointer == 1){
            if(byeDeliveryText.length() > 0){
                model2.processRun(0);
            }else{
                model2.processRun(runPerBall);
            }
        }
    }
    private void processRemainingScores(){
        int remainingRuns = targetScore - totalScore;
        int remainingBalls = ((totalOver - currentOver) * 6) - ballCount;
        inputDisplayTV.setText(secondInningsTeam+" requires "+remainingRuns+" run(s) from "+remainingBalls+" ball(s)");
    }
}

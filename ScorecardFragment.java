package com.example.user.quickscorer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScorecardFragment extends Fragment {
    private ScorecardBattingCustomAdapter scorecardBattingCustomAdapter;
    private ScorecardBowlingCustomAdapter scorecardBowlingCustomAdapter;
    private ArrayList<IndividualScoreModel> firstInningsBattingModels;
    private ArrayList<IndividualScoreModel> secondInningsBattingModels;
    private ArrayList<BowlerModel> firstInningsBowlingModels;
    private ArrayList<BowlerModel> secondInningsBowlingModels;
    private ListView listView;
    private ListView bowlerLV;
    private String firstInningsTeamName;
    private Scorecard scorecard;
    private Button showScore,firstInningsBtn,secondInningsBtn;
    private TextView inningsLabelTV;

    public ScorecardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showScore = (Button) getActivity().findViewById(R.id.show_scoreboard_btn);
        firstInningsBtn = (Button) getActivity().findViewById(R.id.firstInningsScoreBtn);
        secondInningsBtn = (Button) getActivity().findViewById(R.id.secondInningsScoreBtn);
        listView = (ListView) getActivity().findViewById(R.id.scorecardFirstList);
        bowlerLV = (ListView) getActivity().findViewById(R.id.firstBowlingScoreList);
        inningsLabelTV = (TextView) getActivity().findViewById(R.id.inningsLabel2);
        firstInningsBtn.setOnClickListener(MyListener);
        secondInningsBtn.setOnClickListener(MyListener);
        firstInningsBattingModels = new ArrayList<>();
        secondInningsBattingModels = new ArrayList<>();
        firstInningsBowlingModels = new ArrayList<>();
        secondInningsBowlingModels = new ArrayList<>();
        scorecard = new Scorecard();
        showScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareScoreCard();
            }
        });

    }

    private View.OnClickListener MyListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.firstInningsScoreBtn:
                    inningsLabelTV.setText(Constants.FIRST_INNINGS);
                    firstInningsBattingModels = scorecard.getFirstInningsBatsmanModels();
                    firstInningsBowlingModels = scorecard.getFirstInningsBowlerModels();
                    scorecardBattingCustomAdapter = new ScorecardBattingCustomAdapter(getContext(), firstInningsBattingModels);
                    scorecardBowlingCustomAdapter = new ScorecardBowlingCustomAdapter(getContext(), firstInningsBowlingModels);
                    listView.setAdapter(scorecardBattingCustomAdapter);
                    bowlerLV.setAdapter(scorecardBowlingCustomAdapter);
                    break;
                case R.id.secondInningsScoreBtn:
                    inningsLabelTV.setText(Constants.SECOND_INNINGS);
                    secondInningsBattingModels = scorecard.getSecondInningsBatsmanModels();
                    secondInningsBowlingModels = scorecard.getSecondInningsBowlerModels();
                    scorecardBattingCustomAdapter = new ScorecardBattingCustomAdapter(getContext(), secondInningsBattingModels);
                    scorecardBowlingCustomAdapter = new ScorecardBowlingCustomAdapter(getContext(), secondInningsBowlingModels);
                    listView.setAdapter(scorecardBattingCustomAdapter);
                    bowlerLV.setAdapter(scorecardBowlingCustomAdapter);
                    break;
            }
        }
    };

    private void prepareScoreCard() {
        if(scorecard.getCurrentInnings().equals(Constants.FIRST_INNINGS)){
            inningsLabelTV.setText(Constants.FIRST_INNINGS);
            firstInningsBattingModels = scorecard.getFirstInningsBatsmanModels();
            firstInningsBowlingModels = scorecard.getFirstInningsBowlerModels();
            scorecardBattingCustomAdapter = new ScorecardBattingCustomAdapter(getContext(), firstInningsBattingModels);
            scorecardBowlingCustomAdapter = new ScorecardBowlingCustomAdapter(getContext(), firstInningsBowlingModels);
            listView.setAdapter(scorecardBattingCustomAdapter);
            bowlerLV.setAdapter(scorecardBowlingCustomAdapter);
        }else if(scorecard.getCurrentInnings().equals(Constants.SECOND_INNINGS)){
            inningsLabelTV.setText(Constants.SECOND_INNINGS);
            secondInningsBattingModels = scorecard.getSecondInningsBatsmanModels();
            secondInningsBowlingModels = scorecard.getSecondInningsBowlerModels();
            scorecardBattingCustomAdapter = new ScorecardBattingCustomAdapter(getContext(), secondInningsBattingModels);
            scorecardBowlingCustomAdapter = new ScorecardBowlingCustomAdapter(getContext(), secondInningsBowlingModels);
            listView.setAdapter(scorecardBattingCustomAdapter);
            bowlerLV.setAdapter(scorecardBowlingCustomAdapter);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scorecard, container, false);
    }
}

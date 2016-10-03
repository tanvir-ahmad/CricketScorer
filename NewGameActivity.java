package com.example.user.quickscorer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.quickscorer.Database.TeamDatabaseSource;
import com.example.user.quickscorer.Database.TeamModel;

import java.util.ArrayList;

public class NewGameActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Spinner teamASpinner;
    private Spinner teamBSpinner;
    private Spinner tossWinnerSpinner;
    private Spinner batOrBallSpinner;
    private String tossWinner,tossWinnerElectsTo,firstTeam,secondTeam,firstInningsTeam,secondInningsTeam;
    private int totalOver,firstTeamId,secondTeamId;
    private static final String BAT_FIRST="Bat";
    private static final String BOWL_FIRST="Bowl";
    private EditText overET;
    private TeamDatabaseSource teamDatabaseSource;
    private ArrayList<TeamModel> teamModels;
    private CustomSpinnerAdapter teamsAdapter;
    private int id1,id2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        mToolbar = (Toolbar) findViewById(R.id.mToolBar);
        setSupportActionBar(mToolbar);
        teamDatabaseSource = new TeamDatabaseSource(this);
        teamModels = new ArrayList<>();
        teamModels = teamDatabaseSource.getAllTeams();
        overET= (EditText) findViewById(R.id.totalOvers);
        teamASpinner = (Spinner) findViewById(R.id.teamA);
        teamBSpinner = (Spinner) findViewById(R.id.teamB);
        tossWinnerSpinner = (Spinner) findViewById(R.id.tossWinnerTeam);
        batOrBallSpinner = (Spinner) findViewById(R.id.batOrBall);
        teamsAdapter = new CustomSpinnerAdapter(this,teamModels);
        ArrayAdapter<CharSequence> selectionAdapter=ArrayAdapter.createFromResource(this,R.array.batOrBall,android.R.layout.simple_spinner_dropdown_item);
        teamASpinner.setAdapter(teamsAdapter);
        teamBSpinner.setAdapter(teamsAdapter);
        tossWinnerSpinner.setAdapter(teamsAdapter);
        batOrBallSpinner.setAdapter(selectionAdapter);

        teamASpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                firstTeamId = teamModels.get(i).getTeamId();
                firstTeam = teamModels.get(i).getTeamName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        teamBSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                secondTeamId = teamModels.get(i).getTeamId();
                secondTeam = teamModels.get(i).getTeamName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tossWinnerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tossWinner = teamModels.get(i).getTeamName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        batOrBallSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tossWinnerElectsTo = batOrBallSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.startmenu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.view_teams:
                startActivity(new Intent(NewGameActivity.this,TeamListActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void proceedToMainGame(View view) {
        if(firstTeam.equals(secondTeam)){
            Toast.makeText(NewGameActivity.this, "Please select different teams", Toast.LENGTH_SHORT).show();
        }else{
            if(tossWinner.equals(firstTeam) && tossWinnerElectsTo.equals(BAT_FIRST)){
                firstInningsTeam = firstTeam;
                secondInningsTeam = secondTeam;
                id1 = firstTeamId;
                id2 = secondTeamId;
            }else if(tossWinner.equals(firstTeam) && tossWinnerElectsTo.equals(BOWL_FIRST)){
                firstInningsTeam = secondTeam;
                secondInningsTeam = firstTeam;
                id1 = secondTeamId;
                id2 = firstTeamId;
            }else if(tossWinner.equals(secondTeam) && tossWinnerElectsTo.equals(BAT_FIRST)){
                firstInningsTeam = secondTeam;
                secondInningsTeam = firstTeam;
                id1 = secondTeamId;
                id2 = firstTeamId;
            }else if(tossWinner.equals(secondTeam) && tossWinnerElectsTo.equals(BOWL_FIRST)){
                firstInningsTeam = firstTeam;
                secondInningsTeam = secondTeam;
                id1 = firstTeamId;
                id2 = secondTeamId;
            }
            if(overET.getText().length() == 0 || overET.getText().length() > 2){
                overET.setError("Please specify valid overs!");
            }else{
                totalOver = Integer.parseInt(overET.getText().toString());
                Intent intent = new Intent(NewGameActivity.this, MainActivity.class);
                intent.putExtra("firstInningsTeam", firstInningsTeam);
                intent.putExtra("secondInningsTeam", secondInningsTeam);
                intent.putExtra("tossWinner", tossWinner);
                intent.putExtra("electsTo", tossWinnerElectsTo);
                intent.putExtra("totalOver", totalOver);
                intent.putExtra("teamOneId", id1);
                intent.putExtra("teamTwoId", id2);
                startActivity(intent);
            }
        }
    }

    public void createNewTeam(View view) {
        startActivity(new Intent(NewGameActivity.this,CreateNewTeamActivity.class));
    }
}

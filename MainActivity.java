package com.example.user.quickscorer;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.user.quickscorer.Interface.ScorecardListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private SectionsPagerAdapter pagerAdapter;
    private static final int TOTAL_PAGE = 2;
    private String currentInnings,tossWinner,electsTo,firstInningsTeam,secondInningsTeam;
    private int totalOver,teamOneId,teamTwoId;
    private Bundle b;
    private ArrayList<IndividualScoreModel> firstInningsBattingScoreCard;
    private ArrayList<IndividualScoreModel> secondInningsBattingScoreCard;
    private ArrayList<BowlerModel> firstInningsBowlingScorecard;
    private ArrayList<BowlerModel> secondInningsBowlingScorecard;
    private Scorecard scorecard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scorecard = new Scorecard();
        firstInningsTeam = getIntent().getStringExtra("firstInningsTeam");
        secondInningsTeam = getIntent().getStringExtra("secondInningsTeam");
        tossWinner = getIntent().getStringExtra("tossWinner");
        electsTo = getIntent().getStringExtra("electsTo");
        totalOver = getIntent().getIntExtra("totalOver",0);
        teamOneId = getIntent().getIntExtra("teamOneId",0);
        teamTwoId = getIntent().getIntExtra("teamTwoId",0);
        firstInningsBattingScoreCard = new ArrayList<>();
        firstInningsBowlingScorecard = new ArrayList<>();
        secondInningsBattingScoreCard = new ArrayList<>();
        secondInningsBowlingScorecard = new ArrayList<>();
        b = new Bundle();
        mViewPager = (ViewPager) findViewById(R.id.scorecard_fragment_container);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mToolbar = (Toolbar) findViewById(R.id.mToolBar);
        setSupportActionBar(mToolbar);
        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    MainScoreFragment mainScoreFragment = new MainScoreFragment();
                    b.putString(Constants.FIRST_INNINGS_TEAM,firstInningsTeam);
                    b.putString(Constants.SECOND_INNINGS_TEAM,secondInningsTeam);
                    b.putString(Constants.TOSS_WINNER,tossWinner);
                    b.putString(Constants.ELECTS_TO,electsTo);
                    b.putInt(Constants.TOTAL_OVER,totalOver);
                    b.putInt(Constants.TEAM_ONE_ID,teamOneId);
                    b.putInt(Constants.TEAM_TWO_ID,teamTwoId);
                    mainScoreFragment.setArguments(b);
                    return mainScoreFragment;
                case 1:
                    ScorecardFragment scorecardFragment = new ScorecardFragment();
                    return scorecardFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return TOTAL_PAGE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Main Score";
                case 1:
                    return "Scorecard 1st";
                case 2:
                    return "Scorecard 2nd";
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_game:
                startActivity(new Intent(MainActivity.this,NewGameActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}

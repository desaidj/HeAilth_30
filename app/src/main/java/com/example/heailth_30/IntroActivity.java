package com.example.heailth_30;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    //declaring variables
    private ViewPager screenPager;
    IntroViewPageAdapter introViewPageAdapter;
    TabLayout tabIndicator;
    TextView btnSkip;
    ImageButton btnNext;
    int position = 0;
    Button btnGetStarted;
    Animation btnAnim_getStarted, btnAnim_skip;
    //END

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //making this activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //END

        //Automatically starts MainActivity if On_board animation is already viewed
        if (restorePrefData()){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        //END

        setContentView(R.layout.activity_intro);

        //init List
        List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Instant service", "Why to wait for days just for test result when you have an AI enhanced system on your side", R.drawable.img1));
        mList.add(new ScreenItem("Accurate results", "Hard and intensive training regiment design results to more accurate and clear AI system", R.drawable.img2));
        mList.add(new ScreenItem("Easy to use", "You just need to have an account and your x-ray file that's it!", R.drawable.img3));
        //END

        //init variables
        btnSkip = findViewById(R.id.skip_btn);
        btnNext = findViewById(R.id.nxt_btn);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnGetStarted = findViewById(R.id.get_started_btn);
        screenPager = findViewById(R.id.view_pager);
        introViewPageAdapter = new IntroViewPageAdapter(this, mList);
        btnAnim_getStarted = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.btn_animation);
        btnAnim_skip = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.btn_animation_skip);
        //END

    //btn_working

        //setter for variables
        screenPager.setAdapter(introViewPageAdapter);
        tabIndicator.setupWithViewPager(screenPager);
        //END

        //btnSkip action
        btnSkip.setAnimation(btnAnim_skip);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if (position < mList.size()){
                    position = mList.size();
                    screenPager.setCurrentItem(position);
                    btnSkip.setVisibility(View.INVISIBLE);
                }
            }
        });
        //END
        
        //btn_next action
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if (position < mList.size()){
                    position++;
                    screenPager.setCurrentItem(position);
                }
                if (position == mList.size()-1){  //this is when we reach to last sub_page
                    btnSkip.setVisibility(View.INVISIBLE);
                    loadLastScreen();
                }
            }
        });
        //END

        //tab_indicator btn action
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size()-1){
                    btnSkip.setVisibility(View.INVISIBLE);
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //END

        //get_started btn action
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                savePrefData();
                finish();
            }
        });
        //END

    //END
    }

    private boolean restorePrefData(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("user_pref", MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = pref.getBoolean("isIntroOpened", false);
        return isIntroActivityOpenedBefore;
    }

    private void savePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("user_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.commit();
    }

    private void loadLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        btnGetStarted.setAnimation(btnAnim_getStarted);
    }
}
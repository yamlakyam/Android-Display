package com.cnet.VisualAnalysis;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;

public class MainActivity extends AppCompatActivity {

    public interface KeyPress {
        void centerKey();

        void leftKey();

        void rightKey();

    }


    public interface VsmCardKeyPress {
        void dispatchKey(KeyEvent event);

    }

    KeyPress keyPress;
    VsmCardKeyPress vsmCardKeyPress;

    ImageView leftArrow;
    ImageView playPause;
    ImageView rightArrow;
    LinearLayout playPauseKeyPad;


    public static boolean secondCenterKeyPause = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        leftArrow = findViewById(R.id.leftArrow2);
        playPause = findViewById(R.id.playPause2);
        rightArrow = findViewById(R.id.rightArrow2);
        playPauseKeyPad = findViewById(R.id.playPauseKeyPad2);

        Intent intent = getIntent();
        String name = intent.getStringExtra("back");
        if (name != null) {
            if (name.equals("pressed")) {
                if (SplashScreenActivity.allData.getLayoutList().contains(2)) {
                    setHomeFragment(R.id.vsmTransactionFragment);
                } else {
                    setHomeFragment(R.id.vsmCardFragment);
                }
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        keyPress = (KeyPress) getCurrentFragment();

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:

                if (secondCenterKeyPause) {
                    playPause.setImageResource(R.drawable.ic_play_button__2_);
                    playPauseKeyPad.setVisibility(View.VISIBLE);
                    playPause.setImageTintList(ColorStateList.valueOf(
                            ContextCompat.getColor(getApplicationContext(), R.color.playbacksForeground)));
                    leftArrow.setImageTintList(ColorStateList.valueOf(
                            ContextCompat.getColor(getApplicationContext(), R.color.playbacksBackground)));
                    rightArrow.setImageTintList(ColorStateList.valueOf(
                            ContextCompat.getColor(getApplicationContext(), R.color.playbacksBackground)));
                } else {
                    playPauseKeyPad.setVisibility(View.GONE);
                    playPause.setImageResource(R.drawable.ic_pause_button);
                }

                keyPress.centerKey();
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                keyPress.leftKey();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                keyPress.rightKey();
                break;
        }
        return false;
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (getCurrentFragment() instanceof VsmCardFragment) {
//            vsmCardKeyPress = (VsmCardKeyPress) getCurrentFragment();
//            vsmCardKeyPress.dispatchKey(event);
//        }
//
//        return super.dispatchKeyEvent(event);
////        return true;
//    }

    public Fragment getCurrentFragment() {
        Fragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
        return navHostFragment.getChildFragmentManager().getFragments().get(0);
    }

    public void setHomeFragment(int fragmentId) {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavInflater inflater = navHostFragment.getNavController().getNavInflater();
        NavGraph graph = inflater.inflate(R.navigation.main_nav);
        graph.setStartDestination(fragmentId);
        NavController navController = navHostFragment.getNavController();
        navController.setGraph(graph);
    }

}
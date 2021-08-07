package com.cnet.VisualAnalysis;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Fragments.BranchSummaryFragment;
import com.cnet.VisualAnalysis.Fragments.SummarizedByArticleChildCategFragment;
import com.cnet.VisualAnalysis.Fragments.SummarizedByArticleFragment;
import com.cnet.VisualAnalysis.Fragments.SummarizedByArticleParentCategFragment;
import com.cnet.VisualAnalysis.Fragments.SummaryOfLastMonthFragment;
import com.cnet.VisualAnalysis.Fragments.SummaryOfLastSixMonthsFragment;

import org.json.JSONArray;

public class SecondActivity extends AppCompatActivity {
    public static JSONArray dashBoardArray;
    public static DashBoardData dashBoardData;

    NavController navController;

    public static boolean summaryByArticlePause = false;
    public static boolean summaryByParentArticlePause = false;
    public static boolean summaryByChildArticlePause = false;
    public static boolean summaryOfLast6MonsPause = false;
    public static boolean summaryOfLast30DaysPause = false;
    public static boolean summaryOfBranchPause = false;

    public static boolean firstCenterKeyPause = false;

    ImageView leftArrow;
    ImageView playPause;
    ImageView rightArrow;
    LinearLayout playPauseKeyPad;

    public static Context context;

    public boolean[] visibleFragments = {true, true, true, false, true, true};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = SecondActivity.this;
        setContentView(R.layout.activity_second);
        setHomeFragment();


        leftArrow = findViewById(R.id.leftArrow);
        playPause = findViewById(R.id.playPause);
        rightArrow = findViewById(R.id.rightArrow);
        playPauseKeyPad = findViewById(R.id.playPauseKeyPad);

    }

    public void setHomeFragment() {
        int frgamentId;
//        mappedFragment();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_second);
        NavInflater inflater = navHostFragment.getNavController().getNavInflater();
        NavGraph graph = inflater.inflate(R.navigation.second_nav);

        graph.setStartDestination(mappedFragment());


        NavController navController = navHostFragment.getNavController();
        navController.setGraph(graph);

    }

    public int mappedFragment() {
//        int frgamentId = R.id.branchSummaryFragment;
//        if (SplashScreenActivity.allData.getLayoutList().contains(3)) {
//            frgamentId = R.id.summarizedByArticleFragment2;
//        } else if (SplashScreenActivity.allData.getLayoutList().contains(4)) {
//            frgamentId = R.id.summarizedByArticleParentCategFragment;
//        } else if (SplashScreenActivity.allData.getLayoutList().contains(5)) {
//            frgamentId = R.id.summarizedByArticleChildCategFragment;
//        } else if (SplashScreenActivity.allData.getLayoutList().contains(6)) {
//            frgamentId = R.id.summaryOfLastSixMonthsFragment;
//        } else if (SplashScreenActivity.allData.getLayoutList().contains(7)) {
//            frgamentId = R.id.summaryOfLastMonthFragment;
//        } else if (SplashScreenActivity.allData.getLayoutList().contains(8)) {
//            frgamentId = R.id.branchSummaryFragment;
//        }

//        int frgamentId = R.id.userReportForAllOusFragment;
        int frgamentId = R.id.peakHourReportFragment;
//        int frgamentId = R.id.userReportForAllOusFragment2;
        return frgamentId;
    }

    @SuppressLint("HardwareIds")
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (SplashScreenActivity.allData.isEnableNavigation()) {
            navController = NavHostFragment.findNavController(getCurrentFragment());
            switch (keyCode) {

                case KeyEvent.KEYCODE_DPAD_CENTER:
                    playPause.setImageTintList(ColorStateList.valueOf(
                            ContextCompat.getColor(getApplicationContext(), R.color.playbacksForeground)));
                    leftArrow.setImageTintList(ColorStateList.valueOf(
                            ContextCompat.getColor(getApplicationContext(), R.color.playbacksBackground)));
                    rightArrow.setImageTintList(ColorStateList.valueOf(
                            ContextCompat.getColor(getApplicationContext(), R.color.playbacksBackground)));
//                SummarizedByArticleFragment.isInflatingTable = false;

                    if (!firstCenterKeyPause) {
                        //cc70a81e8233444a
                        playPause.setImageResource(R.drawable.ic_pause_button);

                        summaryByParentArticlePause = false;
                        summaryByArticlePause = false;
                        summaryByChildArticlePause = false;
                        summaryOfLast6MonsPause = false;
                        summaryOfLast30DaysPause = false;
                        summaryOfBranchPause = false;

//                            navigate(getCurrentFragment());

                        rightNavigation();
                        firstCenterKeyPause = true;
                        playPauseKeyPad.setVisibility(View.GONE);

                    } else {
                        playPause.setImageResource(R.drawable.ic_play_button__2_);
                        pausedState();
                    }

                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:

                    SummarizedByArticleFragment.isInflatingTable = false;
                    SummarizedByArticleParentCategFragment.isInflatingTable = false;
                    SummarizedByArticleChildCategFragment.isInflatingTable = false;
                    SummaryOfLastSixMonthsFragment.isInflatingTable = false;
                    SummaryOfLastMonthFragment.isInflatingTable = false;
                    BranchSummaryFragment.isInflatingTable = false;

                    leftArrow.setImageTintList(ColorStateList.valueOf(
                            ContextCompat.getColor(getApplicationContext(), R.color.playbacksForeground)));
                    rightArrow.setImageTintList(ColorStateList.valueOf(
                            ContextCompat.getColor(getApplicationContext(), R.color.playbacksBackground)));
//                playPause.setImageTintList(ColorStateList.valueOf(
//                        ContextCompat.getColor(getApplicationContext(), R.color.playbacksForeground)));

                    leftNavigation();
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:

                    SummarizedByArticleFragment.isInflatingTable = false;
                    SummarizedByArticleParentCategFragment.isInflatingTable = false;
                    SummarizedByArticleChildCategFragment.isInflatingTable = false;
                    SummaryOfLastSixMonthsFragment.isInflatingTable = false;
                    SummaryOfLastMonthFragment.isInflatingTable = false;
                    BranchSummaryFragment.isInflatingTable = false;

                    rightArrow.setImageTintList(ColorStateList.valueOf(
                            ContextCompat.getColor(getApplicationContext(), R.color.playbacksForeground)));
//                playPause.setImageTintList(ColorStateList.valueOf(
//                        ContextCompat.getColor(getApplicationContext(), R.color.playbacksForeground)));
                    leftArrow.setImageTintList(ColorStateList.valueOf(
                            ContextCompat.getColor(getApplicationContext(), R.color.playbacksBackground)));

                    rightNavigation();
                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public Fragment getCurrentFragment() {
        Fragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
        return navHostFragment.getChildFragmentManager().getFragments().get(0);
    }

    public void leftNavigation() {
        if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummarizedByArticleFragment")) {
            interrupThreads(SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    SummarizedByArticleFragment.handleRowAnimationThread);

            if (SplashScreenActivity.allData.getLayoutList().contains(3)) {

                if (SplashScreenActivity.allData.getLayoutList().contains(1))
                    startActivity(new Intent(SecondActivity.this, MapsActivity.class));
                else if (SplashScreenActivity.allData.getLayoutList().contains(8))
                    navController.navigate(R.id.branchSummaryFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(7))
                    navController.navigate(R.id.summaryOfLastMonthFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(6))
                    navController.navigate(R.id.summaryOfLastSixMonthsFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(5))
                    navController.navigate(R.id.summarizedByArticleChildCategFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(4))
                    navController.navigate(R.id.summarizedByArticleParentCategFragment);

            }

        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummarizedByArticleParentCategFragment")) {
            interrupThreads(SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);

            if (SplashScreenActivity.allData.getLayoutList().contains(4)) {
                if (SplashScreenActivity.allData.getLayoutList().contains(3))
                    navController.navigate(R.id.summarizedByArticleFragment2);
                else if (SplashScreenActivity.allData.getLayoutList().contains(1))
                    startActivity(new Intent(SecondActivity.this, MapsActivity.class));
                else if (SplashScreenActivity.allData.getLayoutList().contains(8))
                    navController.navigate(R.id.branchSummaryFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(7))
                    navController.navigate(R.id.summaryOfLastMonthFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(6))
                    navController.navigate(R.id.summaryOfLastSixMonthsFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(5))
                    navController.navigate(R.id.summarizedByArticleChildCategFragment);
            }

        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummarizedByArticleChildCategFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);

            if (SplashScreenActivity.allData.getLayoutList().contains(5)) {
                if (SplashScreenActivity.allData.getLayoutList().contains(4))
                    navController.navigate(R.id.summarizedByArticleParentCategFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(3))
                    navController.navigate(R.id.summarizedByArticleFragment2);
                else if (SplashScreenActivity.allData.getLayoutList().contains(1))
                    startActivity(new Intent(SecondActivity.this, MapsActivity.class));
                else if (SplashScreenActivity.allData.getLayoutList().contains(8))
                    navController.navigate(R.id.branchSummaryFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(7))
                    navController.navigate(R.id.summaryOfLastMonthFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(6))
                    navController.navigate(R.id.summaryOfLastSixMonthsFragment);

            }

        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummaryOfLastSixMonthsFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);

            if (SplashScreenActivity.allData.getLayoutList().contains(6)) {
                if (SplashScreenActivity.allData.getLayoutList().contains(5))
                    navController.navigate(R.id.summarizedByArticleChildCategFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(4))
                    navController.navigate(R.id.summarizedByArticleParentCategFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(3))
                    navController.navigate(R.id.summarizedByArticleFragment2);
                else if (SplashScreenActivity.allData.getLayoutList().contains(1))
                    startActivity(new Intent(SecondActivity.this, MapsActivity.class));
                else if (SplashScreenActivity.allData.getLayoutList().contains(8))
                    navController.navigate(R.id.branchSummaryFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(7))
                    navController.navigate(R.id.summaryOfLastMonthFragment);

            }
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummaryOfLastMonthFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);
            if (SplashScreenActivity.allData.getLayoutList().contains(7)) {
                if (SplashScreenActivity.allData.getLayoutList().contains(6))
                    navController.navigate(R.id.summaryOfLastSixMonthsFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(5))
                    navController.navigate(R.id.summarizedByArticleChildCategFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(4))
                    navController.navigate(R.id.summarizedByArticleParentCategFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(3))
                    navController.navigate(R.id.summarizedByArticleFragment2);
                else if (SplashScreenActivity.allData.getLayoutList().contains(1))
                    startActivity(new Intent(SecondActivity.this, MapsActivity.class));
                else if (SplashScreenActivity.allData.getLayoutList().contains(8))
                    navController.navigate(R.id.branchSummaryFragment);

            }
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.BranchSummaryFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);

            if (SplashScreenActivity.allData.getLayoutList().contains(8)) {
                if (SplashScreenActivity.allData.getLayoutList().contains(7))
                    navController.navigate(R.id.summaryOfLastMonthFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(6))
                    navController.navigate(R.id.summaryOfLastSixMonthsFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(5))
                    navController.navigate(R.id.summarizedByArticleChildCategFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(4))
                    navController.navigate(R.id.summarizedByArticleParentCategFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(3))
                    navController.navigate(R.id.summarizedByArticleFragment2);
                else if (SplashScreenActivity.allData.getLayoutList().contains(1))
                    startActivity(new Intent(SecondActivity.this, MapsActivity.class));

            }
        }
    }

    public void rightNavigation() {
        if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummarizedByArticleFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);


            if (SplashScreenActivity.allData.getLayoutList().contains(3)) {
                if (SplashScreenActivity.allData.getLayoutList().contains(4)) {
                    navController.navigate(R.id.summarizedByArticleParentCategFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(5)) {
                    navController.navigate(R.id.summarizedByArticleChildCategFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(6)) {
                    navController.navigate(R.id.summaryOfLastSixMonthsFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(7)) {
                    navController.navigate(R.id.summaryOfLastMonthFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(8)) {
                    navController.navigate(R.id.branchSummaryFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
                    startActivity(new Intent(SecondActivity.this, MapsActivity.class));
                }

            }


        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummarizedByArticleParentCategFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);

            if (SplashScreenActivity.allData.getLayoutList().contains(4)) {

                if (SplashScreenActivity.allData.getLayoutList().contains(5))
                    navController.navigate(R.id.summarizedByArticleChildCategFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(6))
                    navController.navigate(R.id.summaryOfLastSixMonthsFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(7))
                    navController.navigate(R.id.summaryOfLastMonthFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(8))
                    navController.navigate(R.id.branchSummaryFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(1))
                    startActivity(new Intent(SecondActivity.this, MapsActivity.class));
                else if (SplashScreenActivity.allData.getLayoutList().contains(3))
                    navController.navigate(R.id.summarizedByArticleFragment2);

            }


        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummarizedByArticleChildCategFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);

            if (SplashScreenActivity.allData.getLayoutList().contains(5)) {

                if (SplashScreenActivity.allData.getLayoutList().contains(6))
                    navController.navigate(R.id.summaryOfLastSixMonthsFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(7))
                    navController.navigate(R.id.summaryOfLastMonthFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(8))
                    navController.navigate(R.id.branchSummaryFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(1))
                    startActivity(new Intent(SecondActivity.this, MapsActivity.class));
                else if (SplashScreenActivity.allData.getLayoutList().contains(3))
                    navController.navigate(R.id.summarizedByArticleFragment2);
                else if (SplashScreenActivity.allData.getLayoutList().contains(4))
                    navController.navigate(R.id.summarizedByArticleParentCategFragment);

            }


        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummaryOfLastSixMonthsFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);

            if (SplashScreenActivity.allData.getLayoutList().contains(6)) {
                if (SplashScreenActivity.allData.getLayoutList().contains(7))
                    navController.navigate(R.id.summaryOfLastMonthFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(8))
                    navController.navigate(R.id.branchSummaryFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(1))
                    startActivity(new Intent(SecondActivity.this, MapsActivity.class));
                else if (SplashScreenActivity.allData.getLayoutList().contains(3))
                    navController.navigate(R.id.summarizedByArticleFragment2);
                else if (SplashScreenActivity.allData.getLayoutList().contains(4))
                    navController.navigate(R.id.summarizedByArticleParentCategFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(5))
                    navController.navigate(R.id.summarizedByArticleChildCategFragment);

            }

        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummaryOfLastMonthFragment")) {
            interrupThreads(SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    SummarizedByArticleFragment.handleRowAnimationThread);

            if (SplashScreenActivity.allData.getLayoutList().contains(7)) {

                if (SplashScreenActivity.allData.getLayoutList().contains(8))
                    navController.navigate(R.id.branchSummaryFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(1))
                    startActivity(new Intent(SecondActivity.this, MapsActivity.class));
                else if (SplashScreenActivity.allData.getLayoutList().contains(3))
                    navController.navigate(R.id.summarizedByArticleFragment2);
                else if (SplashScreenActivity.allData.getLayoutList().contains(4))
                    navController.navigate(R.id.summarizedByArticleParentCategFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(5))
                    navController.navigate(R.id.summarizedByArticleChildCategFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(6))
                    navController.navigate(R.id.summaryOfLastSixMonthsFragment);

            }

        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.BranchSummaryFragment")) {
            interrupThreads(SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);


            if (SplashScreenActivity.allData.getLayoutList().contains(8)) {
                if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
                    startActivity(new Intent(SecondActivity.this, MapsActivity.class));
                } else if (SplashScreenActivity.allData.getLayoutList().contains(3))
                    navController.navigate(R.id.summarizedByArticleFragment2);
                else if (SplashScreenActivity.allData.getLayoutList().contains(4))
                    navController.navigate(R.id.summarizedByArticleParentCategFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(5))
                    navController.navigate(R.id.summarizedByArticleChildCategFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(6))
                    navController.navigate(R.id.summaryOfLastSixMonthsFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(7))
                    navController.navigate(R.id.summaryOfLastMonthFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(8))
                    navController.navigate(R.id.branchSummaryFragment);
            }

        }
    }

    public void pausedState() {
        firstCenterKeyPause = false;
        playPauseKeyPad.setVisibility(View.VISIBLE);
        summaryByParentArticlePause = true;
        summaryByArticlePause = true;
        summaryByChildArticlePause = true;
        summaryOfLast6MonsPause = true;
        summaryOfLast30DaysPause = true;
        summaryOfBranchPause = true;
    }

    public static void interrupThreads(Thread firstThread, Thread secondThread, Thread thirdThread, Thread fourthThread, Thread fifthThread) {
        try {
            firstThread.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            secondThread.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            thirdThread.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            fourthThread.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            fifthThread.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void naviagateToFragment(int frgamentIndex) {
        if (frgamentIndex == 0) {
            navController.navigate(R.id.summarizedByArticleFragment2);
        } else if (frgamentIndex == 1) {
            navController.navigate(R.id.summarizedByArticleParentCategFragment);

        } else if (frgamentIndex == 2) {
            navController.navigate(R.id.summarizedByArticleChildCategFragment);

        } else if (frgamentIndex == 3) {
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);

        } else if (frgamentIndex == 4) {
            navController.navigate(R.id.summaryOfLastMonthFragment);

        } else if (frgamentIndex == 5) {
            navController.navigate(R.id.branchSummaryFragment);

        }
    }
}
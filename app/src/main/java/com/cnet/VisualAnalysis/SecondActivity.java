package com.cnet.VisualAnalysis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Fragments.BranchSummaryFragment;
import com.cnet.VisualAnalysis.Fragments.PeakHourReportForAllOusFragment;
import com.cnet.VisualAnalysis.Fragments.PeakHourReportFragment;
import com.cnet.VisualAnalysis.Fragments.SummarizedByArticleChildCategFragment;
import com.cnet.VisualAnalysis.Fragments.SummarizedByArticleFragment;
import com.cnet.VisualAnalysis.Fragments.SummarizedByArticleParentCategFragment;
import com.cnet.VisualAnalysis.Fragments.SummaryOfLastMonthFragment;
import com.cnet.VisualAnalysis.Fragments.SummaryOfLastSixMonthsFragment;
import com.cnet.VisualAnalysis.Fragments.UserReportForAllOusFragment;
import com.cnet.VisualAnalysis.Fragments.UserReportForEachOuFragment;

public class SecondActivity extends AppCompatActivity {

    public interface KeyPress {
        void centerKey();

        void leftKey();

        void rightKey();
    }

    public static DashBoardData dashBoardData;

    NavController navController;

    //    public static boolean summaryByArticlePause = false;
    //    public static boolean summaryByParentArticlePause = false;
//    public static boolean summaryByChildArticlePause = false;
//    public static boolean summaryOfLast6MonsPause = false;
//    public static boolean summaryOfLast30DaysPause = false;
//    public static boolean summaryOfBranchPause = false;
    //    public static boolean firstCenterKeyPause = false;
    public static boolean firstCenterKeyPause;

    ImageView leftArrow;
    ImageView playPause;
    ImageView rightArrow;
    LinearLayout playPauseKeyPad;

    public static Context context;


    KeyPress keyPress;


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


        Intent intent = getIntent();
        String name = intent.getStringExtra("left");
        if (name != null) {
            Log.i("Message", name);
            if (name.equals("pressed")) {
                if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                    setHomeFragment(R.id.peakHourReportFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
                    setHomeFragment(R.id.peakHourReportForAllOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                    setHomeFragment(R.id.userReportForEachOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                    setHomeFragment(R.id.userReportForAllOusFragment2);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0) {
                    setHomeFragment(R.id.branchSummaryFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(7)) {
                    setHomeFragment(R.id.summaryOfLastMonthFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(6)) {
                    setHomeFragment(R.id.summaryOfLastSixMonthsFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(5)) {
                    setHomeFragment(R.id.summarizedByArticleChildCategFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(4)) {
                    setHomeFragment(R.id.summarizedByArticleParentCategFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(3)) {
                    setHomeFragment(R.id.summarizedByArticleFragment2);
                }
            }
        }

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

    public void setHomeFragment(int fragment) {
        int frgamentId;
//        mappedFragment();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_second);
        NavInflater inflater = navHostFragment.getNavController().getNavInflater();
        NavGraph graph = inflater.inflate(R.navigation.second_nav);

        graph.setStartDestination(fragment);

        NavController navController = navHostFragment.getNavController();
        navController.setGraph(graph);

    }

    public int mappedFragment() {
        int frgamentId = R.id.branchSummaryFragment;
        if (SplashScreenActivity.allData.getLayoutList().contains(3)) {
            frgamentId = R.id.summarizedByArticleFragment2;
        } else if (SplashScreenActivity.allData.getLayoutList().contains(4)) {
            frgamentId = R.id.summarizedByArticleParentCategFragment;
        } else if (SplashScreenActivity.allData.getLayoutList().contains(5)) {
            frgamentId = R.id.summarizedByArticleChildCategFragment;
        } else if (SplashScreenActivity.allData.getLayoutList().contains(6)) {
            frgamentId = R.id.summaryOfLastSixMonthsFragment;
        } else if (SplashScreenActivity.allData.getLayoutList().contains(7)) {
            frgamentId = R.id.summaryOfLastMonthFragment;
        } else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0) {
            frgamentId = R.id.branchSummaryFragment;
        } else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
            frgamentId = R.id.userReportForAllOusFragment2;
        } else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
            frgamentId = R.id.userReportForEachOusFragment;
        } else if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
            frgamentId = R.id.peakHourReportForAllOusFragment;
        } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
            frgamentId = R.id.peakHourReportFragment;
        }

//        int frgamentId = R.id.userReportForAllOusFragment;
//        int frgamentId = R.id.peakHourReportFragment;
//        int frgamentId = R.id.userReportForAllOusFragment2;
//        int frgamentId = R.id.peakHourReportForAllOusFragment;
//        int frgamentId = R.id.peakHourReportForAllOusFragment;
//        int frgamentId = R.id.vansOfASingleOrganizationFragment;

        return frgamentId;
    }

    @SuppressLint("HardwareIds")
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getCurrentFragment() instanceof KeyPress)
            keyPress = (KeyPress) getCurrentFragment();


        if (SplashScreenActivity.allData.isEnableNavigation()) {
            navController = NavHostFragment.findNavController(getCurrentFragment());
            switch (keyCode) {

                case KeyEvent.KEYCODE_DPAD_CENTER:

//                    playPause.setImageTintList(ColorStateList.valueOf(
//                            ContextCompat.getColor(getApplicationContext(), R.color.playbacksForeground)));
//                    leftArrow.setImageTintList(ColorStateList.valueOf(
//                            ContextCompat.getColor(getApplicationContext(), R.color.playbacksBackground)));
//                    rightArrow.setImageTintList(ColorStateList.valueOf(
//                            ContextCompat.getColor(getApplicationContext(), R.color.playbacksBackground)));
//                SummarizedByArticleFragment.isInflatingTable = false;
//                    if (firstCenterKeyPause) {
//                        playPause.setImageResource(R.drawable.ic_play_button__2_);
//                        playPauseKeyPad.setVisibility(View.VISIBLE);
//                    } else {
//                        playPauseKeyPad.setVisibility(View.GONE);
//                    }
                    if (getCurrentFragment() instanceof KeyPress) {
                        keyPress.centerKey();
                    }
//                    else {
//                        if (!firstCenterKeyPause) {
//                            playPause.setImageResource(R.drawable.ic_pause_button);
//
//                            rightNavigation();
//                            firstCenterKeyPause = true;
//                            playPauseKeyPad.setVisibility(View.GONE);
//
//                        } else {
//                            playPause.setImageResource(R.drawable.ic_play_button__2_);
//                            pausedState();
//                        }
//                    }


                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:

                    SummarizedByArticleFragment.isInflatingTable = false;
                    SummarizedByArticleParentCategFragment.isInflatingTable = false;
                    SummarizedByArticleChildCategFragment.isInflatingTable = false;
                    SummaryOfLastSixMonthsFragment.isInflatingTable = false;
//                    SummaryOfLastMonthFragment.isInflatingTable = false;
//                    BranchSummaryFragment.isInflatingTable = false;

//                    leftArrow.setImageTintList(ColorStateList.valueOf(
//                            ContextCompat.getColor(getApplicationContext(), R.color.playbacksForeground)));
//                    rightArrow.setImageTintList(ColorStateList.valueOf(
//                            ContextCompat.getColor(getApplicationContext(), R.color.playbacksBackground)));
//                playPause.setImageTintList(ColorStateList.valueOf(
//                        ContextCompat.getColor(getApplicationContext(), R.color.playbacksForeground)));

                    if (getCurrentFragment() instanceof KeyPress)
                        keyPress.leftKey();
                    else
                        leftNavigation();
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:

                    SummarizedByArticleFragment.isInflatingTable = false;
                    SummarizedByArticleParentCategFragment.isInflatingTable = false;
                    SummarizedByArticleChildCategFragment.isInflatingTable = false;
                    SummaryOfLastSixMonthsFragment.isInflatingTable = false;
//                    SummaryOfLastMonthFragment.isInflatingTable = false;
//                    BranchSummaryFragment.isInflatingTable = false;

//                    rightArrow.setImageTintList(ColorStateList.valueOf(
//                            ContextCompat.getColor(getApplicationContext(), R.color.playbacksForeground)));
////                playPause.setImageTintList(ColorStateList.valueOf(
////                        ContextCompat.getColor(getApplicationContext(), R.color.playbacksForeground)));
//                    leftArrow.setImageTintList(ColorStateList.valueOf(
//                            ContextCompat.getColor(getApplicationContext(), R.color.playbacksBackground)));

                    if (getCurrentFragment() instanceof KeyPress)
                        keyPress.rightKey();
                    else
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(12))
                    navController.navigate(R.id.peakHourReportFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(11))
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                    navController.navigate(R.id.userReportForEachOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                    navController.navigate(R.id.userReportForAllOusFragment2);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(12))
                    navController.navigate(R.id.peakHourReportFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(11))
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                    navController.navigate(R.id.userReportForEachOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                    navController.navigate(R.id.userReportForAllOusFragment2);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(12))
                    navController.navigate(R.id.peakHourReportFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(11))
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                    navController.navigate(R.id.userReportForEachOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                    navController.navigate(R.id.userReportForAllOusFragment2);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(12))
                    navController.navigate(R.id.peakHourReportFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(11))
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                    navController.navigate(R.id.userReportForEachOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                    navController.navigate(R.id.userReportForAllOusFragment2);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(12))
                    navController.navigate(R.id.peakHourReportFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(11))
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                    navController.navigate(R.id.userReportForEachOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                    navController.navigate(R.id.userReportForAllOusFragment2);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                    navController.navigate(R.id.branchSummaryFragment);

            }
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.BranchSummaryFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);

            if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0) {
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(12))
                    navController.navigate(R.id.peakHourReportFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(11))
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                    navController.navigate(R.id.userReportForEachOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                    navController.navigate(R.id.userReportForAllOusFragment2);
                }

            }
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.UserReportForAllOusFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);

            if (SplashScreenActivity.allData.getLayoutList().contains(9)) {

                if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                    navController.navigate(R.id.branchSummaryFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(7))
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(12))
                    navController.navigate(R.id.peakHourReportFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(11))
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(10))
                    navController.navigate(R.id.userReportForEachOusFragment);


            }
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.UserReportForEachOuFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);

            if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                    navController.navigate(R.id.userReportForAllOusFragment2);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0) {
                    navController.navigate(R.id.branchSummaryFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(7))
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(12))
                    navController.navigate(R.id.peakHourReportFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(11))
                    navController.navigate(R.id.peakHourReportForAllOusFragment);

            }

        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.PeakHourReportForAllOusFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);

            if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
                if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                    navController.navigate(R.id.userReportForEachOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                    navController.navigate(R.id.userReportForAllOusFragment2);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0) {
                    navController.navigate(R.id.branchSummaryFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(7))
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(12))
                    navController.navigate(R.id.peakHourReportFragment);

            }
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.PeakHourReportFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);

            if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                if (SplashScreenActivity.allData.getLayoutList().contains(11))
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(10))
                    navController.navigate(R.id.userReportForEachOusFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(9))
                    navController.navigate(R.id.userReportForAllOusFragment2);
                else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                    navController.navigate(R.id.branchSummaryFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(7))
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(12))
                    navController.navigate(R.id.peakHourReportFragment);


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
                } else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0) {
                    navController.navigate(R.id.branchSummaryFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                    navController.navigate(R.id.userReportForAllOusFragment2);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                    navController.navigate(R.id.userReportForEachOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                    navController.navigate(R.id.peakHourReportFragment);
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                    navController.navigate(R.id.branchSummaryFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                    navController.navigate(R.id.userReportForAllOusFragment2);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                    navController.navigate(R.id.userReportForEachOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                    navController.navigate(R.id.peakHourReportFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(1))
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                    navController.navigate(R.id.branchSummaryFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                    navController.navigate(R.id.userReportForAllOusFragment2);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                    navController.navigate(R.id.userReportForEachOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                    navController.navigate(R.id.peakHourReportFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(1))
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                    navController.navigate(R.id.branchSummaryFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                    navController.navigate(R.id.userReportForAllOusFragment2);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                    navController.navigate(R.id.userReportForEachOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                    navController.navigate(R.id.peakHourReportFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(1))
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

                if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                    navController.navigate(R.id.branchSummaryFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                    navController.navigate(R.id.userReportForAllOusFragment2);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                    navController.navigate(R.id.userReportForEachOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                    navController.navigate(R.id.peakHourReportFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(1))
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
                if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                    navController.navigate(R.id.userReportForAllOusFragment2);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                    navController.navigate(R.id.userReportForEachOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                    navController.navigate(R.id.peakHourReportFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                    navController.navigate(R.id.branchSummaryFragment);
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                    navController.navigate(R.id.branchSummaryFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                    navController.navigate(R.id.userReportForAllOusFragment2);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                    navController.navigate(R.id.userReportForEachOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                    navController.navigate(R.id.peakHourReportFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(1))
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

                if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                    navController.navigate(R.id.branchSummaryFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                    navController.navigate(R.id.userReportForAllOusFragment2);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                    navController.navigate(R.id.userReportForEachOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                    navController.navigate(R.id.peakHourReportFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(1))
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


            if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0) {
                if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                    navController.navigate(R.id.userReportForAllOusFragment2);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                    navController.navigate(R.id.userReportForEachOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                    navController.navigate(R.id.peakHourReportFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                    navController.navigate(R.id.branchSummaryFragment);
            }

        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.UserReportForAllOusFragment")) {
            interrupThreads(SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);


            if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                    navController.navigate(R.id.userReportForEachOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                    navController.navigate(R.id.peakHourReportFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                    navController.navigate(R.id.branchSummaryFragment);
            }

        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.UserReportForEachOuFragment")) {
            interrupThreads(SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);


            if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                    navController.navigate(R.id.peakHourReportFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                    navController.navigate(R.id.branchSummaryFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(9))
                    navController.navigate(R.id.userReportForAllOusFragment2);

            }

        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.PeakHourReportForAllOusFragment")) {
            interrupThreads(SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);


            if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
                if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                    navController.navigate(R.id.peakHourReportFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                    navController.navigate(R.id.branchSummaryFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(9))
                    navController.navigate(R.id.userReportForAllOusFragment2);
                else if (SplashScreenActivity.allData.getLayoutList().contains(10))
                    navController.navigate(R.id.userReportForEachOusFragment);

            }

        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.PeakHourReportFragment")) {
            interrupThreads(SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);

            if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
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
                else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                    navController.navigate(R.id.branchSummaryFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(9))
                    navController.navigate(R.id.userReportForAllOusFragment2);
                else if (SplashScreenActivity.allData.getLayoutList().contains(10))
                    navController.navigate(R.id.userReportForEachOusFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(11))
                    navController.navigate(R.id.peakHourReportForAllOusFragment);
            }

        }


    }

    public void pausedState() {
        firstCenterKeyPause = false;
        playPauseKeyPad.setVisibility(View.VISIBLE);
//        summaryByParentArticlePause = true;
//        summaryByArticlePause = true;
//        summaryByChildArticlePause = true;
//        summaryOfLast6MonsPause = true;
//        summaryOfLast30DaysPause = true;
//        summaryOfBranchPause = true;
    }

    public static void interrupThreads(Thread firstThread, Thread secondThread, Thread
            thirdThread, Thread fourthThread, Thread fifthThread) {
//        try {
//            firstThread.interrupt();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            secondThread.interrupt();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            thirdThread.interrupt();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            fourthThread.interrupt();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            fifthThread.interrupt();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static void pauseAll() {
        BranchSummaryFragment.branchSummaryPaused = true;
        PeakHourReportForAllOusFragment.peakHourForAllPaused = true;
        PeakHourReportFragment.peakHourForEachPaused = true;
        SummarizedByArticleChildCategFragment.summByChildArticlePaused = true;
        SummarizedByArticleFragment.summByarticlePaused = true;
        SummarizedByArticleParentCategFragment.summByParentArticlePaused = true;
        SummaryOfLastMonthFragment.summaryOfLAstXdaysPaused = true;
        SummaryOfLastSixMonthsFragment.summaryOfLAstXmonthPaused = true;
        UserReportForAllOusFragment.userReportForAllPaused = true;
        UserReportForEachOuFragment.userReportForEachPaused = true;
    }

    public static void playAll(){
        BranchSummaryFragment.branchSummaryPaused = false;
        PeakHourReportForAllOusFragment.peakHourForAllPaused = false;
        PeakHourReportFragment.peakHourForEachPaused = false;
        SummarizedByArticleChildCategFragment.summByChildArticlePaused = false;
        SummarizedByArticleFragment.summByarticlePaused = false;
        SummarizedByArticleParentCategFragment.summByParentArticlePaused = false;
        SummaryOfLastMonthFragment.summaryOfLAstXdaysPaused = false;
        SummaryOfLastSixMonthsFragment.summaryOfLAstXmonthPaused = false;
        UserReportForAllOusFragment.userReportForAllPaused = false;
        UserReportForEachOuFragment.userReportForEachPaused = false;
    }

    public static boolean pausedstate() {
        if (BranchSummaryFragment.branchSummaryPaused == true ||
                PeakHourReportForAllOusFragment.peakHourForAllPaused == true ||
                PeakHourReportFragment.peakHourForEachPaused == true ||
                SummarizedByArticleChildCategFragment.summByChildArticlePaused == true ||
                SummarizedByArticleFragment.summByarticlePaused == true ||
                SummarizedByArticleParentCategFragment.summByParentArticlePaused == true ||
                SummaryOfLastMonthFragment.summaryOfLAstXdaysPaused == true ||
                SummaryOfLastSixMonthsFragment.summaryOfLAstXmonthPaused == true ||
                UserReportForAllOusFragment.userReportForAllPaused == true ||
                UserReportForEachOuFragment.userReportForEachPaused == true) {
            return true;
        }
        return false;
    }

}
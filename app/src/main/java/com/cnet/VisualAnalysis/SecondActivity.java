package com.cnet.VisualAnalysis;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
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

//    ImageView leftArrow;
//    ImageView playPause;
//    ImageView rightArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

//        leftArrow = findViewById(R.id.leftArrow);
//        playPause = findViewById(R.id.playPause);
//        rightArrow = findViewById(R.id.rightArrow);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        navController = NavHostFragment.findNavController(getCurrentFragment());
        switch (keyCode) {

            case KeyEvent.KEYCODE_DPAD_CENTER:
                Log.i("key", "center playing" + firstCenterKeyPause);
//                playPause.setImageTintList(ColorStateList.valueOf(
//                        ContextCompat.getColor(getApplicationContext(), R.color.playbacksForeground)));
//                leftArrow.setImageTintList(ColorStateList.valueOf(
//                        ContextCompat.getColor(getApplicationContext(), R.color.playbacksBackground)));
//                rightArrow.setImageTintList(ColorStateList.valueOf(
//                        ContextCompat.getColor(getApplicationContext(), R.color.playbacksBackground)));


//                SummarizedByArticleFragment.isInflatingTable = false;

                if (!firstCenterKeyPause){
                    summaryByParentArticlePause = false;
                    summaryByArticlePause = false;
                    summaryByChildArticlePause = false;
                    summaryOfLast6MonsPause = false;
                    summaryOfLast30DaysPause = false;
                    summaryOfBranchPause = false;
                    getCurrentFragment().onResume();

                    firstCenterKeyPause = true;
                }
                else {
                    pausedState();
                }

                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                Log.i("TAG", "onKeyDown:up ");
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                Log.i("TAG", "onKeyDown:down ");
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                Log.i("key", "onKeyDown: left");
                SummarizedByArticleFragment.isInflatingTable = false;
//                leftArrow.setImageTintList(ColorStateList.valueOf(
//                        ContextCompat.getColor(getApplicationContext(), R.color.playbacksForeground)));
//                rightArrow.setImageTintList(ColorStateList.valueOf(
//                        ContextCompat.getColor(getApplicationContext(), R.color.playbacksBackground)));
//                playPause.setImageTintList(ColorStateList.valueOf(
//                        ContextCompat.getColor(getApplicationContext(), R.color.playbacksForeground)));
                leftNavigation();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Log.i("key", "onKeyDown: right");
                SummarizedByArticleFragment.isInflatingTable = false;
//                rightArrow.setImageTintList(ColorStateList.valueOf(
//                        ContextCompat.getColor(getApplicationContext(), R.color.playbacksForeground)));
//                playPause.setImageTintList(ColorStateList.valueOf(
//                        ContextCompat.getColor(getApplicationContext(), R.color.playbacksForeground)));
//                leftArrow.setImageTintList(ColorStateList.valueOf(
//                        ContextCompat.getColor(getApplicationContext(), R.color.playbacksBackground)));
                rightNavigation();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Fragment getCurrentFragment() {
        Fragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
        Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);

        return fragment;
    }

    public void leftNavigation() {
        Log.i("currentClass-2", getCurrentFragment().getClass().getName());
        if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummarizedByArticleFragment")) {
            interrupThreads(SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    SummarizedByArticleFragment.handleRowAnimationThread);
            navController.navigate(R.id.branchSummaryFragment);
            pausedState();
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummarizedByArticleParentCategFragment")) {
            interrupThreads(SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);
            navController.navigate(R.id.summarizedByArticleFragment2);
            pausedState();
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummarizedByArticleChildCategFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
            pausedState();
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummaryOfLastSixMonthsFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
            pausedState();
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummaryOfLastMonthFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
            pausedState();
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.BranchSummaryFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);
            navController.navigate(R.id.summaryOfLastMonthFragment);
            pausedState();
        }

    }

    public void rightNavigation() {
        if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummarizedByArticleFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
            pausedState();
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummarizedByArticleParentCategFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
            pausedState();
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummarizedByArticleChildCategFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
            pausedState();
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummaryOfLastSixMonthsFragment")) {
            interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                    SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);
            navController.navigate(R.id.summaryOfLastMonthFragment);
            pausedState();
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummaryOfLastMonthFragment")) {
            interrupThreads(SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    SummarizedByArticleFragment.handleRowAnimationThread);
            navController.navigate(R.id.branchSummaryFragment);
            pausedState();
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.BranchSummaryFragment")) {
            interrupThreads(SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                    SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                    SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                    SummaryOfLastMonthFragment.handleRowAnimationThread,
                    BranchSummaryFragment.handleRowAnimationThread);
            navController.navigate(R.id.summarizedByArticleFragment2);
            pausedState();
        }
    }

    public void pausedState() {
        firstCenterKeyPause = false;
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

}
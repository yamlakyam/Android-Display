package com.cnet.VisualAnalysis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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

import com.android.volley.VolleyError;
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
import com.cnet.VisualAnalysis.Fragments.VansOfASingleOrganizationFragment;
import com.cnet.VisualAnalysis.Utils.AllDataParser;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

public class SecondActivity extends AppCompatActivity implements VolleyHttp.GetRequest {

    @Override
    public void onSuccess(JSONObject jsonObject) throws JSONException {

        AllDataParser allDataParser = new AllDataParser(jsonObject);
        SplashScreenActivity.allData = allDataParser.parseAllData();
        Log.i("updated", "onSuccess: ");
    }

    @Override
    public void onFailure(VolleyError error) {

    }

    public interface KeyPress {
        void centerKey();

        void leftKey();

        void rightKey();
    }

    public static DashBoardData dashBoardData;

    NavController navController;

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
                if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
                    setHomeFragment(R.id.vansOfASingleOrganizationFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
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

        Log.i("host activty", "onCreate: ");

        String deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                VolleyHttp http = new VolleyHttp(getApplicationContext());
                http.makeGetRequest(Constants.allDataWithConfigurationURL + "?imei=" + deviceID,
                        SecondActivity.this);
                handler.postDelayed(this, 600000);
            }
        };
        handler.post(runnable);

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
        } else if (SplashScreenActivity.allData.getLayoutList().contains(1))
            frgamentId = R.id.vansOfASingleOrganizationFragment;
//        int frgamentId = R.id.peakHourReportForAllOusFragment;

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
                    if (getCurrentFragment() instanceof KeyPress) {
                        keyPress.centerKey();
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:

                    SummarizedByArticleFragment.isInflatingTable = false;
                    SummarizedByArticleParentCategFragment.isInflatingTable = false;
                    SummarizedByArticleChildCategFragment.isInflatingTable = false;
                    SummaryOfLastSixMonthsFragment.isInflatingTable = false;

                    if (getCurrentFragment() instanceof KeyPress)
                        keyPress.leftKey();

                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:

                    SummarizedByArticleFragment.isInflatingTable = false;
                    SummarizedByArticleParentCategFragment.isInflatingTable = false;
                    SummarizedByArticleChildCategFragment.isInflatingTable = false;
                    SummaryOfLastSixMonthsFragment.isInflatingTable = false;

                    if (getCurrentFragment() instanceof KeyPress)
                        keyPress.rightKey();


                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public Fragment getCurrentFragment() {
        Fragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
        return navHostFragment.getChildFragmentManager().getFragments().get(0);
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
        VansOfASingleOrganizationFragment.vanListPaused = true;

    }

    public static void playAll() {
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
        VansOfASingleOrganizationFragment.vanListPaused = false;
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
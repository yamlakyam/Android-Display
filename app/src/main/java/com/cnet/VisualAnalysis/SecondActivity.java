package com.cnet.VisualAnalysis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.cnet.VisualAnalysis.Utils.AllDataParser;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

public class SecondActivity extends AppCompatActivity implements VolleyHttp.GetRequest {

    public int refreshTime = 300000;
    String updateFail = "Failed updating";

    ProgressBar secondActivtyProgressBar;
    ConstraintLayout refreshingConstraintLayout;
    NavGraph graph;

    public interface KeyPress {
        void centerKey();

        void leftKey();

        void rightKey();
    }

    NavController navController;

    public static boolean firstCenterKeyPause;

    LinearLayout playPauseKeyPad;

    public Context context;

    KeyPress keyPress;

    public static int vanIndex;

    ArrayList<Integer> layouts;

    String lastActivty;
    int lastActivtyFragmentId;
    String deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        secondActivtyProgressBar = findViewById(R.id.secondActivtyProgressBar);
        refreshingConstraintLayout = findViewById(R.id.refreshingConstraintLayout);

        if (SplashScreenActivity.allData != null) {
            if (SplashScreenActivity.allData.getLayoutList() != null)
                layouts = SplashScreenActivity.allData.getLayoutList();
        }

        Log.i("Van-Index-Before", vanIndex + "");
//        vanIndex = 0;

        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
//        Log.i("ThreadSets-onCreate", threadSet.toString());
        Log.i("ThreadSetCount-onCreate", threadSet.size() + "");

        context = SecondActivity.this;
        setContentView(R.layout.activity_second);

        deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        Intent intent = getIntent();
        lastActivty = intent.getStringExtra("prevvvvvvv-activity");
        refreshingConstraintLayout = findViewById(R.id.refreshingConstraintLayout);

        lastActivtyFragmentId = intent.getIntExtra("From Map", 0);

        if (lastActivtyFragmentId == R.id.userReportForEachOusFragment) {
            setHomeFragment(R.id.userReportForEachOusFragment);
        } else if (lastActivtyFragmentId == R.id.peakHourReportFragment) {
            setHomeFragment(R.id.peakHourReportFragment);
        } else if (lastActivty != null) {
            Log.i("FROM_VID", "onCreate: ");
            refreshingConstraintLayout.setVisibility(ConstraintLayout.VISIBLE);
            VolleyHttp http = new VolleyHttp(getApplicationContext());
            http.makeGetRequest(Constants.allDataWithConfigurationURL + "?imei=" + deviceID,
                    SecondActivity.this);
        } else {
            Log.i("FIRST_TIME", "onCreate: ");
            if (mappedFragment() != null) {
                setHomeFragment();
            } else {
                Log.i("ANIMATION_CALLED-3", "onFailure: ");
                startActivity(new Intent(SecondActivity.this, VideoActivity.class));
            }
        }

       /*Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                VolleyHttp http = new VolleyHttp(getApplicationContext());
                try {
                    http.makeGetRequest(Constants.allDataWithConfigurationURL + "?imei=" + deviceID,
                            SecondActivity.this);
                    Log.i("REQUEST", "MADE");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, refreshTime);
            }
        };
        handler.post(runnable);

         */

      /* Timer timer = new Timer();
        TimerTask hourlyTask = new TimerTask() {
            @Override
            public void run() {
                VolleyHttp http = new VolleyHttp(getApplicationContext());
                try {
//                    http.makeGetRequest(Constants.allDataWithConfigurationURL + "?imei=" + deviceID,
//                            SecondActivity.this);
                    Log.i("REQUEST MADE", "run: ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(hourlyTask, 0, refreshTime);

       */

       /*
       Thread refreshDataThread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    try {
                        Thread.sleep(refreshTime);
                        VolleyHttp http = new VolleyHttp(getApplicationContext());
                        http.makeGetRequest(Constants.allDataWithConfigurationURL + "?imei=" + deviceID,
                                SecondActivity.this);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        refreshDataThread.start();
         */

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSuccess(JSONObject jsonObject) throws JSONException {
        Log.i("Success", "onSuccess: ");
        AllDataParser allDataParser = new AllDataParser(jsonObject);
        try {
            SplashScreenActivity.allData = allDataParser.parseAllData();
            Log.i("Refreshed_Data_Parsed", "run: ");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        refreshingConstraintLayout.setVisibility(View.GONE);
        if (mappedFragment() != null) {
            setHomeFragment();
        } else {
            Log.i("ANIMATION_CALLED-2", "onFailure: ");

            startActivity(new Intent(SecondActivity.this, VideoActivity.class));
        }
    }

    @Override
    public void onFailure(VolleyError error) {
        Log.i("update failed", "onFailure: ");
        Toast.makeText(this, updateFail, Toast.LENGTH_LONG);
        refreshingConstraintLayout.setVisibility(View.GONE);

        if (mappedFragment() != null) {

            setHomeFragment();
        } else {
            Log.i("ANIMATION_CALLED-1", "onFailure: ");
            startActivity(new Intent(SecondActivity.this, VideoActivity.class));
        }

    }

    public void fragmentWhileDataRefreshed() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_second);
        NavInflater inflater = navHostFragment.getNavController().getNavInflater();
        graph = inflater.inflate(R.navigation.second_nav);
        graph.setStartDestination(R.id.stallingFragment);
        NavController navController = navHostFragment.getNavController();
        navController.setGraph(graph);
    }

    public void setHomeFragment() {

        Log.i("SET_HOME_FRAGMENT", "setHomeFragment: ");
        int frgamentId;
//        mappedFragment();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_second);
        NavInflater inflater = navHostFragment.getNavController().getNavInflater();
        graph = inflater.inflate(R.navigation.second_nav);

        if (SplashScreenActivity.allData != null) {
            if (SplashScreenActivity.allData.getDashBoardData() != null) {
                if (!SplashScreenActivity.allData.getLayoutList().isEmpty()) {
                    if (layouts.contains(3) || layouts.contains(4) || layouts.contains(5) ||
                            layouts.contains(6) || layouts.contains(7) || layouts.contains(8) ||
                            layouts.contains(9) || layouts.contains(10) || layouts.contains(11) || layouts.contains(12)) {
                        graph.setStartDestination(mappedFragment());
                        NavController navController = navHostFragment.getNavController();
                        navController.setGraph(graph);
                    } else if (layouts.contains(1)) {
                        startActivity(new Intent(this, MapsActivity.class));
                    }

                } else {
                    Log.i("ANIMATION_CALLED-6", "onFailure: ");
                    startActivity(new Intent(SecondActivity.this, SplashScreenActivity.class));
                }
            }
        } else {
            Log.i("ANIMATION_CALLED-5", "onFailure: ");


            startActivity(new Intent(SecondActivity.this, SplashScreenActivity.class));
        }

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

    public Integer mappedFragment() {
        Integer frgamentId = null;
        if (SplashScreenActivity.allData != null) {
            Log.i("TAG", "mappedFragment: ");
            ArrayList<Integer> layouts = SplashScreenActivity.allData.getLayoutList();
            DashBoardData dBData = SplashScreenActivity.allData.getDashBoardData();
            if (layouts.contains(3) &&
                    dBData.getSummarizedByArticleData() != null &&
                    dBData.getSummarizedByArticleData().tableData.size() > 0) {
                Log.i("article", "mappedFragment: ");
                frgamentId = R.id.summarizedByArticleFragment2;
            } else if (layouts.contains(4)
                    && dBData.getSummarizedByParentArticleData() != null
                    && dBData.getSummarizedByParentArticleData().getTableData().size() > 0) {
                Log.i("parent", "mappedFragment: ");
                frgamentId = R.id.summarizedByArticleParentCategFragment;
            } else if (layouts.contains(5)
                    && dBData.getSummarizedByChildArticleData() != null
                    && dBData.getSummarizedByChildArticleData().getTableData().size() > 0) {
                Log.i("child", "mappedFragment: ");
                frgamentId = R.id.summarizedByArticleChildCategFragment;
            } else if (layouts.contains(6)
                    && dBData.getSummaryOfLast6MonthsData() != null
                    && dBData.getSummaryOfLast6MonthsData().getTableData().size() > 0) {
                Log.i("last 6 mons", "mappedFragment: ");
                frgamentId = R.id.summaryOfLastSixMonthsFragment;
            } else if (layouts.contains(7)
                    && dBData.getSummaryOfLast30DaysData() != null
                    && dBData.getSummaryOfLast30DaysData().tableData.size() > 0) {
                Log.i("last 30 days", "mappedFragment: ");
                frgamentId = R.id.summaryOfLastMonthFragment;
            } else if (layouts.contains(8) && dBData.getBranchSummaryData().getBranchSummaryTableRows().size() > 0) {
                Log.i("branch", "mappedFragment: ");
                frgamentId = R.id.branchSummaryFragment;
            } else if (layouts.contains(9)
                    && dBData.getUserReportForAllBranch().size() > 0) {
                Log.i("user-rep", "mappedFragment: ");
                frgamentId = R.id.userReportForAllOusFragment2;
            } else if (layouts.contains(10)
                    && dBData.getUserReportForEachBranch().size() > 0) {
                Log.i("user-rep-each", "mappedFragment: ");
                frgamentId = R.id.userReportForEachOusFragment;
            } else if (layouts.contains(11) &&
                    dBData.getFigureReportDataforAllBranch().size() > 0) {
                Log.i("peaK-HR", "mappedFragment: ");
                frgamentId = R.id.peakHourReportForAllOusFragment;
            } else if (layouts.contains(12)
                    && dBData.getFigureReportDataforEachBranch().size() > 0) {
                Log.i("PeakHr-each", "mappedFragment: ");
                frgamentId = R.id.peakHourReportFragment;
            } else if (layouts.contains(1) &&
                    dBData.getVoucherDataForVans().size() > 0) {
                Log.i("map", "mappedFragment: ");
                frgamentId = R.id.mapsFragment;
            }
        }
        return frgamentId;
    }

    @SuppressLint("HardwareIds")
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getCurrentFragment() instanceof KeyPress)
            keyPress = (KeyPress) getCurrentFragment();

        if (SplashScreenActivity.allData != null) {
            if (SplashScreenActivity.allData.isEnableNavigation()) {
                navController = NavHostFragment.findNavController(getCurrentFragment());
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                        if (getCurrentFragment() instanceof KeyPress) {
                            keyPress.centerKey();
                        }
                        break;
                    case KeyEvent.KEYCODE_DPAD_LEFT:

                        if (getCurrentFragment() instanceof KeyPress)
                            keyPress.leftKey();

                        break;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:

                        if (getCurrentFragment() instanceof KeyPress)
                            keyPress.rightKey();
                        break;
                }
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
//        MapsFragment.mapPaused = true;
        MapsActivity.mapPaused = true;


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
//        MapsFragment.mapPaused = false;
        MapsActivity.mapPaused = false;

//        VansOfASingleOrganizationFragment.vanListPaused = false;
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
                UserReportForEachOuFragment.userReportForEachPaused == true ||
//                MapsFragment.mapPaused == true) {
                MapsActivity.mapPaused == true) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("back button from ", "onBackPressed: ");
        finish();
        System.exit(0);

//        startActivity(new Intent(SecondActivity.this, SplashScreenActivity.class));
    }


    @Override
    protected void onStop() {
        super.onStop();
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();

        Log.i("ThreadSets-onStop", threadSet.toString());
        Log.i("ThreadSetCount-onStop", threadSet.size() + "");
        Log.i("Active Count", Thread.activeCount() + "");

        Runtime runtime = Runtime.getRuntime();
        long usedMemInMB = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        long maxHeapSizeInMB = runtime.maxMemory() / 1048576L;
        long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;
        Log.i("availHeapSizeInMB", availHeapSizeInMB + "");
//        System.gc();
//        Runtime.getRuntime().freeMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context = null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
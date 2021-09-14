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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Fragments.BranchSummaryFragment;
import com.cnet.VisualAnalysis.Fragments.MapsFragment;
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
import java.util.Timer;
import java.util.TimerTask;

public class SecondActivity extends AppCompatActivity implements VolleyHttp.GetRequest {

    public int refreshTime = 300000;
    String updateFail = "Failed updating";

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

    public Context context;

    KeyPress keyPress;

    public static int vanIndex = 0;

    ArrayList<Integer> layouts;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Van-Index-Before", vanIndex + "");
        vanIndex = 0;
        Log.i("Van-Index-After", vanIndex + "");

        context = SecondActivity.this;
        setContentView(R.layout.activity_second);

        if (mappedFragment() != null) {
            setHomeFragment();
        } else {
            startActivity(new Intent(SecondActivity.this, SplashScreenActivity.class));
        }

//        TooLargeTool.startLogging(SecondActivity.this);
        leftArrow = findViewById(R.id.leftArrow);
        playPause = findViewById(R.id.playPause);
        rightArrow = findViewById(R.id.rightArrow);
        playPauseKeyPad = findViewById(R.id.playPauseKeyPad);

        Intent intent = getIntent();
        String name = intent.getStringExtra("left");
        if (name != null) {
            if (name.equals("pressed")) {
                if (SplashScreenActivity.allData != null) {
                    layouts = SplashScreenActivity.allData.getLayoutList();
                    DashBoardData dBData = SplashScreenActivity.allData.getDashBoardData();
                    if (layouts.contains(1)) {
                        setHomeFragment(R.id.mapsFragment);
                    } else if (layouts.contains(12)) {
                        setHomeFragment(R.id.peakHourReportFragment);
                    } else if (layouts.contains(11)) {
                        setHomeFragment(R.id.peakHourReportForAllOusFragment);
                    } else if (layouts.contains(10)) {
                        setHomeFragment(R.id.userReportForEachOusFragment);
                    } else if (layouts.contains(9)) {
                        setHomeFragment(R.id.userReportForAllOusFragment2);
                    } else if (layouts.contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0) {
                        setHomeFragment(R.id.branchSummaryFragment);
                    } else if (layouts.contains(7)) {
                        setHomeFragment(R.id.summaryOfLastMonthFragment);
                    } else if (layouts.contains(6)) {
                        setHomeFragment(R.id.summaryOfLastSixMonthsFragment);
                    } else if (layouts.contains(5)) {
                        setHomeFragment(R.id.summarizedByArticleChildCategFragment);
                    } else if (layouts.contains(4)) {
                        setHomeFragment(R.id.summarizedByArticleParentCategFragment);
                    } else if (layouts.contains(3)) {
                        setHomeFragment(R.id.summarizedByArticleFragment2);
                    }
                }
            }
        }

        int fragId = intent.getIntExtra("fromVideo", 0);
        if (fragId == 3) {
            if (SplashScreenActivity.allData != null) {
                layouts = SplashScreenActivity.allData.getLayoutList();
                if (layouts.contains(4)) {
                    setHomeFragment(R.id.summarizedByArticleParentCategFragment);
                } else if (layouts.contains(5)) {
                    setHomeFragment(R.id.summarizedByArticleChildCategFragment);
                } else if (layouts.contains(6)) {
                    setHomeFragment(R.id.summaryOfLastSixMonthsFragment);
                } else if (layouts.contains(7)) {
                    setHomeFragment(R.id.summaryOfLastMonthFragment);
                } else if (layouts.contains(8)) {
                    setHomeFragment(R.id.branchSummaryFragment);
                } else if (layouts.contains(9)) {
                    setHomeFragment(R.id.userReportForAllOusFragment2);
                } else if (layouts.contains(10)) {
                    setHomeFragment(R.id.userReportForEachOusFragment);
                } else if (layouts.contains(11)) {
                    setHomeFragment(R.id.peakHourReportForAllOusFragment);
                } else if (layouts.contains(12)) {
                    setHomeFragment(R.id.peakHourReportFragment);
                } else if (layouts.contains(1)) {
                    setHomeFragment(R.id.mapsFragment);
                }
            }

        } else if (fragId == 4) {
            if (SplashScreenActivity.allData != null) {
                layouts = SplashScreenActivity.allData.getLayoutList();

                if (layouts.contains(5)) {
                    setHomeFragment(R.id.summarizedByArticleChildCategFragment);
                } else if (layouts.contains(6)) {
                    setHomeFragment(R.id.summaryOfLastSixMonthsFragment);
                } else if (layouts.contains(7)) {
                    setHomeFragment(R.id.summaryOfLastMonthFragment);
                } else if (layouts.contains(8)) {
                    setHomeFragment(R.id.branchSummaryFragment);
                } else if (layouts.contains(9)) {
                    setHomeFragment(R.id.userReportForAllOusFragment2);
                } else if (layouts.contains(10)) {
                    setHomeFragment(R.id.userReportForEachOusFragment);
                } else if (layouts.contains(11)) {
                    setHomeFragment(R.id.peakHourReportForAllOusFragment);
                } else if (layouts.contains(12)) {
                    setHomeFragment(R.id.peakHourReportFragment);
                } else if (layouts.contains(1)) {
                    setHomeFragment(R.id.mapsFragment);
                } else if (layouts.contains(3)) {
                    setHomeFragment(R.id.summarizedByArticleFragment2);
                }
            }

        } else if (fragId == 5) {
            if (SplashScreenActivity.allData != null) {
                layouts = SplashScreenActivity.allData.getLayoutList();

                if (layouts.contains(6)) {
                    setHomeFragment(R.id.summaryOfLastSixMonthsFragment);
                } else if (layouts.contains(7)) {
                    setHomeFragment(R.id.summaryOfLastMonthFragment);
                } else if (layouts.contains(8)) {
                    setHomeFragment(R.id.branchSummaryFragment);
                } else if (layouts.contains(9)) {
                    setHomeFragment(R.id.userReportForAllOusFragment2);
                } else if (layouts.contains(10)) {
                    setHomeFragment(R.id.userReportForEachOusFragment);
                } else if (layouts.contains(11)) {
                    setHomeFragment(R.id.peakHourReportForAllOusFragment);
                } else if (layouts.contains(12)) {
                    setHomeFragment(R.id.peakHourReportFragment);
                } else if (layouts.contains(1)) {
                    setHomeFragment(R.id.mapsFragment);
                } else if (layouts.contains(3)) {
                    setHomeFragment(R.id.summarizedByArticleFragment2);
                } else if (layouts.contains(4)) {
                    setHomeFragment(R.id.summarizedByArticleParentCategFragment);
                }
            }

        } else if (fragId == 6) {
            if (SplashScreenActivity.allData != null) {
                layouts = SplashScreenActivity.allData.getLayoutList();

                if (layouts.contains(7)) {
                    setHomeFragment(R.id.summaryOfLastMonthFragment);
                } else if (layouts.contains(8)) {
                    setHomeFragment(R.id.branchSummaryFragment);
                } else if (layouts.contains(9)) {
                    setHomeFragment(R.id.userReportForAllOusFragment2);
                } else if (layouts.contains(10)) {
                    setHomeFragment(R.id.userReportForEachOusFragment);
                } else if (layouts.contains(11)) {
                    setHomeFragment(R.id.peakHourReportForAllOusFragment);
                } else if (layouts.contains(12)) {
                    setHomeFragment(R.id.peakHourReportFragment);
                } else if (layouts.contains(1)) {
                    setHomeFragment(R.id.mapsFragment);
                } else if (layouts.contains(3)) {
                    setHomeFragment(R.id.summarizedByArticleFragment2);
                } else if (layouts.contains(4)) {
                    setHomeFragment(R.id.summarizedByArticleParentCategFragment);
                } else if (layouts.contains(5)) {
                    setHomeFragment(R.id.summarizedByArticleChildCategFragment);
                }
            }

        } else if (fragId == 7) {
            if (SplashScreenActivity.allData != null) {
                layouts = SplashScreenActivity.allData.getLayoutList();

                if (layouts.contains(8)) {
                    setHomeFragment(R.id.branchSummaryFragment);
                } else if (layouts.contains(9)) {
                    setHomeFragment(R.id.userReportForAllOusFragment2);
                } else if (layouts.contains(10)) {
                    setHomeFragment(R.id.userReportForEachOusFragment);
                } else if (layouts.contains(11)) {
                    setHomeFragment(R.id.peakHourReportForAllOusFragment);
                } else if (layouts.contains(12)) {
                    setHomeFragment(R.id.peakHourReportFragment);
                } else if (layouts.contains(1)) {
                    setHomeFragment(R.id.mapsFragment);
                } else if (layouts.contains(3)) {
                    setHomeFragment(R.id.summarizedByArticleFragment2);
                } else if (layouts.contains(4)) {
                    setHomeFragment(R.id.summarizedByArticleParentCategFragment);
                } else if (layouts.contains(5)) {
                    setHomeFragment(R.id.summarizedByArticleChildCategFragment);
                } else if (layouts.contains(6)) {
                    setHomeFragment(R.id.summaryOfLastSixMonthsFragment);
                }
            }

        } else if (fragId == 8) {
            if (SplashScreenActivity.allData != null) {
                layouts = SplashScreenActivity.allData.getLayoutList();

                if (layouts.contains(9)) {
                    setHomeFragment(R.id.userReportForAllOusFragment2);
                } else if (layouts.contains(10)) {
                    setHomeFragment(R.id.userReportForEachOusFragment);
                } else if (layouts.contains(11)) {
                    setHomeFragment(R.id.peakHourReportForAllOusFragment);
                } else if (layouts.contains(12)) {
                    setHomeFragment(R.id.peakHourReportFragment);
                } else if (layouts.contains(1)) {
                    setHomeFragment(R.id.mapsFragment);
                } else if (layouts.contains(3)) {
                    setHomeFragment(R.id.summarizedByArticleFragment2);
                } else if (layouts.contains(4)) {
                    setHomeFragment(R.id.summarizedByArticleParentCategFragment);
                } else if (layouts.contains(5)) {
                    setHomeFragment(R.id.summarizedByArticleChildCategFragment);
                } else if (layouts.contains(6)) {
                    setHomeFragment(R.id.summaryOfLastSixMonthsFragment);
                } else if (layouts.contains(7)) {
                    setHomeFragment(R.id.summaryOfLastMonthFragment);
                }
            }

        } else if (fragId == 9) {
            if (SplashScreenActivity.allData != null) {
                layouts = SplashScreenActivity.allData.getLayoutList();

                if (layouts.contains(10)) {
                    setHomeFragment(R.id.userReportForEachOusFragment);
                } else if (layouts.contains(11)) {
                    setHomeFragment(R.id.peakHourReportForAllOusFragment);
                } else if (layouts.contains(12)) {
                    setHomeFragment(R.id.peakHourReportFragment);
                } else if (layouts.contains(1)) {
                    setHomeFragment(R.id.mapsFragment);
                } else if (layouts.contains(3)) {
                    setHomeFragment(R.id.summarizedByArticleFragment2);
                } else if (layouts.contains(4)) {
                    setHomeFragment(R.id.summarizedByArticleParentCategFragment);
                } else if (layouts.contains(5)) {
                    setHomeFragment(R.id.summarizedByArticleChildCategFragment);
                } else if (layouts.contains(6)) {
                    setHomeFragment(R.id.summaryOfLastSixMonthsFragment);
                } else if (layouts.contains(7)) {
                    setHomeFragment(R.id.summaryOfLastMonthFragment);
                } else if (layouts.contains(8)) {
                    setHomeFragment(R.id.branchSummaryFragment);
                }
            }

        } else if (fragId == 10) {
            if (SplashScreenActivity.allData != null) {
                layouts = SplashScreenActivity.allData.getLayoutList();

                if (layouts.contains(11)) {
                    setHomeFragment(R.id.peakHourReportForAllOusFragment);
                } else if (layouts.contains(12)) {
                    setHomeFragment(R.id.peakHourReportFragment);
                } else if (layouts.contains(1)) {
                    setHomeFragment(R.id.mapsFragment);
                } else if (layouts.contains(3)) {
                    setHomeFragment(R.id.summarizedByArticleFragment2);
                } else if (layouts.contains(4)) {
                    setHomeFragment(R.id.summarizedByArticleParentCategFragment);
                } else if (layouts.contains(5)) {
                    setHomeFragment(R.id.summarizedByArticleChildCategFragment);
                } else if (layouts.contains(6)) {
                    setHomeFragment(R.id.summaryOfLastSixMonthsFragment);
                } else if (layouts.contains(7)) {
                    setHomeFragment(R.id.summaryOfLastMonthFragment);
                } else if (layouts.contains(8)) {
                    setHomeFragment(R.id.branchSummaryFragment);
                } else if (layouts.contains(9)) {
                    setHomeFragment(R.id.userReportForAllOusFragment2);
                }
            }

        } else if (fragId == 11) {
            if (SplashScreenActivity.allData != null) {
                layouts = SplashScreenActivity.allData.getLayoutList();

                if (layouts.contains(12)) {
                    setHomeFragment(R.id.peakHourReportFragment);
                } else if (layouts.contains(1)) {
                    setHomeFragment(R.id.mapsFragment);
                } else if (layouts.contains(3)) {
                    setHomeFragment(R.id.summarizedByArticleFragment2);
                } else if (layouts.contains(4)) {
                    setHomeFragment(R.id.summarizedByArticleParentCategFragment);
                } else if (layouts.contains(5)) {
                    setHomeFragment(R.id.summarizedByArticleChildCategFragment);
                } else if (layouts.contains(6)) {
                    setHomeFragment(R.id.summaryOfLastSixMonthsFragment);
                } else if (layouts.contains(7)) {
                    setHomeFragment(R.id.summaryOfLastMonthFragment);
                } else if (layouts.contains(8)) {
                    setHomeFragment(R.id.branchSummaryFragment);
                } else if (layouts.contains(9)) {
                    setHomeFragment(R.id.userReportForAllOusFragment2);
                } else if (layouts.contains(10)) {
                    setHomeFragment(R.id.userReportForEachOusFragment);
                }
            }

        } else if (fragId == 12) {
            if (SplashScreenActivity.allData != null) {

                layouts = SplashScreenActivity.allData.getLayoutList();

                if (layouts.contains(1)) {
                    setHomeFragment(R.id.mapsFragment);
                } else if (layouts.contains(3)) {
                    setHomeFragment(R.id.summarizedByArticleFragment2);
                } else if (layouts.contains(4)) {
                    setHomeFragment(R.id.summarizedByArticleParentCategFragment);
                } else if (layouts.contains(5)) {
                    setHomeFragment(R.id.summarizedByArticleChildCategFragment);
                } else if (layouts.contains(6)) {
                    setHomeFragment(R.id.summaryOfLastSixMonthsFragment);
                } else if (layouts.contains(7)) {
                    setHomeFragment(R.id.summaryOfLastMonthFragment);
                } else if (layouts.contains(8)) {
                    setHomeFragment(R.id.branchSummaryFragment);
                } else if (layouts.contains(9)) {
                    setHomeFragment(R.id.userReportForAllOusFragment2);
                } else if (layouts.contains(10)) {
                    setHomeFragment(R.id.userReportForEachOusFragment);
                } else if (layouts.contains(11)) {
                    setHomeFragment(R.id.peakHourReportForAllOusFragment);
                }
            }

        } else if (fragId == 1) {

            if (SplashScreenActivity.allData != null) {
                layouts = SplashScreenActivity.allData.getLayoutList();

                if (layouts.contains(1)) {
                    setHomeFragment(R.id.mapsFragment);
                } else if (layouts.contains(3)) {
                    setHomeFragment(R.id.summarizedByArticleFragment2);
                } else if (layouts.contains(4)) {
                    setHomeFragment(R.id.summarizedByArticleParentCategFragment);
                } else if (layouts.contains(5)) {
                    setHomeFragment(R.id.summarizedByArticleChildCategFragment);
                } else if (layouts.contains(6)) {
                    setHomeFragment(R.id.summaryOfLastSixMonthsFragment);
                } else if (layouts.contains(7)) {
                    setHomeFragment(R.id.summaryOfLastMonthFragment);
                } else if (layouts.contains(8)) {
                    setHomeFragment(R.id.branchSummaryFragment);
                } else if (layouts.contains(9)) {
                    setHomeFragment(R.id.userReportForAllOusFragment2);
                } else if (layouts.contains(10)) {
                    setHomeFragment(R.id.userReportForEachOusFragment);
                } else if (layouts.contains(11)) {
                    setHomeFragment(R.id.peakHourReportForAllOusFragment);
                }
            }
        }
        //////////////////////

        String deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        /*Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                VolleyHttp http = new VolleyHttp(getApplicationContext());
                try {
                    http.makeGetRequest(Constants.allDataWithConfigurationURL + "?imei=" + deviceID,
                            SecondActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, refreshTime);
            }
        };
        handler.post(runnable);

         */


        Timer timer = new Timer();
        TimerTask hourlyTask = new TimerTask() {
            @Override
            public void run() {
                VolleyHttp http = new VolleyHttp(getApplicationContext());
                try {
                    http.makeGetRequest(Constants.allDataWithConfigurationURL + "?imei=" + deviceID,
                            SecondActivity.this);
                    Log.i("REQUEST MADE", "run: ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(hourlyTask, 0, refreshTime);


       /* Thread refreshDataThread = new Thread() {
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

        Thread refreshedDataParsingThread = new Thread() {
            @Override
            public void run() {
                super.run();
                AllDataParser allDataParser = new AllDataParser(jsonObject);
                try {
                    SplashScreenActivity.allData = allDataParser.parseAllData();
                    Log.i("Refreshed_Data_Parsed", "run: ");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        refreshedDataParsingThread.start();

    }

    @Override
    public void onFailure(VolleyError error) {
        Log.i("update failed", "onFailure: ");
        Toast.makeText(this, updateFail, Toast.LENGTH_LONG);
    }

    public void setHomeFragment() {
        int frgamentId;
//        mappedFragment();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_second);
        NavInflater inflater = navHostFragment.getNavController().getNavInflater();
        NavGraph graph = inflater.inflate(R.navigation.second_nav);

        if (SplashScreenActivity.allData != null) {
            if (SplashScreenActivity.allData.getDashBoardData() != null) {
                if (!SplashScreenActivity.allData.getLayoutList().isEmpty()) {
                    graph.setStartDestination(mappedFragment());
                    NavController navController = navHostFragment.getNavController();
                    navController.setGraph(graph);
                } else {
                    startActivity(new Intent(SecondActivity.this, SplashScreenActivity.class));
                }
            }
        } else {
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
                frgamentId = R.id.summarizedByArticleFragment2;
            } else if (layouts.contains(4)
                    && dBData.getSummarizedByParentArticleData() != null
                    && dBData.getSummarizedByParentArticleData().getTableData().size() > 0) {
                frgamentId = R.id.summarizedByArticleParentCategFragment;
            } else if (layouts.contains(5)
                    && dBData.getSummarizedByChildArticleData() != null
                    && dBData.getSummarizedByChildArticleData().getTableData().size() > 0) {
                frgamentId = R.id.summarizedByArticleChildCategFragment;
            } else if (layouts.contains(6)
                    && dBData.getSummaryOfLast6MonthsData() != null
                    && dBData.getSummaryOfLast6MonthsData().getTableData().size() > 0) {
                frgamentId = R.id.summaryOfLastSixMonthsFragment;
            } else if (layouts.contains(7)
                    && dBData.getSummaryOfLast30DaysData() != null
                    && dBData.getSummaryOfLast30DaysData().tableData.size() > 0) {
                frgamentId = R.id.summaryOfLastMonthFragment;
            } else if (layouts.contains(8) && dBData.getBranchSummaryData().getBranchSummaryTableRows().size() > 0) {
                frgamentId = R.id.branchSummaryFragment;
            } else if (layouts.contains(9)
                    && dBData.getUserReportForAllBranch().size() > 0) {
                frgamentId = R.id.userReportForAllOusFragment2;
            } else if (layouts.contains(10)
                    && dBData.getUserReportForEachBranch().size() > 0) {
                frgamentId = R.id.userReportForEachOusFragment;
            } else if (layouts.contains(11) &&
                    dBData.getFigureReportDataforAllBranch().size() > 0) {
                frgamentId = R.id.peakHourReportForAllOusFragment;
            } else if (layouts.contains(12)
                    && dBData.getFigureReportDataforEachBranch().size() > 0) {
                frgamentId = R.id.peakHourReportFragment;
            } else if (layouts.contains(1) &&
                    dBData.getVoucherDataForVans().size() > 0)
                frgamentId = R.id.mapsFragment;
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
        MapsFragment.mapPaused = true;
//        VansOfASingleOrganizationFragment.vanListPaused = true;

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
        MapsFragment.mapPaused = false;

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
                MapsFragment.mapPaused == true) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SecondActivity.this, SplashScreenActivity.class));
    }
}
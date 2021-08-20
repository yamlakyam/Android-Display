package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.UserReportDataForSingleOu;
import com.cnet.VisualAnalysis.Data.UserReportTableRow;
import com.cnet.VisualAnalysis.MapsActivity;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleDataChangeThread;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.github.mikephil.charting.charts.PieChart;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class UserReportForEachOuFragment extends Fragment implements SecondActivity.KeyPress {

    TableLayout userReportTableLayout;
    PieChart pieChart;
    ScrollView userReportScrollView;
    TextView userReportTitle;
    TextView scrollingUserReportForEachText;
    DigitalClock digitalClock;
    public Handler animationHandler;
    public Handler changeDataHandler;
    public HandleRowAnimationThread handleRowAnimationThread;
    public HandleDataChangeThread handleDataChangeThread;
    private ArrayList<UserReportTableRow> tablesToDisplay;
    Fragment fragment;
    int branchIndex;

    LinearLayout eachUserReportKeyPad;
    ImageView userRepEachleftArrow;
    ImageView userRepEachplayPause;
    ImageView userRepEachrightArrow;
    NavController navController;


    double grandTotalSum;
    public static boolean userReportForEachPaused;

    ArrayList<UserReportDataForSingleOu> allVansUserReport;
    ArrayList<UserReportDataForSingleOu> tablesToDisplay2;
    ArrayList<UserReportDataForSingleOu> userReportDataForSingleOus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SecondActivity.pausedstate()) {
            userReportForEachPaused = false;
        } else {
            userReportForEachPaused = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_report_for_each_ou, container, false);

        userReportTableLayout = view.findViewById(R.id.userReportTableLayout);
        pieChart = view.findViewById(R.id.pchartUserReport);
        userReportScrollView = view.findViewById(R.id.userReportScrollView);
        userReportTitle = view.findViewById(R.id.userReportTitle);
        scrollingUserReportForEachText = view.findViewById(R.id.scrollingUserReportForEachText);
        scrollingUserReportForEachText.setSelected(true);
        digitalClock = view.findViewById(R.id.digitalClock);
        digitalClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));

        eachUserReportKeyPad = view.findViewById(R.id.eachUserReportKeyPad);
        userRepEachleftArrow = view.findViewById(R.id.userRepEachleftArrow);
        userRepEachplayPause = view.findViewById(R.id.userRepEachplayPause);
        userRepEachrightArrow = view.findViewById(R.id.userRepEachrightArrow);
        keyPadControl(userReportForEachPaused);

        fragment = this;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (SplashScreenActivity.allData != null) {
//            distributorTableProgressBar.setVisibility(View.GONE);
            userReportDataForSingleOus = userReportData(SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch());
            inflateAllTables(0);
        }
    }

    @SuppressLint("HandlerLeak")
    public void inflateAllTables(int startingIndex) {

        changeDataHandler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                    branchIndex = index;
                }

//                if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(index).userReportTableRowArrayList.size() > 0) {
//                if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(index) != null) {
                if (userReportDataForSingleOus.get(index) != null) {
                    inflateTable(index);
//                    UtilityFunctionsForActivity2.drawPieChart(SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(index).pieChartData, pieChart, "User Report");
                    UtilityFunctionsForActivity2.drawPieChart(userReportDataForSingleOus.get(index).pieChartData, pieChart, "User Report");
//                    userReportTitle.setText("User Report For " + SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(index).org);
                    userReportTitle.setText("User Report For " + userReportDataForSingleOus.get(index).org);
                    userReportTitle.append(" from " + new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()));
                }

            }
        };

        float secondsToWait = Float.parseFloat(SplashScreenActivity.allData.getTransitionTimeInMinutes());

//        handleDataChangeThread = new HandleDataChangeThread(changeDataHandler, SplashScreenActivity.allData.getFmcgData().getDistributorTableRows().size(), (int) secondsToWait, startingIndex);
//        handleDataChangeThread = new HandleDataChangeThread(changeDataHandler, SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size(), (int) secondsToWait, startingIndex);
        handleDataChangeThread = new HandleDataChangeThread(changeDataHandler, userReportDataForSingleOus.size(), (int) secondsToWait, startingIndex);
        handleDataChangeThread.start();

    }

    @SuppressLint("HandlerLeak")
    public void inflateTable(int dataIndex) {
        grandTotalSum = 0;
        if (userReportTableLayout != null) {
            userReportTableLayout.removeAllViews();
        }
//        tablesToDisplay = SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(dataIndex).userReportTableRowArrayList;
        tablesToDisplay = userReportDataForSingleOus.get(dataIndex).userReportTableRowArrayList;
        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }

                if (index == tablesToDisplay.size()) {
                    drawLastRow();
                    UtilityFunctionsForActivity1.scrollRows(userReportScrollView);
//                } else if (index == tablesToDisplay.size() + 1 && dataIndex == SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size() - 1) {
//                } else if (index == tablesToDisplay.size() + 1 && dataIndex == getallUserReportSize(SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch()) - 1) {
                } else if (index == tablesToDisplay.size() + 1 && dataIndex == getallUserReportSize(userReportDataForSingleOus) - 1) {
                    if (userReportForEachPaused) {
                        if (handleRowAnimationThread != null) {
                            handleRowAnimationThread.interrupt();
                        }
                    } else {
                        navigate(fragment);
                    }

                } else if (index < tablesToDisplay.size()) {
                    grandTotalSum = grandTotalSum + tablesToDisplay.get(index).grandTotal;
                    UtilityFunctionsForActivity1.drawUserReportForEachOu(tablesToDisplay, getContext(), userReportTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(userReportScrollView);

                }
            }
        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, 200, this, 0);
        handleRowAnimationThread.start();
    }

    public void drawLastRow() {
        View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.table_row_summary_by_parent_article, null, false);
        TextView userReportSN = tableElements.findViewById(R.id.tableRowParentArtProperty1);
        TextView userReportSummaryType = tableElements.findViewById(R.id.tableRowParentArtProperty2);
        TextView userReportGrandTotal = tableElements.findViewById(R.id.tableRowParentArtProperty3);
        TextView userReportPercentage = tableElements.findViewById(R.id.tableRowParentArtProperty4);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        userReportSN.setText("");
        userReportSummaryType.setText("Total Amount");
        userReportSummaryType.setTypeface(Typeface.DEFAULT_BOLD);
        userReportSummaryType.setTextSize(16f);

        userReportGrandTotal.setText(numberFormat.format(grandTotalSum));
        userReportGrandTotal.setTypeface(Typeface.DEFAULT_BOLD);
        userReportGrandTotal.setTextSize(16f);

        userReportPercentage.setText("");
        userReportPercentage.setTypeface(Typeface.DEFAULT_BOLD);
        userReportPercentage.setTextSize(16f);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        userReportSummaryType.startAnimation(animation);
        userReportGrandTotal.startAnimation(animation);
        userReportPercentage.startAnimation(animation);

        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));

        if (userReportTableLayout != null) {
            userReportTableLayout.addView(tableElements);
            UtilityFunctionsForActivity2.animateBottomToTop(userReportTableLayout, tableElements);

        }

    }

    public void navigate(Fragment fragment) {

//        Intent intent = new Intent(getActivity(), VideoActivity.class);
//        intent.putExtra("from", 10);
//        startActivity(intent);

        try {
            navController = NavHostFragment.findNavController(fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
            if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
                navController.navigate(R.id.peakHourReportForAllOusFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                navController.navigate(R.id.peakHourReportFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
//                startActivity(new Intent(requireActivity(), MapsActivity.class));
                navController.navigate(R.id.vansOfASingleOrganizationFragment);

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
    }

    public void leftNavigate(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);

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
            startActivity(new Intent(requireActivity(), MapsActivity.class));
//            navController.navigate(R.id.vansOfASingleOrganizationFragment);

        else if (SplashScreenActivity.allData.getLayoutList().contains(12))
            navController.navigate(R.id.peakHourReportFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(11))
            navController.navigate(R.id.peakHourReportForAllOusFragment);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        if (handleDataChangeThread != null) {
            handleDataChangeThread.interrupt();
        }

    }

    @Override
    public void centerKey() {


        userReportForEachPaused = !userReportForEachPaused;
//        SecondActivity.firstCenterKeyPause = userReportForEachPaused;
        if (userReportForEachPaused) {
            if (handleDataChangeThread != null) {
                handleDataChangeThread.interrupt();
            }
        }
        if (!userReportForEachPaused) {
//            if (branchIndex == getallUserReportSize(SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch()) - 1) {
            if (branchIndex == getallUserReportSize(userReportDataForSingleOus) - 1) {
                navigate(fragment);
                SecondActivity.playAll();

            } else {
                inflateAllTables(branchIndex + 1);
            }
        } else {
            SecondActivity.pauseAll();
        }
        keyPadControl(userReportForEachPaused);

    }

    @Override
    public void leftKey() {
        if (handleDataChangeThread != null) {
            handleDataChangeThread.interrupt();
        }
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        if (branchIndex == 0) {
            leftNavigate(fragment);
        } else {
            inflateAllTables(branchIndex - 1);
        }
    }

    @Override
    public void rightKey() {
        if (handleDataChangeThread != null) {
            handleDataChangeThread.interrupt();
        }
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }

//        if (branchIndex == SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size() - 1) {
//        if (branchIndex == getallUserReportSize(SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch()) - 1) {
        if (branchIndex == getallUserReportSize(userReportDataForSingleOus) - 1) {
            navigate(fragment);
        } else {
            inflateAllTables(branchIndex + 1);
        }
    }

    public void keyPadControl(boolean paused) {
        if (paused) {
            userRepEachplayPause.setImageResource(R.drawable.ic_play_button__2_);
            userRepEachplayPause.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireActivity(), R.color.playbacksForeground)));
            eachUserReportKeyPad.setVisibility(View.VISIBLE);

        } else {
            eachUserReportKeyPad.setVisibility(View.GONE);
        }
    }

    public int getallUserReportSize(ArrayList<UserReportDataForSingleOu> userReportDatas) {
        int count = 0;
        for (UserReportDataForSingleOu u : userReportDatas) {
            if (u.userReportTableRowArrayList.size() > 0) {
                count = count + 1;
            }
        }
        return count;
    }

    public ArrayList<UserReportDataForSingleOu> userReportData(ArrayList<UserReportDataForSingleOu> userReportData) {
        tablesToDisplay2 = new ArrayList<>();
        for (UserReportDataForSingleOu userRepData : userReportData) {
            if (userRepData.userReportTableRowArrayList.size() > 0) {
                tablesToDisplay2.add(userRepData);
            }
        }
        return tablesToDisplay2;
    }
}
package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.UserReportTableRow;
import com.cnet.VisualAnalysis.MapsActivity;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleDataChangeThread;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.github.mikephil.charting.charts.PieChart;

import java.text.NumberFormat;
import java.util.ArrayList;

public class UserReportForEachOuFragment extends Fragment {

    TableLayout userReportTableLayout;
    PieChart pieChart;
    ScrollView userReportScrollView;
    TextView userReportTitle;
    public static Handler animationHandler;
    public static Handler changeDataHandler;
    public HandleRowAnimationThread handleRowAnimationThread;
    public HandleDataChangeThread handleDataChangeThread;
    private ArrayList<UserReportTableRow> tablesToDisplay;
    Fragment fragment;

    double grandTotalSum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_report_for_each_ou, container, false);
        userReportTableLayout = view.findViewById(R.id.userReportTableLayout);
        pieChart = view.findViewById(R.id.pchartUserReport);
        userReportScrollView = view.findViewById(R.id.userReportScrollView);
        userReportTitle = view.findViewById(R.id.userReportTitle);

        fragment = this;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch() != null) {
//            distributorTableProgressBar.setVisibility(View.GONE);
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

                }
                inflateTable(index);
                UtilityFunctionsForActivity2.drawPieChart(SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(index).pieChartData,
                        pieChart, "User Report");
                userReportTitle.setText("User Report For " +
                        SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(index).org);

            }
        };

        float secondsToWait = Float.parseFloat(SplashScreenActivity.allData.getTransitionTimeInMinutes());

//        handleDataChangeThread = new HandleDataChangeThread(changeDataHandler, SplashScreenActivity.allData.getFmcgData().getDistributorTableRows().size(), (int) secondsToWait, startingIndex);
        handleDataChangeThread = new HandleDataChangeThread(changeDataHandler, SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size(), (int) secondsToWait, startingIndex);
        handleDataChangeThread.start();

    }


    @SuppressLint("HandlerLeak")
    public void inflateTable(int dataIndex) {
        grandTotalSum = 0;
        if (userReportTableLayout != null) {
            userReportTableLayout.removeAllViews();
        }
        tablesToDisplay = SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(dataIndex).userReportTableRowArrayList;

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
                } else if (index == tablesToDisplay.size() + 1 && dataIndex == SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size() - 1) {
                    if (fragment != null) {
//                        NavController navController = NavHostFragment.findNavController(fragment);
//                        navController.navigate(R.id.vsmCardFragment);
                    }

                } else if (index < tablesToDisplay.size()) {
                    grandTotalSum = grandTotalSum + tablesToDisplay.get(index).grandTotal;
                    UtilityFunctionsForActivity1.drawUserReportForEachOu(tablesToDisplay, getContext(), userReportTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(userReportScrollView);
                }
            }
        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), UserReportForEachOuFragment.animationHandler, 200, this);
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
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
            if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
                navController.navigate(R.id.peakHourReportForAllOusFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                navController.navigate(R.id.peakHourReportFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
                startActivity(new Intent(requireActivity(), MapsActivity.class));
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
            else if(SplashScreenActivity.allData.getLayoutList().contains(9))
                navController.navigate(R.id.userReportForAllOusFragment2);

        }
    }


}
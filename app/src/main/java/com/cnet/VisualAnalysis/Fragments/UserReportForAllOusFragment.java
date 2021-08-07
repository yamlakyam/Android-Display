package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
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

import com.cnet.VisualAnalysis.Data.PieChartData;
import com.cnet.VisualAnalysis.Data.UserReportTableRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleDataChangeThread;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.github.mikephil.charting.charts.PieChart;

import java.text.NumberFormat;
import java.util.ArrayList;

public class UserReportForAllOusFragment extends Fragment {

    TableLayout userReportForAllTableLayout;
    PieChart pieChart;
    ScrollView userReportForAllScrollView;
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
        View view = inflater.inflate(R.layout.fragment_user_report_for_all_ous, container, false);

        userReportForAllTableLayout = view.findViewById(R.id.userReportForAllTableLayout);
        pieChart = view.findViewById(R.id.pchartUserReportForAll);
        userReportForAllScrollView = view.findViewById(R.id.userReportForAllScrollView);
        fragment = this;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch() != null) {
            inflateTable();
            drawPieChartForAllUsers();
        }
    }

    @SuppressLint("HandlerLeak")
    public void inflateTable() {
        grandTotalSum = 0;
        if (userReportForAllTableLayout != null) {
            userReportForAllTableLayout.removeAllViews();
        }
        tablesToDisplay = SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch();
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
                    UtilityFunctionsForActivity1.scrollRows(userReportForAllScrollView);
                } else if (index == tablesToDisplay.size() + 1) {
                    if (fragment != null) {
//                        NavController navController = NavHostFragment.findNavController(fragment);
//                        navController.navigate(R.id.vsmCardFragment);
                    }

                } else if (index < tablesToDisplay.size()) {
                    grandTotalSum = grandTotalSum + tablesToDisplay.get(index).grandTotal;
                    UtilityFunctionsForActivity1.drawUserReportForAllOu(tablesToDisplay, getContext(), userReportForAllTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(userReportForAllScrollView);
                }
            }
        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, 200, this);
        handleRowAnimationThread.start();
    }

    private void drawLastRow() {

        View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.user_report_for_all_table_row, null, false);
        TextView userReportForAllSN = tableElements.findViewById(R.id.tableRowAllUserReportProperty1);
        TextView userReportForAllUserName = tableElements.findViewById(R.id.tableRowAllUserReportProperty2);
        TextView userReportForAllGrandTotal = tableElements.findViewById(R.id.tableRowAllUserReportProperty3);
        TextView userReportForAllBranchName = tableElements.findViewById(R.id.tableRowAllUserReportProperty4);
        TextView userReportForAllPercentage = tableElements.findViewById(R.id.tableRowAllUserReportProperty5);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        userReportForAllSN.setText("");
        userReportForAllUserName.setText("Total Amount");
        userReportForAllUserName.setTypeface(Typeface.DEFAULT_BOLD);
        userReportForAllUserName.setTextSize(16f);

        userReportForAllGrandTotal.setText(numberFormat.format(grandTotalSum));
        userReportForAllGrandTotal.setTypeface(Typeface.DEFAULT_BOLD);
        userReportForAllGrandTotal.setTextSize(16f);

        userReportForAllBranchName.setText("");
        userReportForAllBranchName.setTypeface(Typeface.DEFAULT_BOLD);
        userReportForAllBranchName.setTextSize(16f);

        userReportForAllPercentage.setText("");
        userReportForAllPercentage.setTypeface(Typeface.DEFAULT_BOLD);
        userReportForAllPercentage.setTextSize(16f);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        userReportForAllUserName.startAnimation(animation);
        userReportForAllGrandTotal.startAnimation(animation);
        userReportForAllPercentage.startAnimation(animation);

        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));

        if (userReportForAllTableLayout != null) {
            userReportForAllTableLayout.addView(tableElements);
            UtilityFunctionsForActivity2.animateBottomToTop(userReportForAllTableLayout, tableElements);

        }

    }

    private void drawPieChartForAllUsers() {

        float[] x = new float[SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().size()];
        String[] label = new String[SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().size()];
        for (int i = 0; i < SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().size(); i++) {
            x[i] = (float) SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().get(i).grandTotal;
            label[i] = SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().get(i).summaryType;
        }
        PieChartData pieChartData = new PieChartData(x, label);
        UtilityFunctionsForActivity2.drawPieChart(pieChartData, pieChart, "User Report For All Organizations");

    }

}
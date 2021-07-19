package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Data.SummaryOfLast30DaysRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import java.text.NumberFormat;
import java.util.ArrayList;

public class SummaryOfLastMonthFragment extends Fragment {
    TableLayout summaryOfLast30DaysTableLayout;
    Handler animationHandler;
    LineChart lineChart;
    BarChart barChart;
    ScrollView scrollView;
    Fragment fragment;

    public static HandleRowAnimationThread handleRowAnimationThread;

    public double totalAmount = 0;

    public SummaryOfLastMonthFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SecondActivity.interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                BranchSummaryFragment.handleRowAnimationThread);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary_of_last_month, container, false);
        fragment = this;
        summaryOfLast30DaysTableLayout = view.findViewById(R.id.summaryOfLast30DaysTableLayout);
        lineChart = view.findViewById(R.id.last30daysLineChart);
        scrollView = view.findViewById(R.id.summaryOfLastMonthScrollView);
        barChart = view.findViewById(R.id.last30daysBarChart);


        backTraverse(fragment, R.id.summaryOfLastSixMonthsFragment);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SecondActivity.dashBoardArray != null) {
            initFragment(200);
        }
    }

    public void initFragment(int seconds) {

        Log.i("success", fragment + "");

        DashBoardData dashBoardData = SecondActivity.dashBoardData;
        inflateTable(dashBoardData.getSummaryOfLast30DaysData().tableData, seconds);
        UtilityFunctionsForActivity2.drawLineChart(dashBoardData.getSummaryOfLast30DaysData().lineChartData, lineChart);
        UtilityFunctionsForActivity2.drawBarChart(dashBoardData.getSummaryOfLast30DaysData().barChartData, barChart, "Summarized by last 30 days");
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummaryOfLast30DaysRow> tablesToDisplay, int seconds) {
        summaryOfLast30DaysTableLayout.removeAllViews();
        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);

                }

                if (index == tablesToDisplay.size()) {
                    drawLastTotalRow();
                    UtilityFunctionsForActivity1.scrollRows(scrollView);
                } else if (index == tablesToDisplay.size() + 1 && !SecondActivity.summaryOfLast30DaysPause) {
                    NavController navController = NavHostFragment.findNavController(fragment);
                    navController.navigate(R.id.branchSummaryFragment);
                } else if(index<tablesToDisplay.size()) {
                    totalLastRow(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity2.drawSummaryOfLast30Days(tablesToDisplay, getContext(), summaryOfLast30DaysTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(scrollView);
                }

            }

        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler,seconds);
        handleRowAnimationThread.start();
    }

    public void totalLastRow(SummaryOfLast30DaysRow row) {

        totalAmount = totalAmount + row.getAmount();

    }

    public void drawLastTotalRow() {
        View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.table_row_summary_by_parent_article, null, false);

        TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowParentArtProperty1);
        TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowParentArtProperty2);
        TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowParentArtProperty3);
        TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowParentArtProperty4);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);


        tableRowProperty1.setText("");
        tableRowProperty2.setText("Total Amount");
        tableRowProperty2.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty2.setTextSize(25f);


        tableRowProperty3.setText(numberFormat.format(totalAmount));
        tableRowProperty3.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty3.setTextSize(25f);


        tableRowProperty4.setText("");

        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        tableRowProperty3.startAnimation(animation);

        summaryOfLast30DaysTableLayout.addView(tableElements);
        UtilityFunctionsForActivity1.animate(summaryOfLast30DaysTableLayout, tableElements);
    }


    public void backTraverse(Fragment fragment, int id) {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavController navController = NavHostFragment.findNavController(fragment);
                navController.navigate(id);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if(handleRowAnimationThread!=null)
            handleRowAnimationThread.interrupt();
    }
}
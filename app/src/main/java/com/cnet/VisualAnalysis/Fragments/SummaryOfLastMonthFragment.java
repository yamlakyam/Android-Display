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
import android.widget.DigitalClock;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Data.SummaryOfLast30DaysRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.StartingActivty;
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
    FrameLayout summaryOfLastMonthFrameLayout;
    TextView scrollingLastMonthText;
    DigitalClock digitalClock;

    public static HandleRowAnimationThread handleRowAnimationThread;

    public static boolean isInflatingTable;
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
        summaryOfLastMonthFrameLayout = view.findViewById(R.id.summaryOfLastMonthFrameLayout);
        scrollingLastMonthText=view.findViewById(R.id.scrollingLastMonthText);
        scrollingLastMonthText.setSelected(true);
        digitalClock = view.findViewById(R.id.digitalClock);
        digitalClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));


        backTraverse(fragment, R.id.summaryOfLastSixMonthsFragment);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SplashScreenActivity.allData.getDashBoardData() != null && !isInflatingTable) {
            summaryOfLastMonthFrameLayout.setVisibility(View.GONE);
            initFragment(SplashScreenActivity.allData.getDashBoardData(), 200);
        }
    }

    public void initFragment(DashBoardData dashBoardDataParam, int seconds) {
        isInflatingTable = true;

        DashBoardData dashBoardData = dashBoardDataParam;
        inflateTable(dashBoardData.getSummaryOfLast30DaysData().tableData, seconds);
        UtilityFunctionsForActivity2.drawLineChart(dashBoardData.getSummaryOfLast30DaysData().lineChartData, lineChart, "Summarized by last 30 days");
        UtilityFunctionsForActivity2.drawBarChart(dashBoardData.getSummaryOfLast30DaysData().barChartData, barChart, "Summarized by last 30 days");
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummaryOfLast30DaysRow> tablesToDisplay, int seconds) {
        totalAmount = 0;

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
//                    NavController navController = NavHostFragment.findNavController(fragment);
//                    navController.navigate(R.id.branchSummaryFragment);
//                    SecondActivity secondActivity = new SecondActivity();
//                    secondActivity.navigations(fragment);
                    navigate(fragment);

                } else if (index < tablesToDisplay.size()) {
                    totalLastRow(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity2.drawSummaryOfLast30Days(tablesToDisplay, getContext(), summaryOfLast30DaysTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(scrollView);
                }

            }

        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, seconds,this);
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
        tableRowProperty2.setTextSize(16f);


        tableRowProperty3.setText(numberFormat.format(totalAmount));
        tableRowProperty3.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty3.setTextSize(16f);


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
                Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    public void navigate(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(7)) {

            if (SplashScreenActivity.allData.getLayoutList().size() > SplashScreenActivity.allData.getLayoutList().indexOf(7) + 1) {
                int next = SplashScreenActivity.allData.getLayoutList().indexOf(7) + 1;
                if (SplashScreenActivity.allData.getLayoutList().get(next) == 8)
                    navController.navigate(R.id.branchSummaryFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().size() > 1) {
                if (SplashScreenActivity.allData.getLayoutList().get(0) == 3)
                    navController.navigate(R.id.summarizedByArticleFragment2);
                else if (SplashScreenActivity.allData.getLayoutList().get(0) == 4)
                    navController.navigate(R.id.summarizedByArticleParentCategFragment);
                else if (SplashScreenActivity.allData.getLayoutList().get(0) == 5)
                    navController.navigate(R.id.summarizedByArticleChildCategFragment);
                else if (SplashScreenActivity.allData.getLayoutList().get(0) == 6)
                    navController.navigate(R.id.summaryOfLastSixMonthsFragment);
                else if (SplashScreenActivity.allData.getLayoutList().get(0) == 8)
                    navController.navigate(R.id.branchSummaryFragment);
            }

        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (handleRowAnimationThread != null)
            handleRowAnimationThread.interrupt();
    }

}
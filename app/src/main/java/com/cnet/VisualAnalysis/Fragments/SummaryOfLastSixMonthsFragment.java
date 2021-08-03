package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.style.TtsSpan;
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
import com.cnet.VisualAnalysis.Data.SummaryOfLast6MonthsRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.StartingActivty;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import java.text.NumberFormat;
import java.util.ArrayList;


public class SummaryOfLastSixMonthsFragment extends Fragment {


    TableLayout summaryOfLast6MonthsTableLayout;
    Handler animationHandler;
    BarChart barChart;
    PieChart pieChart;
    ScrollView scrollView;
    Fragment fragment;
    FrameLayout summaryOfLastSixMonthFrameLayout;
    TextView scrollingLast6MonthText;
    DigitalClock digitalClock;

    public static HandleRowAnimationThread handleRowAnimationThread;
    public static boolean isInflatingTable;
    double totalAmount = 0;

    public SummaryOfLastSixMonthsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SecondActivity.interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                SummaryOfLastMonthFragment.handleRowAnimationThread,
                BranchSummaryFragment.handleRowAnimationThread);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary_of_last_six_months, container, false);
        fragment = this;
        summaryOfLast6MonthsTableLayout = view.findViewById(R.id.summaryOfLast6MonthsTableLayout);
        pieChart = view.findViewById(R.id.pchartsummaryOfLast6Months);
        barChart = view.findViewById(R.id.bChartSummaryOfLast6Months);
        scrollView = view.findViewById(R.id.summaryOfLast6MonsScrollView);
        summaryOfLastSixMonthFrameLayout = view.findViewById(R.id.summaryOfLastSixMonthFrameLayout);
        scrollingLast6MonthText= view.findViewById(R.id.scrollingLast6MonthText);
        scrollingLast6MonthText.setSelected(true);
        digitalClock = view.findViewById(R.id.digitalClock);
        digitalClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));

        backTraverse(fragment, R.id.summarizedByArticleChildCategFragment);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SplashScreenActivity.allData.getDashBoardData() != null && !isInflatingTable) {
            summaryOfLastSixMonthFrameLayout.setVisibility(View.GONE);
            initFragment(SplashScreenActivity.allData.getDashBoardData(), 200);
        }
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummaryOfLast6MonthsRow> tablesToDisplay, int seconds) {
        totalAmount = 0;

        summaryOfLast6MonthsTableLayout.removeAllViews();
        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }

                if (index == tablesToDisplay.size()) {
                    drawLast6MonsTotalRow();
                    UtilityFunctionsForActivity1.scrollRows(scrollView);
                } else if (index == tablesToDisplay.size() + 1 && !SecondActivity.summaryOfLast6MonsPause) {
//                    NavController navController = NavHostFragment.findNavController(fragment);
//                    navController.navigate(R.id.summaryOfLastMonthFragment);
//                    SecondActivity secondActivity = new SecondActivity();
//                    secondActivity.navigations(fragment);
                    navigate(fragment);

                } else if (index < tablesToDisplay.size()) {
                    totalLastRow(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity2.drawSummaryOfLAst6Months(tablesToDisplay, getContext(), summaryOfLast6MonthsTableLayout, index, totalAmount);
                    UtilityFunctionsForActivity1.scrollRows(scrollView);
                }

            }

        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, seconds,this);
        handleRowAnimationThread.start();
    }


    public void initFragment(DashBoardData dashBoardDataParam, int seconds) {
        isInflatingTable = true;

        DashBoardData dashBoardData = dashBoardDataParam;

        inflateTable(dashBoardData.getSummaryOfLast6MonthsData().getTableData(), seconds);
        UtilityFunctionsForActivity2.drawBarChart(dashBoardData.getSummaryOfLast6MonthsData().getBarChartData(), barChart, "Summarized by last 6 months");
        UtilityFunctionsForActivity2.drawPieChart(dashBoardData.getSummaryOfLast6MonthsData().getPieChartData(), pieChart, "Summarized by last 6 months");

    }


    public void totalLastRow(SummaryOfLast6MonthsRow row) {

        totalAmount = totalAmount + row.getAmount();

    }


    public void drawLast6MonsTotalRow() {
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


        tableRowProperty3.setText(numberFormat.format(Math.round(totalAmount * 100.0) / 100.0));
        tableRowProperty3.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty3.setTextSize(16f);


        tableRowProperty4.setText("");
        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        tableRowProperty3.startAnimation(animation);
        summaryOfLast6MonthsTableLayout.addView(tableElements);
        UtilityFunctionsForActivity1.animate(summaryOfLast6MonthsTableLayout, tableElements);
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
        if (SplashScreenActivity.allData.getLayoutList().contains(6)) {

            if (SplashScreenActivity.allData.getLayoutList().size() > SplashScreenActivity.allData.getLayoutList().indexOf(6) + 1) {
                int next = SplashScreenActivity.allData.getLayoutList().indexOf(6) + 1;
                if (SplashScreenActivity.allData.getLayoutList().get(next) == 7)
                    navController.navigate(R.id.summaryOfLastMonthFragment);
                else if (SplashScreenActivity.allData.getLayoutList().get(next) == 8)
                    navController.navigate(R.id.branchSummaryFragment);
            }
            else if (SplashScreenActivity.allData.getLayoutList().size() > 1) {
                if (SplashScreenActivity.allData.getLayoutList().get(0) == 3)
                    navController.navigate(R.id.summarizedByArticleFragment2);
                else if (SplashScreenActivity.allData.getLayoutList().get(0) == 4)
                    navController.navigate(R.id.summarizedByArticleParentCategFragment);
                else if (SplashScreenActivity.allData.getLayoutList().get(0) == 5)
                    navController.navigate(R.id.summarizedByArticleChildCategFragment);
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
package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Data.SummaryOfLast30DaysRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SummaryOfLastMonthFragment extends Fragment implements SecondActivity.KeyPress {
    TableLayout summaryOfLast30DaysTableLayout;
    Handler animationHandler;
    LineChart lineChart;
    BarChart barChart;
    ScrollView scrollView;
    Fragment fragment;
    FrameLayout summaryOfLastMonthFrameLayout;
    TextView scrollingLastMonthText;
    DigitalClock digitalClock;
    TextView summaryOfLast30DaysTitle;
    public static boolean summaryOfLAstXdaysPaused;
    int rowIndex;

    LinearLayout sumOfLastXDaysKeyPad;
    ImageView sumLastXDayleftArrow;
    ImageView sumLastXDayplayPause;
    ImageView sumLastXDayrightArrow;


    public HandleRowAnimationThread handleRowAnimationThread;

    public double totalAmount = 0;

    public SummaryOfLastMonthFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!SecondActivity.pausedstate()) {
            summaryOfLAstXdaysPaused = false;
        } else {
            summaryOfLAstXdaysPaused = true;

        }

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
        scrollingLastMonthText = view.findViewById(R.id.scrollingLastMonthText);
        scrollingLastMonthText.setSelected(true);
        digitalClock = view.findViewById(R.id.lastXdays_digitalClock);
        digitalClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));
        summaryOfLast30DaysTitle = view.findViewById(R.id.summaryOfLastxDaysTitle);

        sumOfLastXDaysKeyPad = view.findViewById(R.id.sumOfLastXDaysKeyPad);
        sumLastXDayleftArrow = view.findViewById(R.id.sumLastXDayleftArrow);
        sumLastXDayplayPause = view.findViewById(R.id.sumLastXDayplayPause);
        sumLastXDayrightArrow = view.findViewById(R.id.sumLastXDayrightArrow);

        backTraverse(fragment, R.id.summaryOfLastSixMonthsFragment);

        keyPadControl(summaryOfLAstXdaysPaused);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();


        if (SplashScreenActivity.allData != null) {

            int days = SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData().tableData.size();
            summaryOfLast30DaysTitle.setText("Summary of Last " + days + " days");
            summaryOfLast30DaysTitle.append(" from " + new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()));
            scrollingLastMonthText.append(" " + days + " days");
            summaryOfLastMonthFrameLayout.setVisibility(View.GONE);
            initFragment(SplashScreenActivity.allData.getDashBoardData(), 200, 0);
        }
    }

    public void initFragment(DashBoardData dashBoardDataParam, int seconds, int startingIndex) {
        int days = SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData().tableData.size();


        DashBoardData dashBoardData = dashBoardDataParam;
        inflateTable(dashBoardData.getSummaryOfLast30DaysData().tableData, seconds, startingIndex);
//        UtilityFunctionsForActivity2.drawLineChart(dashBoardData.getSummaryOfLast30DaysData().lineChartData, lineChart, "Summarized by last 30 days");
        UtilityFunctionsForActivity2.drawBarChart(dashBoardData.getSummaryOfLast30DaysData().barChartData, barChart, "Summarized by last " + days + "  days");
//        UtilityFunctionsForActivity2.drawStackedBarChart(dashBoardData.getSummaryOfLast30DaysData().barChartData, barChart, "Summarized by last " + days + "  days");
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummaryOfLast30DaysRow> tablesToDisplay, int seconds, int startingIndex) {
        totalAmount = 0;

        summaryOfLast30DaysTableLayout.removeAllViews();
        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                    rowIndex = index;

                }

                if (index == tablesToDisplay.size()) {
                    drawLastTotalRow();
                    new UtilityFunctionsForActivity1().scrollRows(scrollView);
//                } else if (index == tablesToDisplay.size() + 1 && !SecondActivity.summaryOfLast30DaysPause) {
                } else if (index == tablesToDisplay.size() + 1) {

                    if (summaryOfLAstXdaysPaused) {
                        if (handleRowAnimationThread != null) {
                            handleRowAnimationThread.interrupt();
                        }
                    } else {
                        navigate(fragment);
                    }

                } else if (index < tablesToDisplay.size()) {
                    totalLastRow(tablesToDisplay.get(index));
                    new UtilityFunctionsForActivity2().drawSummaryOfLast30Days(tablesToDisplay, getContext(), summaryOfLast30DaysTableLayout, index);
                    new UtilityFunctionsForActivity1().scrollRows(scrollView);
                }

            }

        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, seconds);
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


        tableRowProperty3.setText(numberFormat.format(Math.round(totalAmount * 100.0) / 100.0));
        tableRowProperty3.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty3.setTextSize(16f);


        tableRowProperty4.setText("");

        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        tableRowProperty3.startAnimation(animation);

        summaryOfLast30DaysTableLayout.addView(tableElements);
        new UtilityFunctionsForActivity1().animate(summaryOfLast30DaysTableLayout, tableElements);
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

//        Intent intent = new Intent(getActivity(), VideoActivity.class);
//        intent.putExtra("from", 7);
//        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);


        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(7)) {

//            if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
            if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                navController.navigate(R.id.branchSummaryFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(9))
                navController.navigate(R.id.userReportForAllOusFragment2);
            else if (SplashScreenActivity.allData.getLayoutList().contains(11))
                navController.navigate(R.id.peakHourReportForAllOusFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(1))
//                startActivity(new Intent(requireActivity(), MapsActivity.class));
                navController.navigate(R.id.vansOfASingleOrganizationFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(10))
                navController.navigate(R.id.userReportForEachOusFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(12))
                navController.navigate(R.id.peakHourReportFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(1))
//                startActivity(new Intent(requireActivity(), MapsActivity.class));
                navController.navigate(R.id.mapsFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(3))
                navController.navigate(R.id.summarizedByArticleFragment2);
            else if (SplashScreenActivity.allData.getLayoutList().contains(4))
                navController.navigate(R.id.summarizedByArticleParentCategFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(5))
                navController.navigate(R.id.summarizedByArticleChildCategFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(6))
                navController.navigate(R.id.summaryOfLastSixMonthsFragment);

        }
    }

    public void navigateLeft(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);

        if (SplashScreenActivity.allData.getLayoutList().contains(6))
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(5))
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(4))
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(3))
            navController.navigate(R.id.summarizedByArticleFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(10))
            navController.navigate(R.id.userReportForEachOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(12))
            navController.navigate(R.id.peakHourReportFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(1))
//            navController.navigate(R.id.vansOfASingleOrganizationFragment);
            navController.navigate(R.id.mapsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(1))
//            navController.navigate(R.id.vansOfASingleOrganizationFragment);
            navController.navigate(R.id.vansOfASingleOrganizationFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(11))
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(9))
            navController.navigate(R.id.userReportForAllOusFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
            navController.navigate(R.id.branchSummaryFragment);


    }


    @Override
    public void onStop() {
        super.onStop();
        if (handleRowAnimationThread != null)
            handleRowAnimationThread.interrupt();
    }

    @Override
    public void centerKey() {
        summaryOfLAstXdaysPaused = !summaryOfLAstXdaysPaused;
//        SecondActivity.firstCenterKeyPause = summaryOfLAstXdaysPaused;
        if (!summaryOfLAstXdaysPaused) {
            SecondActivity.playAll();
            navigate(fragment);
        } else {
            SecondActivity.pauseAll();
        }
        keyPadControl(summaryOfLAstXdaysPaused);


    }

    @Override
    public void leftKey() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        navigateLeft(fragment);
    }

    @Override
    public void rightKey() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        navigate(fragment);

    }

    public void keyPadControl(boolean paused) {
        if (paused) {
            sumLastXDayplayPause.setImageResource(R.drawable.ic_play_button__2_);
            sumLastXDayplayPause.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireActivity(), R.color.playbacksForeground)));
            sumOfLastXDaysKeyPad.setVisibility(View.VISIBLE);

        } else {
            sumOfLastXDaysKeyPad.setVisibility(View.GONE);

        }
    }
}
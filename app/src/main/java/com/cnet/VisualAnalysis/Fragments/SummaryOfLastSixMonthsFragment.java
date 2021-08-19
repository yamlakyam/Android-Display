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
import com.cnet.VisualAnalysis.Data.SummaryOfLast6MonthsRow;
import com.cnet.VisualAnalysis.MapsActivity;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class SummaryOfLastSixMonthsFragment extends Fragment implements SecondActivity.KeyPress {


    TableLayout summaryOfLast6MonthsTableLayout;
    Handler animationHandler;
    BarChart barChart;
    PieChart pieChart;
    ScrollView scrollView;
    Fragment fragment;
    FrameLayout summaryOfLastSixMonthFrameLayout;
    TextView scrollingLast6MonthText;
    DigitalClock digitalClock;
    TextView SummaryOfLast6MonthsTitle;

    LinearLayout sumOfLAstXMonthsKeyPad;
    ImageView sumLastXMonthleftArrow;
    ImageView sumLastXMonthrightArrow;
    ImageView sumLastXMonthplayPause;

    public static HandleRowAnimationThread handleRowAnimationThread;
    public static boolean isInflatingTable;
    double totalAmount = 0;

    public static boolean summaryOfLAstXmonthPaused;

    public SummaryOfLastSixMonthsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!SecondActivity.pausedstate()) {
            summaryOfLAstXmonthPaused = false;
        } else {
            summaryOfLAstXmonthPaused = true;

        }


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
        scrollingLast6MonthText = view.findViewById(R.id.scrollingLast6MonthText);
        scrollingLast6MonthText.setSelected(true);

        digitalClock = view.findViewById(R.id.digitalClock);
        digitalClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));
        SummaryOfLast6MonthsTitle = view.findViewById(R.id.SummaryOfLast6MonthsTitle);

        sumOfLAstXMonthsKeyPad = view.findViewById(R.id.sumOfLAstXMonthsKeyPad);
        sumLastXMonthleftArrow = view.findViewById(R.id.sumLastXMonthleftArrow);
        sumLastXMonthrightArrow = view.findViewById(R.id.sumLastXMonthrightArrow);
        sumLastXMonthplayPause = view.findViewById(R.id.sumLastXMonthplayPause);


        backTraverse(fragment, R.id.summarizedByArticleChildCategFragment);
        keyPadControl(summaryOfLAstXmonthPaused);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        if (SplashScreenActivity.allData != null) {
            int months = SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast6MonthsData().getTableData().size();
            SummaryOfLast6MonthsTitle.setText("Summary of Last " + months + " Months");
            SummaryOfLast6MonthsTitle.append(" from " + new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()));
            scrollingLast6MonthText.append(" " + months + " months");
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
//                } else if (index == tablesToDisplay.size() + 1 && !SecondActivity.summaryOfLast6MonsPause) {
                } else if (index == tablesToDisplay.size() + 1) {
                    if (summaryOfLAstXmonthPaused) {
                        if (handleRowAnimationThread != null) {
                            handleRowAnimationThread.interrupt();
                        }
                    } else {
                        navigate(fragment);
                    }
                } else if (index < tablesToDisplay.size()) {
                    totalLastRow(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity2.drawSummaryOfLAst6Months(tablesToDisplay, getContext(), summaryOfLast6MonthsTableLayout, index, totalAmount);
                    UtilityFunctionsForActivity1.scrollRows(scrollView);
                }

            }

        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, seconds, this, 0);
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

//        Intent intent = new Intent(getActivity(), VideoActivity.class);
//        intent.putExtra("from", 6);
//        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);


        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(6)) {
            if (SplashScreenActivity.allData.getLayoutList().contains(7))
                navController.navigate(R.id.summaryOfLastMonthFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                navController.navigate(R.id.branchSummaryFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                navController.navigate(R.id.userReportForAllOusFragment2);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                navController.navigate(R.id.userReportForEachOusFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
                navController.navigate(R.id.peakHourReportForAllOusFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                navController.navigate(R.id.peakHourReportFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(1))
//                startActivity(new Intent(requireActivity(), MapsActivity.class));
                navController.navigate(R.id.vansOfASingleOrganizationFragment);

            else if (SplashScreenActivity.allData.getLayoutList().contains(3))
                navController.navigate(R.id.summarizedByArticleFragment2);
            else if (SplashScreenActivity.allData.getLayoutList().contains(4))
                navController.navigate(R.id.summarizedByArticleParentCategFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(5))
                navController.navigate(R.id.summarizedByArticleChildCategFragment);
        }

    }

    public void navigateLeft(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);

        if (SplashScreenActivity.allData.getLayoutList().contains(6)) {
            if (SplashScreenActivity.allData.getLayoutList().contains(5))
                navController.navigate(R.id.summarizedByArticleChildCategFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(4))
                navController.navigate(R.id.summarizedByArticleParentCategFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(3))
                navController.navigate(R.id.summarizedByArticleFragment2);

            else if (SplashScreenActivity.allData.getLayoutList().contains(1))
                startActivity(new Intent(requireActivity(), MapsActivity.class));
//                navController.navigate(R.id.vansOfASingleOrganizationFragment);

            else if (SplashScreenActivity.allData.getLayoutList().contains(12))
                navController.navigate(R.id.peakHourReportFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(11))
                navController.navigate(R.id.peakHourReportForAllOusFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                navController.navigate(R.id.userReportForEachOusFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                navController.navigate(R.id.userReportForAllOusFragment2);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                navController.navigate(R.id.branchSummaryFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(7))
                navController.navigate(R.id.summaryOfLastMonthFragment);

        }

    }


    @Override
    public void onStop() {
        super.onStop();
        if (handleRowAnimationThread != null)
            handleRowAnimationThread.interrupt();
    }

    @Override
    public void centerKey() {
        summaryOfLAstXmonthPaused = !summaryOfLAstXmonthPaused;
//        SecondActivity.firstCenterKeyPause = summaryOfLAstXmonthPaused;

        if (!summaryOfLAstXmonthPaused) {
            SecondActivity.playAll();
            navigate(fragment);
        } else {
            SecondActivity.pauseAll();
        }
        keyPadControl(summaryOfLAstXmonthPaused);

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
            sumLastXMonthplayPause.setImageResource(R.drawable.ic_play_button__2_);
            sumLastXMonthplayPause.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireActivity(), R.color.playbacksForeground)));
            sumOfLAstXMonthsKeyPad.setVisibility(View.VISIBLE);

        } else {
            sumOfLAstXMonthsKeyPad.setVisibility(View.GONE);

        }
    }
}
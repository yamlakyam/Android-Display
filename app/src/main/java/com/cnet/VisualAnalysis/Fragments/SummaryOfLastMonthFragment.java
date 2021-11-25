package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Data.SummaryOfLast30DaysRow;
import com.cnet.VisualAnalysis.MapsActivity;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.cnet.VisualAnalysis.VideoActivity;
import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SummaryOfLastMonthFragment extends Fragment implements SecondActivity.KeyPress {
    TableLayout summaryOfLast30DaysTableLayout;
    Handler animationHandler;
    ScrollView scrollView;
    Fragment fragment;
    TextView scrollingLastMonthText;
    TextClock lastXdays_textClock;
    TextView summaryOfLast30DaysTitle;
    public static boolean summaryOfLAstXdaysPaused;
    int rowIndex;

    LinearLayout sumOfLastXDaysKeyPad;
    ImageView sumLastXDayleftArrow;
    ImageView sumLastXDayplayPause;
    ImageView sumLastXDayrightArrow;

    MaterialCardView bCardSummaryOfLast30Days;
    TextView tableRowProperty1;
    TextView tableRowProperty2;
    TextView tableRowProperty3;
    TextView tableRowProperty4;

    ConstraintLayout lastXdaysCL;

    public HandleRowAnimationThread handleRowAnimationThread;

    public double totalAmount = 0;
    public int totalTransCount = 0;

    public SummaryOfLastMonthFragment() {

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
        scrollView = view.findViewById(R.id.summaryOfLastMonthScrollView);
        scrollingLastMonthText = view.findViewById(R.id.scrollingLastMonthText);
        scrollingLastMonthText.setSelected(true);

        lastXdays_textClock = view.findViewById(R.id.lastXdays_textClock);
        lastXdays_textClock.setFormat12Hour("kk:mm:ss");
        lastXdays_textClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));

        summaryOfLast30DaysTitle = view.findViewById(R.id.summaryOfLastxDaysTitle);
        sumOfLastXDaysKeyPad = view.findViewById(R.id.sumOfLastXDaysKeyPad);
        sumLastXDayleftArrow = view.findViewById(R.id.sumLastXDayleftArrow);
        sumLastXDayplayPause = view.findViewById(R.id.sumLastXDayplayPause);
        sumLastXDayrightArrow = view.findViewById(R.id.sumLastXDayrightArrow);

        bCardSummaryOfLast30Days = view.findViewById(R.id.bCardSummaryOfLast30Days);
        lastXdaysCL = view.findViewById(R.id.lastXdaysCL);

//        backTraverse(fragment, R.id.summaryOfLastSixMonthsFragment);

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
            summaryOfLast30DaysTitle.append(" on " + new SimpleDateFormat(Constants.dateCriteriaFormat, Locale.getDefault()).format(Calendar.getInstance().getTime()));
            scrollingLastMonthText.append(" " + days + " days");
            initFragment(SplashScreenActivity.allData.getDashBoardData(), 200, 0);
        }
    }

    public void initFragment(DashBoardData dashBoardDataParam, int seconds, int startingIndex) {
        int days = SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData().tableData.size();

        DashBoardData dashBoardData = dashBoardDataParam;
        inflateTable(dashBoardData.getSummaryOfLast30DaysData().tableData, seconds, startingIndex);

        int chartTypeIndex = SplashScreenActivity.allData.getLayoutList().indexOf(Constants.SUMMARY_OF_LAST_X_DAYS_INDEX);
        String chartType = "";
        if (chartTypeIndex < SplashScreenActivity.allData.getChartList().size()) {
            chartType = SplashScreenActivity.allData.getChartList().get(chartTypeIndex);
        } else {
            chartType = "";
        }

        new UtilityFunctionsForActivity2().drawChart(getContext(), chartType, bCardSummaryOfLast30Days,
                dashBoardData.getSummaryOfLast30DaysData().getPieChartData(), dashBoardData.getSummaryOfLast30DaysData().getBarChartData1(),
                dashBoardData.getSummaryOfLast30DaysData().getBarChartData2(), dashBoardData.getSummaryOfLast30DaysData().getLineChartData1(),
                dashBoardData.getSummaryOfLast30DaysData().getLineChartData2(), "Grand Total for the last " + days + " days", "Transaction Count");

    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummaryOfLast30DaysRow> tablesToDisplay, int seconds, int startingIndex) {
        totalAmount = 0;
        totalTransCount = 0;

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
                            handleRowAnimationThread = null;

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
        totalTransCount = totalTransCount + row.getTransactionCount();

    }

    public void drawLastTotalRow() {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View tableElements = inflater.inflate(R.layout.table_row_summary_by_parent_article, null, false);
        tableRowProperty1 = tableElements.findViewById(R.id.tableRowParentArtProperty1);
        tableRowProperty2 = tableElements.findViewById(R.id.tableRowParentArtProperty2);
        tableRowProperty3 = tableElements.findViewById(R.id.tableRowParentArtProperty3);
        tableRowProperty4 = tableElements.findViewById(R.id.tableRowParentArtProperty4);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        tableRowProperty1.setText("");
        tableRowProperty2.setText("Total Amount");
        tableRowProperty2.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty2.setTextSize(16f);

        tableRowProperty3.setText(numberFormat.format(totalTransCount));
        tableRowProperty3.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty3.setTextSize(16f);

//        tableRowProperty4.setText(numberFormat.format(Math.round(totalAmount * 100.0) / 100.0));
        tableRowProperty4.setText((totalAmount > 1) ? UtilityFunctionsForActivity2.decimalFormat.format(totalAmount) :
                UtilityFunctionsForActivity2.smallDecimlFormat.format(totalAmount));
        tableRowProperty4.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty4.setTextSize(16f);

        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        tableRowProperty2.startAnimation(animation);
        tableRowProperty3.startAnimation(animation);
        tableRowProperty4.startAnimation(animation);

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
        requireActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

        NavController navController = NavHostFragment.findNavController(fragment);

//            if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
        if (SplashScreenActivity.allData.getLayoutList().contains(8) &&
                SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData() != null
                && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
            navController.navigate(R.id.branchSummaryFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(9) &&
                SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().size() > 0)
            navController.navigate(R.id.userReportForAllOusFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(11) &&
                SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch().size() > 0)
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(10) &&
                SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size() > 0)
            navController.navigate(R.id.userReportForEachOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(12) &&
                SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size() > 0)
            navController.navigate(R.id.peakHourReportFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(1) &&
                SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans() != null
                && SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size() > 0)
//            navController.navigate(R.id.mapsFragment);
            startActivity(new Intent(requireActivity(), MapsActivity.class));

        else
            startActivity(new Intent(requireActivity(), VideoActivity.class));

    }

    public void navigateLeft(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);

        if (SplashScreenActivity.allData.getLayoutList().contains(6) &&
                SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast6MonthsData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast6MonthsData().getTableData().size() > 0)
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(5) &&
                SplashScreenActivity.allData.getDashBoardData().getSummarizedByChildArticleData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummarizedByChildArticleData().getTableData().size() > 0)
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(4) &&
                SplashScreenActivity.allData.getDashBoardData().getSummarizedByParentArticleData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummarizedByParentArticleData().getTableData().size() > 0)
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(3) &&
                SplashScreenActivity.allData.getDashBoardData().getSummarizedByArticleData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummarizedByArticleData().tableData.size() > 0)
            navController.navigate(R.id.summarizedByArticleFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(1) &&
                SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans() != null
                && SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size() > 0)
//            navController.navigate(R.id.mapsFragment);
            startActivity(new Intent(requireActivity(), MapsActivity.class));

        else if (SplashScreenActivity.allData.getLayoutList().contains(12) &&
                SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size() > 0)
            navController.navigate(R.id.peakHourReportFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(10) &&
                SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size() > 0)
            navController.navigate(R.id.userReportForEachOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(11) &&
                SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch().size() > 0)
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(9) &&
                SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().size() > 0)
            navController.navigate(R.id.userReportForAllOusFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(8) &&
                SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData() != null
                && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
            navController.navigate(R.id.branchSummaryFragment);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
            handleRowAnimationThread = null;
        }

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
            handleRowAnimationThread = null;

        }
        navigateLeft(fragment);
    }

    @Override
    public void rightKey() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
            handleRowAnimationThread = null;

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        summaryOfLast30DaysTableLayout = null;
        scrollView = null;
        scrollingLastMonthText = null;
        lastXdays_textClock = null;
        summaryOfLast30DaysTitle = null;
        sumOfLastXDaysKeyPad = null;
        sumLastXDayleftArrow = null;
        sumLastXDayplayPause = null;
        sumLastXDayrightArrow = null;
        bCardSummaryOfLast30Days = null;

        tableRowProperty1 = null;
        tableRowProperty2 = null;
        tableRowProperty3 = null;
        tableRowProperty4 = null;

        lastXdaysCL = null;
        if (animationHandler != null)
            animationHandler.removeCallbacksAndMessages(null);

    }
}
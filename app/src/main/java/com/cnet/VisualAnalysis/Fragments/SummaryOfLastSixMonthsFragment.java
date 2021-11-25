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
import com.cnet.VisualAnalysis.Data.SummaryOfLast6MonthsRow;
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


public class SummaryOfLastSixMonthsFragment extends Fragment implements SecondActivity.KeyPress {


    TableLayout summaryOfLast6MonthsTableLayout;
    Handler animationHandler;
    ScrollView scrollView;
    Fragment fragment;
    TextView scrollingLast6MonthText;
    TextClock lastXmons_textClock;
    TextView SummaryOfLast6MonthsTitle;
    LinearLayout sumOfLAstXMonthsKeyPad;
    ImageView sumLastXMonthleftArrow;
    ImageView sumLastXMonthrightArrow;
    ImageView sumLastXMonthplayPause;

    MaterialCardView pCardSummOfLast6Months;

    ConstraintLayout lastXmonthCL;

    public HandleRowAnimationThread handleRowAnimationThread;
    double totalAmount = 0;
    int totalTransCount = 0;
    public static boolean summaryOfLAstXmonthPaused;

    TextView tableRowProperty1;
    TextView tableRowProperty2;
    TextView tableRowProperty3;
    TextView tableRowProperty4;

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
        scrollView = view.findViewById(R.id.summaryOfLast6MonsScrollView);
        scrollingLast6MonthText = view.findViewById(R.id.scrollingLast6MonthText);
        scrollingLast6MonthText.setSelected(true);

        lastXmons_textClock = view.findViewById(R.id.lastXmons_textClock);
        lastXmons_textClock.setFormat12Hour("kk:mm:ss");
        lastXmons_textClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));

        SummaryOfLast6MonthsTitle = view.findViewById(R.id.SummaryOfLast6MonthsTitle);
        sumOfLAstXMonthsKeyPad = view.findViewById(R.id.sumOfLAstXMonthsKeyPad);
        sumLastXMonthleftArrow = view.findViewById(R.id.sumLastXMonthleftArrow);
        sumLastXMonthrightArrow = view.findViewById(R.id.sumLastXMonthrightArrow);
        sumLastXMonthplayPause = view.findViewById(R.id.sumLastXMonthplayPause);
        pCardSummOfLast6Months = view.findViewById(R.id.pCardSummOfLast6Months);
        lastXmonthCL = view.findViewById(R.id.lastXmonthCL);

//        backTraverse(fragment, R.id.summarizedByArticleChildCategFragment);
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
            SummaryOfLast6MonthsTitle.append(" on " + new SimpleDateFormat(Constants.dateCriteriaFormat, Locale.getDefault()).format(Calendar.getInstance().getTime()));
            scrollingLast6MonthText.append(" " + months + " months");

            initFragment(SplashScreenActivity.allData.getDashBoardData(), 200);
        }
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummaryOfLast6MonthsRow> tablesToDisplay, int seconds) {
        totalAmount = 0;

        summaryOfLast6MonthsTableLayout.removeAllViews();
        animationHandler = new Handler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }

                if (index == tablesToDisplay.size()) {
                    drawLast6MonsTotalRow();
                    new UtilityFunctionsForActivity1().scrollRows(scrollView);
//                } else if (index == tablesToDisplay.size() + 1 && !SecondActivity.summaryOfLast6MonsPause) {
                } else if (index == tablesToDisplay.size() + 1) {
                    if (summaryOfLAstXmonthPaused) {
                        if (handleRowAnimationThread != null) {
                            handleRowAnimationThread.interrupt();
                            handleRowAnimationThread = null;

                        }
                    } else {
                        navigate(fragment);
                    }
                } else if (index < tablesToDisplay.size()) {
                    totalLastRow(tablesToDisplay.get(index));
                    new UtilityFunctionsForActivity2().drawSummaryOfLAst6Months(tablesToDisplay, getContext(), summaryOfLast6MonthsTableLayout, index);
                    new UtilityFunctionsForActivity1().scrollRows(scrollView);
                }

            }

        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, seconds);
        handleRowAnimationThread.start();
    }


    public void initFragment(DashBoardData dashBoardDataParam, int seconds) {

        DashBoardData dashBoardData = dashBoardDataParam;

        inflateTable(dashBoardData.getSummaryOfLast6MonthsData().getTableData(), seconds);
//        new UtilityFunctionsForActivity2().drawBarChart(dashBoardData.getSummaryOfLast6MonthsData().getBarChartData(), barChart, "Summarized by last 6 months");
//        new UtilityFunctionsForActivity2().drawPieChart(dashBoardData.getSummaryOfLast6MonthsData().getPieChartData(), pieChart, "Summarized by last 6 months");

        int chartTypeIndex = SplashScreenActivity.allData.getLayoutList().indexOf(Constants.SUMMARY_OF_LAST_X_MONTHS_INDEX);
        String chartType = "";
        if (chartTypeIndex < SplashScreenActivity.allData.getChartList().size()) {
            chartType = SplashScreenActivity.allData.getChartList().get(chartTypeIndex);
        } else {
            chartType = "";
        }
        int months = SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast6MonthsData().getTableData().size();

        new UtilityFunctionsForActivity2().drawChart(getContext(), chartType, pCardSummOfLast6Months,
                dashBoardData.getSummaryOfLast6MonthsData().getPieChartData(), dashBoardData.getSummaryOfLast6MonthsData().getBarChartData1(),
                dashBoardData.getSummaryOfLast6MonthsData().getBarChartData2(), dashBoardData.getSummaryOfLast6MonthsData().getLineChartDat1(),
                dashBoardData.getSummaryOfLast6MonthsData().getLineChartData2(), "Grand Total for the last " + months + " months", "Transaction Count");
    }


    public void totalLastRow(SummaryOfLast6MonthsRow row) {
        totalAmount = totalAmount + row.getAmount();
        totalTransCount = totalTransCount + row.getTransactionCount();

    }

    public void drawLast6MonsTotalRow() {
        View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.table_row_summary_by_parent_article, null, false);

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
        summaryOfLast6MonthsTableLayout.addView(tableElements);
        new UtilityFunctionsForActivity1().animate(summaryOfLast6MonthsTableLayout, tableElements);
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

        if (SplashScreenActivity.allData.getLayoutList().contains(7) &&
                SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData().tableData.size() > 0)
            navController.navigate(R.id.summaryOfLastMonthFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(8) &&
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

        if (SplashScreenActivity.allData.getLayoutList().contains(6)) {
            if (SplashScreenActivity.allData.getLayoutList().contains(5) &&
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
//                navController.navigate(R.id.mapsFragment);
                startActivity(new Intent(requireActivity(), MapsActivity.class));

            else if (SplashScreenActivity.allData.getLayoutList().contains(12) && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch() != null)
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
            else if (SplashScreenActivity.allData.getLayoutList().contains(7) &&
                    SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData() != null
                    && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData().tableData.size() > 0)
                navController.navigate(R.id.summaryOfLastMonthFragment);
            else
//                initFragment(SplashScreenActivity.allData.getDashBoardData(), 200);
                startActivity(new Intent(requireActivity(), VideoActivity.class));

        }

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
            sumLastXMonthplayPause.setImageResource(R.drawable.ic_play_button__2_);
            sumLastXMonthplayPause.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireActivity(), R.color.playbacksForeground)));
            sumOfLAstXMonthsKeyPad.setVisibility(View.VISIBLE);

        } else {
            sumOfLAstXMonthsKeyPad.setVisibility(View.GONE);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        summaryOfLast6MonthsTableLayout = null;
        scrollView = null;
        scrollingLast6MonthText = null;
        lastXmons_textClock = null;
        SummaryOfLast6MonthsTitle = null;
        sumOfLAstXMonthsKeyPad = null;
        sumLastXMonthleftArrow = null;
        sumLastXMonthrightArrow = null;
        sumLastXMonthplayPause = null;
        pCardSummOfLast6Months = null;
        tableRowProperty1 = null;
        tableRowProperty2 = null;
        tableRowProperty3 = null;
        tableRowProperty4 = null;
        lastXmonthCL = null;
        if (animationHandler != null)
            animationHandler.removeCallbacksAndMessages(null);
    }
}
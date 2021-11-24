package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.cnet.VisualAnalysis.Data.BranchSummaryTableRow;
import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.cnet.VisualAnalysis.VideoActivity;
import com.github.mikephil.charting.charts.PieChart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class BranchSummaryFragment extends Fragment implements SecondActivity.KeyPress {

    TableLayout branchSummaryTableLayout;
    ScrollView scrollBranchSummaryTable;
    PieChart pChartBranchSummary;
    Handler animationHandler;
    Fragment fragment;
    TextView scrollingBranchText;

    TextClock branchSummary_textClock;
    TextView branchSummaryHeaderTextView;
    public static boolean branchSummaryPaused;
    ImageView brancplayPause;
    ImageView brancleft;
    ImageView brancright;
    LinearLayout linearLayout;

    TextView tableRowProperty1;
    TextView tableRowProperty2;
    TextView tableRowProperty3;
    TextView tableRowProperty4;
    TextView tableRowProperty5;
    TextView tableRowProperty6;
    TextView tableRowProperty7;

    ConstraintLayout branchSummaryCL;

    public HandleRowAnimationThread handleRowAnimationThread;

    int totalQuantity = 0;
    double grandTotal = 0;
    int totalTransCount = 0;
    double totalPercentage = 0;

    Activity activity;
    int rowIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SecondActivity.pausedstate()) {
            branchSummaryPaused = false;
        } else {
            branchSummaryPaused = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = getActivity();

        View view = inflater.inflate(R.layout.fragment_branch_summary, container, false);

        branchSummaryTableLayout = view.findViewById(R.id.branchSummaryTableLayout);
        scrollBranchSummaryTable = view.findViewById(R.id.scrollBranchSummaryTable);
        pChartBranchSummary = view.findViewById(R.id.pChartBranchSummary);
        scrollingBranchText = view.findViewById(R.id.scrollingBranchText);
        brancplayPause = view.findViewById(R.id.brancplayPause);
        brancleft = view.findViewById(R.id.branchleftArrow);
        brancright = view.findViewById(R.id.brancrightArrow);
        linearLayout = view.findViewById(R.id.branchKeypad);
        branchSummaryCL = view.findViewById(R.id.branchSummaryCL);

        scrollingBranchText.setSelected(true);
        branchSummary_textClock = view.findViewById(R.id.branchSummary_textClock);
        branchSummary_textClock.setFormat12Hour("kk:mm:ss");
        branchSummary_textClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));

        branchSummaryHeaderTextView = view.findViewById(R.id.branchSummaryHeaderTextView);
        branchSummaryHeaderTextView.append(" on " + new SimpleDateFormat(Constants.dateCriteriaFormat, Locale.getDefault()).format(Calendar.getInstance().getTime()));
        fragment = this;

        backTraverse(fragment, R.id.summaryOfLastMonthFragment);

        keyPadControl(branchSummaryPaused);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SplashScreenActivity.allData != null) {
            initFragment(SplashScreenActivity.allData.getDashBoardData(), 200, 0);
        }
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<BranchSummaryTableRow> tablesToDisplay, int seconds, int startingRowIndex) {
        grandTotal = 0;
        totalQuantity = 0;
        totalTransCount = 0;
        totalPercentage = 0;

        branchSummaryTableLayout.removeAllViews();
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
                    drawLastBranchSummaryRow();
                    new UtilityFunctionsForActivity1().scrollRows(scrollBranchSummaryTable);

                } else if (index == tablesToDisplay.size() + 1) {
                    if (branchSummaryPaused) {
                        if (handleRowAnimationThread != null) {
                            handleRowAnimationThread.interrupt();
                            handleRowAnimationThread = null;

                        }
                    } else {
                        navigate(fragment);
                    }

                } else if (index < tablesToDisplay.size()) {
                    totalLastRow(tablesToDisplay.get(index));
                    new UtilityFunctionsForActivity2().drawBranchSummary(tablesToDisplay, getContext(), branchSummaryTableLayout, index);
                    new UtilityFunctionsForActivity1().scrollRows(scrollBranchSummaryTable);
                }

            }

        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, seconds);
        handleRowAnimationThread.start();
    }


    public void initFragment(DashBoardData dashBoardDataParam, int seconds, int startingRowIndex) {
        DashBoardData dashBoardData = dashBoardDataParam;

        ArrayList<BranchSummaryTableRow> branchSummaryTableRows = dashBoardData.getBranchSummaryData().getBranchSummaryTableRows();
        try {
//            Collections.sort(branchSummaryTableRows, new Comparator<BranchSummaryTableRow>() {
//                @Override
//                public int compare(BranchSummaryTableRow o1, BranchSummaryTableRow o2) {
//                    return o1.getGrandTotal() > o2.getGrandTotal() ? 1 : 0;
//                }
//            });
            Collections.sort(branchSummaryTableRows, new Comparator<BranchSummaryTableRow>() {
                @Override
                public int compare(BranchSummaryTableRow o1, BranchSummaryTableRow o2) {
                    return new UtilityFunctionsForActivity1().formatTime(o2.getLastActivity())
                            .compareTo(new UtilityFunctionsForActivity1().formatTime(o1.getLastActivity()));


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        inflateTable(dashBoardData.getBranchSummaryData().getBranchSummaryTableRows(), seconds, startingRowIndex);
        new UtilityFunctionsForActivity2().drawPieChart(dashBoardData.getBranchSummaryData().getPieChartData(), pChartBranchSummary, "Branch summary");
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

    public void totalLastRow(BranchSummaryTableRow row) {

        double grandTotalForAll = 0;
        ArrayList<BranchSummaryTableRow> branchSummaryTableRowArrayList =
                SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows();

        for (int i = 0; i < branchSummaryTableRowArrayList.size(); i++) {
            double grandTotalForI = branchSummaryTableRowArrayList.get(i).getGrandTotal();
            grandTotalForAll = grandTotalForAll + grandTotalForI;
        }

        totalQuantity = totalQuantity + row.getLineItems();
        grandTotal = grandTotal + row.getGrandTotal();
        totalTransCount = totalTransCount + row.getTransactionCount();
        totalPercentage = totalPercentage + (row.getGrandTotal() / grandTotalForAll) * 100;
    }

    public void drawLastBranchSummaryRow() {
        View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.table_row_branch_summary, null, false);

        tableRowProperty1 = tableElements.findViewById(R.id.tableRowBranchSummary1);
        tableRowProperty2 = tableElements.findViewById(R.id.tableRowBranchSummary2);
        tableRowProperty3 = tableElements.findViewById(R.id.tableRowBranchSummary3);
        tableRowProperty4 = tableElements.findViewById(R.id.tableRowBranchSummary4);
        tableRowProperty5 = tableElements.findViewById(R.id.tableRowBranchSummary5);
        tableRowProperty6 = tableElements.findViewById(R.id.tableRowBranchSummary6);
        tableRowProperty7 = tableElements.findViewById(R.id.tableRowBranchSummary7);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        tableRowProperty1.setText("");
        tableRowProperty2.setText("Total Amount");
        tableRowProperty2.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty2.setTextSize(16f);
        tableRowProperty3.setText(numberFormat.format(totalQuantity));
        tableRowProperty3.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty3.setTextSize(16f);
        tableRowProperty4.setText(numberFormat.format(totalTransCount));
        tableRowProperty4.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty4.setTextSize(16f);
        tableRowProperty5.setText("");
        tableRowProperty6.setText(UtilityFunctionsForActivity2.decimalFormat.format(totalPercentage) + "%");
        tableRowProperty6.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty6.setTextSize(16f);
        tableRowProperty7.setText(UtilityFunctionsForActivity2.decimalFormat.format(grandTotal));
        tableRowProperty7.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty7.setTextSize(16f);

        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        tableRowProperty2.startAnimation(animation);
        tableRowProperty3.startAnimation(animation);
        tableRowProperty4.startAnimation(animation);
        tableRowProperty4.startAnimation(animation);
        tableRowProperty5.startAnimation(animation);
        tableRowProperty6.startAnimation(animation);
        tableRowProperty7.startAnimation(animation);

        branchSummaryTableLayout.addView(tableElements);
        new UtilityFunctionsForActivity2().animateBottomToTop(branchSummaryTableLayout, tableElements);
    }

    public void navigate(Fragment fragment) {
        requireActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        NavController navController = NavHostFragment.findNavController(fragment);

        if (SplashScreenActivity.allData.getLayoutList().contains(9) &&
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
            navController.navigate(R.id.mapsFragment);
        else {
            startActivity(new Intent(requireActivity(), VideoActivity.class));
        }

    }

    public void leftNavigate(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(7) &&
                SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData() != null &&
                SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData().tableData.size() > 0)
            navController.navigate(R.id.summaryOfLastMonthFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(6) &&
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
            navController.navigate(R.id.mapsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(10) &&
                SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size() > 0)
            navController.navigate(R.id.userReportForEachOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(12) &&
                SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size() > 0)
            navController.navigate(R.id.peakHourReportFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(11) &&
                SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch().size() > 0)
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(9) &&
                SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().size() > 0)
            navController.navigate(R.id.userReportForAllOusFragment2);
        else
//            initFragment(SplashScreenActivity.allData.getDashBoardData(), 200, 0);
            startActivity(new Intent(requireActivity(), VideoActivity.class));

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
        branchSummaryPaused = !branchSummaryPaused;

        if (!branchSummaryPaused) {
            SecondActivity.playAll();
            navigate(fragment);
        } else {
            SecondActivity.pauseAll();
        }
        keyPadControl(branchSummaryPaused);

    }

    @Override
    public void leftKey() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
            handleRowAnimationThread = null;
        }
        leftNavigate(fragment);
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
            brancplayPause.setImageResource(R.drawable.ic_play_button__2_);
            brancplayPause.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireActivity(), R.color.playbacksForeground)));
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        branchSummaryTableLayout = null;
        scrollBranchSummaryTable = null;
        pChartBranchSummary = null;
        scrollingBranchText = null;
        brancplayPause = null;
        brancleft = null;
        brancright = null;
        linearLayout = null;
        branchSummary_textClock = null;
        branchSummaryHeaderTextView = null;
        branchSummaryCL = null;

        tableRowProperty1 = null;
        tableRowProperty2 = null;
        tableRowProperty3 = null;
        tableRowProperty4 = null;
        tableRowProperty6 = null;
        tableRowProperty7 = null;

        if (animationHandler != null)
            animationHandler.removeCallbacksAndMessages(null);

    }
}
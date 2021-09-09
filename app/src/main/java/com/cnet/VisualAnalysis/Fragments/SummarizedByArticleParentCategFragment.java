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
import android.widget.TextClock;
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
import com.cnet.VisualAnalysis.Data.SummarizedByParentArticleRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SummarizedByArticleParentCategFragment extends Fragment implements SecondActivity.KeyPress {

    Handler animationHandler;

    TableLayout summarizedByParentArticleTableLayout;
    ScrollView scrollView;
    PieChart pieChart;
    BarChart barChart;
    Fragment fragment;
    FrameLayout summarizedByParentArticleFrameLayout;
    TextView scrollingParentText;
    DigitalClock digitalClock;
    TextClock parentArticle_textClock;
    TextView articleParentSummaryTitle;

    LinearLayout sumByParentKeyPad;
    ImageView sumParentArticleleftArrow;
    ImageView sumParentArticleplayPause;
    ImageView sumParentArticlerightArrow;

    MaterialCardView pCardSummByArticleParent;

    public HandleRowAnimationThread handleRowAnimationThread;

    double grandTotal = 0;
    double totalPercentage = 0;
    public static boolean summByParentArticlePaused;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SecondActivity.pausedstate()) {
            summByParentArticlePaused = false;
        } else {
            summByParentArticlePaused = true;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_summarized_by_article_parent_categ, container, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragment = this;


        summarizedByParentArticleTableLayout = view.findViewById(R.id.summaryByChildArticleTableLayout);
        scrollView = view.findViewById(R.id.summarizedByChildArticleScrollView);
        barChart = view.findViewById(R.id.bChartSumByArticleParent);
        summarizedByParentArticleFrameLayout = view.findViewById(R.id.summarizedByParentArticleFrameLayout);
        scrollingParentText = view.findViewById(R.id.scrollingParentText);
        scrollingParentText.setSelected(true);
//        digitalClock = view.findViewById(R.id.parentArticle_digitalClock);
//        digitalClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));

        parentArticle_textClock = view.findViewById(R.id.parentArticle_textClock);
        parentArticle_textClock.setFormat12Hour("kk:mm:ss");
        parentArticle_textClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));

        articleParentSummaryTitle = view.findViewById(R.id.articleParentSummaryTitle);
        articleParentSummaryTitle.append(" on " + new SimpleDateFormat(Constants.dateCriteriaFormat, Locale.getDefault()).format(Calendar.getInstance().getTime()));
        sumByParentKeyPad = view.findViewById(R.id.sumByParentKeyPad);
        sumParentArticleleftArrow = view.findViewById(R.id.sumParentArticleleftArrow);
        sumParentArticleplayPause = view.findViewById(R.id.sumParentArticleplayPause);
        sumParentArticlerightArrow = view.findViewById(R.id.sumParentArticlerightArrow);

        pCardSummByArticleParent = view.findViewById(R.id.pCardSummByArticleParent);

//        backTraverse(fragment, R.id.summarizedByArticleFragment2);

        keyPadControl(summByParentArticlePaused);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (SplashScreenActivity.allData != null) {
            summarizedByParentArticleFrameLayout.setVisibility(View.GONE);
            if (SplashScreenActivity.allData.getDashBoardData().getSummarizedByParentArticleData() != null) {
                initFragment(SplashScreenActivity.allData.getDashBoardData(), 200);
            } else {
                navigate(fragment);
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummarizedByParentArticleRow> tablesToDisplay, int seconds) {

        grandTotal = 0;
        totalPercentage = 0;
        if (summarizedByParentArticleTableLayout != null) {
            summarizedByParentArticleTableLayout.removeAllViews();

        }
        animationHandler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }
                if (index == tablesToDisplay.size()) {
                    drawLastArticleParentRow();
                    new UtilityFunctionsForActivity1().scrollRows(scrollView);

//                } else if (index == tablesToDisplay.size() + 1 && !SecondActivity.summaryByParentArticlePause) {
                } else if (index == tablesToDisplay.size() + 1) {

                    if (summByParentArticlePaused) {
                        if (handleRowAnimationThread != null) {
                            handleRowAnimationThread.interrupt();
                        }
                    } else {
                        navigate(fragment);
                    }

                } else if (index < tablesToDisplay.size()) {
                    totalLastRow(tablesToDisplay.get(index));
                    new UtilityFunctionsForActivity2().drawSummaryByParentArticleTable(tablesToDisplay, requireActivity().getApplicationContext(), summarizedByParentArticleTableLayout, index);
                    new UtilityFunctionsForActivity1().scrollRows(scrollView);
                }
            }
        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, seconds);
        handleRowAnimationThread.start();

    }


    private void initFragment(DashBoardData dashBoardDataP, int seconds) {


        DashBoardData dashBoardData = dashBoardDataP;

        if (dashBoardData != null) {
            if (dashBoardData.getSummarizedByParentArticleData() != null) {
                inflateTable(dashBoardData.getSummarizedByParentArticleData().getTableData(), seconds);

                int chartTypeIndex = SplashScreenActivity.allData.getLayoutList().indexOf(Constants.SUMMARY_OF_parent_ARTICLE_INDEX);
                String chartType = "";
                if (chartTypeIndex < SplashScreenActivity.allData.getChartList().size()) {
                    chartType = SplashScreenActivity.allData.getChartList().get(chartTypeIndex);
                } else {
                    chartType = "";
                }

//                new UtilityFunctionsForActivity2().drawBarChart(dashBoardData.getSummarizedByParentArticleData().getBarChartData(), barChart, "Summarized by Article parent category");
//                new UtilityFunctionsForActivity2().drawPieChart(dashBoardData.getSummarizedByParentArticleData().getPieChartData(), pieChart, "Summarized by Article parent category");
                new UtilityFunctionsForActivity2().drawChart(getContext(), chartType, pCardSummByArticleParent,
                        dashBoardData.getSummarizedByParentArticleData().getPieChartData(), dashBoardData.getSummarizedByParentArticleData().getBarChartData(),
                        dashBoardData.getSummarizedByParentArticleData().getLineChartData(), "Summarized by Article parent category");
            }
        }
    }

    public void totalLastRow(SummarizedByParentArticleRow row) {

        double grandTotalForAll = 0;
        ArrayList<SummarizedByParentArticleRow> summarizedByParentArticleRows = SplashScreenActivity.allData.getDashBoardData().getSummarizedByParentArticleData().getTableData();
        for (int i = 0; i < summarizedByParentArticleRows.size(); i++) {
            double grandTotalForI = summarizedByParentArticleRows.get(i).getTotalAmount() +
                    summarizedByParentArticleRows.get(i).getTotalServCharge() +
                    summarizedByParentArticleRows.get(i).getTaxAmount();
            grandTotalForAll = grandTotalForAll + grandTotalForI;
        }
//        double percentage = (grandTotal / grandTotalForAll) * 100;

        grandTotal = grandTotal + row.getTotalAmount() + row.getTaxAmount() + row.getTotalServCharge();
        totalPercentage = totalPercentage + ((row.getTotalAmount() + row.getTotalServCharge() + row.getTaxAmount()) / grandTotalForAll) * 100;

    }

    public void drawLastArticleParentRow() {
        View tableElements = LayoutInflater.from(requireActivity().getApplicationContext()).inflate(R.layout.table_row_summary_by_parent_article, null, false);
        TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowParentArtProperty1);
        TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowParentArtProperty2);
        TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowParentArtProperty3);
        TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowParentArtProperty4);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        tableRowProperty1.setText("");
        tableRowProperty2.setText("Grand Total");
        tableRowProperty2.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty2.setTextSize(16f);

        tableRowProperty3.setText(UtilityFunctionsForActivity2.decimalFormat.format(totalPercentage) + "%");
        tableRowProperty3.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty3.setTextSize(16f);

        tableRowProperty4.setText(UtilityFunctionsForActivity2.decimalFormat.format(grandTotal));
        tableRowProperty4.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty4.setTextSize(16f);

        Animation animation = AnimationUtils.loadAnimation(requireActivity().getApplicationContext(), R.anim.blink);
        tableRowProperty2.startAnimation(animation);
        tableRowProperty3.startAnimation(animation);
        tableRowProperty4.startAnimation(animation);
        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));
        summarizedByParentArticleTableLayout.addView(tableElements);
        new UtilityFunctionsForActivity1().animate(summarizedByParentArticleTableLayout, tableElements);

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
        if (SplashScreenActivity.allData.getLayoutList().contains(5) && SplashScreenActivity.allData.getDashBoardData().getSummarizedByChildArticleData() != null)
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(6) && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast6MonthsData() != null)
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(7) && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData() != null)
            navController.navigate(R.id.summaryOfLastMonthFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
            navController.navigate(R.id.branchSummaryFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(9) && SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch() != null)
            navController.navigate(R.id.userReportForAllOusFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(11) && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch() != null)
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(10) && SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch() != null)
            navController.navigate(R.id.userReportForEachOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(12) && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch() != null)
            navController.navigate(R.id.peakHourReportFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(1) && SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans() != null)
            navController.navigate(R.id.mapsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(3) && SplashScreenActivity.allData.getDashBoardData().getSummarizedByArticleData() != null)
            navController.navigate(R.id.summarizedByArticleFragment2);
        else
            initFragment(SplashScreenActivity.allData.getDashBoardData(), 200);

    }

    public void navigateLeft(Fragment fragment) {

        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(4)) {
            if (SplashScreenActivity.allData.getLayoutList().contains(3) && SplashScreenActivity.allData.getDashBoardData().getSummarizedByArticleData() != null)
                navController.navigate(R.id.summarizedByArticleFragment2);
            else if (SplashScreenActivity.allData.getLayoutList().contains(1) && SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans() != null)
                navController.navigate(R.id.mapsFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(12) && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch() != null)
                navController.navigate(R.id.peakHourReportFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(10) && SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch() != null)
                navController.navigate(R.id.userReportForEachOusFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(11) && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch() != null)
                navController.navigate(R.id.peakHourReportForAllOusFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(9) && SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch() != null)
                navController.navigate(R.id.userReportForAllOusFragment2);
            else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                navController.navigate(R.id.branchSummaryFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(7) && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData() != null)
                navController.navigate(R.id.summaryOfLastMonthFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(6) && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast6MonthsData() != null)
                navController.navigate(R.id.summaryOfLastSixMonthsFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(5) && SplashScreenActivity.allData.getDashBoardData().getSummarizedByChildArticleData() != null)
                navController.navigate(R.id.summarizedByArticleChildCategFragment);
            else
                initFragment(SplashScreenActivity.allData.getDashBoardData(), 200);

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
        summByParentArticlePaused = !summByParentArticlePaused;
        if (!summByParentArticlePaused) {
            SecondActivity.playAll();
            navigate(fragment);
        } else {
            SecondActivity.pauseAll();
        }
        keyPadControl(summByParentArticlePaused);

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
            sumParentArticleplayPause.setImageResource(R.drawable.ic_play_button__2_);
            sumParentArticleplayPause.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireActivity(), R.color.playbacksForeground)));
            sumByParentKeyPad.setVisibility(View.VISIBLE);

        } else {
            sumByParentKeyPad.setVisibility(View.GONE);
        }
    }
}
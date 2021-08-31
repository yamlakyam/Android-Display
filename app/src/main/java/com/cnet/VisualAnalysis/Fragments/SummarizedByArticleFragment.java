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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
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
import com.cnet.VisualAnalysis.Data.SummarizedByArticleTableRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SummarizedByArticleFragment extends Fragment implements SecondActivity.KeyPress {

    BarChart barChartSumByArticle;
    LineChart lineChartSumByArticle;

    TableLayout summarizedByArticleTableLayout;
    Handler animationHandler;
    TextView scrollingArticleText;
    TextView summarizedByArticleTextView;
    ScrollView summByArticleScrollView;
    ProgressBar articleSummaryProgressBar;
    ConstraintLayout constraintLayout;

    LinearLayout sumByArticleKeyPad;
    ImageView summarticleleftArrow;
    ImageView summarticleplayPause;
    ImageView summarticlerightArrow;

    DigitalClock digitalClock;
    public HandleRowAnimationThread handleRowAnimationThread;
    public static boolean summByarticlePaused;

    double totalUnitAmount = 0;
    int totalQuantity = 0;

    Fragment fragment;
    MaterialCardView pCardSummByArticle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SecondActivity.pausedstate()) {
            summByarticlePaused = false;
        } else {
            summByarticlePaused = true;
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_summarized_by_article, container, false);
        fragment = this;


//        pChartSumByArticle = view.findViewById(R.id.pChartSumByArticle);
        summarizedByArticleTableLayout = view.findViewById(R.id.summaryByArticleTableLayout);
        scrollingArticleText = view.findViewById(R.id.scrollingArticleText);
        scrollingArticleText.setSelected(true);
        summByArticleScrollView = view.findViewById(R.id.summByArticleScrollView);
        articleSummaryProgressBar = view.findViewById(R.id.articleSummaryProgressBar);
        constraintLayout = view.findViewById(R.id.constraintLayout);
        digitalClock = view.findViewById(R.id.articleSummary_digitalClock);
        digitalClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));
        summarizedByArticleTextView = view.findViewById(R.id.summarizedByArticleTextView);
        summarizedByArticleTextView.append(" from " + new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()));

        summarticlerightArrow = view.findViewById(R.id.summarticlerightArrow);
        summarticleplayPause = view.findViewById(R.id.summarticleplayPause);
        summarticleleftArrow = view.findViewById(R.id.summarticleleftArrow);
        sumByArticleKeyPad = view.findViewById(R.id.sumByArticleKeyPad);

        pCardSummByArticle = view.findViewById(R.id.pCardSummByArticle);

//        AnimationVideoFragment animationVideoFragment = new AnimationVideoFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt("nav_from", 3);
//        animationVideoFragment.setArguments(bundle);

        keyPadControl(summByarticlePaused);
        backTraverse();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (SplashScreenActivity.allData != null) {
            articleSummaryProgressBar.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.VISIBLE);
            initFragment(SplashScreenActivity.allData.getDashBoardData(), 100);
        }
    }


    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummarizedByArticleTableRow> tablesToDisplay, int seconds) {
        totalQuantity = 0;
        totalUnitAmount = 0;
        summarizedByArticleTableLayout.removeAllViews();
        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }
                if (index == tablesToDisplay.size()) {
                    drawLastArticleSummaryTotalRow();
                    new UtilityFunctionsForActivity1().scrollRows(summByArticleScrollView);
//                } else if (index == tablesToDisplay.size() + 1 && !SecondActivity.summaryByArticlePause) {
                } else if (index == tablesToDisplay.size() + 1) {
                    if (getContext() != null) {
                        if (summByarticlePaused) {
                            if (handleRowAnimationThread != null) {
                                handleRowAnimationThread.interrupt();
                            }
                        } else {
                            navigate(fragment);
                        }
                    }

                } else if (index < tablesToDisplay.size()) {
                    totalLastRow(tablesToDisplay.get(index));
                    new UtilityFunctionsForActivity2().drawSummaryByArticleTable(tablesToDisplay, getContext(), summarizedByArticleTableLayout, index);
                    new UtilityFunctionsForActivity1().scrollRows(summByArticleScrollView);
                }

            }

        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, seconds);
        handleRowAnimationThread.start();
    }


    public void navigate(Fragment fragment) {

        NavController navController = NavHostFragment.findNavController(fragment);

        if (SplashScreenActivity.allData.getLayoutList().contains(4)) {
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(5)) {
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(6)) {
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(7)) {
            navController.navigate(R.id.summaryOfLastMonthFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0) {
            navController.navigate(R.id.branchSummaryFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
            navController.navigate(R.id.userReportForAllOusFragment2);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
            navController.navigate(R.id.userReportForEachOusFragment);
//            if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size() > 0) {
//                if ((SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(SecondActivity.vanIndex).userReportTableRowArrayList).size() > 0)
//                    navController.navigate(R.id.userReportForEachOusFragment);
//                else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
//                    if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size() > 0) {
//                        if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
//                            navController.navigate(R.id.peakHourReportFragment);
//                        } else if ()
//
//                    }
//                }
//            } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
//
//            }
        } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
            navController.navigate(R.id.peakHourReportFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
            navController.navigate(R.id.mapsFragment);
        } else {
            initFragment(SplashScreenActivity.allData.getDashBoardData(), 100);
        }

    }

    public void naviagteLeft(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(3)) {
            if (SplashScreenActivity.allData.getLayoutList().contains(1))
                navController.navigate(R.id.mapsFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(12))
                navController.navigate(R.id.peakHourReportFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(10))
                navController.navigate(R.id.userReportForEachOusFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(11))
                navController.navigate(R.id.peakHourReportForAllOusFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(9))
                navController.navigate(R.id.userReportForAllOusFragment2);
            else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                navController.navigate(R.id.branchSummaryFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(7))
                navController.navigate(R.id.summaryOfLastMonthFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(6))
                navController.navigate(R.id.summaryOfLastSixMonthsFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(5))
                navController.navigate(R.id.summarizedByArticleChildCategFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(4))
                navController.navigate(R.id.summarizedByArticleParentCategFragment);
            else
                initFragment(SplashScreenActivity.allData.getDashBoardData(), 100);


        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (handleRowAnimationThread != null)
            handleRowAnimationThread.interrupt();
    }

    public void totalLastRow(SummarizedByArticleTableRow row) {

        totalQuantity = totalQuantity + row.getQuantity();
        totalUnitAmount = totalUnitAmount + row.getAvgAmount();

    }

    public void drawLastArticleSummaryTotalRow() {
        if (getContext() != null) {
            View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.table_row_summary_by_article, null, false);

            TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowArticleProperty1);
            TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowArticleProperty2);
            TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowArticleProperty3);
            TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowArticleProperty4);
            TextView tableRowProperty5 = tableElements.findViewById(R.id.tableRowArticleProperty5);

            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(true);


            tableRowProperty1.setText("");
            tableRowProperty2.setText("Total Amount");
            tableRowProperty2.setTypeface(Typeface.DEFAULT_BOLD);
            tableRowProperty2.setTextSize(16f);


            tableRowProperty3.setText(numberFormat.format(totalQuantity));
            tableRowProperty3.setTypeface(Typeface.DEFAULT_BOLD);
            tableRowProperty3.setTextSize(16f);


            tableRowProperty4.setText(numberFormat.format(Math.round(totalUnitAmount * 100.0) / 100.0));
            tableRowProperty4.setTypeface(Typeface.DEFAULT_BOLD);
            tableRowProperty4.setTextSize(16f);


            tableRowProperty5.setText("");
            tableElements.setBackgroundColor(Color.parseColor("#3f4152"));

            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
            tableRowProperty3.startAnimation(animation);
            tableRowProperty4.startAnimation(animation);

            summarizedByArticleTableLayout.addView(tableElements);
            new UtilityFunctionsForActivity1().animate(summarizedByArticleTableLayout, tableElements);
        }

    }


    private void initFragment(DashBoardData dashBoardDataParam, int seconds) {

        DashBoardData dashBoardData = dashBoardDataParam;

        inflateTable(dashBoardData.getSummarizedByArticleData().getTableData(), seconds);

        int chartTypeIndex = SplashScreenActivity.allData.getLayoutList().indexOf(Constants.SUMMARY_OF_ARTICLE_INDEX);
        Log.i("TAG", chartTypeIndex + "");
        String chartType = "";
        if (chartTypeIndex < SplashScreenActivity.allData.getChartList().size()) {
            chartType = SplashScreenActivity.allData.getChartList().get(chartTypeIndex);
        } else {
            chartType = "";
        }
        Log.i("TAG", chartType + "");


        new UtilityFunctionsForActivity2().drawChart(getContext(), chartType, pCardSummByArticle,
                dashBoardData.getSummarizedByArticleData().pieChartData, dashBoardData.getSummarizedByArticleData().barChartData,
                dashBoardData.getSummarizedByArticleData().lineChartData, "Summarized by Article");

//        if (chartType.equals(Constants.DONUT_TYPE)) {
//            View pieElement = LayoutInflater.from(getContext()).inflate(R.layout.pie_chart_layout, null, false);
//            pCardSummByArticle.addView(pieElement);
//            new UtilityFunctionsForActivity2().drawDonutChart(dashBoardData.getSummarizedByArticleData().pieChartData, (PieChart) pieElement, "Summarized by Article");
//        } else if (chartType.equals(Constants.BAR_TYPE)) {
//            View barElement = LayoutInflater.from(getContext()).inflate(R.layout.bar_chart_layout, null, false);
//            pCardSummByArticle.addView(barElement);
//            new UtilityFunctionsForActivity2().drawBarChart(dashBoardData.getSummarizedByArticleData().barChartData, (BarChart) barElement, "Summarized by Article");
//        } else if (chartType.equals(Constants.LINE_TYPE)) {
//            View lineElement = LayoutInflater.from(getContext()).inflate(R.layout.line_chart_layout, null, false);
//            pCardSummByArticle.addView(lineElement);
//            new UtilityFunctionsForActivity2().drawSingleLineChart(dashBoardData.getSummarizedByArticleData().lineChartData, (LineChart) lineElement, "Summarized by Article");
//        } else {//default pie chart
//            View pieElement = LayoutInflater.from(getContext()).inflate(R.layout.pie_chart_layout, null, false);
//            pCardSummByArticle.addView(pieElement);
//            new UtilityFunctionsForActivity2().drawPieChart(dashBoardData.getSummarizedByArticleData().pieChartData, (PieChart) pieElement, "Summarized by Article");
//        }

    }

    public void backTraverse() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                Intent intent = new Intent(requireActivity(), SplashScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void centerKey() {
        summByarticlePaused = !summByarticlePaused;
//        SecondActivity.firstCenterKeyPause = summByarticlePaused;
        if (!summByarticlePaused) {
            SecondActivity.playAll();
            navigate(fragment);
        } else {
            SecondActivity.pauseAll();
        }
        keyPadControl(summByarticlePaused);

    }

    @Override
    public void leftKey() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        naviagteLeft(fragment);
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
            summarticleplayPause.setImageResource(R.drawable.ic_play_button__2_);
            summarticleplayPause.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireActivity(), R.color.playbacksForeground)));
            sumByArticleKeyPad.setVisibility(View.VISIBLE);
        } else {
            sumByArticleKeyPad.setVisibility(View.GONE);
        }
    }
}
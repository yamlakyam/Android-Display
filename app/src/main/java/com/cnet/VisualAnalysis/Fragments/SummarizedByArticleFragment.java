package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Data.SummarizedByArticleTableRow;
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

public class SummarizedByArticleFragment extends Fragment {

    BarChart barChartSumByArticle;
    LineChart lineChartSumByArticle;
    TableLayout summarizedByArticleTableLayout;
    Handler animationHandler;
    TextView scrollingArticleText;
    ScrollView summByArticleScrollView;
    ProgressBar articleSummaryProgressBar;
    ProgressBar mainProgressBar;
    ConstraintLayout mainConstraintLayout;
    ConstraintLayout constraintLayout;
    public static HandleRowAnimationThread handleRowAnimationThread;
    public static boolean isInflatingTable = false;

    double totalUnitAmount = 0;
    int totalQuantity = 0;

    Fragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("TAG", "onCreate: ");


        SecondActivity.interrupThreads(SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                SummaryOfLastMonthFragment.handleRowAnimationThread,
                BranchSummaryFragment.handleRowAnimationThread);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_summarized_by_article, container, false);
        fragment = this;

        barChartSumByArticle = view.findViewById(R.id.bChartSumByArticle);
        lineChartSumByArticle = view.findViewById(R.id.lchartsumByArticle);
        summarizedByArticleTableLayout = view.findViewById(R.id.summaryByArticleTableLayout);
        scrollingArticleText = view.findViewById(R.id.scrollingArticleText);
        scrollingArticleText.setSelected(true);
        summByArticleScrollView = view.findViewById(R.id.summByArticleScrollView);
        articleSummaryProgressBar = view.findViewById(R.id.articleSummaryProgressBar);
        constraintLayout = view.findViewById(R.id.constraintLayout);
        Log.i("article data", SplashScreenActivity.allData.getDashBoardData().getSummarizedByArticleData().tableData.toString());


        backTraverse();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("resume", "IS INFLATING  : " + isInflatingTable);
        if (SplashScreenActivity.allData.getDashBoardData() != null && !isInflatingTable) {
            Log.i("article data", SplashScreenActivity.allData.getDashBoardData().getSummarizedByArticleData().tableData.toString());
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
                    UtilityFunctionsForActivity1.scrollRows(summByArticleScrollView);
                } else if (index == tablesToDisplay.size() + 1 && !SecondActivity.summaryByArticlePause) {
//                    NavController navController = NavHostFragment.findNavController(fragment);
//                    navController.navigate(R.id.summarizedByArticleParentCategFragment);
//                    SecondActivity secondActivity = new SecondActivity();
//                    secondActivity.navigations(fragment);

                    if (getContext() != null) {
                        navigate(fragment);

                    }

                } else if (index < tablesToDisplay.size()) {
                    totalLastRow(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity2.drawSummaryByArticleTable(tablesToDisplay, getContext(), summarizedByArticleTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(summByArticleScrollView);
                }

            }

        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, seconds);
        handleRowAnimationThread.start();
    }


    public void navigate(Fragment fragment) {

        NavController navController = NavHostFragment.findNavController(fragment);

        Log.i("next", SplashScreenActivity.allData.getLayoutList().get(1).toString());
        Log.i("next", SplashScreenActivity.allData.getLayoutList().toString());

        if (SplashScreenActivity.allData.getLayoutList().size() >= 2) {
            if (SplashScreenActivity.allData.getLayoutList().get(1) == 4) {
                navController.navigate(R.id.summarizedByArticleParentCategFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().get(1) == 5) {
                navController.navigate(R.id.summarizedByArticleChildCategFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().get(1) == 6) {
                navController.navigate(R.id.summaryOfLastSixMonthsFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().get(1) == 7) {
                navController.navigate(R.id.summaryOfLastMonthFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().get(1) == 8) {
                navController.navigate(R.id.branchSummaryFragment);
            }
        }


//        SecondActivity secondActivity = new SecondActivity();
//
//
//        if (secondActivity.visibleFragments[1]) {
//            navController.navigate(R.id.summarizedByArticleParentCategFragment);
//        } else if (secondActivity.visibleFragments[2]) {
//            navController.navigate(R.id.summarizedByArticleChildCategFragment);
//        } else if (secondActivity.visibleFragments[3]) {
//            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
//        } else if (secondActivity.visibleFragments[4]) {
//            navController.navigate(R.id.summaryOfLastMonthFragment);
//        } else if ((secondActivity.visibleFragments[5])) {
//            navController.navigate(R.id.branchSummaryFragment);
//        }
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
            tableRowProperty2.setTextSize(25f);


            tableRowProperty3.setText(numberFormat.format(totalQuantity));
            tableRowProperty3.setTypeface(Typeface.DEFAULT_BOLD);
            tableRowProperty3.setTextSize(25f);


            tableRowProperty4.setText(numberFormat.format(Math.round(totalUnitAmount * 100.0) / 100.0));
            tableRowProperty4.setTypeface(Typeface.DEFAULT_BOLD);
            tableRowProperty4.setTextSize(25f);


            tableRowProperty5.setText("");
            tableElements.setBackgroundColor(Color.parseColor("#3f4152"));

            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
            tableRowProperty3.startAnimation(animation);
            tableRowProperty4.startAnimation(animation);

            summarizedByArticleTableLayout.addView(tableElements);
            UtilityFunctionsForActivity1.animate(summarizedByArticleTableLayout, tableElements);
        }

    }


    private void initFragment(DashBoardData dashBoardDataParam, int seconds) {

        isInflatingTable = true;
        DashBoardData dashBoardData = dashBoardDataParam;

        inflateTable(dashBoardData.getSummarizedByArticleData().getTableData(), seconds);
        UtilityFunctionsForActivity2.drawBarChart(dashBoardData.getSummarizedByArticleData().getBarChartData(), barChartSumByArticle, "Summarized by Article");
        UtilityFunctionsForActivity2.drawLineChart(dashBoardData.getSummarizedByArticleData().getLineChartData(), lineChartSumByArticle, "Summarized by Article");

    }

    public void backTraverse() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                Intent intent = new Intent(requireActivity(), StartingActivty.class);
                startActivity(intent);
            }
        });
    }
}
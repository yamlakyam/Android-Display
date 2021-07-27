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
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Data.SummarizedByParentArticleRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.StartingActivty;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.DashBoardDataParser;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import org.json.JSONArray;

import java.text.NumberFormat;
import java.util.ArrayList;

public class SummarizedByArticleParentCategFragment extends Fragment implements VolleyHttp.GetRequest {

    Handler animationHandler;

    TableLayout summarizedByParentArticleTableLayout;
    ScrollView scrollView;
    PieChart pieChart;
    BarChart barChart;
    Fragment fragment;
    FrameLayout summarizedByParentArticleFrameLayout;

    public static HandleRowAnimationThread handleRowAnimationThread;
    public static boolean isInflatingTable;

    double grandTotal = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SecondActivity.interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                SummaryOfLastMonthFragment.handleRowAnimationThread,
                BranchSummaryFragment.handleRowAnimationThread);

        if (SecondActivity.dashBoardData == null) {
            VolleyHttp http = new VolleyHttp(getContext());
            http.makeGetRequest(Constants.DashboardURL, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summarized_by_article_parent_categ, container, false);
        fragment = this;

        summarizedByParentArticleTableLayout = view.findViewById(R.id.summaryByChildArticleTableLayout);
        scrollView = view.findViewById(R.id.summarizedByChildArticleScrollView);
        pieChart = view.findViewById(R.id.pchartsumByArticleParent);
        barChart = view.findViewById(R.id.bChartSumByArticleParent);
        summarizedByParentArticleFrameLayout = view.findViewById(R.id.summarizedByParentArticleFrameLayout);


        backTraverse(fragment, R.id.summarizedByArticleFragment2);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (SecondActivity.dashBoardArray != null && !isInflatingTable) {
            summarizedByParentArticleFrameLayout.setVisibility(View.GONE);
            initFragment(SecondActivity.dashBoardData, 200);
        }
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummarizedByParentArticleRow> tablesToDisplay, int seconds) {

        grandTotal = 0;
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
                    UtilityFunctionsForActivity1.scrollRows(scrollView);

                } else if (index == tablesToDisplay.size() + 1 && !SecondActivity.summaryByParentArticlePause) {
//                    NavController navController = NavHostFragment.findNavController(fragment);
//                    navController.navigate(R.id.summarizedByArticleChildCategFragment);
//                    SecondActivity secondActivity=new SecondActivity();
//                    secondActivity.navigations(fragment);
                    navigate(fragment);

                } else if (index < tablesToDisplay.size()) {
                    totalLastRow(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity2.drawSummaryByParentArticleTable(tablesToDisplay, getContext(), summarizedByParentArticleTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(scrollView);
                }

            }

        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, seconds);
        handleRowAnimationThread.start();
        Log.i("parent Article", "is interrupted " + handleRowAnimationThread.isInterrupted() + "");
    }


    private void initFragment(DashBoardData dashBoardDataP, int seconds) {

        isInflatingTable = true;
        Log.i("success", fragment + "");

        DashBoardData dashBoardData = dashBoardDataP;

        if (dashBoardData != null) {
            if (dashBoardData.getSummarizedByParentArticleData() != null) {
                inflateTable(dashBoardData.getSummarizedByParentArticleData().getTableData(), seconds);
                Log.i("parent Article", "is interrupted " + handleRowAnimationThread.isInterrupted() + "");

                UtilityFunctionsForActivity2.drawBarChart(dashBoardData.getSummarizedByParentArticleData().getBarChartData(), barChart, "Summarized by Article parent category");
                UtilityFunctionsForActivity2.drawPieChart(dashBoardData.getSummarizedByParentArticleData().getPieChartData(), pieChart, "Summarized by Article parent category");
            }
        }
    }

    public void totalLastRow(SummarizedByParentArticleRow row) {

        grandTotal = grandTotal + row.getTotalAmount() + row.getTaxAmount() + row.getTotalServCharge();


    }

    public void drawLastArticleParentRow() {
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
        tableRowProperty2.setTextSize(25f);


        tableRowProperty3.setText(numberFormat.format(grandTotal));
        tableRowProperty3.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty3.setTextSize(25f);


        tableRowProperty4.setText("");

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        tableRowProperty3.startAnimation(animation);
        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));
        summarizedByParentArticleTableLayout.addView(tableElements);
        UtilityFunctionsForActivity1.animate(summarizedByParentArticleTableLayout, tableElements);

    }

    public void backTraverse(Fragment fragment, int id) {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getActivity(), StartingActivty.class);
                startActivity(intent);
            }
        });
    }

    public void navigate(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        SecondActivity secondActivity = new SecondActivity();
        if (secondActivity.visibleFragments[2]) {
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        } else if (secondActivity.visibleFragments[3]) {
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        } else if (secondActivity.visibleFragments[4]) {
            navController.navigate(R.id.summaryOfLastMonthFragment);
        } else if (secondActivity.visibleFragments[5]) {
            navController.navigate(R.id.branchSummaryFragment);
        } else if (secondActivity.visibleFragments[0]) {
            navController.navigate(R.id.summarizedByArticleFragment2);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (handleRowAnimationThread != null)
            handleRowAnimationThread.interrupt();
    }

    @Override
    public void onSuccess(JSONArray jsonArray) {

        Log.i("parent success", "onSuccess: from parent");

        SecondActivity.dashBoardArray = jsonArray;
        DashBoardDataParser dashBoardDataParser = new DashBoardDataParser(jsonArray);
        DashBoardData dashBoardData = dashBoardDataParser.parseDashBoardData();
        SecondActivity.dashBoardData = dashBoardData;
        summarizedByParentArticleFrameLayout.setVisibility(View.GONE);
        initFragment(dashBoardData, 200);
    }

    @Override
    public void onFailure(VolleyError error) {

    }
}
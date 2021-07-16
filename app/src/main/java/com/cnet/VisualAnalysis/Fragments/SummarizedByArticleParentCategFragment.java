package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
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
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Data.SummarizedByParentArticleRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import org.json.JSONArray;

import java.text.NumberFormat;
import java.util.ArrayList;

public class SummarizedByArticleParentCategFragment extends Fragment {

    Handler animationHandler;

    TableLayout summarizedByParentArticleTableLayout;
    ScrollView scrollView;
    PieChart pieChart;
    BarChart barChart;
    Fragment fragment;

    double grandTotal = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summarized_by_article_parent_categ, container, false);
        fragment = this;

        Log.i("DASHBOARD ARRAY", SecondActivity.dashBoardArray.toString());
        summarizedByParentArticleTableLayout = view.findViewById(R.id.summaryByChildArticleTableLayout);
        scrollView = view.findViewById(R.id.summarizedByChildArticleScrollView);
        pieChart = view.findViewById(R.id.pchartsumByArticleParent);
        barChart = view.findViewById(R.id.bChartSumByArticleParent);

        backTraverse(fragment, R.id.summarizedByArticleFragment2);

        if (SecondActivity.dashBoardArray != null) {
            initFragment();
        }

        return view;
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummarizedByParentArticleRow> tablesToDisplay) {


        summarizedByParentArticleTableLayout.removeAllViews();
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

                } else if (index == tablesToDisplay.size() + 1) {
                    NavController navController = NavHostFragment.findNavController(fragment);
                    navController.navigate(R.id.summarizedByArticleChildCategFragment);
                } else {
                    totalLastRow(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity2.drawSummaryByParentArticleTable(tablesToDisplay, getContext(), summarizedByParentArticleTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(scrollView);
                }

            }

        };

        HandleRowAnimationThread handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler);
        handleRowAnimationThread.start();
    }


    public void initFragment() {

        Log.i("success", fragment + "");

        DashBoardData dashBoardData = SecondActivity.dashBoardData;
        inflateTable(dashBoardData.getSummarizedByParentArticleData().getTableData());
        UtilityFunctionsForActivity2.drawBarChart(dashBoardData.getSummarizedByParentArticleData().getBarChartData(), barChart, "Summarized by Article parent category");
        UtilityFunctionsForActivity2.drawPieChart(dashBoardData.getSummarizedByParentArticleData().getPieChartData(), pieChart, "Summarized by Article parent category");

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

        tableRowProperty3.setText(numberFormat.format(grandTotal));
        tableRowProperty3.setTypeface(Typeface.DEFAULT_BOLD);

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
                NavController navController = NavHostFragment.findNavController(fragment);
                navController.navigate(id);
            }
        });
    }
}
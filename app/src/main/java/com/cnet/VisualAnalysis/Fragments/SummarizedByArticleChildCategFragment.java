package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.SummarizedByArticleTableRow;
import com.cnet.VisualAnalysis.Data.SummarizedByChildArticleData;
import com.cnet.VisualAnalysis.Data.SummarizedByChildArticleRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.NumberFormat;
import java.util.ArrayList;

public class SummarizedByArticleChildCategFragment extends Fragment  {

    TableLayout summaryByChildArticleTableLayout;
    ScrollView summarizedByChildArticleScrollView;
    Handler animationHandler;
    BarChart barChart;
    PieChart pieChart;
    Fragment fragment;

    double grandTotal=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summarized_by_article_child_categ, container, false);

        fragment = this;
        summaryByChildArticleTableLayout = view.findViewById(R.id.summaryByChildArticleTableLayout);
        summarizedByChildArticleScrollView = view.findViewById(R.id.summarizedByChildArticleScrollView);
        pieChart = view.findViewById(R.id.pchartsumByArticleChild);
        barChart = view.findViewById(R.id.bChartSumByArticleChild);
        if(SecondActivity.dashBoardArray!=null){
            initFragment(SecondActivity.dashBoardArray);
        }

        backTraverse(fragment, R.id.summarizedByArticleParentCategFragment);


        return view;
    }


    public void initFragment(JSONArray jsonArray) {
        try {
            Log.i("success", fragment + "");

            SummarizedByChildArticleData summarizedByChildArticleData = UtilityFunctionsForActivity2.summarizedByChildArticleParser(jsonArray);
            inflateTable(summarizedByChildArticleData.getTableData());
            UtilityFunctionsForActivity2.drawBarChart(summarizedByChildArticleData.getBarChartData(), barChart, "Summarized by Article Child Category");
            UtilityFunctionsForActivity2.drawPieChart(summarizedByChildArticleData.getPieChartData(), pieChart, "Summarized by Article Child Category");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummarizedByChildArticleRow> tablesToDisplay) {

        summaryByChildArticleTableLayout.removeAllViews();
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
                    drawLastArticleChildRow();
                } else if (index == tablesToDisplay.size()+1) {
                    NavController navController = NavHostFragment.findNavController(fragment);
                    navController.navigate(R.id.summaryOfLastSixMonthsFragment);
                } else {
                    totalLastRow(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity2.drawSummaryByChildArticleTable(tablesToDisplay, getContext(), summaryByChildArticleTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(summarizedByChildArticleScrollView);
                }

            }

        };

        HandleRowAnimationThread handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler);
        handleRowAnimationThread.start();
    }

    public void totalLastRow(SummarizedByChildArticleRow row) {

        grandTotal=grandTotal + row.getTotalAmount()+row.getTaxAmount()+row.getTotalServCharge();


    }

   public void  drawLastArticleChildRow(){
       View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.table_row_summary_by_parent_article, null, false);

       TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowParentArtProperty1);
       TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowParentArtProperty2);
       TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowParentArtProperty3);
       TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowParentArtProperty4);

       NumberFormat numberFormat = NumberFormat.getInstance();
       numberFormat.setGroupingUsed(true);


       tableRowProperty1.setText("");
       tableRowProperty2.setText("Total Amount");
       tableRowProperty3.setText(numberFormat.format(grandTotal));
       tableRowProperty4.setText("");


       summaryByChildArticleTableLayout.addView(tableElements);
       UtilityFunctionsForActivity1.animate(summaryByChildArticleTableLayout, tableElements);
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
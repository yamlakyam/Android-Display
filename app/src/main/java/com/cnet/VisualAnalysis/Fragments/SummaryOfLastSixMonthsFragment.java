package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.SummaryOfLast6MonthsData;
import com.cnet.VisualAnalysis.Data.SummaryOfLast6MonthsRow;
import com.cnet.VisualAnalysis.R;
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


public class SummaryOfLastSixMonthsFragment extends Fragment implements VolleyHttp.GetRequest {

//    public static final String URL = "http://192.168.1.248:8001/api/DashBoardData/GetDashBoardData";

    TableLayout summaryOfLast6MonthsTableLayout;
    Handler animationHandler;
    BarChart barChart;
    PieChart pieChart;
    ScrollView scrollView;

    double totalAmount = 0;

    public SummaryOfLastSixMonthsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VolleyHttp http = new VolleyHttp(getContext());
        http.makeGetRequest(Constants.DashboardURL, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary_of_last_six_months, container, false);
        summaryOfLast6MonthsTableLayout = view.findViewById(R.id.summaryOfLast6MonthsTableLayout);
        pieChart = view.findViewById(R.id.pchartsummaryOfLast6Months);
        barChart = view.findViewById(R.id.bChartSummaryOfLast6Months);
        scrollView = view.findViewById(R.id.summaryOfLast6MonsScrollView);

        return view;
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummaryOfLast6MonthsRow> tablesToDisplay) {

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
                } else {
                    totalLastRow(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity2.drawSummaryOfLAst6Months(tablesToDisplay, getContext(), summaryOfLast6MonthsTableLayout, index, totalAmount);
                    UtilityFunctionsForActivity1.scrollRows(scrollView);
                }

            }

        };

        HandleRowAnimationThread handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler);
        handleRowAnimationThread.start();
    }

    @Override
    public void onSuccess(JSONArray jsonArray) {
        try {
            SummaryOfLast6MonthsData summaryOfLast6MonthsData = UtilityFunctionsForActivity2.last6MonthsDataParser(jsonArray);
            inflateTable(summaryOfLast6MonthsData.getTableData());
            UtilityFunctionsForActivity2.drawBarChart(summaryOfLast6MonthsData.getBarChartData(), barChart, "Summarized by last 6 months");
            UtilityFunctionsForActivity2.drawPieChart(summaryOfLast6MonthsData.getPieChartData(), pieChart, "Summarized by last 6 months");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(VolleyError error) {

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
        tableRowProperty3.setText(numberFormat.format(Math.round(totalAmount * 100.0) / 100.0));
        tableRowProperty4.setText("");
        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        tableRowProperty3.startAnimation(animation);


        summaryOfLast6MonthsTableLayout.addView(tableElements);
        UtilityFunctionsForActivity1.animate(summaryOfLast6MonthsTableLayout, tableElements);
    }
}
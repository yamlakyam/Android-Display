package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.SummaryOfLast6MonthsData;
import com.cnet.VisualAnalysis.Data.SummaryOfLast6MonthsRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.Activity2UtilityFunctions;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class SummaryOfLastSixMonthsFragment extends Fragment implements VolleyHttp.GetRequest {

    public static final String URL = "http://192.168.1.248:8001/api/DashBoardData/GetDashBoardData";

    TableLayout summaryOfLast6MonthsTableLayout;
    Handler animationHandler;
    BarChart barChart;
    PieChart pieChart;

    public SummaryOfLastSixMonthsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VolleyHttp http = new VolleyHttp(getContext());
        http.makeGetRequest(URL, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary_of_last_six_months, container, false);
        summaryOfLast6MonthsTableLayout = view.findViewById(R.id.summaryOfLast6MonthsTableLayout);
        pieChart = view.findViewById(R.id.pchartsummaryOfLast6Months);
        barChart = view.findViewById(R.id.bChartSummaryOfLast6Months);

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

                Activity2UtilityFunctions.drawSummaryOfLAst6Months(tablesToDisplay, getContext(), summaryOfLast6MonthsTableLayout, index);
            }

        };

        HandleRowAnimationThread handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler);
        handleRowAnimationThread.start();
    }

    @Override
    public void onSuccess(JSONArray jsonArray) {
        try {
            SummaryOfLast6MonthsData summaryOfLast6MonthsData = Activity2UtilityFunctions.last6MonthsDataParser(jsonArray);
            inflateTable(summaryOfLast6MonthsData.getTableData());
            Activity2UtilityFunctions.drawBarChart(summaryOfLast6MonthsData.getBarChartData(), barChart);
            Activity2UtilityFunctions.drawPieChart(summaryOfLast6MonthsData.getPieChartData(), pieChart);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(VolleyError error) {

    }
}
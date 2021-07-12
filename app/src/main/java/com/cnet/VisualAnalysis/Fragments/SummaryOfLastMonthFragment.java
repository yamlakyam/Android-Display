package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.LineChartData;
import com.cnet.VisualAnalysis.Data.SummarizedByArticleTableRow;
import com.cnet.VisualAnalysis.Data.SummaryOfLast30DaysData;
import com.cnet.VisualAnalysis.Data.SummaryOfLast30DaysRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.Activity2UtilityFunctions;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

public class SummaryOfLastMonthFragment extends Fragment implements VolleyHttp.GetRequest {
    private final String URL = "http://192.168.1.248:8001/api/DashBoardData/GetDashBoardData";
    TableLayout summaryOfLast30DaysTableLayout;
    Handler animationHandler;
    LineChart lineChart;



    public SummaryOfLastMonthFragment() {
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
        View view=inflater.inflate(R.layout.fragment_summary_of_last_month, container, false);
        summaryOfLast30DaysTableLayout=view.findViewById(R.id.summaryOfLast30DaysTableLayout);
        lineChart=view.findViewById(R.id.last30daysLineChart);
        return view;
    }

    @Override
    public void onSuccess(JSONArray jsonArray) {
        try {
            SummaryOfLast30DaysData summaryOfLast30DaysData = Activity2UtilityFunctions.last30DaysDataParser(jsonArray);
            inflateTable(summaryOfLast30DaysData.tableData);
            drawLineChart(summaryOfLast30DaysData.lineChartData);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummaryOfLast30DaysRow> tablesToDisplay) {
        summaryOfLast30DaysTableLayout.removeAllViews();
        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }

                Activity2UtilityFunctions.drawSummaryOfLast30Days(tablesToDisplay, getContext(), summaryOfLast30DaysTableLayout, index);
            }

        };

        HandleRowAnimationThread handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler);
        handleRowAnimationThread.start();
    }

    public void drawLineChart(LineChartData lineChartData){
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        for (int i = 0; i < lineChartData.x.length; i++) {
            dataVals.add(new Entry(lineChartData.x[i], lineChartData.y[i]));
        }

        LineDataSet lineDataSet = new LineDataSet(dataVals, "active users");
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        lineChart.setData(lineData);

        lineChart.getAxisLeft().setDrawLabels(true);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        //lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisRight().setDrawAxisLine(false);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getXAxis().setDrawAxisLine(false);
        lineChart.getXAxis().setDrawGridLines(false);
//        lineChart.getXAxis().enableAxisLineDashedLine(10f,10f,0);
//        lineChart.getAxisRight().enableAxisLineDashedLine(10f,10f,0);
//        lineChart.getAxisLeft().enableAxisLineDashedLine(10f,10f,0);

        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setDrawGridBackground(false);
        lineChart.getAxisLeft().setDrawGridLines(false);


        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawValues(false);
        lineDataSet.setColors(Color.parseColor("#5b79e7"));
        lineDataSet.setDrawCircles(false);
//        lineDataSet.enableDashedLine(10f,10f,0);


//        ArrayList<String> xAxisVals = new ArrayList<>(Arrays.asList("Apr 6", "Apr 7", "Apr 8", "Apr 9", "Apr 10", "Apr 11", "Apr 12"));
//        ArrayList<String> yAxisVals = new ArrayList<>(Arrays.asList("0", "10k", "20k", "30k", "40k", "50k", "60k"));
//        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisVals));
//        //lineChart.getAxisLeft().setValueFormatter(new IndexAxisValueFormatter(yAxisVals));
//        lineChart.getAxisLeft().setLabelCount(yAxisVals.size());
        lineChart.setExtraBottomOffset(15f);
        lineChart.setExtraTopOffset(15f);
        lineChart.animateX(500, Easing.EaseInCubic);
    }

    @Override
    public void onFailure(VolleyError error) {

    }
}
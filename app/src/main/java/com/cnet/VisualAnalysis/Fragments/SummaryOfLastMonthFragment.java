package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.LineChartData;
import com.cnet.VisualAnalysis.Data.SummaryOfLast30DaysData;
import com.cnet.VisualAnalysis.Data.SummaryOfLast30DaysRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.NumberFormat;
import java.util.ArrayList;

public class SummaryOfLastMonthFragment extends Fragment implements VolleyHttp.GetRequest {
    TableLayout summaryOfLast30DaysTableLayout;
    Handler animationHandler;
    LineChart lineChart;
    BarChart barChart;
    ScrollView scrollView;

    public double totalAmount = 0;

    public SummaryOfLastMonthFragment() {
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
        View view = inflater.inflate(R.layout.fragment_summary_of_last_month, container, false);
        summaryOfLast30DaysTableLayout = view.findViewById(R.id.summaryOfLast30DaysTableLayout);
        lineChart = view.findViewById(R.id.last30daysLineChart);
        scrollView = view.findViewById(R.id.summaryOfLastMonthScrollView);
        barChart = view.findViewById(R.id.last30daysBarChart);
        return view;
    }

    @Override
    public void onSuccess(JSONArray jsonArray) {
        try {
            SummaryOfLast30DaysData summaryOfLast30DaysData = UtilityFunctionsForActivity2.last30DaysDataParser(jsonArray);
            inflateTable(summaryOfLast30DaysData.tableData);
            drawLineChart(summaryOfLast30DaysData.lineChartData);
            UtilityFunctionsForActivity2.drawBarChart(summaryOfLast30DaysData.barChartData, barChart,"Summarized by last 30 days");
        } catch (JSONException e) {
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

//                totalLastRow(tablesToDisplay.get(index));

                if (index == tablesToDisplay.size()) {
                    drawLastTotalRow();
                    UtilityFunctionsForActivity1.scrollRows(scrollView);
                } else {
                    totalLastRow(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity2.drawSummaryOfLast30Days(tablesToDisplay, getContext(), summaryOfLast30DaysTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(scrollView);
                }

            }

        };

        HandleRowAnimationThread handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler);
        handleRowAnimationThread.start();
    }

    public void totalLastRow(SummaryOfLast30DaysRow row) {

        totalAmount = totalAmount + row.getAmount();

    }

    public void drawLastTotalRow() {
        View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.table_row_summary_by_parent_article, null, false);

        TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowParentArtProperty1);
        TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowParentArtProperty2);
        TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowParentArtProperty3);
        TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowParentArtProperty4);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);


        tableRowProperty1.setText("");
        tableRowProperty2.setText("");
        tableRowProperty3.setText(numberFormat.format(totalAmount));
        tableRowProperty4.setText("");


        summaryOfLast30DaysTableLayout.addView(tableElements);
        UtilityFunctionsForActivity1.animate(summaryOfLast30DaysTableLayout, tableElements);
    }

    public void drawLineChart(LineChartData lineChartData) {
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
        lineChart.getAxisRight().setDrawAxisLine(false);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getXAxis().setDrawAxisLine(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setDrawGridBackground(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawValues(false);
        lineDataSet.setColors(Color.parseColor("#5b79e7"));
        lineDataSet.setDrawCircles(false);

//        ArrayList<String> xAxisVals = new ArrayList<>(Arrays.asList("Apr 6", "Apr 7", "Apr 8", "Apr 9", "Apr 10", "Apr 11", "Apr 12"));
//        ArrayList<String> yAxisVals = new ArrayList<>(Arrays.asList("0", "10k", "20k", "30k", "40k", "50k", "60k"));
//        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisVals));
//        //lineChart.getAxisLeft().setValueFormatter(new IndexAxisValueFormatter(yAxisVals));
//        lineChart.getAxisLeft().setLabelCount(yAxisVals.size());
        lineChart.setExtraBottomOffset(15f);
        lineChart.setExtraTopOffset(15f);
        lineChart.animateX(500, Easing.EaseInCubic);
        lineChart.getXAxis().setTextColor(Color.parseColor("#f6f8fb"));
        lineChart.getAxisLeft().setTextColor(Color.parseColor("#f6f8fb"));
        lineChart.getLegend().setTextColor(Color.parseColor("#f6f8fb"));
    }

    @Override
    public void onFailure(VolleyError error) {

    }
}
package com.cnet.VisualAnalysis.Utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.cnet.VisualAnalysis.Data.BarChartData;
import com.cnet.VisualAnalysis.Data.LineChartData;
import com.cnet.VisualAnalysis.Data.PieChartData;
import com.cnet.VisualAnalysis.Data.SummarizedByArticleData;
import com.cnet.VisualAnalysis.Data.SummarizedByArticleTableRow;
import com.cnet.VisualAnalysis.Data.SummarizedByChildArticleData;
import com.cnet.VisualAnalysis.Data.SummarizedByChildArticleRow;
import com.cnet.VisualAnalysis.Data.SummarizedByParentArticleData;
import com.cnet.VisualAnalysis.Data.SummarizedByParentArticleRow;
import com.cnet.VisualAnalysis.Data.SummaryOfLast30DaysData;
import com.cnet.VisualAnalysis.Data.SummaryOfLast30DaysRow;
import com.cnet.VisualAnalysis.Data.SummaryOfLast6MonthsData;
import com.cnet.VisualAnalysis.Data.SummaryOfLast6MonthsRow;
import com.cnet.VisualAnalysis.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;

public class UtilityFunctionsForActivity2 {

    public static void drawPieChart(PieChartData pieChartData, PieChart piechart, String label) {
        piechart.setDrawSliceText(false);

        piechart.animateX(3000, Easing.EaseInOutCirc);
        piechart.setDrawHoleEnabled(false);
        piechart.getDescription().setTextColor(Color.parseColor("#f6f8fb"));
        piechart.getDescription().setText(label);
        piechart.getLegend().setTextColor(Color.parseColor("#f6f8fb"));

        ArrayList<PieEntry> pieChartEntries = new ArrayList<>();

        if (pieChartData != null) {
            for (int i = 0; i < pieChartData.x.length; i++) {
                pieChartEntries.add(new PieEntry(pieChartData.x[i], pieChartData.y[i]));
            }
        }

        PieDataSet pieDataSet = new PieDataSet(pieChartEntries, "");
        pieDataSet.setColors(Color.parseColor("#5472e8"), Color.parseColor("#26adb9"),
                Color.parseColor("#195d57"), Color.parseColor("#acefe8"),
                Color.parseColor("#08bed6"), Color.parseColor("#1a76ca")
        );
        pieDataSet.setDrawValues(false);

        PieData pieData = new PieData(pieDataSet);
        piechart.setData(pieData);

    }

    public static void drawBarChart(BarChartData barChartData, BarChart barChart, String label) {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.getXAxis().setCenterAxisLabels(true);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawAxisLine(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.setPinchZoom(false);
        barChart.getXAxis().setGranularity(1f);

        ArrayList<BarEntry> barChartEntries = new ArrayList<BarEntry>();
        for (int i = 0; i < barChartData.x.length; i++) {
            barChartEntries.add(new BarEntry(barChartData.x[i], barChartData.y[i]));
        }

        BarDataSet barDataSet = new BarDataSet(barChartEntries, label);
        barDataSet.setForm(Legend.LegendForm.CIRCLE);

        BarData barData = new BarData(barDataSet);

        barChart.setData(barData);
        barChart.setVisibleXRangeMaximum(barChartEntries.size());
        barChart.setExtraBottomOffset(15f);

        barDataSet.setColor(Color.parseColor("#27adb9"));
        barDataSet.setDrawValues(false);

        barChart.animateXY(3000, 3000);
        barChart.getAxisRight().setDrawLabels(false);

        barChart.getAxisLeft().setTextColor(Color.parseColor("#f6f8fb"));
        barChart.getXAxis().setTextColor(Color.parseColor("#f6f8fb"));
        barChart.getLegend().setTextColor(Color.parseColor("#f6f8fb"));
        barChart.getDescription().setTextColor(Color.parseColor("#f6f8fb"));

    }


    public static void drawSummaryByArticleTable(
            ArrayList<SummarizedByArticleTableRow> summarizedByArticleDataRows,
            Context context,
            TableLayout summarizedByArticleTableLayout,
            int index) {

        SummarizedByArticleTableRow row = summarizedByArticleDataRows.get(index);
        View tableElements = LayoutInflater.from(context).inflate(R.layout.table_row_summary_by_article, null, false);

        TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowArticleProperty1);
        TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowArticleProperty2);
        TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowArticleProperty3);
        TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowArticleProperty4);
        TextView tableRowProperty5 = tableElements.findViewById(R.id.tableRowArticleProperty5);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        double grandTotal= row.getTotalAmount()+row.getTotalServCharge()+row.getTaxAmount();


        tableRowProperty1.setText(String.valueOf(index + 1));
        tableRowProperty2.setText(row.getArticleName());
        tableRowProperty3.setText(String.valueOf(row.getQuantity()));
        tableRowProperty4.setText(numberFormat.format(Math.round(row.getAvgAmount() * 100.0) / 100.0));
        tableRowProperty5.setText(numberFormat.format(Math.round(grandTotal * 100.0) / 100.0));

//        Animation animation = AnimationUtils.loadAnimation(context, R.anim.blink);
//        tableRowProperty5.startAnimation(animation);

//        AnimatorSet anim = (AnimatorSet ) AnimatorInflater.loadAnimator(context, R.animator.blink_2);
//        anim.setTarget(tableRowProperty5);
//        anim.start();


        summarizedByArticleTableLayout.addView(tableElements);
        animate(summarizedByArticleTableLayout, tableElements);

    }

    public static void drawSummaryByParentArticleTable(ArrayList<SummarizedByParentArticleRow> summarizedByParentArticleRows,
                                                       Context context,
                                                       TableLayout summarizedByParentArticleTableLayout, int index) {

        SummarizedByParentArticleRow row = summarizedByParentArticleRows.get(index);
        View tableElements = LayoutInflater.from(context).inflate(R.layout.table_row_summary_by_parent_article, null, false);

        TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowParentArtProperty1);
        TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowParentArtProperty2);
        TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowParentArtProperty3);
        TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowParentArtProperty4);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        double grandTotal= row.getTotalAmount()+row.getTotalServCharge()+row.getTaxAmount();

        tableRowProperty1.setText(String.valueOf(index + 1));
        tableRowProperty2.setText(row.getCategoryType());
        tableRowProperty3.setText(numberFormat.format(Math.round(grandTotal * 100.0) / 100.0));
        tableRowProperty4.setText(numberFormat.format(Math.round(row.getTotalAmount() * 100.0) / 100.0));


        summarizedByParentArticleTableLayout.addView(tableElements);
        animate(summarizedByParentArticleTableLayout, tableElements);

    }

    public static void drawSummaryByChildArticleTable(ArrayList<SummarizedByChildArticleRow> summarizedByChildArticleRows,
                                                      Context context,
                                                      TableLayout summarizedByChildArticleTableLayout, int index) {

        SummarizedByChildArticleRow row = summarizedByChildArticleRows.get(index);
        View tableElements = LayoutInflater.from(context).inflate(R.layout.table_row_summary_by_parent_article, null, false);

        TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowParentArtProperty1);
        TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowParentArtProperty2);
        TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowParentArtProperty3);
        TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowParentArtProperty4);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        double grandTotal= row.getTotalAmount()+row.getTotalServCharge()+row.getTaxAmount();


        tableRowProperty1.setText(String.valueOf(index + 1));
        tableRowProperty2.setText(row.getCategoryType());
        tableRowProperty3.setText(numberFormat.format(Math.round(grandTotal * 100.0) / 100.0));
        tableRowProperty4.setText(numberFormat.format(Math.round(row.getTotalAmount() * 100.0) / 100.0));


        summarizedByChildArticleTableLayout.addView(tableElements);
        animate(summarizedByChildArticleTableLayout, tableElements);

    }


    public static void drawSummaryOfLast30Days(ArrayList<SummaryOfLast30DaysRow> summaryOfLast30DaysRows,
                                               Context context,
                                               TableLayout summarizedByLast30DaysTableLayout, int index) {


        SummaryOfLast30DaysRow row = summaryOfLast30DaysRows.get(index);
        View tableElements = LayoutInflater.from(context).inflate(R.layout.table_row_summary_by_parent_article, null, false);

        TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowParentArtProperty1);
        TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowParentArtProperty2);
        TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowParentArtProperty3);
        TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowParentArtProperty4);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);


        tableRowProperty1.setText(String.valueOf(index + 1));
        tableRowProperty2.setText(row.getName());
        tableRowProperty3.setText(numberFormat.format(Math.round(row.getAmount() * 100.0) / 100.0));
        tableRowProperty4.setText(UtilityFunctionsForActivity1.formatDateTimeToString(row.getDateTime()));


        summarizedByLast30DaysTableLayout.addView(tableElements);
        animate(summarizedByLast30DaysTableLayout, tableElements);

    }


    public static void drawSummaryOfLAst6Months(ArrayList<SummaryOfLast6MonthsRow> summaryOfLast6MonthsRows,
                                                Context context,
                                                TableLayout summarizedByLast6MonthsTableLayout, int index, double total) {

        SummaryOfLast6MonthsRow row = summaryOfLast6MonthsRows.get(index);
        View tableElements = LayoutInflater.from(context).inflate(R.layout.table_row_summary_by_parent_article, null, false);

        TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowParentArtProperty1);
        TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowParentArtProperty2);
        TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowParentArtProperty3);
        TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowParentArtProperty4);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        tableRowProperty1.setText(String.valueOf(index + 1));
        tableRowProperty2.setText(row.getName());
        tableRowProperty3.setText(numberFormat.format(Math.round(row.getAmount() * 100.0) / 100.0));
        tableRowProperty4.setText(UtilityFunctionsForActivity1.formatDateTimeToString(row.getDateTime()));


        summarizedByLast6MonthsTableLayout.addView(tableElements);
        animate(summarizedByLast6MonthsTableLayout, tableElements);

    }

    public static void animate(View container, View child) {
        Animation animation = AnimationUtils.loadAnimation(container.getContext(), R.anim.slide_out_bottom);
        child.startAnimation(animation);
    }

    public static void scrollRows(ScrollView scrollView) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

    }

    public static SummaryOfLast30DaysData last30DaysDataParser(JSONArray jsonArray) throws JSONException {
        JSONObject allData = jsonArray.getJSONObject(0);
        JSONArray lastDaysOfMonthSale = allData.getJSONArray("lastDaysOfMonthSale");

        ArrayList<SummaryOfLast30DaysRow> tableData = new ArrayList<>();
        float[] xValues = new float[lastDaysOfMonthSale.length()];
        float[] yValues = new float[lastDaysOfMonthSale.length()];
        String[] legends = new String[lastDaysOfMonthSale.length()];

        for (int i = 0; i < lastDaysOfMonthSale.length(); i++) {
            JSONObject summaryAtIndex = lastDaysOfMonthSale.getJSONObject(i);

            SummaryOfLast30DaysRow summaryOfLast30DaysRow = new SummaryOfLast30DaysRow(
                    summaryAtIndex.getString("name"),
                    summaryAtIndex.getDouble("amount"),
                    summaryAtIndex.getString("dateTime")
            );

            tableData.add(summaryOfLast30DaysRow);
            xValues[i] = i + 1;
            yValues[i] = (float) summaryAtIndex.getDouble("amount");
            legends[i] = summaryAtIndex.getString("name");

        }


        LineChartData lineChartData = new LineChartData(xValues, yValues);
        BarChartData barChartData = new BarChartData(xValues, yValues, legends);
        SummaryOfLast30DaysData summaryOfLast30DaysData = new SummaryOfLast30DaysData(lineChartData, barChartData,tableData);

        return summaryOfLast30DaysData;
    }

    public static SummaryOfLast6MonthsData last6MonthsDataParser(JSONArray jsonArray) throws JSONException {
        JSONObject allData = jsonArray.getJSONObject(0);
        JSONArray last6MonthsSale = allData.getJSONArray("lastMonthsOfYearSale");

        ArrayList<SummaryOfLast6MonthsRow> tableData = new ArrayList<>();
        float[] xValues = new float[last6MonthsSale.length()];
        float[] yValues = new float[last6MonthsSale.length()];
        String[] legends = new String[last6MonthsSale.length()];

        for (int i = 0; i < last6MonthsSale.length(); i++) {
            JSONObject last6MonsSummaryAtInedx = last6MonthsSale.getJSONObject(i);

            SummaryOfLast6MonthsRow summaryOfLast6MonthsRow = new SummaryOfLast6MonthsRow(
                    last6MonsSummaryAtInedx.getString("name"),
                    last6MonsSummaryAtInedx.getDouble("amount"),
                    last6MonsSummaryAtInedx.getString("dateTime")
            );
            tableData.add(summaryOfLast6MonthsRow);
            xValues[i] = i+1;
            yValues[i] = (float) last6MonsSummaryAtInedx.getDouble("amount");
            legends[i] = last6MonsSummaryAtInedx.getString("name");


        }

        PieChartData pieChartData = new PieChartData(xValues, legends);
        BarChartData barChartData = new BarChartData(xValues, yValues, legends);


        SummaryOfLast6MonthsData summaryOfLast6MonthsData = new SummaryOfLast6MonthsData(pieChartData, tableData, barChartData);

        return summaryOfLast6MonthsData;
    }

    public static SummarizedByParentArticleData summarizedByParentArticleParser(JSONArray jsonArray) throws JSONException {
        JSONObject allData = jsonArray.getJSONObject(0);
        JSONArray summaryOfParentArticle = allData.getJSONArray("summarizedByArticleParentCategory");

        ArrayList<SummarizedByParentArticleRow> tableData = new ArrayList<>();
        float[] xValues = new float[summaryOfParentArticle.length()];
        float[] yValues = new float[summaryOfParentArticle.length()];
        String[] legends = new String[summaryOfParentArticle.length()];

        for (int i = 0; i < summaryOfParentArticle.length(); i++) {
            JSONObject summaryOfParentarticleAtInedx = summaryOfParentArticle.getJSONObject(i);

            SummarizedByParentArticleRow summarizedByParentArticleRow = new SummarizedByParentArticleRow(
                    summaryOfParentarticleAtInedx.getString("categoryType"),
                    summaryOfParentarticleAtInedx.getDouble("quantity"),
                    summaryOfParentarticleAtInedx.getDouble("avgAmount"),
                    summaryOfParentarticleAtInedx.getDouble("totalAmount"),
                    summaryOfParentarticleAtInedx.getDouble("totalServCharge"),
                    summaryOfParentarticleAtInedx.getDouble("totalDiscount"),
                    summaryOfParentarticleAtInedx.getDouble("taxAmount")
            );
            tableData.add(summarizedByParentArticleRow);
            xValues[i] = i;
            yValues[i] = (float) summaryOfParentarticleAtInedx.getDouble("totalAmount");
            legends[i] = summaryOfParentarticleAtInedx.getString("categoryType");

        }

        PieChartData pieChartData = new PieChartData(xValues, legends);
        BarChartData barChartData = new BarChartData(xValues, yValues, legends);


        SummarizedByParentArticleData summarizedByParentArticleData = new SummarizedByParentArticleData(tableData, barChartData, pieChartData);

        return summarizedByParentArticleData;
    }

    public static SummarizedByChildArticleData summarizedByChildArticleParser(JSONArray jsonArray) throws JSONException {
        JSONObject allData = jsonArray.getJSONObject(0);
        JSONArray summaryOfChildArticle = allData.getJSONArray("summarizedByArticleChildCategory");

        ArrayList<SummarizedByChildArticleRow> tableData = new ArrayList<>();
        float[] xValues = new float[summaryOfChildArticle.length()];
        float[] yValues = new float[summaryOfChildArticle.length()];
        String[] legends = new String[summaryOfChildArticle.length()];

        for (int i = 0; i < summaryOfChildArticle.length(); i++) {
            JSONObject summaryOfChildArticleAtInedx = summaryOfChildArticle.getJSONObject(i);

            SummarizedByChildArticleRow summarizedByChildArticleRow = new SummarizedByChildArticleRow(
                    summaryOfChildArticleAtInedx.getString("categoryType"),
                    summaryOfChildArticleAtInedx.getDouble("quantity"),
                    summaryOfChildArticleAtInedx.getDouble("avgAmount"),
                    summaryOfChildArticleAtInedx.getDouble("totalAmount"),
                    summaryOfChildArticleAtInedx.getDouble("totalServCharge"),
                    summaryOfChildArticleAtInedx.getDouble("totalDiscount"),
                    summaryOfChildArticleAtInedx.getDouble("taxAmount")
            );
            tableData.add(summarizedByChildArticleRow);
            xValues[i] = i;
            yValues[i] = (float) summaryOfChildArticleAtInedx.getDouble("totalAmount");
            legends[i] = summaryOfChildArticleAtInedx.getString("categoryType");

        }

        PieChartData pieChartData = new PieChartData(xValues, legends);
        BarChartData barChartData = new BarChartData(xValues, yValues, legends);


        SummarizedByChildArticleData summarizedByChildArticleData = new SummarizedByChildArticleData(tableData, barChartData, pieChartData);

        return summarizedByChildArticleData;
    }

    public static SummarizedByArticleData summarizedByArticleParser(JSONArray jsonArray) throws JSONException{
        JSONObject allData = jsonArray.getJSONObject(0);
        JSONArray summaryOfArticle = allData.getJSONArray("summarizedByArticleList");

        ArrayList<SummarizedByArticleTableRow> tableData = new ArrayList<>();
        float[] xValues = new float[summaryOfArticle.length()];
        float[] yValues = new float[summaryOfArticle.length()];
        String[] legends = new String[summaryOfArticle.length()];

        for (int i = 0; i < summaryOfArticle.length(); i++) {
            JSONObject summaryOfArticleAtInedx = summaryOfArticle.getJSONObject(i);

            SummarizedByArticleTableRow summarizedByArticleTableRow = new SummarizedByArticleTableRow(
                    summaryOfArticleAtInedx.getString("articleCode"),
                    summaryOfArticleAtInedx.getString("articleName"),
                    (int) summaryOfArticleAtInedx.getDouble("quantity"),
                    summaryOfArticleAtInedx.getDouble("avgAmount"),
                    summaryOfArticleAtInedx.getDouble("totalAmount"),
                    summaryOfArticleAtInedx.getDouble("totalServCharge"),
                    summaryOfArticleAtInedx.getDouble("totalDiscount"),
                    summaryOfArticleAtInedx.getDouble("taxAmount")
            );
            double grandTotal =summaryOfArticleAtInedx.getDouble("totalAmount")+
                    summaryOfArticleAtInedx.getDouble("totalServCharge")+
                    summaryOfArticleAtInedx.getDouble("taxAmount");

            tableData.add(summarizedByArticleTableRow);
            xValues[i] = i;
            yValues[i] = (float) grandTotal;
            legends[i] = summaryOfArticleAtInedx.getString("articleName");

        }

        PieChartData pieChartData = new PieChartData(xValues, legends);
        BarChartData barChartData = new BarChartData(xValues, yValues, legends);


        SummarizedByArticleData summarizedByArticleData = new SummarizedByArticleData(tableData, barChartData, pieChartData);
        return summarizedByArticleData;
    }
}

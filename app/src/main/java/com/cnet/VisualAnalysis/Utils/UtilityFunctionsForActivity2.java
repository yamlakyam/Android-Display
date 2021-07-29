package com.cnet.VisualAnalysis.Utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;
import android.widget.TextView;

import com.cnet.VisualAnalysis.Data.BarChartData;
import com.cnet.VisualAnalysis.Data.BranchSummaryTableRow;
import com.cnet.VisualAnalysis.Data.LineChartData;
import com.cnet.VisualAnalysis.Data.PieChartData;
import com.cnet.VisualAnalysis.Data.SummarizedByArticleTableRow;
import com.cnet.VisualAnalysis.Data.SummarizedByChildArticleRow;
import com.cnet.VisualAnalysis.Data.SummarizedByParentArticleRow;
import com.cnet.VisualAnalysis.Data.SummaryOfLast30DaysRow;
import com.cnet.VisualAnalysis.Data.SummaryOfLast6MonthsRow;
import com.cnet.VisualAnalysis.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.NumberFormat;
import java.util.ArrayList;

public class UtilityFunctionsForActivity2 {

    public static void drawPieChart(PieChartData pieChartData, PieChart piechart, String label) {
        piechart.setDrawSliceText(true);// to draw the labels


        piechart.animateX(3000, Easing.EaseInOutCirc);
        piechart.setDrawHoleEnabled(false);
        piechart.getDescription().setTextColor(Color.parseColor("#f6f8fb"));
        piechart.getDescription().setText(label);


//        piechart.getLegend().setTextColor(Color.parseColor("#f6f8fb"));
//        piechart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
//        piechart.getLegend().setFormLineDashEffect(new DashPathEffect(new float[] {10f, 5f},0f));
//        piechart.getLegend().setTextSize(3f);
        piechart.getLegend().setEnabled(false);


        ArrayList<PieEntry> pieChartEntries = new ArrayList<>();

        if (pieChartData != null) {
            for (int i = 0; i < pieChartData.x.length; i++) {
                pieChartEntries.add(new PieEntry(pieChartData.x[i], pieChartData.y[i]));

            }
        }
        PieDataSet pieDataSet = new PieDataSet(pieChartEntries, "");
        pieDataSet.setColors(Color.parseColor("#5472e8"), Color.parseColor("#26adb9"),
                Color.parseColor("#195d57"), Color.parseColor("#acefe8"),
                Color.parseColor("#08bed6"), Color.parseColor("#1a76ca"),
                Color.parseColor("#1e81b0"), Color.parseColor("#063970"),
                Color.parseColor("#21130d"), Color.parseColor("#873e23")
        );


        pieDataSet.setDrawValues(false);
        pieDataSet.setDrawIcons(true);

        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLinePart1OffsetPercentage(10); //starting of the line from center of the chart offset
        pieDataSet.setValueLinePart1Length(0.7f);
        pieDataSet.setValueLinePart2Length(0.5f);
//        pieDataSet.setValueLineColor(Color.parseColor("#FFFFFF"));
        pieDataSet.setUsingSliceColorAsValueLineColor(true);
        pieDataSet.setValueTextColor(Color.parseColor("#FFFFFF"));


//        pieDataSet.setSliceSpace(2);

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

//        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(barChartData.legends));
//        barChart.getXAxis().setLabelRotationAngle(-45);
//        barChart.getXAxis().setLabelCount(barChartData.legends.length);


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

    public static void drawLineChart(LineChartData lineChartData, LineChart lineChart, String label) {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        for (int i = 0; i < lineChartData.x.length; i++) {
            dataVals.add(new Entry(lineChartData.x[i], lineChartData.y[i]));
        }

        LineDataSet lineDataSet = new LineDataSet(dataVals, label);
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


    public static void drawSummaryByArticleTable(
            ArrayList<SummarizedByArticleTableRow> summarizedByArticleDataRows,
            Context context,
            TableLayout summarizedByArticleTableLayout,
            int index) {

        SummarizedByArticleTableRow row = summarizedByArticleDataRows.get(index);

        if (context != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View tableElements = inflater.inflate(R.layout.table_row_summary_by_article, null, false);

            TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowArticleProperty1);
            TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowArticleProperty2);
            TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowArticleProperty3);
            TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowArticleProperty4);
            TextView tableRowProperty5 = tableElements.findViewById(R.id.tableRowArticleProperty5);

            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(true);

            double grandTotal = row.getTotalAmount() + row.getTotalServCharge() + row.getTaxAmount();

            String formattedArticleName;


            if (!row.getArticleName().equals("null")) {
                if (row.getArticleName().length() > 28) {
                    formattedArticleName = row.getArticleName().substring(0, 25) + "...";
                } else {
                    formattedArticleName = row.getArticleName();
                }
            } else {
                formattedArticleName = "- - - - - - - - - - - - - ";
            }


            tableRowProperty1.setText(String.valueOf(index + 1));
            tableRowProperty2.setText(formattedArticleName);
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

    }

    public static void drawSummaryByParentArticleTable(ArrayList<SummarizedByParentArticleRow> summarizedByParentArticleRows,
                                                       Context context,
                                                       TableLayout summarizedByParentArticleTableLayout, int index) {

        SummarizedByParentArticleRow row = summarizedByParentArticleRows.get(index);

        if (context != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View tableElements = inflater.inflate(R.layout.table_row_summary_by_parent_article, null, false);

            TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowParentArtProperty1);
            TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowParentArtProperty2);
            TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowParentArtProperty3);
            TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowParentArtProperty4);

            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(true);

            double grandTotal = row.getTotalAmount() + row.getTotalServCharge() + row.getTaxAmount();

            String formattedCategoryType;
            if (row.getCategoryType().equals("null")) {
                formattedCategoryType = "- - - - - - - - - - - - - ";
            } else {
                formattedCategoryType = row.getCategoryType();
            }


            double grandTotalForAll = 0;
            for (int i = 0; i < summarizedByParentArticleRows.size(); i++) {
                double grandTotalForI = summarizedByParentArticleRows.get(i).getTotalAmount() +
                        summarizedByParentArticleRows.get(i).getTotalServCharge() +
                        summarizedByParentArticleRows.get(i).getTaxAmount();
                grandTotalForAll = grandTotalForAll + grandTotalForI;
            }
            double percentage = (grandTotal / grandTotalForAll) * 100;

            tableRowProperty1.setText(String.valueOf(index + 1));
            tableRowProperty2.setText(formattedCategoryType);
            tableRowProperty3.setText(numberFormat.format(Math.round(grandTotal * 100.0) / 100.0));
            tableRowProperty4.setText(numberFormat.format(Math.round(percentage * 1000.0) / 1000.0) + "%");


            summarizedByParentArticleTableLayout.addView(tableElements);
            animate(summarizedByParentArticleTableLayout, tableElements);
        }


    }

    public static void drawSummaryByChildArticleTable(ArrayList<SummarizedByChildArticleRow> summarizedByChildArticleRows,
                                                      Context context,
                                                      TableLayout summarizedByChildArticleTableLayout, int index) {

        SummarizedByChildArticleRow row = summarizedByChildArticleRows.get(index);

        if (context != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View tableElements = inflater.inflate(R.layout.table_row_summary_by_parent_article, null, false);

            TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowParentArtProperty1);
            TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowParentArtProperty2);
            TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowParentArtProperty3);
            TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowParentArtProperty4);

            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(true);

            double grandTotal = row.getTotalAmount() + row.getTotalServCharge() + row.getTaxAmount();


            double grandTotalForAll = 0;
            for (int i = 0; i < summarizedByChildArticleRows.size(); i++) {
                double grandTotalForI = summarizedByChildArticleRows.get(i).getTotalAmount() +
                        summarizedByChildArticleRows.get(i).getTotalServCharge() +
                        summarizedByChildArticleRows.get(i).getTaxAmount();
                grandTotalForAll = grandTotalForAll + grandTotalForI;
            }
            double percentage = (grandTotal / grandTotalForAll) * 100;

            String formattedCategoryType;
            if (row.getCategoryType().equals("null")) {
                formattedCategoryType = "- - - - - - - - - - - - - ";
            } else {
                formattedCategoryType = row.getCategoryType();
            }


            tableRowProperty1.setText(String.valueOf(index + 1));
            tableRowProperty2.setText(formattedCategoryType);
            tableRowProperty3.setText(numberFormat.format(Math.round(grandTotal * 100.0) / 100.0));
            tableRowProperty4.setText(numberFormat.format(Math.round(percentage * 1000.0) / 1000.0) + "%");


            summarizedByChildArticleTableLayout.addView(tableElements);
            animate(summarizedByChildArticleTableLayout, tableElements);
        }


    }


    public static void drawSummaryOfLast30Days(ArrayList<SummaryOfLast30DaysRow> summaryOfLast30DaysRows,
                                               Context context,
                                               TableLayout summarizedByLast30DaysTableLayout, int index) {


        SummaryOfLast30DaysRow row = summaryOfLast30DaysRows.get(index);
        if (context != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View tableElements = inflater.inflate(R.layout.table_row_summary_by_parent_article, null, false);

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


    }


    public static void drawSummaryOfLAst6Months(ArrayList<SummaryOfLast6MonthsRow> summaryOfLast6MonthsRows,
                                                Context context,
                                                TableLayout summarizedByLast6MonthsTableLayout, int index, double total) {

        SummaryOfLast6MonthsRow row = summaryOfLast6MonthsRows.get(index);
        if (context != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View tableElements = inflater.inflate(R.layout.table_row_summary_by_parent_article, null, false);

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


    }

    public static void drawBranchSummary(ArrayList<BranchSummaryTableRow> branchSummaryTableRows,
                                         Context context,
                                         TableLayout branchSummaryTableLayout, int index) {

        BranchSummaryTableRow row = branchSummaryTableRows.get(index);

        if (context != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View tableElements = inflater.inflate(R.layout.table_row_branch_summary, null, false);

            TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowBranchSummary1);
            TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowBranchSummary2);
            TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowBranchSummary3);
            TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowBranchSummary4);
            TextView tableRowProperty5 = tableElements.findViewById(R.id.tableRowBranchSummary5);

            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(true);


            double grandTotalForAll = 0;

            double grandTotal = row.getGrandTotal();
            for (int i = 0; i < branchSummaryTableRows.size(); i++) {
                double grandTotalForI = branchSummaryTableRows.get(i).getGrandTotal();
                grandTotalForAll = grandTotalForAll + grandTotalForI;
            }
            double percentage = (grandTotal / grandTotalForAll) * 100;


            tableRowProperty1.setText(String.valueOf(index + 1));
            tableRowProperty2.setText(row.getBranch());
            tableRowProperty3.setText(String.valueOf(row.getQuantity()));
            tableRowProperty4.setText(numberFormat.format(percentage) + "%");
            tableRowProperty5.setText(numberFormat.format(Math.round(row.getGrandTotal() * 100.0) / 100.0));

            branchSummaryTableLayout.addView(tableElements);
            animate(branchSummaryTableLayout, tableElements);
        }


    }

    public static void animate(View container, View child) {
        Animation animation = AnimationUtils.loadAnimation(container.getContext(), R.anim.bottom_to_top);
        child.startAnimation(animation);
    }
}

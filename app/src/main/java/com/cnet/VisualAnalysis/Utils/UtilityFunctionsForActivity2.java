package com.cnet.VisualAnalysis.Utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.card.MaterialCardView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class UtilityFunctionsForActivity2 {
    public static DecimalFormat decimalFormat = new DecimalFormat("#,###.00");

    public void drawPieChart(PieChartData pieChartData, PieChart piechart, String label) {
        piechart.setDrawSliceText(true);// to draw the labels
        piechart.animateX(3000, Easing.EaseInOutCirc);
        piechart.setDrawHoleEnabled(false);
        piechart.getDescription().setTextColor(Color.parseColor("#f6f8fb"));
        piechart.getDescription().setText(label);


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
                Color.parseColor("#21130d"), Color.parseColor("#873e23"),
                Color.parseColor("#B6D0E2"), Color.parseColor("#96DED1"),
                Color.parseColor("#4169E1"), Color.parseColor("#87CEEB"),
                Color.parseColor("#4682B4"), Color.parseColor("#008080"),
                Color.parseColor("#40E0D0"), Color.parseColor("#40B5AD"),
                Color.parseColor("#9FE2BF"), Color.parseColor("#0F52BA")
        );

        pieDataSet.setDrawValues(false);
        pieDataSet.setDrawIcons(true);

        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLinePart1OffsetPercentage(10); //starting of the line from center of the chart offset
        pieDataSet.setValueLinePart1Length(0.6f);
        pieDataSet.setValueLinePart2Length(0.6f);
//        pieDataSet.setValueLineColor(Color.parseColor("#FFFFFF"));
        pieDataSet.setUsingSliceColorAsValueLineColor(true);
        pieDataSet.setValueTextColor(Color.parseColor("#FFFFFF"));
        piechart.setExtraOffsets(7f, 7f, 7f, 7f);
//        pieDataSet.setSliceSpace(2);
        PieData pieData = new PieData(pieDataSet);
        piechart.setData(pieData);
    }

    public void drawDonutChart(PieChartData pieChartData, PieChart piechart, String label) {
        piechart.setDrawSliceText(true);// to draw the labels
        piechart.animateX(3000, Easing.EaseInOutCirc);
//        piechart.setDrawHoleEnabled(false);
        piechart.setHoleRadius(75);
        piechart.setHoleColor(Color.parseColor("#121729"));
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
                Color.parseColor("#21130d"), Color.parseColor("#873e23"),
                Color.parseColor("#B6D0E2"), Color.parseColor("#96DED1"),
                Color.parseColor("#4169E1"), Color.parseColor("#87CEEB"),
                Color.parseColor("#4682B4"), Color.parseColor("#008080"),
                Color.parseColor("#40E0D0"), Color.parseColor("#40B5AD"),
                Color.parseColor("#9FE2BF"), Color.parseColor("#0F52BA")
        );


        pieDataSet.setDrawValues(false);
        pieDataSet.setDrawIcons(true);

        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLinePart1OffsetPercentage(6); //starting of the line from center of the chart offset
        pieDataSet.setValueLinePart1Length(0.3f);
        pieDataSet.setValueLinePart2Length(0.4f);
        pieDataSet.setValueLineColor(Color.parseColor("#FFFFFF"));
        pieDataSet.setUsingSliceColorAsValueLineColor(true);
        pieDataSet.setValueTextColor(Color.parseColor("#FFFFFF"));
        piechart.setExtraOffsets(7f, 5f, 7f, 5f);

        PieData pieData = new PieData(pieDataSet);
        piechart.setData(pieData);

    }

    public void drawBarChart(BarChartData barChartData, BarChart barChart, String label) {
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
        barChart.getXAxis().setCenterAxisLabels(true);
        barChart.getXAxis().setLabelCount(barChartData.x.length);
//        barChart.getXAxis().setAxisMinimum(0f);

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

    public void drawDoubleBarChart(BarChartData barChartData1, BarChartData barChartData2, BarChart barChart, String label1, String label2) {
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
        barChart.getXAxis().setCenterAxisLabels(true);
        barChart.getXAxis().setLabelCount(barChartData1.x.length);

        ArrayList<BarEntry> barChartEntries1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> barChartEntries2 = new ArrayList<BarEntry>();

        ArrayList<String> formattedXLabels = new ArrayList<>();

        for (int i = 0; i < barChartData1.x.length; i++) {
            barChartEntries1.add(new BarEntry(barChartData1.x[i], barChartData1.y[i]));
            barChartEntries2.add(new BarEntry(barChartData2.x[i], barChartData2.y[i]));

            String xLabelAtIndex = barChartData1.legends[i];
            formattedXLabels.add(xLabelAtIndex);

        }

        BarDataSet barDataSet1 = new BarDataSet(barChartEntries1, label1);
        BarDataSet barDataSet2 = new BarDataSet(barChartEntries2, label2);
        barDataSet1.setForm(Legend.LegendForm.CIRCLE);
        barDataSet2.setForm(Legend.LegendForm.CIRCLE);

        BarData barData = new BarData(barDataSet1, barDataSet2);

        barChart.setData(barData);
        barChart.setVisibleXRangeMaximum(barChartEntries1.size());
        barChart.setExtraBottomOffset(15f);

        barDataSet1.setColor(Color.parseColor("#5b79e7"));
        barDataSet2.setColor(Color.parseColor("#27adb9"));
        barDataSet1.setDrawValues(false);
        barDataSet2.setDrawValues(false);

        barData.setBarWidth(0.3f);
        barChart.getXAxis().setAxisMinimum(-0.7f);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(0.6f, 0f) * barChartData1.x.length);
        barChart.groupBars(0, 0.4f, 0f);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getXAxis().setLabelCount(barChartData1.x.length);
        barChart.animateXY(3000, 3000);
        barChart.getAxisLeft().setTextColor(Color.parseColor("#f6f8fb"));
        barChart.getXAxis().setTextColor(Color.parseColor("#f6f8fb"));
        barChart.getLegend().setTextColor(Color.parseColor("#f6f8fb"));
        barChart.getDescription().setTextColor(Color.parseColor("#f6f8fb"));
        try {
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(formattedXLabels));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void drawStackedBarChart(BarChartData barChartData, BarChart barChart, String label) {
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
        barChart.getXAxis().setCenterAxisLabels(false);
        barChart.getXAxis().setLabelCount(barChartData.x.length);

//        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(barChartData.legends));
//        barChart.getXAxis().setLabelRotationAngle(-45);
//        barChart.getXAxis().setLabelCount(barChartData.legends.length);

        ArrayList<BarEntry> barChartEntries = new ArrayList<BarEntry>();
        for (int i = 0; i < barChartData.x.length; i++) {
            barChartEntries.add(new BarEntry(1, barChartData.y[i]));
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

    public void drawDoubleLineChart(LineChartData lineChartData1, LineChartData lineChartData2, LineChart lineChart, String label1, String label2) {
        ArrayList<Entry> dataVals1 = new ArrayList<Entry>();
        ArrayList<Entry> dataVals2 = new ArrayList<Entry>();
        ArrayList<String> formattedXLabels = new ArrayList<>();

        if (lineChartData1 != null) {
            for (int i = 0; i < lineChartData1.x.length; i++) {
                dataVals1.add(new Entry(lineChartData1.x[i], lineChartData1.y[i]));
                dataVals2.add(new Entry(lineChartData2.x[i], lineChartData2.y[i]));

//                String xLabelAtIndex = new UtilityFunctionsForActivity1().formatHourNmin(lineChartData1.legends[i]);
                String xLabelAtIndex = lineChartData1.legends[i];
                formattedXLabels.add(xLabelAtIndex);
            }

            LineDataSet lineDataSet1 = new LineDataSet(dataVals1, label1);
            LineDataSet lineDataSet2 = new LineDataSet(dataVals2, label2);
            LineData lineData = new LineData();
            lineData.addDataSet(lineDataSet1);
            lineData.addDataSet(lineDataSet2);
            lineChart.setData(lineData);
            lineChart.getAxisLeft().setDrawLabels(true);
            lineChart.getAxisRight().setDrawGridLines(false);
            lineChart.getAxisLeft().setDrawGridLines(false);
            lineChart.getAxisRight().setDrawAxisLine(false);
            lineChart.getAxisRight().setDrawLabels(false);
            lineChart.getDescription().setEnabled(false);
            lineChart.getXAxis().setDrawAxisLine(true);
            lineChart.getXAxis().setDrawGridLines(false);
            lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            lineChart.setDrawGridBackground(false);
            lineChart.getAxisLeft().setDrawGridLines(false);
            lineChart.getXAxis().setGranularity(1f);
            lineChart.getXAxis().setCenterAxisLabels(false);
            lineDataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet1.setDrawValues(false);
            lineDataSet1.setColors(Color.parseColor("#5b79e7"));
            lineDataSet1.setDrawCircles(false);
            lineDataSet2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet2.setDrawValues(false);
            lineDataSet2.setColors(Color.parseColor("#27adb9"));
            lineDataSet2.setDrawCircles(false);
            lineChart.setExtraBottomOffset(15f);
            lineChart.setExtraTopOffset(15f);
            lineChart.animateX(500, Easing.EaseInCubic);
            lineChart.getXAxis().setTextColor(Color.parseColor("#f6f8fb"));
            lineChart.getAxisLeft().setTextColor(Color.parseColor("#f6f8fb"));
            lineChart.getLegend().setTextColor(Color.parseColor("#f6f8fb"));
            try {
                lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(formattedXLabels));
            } catch (Exception e) {
                e.printStackTrace();
            }
            lineChart.getXAxis().setLabelRotationAngle(-45);
            lineChart.getXAxis().setLabelCount(lineChartData1.legends.length, true);
//            lineChart.getXAxis().setGranularity(1f);
            lineChart.getXAxis().setCenterAxisLabels(false);
//            lineChart.setExtraOffsets(7f, 7f, 7f, 7f);
//            lineChart.setPinchZoom(false);
//            lineChart.getXAxis().setAxisMaximum(dataVals.get(lineChartData.x.length - 1).getX() + 0.1f);
        }
    }

    public void drawSingleLineChart(LineChartData lineChartData1, LineChart lineChart, String label) {
        ArrayList<Entry> dataVals1 = new ArrayList<Entry>();
        ArrayList<String> formattedXLabels = new ArrayList<>();

        if (lineChartData1 != null) {
            for (int i = 0; i < lineChartData1.x.length; i++) {
                dataVals1.add(new Entry(lineChartData1.x[i], lineChartData1.y[i]));

                String xLabelAtIndex = lineChartData1.legends[i];
                formattedXLabels.add(xLabelAtIndex);
            }

            LineDataSet lineDataSet1 = new LineDataSet(dataVals1, label);
            LineData lineData = new LineData();
            lineData.addDataSet(lineDataSet1);
            lineChart.setData(lineData);
            lineChart.getAxisLeft().setDrawLabels(true);
            lineChart.getAxisRight().setDrawGridLines(false);
            lineChart.getAxisLeft().setDrawGridLines(false);
            lineChart.getAxisRight().setDrawAxisLine(false);
            lineChart.getAxisRight().setDrawLabels(false);
            lineChart.getDescription().setEnabled(false);
            lineChart.getXAxis().setDrawAxisLine(true);
            lineChart.getXAxis().setDrawGridLines(false);
            lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            lineChart.setDrawGridBackground(false);
            lineChart.getAxisLeft().setDrawGridLines(false);
            lineChart.getXAxis().setGranularity(1f);
            lineChart.getXAxis().setCenterAxisLabels(false);
            lineDataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet1.setDrawValues(false);
            lineDataSet1.setColors(Color.parseColor("#5b79e7"));
            lineDataSet1.setDrawCircles(false);
            lineChart.setExtraBottomOffset(15f);
            lineChart.setExtraTopOffset(15f);
            lineChart.animateX(500, Easing.EaseInCubic);
            lineChart.getXAxis().setTextColor(Color.parseColor("#f6f8fb"));
            lineChart.getAxisLeft().setTextColor(Color.parseColor("#f6f8fb"));
            lineChart.getLegend().setTextColor(Color.parseColor("#f6f8fb"));
            try {
                lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(formattedXLabels));
            } catch (Exception e) {
                e.printStackTrace();
            }
            lineChart.getXAxis().setLabelRotationAngle(-45);
            lineChart.getXAxis().setLabelCount(lineChartData1.legends.length, true);
//            lineChart.getXAxis().setGranularity(1f);
            lineChart.getXAxis().setCenterAxisLabels(false);
//            lineChart.setExtraOffsets(7f, 7f, 7f, 7f);
//            lineChart.setPinchZoom(false);
//            lineChart.getXAxis().setAxisMaximum(dataVals.get(lineChartData.x.length - 1).getX() + 0.1f);
        }
    }

    public void drawSummaryByArticleTable(
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


            tableRowProperty1.setText(numberFormat.format(index + 1));
            tableRowProperty2.setText(formattedArticleName);
            tableRowProperty3.setText(numberFormat.format(row.getQuantity()));
//            tableRowProperty4.setText(numberFormat.format(Math.round(row.getAvgAmount() * 100.0) / 100.0));
            tableRowProperty4.setText(decimalFormat.format(row.getAvgAmount()));
//            tableRowProperty5.setText(numberFormat.format(Math.round(grandTotal * 100.0) / 100.0));
            tableRowProperty5.setText(decimalFormat.format(grandTotal));

            summarizedByArticleTableLayout.addView(tableElements);
            animateBottomToTop(summarizedByArticleTableLayout, tableElements);

        }

    }

    public void drawSummaryByParentArticleTable(ArrayList<SummarizedByParentArticleRow> summarizedByParentArticleRows,
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
//            tableRowProperty3.setText(numberFormat.format(Math.round(percentage * 100.0) / 100.0) + "%");
            tableRowProperty3.setText(decimalFormat.format(percentage) + "%");
//            tableRowProperty4.setText(numberFormat.format(Math.round(grandTotal * 100.0) / 100.0));
            tableRowProperty4.setText(decimalFormat.format(grandTotal));

            summarizedByParentArticleTableLayout.addView(tableElements);
            animateBottomToTop(summarizedByParentArticleTableLayout, tableElements);
        }


    }

    public void drawSummaryByChildArticleTable(ArrayList<SummarizedByChildArticleRow> summarizedByChildArticleRows,
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
//            tableRowProperty4.setText(numberFormat.format(Math.round(grandTotal * 100.0) / 100.0));
            tableRowProperty4.setText(decimalFormat.format(grandTotal));
//            tableRowProperty3.setText(numberFormat.format(Math.round(percentage * 100.0) / 100.0) + "%");
            tableRowProperty3.setText(decimalFormat.format(percentage) + "%");


            summarizedByChildArticleTableLayout.addView(tableElements);
            animateBottomToTop(summarizedByChildArticleTableLayout, tableElements);
        }


    }

    public void drawSummaryOfLast30Days(ArrayList<SummaryOfLast30DaysRow> summaryOfLast30DaysRows,
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
            tableRowProperty2.setText(new UtilityFunctionsForActivity1().formatDateToString2(row.getDateTime()));
            tableRowProperty3.setText(numberFormat.format(row.getTransactionCount()));
//            tableRowProperty4.setText(numberFormat.format(Math.round(row.getAmount() * 100.0) / 100.0));
            tableRowProperty4.setText(decimalFormat.format(row.getAmount()));

            summarizedByLast30DaysTableLayout.addView(tableElements);
            animateBottomToTop(summarizedByLast30DaysTableLayout, tableElements);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void drawSummaryOfLAst6Months(ArrayList<SummaryOfLast6MonthsRow> summaryOfLast6MonthsRows,
                                         Context context,
                                         TableLayout summarizedByLast6MonthsTableLayout, int index) {

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
//            tableRowProperty2.setText(row.getName());
            tableRowProperty2.setText(new UtilityFunctionsForActivity1().formatDateToString(row.getDateTime()));
            tableRowProperty3.setText(numberFormat.format(row.getTransactionCount()));
//            tableRowProperty4.setText(numberFormat.format(Math.round(row.getAmount() * 100.0) / 100.0));
            tableRowProperty4.setText(decimalFormat.format(row.getAmount()));
//            tableRowProperty4.setText(new SimpleDateFormat(Constants.dateCriteriaFormat, Locale.getDefault()).format(Calendar.getInstance().getTime()));

            summarizedByLast6MonthsTableLayout.addView(tableElements);
            animateBottomToTop(summarizedByLast6MonthsTableLayout, tableElements);
        }


    }

    public void drawBranchSummary(ArrayList<BranchSummaryTableRow> branchSummaryTableRows,
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
            TextView tableRowProperty6 = tableElements.findViewById(R.id.tableRowBranchSummary6);

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
            tableRowProperty3.setText(String.valueOf(row.getLineItems()));
            tableRowProperty4.setText(numberFormat.format(row.getTransactionCount()));
//            tableRowProperty5.setText(numberFormat.format(Math.round(percentage * 100.0) / 100.0) + "%");
            tableRowProperty5.setText(decimalFormat.format(percentage) + "%");
//            tableRowProperty6.setText(numberFormat.format(Math.round(row.getGrandTotal() * 100.0) / 100.0));
            tableRowProperty6.setText(decimalFormat.format(row.getGrandTotal()));

            branchSummaryTableLayout.addView(tableElements);
            animateBottomToTop(branchSummaryTableLayout, tableElements);
        }


    }

    public void animateBottomToTop(View container, View child) {
        Animation animation = AnimationUtils.loadAnimation(container.getContext(), R.anim.bottom_to_top);
        child.startAnimation(animation);
    }

    public void drawChart(Context context, String chartType, MaterialCardView viewHolder, PieChartData pieChartData, BarChartData barChartData, LineChartData lineChartData,
                          String label) {

        if (chartType.equals(Constants.DONUT_TYPE)) {
            View pieElement = LayoutInflater.from(context).inflate(R.layout.pie_chart_layout, null, false);
            viewHolder.addView(pieElement);
            new UtilityFunctionsForActivity2().drawDonutChart(pieChartData, (PieChart) pieElement, label);
        } else if (chartType.equals(Constants.BAR_TYPE)) {
            View barElement = LayoutInflater.from(context).inflate(R.layout.bar_chart_layout, null, false);
            viewHolder.addView(barElement);
            new UtilityFunctionsForActivity2().drawBarChart(barChartData, (BarChart) barElement, label);
        } else if (chartType.equals(Constants.LINE_TYPE)) {
            View lineElement = LayoutInflater.from(context).inflate(R.layout.line_chart_layout, null, false);
            viewHolder.addView(lineElement);
            new UtilityFunctionsForActivity2().drawSingleLineChart(lineChartData, (LineChart) lineElement, label);
        } else {//default pie chart
            View pieElement = LayoutInflater.from(context).inflate(R.layout.pie_chart_layout, null, false);
            viewHolder.addView(pieElement);
            new UtilityFunctionsForActivity2().drawPieChart(pieChartData, (PieChart) pieElement, label);
        }
    }

    public void drawChart(Context context, String chartType, MaterialCardView viewHolder, PieChartData pieChartData, BarChartData barChartData1, BarChartData barChartData2, LineChartData lineChartData1,
                          LineChartData lineChartData2, String label1, String label2) {

        if (chartType.equals(Constants.DONUT_TYPE)) {
            View pieElement = LayoutInflater.from(context).inflate(R.layout.pie_chart_layout, null, false);
            viewHolder.addView(pieElement);
            drawDonutChart(pieChartData, (PieChart) pieElement, label1);
        } else if (chartType.equals(Constants.BAR_TYPE)) {
            View barElement = LayoutInflater.from(context).inflate(R.layout.bar_chart_layout, null, false);
            viewHolder.addView(barElement);
//            drawBarChart(barChartData, (BarChart) barElement, label1);
            drawDoubleBarChart(barChartData1, barChartData2, (BarChart) barElement, label1, label2);
        } else if (chartType.equals(Constants.PIE_TYPE)) {//default pie chart
            View pieElement = LayoutInflater.from(context).inflate(R.layout.pie_chart_layout, null, false);
            viewHolder.addView(pieElement);
            drawPieChart(pieChartData, (PieChart) pieElement, label1);
        } else {//default lineChart
            View lineElement = LayoutInflater.from(context).inflate(R.layout.line_chart_layout, null, false);
            viewHolder.addView(lineElement);
//            new UtilityFunctionsForActivity2().drawSingleLineChart(lineChartData, (LineChart) lineElement, label);
            drawDoubleLineChart(lineChartData1, lineChartData2, (LineChart) lineElement, label1, label2);
        }
    }


}

package com.cnet.VisualAnalysis;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;

public class SummarizedByArticleFragment extends Fragment {

    BarChart barChartSumByArticle;
    PieChart pieChartSumByArticle;

    public SummarizedByArticleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summarized_by_article, container, false);

        barChartSumByArticle = view.findViewById(R.id.bChartSumByArticle);
        pieChartSumByArticle = view.findViewById(R.id.pchartsumByArticle);


        drawBarChart();
        drawPieChart();

        return view;
    }

    private void drawPieChart() {
        pieChartSumByArticle.setDrawSliceText(false);
//        pieChartSumByArticle.setHoleRadius(80);
        pieChartSumByArticle.animateX(3000, Easing.EaseInOutCirc);
        pieChartSumByArticle.setDrawHoleEnabled(false);
        pieChartSumByArticle.getDescription().setTextColor(Color.parseColor("#f6f8fb"));
        pieChartSumByArticle.getLegend().setTextColor(Color.parseColor("#f6f8fb"));

        ArrayList<PieEntry> piedatas = new ArrayList<>();
        piedatas.add(new PieEntry(2566,"Adminstrative Officer"));
        piedatas.add(new PieEntry(7450, "Sales and Cashier"));

        PieDataSet pieDataSet = new PieDataSet(piedatas,"");
        pieDataSet.setColors(Color.parseColor("#5472e8"),Color.parseColor("#26adb9"));
        pieDataSet.setDrawValues(false);

        PieData pieData = new PieData(pieDataSet);
        pieChartSumByArticle.setData(pieData);

    }

    private void drawBarChart() {
        barChartSumByArticle.getDescription().setEnabled(false);
        barChartSumByArticle.setDrawGridBackground(false);
        barChartSumByArticle.getXAxis().setCenterAxisLabels(true);
        barChartSumByArticle.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChartSumByArticle.getXAxis().setDrawGridLines(false);
        barChartSumByArticle.getAxisRight().setDrawGridLines(false);
        barChartSumByArticle.getAxisRight().setDrawAxisLine(false);
        barChartSumByArticle.getAxisRight().setDrawLabels(false);
        barChartSumByArticle.getAxisLeft().setDrawGridLines(false);
        barChartSumByArticle.setPinchZoom(false);
        barChartSumByArticle.getXAxis().setGranularity(1f);

        ArrayList<BarEntry> barchartData = new ArrayList<BarEntry>();
        barchartData.add(new BarEntry(1, 6f));
        barchartData.add(new BarEntry(2, 8));
        barchartData.add(new BarEntry(3, 5));
        barchartData.add(new BarEntry(4, 2));
        barchartData.add(new BarEntry(5, 3.5f));
        barchartData.add(new BarEntry(6, 3.5f));
        barchartData.add(new BarEntry(7, 3.5f));
        barchartData.add(new BarEntry(8, 3.5f));

        BarDataSet barDataSet = new BarDataSet(barchartData, "Summarized by article");
        barDataSet.setForm(Legend.LegendForm.CIRCLE);

        BarData barData = new BarData(barDataSet);

        barChartSumByArticle.setData(barData);
        barChartSumByArticle.setVisibleXRangeMaximum(barchartData.size());
        barChartSumByArticle.setExtraBottomOffset(15f);

        barDataSet.setColor(Color.parseColor("#27adb9"));
        barDataSet.setDrawValues(false);

        barChartSumByArticle.animateXY(3000, 3000);
        barChartSumByArticle.getAxisRight().setDrawLabels(false);

        barChartSumByArticle.getAxisLeft().setTextColor(Color.parseColor("#f6f8fb"));
        barChartSumByArticle.getXAxis().setTextColor(Color.parseColor("#f6f8fb"));
        barChartSumByArticle.getLegend().setTextColor(Color.parseColor("#f6f8fb"));
        barChartSumByArticle.getDescription().setTextColor(Color.parseColor("#f6f8fb"));

    }
}
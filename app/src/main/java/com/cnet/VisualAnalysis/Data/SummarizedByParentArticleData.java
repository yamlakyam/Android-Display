package com.cnet.VisualAnalysis.Data;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;

public class SummarizedByParentArticleData {
    private ArrayList<SummarizedByParentArticleRow> tableData;
    private BarChartData barChartData;
    private PieChartData pieChartData;

    public ArrayList<SummarizedByParentArticleRow> getTableData() {
        return tableData;
    }

    public BarChartData getBarChartData() {
        return barChartData;
    }

    public PieChartData getPieChartData() {
        return pieChartData;
    }

    public SummarizedByParentArticleData(ArrayList<SummarizedByParentArticleRow> tableData, BarChartData barChartData, PieChartData pieChartData) {
        this.tableData = tableData;
        this.barChartData = barChartData;
        this.pieChartData = pieChartData;


    }
}

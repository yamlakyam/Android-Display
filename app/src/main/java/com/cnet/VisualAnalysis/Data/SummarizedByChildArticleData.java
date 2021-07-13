package com.cnet.VisualAnalysis.Data;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;

public class SummarizedByChildArticleData {
    private ArrayList<SummarizedByChildArticleRow> tableData;
    private BarChartData barChartData;
    private PieChartData pieChartData;

    public SummarizedByChildArticleData(ArrayList<SummarizedByChildArticleRow> tableData, BarChartData barChartData, PieChartData pieChartData) {
        this.tableData = tableData;
        this.barChartData = barChartData;
        this.pieChartData = pieChartData;
    }

    public ArrayList<SummarizedByChildArticleRow> getTableData() {
        return tableData;
    }

    public BarChartData getBarChartData() {
        return barChartData;
    }

    public PieChartData getPieChartData() {
        return pieChartData;
    }
}

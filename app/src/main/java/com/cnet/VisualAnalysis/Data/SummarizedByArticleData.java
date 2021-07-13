package com.cnet.VisualAnalysis.Data;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;

public class SummarizedByArticleData {
    public ArrayList<SummarizedByArticleTableRow> tableData;
    public BarChartData barChartData;
    public PieChartData pieChartData;


    public SummarizedByArticleData(ArrayList<SummarizedByArticleTableRow> tableData, BarChartData barChartData, PieChartData pieChartData) {
        this.tableData = tableData;
        this.barChartData = barChartData;
        this.pieChartData = pieChartData;
    }

    public ArrayList<SummarizedByArticleTableRow> getTableData() {
        return tableData;
    }

    public BarChartData getBarChartData() {
        return barChartData;
    }

    public PieChartData getPieChartData() {
        return pieChartData;
    }
}





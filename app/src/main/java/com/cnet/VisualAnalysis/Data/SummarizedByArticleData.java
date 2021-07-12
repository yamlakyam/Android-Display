package com.cnet.VisualAnalysis.Data;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;

public class SummarizedByArticleData {
    public ArrayList<SummarizedByArticleTableRow> tableData;
    public BarChart barChartData;
    public PieChart pieChartData;


    public SummarizedByArticleData(ArrayList<SummarizedByArticleTableRow> tableData, BarChart barChartData, PieChart pieChartData) {
        this.tableData = tableData;
        this.barChartData = barChartData;
        this.pieChartData = pieChartData;
    }

    public ArrayList<SummarizedByArticleTableRow> getTableData() {
        return tableData;
    }

    public BarChart getBarChartData() {
        return barChartData;
    }

    public PieChart getPieChartData() {
        return pieChartData;
    }
}





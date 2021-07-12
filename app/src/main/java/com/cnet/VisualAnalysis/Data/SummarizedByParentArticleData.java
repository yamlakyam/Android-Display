package com.cnet.VisualAnalysis.Data;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;

public class SummarizedByParentArticleData {
    private ArrayList<SummarizedByParentArticleRow> tableData;
    private BarChart barChartData;
    private PieChart pieChartData;

    public ArrayList<SummarizedByParentArticleRow> getTableData() {
        return tableData;
    }

    public BarChart getBarChartData() {
        return barChartData;
    }

    public PieChart getPieChartData() {
        return pieChartData;
    }

    public SummarizedByParentArticleData(ArrayList<SummarizedByParentArticleRow> tableData, BarChart barChartData, PieChart pieChartData) {
        this.tableData = tableData;
        this.barChartData = barChartData;
        this.pieChartData = pieChartData;


    }
}

package com.cnet.VisualAnalysis.Data;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;

public class SummarizedByArticleData {
    public ArrayList<SummarizedByArticleTableRow> tableData;
    public BarChartData barChartData;
    public LineChartData lineChartData;


    public SummarizedByArticleData(ArrayList<SummarizedByArticleTableRow> tableData, BarChartData barChartData, LineChartData lineChartData) {
        this.tableData = tableData;
        this.barChartData = barChartData;
        this.lineChartData = lineChartData;
    }

    public ArrayList<SummarizedByArticleTableRow> getTableData() {
        return tableData;
    }

    public BarChartData getBarChartData() {
        return barChartData;
    }

    public LineChartData getLineChartData() {
        return lineChartData;
    }
}





package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class SummarizedByParentArticleData {
    private ArrayList<SummarizedByParentArticleRow> tableData;
    private BarChartData barChartData;
    private PieChartData pieChartData;
    private LineChartData lineChartData;

    public ArrayList<SummarizedByParentArticleRow> getTableData() {
        return tableData;
    }

    public BarChartData getBarChartData() {
        return barChartData;
    }

    public PieChartData getPieChartData() {
        return pieChartData;
    }

    public LineChartData getLineChartData() {
        return lineChartData;
    }

    public SummarizedByParentArticleData(ArrayList<SummarizedByParentArticleRow> tableData, BarChartData barChartData, PieChartData pieChartData, LineChartData lineChartData) {
        this.tableData = tableData;
        this.barChartData = barChartData;
        this.pieChartData = pieChartData;
        this.lineChartData = lineChartData;


    }
}

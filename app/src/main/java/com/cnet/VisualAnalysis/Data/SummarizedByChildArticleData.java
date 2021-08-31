package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class SummarizedByChildArticleData {
    private ArrayList<SummarizedByChildArticleRow> tableData;
    private BarChartData barChartData;
    private PieChartData pieChartData;
    private LineChartData lineChartData;

    public SummarizedByChildArticleData(ArrayList<SummarizedByChildArticleRow> tableData, BarChartData barChartData, PieChartData pieChartData, LineChartData lineChartData) {
        this.tableData = tableData;
        this.barChartData = barChartData;
        this.pieChartData = pieChartData;
        this.lineChartData = lineChartData;
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

    public LineChartData getLineChartData() {
        return lineChartData;
    }
}

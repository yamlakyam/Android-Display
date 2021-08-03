package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class SummarizedByArticleData {
    public ArrayList<SummarizedByArticleTableRow> tableData;
    public BarChartData barChartData;
    public LineChartData lineChartData;
    public PieChartData pieChartData;


    public SummarizedByArticleData(ArrayList<SummarizedByArticleTableRow> tableData,
                                   BarChartData barChartData,
                                   LineChartData lineChartData,
                                   PieChartData pieChartData) {
        this.tableData = tableData;
        this.barChartData = barChartData;
        this.lineChartData = lineChartData;
        this.pieChartData = pieChartData;
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

    public PieChartData getPieChartData() {
        return pieChartData;
    }
}





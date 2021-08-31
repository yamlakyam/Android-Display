package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class SummarizedByArticleData {
    public ArrayList<SummarizedByArticleTableRow> tableData;
    public BarChartData barChartData;
    public LineChartData lineChartData;
    public PieChartData pieChartData;


    public SummarizedByArticleData(ArrayList<SummarizedByArticleTableRow> tableData,

                                   BarChartData barChartData,

                                   PieChartData pieChartData, LineChartData lineChartData) {
        this.tableData = tableData;
        this.barChartData = barChartData;

        this.pieChartData = pieChartData;
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

    public PieChartData getPieChartData() {
        return pieChartData;
    }
}





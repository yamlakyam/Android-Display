package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class SummaryOfLast30DaysData {
    public LineChartData lineChartData;
    public BarChartData barChartData;
    public ArrayList<SummaryOfLast30DaysRow> tableData;
    public PieChartData pieChartData;

    public SummaryOfLast30DaysData(BarChartData barChartData, ArrayList<SummaryOfLast30DaysRow> tableData, LineChartData lineChartData,
                                   PieChartData pieChartData) {

        this.tableData = tableData;
        this.barChartData = barChartData;
        this.lineChartData = lineChartData;
        this.pieChartData = pieChartData;
    }

    public LineChartData getLineChartData() {
        return lineChartData;
    }

    public ArrayList<SummaryOfLast30DaysRow> getTableData() {
        return tableData;
    }

    public BarChartData getBarChartData() {
        return barChartData;
    }

    public PieChartData getPieChartData() {
        return pieChartData;
    }
}

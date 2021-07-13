package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class SummaryOfLast30DaysData {
    public LineChartData lineChartData;
    public BarChartData barChartData;
    public ArrayList<SummaryOfLast30DaysRow> tableData;

    public SummaryOfLast30DaysData(LineChartData lineChartData, BarChartData barChartData, ArrayList<SummaryOfLast30DaysRow> tableData) {
        this.lineChartData = lineChartData;
        this.tableData = tableData;
        this.barChartData = barChartData;
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
}

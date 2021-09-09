package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class SummaryOfLast30DaysData {
    public LineChartData lineChartData1;
    public LineChartData lineChartData2;
    public BarChartData barChartData1;
    public BarChartData barChartData2;
    public ArrayList<SummaryOfLast30DaysRow> tableData;
    public PieChartData pieChartData;

    public SummaryOfLast30DaysData(LineChartData lineChartData1, LineChartData lineChartData2, BarChartData barChartData1, BarChartData barChartData2, ArrayList<SummaryOfLast30DaysRow> tableData, PieChartData pieChartData) {
        this.lineChartData1 = lineChartData1;
        this.lineChartData2 = lineChartData2;
        this.barChartData1 = barChartData1;
        this.barChartData2 = barChartData2;
        this.tableData = tableData;
        this.pieChartData = pieChartData;
    }

    public LineChartData getLineChartData1() {
        return lineChartData1;
    }

    public LineChartData getLineChartData2() {
        return lineChartData2;
    }

    public BarChartData getBarChartData1() {
        return barChartData1;
    }

    public BarChartData getBarChartData2() {
        return barChartData2;
    }

    public ArrayList<SummaryOfLast30DaysRow> getTableData() {
        return tableData;
    }

    public PieChartData getPieChartData() {
        return pieChartData;
    }
}

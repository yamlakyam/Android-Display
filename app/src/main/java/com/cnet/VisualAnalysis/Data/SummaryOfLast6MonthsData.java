package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class SummaryOfLast6MonthsData {
    private PieChartData pieChartData;
    private ArrayList<SummaryOfLast6MonthsRow> tableData;
    private BarChartData barChartData;

    public SummaryOfLast6MonthsData(PieChartData lineChartData, ArrayList<SummaryOfLast6MonthsRow> tableData, BarChartData barChartData) {
        this.pieChartData = pieChartData;
        this.tableData = tableData;
        this.barChartData =barChartData;
    }

    public PieChartData getPieChartData() {
        return pieChartData;
    }

    public ArrayList<SummaryOfLast6MonthsRow> getTableData() {
        return tableData;
    }

    public BarChartData getBarChartData() {
        return barChartData;
    }
}

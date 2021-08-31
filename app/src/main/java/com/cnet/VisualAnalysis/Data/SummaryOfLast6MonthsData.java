package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class SummaryOfLast6MonthsData {
    private PieChartData pieChartData;
    private ArrayList<SummaryOfLast6MonthsRow> tableData;
    private BarChartData barChartData;
    private LineChartData lineChartData;

    public SummaryOfLast6MonthsData(PieChartData pieChartData, ArrayList<SummaryOfLast6MonthsRow> tableData, BarChartData barChartData,
                                    LineChartData lineChartData) {
        this.pieChartData = pieChartData;
        this.tableData = tableData;
        this.barChartData = barChartData;
        this.lineChartData = lineChartData;
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

    public LineChartData getLineChartData() {
        return lineChartData;
    }
}

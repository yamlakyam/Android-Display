package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class SummaryOfLast6MonthsData {
    private PieChartData pieChartData;
    private ArrayList<SummaryOfLast6MonthsRow> tableData;
    private BarChartData barChartData1;
    private BarChartData barChartData2;
    private LineChartData lineChartDat1;
    private LineChartData lineChartData2;

    public SummaryOfLast6MonthsData(PieChartData pieChartData, ArrayList<SummaryOfLast6MonthsRow> tableData,
                                    BarChartData barChartData1, BarChartData barChartData2,
                                    LineChartData lineChartDat1, LineChartData lineChartData2) {
        this.pieChartData = pieChartData;
        this.tableData = tableData;
        this.barChartData1 = barChartData1;
        this.barChartData2 = barChartData2;
        this.lineChartDat1 = lineChartDat1;
        this.lineChartData2 = lineChartData2;
    }

    public PieChartData getPieChartData() {
        return pieChartData;
    }

    public ArrayList<SummaryOfLast6MonthsRow> getTableData() {
        return tableData;
    }

    public BarChartData getBarChartData1() {
        return barChartData1;
    }

    public BarChartData getBarChartData2() {
        return barChartData2;
    }

    public LineChartData getLineChartDat1() {
        return lineChartDat1;
    }

    public LineChartData getLineChartData2() {
        return lineChartData2;
    }
}

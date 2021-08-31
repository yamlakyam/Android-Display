package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class UserReportDataForSingleOu {

    public ArrayList<UserReportTableRow> userReportTableRowArrayList;
    public PieChartData pieChartData;
    public String org;
    public LineChartData lineChartData;
    public BarChartData barChartData;

    public UserReportDataForSingleOu(ArrayList<UserReportTableRow> userReportTableRowArrayList, PieChartData pieChartData, String org,
                                     LineChartData lineChartData, BarChartData barChartData) {
        this.userReportTableRowArrayList = userReportTableRowArrayList;
        this.pieChartData = pieChartData;
        this.lineChartData = lineChartData;
        this.barChartData = barChartData;
        this.org = org;
    }

    public ArrayList<UserReportTableRow> getUserReportTableRowArrayList() {
        return userReportTableRowArrayList;
    }

    public PieChartData getPieChartData() {
        return pieChartData;
    }

    public String getOrg() {
        return org;
    }

    public LineChartData getLineChartData() {
        return lineChartData;
    }

    public BarChartData getBarChartData() {
        return barChartData;
    }
}

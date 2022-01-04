package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class UserReportDataForSingleOu {

    public ArrayList<UserReportTableRow> userReportTableRowArrayList;
    public PieChartData pieChartData;
    public String org;

    public UserReportDataForSingleOu(ArrayList<UserReportTableRow> userReportTableRowArrayList, PieChartData pieChartData, String org) {
        this.userReportTableRowArrayList = userReportTableRowArrayList;
        this.pieChartData = pieChartData;
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
}

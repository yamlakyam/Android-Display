package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class BranchSummaryData {

    ArrayList<BranchSummaryTableRow> branchSummaryTableRows;
    PieChartData pieChartData;

    public BranchSummaryData(ArrayList<BranchSummaryTableRow> branchSummaryTableRows, PieChartData pieChartData) {
        this.branchSummaryTableRows = branchSummaryTableRows;
        this.pieChartData =pieChartData;
    }

    public ArrayList<BranchSummaryTableRow> getBranchSummaryTableRows() {
        return branchSummaryTableRows;
    }

    public PieChartData getPieChartData() {
        return pieChartData;
    }
}

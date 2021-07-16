package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class BranchSummaryData {

    ArrayList<BranchSummaryTableRow> branchSummaryTableRows;

    public BranchSummaryData(ArrayList<BranchSummaryTableRow> branchSummaryTableRows) {
        this.branchSummaryTableRows = branchSummaryTableRows;
    }

    public ArrayList<BranchSummaryTableRow> getBranchSummaryTableRows() {
        return branchSummaryTableRows;
    }
}

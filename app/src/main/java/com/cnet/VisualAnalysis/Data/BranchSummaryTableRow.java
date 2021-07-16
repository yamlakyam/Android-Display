package com.cnet.VisualAnalysis.Data;

public class BranchSummaryTableRow {
    String branch;
    double grandTotal;
    int quantity;

    public BranchSummaryTableRow(String branch, double grandTotal, int quantity) {
        this.branch = branch;
        this.grandTotal = grandTotal;
        this.quantity = quantity;
    }

    public String getBranch() {
        return branch;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public int getQuantity() {
        return quantity;
    }
}

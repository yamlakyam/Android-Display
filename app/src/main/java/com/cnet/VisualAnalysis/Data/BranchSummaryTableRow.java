package com.cnet.VisualAnalysis.Data;

public class BranchSummaryTableRow {
    String branch;
    double grandTotal;
    int quantity;
    int transactionCount;
    int lineItems;
    String lastActivity;

    public BranchSummaryTableRow(String branch, double grandTotal, int transactionCount, int lineItems, String lastActivity) {
        this.branch = branch;
        this.grandTotal = grandTotal;
        this.transactionCount = transactionCount;
        this.lineItems = lineItems;
        this.lastActivity = lastActivity;

    }

    public String getBranch() {
        return branch;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public int getLineItems() {
        return lineItems;
    }

    public String getLastActivity() {
        return lastActivity;
    }
}

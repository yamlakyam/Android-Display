package com.cnet.VisualAnalysis.Data;

public class BranchSummaryTableRow {
    String branch;
    double grandTotal;
    int quantity;
    int transactionCount;
    int lineItems;

    public BranchSummaryTableRow(String branch, double grandTotal, int transactionCount, int lineItems) {
        this.branch = branch;
        this.grandTotal = grandTotal;
        this.transactionCount = transactionCount;
        this.lineItems = lineItems;

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
}

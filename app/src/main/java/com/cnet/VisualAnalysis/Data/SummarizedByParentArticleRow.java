package com.cnet.VisualAnalysis.Data;

public class SummarizedByParentArticleRow {

    String branchName;
    String percentage;
    double total;

    public SummarizedByParentArticleRow(String branchName, String percentage, double total) {
        this.branchName = branchName;
        this.percentage = percentage;
        this.total = total;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getPercentage() {
        return percentage;
    }

    public double getTotal() {
        return total;
    }
}

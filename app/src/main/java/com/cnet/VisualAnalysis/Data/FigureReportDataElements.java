package com.cnet.VisualAnalysis.Data;

public class FigureReportDataElements {
    public String summaryType;
    public int totalCount;
    public double grandTotal;

    public FigureReportDataElements(String summaryType, int totalCount, double grandTotal) {
        this.summaryType = summaryType;
        this.totalCount = totalCount;
        this.grandTotal = grandTotal;
    }

    public String getSummaryType() {
        return summaryType;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public double getGrandTotal() {
        return grandTotal;
    }
}

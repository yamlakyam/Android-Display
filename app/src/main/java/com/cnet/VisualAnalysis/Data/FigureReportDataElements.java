package com.cnet.VisualAnalysis.Data;

public class FigureReportDataElements {
    public String dateNTime;
    public int totalCount;
    public double grandTotal;
    public String org;

    public FigureReportDataElements(String dateNTime, int totalCount, double grandTotal, String org) {
        this.dateNTime = dateNTime;
        this.totalCount = totalCount;
        this.grandTotal = grandTotal;
        this.org = org;
    }

    public String getDateNTime() {
        return dateNTime;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public String getOrg() {
        return org;
    }
}

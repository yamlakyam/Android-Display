package com.cnet.VisualAnalysis.Data;

public class UserReportTableRow {

    public String summaryType;
    public int totalCount;
    public double subTotal;
    public double additionalCharge;
    public double discount;
    public double totalTaxAmt;
    public double grandTotal;
    public String org;

    public UserReportTableRow(String summaryType, int totalCount, double subTotal, double additionalCharge, double discount, double totalTaxAmt, double grandTotal, String org) {
        this.summaryType = summaryType;
        this.totalCount = totalCount;
        this.subTotal = subTotal;
        this.additionalCharge = additionalCharge;
        this.discount = discount;
        this.totalTaxAmt = totalTaxAmt;
        this.grandTotal = grandTotal;
        this.org = org;
    }

    public String getSummaryType() {
        return summaryType;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public double getAdditionalCharge() {
        return additionalCharge;
    }

    public double getDiscount() {
        return discount;
    }

    public double getTotalTaxAmt() {
        return totalTaxAmt;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public String getOrg() {
        return org;
    }
}

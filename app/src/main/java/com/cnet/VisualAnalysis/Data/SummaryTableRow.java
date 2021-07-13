package com.cnet.VisualAnalysis.Data;

public class SummaryTableRow {
    private String organizationName;
    private String startTimeStamp;
    private String endTimeStamp;
    private int vsiCount;
    private int salesOutLateCount;
    private int skuCount;
    private int quantityCount;
    private double totalSalesAmountAfterTax;
    private int activeVans;
    private int prospect;

    public SummaryTableRow(
            String organizationName, String startTimeStamp,
            String endTimeStamp, int vsiCount,
            int salesOutLateCount, int skuCount,
            int quantityCount, double totalSalesAmountAfterTax,
            int activeVans, int prospect) {
        this.organizationName = organizationName;
        this.startTimeStamp = startTimeStamp;
        this.endTimeStamp = endTimeStamp;
        this.vsiCount = vsiCount;
        this.salesOutLateCount = salesOutLateCount;
        this.skuCount = skuCount;
        this.quantityCount = quantityCount;
        this.totalSalesAmountAfterTax = totalSalesAmountAfterTax;
        this.activeVans = activeVans;
        this.prospect = prospect;
    }

    public String getOrganizationName() {
        return organizationName;
    }


    public String getStartTimeStamp() {
        return startTimeStamp;
    }


    public String getEndTimeStamp() {
        return endTimeStamp;
    }


    public int getVsiCount() {
        return vsiCount;
    }


    public int getSalesOutLateCount() {
        return salesOutLateCount;
    }


    public int getSkuCount() {
        return skuCount;
    }

    public int getQuantityCount() {
        return quantityCount;
    }

    public double getTotalSalesAmountAfterTax() {
        return totalSalesAmountAfterTax;
    }


    public int getActiveVans() {
        return activeVans;
    }


    public int getProspect() {
        return prospect;
    }


}

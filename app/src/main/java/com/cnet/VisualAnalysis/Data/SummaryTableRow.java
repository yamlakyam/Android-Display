package com.cnet.VisualAnalysis.Data;

public class SummaryTableRow {
    private String organizationName;
    private String startTimeStamp;
    private String endTimeStamp;
    private String vsiCount;
    private String salesOutLateCount;
    private String skuCount;
    private String quantityCount;
    private String totalSalesAmountAfterTax;
    private String activeVans;
    private String prospect;

    public SummaryTableRow(
            String organizationName, String startTimeStamp,
            String endTimeStamp, String vsiCount,
            String salesOutLateCount, String skuCount,
            String quantityCount, String totalSalesAmountAfterTax,
            String activeVans, String prospect) {
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

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(String startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public String getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(String endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public String getVsiCount() {
        return vsiCount;
    }

    public void setVsiCount(String vsiCount) {
        this.vsiCount = vsiCount;
    }

    public String getSalesOutLateCount() {
        return salesOutLateCount;
    }

    public void setSalesOutLateCount(String salesOutLateCount) {
        this.salesOutLateCount = salesOutLateCount;
    }

    public String getSkuCount() {
        return skuCount;
    }

    public void setSkuCount(String skuCount) {
        this.skuCount = skuCount;
    }

    public String getQuantityCount() {
        return quantityCount;
    }

    public void setQuantityCount(String quantityCount) {
        this.quantityCount = quantityCount;
    }

    public String getTotalSalesAmountAfterTax() {
        return totalSalesAmountAfterTax;
    }

    public void setTotalSalesAmountAfterTax(String totalSalesAmountAfterTax) {
        this.totalSalesAmountAfterTax = totalSalesAmountAfterTax;
    }

    public String getActiveVans() {
        return activeVans;
    }

    public void setActiveVans(String activeVans) {
        this.activeVans = activeVans;
    }

    public String getProspect() {
        return prospect;
    }

    public void setProspect(String prospect) {
        this.prospect = prospect;
    }
}

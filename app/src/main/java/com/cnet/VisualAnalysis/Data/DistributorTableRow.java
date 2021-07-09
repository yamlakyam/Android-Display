package com.cnet.VisualAnalysis.Data;

public class DistributorTableRow {
    private String nameOfOrg;
    private String vsi;
    private int salesOutLateCount;
    private int skuCount;
    private int quantityCount;
    private double totalSalesAmountAfterTax;
    private int prospect;
    private String startTimeStamp;
    private String endTimeStamp;

    public DistributorTableRow(String nameOfOrg, String vsi, int salesOutLateCount, int skuCount, int quantityCount, double totalSalesAmountAfterTax, int prospect, String startTimeStamp, String endTimeStamp) {
        this.nameOfOrg = nameOfOrg;
        this.vsi = vsi;
        this.salesOutLateCount = salesOutLateCount;
        this.skuCount = skuCount;
        this.quantityCount = quantityCount;
        this.totalSalesAmountAfterTax = totalSalesAmountAfterTax;
        this.prospect = prospect;
        this.startTimeStamp = startTimeStamp;
        this.endTimeStamp = endTimeStamp;
    }

    public String getNameOfOrg() {
        return nameOfOrg;
    }

    public void setNameOfOrg(String nameOfOrg) {
        this.nameOfOrg = nameOfOrg;
    }

    public String getVsi() {
        return vsi;
    }

    public void setVsi(String vsi) {
        this.vsi = vsi;
    }

    public int getSalesOutLateCount() {
        return salesOutLateCount;
    }

    public void setSalesOutLateCount(int salesOutLateCount) {
        this.salesOutLateCount = salesOutLateCount;
    }

    public int getSkuCount() {
        return skuCount;
    }

    public void setSkuCount(int skuCount) {
        this.skuCount = skuCount;
    }

    public int getQuantityCount() {
        return quantityCount;
    }

    public void setQuantityCount(int quantityCount) {
        this.quantityCount = quantityCount;
    }

    public double getTotalSalesAmountAfterTax() {
        return totalSalesAmountAfterTax;
    }

    public void setTotalSalesAmountAfterTax(double totalSalesAmountAfterTax) {
        this.totalSalesAmountAfterTax = totalSalesAmountAfterTax;
    }

    public int getProspect() {
        return prospect;
    }

    public void setProspect(int prospect) {
        this.prospect = prospect;
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
}

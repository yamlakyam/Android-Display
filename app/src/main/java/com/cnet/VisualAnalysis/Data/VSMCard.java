package com.cnet.VisualAnalysis.Data;

public class VSMCard {
   private String vsi;
   private int salesOutLateCount;
   private String lastSeen;
   private int vCount;
   private Double totalSales;
   private String distributorName;

    public VSMCard(String vsi, int salesOutLateCount, String lastSeen, int vCount, Double totalSales, String distributorName) {
        this.salesOutLateCount = salesOutLateCount;
        this.totalSales = totalSales;
        this.vsi = vsi;
        this.lastSeen = lastSeen;
        this.vCount = vCount;
        this.distributorName = distributorName;
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

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public int getvCount() {
        return vCount;
    }

    public void setvCount(int vCount) {
        this.vCount = vCount;
    }

    public Double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Double totalSales) {
        this.totalSales = totalSales;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorNAme(String distributorName) {
        this.distributorName = distributorName;
    }
}

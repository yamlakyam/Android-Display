package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class VsmTableDataForSingleVan {
    public String nameOfVan;
    public ArrayList<VsmTransactionTableRow> tableRows;
    public int salesOutLateCount;
    public String lastActive;
    public int allLineItemCount;
    public double totalPrice;


    public VsmTableDataForSingleVan(String nameOfVan, ArrayList<VsmTransactionTableRow> tableRows, int salesOutLateCount,
                                    String lastActive, int allLineItemCount, double totalPrice) {
        this.nameOfVan = nameOfVan;
        this.tableRows = tableRows;
        this.salesOutLateCount = salesOutLateCount;
        this.lastActive = lastActive;
        this.allLineItemCount = allLineItemCount;
        this.totalPrice = totalPrice;

    }

    public ArrayList<VsmTransactionTableRow> getTableRows() {
        return tableRows;
    }

    public void setTableRows(ArrayList<VsmTransactionTableRow> tableRows) {
        this.tableRows = tableRows;
    }

    public String getNameOfVan() {
        return nameOfVan;
    }

    public void setNameOfVan(String nameOfVan) {
        this.nameOfVan = nameOfVan;
    }

    public int getSalesOutLateCount() {
        return salesOutLateCount;
    }

    public String getLastActive() {
        return lastActive;
    }

    public int getAllLineItemCount() {
        return allLineItemCount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}

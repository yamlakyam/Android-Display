package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class VsmTableDataForSingleVan {
    public String nameOfVan;
    public ArrayList<VsmTransactionTableRow> tableRows;
//    public int salesOutLateCount;
//    public String lastActive;
//    public int allLineItemCount;
//    public double totalPrice;


    public VsmTableDataForSingleVan(String nameOfVan, ArrayList<VsmTransactionTableRow> tableRows) {
        this.nameOfVan = nameOfVan;
        this.tableRows = tableRows;
    }

    public String getNameOfVan() {
        return nameOfVan;
    }

    public ArrayList<VsmTransactionTableRow> getTableRows() {
        return tableRows;
    }
}

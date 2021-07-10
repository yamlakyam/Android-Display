package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class VsmTableDataForSingleVan {
    public String nameOfVan;
    public ArrayList<VsmTransactionTableRow> tableRows;

    public VsmTableDataForSingleVan(String nameOfVan, ArrayList<VsmTransactionTableRow> tableRows) {
        this.nameOfVan = nameOfVan;
        this.tableRows = tableRows;
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
}

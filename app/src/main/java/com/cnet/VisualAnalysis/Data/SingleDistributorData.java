package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class SingleDistributorData {
    private ArrayList<DistributorTableRow> tableData;
    private String nameOfOrg;

    public SingleDistributorData(ArrayList<DistributorTableRow> tableData, String nameOfOrg) {
        this.tableData = tableData;
        this.nameOfOrg = nameOfOrg;
    }

    public ArrayList<DistributorTableRow> getTableData() {
        return tableData;
    }

    public void setTableData(ArrayList<DistributorTableRow> tableData) {
        this.tableData = tableData;
    }

    public String getNameOfOrg() {
        return nameOfOrg;
    }

    public void setNameOfOrg(String nameOfOrg) {
        this.nameOfOrg = nameOfOrg;
    }
}
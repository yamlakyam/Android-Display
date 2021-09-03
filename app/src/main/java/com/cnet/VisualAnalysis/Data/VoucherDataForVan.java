package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class VoucherDataForVan {
    public String nameOfVan;
    public ArrayList<VoucherData> voucherDataArrayList;
    public double grandTotal;
    public int transactionCount;
    public int lineItems;

    public VoucherDataForVan(String nameOfVan, ArrayList<VoucherData> voucherDataArrayList, double grandTotal, int transactionCount, int lineItems) {
        this.nameOfVan = nameOfVan;
        this.voucherDataArrayList = voucherDataArrayList;
        this.grandTotal = grandTotal;
        this.transactionCount = transactionCount;
        this.lineItems = lineItems;

    }

    public String getNameOfVan() {
        return nameOfVan;
    }

    public ArrayList<VoucherData> getVoucherDataArrayList() {
        return voucherDataArrayList;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

}

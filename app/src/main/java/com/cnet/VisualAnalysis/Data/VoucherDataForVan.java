package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class VoucherDataForVan {
    public String nameOfVan;
    public ArrayList<VoucherData> voucherDataArrayList;
    public double grandTotal;
    public int countS;

    public VoucherDataForVan(String nameOfVan, ArrayList<VoucherData> voucherDataArrayList, double grandTotal, int countS) {
        this.nameOfVan = nameOfVan;
        this.voucherDataArrayList = voucherDataArrayList;
        this.grandTotal = grandTotal;
        this.countS=countS;
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

    public int getCountS() {
        return countS;
    }
}

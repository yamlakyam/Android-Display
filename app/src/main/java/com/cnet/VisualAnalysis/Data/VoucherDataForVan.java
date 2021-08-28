package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class VoucherDataForVan {
    public String nameOfVan;
    public ArrayList<VoucherData> voucherDataArrayList ;

    public VoucherDataForVan(String nameOfVan, ArrayList<VoucherData> voucherDataArrayList) {
        this.nameOfVan = nameOfVan;
        this.voucherDataArrayList = voucherDataArrayList;
    }

    public String getNameOfVan() {
        return nameOfVan;
    }

    public ArrayList<VoucherData> getVoucherDataArrayList() {
        return voucherDataArrayList;
    }
}

package com.cnet.VisualAnalysis.Data;

public class VoucherData {
    public String voucherNo;
    public String outlates;
    public double grandTotal;
    public double latitude;
    public double longitude;
    public double taxAmount;
    public String tin;
    public String dateAndTime;
    public double subTotal;
    public String username;
    public int itemCount;

    public VoucherData(String voucherNo, String outlates, double grandTotal, double latitude, double longitude, double taxAmount, String tin, String dateAndTime, double subTotal, String username, int itemCount) {
        this.voucherNo = voucherNo;
        this.outlates = outlates;
        this.grandTotal = grandTotal;
        this.latitude = latitude;
        this.longitude = longitude;
        this.taxAmount = taxAmount;
        this.tin = tin;
        this.dateAndTime = dateAndTime;
        this.subTotal = subTotal;
        this.username = username;
        this.itemCount = itemCount;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public String getOutlates() {
        return outlates;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public String getTin() {
        return tin;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public String getUsername() {
        return username;
    }

    public int getItemCount() {
        return itemCount;
    }


}

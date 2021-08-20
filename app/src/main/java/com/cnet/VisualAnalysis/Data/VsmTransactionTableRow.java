package com.cnet.VisualAnalysis.Data;

public class VsmTransactionTableRow {
    private String voucherNo;
    private String outlet;
    private String TIN;
    private String dateNtime;
    private int itemCount;
    private double subTotal;
    private double VAT;
    private double totalSales;
    private double latitude;
    private double longitude;
    private String username;

    public VsmTransactionTableRow(String voucherNo, String outlet,
                                  String TIN, String dateNtime,
                                  int itemCount, double subTotal,
                                  double VAT, double totalSales,
                                  double latitude, double longitude, String username) {
        this.voucherNo = voucherNo;
        this.outlet = outlet;
        this.TIN = TIN;
        this.dateNtime = dateNtime;
        this.itemCount = itemCount;
        this.subTotal = subTotal;
        this.VAT = VAT;
        this.totalSales = totalSales;
        this.latitude = latitude;
        this.longitude = longitude;
        this.username = username;

    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getOutlet() {
        return outlet;
    }

    public void setOutlet(String outlet) {
        this.outlet = outlet;
    }

    public String getTIN() {
        return TIN;
    }

    public void setTIN(String TIN) {
        this.TIN = TIN;
    }

    public String getDateNtime() {
        return dateNtime;
    }

    public void setDateNtime(String dateNtime) {
        this.dateNtime = dateNtime;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getVAT() {
        return VAT;
    }

    public void setVAT(double VAT) {
        this.VAT = VAT;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

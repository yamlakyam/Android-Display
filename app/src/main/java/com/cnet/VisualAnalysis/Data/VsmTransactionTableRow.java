package com.cnet.VisualAnalysis.Data;

public class VsmTransactionTableRow {
    private String voucherNo;
    private String outlet;
    private String TIN;
    private String dateNtime;
    private int itemCount;
    private double subTotal;
    private String VAT;
    private double totalSales;

    public VsmTransactionTableRow(String voucherNo, String outlet, String TIN, String dateNtime, int itemCount, double subTotal, String VAT, double totalSales) {
        this.voucherNo = voucherNo;
        this.outlet = outlet;
        this.TIN = TIN;
        this.dateNtime = dateNtime;
        this.itemCount = itemCount;
        this.subTotal = subTotal;
        this.VAT = VAT;
        this.totalSales = totalSales;
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

    public String getVAT() {
        return VAT;
    }

    public void setVAT(String VAT) {
        this.VAT = VAT;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }
}

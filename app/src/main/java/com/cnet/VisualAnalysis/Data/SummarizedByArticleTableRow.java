package com.cnet.VisualAnalysis.Data;

public class SummarizedByArticleTableRow {
    private String articleCode;
    private String articleName;
    private int quantity;
    private double avgAmount;
    private double totalAmount;
    private double totalServCharge;
    private double totalDiscount;
    private double taxAmount;

    public SummarizedByArticleTableRow(String articleCode, String articleName, int quantity,
                                       double avgAmount, double totalAmount, double totalServCharge,
                                       double totalDiscount, double taxAmount) {
        this.articleCode = articleCode;
        this.articleName = articleName;
        this.quantity = quantity;
        this.avgAmount = avgAmount;
        this.totalAmount = totalAmount;
        this.totalServCharge = totalServCharge;
        this.totalDiscount = totalDiscount;
        this.taxAmount = taxAmount;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public String getArticleName() {
        return articleName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getAvgAmount() {
        return avgAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getTotalServCharge() {
        return totalServCharge;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }
}

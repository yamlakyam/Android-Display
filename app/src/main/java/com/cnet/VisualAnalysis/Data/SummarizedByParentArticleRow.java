package com.cnet.VisualAnalysis.Data;

public class SummarizedByParentArticleRow {
    private double quantity;
    private double avgAmount;
    private double totalAmount;
    private double totalServCharge;
    private double totalDiscount;
    private double taxAmount;
    private String categoryType;


    public SummarizedByParentArticleRow(String categoryType,double quantity, double avgAmount, double totalAmount, double totalServCharge, double totalDiscount, double taxAmount) {
        this.quantity = quantity;
        this.avgAmount = avgAmount;
        this.totalAmount = totalAmount;
        this.totalServCharge = totalServCharge;
        this.totalDiscount = totalDiscount;
        this.taxAmount = taxAmount;
        this.categoryType =categoryType;
    }

    public double getQuantity() {
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

    public String getCategoryType() {
        return categoryType;
    }
}

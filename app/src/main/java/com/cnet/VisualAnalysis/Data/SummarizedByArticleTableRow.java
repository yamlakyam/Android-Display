package com.cnet.VisualAnalysis.Data;

public class SummarizedByArticleTableRow {
    String productName;
    int quantity;
    double unitAmount;
    double totalAmount;

    public SummarizedByArticleTableRow(String productName, int quantity, double unitAmount, double totalAmount) {
        this.productName = productName;
        this.quantity = quantity;
        this.unitAmount = unitAmount;
        this.totalAmount = totalAmount;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitAmount() {
        return unitAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}

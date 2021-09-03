package com.cnet.VisualAnalysis.Data;

public class SummaryOfLast30DaysRow {
    private String name;
    private double amount;
    private String dateTime;
    private int transactionCount;

    public SummaryOfLast30DaysRow(String name, double amount, String dateTime, int transactionCount) {
        this.name = name;
        this.amount = amount;
        this.dateTime = dateTime;
        this.transactionCount = transactionCount;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getTransactionCount() {
        return transactionCount;
    }
}

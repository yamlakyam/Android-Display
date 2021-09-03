package com.cnet.VisualAnalysis.Data;

public class SummaryOfLast6MonthsRow {
    private String name;
    private double amount;
    private String dateTime;
    private int transactionCount;

    public SummaryOfLast6MonthsRow(String name, double amount, String dateTime, int transactionCount) {
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

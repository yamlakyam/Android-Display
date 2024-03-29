package com.cnet.VisualAnalysis.Data;

public class SummaryOfLast6MonthsRow {
    private String name;
    private double amount;
    private String dateTime;

    public SummaryOfLast6MonthsRow(String name, double amount, String dateTime) {
        this.name = name;
        this.amount = amount;
        this.dateTime = dateTime;
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
}

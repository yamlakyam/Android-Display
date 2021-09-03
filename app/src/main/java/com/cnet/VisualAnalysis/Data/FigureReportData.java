package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class FigureReportData {
    public ArrayList<FigureReportDataElements> figureReportDataElementsArrayList;
    public LineChartData lineChartData1;
    public LineChartData lineChartData2;
    public String org;
    public double totalForThisVan;
    public PieChartData pieChartData;
    public BarChartData barChartData1;
    public BarChartData barChartData2;

    public FigureReportData(ArrayList<FigureReportDataElements> figureReportDataElementsArrayList, LineChartData lineChartData1, LineChartData lineChartData2, String org, double totalForThisVan,
                            PieChartData pieChartData, BarChartData barChartData1, BarChartData barChartData2) {
        this.figureReportDataElementsArrayList = figureReportDataElementsArrayList;
        this.lineChartData1 = lineChartData1;
        this.lineChartData2 = lineChartData2;
        this.org = org;
        this.totalForThisVan = totalForThisVan;
        this.pieChartData = pieChartData;
        this.barChartData1 = barChartData1;
        this.barChartData2 = barChartData2;
    }

    public ArrayList<FigureReportDataElements> getFigureReportDataElementsArrayList() {
        return figureReportDataElementsArrayList;
    }

    public String getOrg() {
        return org;
    }

    public LineChartData getLineChartData1() {
        return lineChartData1;
    }

    public LineChartData getLineChartData2() {
        return lineChartData2;
    }

    public double getTotalForThisVan() {
        return totalForThisVan;
    }

    public PieChartData getPieChartData() {
        return pieChartData;
    }

    public BarChartData getBarChartData1() {
        return barChartData1;
    }

    public BarChartData getBarChartData2() {
        return barChartData2;
    }
}

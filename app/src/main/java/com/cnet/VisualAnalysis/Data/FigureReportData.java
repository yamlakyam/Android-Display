package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class FigureReportData {
    public ArrayList<FigureReportDataElements> figureReportDataElementsArrayList;
    public LineChartData lineChartData;
    public String org;

    public FigureReportData(ArrayList<FigureReportDataElements> figureReportDataElementsArrayList, LineChartData lineChartData, String org) {
        this.figureReportDataElementsArrayList = figureReportDataElementsArrayList;
        this.lineChartData = lineChartData;
        this.org = org;
    }

    public ArrayList<FigureReportDataElements> getFigureReportDataElementsArrayList() {
        return figureReportDataElementsArrayList;
    }

    public LineChartData getLineChartData() {
        return lineChartData;
    }

    public String getOrg() {
        return org;
    }
}

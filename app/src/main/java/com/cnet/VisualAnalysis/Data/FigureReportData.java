package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class FigureReportData {
    public ArrayList<FigureReportDataElements> figureReportDataElementsArrayList;
    public LineChartData lineChartData1;
    public LineChartData lineChartData2;
    public String org;

    public FigureReportData(ArrayList<FigureReportDataElements> figureReportDataElementsArrayList, LineChartData lineChartData1, LineChartData lineChartData2, String org) {
        this.figureReportDataElementsArrayList = figureReportDataElementsArrayList;
        this.lineChartData1 = lineChartData1;
        this.lineChartData2 = lineChartData2;
        this.org = org;
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
}

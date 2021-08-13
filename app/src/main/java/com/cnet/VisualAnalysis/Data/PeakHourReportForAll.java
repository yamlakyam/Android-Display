package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class PeakHourReportForAll {
    public ArrayList<FigureReportDataElements> figureReportDataElements;
    public LineChartData lineChartData;

    public PeakHourReportForAll(ArrayList<FigureReportDataElements> figureReportDataElements, LineChartData lineChartData) {
        this.figureReportDataElements = figureReportDataElements;
        this.lineChartData = lineChartData;
    }

    public ArrayList<FigureReportDataElements> getFigureReportDataElements() {
        return figureReportDataElements;
    }

    public LineChartData getLineChartData() {
        return lineChartData;
    }
}

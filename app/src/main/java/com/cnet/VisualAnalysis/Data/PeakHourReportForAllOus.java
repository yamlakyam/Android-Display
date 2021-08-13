package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class PeakHourReportForAllOus {

 public ArrayList<FigureReportDataElements> figureReportDataElements;
 public LineChartData lineChartData;

    public PeakHourReportForAllOus(ArrayList<FigureReportDataElements> figureReportDataElements, LineChartData lineChartData) {
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

package com.cnet.VisualAnalysis.Data;

import java.util.Date;

public class LineChartData {
    public float[] x;
    public float[] y;
    public String[] legends;
    public Date[] x_labels;

    public LineChartData(float[] x, float[] y, String[] legends, Date[] x_labels) {
        this.x = x;
        this.y = y;
        this.legends = legends;
        this.x_labels =x_labels;
    }

    public float[] getX() {
        return x;
    }

    public float[] getY() {
        return y;
    }

    public String[] getLegends() {
        return legends;
    }

    public Date[] getX_labels() {
        return x_labels;
    }
}

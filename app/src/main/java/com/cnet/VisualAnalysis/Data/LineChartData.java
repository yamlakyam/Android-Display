package com.cnet.VisualAnalysis.Data;

public class LineChartData {
    public float[] x;
    public float[] y;

    public LineChartData(float[] x, float[] y) {
        this.x = x;
        this.y = y;
    }

    public float[] getX() {
        return x;
    }

    public float[] getY() {
        return y;
    }

}

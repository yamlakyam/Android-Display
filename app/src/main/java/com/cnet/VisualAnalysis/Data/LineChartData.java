package com.cnet.VisualAnalysis.Data;

public class LineChartData {
    public float[] x;
    public float[] y;
    public String[] legends;

    public LineChartData(float[] x, float[] y,String[] legends) {
        this.x = x;
        this.y = y;
        this.legends=legends;
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
}

package com.cnet.VisualAnalysis.Data;

public class PieChartData {
    public float [] x;
    public String [] y;

    public PieChartData(float[] x, String[] y) {
        this.x = x;
        this.y = y;
    }

    public float[] getX() {
        return x;
    }

    public String[] getY() {
        return y;
    }
}

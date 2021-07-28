package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class AllData {

    boolean enableNavigation;
    String transitionTimeInMinutes;
    ArrayList<Integer> layoutList;
    DashBoardData dashBoardData;
    FmcgData fmcgData;

    public boolean isEnableNavigation() {
        return enableNavigation;
    }

    public void setEnableNavigation(boolean enableNavigation) {
        this.enableNavigation = enableNavigation;
    }

    public String getTransitionTimeInMinutes() {
        return transitionTimeInMinutes;
    }

    public void setTransitionTimeInMinutes(String transitionTimeInMinutes) {
        this.transitionTimeInMinutes = transitionTimeInMinutes;
    }

    public ArrayList<Integer> getLayoutList() {
        return layoutList;
    }

    public void setLayoutList(ArrayList<Integer> layoutList) {
        this.layoutList = layoutList;
    }

    public DashBoardData getDashBoardData() {
        return dashBoardData;
    }

    public void setDashBoardData(DashBoardData dashBoardData) {
        this.dashBoardData = dashBoardData;
    }

    public FmcgData getFmcgData() {
        return fmcgData;
    }

    public void setFmcgData(FmcgData fmcgData) {
        this.fmcgData = fmcgData;
    }
}

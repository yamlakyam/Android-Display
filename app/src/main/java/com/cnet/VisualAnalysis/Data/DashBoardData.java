package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class DashBoardData {

    SummarizedByArticleData summarizedByArticleData;
    SummarizedByChildArticleData summarizedByChildArticleData;
    SummarizedByParentArticleData summarizedByParentArticleData;
    SummaryOfLast6MonthsData summaryOfLast6MonthsData;
    SummaryOfLast30DaysData summaryOfLast30DaysData;
    BranchSummaryData branchSummaryData;
    VsmTableForSingleDistributor vsmTableForSingleDistributor;

    ArrayList<UserReportDataForSingleOu> userReportForEachBranch;
    ArrayList<UserReportTableRow> userReportForAllBranch;

    ArrayList<FigureReportData> figureReportDataforEachBranch;
    ArrayList<FigureReportDataElements> figureReportDataforAllBranch;


    public SummarizedByArticleData getSummarizedByArticleData() {
        return summarizedByArticleData;
    }

    public void setSummarizedByArticleData(SummarizedByArticleData summarizedByArticleData) {
        this.summarizedByArticleData = summarizedByArticleData;
    }

    public SummarizedByChildArticleData getSummarizedByChildArticleData() {
        return summarizedByChildArticleData;
    }

    public void setSummarizedByChildArticleData(SummarizedByChildArticleData summarizedByChildArticleData) {
        this.summarizedByChildArticleData = summarizedByChildArticleData;
    }

    public SummarizedByParentArticleData getSummarizedByParentArticleData() {
        return summarizedByParentArticleData;
    }

    public void setSummarizedByParentArticleData(SummarizedByParentArticleData summarizedByParentArticleData) {
        this.summarizedByParentArticleData = summarizedByParentArticleData;
    }

    public SummaryOfLast6MonthsData getSummaryOfLast6MonthsData() {
        return summaryOfLast6MonthsData;
    }

    public void setSummaryOfLast6MonthsData(SummaryOfLast6MonthsData summaryOfLast6MonthsData) {
        this.summaryOfLast6MonthsData = summaryOfLast6MonthsData;
    }

    public SummaryOfLast30DaysData getSummaryOfLast30DaysData() {
        return summaryOfLast30DaysData;
    }

    public void setSummaryOfLast30DaysData(SummaryOfLast30DaysData summaryOfLast30DaysData) {
        this.summaryOfLast30DaysData = summaryOfLast30DaysData;
    }

    public BranchSummaryData getBranchSummaryData() {
        return branchSummaryData;
    }

    public void setBranchSummaryData(BranchSummaryData branchSummaryData) {
        this.branchSummaryData = branchSummaryData;
    }

    public VsmTableForSingleDistributor getVsmTableForSingleDistributor() {
        return vsmTableForSingleDistributor;
    }

    public void setVsmTableForSingleDistributor(VsmTableForSingleDistributor vsmTableForSingleDistributor) {
        this.vsmTableForSingleDistributor = vsmTableForSingleDistributor;
    }

    public ArrayList<UserReportDataForSingleOu> getUserReportForEachBranch() {
        return userReportForEachBranch;
    }

    public void setUserReportForEachBranch(ArrayList<UserReportDataForSingleOu> userReportForAllOus) {
        this.userReportForEachBranch = userReportForAllOus;
    }

    public ArrayList<FigureReportData> getFigureReportDataforEachBranch() {
        return figureReportDataforEachBranch;
    }

    public void setFigureReportDataforEachBranch(ArrayList<FigureReportData> figureReportDataforEachBranch) {
        this.figureReportDataforEachBranch = figureReportDataforEachBranch;
    }

    public ArrayList<UserReportTableRow> getUserReportForAllBranch() {
        return userReportForAllBranch;
    }

    public void setUserReportForAllBranch(ArrayList<UserReportTableRow> userReportForAllBranch) {
        this.userReportForAllBranch = userReportForAllBranch;
    }

    public ArrayList<FigureReportDataElements> getFigureReportDataforAllBranch() {
        return figureReportDataforAllBranch;
    }

    public void setFigureReportDataforAllBranch(ArrayList<FigureReportDataElements> figureReportDataforAllBranch) {
        this.figureReportDataforAllBranch = figureReportDataforAllBranch;
    }
}



package com.cnet.VisualAnalysis.Data;

public class DashBoardData {


    SummarizedByArticleData summarizedByArticleData;
    SummarizedByChildArticleData summarizedByChildArticleData;
    SummarizedByParentArticleData summarizedByParentArticleData;
    SummaryOfLast6MonthsData summaryOfLast6MonthsData;
    SummaryOfLast30DaysData summaryOfLast30DaysData;
    BranchSummaryData branchSummaryData;

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
}



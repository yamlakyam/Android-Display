package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class FmcgData {

    ArrayList<SummaryTableRow> summaryTableRows;
    ArrayList<SingleDistributorData> distributorTableRows;
    ArrayList<ArrayList<VSMCard>> vsmCards;
    ArrayList<VsmTableForSingleDistributor> vsmTableForSingleDistributors;

    public ArrayList<SummaryTableRow> getSummaryTableRows() {
        return summaryTableRows;
    }

    public void setSummaryTableRows(ArrayList<SummaryTableRow> summaryTableRows) {
        this.summaryTableRows = summaryTableRows;
    }

    public ArrayList<SingleDistributorData> getDistributorTableRows() {
        return distributorTableRows;
    }

    public void setDistributorTableRows(ArrayList<SingleDistributorData> distributorTableRows) {
        this.distributorTableRows = distributorTableRows;
    }

    public ArrayList<ArrayList<VSMCard>> getVsmCards() {
        return vsmCards;
    }

    public void setVsmCards(ArrayList<ArrayList<VSMCard>> vsmCards) {
        this.vsmCards = vsmCards;
    }

    public ArrayList<VsmTableForSingleDistributor> getVsmTableForSingleDistributors() {
        return vsmTableForSingleDistributors;
    }

    public void setVsmTableForSingleDistributors(ArrayList<VsmTableForSingleDistributor> vsmTableForSingleDistributors) {
        this.vsmTableForSingleDistributors = vsmTableForSingleDistributors;
    }
}

package com.cnet.VisualAnalysis.Data;

import java.util.ArrayList;

public class VsmTableForSingleDistributor {
   private final ArrayList<VsmTableDataForSingleVan> allVansData;
   private final String nameOfDistributor;

    public VsmTableForSingleDistributor(ArrayList<VsmTableDataForSingleVan> allVansData, String nameOfDistributor) {
        this.allVansData = allVansData;
        this.nameOfDistributor = nameOfDistributor;
    }

    public ArrayList<VsmTableDataForSingleVan> getAllVansData() {
        return allVansData;
    }

    public String getNameOfDistributor() {
        return nameOfDistributor;
    }
}

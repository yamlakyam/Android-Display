package com.cnet.VisualAnalysis.Utils;

import android.util.Log;

import com.cnet.VisualAnalysis.Data.DistributorTableRow;
import com.cnet.VisualAnalysis.Data.FmcgData;
import com.cnet.VisualAnalysis.Data.SingleDistributorData;
import com.cnet.VisualAnalysis.Data.SummaryTableRow;
import com.cnet.VisualAnalysis.Data.VSMCard;
import com.cnet.VisualAnalysis.Data.VsmTableDataForSingleVan;
import com.cnet.VisualAnalysis.Data.VsmTableForSingleDistributor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FmcgDataParser {

    JSONObject jsonObject;

    public FmcgDataParser(JSONArray jsonArray) {
        try {
            this.jsonObject = jsonArray.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public FmcgData parseFmcgData() throws JSONException {
        FmcgData fmcgData = new FmcgData();

        fmcgData.setSummaryTableRows(summaryTableParser(jsonObject));
        fmcgData.setDistributorTableRows(allDistributorTableData(jsonObject));
        fmcgData.setVsmCards(vsmCardParser(jsonObject));
        fmcgData.setVsmTableForSingleDistributors(vsmTransactionParser(jsonObject));

        return fmcgData;
    }

    public static ArrayList<SummaryTableRow> summaryTableParser(JSONObject jsonObject) throws JSONException {
        ArrayList<SummaryTableRow> tableData = new ArrayList<>();
        if (jsonObject != null) {
            JSONArray tableInJson = jsonObject.getJSONArray("getSalesDataForAllOrganizations");
            for (int i = 0; i < tableInJson.length(); i++) {
                JSONObject table = tableInJson.getJSONObject(i);

                tableData.add(
                        new SummaryTableRow(
                                table.getString("organizationName"),
                                new UtilityFunctionsForActivity1().formatTimeToString(table.getString("startTimeStamp")),
                                new UtilityFunctionsForActivity1().formatTimeToString(table.getString("endTimeStamp")),
                                table.getInt("vsiCount"),
                                table.getInt("salesOutLateCount"),
                                table.getInt("skuCount"),
                                table.getInt("quantityCount"),
                                table.getDouble("totalSalesAmountAfterTax"),
                                table.getInt("active"),
                                table.getInt("prospect")
                        )
                );
            }
        }
        return tableData;
    }


    public static ArrayList<SingleDistributorData> allDistributorTableData(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("getSalesDataForSingleOrganization");
        ArrayList<SingleDistributorData> allDistributorData = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject singleDisData = jsonArray.getJSONObject(i);
            JSONArray singleDisTableData = singleDisData.getJSONArray("organizationChartDataList");
            String nameOfOrg = singleDisData.getString("nameOfOrg");
            ArrayList<DistributorTableRow> distributorTableRows = new ArrayList<>();
            for (int j = 0; j < singleDisTableData.length(); j++) {
                JSONObject singleDisTableObject = singleDisTableData.getJSONObject(j);
                DistributorTableRow distributorTableRow = new DistributorTableRow(
                        nameOfOrg,
                        singleDisTableObject.getString("vsi"),
                        singleDisTableObject.getInt("salesOutLateCount"),
                        singleDisTableObject.getInt("skuCount"),
                        singleDisTableObject.getInt("quantityCount"),
                        singleDisTableObject.getDouble("totalSalesAmountAfterTax"),
                        singleDisTableObject.getInt("prospect"),
                        singleDisTableObject.getString("startTimeStamp"),
                        singleDisTableObject.getString("endTimeStamp")
                );
                distributorTableRows.add(distributorTableRow);

            }

            SingleDistributorData singleDistributorData = new SingleDistributorData(
                    distributorTableRows,
                    nameOfOrg
            );

            allDistributorData.add(singleDistributorData);
        }

        return allDistributorData;
    }

    public static ArrayList<DistributorTableRow> distributorTableParser(JSONObject jsonObject, int index) throws JSONException {
        ArrayList<DistributorTableRow> distributorTableData = new ArrayList<>();
        if (jsonObject != null) {
            JSONArray tableInJson = jsonObject.getJSONArray("getSalesDataForSingleOrganization");

            JSONObject singleDistributor = tableInJson.getJSONObject(index);

            String orgName = singleDistributor.getString("nameOfOrg");

            JSONArray tableData = singleDistributor.getJSONArray("organizationChartDataList");
            for (int j = 0; j < tableData.length(); j++) {
                JSONObject singleVan = tableData.getJSONObject(j);
                distributorTableData.add(new DistributorTableRow(orgName,
                        singleVan.getString("vsi"),
                        singleVan.getInt("salesOutLateCount"),
                        singleVan.getInt("skuCount"),
                        singleVan.getInt("quantityCount"),
                        singleVan.getDouble("totalSalesAmountAfterTax"),
                        singleVan.getInt("prospect"),
                        singleVan.getString("startTimeStamp"),
                        singleVan.getString("endTimeStamp")));
            }

            Log.i("COUNT", distributorTableData.size() + "");
        }

        return distributorTableData;

    }

    public static ArrayList<ArrayList<VSMCard>> vsmCardParser(JSONObject jsonObject) throws JSONException {

        ArrayList<ArrayList<VSMCard>> allVSMcards = new ArrayList<>();

        if (jsonObject != null) {
            JSONArray cardsInJSon = jsonObject.getJSONArray("getSalesDataToDisplayVsmCards");
            for (int i = 0; i < cardsInJSon.length(); i++) {
                ArrayList<VSMCard> vsmCardData = new ArrayList<>();
                JSONObject singleDistributor = cardsInJSon.getJSONObject(i);
                //change this to new method
                String distributorName;
                if (singleDistributor.getString("nameOfOrg").length() > 20) {
                    distributorName = singleDistributor.getString("nameOfOrg").substring(0, 15) + "...";
                } else
                    distributorName = singleDistributor.getString("nameOfOrg");

                JSONArray vans = singleDistributor.getJSONArray("vsmCards");

                for (int j = 0; j < vans.length(); j++) {

                    JSONObject van = vans.getJSONObject(j);

                    vsmCardData.add(new VSMCard(van.getString("vsm"),
                            van.getInt("salesOutLateCount"),
                            van.getString("lastActive"),
                            van.getInt("allLineItemCount"),
                            van.getDouble("totalSalesAmount"),
                            distributorName
                    ));
                }
                allVSMcards.add(vsmCardData);
            }
        }

        return allVSMcards;
    }

    public static ArrayList<VsmTableForSingleDistributor> vsmTransactionParser(JSONObject jsonObject) throws JSONException {
        ArrayList<VsmTableForSingleDistributor> dataForAllDis = new ArrayList<>();

        if (jsonObject != null) {
            JSONArray jsonArray = jsonObject.getJSONArray("getSalesDataToDisplayOnVsmTable");

            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject tableDataObjectForSingleOrgInJson = jsonArray.getJSONObject(i);
                    JSONArray tableDataForSingleOrgInJson = tableDataObjectForSingleOrgInJson.getJSONArray("vsmTables");
                    String distributorName = tableDataObjectForSingleOrgInJson.getString("orgName");

                    ArrayList<VsmTableDataForSingleVan> dataToDisplaySingleDis = new ArrayList<>();
                    for (int j = 0; j < tableDataForSingleOrgInJson.length(); j++) {
                        JSONObject tableDataObjectForSingleVanInJson = tableDataForSingleOrgInJson.getJSONObject(j);
//                        VsmTableDataForSingleVan dataToDisplaySingleVanTable =new UtilityFunctionsForActivity1().getSingleVanData(tableDataObjectForSingleVanInJson);
//                        dataToDisplaySingleDis.add(dataToDisplaySingleVanTable);
                    }

                    VsmTableForSingleDistributor vsmTableForSingleDistributor = new VsmTableForSingleDistributor(
                            dataToDisplaySingleDis,
                            distributorName
                    );

                    dataForAllDis.add(vsmTableForSingleDistributor);
                }
            }

        }

        return dataForAllDis;
    }

}

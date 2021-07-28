package com.cnet.VisualAnalysis.Utils;

import com.cnet.VisualAnalysis.Data.AllData;
import com.cnet.VisualAnalysis.Data.FmcgData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllDataParser {
    JSONObject jsonObject;

    public AllDataParser(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public AllData parseAllData() throws JSONException {
        AllData allData = new AllData();

        allData.setLayoutList(layoutListParser(jsonObject));

//        if(jsonObject.getJSONArray("consolidationObjectData").length()!=0){

        if(jsonObject.has("consolidationObjectData") && !jsonObject.isNull("consolidationObjectData")){
            allData.setFmcgData(new FmcgDataParser(jsonObject.getJSONArray("consolidationObjectData")).parseFmcgData());
        }
        if(jsonObject.has("dashBoardData") && !jsonObject.isNull("dashBoardData")){
            allData.setDashBoardData(new DashBoardDataParser(jsonObject.getJSONArray("dashBoardData")).parseDashBoardData());
        }
        if(jsonObject.has("enableNavigation") && !jsonObject.isNull("enableNavigation")){
            allData.setEnableNavigation(jsonObject.getBoolean("enableNavigation"));
        }
        if(jsonObject.has("transitionTimeInMinutes") && !jsonObject.isNull("transitionTimeInMinutes")){
            allData.setTransitionTimeInMinutes(jsonObject.getString("transitionTimeInMinutes"));
        }
        return allData;
    }

    public ArrayList<Integer> layoutListParser(JSONObject jsonObject) throws JSONException {
        JSONArray layoutLists = jsonObject.getJSONArray("layoutList");

        ArrayList<Integer> fragmentsToBeDisplayed = new ArrayList<>();
        for (int i = 0; i < layoutLists.length(); i++) {
            fragmentsToBeDisplayed.add(layoutLists.getInt(i));
        }
        return fragmentsToBeDisplayed;
    }
}
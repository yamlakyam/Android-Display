package com.cnet.VisualAnalysis.Utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.cnet.VisualAnalysis.Data.AllData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class AllDataParser {
    JSONObject jsonObject;

    public AllDataParser(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public AllData parseAllData() throws JSONException {
        AllData allData = new AllData();
//        allData.setLayoutList(layoutListParser(jsonObject));

        ArrayList<Integer> layoutList = new ArrayList<Integer>();
//        layoutList.addAll(Arrays.asList(1, 3, 4, 5, 6, 7, 8, 11, 12, 13));
        layoutList.addAll(Arrays.asList(1, 10, 12));
        allData.setLayoutList(layoutList);

        if (jsonObject.has("consolidationObjectData") && !jsonObject.isNull("consolidationObjectData") && jsonObject.getJSONArray("consolidationObjectData").length() > 0) {
            allData.setFmcgData(new FmcgDataParser(jsonObject.getJSONArray("consolidationObjectData")).parseFmcgData());
        }
        if (jsonObject.has("dashBoardData") && !jsonObject.isNull("dashBoardData")) {
            allData.setDashBoardData(new DashBoardDataParser(jsonObject.getJSONArray("dashBoardData")).parseDashBoardData());
        }
        if (jsonObject.has("enableNavigation") && !jsonObject.isNull("enableNavigation")) {
            allData.setEnableNavigation(jsonObject.getBoolean("enableNavigation"));
        }
        if (jsonObject.has("transitionTimeInSeconds") && !jsonObject.isNull("transitionTimeInSeconds")) {
            allData.setTransitionTimeInMinutes(jsonObject.getString("transitionTimeInSeconds"));
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

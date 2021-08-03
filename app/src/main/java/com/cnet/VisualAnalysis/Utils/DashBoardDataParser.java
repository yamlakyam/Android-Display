package com.cnet.VisualAnalysis.Utils;

import android.util.Log;

import com.cnet.VisualAnalysis.Data.BarChartData;
import com.cnet.VisualAnalysis.Data.BranchSummaryData;
import com.cnet.VisualAnalysis.Data.BranchSummaryTableRow;
import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Data.LineChartData;
import com.cnet.VisualAnalysis.Data.PieChartData;
import com.cnet.VisualAnalysis.Data.SummarizedByArticleData;
import com.cnet.VisualAnalysis.Data.SummarizedByArticleTableRow;
import com.cnet.VisualAnalysis.Data.SummarizedByChildArticleData;
import com.cnet.VisualAnalysis.Data.SummarizedByChildArticleRow;
import com.cnet.VisualAnalysis.Data.SummarizedByParentArticleData;
import com.cnet.VisualAnalysis.Data.SummarizedByParentArticleRow;
import com.cnet.VisualAnalysis.Data.SummaryOfLast30DaysData;
import com.cnet.VisualAnalysis.Data.SummaryOfLast30DaysRow;
import com.cnet.VisualAnalysis.Data.SummaryOfLast6MonthsData;
import com.cnet.VisualAnalysis.Data.SummaryOfLast6MonthsRow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashBoardDataParser {

    private static JSONArray jsonArray;

    private static final String TAG_summarized_by_article_list = "summarizedByArticleList";
    private static final String TAG_last_days_of_month_sale = "lastDaysOfMonthSale";
    private static final String TAG_last_months_of_year_sale = "lastMonthsOfYearSale";
    private static final String TAG_summarized_by_article_child_cate_list = "summarizedByArticleChildCategory";
    private static final String TAG_summarized_by_article_parent_cate_list = "summarizedByArticleParentCategory";

    public DashBoardDataParser(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public DashBoardData parseDashBoardData() {
        DashBoardData dashBoardData = new DashBoardData();
        try {
            JSONObject rootJSON = jsonArray.getJSONObject(0);

            Log.i("ROOT JSON", rootJSON.toString());

            dashBoardData.setSummarizedByArticleData(summarizedByArticleParser(rootJSON));
            dashBoardData.setSummarizedByParentArticleData(summarizedByParentArticleParser(rootJSON));
            dashBoardData.setSummarizedByChildArticleData(summarizedByChildArticleParser(rootJSON));
            dashBoardData.setSummaryOfLast6MonthsData(last6MonthsDataParser(rootJSON));
            dashBoardData.setSummaryOfLast30DaysData(last30DaysDataParser(rootJSON));
            dashBoardData.setBranchSummaryData(branchSummaryParser(rootJSON));

//            Log.i("ROOT JSON",  dashBoardData.getSummarizedByArticleData().tableData.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dashBoardData;

    }


    public static SummarizedByArticleData summarizedByArticleParser(JSONObject jsonObject) throws JSONException {
        JSONArray summaryOfArticle = jsonObject.getJSONArray(TAG_summarized_by_article_list);
        Log.i("JSON ARRAY", summaryOfArticle.toString());

        ArrayList<SummarizedByArticleTableRow> tableData = new ArrayList<>();
        float[] xValues = new float[summaryOfArticle.length()];
        float[] yValues = new float[summaryOfArticle.length()];
        String[] legends = new String[summaryOfArticle.length()];

        for (int i = 0; i < summaryOfArticle.length(); i++) {
            JSONObject summaryOfArticleAtInedx = summaryOfArticle.getJSONObject(i);

            SummarizedByArticleTableRow summarizedByArticleTableRow = new SummarizedByArticleTableRow(
                    summaryOfArticleAtInedx.getString("articleCode"),
                    summaryOfArticleAtInedx.getString("articleName"),
                    (int) summaryOfArticleAtInedx.getDouble("quantity"),
                    summaryOfArticleAtInedx.getDouble("avgAmount"),
                    summaryOfArticleAtInedx.getDouble("totalAmount"),
                    summaryOfArticleAtInedx.getDouble("totalServCharge"),
                    summaryOfArticleAtInedx.getDouble("totalDiscount"),
                    summaryOfArticleAtInedx.getDouble("taxAmount")
            );
            double grandTotal = summaryOfArticleAtInedx.getDouble("totalAmount") +
                    summaryOfArticleAtInedx.getDouble("totalServCharge") +
                    summaryOfArticleAtInedx.getDouble("taxAmount");

            tableData.add(summarizedByArticleTableRow);
            xValues[i] = i;
            yValues[i] = (float) grandTotal;
            legends[i] = summaryOfArticleAtInedx.getString("articleName");

        }

        BarChartData barChartData = new BarChartData(xValues, yValues, legends);
        LineChartData lineChartData = new LineChartData(xValues, yValues, legends);
        PieChartData pieChartData = new PieChartData(yValues, legends);

        SummarizedByArticleData summarizedByArticleData = new SummarizedByArticleData(tableData, barChartData, lineChartData, pieChartData);
        return summarizedByArticleData;
    }

    public static SummarizedByParentArticleData summarizedByParentArticleParser(JSONObject jsonObject) throws JSONException {
        JSONArray summaryOfParentArticle = jsonObject.getJSONArray(TAG_summarized_by_article_parent_cate_list);

        ArrayList<SummarizedByParentArticleRow> tableData = new ArrayList<>();
        float[] xValues = new float[summaryOfParentArticle.length()];
        float[] yValues = new float[summaryOfParentArticle.length()];
        String[] legends = new String[summaryOfParentArticle.length()];

        for (int i = 0; i < summaryOfParentArticle.length(); i++) {
            JSONObject summaryOfParentarticleAtInedx = summaryOfParentArticle.getJSONObject(i);

            SummarizedByParentArticleRow summarizedByParentArticleRow = new SummarizedByParentArticleRow(
                    summaryOfParentarticleAtInedx.getString("categoryType"),
                    summaryOfParentarticleAtInedx.getDouble("quantity"),
                    summaryOfParentarticleAtInedx.getDouble("avgAmount"),
                    summaryOfParentarticleAtInedx.getDouble("totalAmount"),
                    summaryOfParentarticleAtInedx.getDouble("totalServCharge"),
                    summaryOfParentarticleAtInedx.getDouble("totalDiscount"),
                    summaryOfParentarticleAtInedx.getDouble("taxAmount")
            );
            tableData.add(summarizedByParentArticleRow);
            xValues[i] = i;
            yValues[i] = (float) summaryOfParentarticleAtInedx.getDouble("totalAmount");
            legends[i] = summaryOfParentarticleAtInedx.getString("categoryType");

        }

        PieChartData pieChartData = new PieChartData(yValues, legends);
        BarChartData barChartData = new BarChartData(xValues, yValues, legends);


        SummarizedByParentArticleData summarizedByParentArticleData = new SummarizedByParentArticleData(tableData, barChartData, pieChartData);

        return summarizedByParentArticleData;
    }

    public static SummarizedByChildArticleData summarizedByChildArticleParser(JSONObject jsonObject) throws JSONException {
        JSONArray summaryOfChildArticle = jsonObject.getJSONArray(TAG_summarized_by_article_child_cate_list);

        ArrayList<SummarizedByChildArticleRow> tableData = new ArrayList<>();
        float[] xValues = new float[summaryOfChildArticle.length()];
        float[] yValues = new float[summaryOfChildArticle.length()];
        String[] legends = new String[summaryOfChildArticle.length()];

        for (int i = 0; i < summaryOfChildArticle.length(); i++) {
            JSONObject summaryOfChildArticleAtInedx = summaryOfChildArticle.getJSONObject(i);

            SummarizedByChildArticleRow summarizedByChildArticleRow = new SummarizedByChildArticleRow(
                    summaryOfChildArticleAtInedx.getString("categoryType"),
                    summaryOfChildArticleAtInedx.getDouble("quantity"),
                    summaryOfChildArticleAtInedx.getDouble("avgAmount"),
                    summaryOfChildArticleAtInedx.getDouble("totalAmount"),
                    summaryOfChildArticleAtInedx.getDouble("totalServCharge"),
                    summaryOfChildArticleAtInedx.getDouble("totalDiscount"),
                    summaryOfChildArticleAtInedx.getDouble("taxAmount")
            );
            tableData.add(summarizedByChildArticleRow);
            xValues[i] = i;
            yValues[i] = (float) summaryOfChildArticleAtInedx.getDouble("totalAmount");
            legends[i] = summaryOfChildArticleAtInedx.getString("categoryType");

        }

        PieChartData pieChartData = new PieChartData(yValues, legends);
        BarChartData barChartData = new BarChartData(xValues, yValues, legends);


        SummarizedByChildArticleData summarizedByChildArticleData = new SummarizedByChildArticleData(tableData, barChartData, pieChartData);

        return summarizedByChildArticleData;
    }

    public static SummaryOfLast6MonthsData last6MonthsDataParser(JSONObject jsonObject) throws JSONException {
        JSONArray last6MonthsSale = jsonObject.getJSONArray(TAG_last_months_of_year_sale);

        ArrayList<SummaryOfLast6MonthsRow> tableData = new ArrayList<>();
        float[] xValues = new float[last6MonthsSale.length()];
        float[] yValues = new float[last6MonthsSale.length()];
        String[] legends = new String[last6MonthsSale.length()];

        for (int i = 0; i < last6MonthsSale.length(); i++) {
            JSONObject last6MonsSummaryAtInedx = last6MonthsSale.getJSONObject(i);

            SummaryOfLast6MonthsRow summaryOfLast6MonthsRow = new SummaryOfLast6MonthsRow(
                    last6MonsSummaryAtInedx.getString("name"),
                    last6MonsSummaryAtInedx.getDouble("amount"),
                    last6MonsSummaryAtInedx.getString("dateTime")
            );
            tableData.add(summaryOfLast6MonthsRow);
            xValues[i] = i + 1;
            yValues[i] = (float) last6MonsSummaryAtInedx.getDouble("amount");
            legends[i] = last6MonsSummaryAtInedx.getString("name");


        }

        PieChartData pieChartData = new PieChartData(yValues, legends);
        BarChartData barChartData = new BarChartData(xValues, yValues, legends);


        SummaryOfLast6MonthsData summaryOfLast6MonthsData = new SummaryOfLast6MonthsData(pieChartData, tableData, barChartData);

        return summaryOfLast6MonthsData;
    }

    public static SummaryOfLast30DaysData last30DaysDataParser(JSONObject jsonObject) throws JSONException {
        JSONArray lastDaysOfMonthSale = jsonObject.getJSONArray(TAG_last_days_of_month_sale);

        ArrayList<SummaryOfLast30DaysRow> tableData = new ArrayList<>();
        float[] xValues = new float[lastDaysOfMonthSale.length()];
        float[] yValues = new float[lastDaysOfMonthSale.length()];
        String[] legends = new String[lastDaysOfMonthSale.length()];

        for (int i = 0; i < lastDaysOfMonthSale.length(); i++) {
            JSONObject summaryAtIndex = lastDaysOfMonthSale.getJSONObject(i);

            SummaryOfLast30DaysRow summaryOfLast30DaysRow = new SummaryOfLast30DaysRow(
                    summaryAtIndex.getString("name"),
                    summaryAtIndex.getDouble("amount"),
                    summaryAtIndex.getString("dateTime")
            );

            tableData.add(summaryOfLast30DaysRow);
            xValues[i] = i + 1;
            yValues[i] = (float) summaryAtIndex.getDouble("amount");
            legends[i] = summaryAtIndex.getString("name");

        }


        LineChartData lineChartData = new LineChartData(xValues, yValues, legends);
        BarChartData barChartData = new BarChartData(xValues, yValues, legends);
        SummaryOfLast30DaysData summaryOfLast30DaysData = new SummaryOfLast30DaysData(lineChartData, barChartData, tableData);

        return summaryOfLast30DaysData;
    }

    public static BranchSummaryData branchSummaryParser(JSONObject jsonObject) throws JSONException {

        JSONArray summaryOfBranch = jsonObject.getJSONArray("orgUnitSales");

        ArrayList<BranchSummaryTableRow> tableData = new ArrayList<>();

        for (int i = 0; i < summaryOfBranch.length(); i++) {
            JSONObject branchSummaryAtInedx = summaryOfBranch.getJSONObject(i);

            BranchSummaryTableRow branchSummaryTableRow = new BranchSummaryTableRow(
                    branchSummaryAtInedx.getString("org"),
                    branchSummaryAtInedx.getDouble("grandTotal"),
                    branchSummaryAtInedx.getInt("countS")

            );

            tableData.add(branchSummaryTableRow);

        }


        BranchSummaryData branchSummaryData = new BranchSummaryData(tableData);
        return branchSummaryData;
    }


}




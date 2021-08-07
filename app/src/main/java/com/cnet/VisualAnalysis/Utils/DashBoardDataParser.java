package com.cnet.VisualAnalysis.Utils;

import com.cnet.VisualAnalysis.Data.BarChartData;
import com.cnet.VisualAnalysis.Data.BranchSummaryData;
import com.cnet.VisualAnalysis.Data.BranchSummaryTableRow;
import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Data.FigureReportData;
import com.cnet.VisualAnalysis.Data.FigureReportDataElements;
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
import com.cnet.VisualAnalysis.Data.UserReportDataForSingleOu;
import com.cnet.VisualAnalysis.Data.UserReportTableRow;
import com.cnet.VisualAnalysis.Data.VsmTableDataForSingleVan;
import com.cnet.VisualAnalysis.Data.VsmTableForSingleDistributor;
import com.cnet.VisualAnalysis.Data.VsmTransactionTableRow;
import com.cnet.VisualAnalysis.Fragments.PeakHourReportFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

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

//            Log.i("ROOT JSON", rootJSON.toString());

            dashBoardData.setSummarizedByArticleData(summarizedByArticleParser(rootJSON));
            dashBoardData.setSummarizedByParentArticleData(summarizedByParentArticleParser(rootJSON));
            dashBoardData.setSummarizedByChildArticleData(summarizedByChildArticleParser(rootJSON));
            dashBoardData.setSummaryOfLast6MonthsData(last6MonthsDataParser(rootJSON));
            dashBoardData.setSummaryOfLast30DaysData(last30DaysDataParser(rootJSON));
            dashBoardData.setBranchSummaryData(branchSummaryParser(rootJSON));
//            dashBoardData.setVsmTableForSingleDistributor(vsmTransactionForSingleCompanyParser(rootJSON));
            dashBoardData.setUserReportForEachBranch(userReportForEachOuDataParser(rootJSON));
            dashBoardData.setAllFigureReportData(figureReportDataParser(rootJSON));
            dashBoardData.setUserReportForAllBranch(userReportAllTogetherOuDataParser(rootJSON));

//            dashBoardData.setAllFigureReportData(filteredFigureReportDataParser(parseFilteredFigureReport(rootJSON)));
            parseFilteredFigureReport(rootJSON);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dashBoardData;

    }


    public static SummarizedByArticleData summarizedByArticleParser(JSONObject jsonObject) throws JSONException {
        JSONArray summaryOfArticle = jsonObject.getJSONArray(TAG_summarized_by_article_list);
//        Log.i("JSON ARRAY", summaryOfArticle.toString());

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
//        LineChartData lineChartData = new LineChartData(xValues, yValues, legends);
        PieChartData pieChartData = new PieChartData(yValues, legends);

        SummarizedByArticleData summarizedByArticleData = new SummarizedByArticleData(tableData, barChartData, pieChartData);
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


//        LineChartData lineChartData = new LineChartData(xValues, yValues, legends);
        BarChartData barChartData = new BarChartData(xValues, yValues, legends);
        SummaryOfLast30DaysData summaryOfLast30DaysData = new SummaryOfLast30DaysData(barChartData, tableData);

        return summaryOfLast30DaysData;
    }

    public static BranchSummaryData branchSummaryParser(JSONObject jsonObject) throws JSONException {

        JSONArray summaryOfBranch = jsonObject.getJSONArray("orgUnitSales");

        ArrayList<BranchSummaryTableRow> tableData = new ArrayList<>();

        float[] xValues = new float[summaryOfBranch.length()];
        float[] yValues = new float[summaryOfBranch.length()];
        String[] legends = new String[summaryOfBranch.length()];

        for (int i = 0; i < summaryOfBranch.length(); i++) {
            JSONObject branchSummaryAtInedx = summaryOfBranch.getJSONObject(i);

            BranchSummaryTableRow branchSummaryTableRow = new BranchSummaryTableRow(
                    branchSummaryAtInedx.getString("org"),
                    branchSummaryAtInedx.getDouble("grandTotal"),
                    branchSummaryAtInedx.getInt("countS")

            );

            tableData.add(branchSummaryTableRow);
            xValues[i] = i + 1;
            yValues[i] = (float) branchSummaryAtInedx.getDouble("grandTotal");
            legends[i] = branchSummaryAtInedx.getString("org");

        }

        PieChartData pieChartData = new PieChartData(yValues, legends);
        BranchSummaryData branchSummaryData = new BranchSummaryData(tableData, pieChartData);
        return branchSummaryData;
    }

    public static ArrayList<UserReportDataForSingleOu> userReportForEachOuDataParser(JSONObject jsonObject) throws JSONException {
//        Log.i("json obj",jsonObject+"");

//        Log.i("jsonArray", jsonObject.has("orgUnitSales") + "");
        JSONArray summaryOfBranchArray = jsonObject.getJSONArray("orgUnitSales");
        ArrayList<UserReportDataForSingleOu> userReportDataForSingleOuArrayList = new ArrayList<>();
        for (int i = 0; i < summaryOfBranchArray.length(); i++) {
            JSONArray userReportArray = summaryOfBranchArray.getJSONObject(i).getJSONArray("userReport");
            String org = summaryOfBranchArray.getJSONObject(i).getString("org");
            ArrayList<UserReportTableRow> userReportTableRowArrayList = new ArrayList<>();

            float[] xValues = new float[userReportArray.length()];
            float[] yValues = new float[userReportArray.length()];
            String[] legends = new String[userReportArray.length()];

            for (int j = 0; j < userReportArray.length(); j++) {
                JSONObject userReportForSingleOu = userReportArray.getJSONObject(j);
                UserReportTableRow userReportTableRow = new UserReportTableRow(userReportForSingleOu.getString("summaryType"),
                        userReportForSingleOu.getInt("totalCount"), userReportForSingleOu.getDouble("subTotal"),
                        userReportForSingleOu.getDouble("additionalCharge"), userReportForSingleOu.getDouble("discount"),
                        userReportForSingleOu.getDouble("totalTaxAmt"), userReportForSingleOu.getDouble("grandTotal"), org);
                userReportTableRowArrayList.add(userReportTableRow);

                xValues[j] = j + 1;
                yValues[j] = (float) userReportForSingleOu.getDouble("grandTotal");
                legends[j] = userReportForSingleOu.getString("summaryType");
            }

            PieChartData pieChartData = new PieChartData(yValues, legends);
            UserReportDataForSingleOu userReportDataForSingleOu = new UserReportDataForSingleOu(userReportTableRowArrayList, pieChartData, org);
            userReportDataForSingleOuArrayList.add(userReportDataForSingleOu);
        }
        return userReportDataForSingleOuArrayList;
    }

    public static ArrayList<UserReportTableRow> userReportAllTogetherOuDataParser(JSONObject jsonObject) throws JSONException {

        JSONArray summaryOfBranchArray = jsonObject.getJSONArray("orgUnitSales");
        ArrayList<UserReportTableRow> userReportTableRowArrayList = new ArrayList<>();

        for (int i = 0; i < summaryOfBranchArray.length(); i++) {
            JSONArray userReportArray = summaryOfBranchArray.getJSONObject(i).getJSONArray("userReport");
            String org = summaryOfBranchArray.getJSONObject(i).getString("org");
            for (int j = 0; j < userReportArray.length(); j++) {
                JSONObject userReportForSingleOu = userReportArray.getJSONObject(j);
                UserReportTableRow userReportTableRow = new UserReportTableRow(userReportForSingleOu.getString("summaryType"),
                        userReportForSingleOu.getInt("totalCount"), userReportForSingleOu.getDouble("subTotal"),
                        userReportForSingleOu.getDouble("additionalCharge"), userReportForSingleOu.getDouble("discount"),
                        userReportForSingleOu.getDouble("totalTaxAmt"), userReportForSingleOu.getDouble("grandTotal"), org);
                userReportTableRowArrayList.add(userReportTableRow);

            }
        }

        return userReportTableRowArrayList;
    }

    public static ArrayList<FigureReportData> figureReportDataParser(JSONObject jsonObject) throws JSONException {
        JSONArray summaryOfBranchArray = jsonObject.getJSONArray("orgUnitSales");

        ArrayList<FigureReportData> figureReportDataArrayList = new ArrayList<>();
        ArrayList<FigureReportData> figureReportDataArrayListFiltered = new ArrayList<>();

        for (int i = 0; i < summaryOfBranchArray.length(); i++) {

            String org = summaryOfBranchArray.getJSONObject(i).getString("org");

            JSONArray figureReportArray = summaryOfBranchArray.getJSONObject(i).getJSONArray("figureReport");
            ArrayList<FigureReportDataElements> figureReportDataElementsArrayList = new ArrayList<>();
            ArrayList<FigureReportDataElements> figureReportDataElementsFiltered = new ArrayList<>();

            float[] xValues = new float[figureReportArray.length()];
            float[] yValues = new float[figureReportArray.length()];
            String[] legends = new String[figureReportArray.length()];

            int count = 0;
            for (int j = 0; j < figureReportArray.length(); j++) {
                JSONObject singleFigureReportObject = figureReportArray.getJSONObject(j);

                FigureReportDataElements figureReportDataElements = new FigureReportDataElements(singleFigureReportObject.getString("summaryType"),
                        singleFigureReportObject.getInt("totalCount"), singleFigureReportObject.getDouble("grandTotal"), org);
                figureReportDataElementsArrayList.add(figureReportDataElements);

                xValues[j] = j + 1;
                yValues[j] = (float) singleFigureReportObject.getDouble("grandTotal");
                legends[j] = singleFigureReportObject.getString("summaryType");

//                if (PeakHourReportFragment.convertToTime(singleFigureReportObject.getString("summaryType")).getMonth() == Calendar.MAY
//                ) {
//
//                    FigureReportDataElements figureReportDataElementsFilterd = new FigureReportDataElements(singleFigureReportObject.getString("summaryType"),
//                            singleFigureReportObject.getInt("totalCount"), singleFigureReportObject.getDouble("grandTotal"), org);
//                    figureReportDataElementsFiltered.add(figureReportDataElementsFilterd);
//
//                }
            }

//            for (int k = 0; k < figureReportDataElementsFiltered.size(); k++) {
//                xValues[k] = k + 1;
//                yValues[k] = (float) figureReportDataElementsFiltered.get(k).grandTotal;
//                legends[k] = figureReportDataElementsFiltered.get(k).dateNTime;
//            }

            LineChartData lineChartData = new LineChartData(xValues, yValues, legends);
            FigureReportData figureReportData = new FigureReportData(figureReportDataElementsArrayList, lineChartData, org);
            figureReportDataArrayList.add(figureReportData);

//            LineChartData lineChartDataFiltered = new LineChartData(xValues, yValues, legends);
//            FigureReportData figureReportData = new FigureReportData(figureReportDataElementsFiltered, lineChartDataFiltered, org);
//            figureReportDataArrayListFiltered.add(figureReportData);

        }
//        return figureReportDataArrayListFiltered;
        return figureReportDataArrayList;
    }

    public static ArrayList<ArrayList<FigureReportDataElements>> parseFilteredFigureReport(JSONObject jsonObject) throws JSONException {
        JSONArray summaryOfBranchArray = jsonObject.getJSONArray("orgUnitSales");
        ArrayList<FigureReportData> figureReportDataArrayListFiltered = new ArrayList<>();

        ArrayList<ArrayList<FigureReportDataElements>> allFigureReportDataElementsFiltered = new ArrayList<>();

        for (int i = 0; i < summaryOfBranchArray.length(); i++) {

            String org = summaryOfBranchArray.getJSONObject(i).getString("org");

            JSONArray figureReportArray = summaryOfBranchArray.getJSONObject(i).getJSONArray("figureReport");
            ArrayList<FigureReportDataElements> figureReportDataElementsFiltered = new ArrayList<>();

            float[] xValues = new float[figureReportArray.length()];
            float[] yValues = new float[figureReportArray.length()];
            String[] legends = new String[figureReportArray.length()];

            int count = 0;
            for (int j = 0; j < figureReportArray.length(); j++) {
                JSONObject singleFigureReportObject = figureReportArray.getJSONObject(j);

//                FigureReportDataElements figureReportDataElements = new FigureReportDataElements(singleFigureReportObject.getString("summaryType"),
//                        singleFigureReportObject.getInt("totalCount"), singleFigureReportObject.getDouble("grandTotal"));
//                figureReportDataElementsArrayList.add(figureReportDataElements);

//                xValues[j] = j + 1;
//                yValues[j] = (float) singleFigureReportObject.getDouble("grandTotal");
//                legends[j] = singleFigureReportObject.getString("summaryType");
//                x_legends[j] = UtilityFunctionsForActivity1.peakHourFormatter(singleFigureReportObject.getString("summaryType"));

                if (PeakHourReportFragment.convertToTime(singleFigureReportObject.getString("summaryType")).getMonth() == Calendar.MAY
                ) {

                    FigureReportDataElements figureReportDataElementsFilterd = new FigureReportDataElements(singleFigureReportObject.getString("summaryType"),
                            singleFigureReportObject.getInt("totalCount"), singleFigureReportObject.getDouble("grandTotal"), org);
                    figureReportDataElementsFiltered.add(figureReportDataElementsFilterd);

                }
            }

            allFigureReportDataElementsFiltered.add(figureReportDataElementsFiltered);
        }
        return allFigureReportDataElementsFiltered;
    }

    public static VsmTableForSingleDistributor vsmTransactionForSingleCompanyParser(JSONObject jsonObject) throws JSONException {
        JSONArray vsmTransactionArray = jsonObject.getJSONArray("getSalesDataToDisplayOnVsmTable");

        JSONObject vsmTransactionObject = vsmTransactionArray.getJSONObject(0);

//        VsmTableForSingleDistributor dataForAdistributor = new VsmTableForSingleDistributor();
        JSONArray vsmTables = vsmTransactionObject.getJSONArray("vsmTables");
        String orgName = vsmTransactionObject.getString("orgName");

        ArrayList<VsmTableDataForSingleVan> allVans = new ArrayList<VsmTableDataForSingleVan>();

        for (int i = 0; i < vsmTables.length(); i++) {
            JSONObject tableDataObjectForSingleVanInJson = vsmTables.getJSONObject(i);

            JSONArray transactionsOfaVan = tableDataObjectForSingleVanInJson.getJSONArray("tableRows");
            String vanName = tableDataObjectForSingleVanInJson.getString("van");

            ArrayList<VsmTransactionTableRow> singleTransactionOfaVan = new ArrayList<VsmTransactionTableRow>();
            for (int j = 0; j < transactionsOfaVan.length(); j++) {
                JSONObject singleTransactionObject = transactionsOfaVan.getJSONObject(j);

                VsmTransactionTableRow vsmTransactionTableRow = new VsmTransactionTableRow(
                        singleTransactionObject.getString("voucherNo"),
                        singleTransactionObject.getString("outlates"),
                        singleTransactionObject.getString("tin"),
                        singleTransactionObject.getString("dateAndTime"),
                        (int) singleTransactionObject.getDouble("itemCount"),
                        singleTransactionObject.getDouble("subTotal"),
                        singleTransactionObject.getDouble("vat"),
                        singleTransactionObject.getDouble("grandTotal"),
                        singleTransactionObject.getDouble("latitude"),
                        singleTransactionObject.getDouble("longitude"));

                singleTransactionOfaVan.add(vsmTransactionTableRow);
            }

            VsmTableDataForSingleVan vsmTableDataForSingleVan = new VsmTableDataForSingleVan(vanName, singleTransactionOfaVan);
            allVans.add(vsmTableDataForSingleVan);

        }

        VsmTableForSingleDistributor vsmTableForSingleDistributor = new VsmTableForSingleDistributor(allVans, orgName);
        return vsmTableForSingleDistributor;
    }

    public static ArrayList<FigureReportData> filteredFigureReportDataParser(ArrayList<ArrayList<FigureReportDataElements>> filteredUserReport) {

        ArrayList<FigureReportData> figureReportDataArrayList = new ArrayList<>();
        for (int i = 0; i < filteredUserReport.size(); i++) {

            ArrayList<FigureReportDataElements> figureReportDataElementsArrayList = new ArrayList<>();

            float[] xValues = new float[filteredUserReport.get(i).size()];
            float[] yValues = new float[filteredUserReport.get(i).size()];
            String[] legends = new String[filteredUserReport.get(i).size()];

            for (int j = 0; j < filteredUserReport.get(i).size(); j++) {
                FigureReportDataElements figureReportDataElements = new FigureReportDataElements(filteredUserReport.get(i).get(j).dateNTime,
                        filteredUserReport.get(i).get(j).totalCount, filteredUserReport.get(i).get(j).grandTotal, filteredUserReport.get(i).get(j).org);
                figureReportDataElementsArrayList.add(figureReportDataElements);

                xValues[j] = j + 1;
                yValues[j] = (float) filteredUserReport.get(i).get(j).grandTotal;
                legends[j] = filteredUserReport.get(i).get(j).dateNTime;
            }

            LineChartData lineChartData = new LineChartData(xValues, yValues, legends);
            FigureReportData figureReportData = new FigureReportData(figureReportDataElementsArrayList, lineChartData, figureReportDataElementsArrayList.get(i).org);
            figureReportDataArrayList.add(figureReportData);

        }
        return figureReportDataArrayList;
    }

}




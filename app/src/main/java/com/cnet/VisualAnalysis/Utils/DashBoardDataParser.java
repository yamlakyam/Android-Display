package com.cnet.VisualAnalysis.Utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

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
import com.cnet.VisualAnalysis.Data.VoucherData;
import com.cnet.VisualAnalysis.Data.VoucherDataForVan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public DashBoardData parseDashBoardData() {
        DashBoardData dashBoardData = new DashBoardData();
        try {
            JSONObject rootJSON = jsonArray.getJSONObject(0);

            parseAllDashBoardDataCatching(dashBoardData, rootJSON);

//            dashBoardData.setSummarizedByArticleData(summarizedByArticleParser(rootJSON));
//            dashBoardData.setSummarizedByParentArticleData(summarizedByParentArticleParser(rootJSON));
//            dashBoardData.setSummarizedByChildArticleData(summarizedByChildArticleParser(rootJSON));
//            dashBoardData.setSummaryOfLast6MonthsData(last6MonthsDataParser(rootJSON));
//            dashBoardData.setSummaryOfLast30DaysData(last30DaysDataParser(rootJSON));
//            dashBoardData.setBranchSummaryData(branchSummaryParser(rootJSON));
//            dashBoardData.setVsmTableForSingleDistributor(vsmTransactionForSingleCompanyParser(rootJSON));
//            dashBoardData.setUserReportForEachBranch(userReportForEachOuDataParser(rootJSON));
//            dashBoardData.setFigureReportDataforEachBranch(figureReportDataParser(rootJSON));
//            dashBoardData.setUserReportForAllBranch(userReportAllTogetherOuDataParser(rootJSON));
//            dashBoardData.setFigureReportDataforAllBranch(figureReportAllTogetherOuDataParser(rootJSON));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dashBoardData;

    }


    public static SummarizedByArticleData summarizedByArticleParser(JSONObject jsonObject) throws JSONException {
        JSONArray summaryOfArticle = jsonObject.getJSONArray(TAG_summarized_by_article_list);
//        Log.i("JSON ARRAY", summaryOfArticle.toString());
        if (summaryOfArticle.length() > 0) {
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

            SummarizedByArticleData summarizedByArticleData = new SummarizedByArticleData(tableData, barChartData, pieChartData, lineChartData);
            return summarizedByArticleData;
        }
        return new SummarizedByArticleData(new ArrayList<SummarizedByArticleTableRow>(), new BarChartData(new float[0], new float[0], new String[0]),
                new PieChartData(new float[0], new String[0]), new LineChartData(new float[0], new float[0], new String[0]));
    }

    public static SummarizedByParentArticleData summarizedByParentArticleParser(JSONObject jsonObject) throws JSONException {
        JSONArray summaryOfParentArticle = jsonObject.getJSONArray(TAG_summarized_by_article_parent_cate_list);

        if (summaryOfParentArticle.length() > 0) {
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
            LineChartData lineChartData = new LineChartData(xValues, yValues, legends);


            SummarizedByParentArticleData summarizedByParentArticleData = new SummarizedByParentArticleData(tableData, barChartData, pieChartData, lineChartData);

            return summarizedByParentArticleData;
        }
        return new SummarizedByParentArticleData(new ArrayList<SummarizedByParentArticleRow>(), new BarChartData(new float[0],
                new float[0], new String[0]), new PieChartData(new float[0], new String[0]), new LineChartData(new float[0],
                new float[0], new String[0]));
    }

    public static SummarizedByChildArticleData summarizedByChildArticleParser(JSONObject jsonObject) throws JSONException {
        JSONArray summaryOfChildArticle = jsonObject.getJSONArray(TAG_summarized_by_article_child_cate_list);

        if (summaryOfChildArticle.length() > 0) {
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
            LineChartData lineChartData = new LineChartData(xValues, yValues, legends);


            SummarizedByChildArticleData summarizedByChildArticleData = new SummarizedByChildArticleData(tableData, barChartData, pieChartData, lineChartData);

            return summarizedByChildArticleData;
        }
        return new SummarizedByChildArticleData(new ArrayList<SummarizedByChildArticleRow>(), new BarChartData(new float[0], new float[0], new String[0]), new PieChartData(new float[0], new String[0])
                , new LineChartData(new float[0], new float[0], new String[0]));
    }

    public static SummaryOfLast6MonthsData last6MonthsDataParser(JSONObject jsonObject) throws JSONException {
        JSONArray last6MonthsSale = jsonObject.getJSONArray(TAG_last_months_of_year_sale);

        if (last6MonthsSale.length() > 0) {
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
//                xValues[i] = i + 1;
                xValues[i] = i;
                yValues[i] = (float) last6MonsSummaryAtInedx.getDouble("amount");
                legends[i] = last6MonsSummaryAtInedx.getString("name");

            }

            PieChartData pieChartData = new PieChartData(yValues, legends);
            BarChartData barChartData = new BarChartData(xValues, yValues, legends);
            LineChartData lineChartData = new LineChartData(xValues, yValues, legends);

            SummaryOfLast6MonthsData summaryOfLast6MonthsData = new SummaryOfLast6MonthsData(pieChartData, tableData, barChartData, lineChartData);

            return summaryOfLast6MonthsData;
        }
        return new SummaryOfLast6MonthsData(new PieChartData(new float[0], new String[0]), new ArrayList<SummaryOfLast6MonthsRow>(), new BarChartData(new float[0], new float[0], new String[0]),
                new LineChartData(new float[0], new float[0], new String[0]));

    }

    public static SummaryOfLast30DaysData last30DaysDataParser(JSONObject jsonObject) throws JSONException {
        JSONArray lastDaysOfMonthSale = jsonObject.getJSONArray(TAG_last_days_of_month_sale);
        if (lastDaysOfMonthSale.length() > 0) {
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
//                xValues[i] = i + 1;
                xValues[i] = i;
                yValues[i] = (float) summaryAtIndex.getDouble("amount");
                legends[i] = summaryAtIndex.getString("name");

            }


//        LineChartData lineChartData = new LineChartData(xValues, yValues, legends);
            BarChartData barChartData = new BarChartData(xValues, yValues, legends);
            LineChartData lineChartData = new LineChartData(xValues, yValues, legends);
            PieChartData pieChartData = new PieChartData(yValues, legends);
            SummaryOfLast30DaysData summaryOfLast30DaysData = new SummaryOfLast30DaysData(barChartData, tableData, lineChartData, pieChartData);

            return summaryOfLast30DaysData;
        }
        return new SummaryOfLast30DaysData(new BarChartData(new float[0], new float[0], new String[0]), new ArrayList<SummaryOfLast30DaysRow>(),
                new LineChartData(new float[0], new float[0], new String[0]), new PieChartData(new float[0], new String[0]));
    }

    public static BranchSummaryData branchSummaryParser(JSONObject jsonObject) throws JSONException {

        JSONArray summaryOfBranch = jsonObject.getJSONArray("orgUnitSales");

        if (summaryOfBranch.length() > 0) {
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
        return new BranchSummaryData(new ArrayList<BranchSummaryTableRow>(), new PieChartData(new float[0], new String[0]));
    }

    public static ArrayList<UserReportDataForSingleOu> userReportForEachOuDataParser(JSONObject jsonObject) throws JSONException {

        JSONArray summaryOfBranchArray = jsonObject.getJSONArray("orgUnitSales");
        ArrayList<UserReportDataForSingleOu> userReportDataForSingleOuArrayList = new ArrayList<>();
        if (summaryOfBranchArray.length() > 0) {
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

//                    xValues[j] = j + 1;
                    xValues[j] = j;
                    yValues[j] = (float) userReportForSingleOu.getDouble("grandTotal");
                    legends[j] = userReportForSingleOu.getString("summaryType");
                }

                PieChartData pieChartData = new PieChartData(yValues, legends);
                LineChartData lineChartData = new LineChartData(xValues, yValues, legends);
                BarChartData barChartData = new BarChartData(xValues, yValues, legends);
                UserReportDataForSingleOu userReportDataForSingleOu = new UserReportDataForSingleOu(userReportTableRowArrayList, pieChartData, org,
                        lineChartData, barChartData);
                userReportDataForSingleOuArrayList.add(userReportDataForSingleOu);
            }
            return userReportDataForSingleOuArrayList;
        }
        return new ArrayList<UserReportDataForSingleOu>();
    }

    public static ArrayList<UserReportTableRow> userReportAllTogetherOuDataParser(JSONObject jsonObject) throws JSONException {

        JSONArray summaryOfBranchArray = jsonObject.getJSONArray("orgUnitSales");
        ArrayList<UserReportTableRow> userReportTableRowArrayList = new ArrayList<>();

        if (summaryOfBranchArray.length() > 0) {
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
        return new ArrayList<UserReportTableRow>();
    }

    public static ArrayList<FigureReportData> figureReportDataParser(JSONObject jsonObject) throws JSONException {
        JSONArray summaryOfBranchArray = jsonObject.getJSONArray("orgUnitSales");

        ArrayList<FigureReportData> figureReportDataArrayList = new ArrayList<>();
        ArrayList<FigureReportData> figureReportDataArrayListFiltered = new ArrayList<>();

        if (summaryOfBranchArray.length() > 0) {
            for (int i = 0; i < summaryOfBranchArray.length(); i++) {

                String org = summaryOfBranchArray.getJSONObject(i).getString("org");
                double totalForThisOrg = summaryOfBranchArray.getJSONObject(i).getDouble("grandTotal");

                JSONArray figureReportArray = summaryOfBranchArray.getJSONObject(i).getJSONArray("figureReport");
                ArrayList<FigureReportDataElements> figureReportDataElementsArrayList = new ArrayList<>();
                ArrayList<FigureReportDataElements> figureReportDataElementsFiltered = new ArrayList<>();

                float[] xValues = new float[figureReportArray.length()];
                float[] yValues1 = new float[figureReportArray.length()];
                float[] yValues2 = new float[figureReportArray.length()];
                String[] legends = new String[figureReportArray.length()];

                int count = 0;
                for (int j = 0; j < figureReportArray.length(); j++) {
                    JSONObject singleFigureReportObject = figureReportArray.getJSONObject(j);

                    FigureReportDataElements figureReportDataElements = new FigureReportDataElements(singleFigureReportObject.getString("summaryType"),
                            singleFigureReportObject.getInt("totalCount"), singleFigureReportObject.getDouble("grandTotal"), org);
                    figureReportDataElementsArrayList.add(figureReportDataElements);

                    xValues[j] = j;
                    yValues1[j] = (float) singleFigureReportObject.getDouble("grandTotal");
                    yValues2[j] = (float) singleFigureReportObject.getInt("totalCount");
                    legends[j] = singleFigureReportObject.getString("summaryType");

                }

                LineChartData lineChartData1 = new LineChartData(xValues, yValues1, legends);
                LineChartData lineChartData2 = new LineChartData(xValues, yValues2, legends);
                PieChartData pieChartData = new PieChartData(yValues1, legends);
                BarChartData barChartData = new BarChartData(xValues, yValues1, legends);
                FigureReportData figureReportData = new FigureReportData(figureReportDataElementsArrayList, lineChartData1, lineChartData2, org,
                        totalForThisOrg, pieChartData, barChartData);
                figureReportDataArrayList.add(figureReportData);

            }

            return figureReportDataArrayList;
        }
        return new ArrayList<FigureReportData>();
    }

    public static ArrayList<FigureReportDataElements> figureReportAllTogetherOuDataParser(JSONObject jsonObject) throws JSONException {
        JSONArray summaryOfBranchArray = jsonObject.getJSONArray("orgUnitSales");
        ArrayList<FigureReportDataElements> figureReportTableRowArrayList = new ArrayList<>();
        if (summaryOfBranchArray.length() > 0) {
            for (int i = 0; i < summaryOfBranchArray.length(); i++) {
                JSONArray figureReportDatForEach = summaryOfBranchArray.getJSONObject(i).getJSONArray("figureReport");
                String org = summaryOfBranchArray.getJSONObject(i).getString("org");

                float[] xValues = new float[summaryOfBranchArray.length()];
                float[] yValues = new float[summaryOfBranchArray.length()];
                String[] legends = new String[summaryOfBranchArray.length()];

                double grandTotalSum = 0;


                for (int j = 0; j < figureReportDatForEach.length(); j++) {
                    FigureReportDataElements figureReportDataElements = new FigureReportDataElements(figureReportDatForEach.getJSONObject(j).getString("summaryType"),
                            figureReportDatForEach.getJSONObject(j).getInt("totalCount"),
                            figureReportDatForEach.getJSONObject(j).getDouble("grandTotal"),
                            org);
                    figureReportTableRowArrayList.add(figureReportDataElements);

                }
            }
//            Log.i("DISTINICT TIMES",distinictDates(figureReportTableRowArrayList).toString());
            return figureReportTableRowArrayList;
        }
        return new ArrayList<FigureReportDataElements>();
    }


    public static ArrayList<String> distinictDates(ArrayList<FigureReportDataElements> figureReportDataElements) {
        ArrayList<String> dates = new ArrayList<>();
        for (FigureReportDataElements figure : figureReportDataElements) {
            dates.add(figure.dateNTime);
        }
        Set<String> set = new HashSet<>(dates);
        dates.clear();
        dates.addAll(set);
        return dates;
    }

//    public static ArrayList<VoucherDataForVan> voucherParser(JSONObject jsonObject) throws JSONException {
//
//        JSONArray summaryOfBranchArray = jsonObject.getJSONArray("orgUnitSales");
//        ArrayList<VoucherDataForVan> voucherDataForVanList = new ArrayList<>();
////        if (summaryOfBranchArray.length() > 0) {
//        for (int i = 0; i < summaryOfBranchArray.length(); i++) {
//
//            JSONArray voucherArray = summaryOfBranchArray.getJSONObject(i).getJSONArray("vouchers");
////            Log.i("voucherArray", voucherArray + "");
//            String org = summaryOfBranchArray.getJSONObject(i).getString("org");
//            ArrayList<VoucherData> voucherDataList = new ArrayList<>();
//
//            for (int j = 0; j < voucherArray.length(); j++) {
//                JSONObject voucherForVan = voucherArray.getJSONObject(j);
//                VoucherData voucherData = new VoucherData(voucherForVan.getString("voucherNo"), "",
//                        voucherForVan.getDouble("grandTotal"), voucherForVan.getDouble("latitude"), voucherForVan.getDouble("longitude"),
//                        voucherForVan.getDouble("taxAmount"), voucherForVan.getString("tin"), voucherForVan.getString("dateAndTime"),
//                        voucherForVan.getDouble("subTotal"), voucherForVan.getString("username"), voucherForVan.getInt("itemCount"));
//                voucherDataList.add(voucherData);
//            }
//
//            VoucherDataForVan voucherDataForVan = new VoucherDataForVan(org, voucherDataList);
//            voucherDataForVanList.add(voucherDataForVan);
//
//        }
//        return voucherDataForVanList;
//    }


    public ArrayList<VoucherDataForVan> voucherParser(JSONObject jsonObject) throws JSONException {
        JSONArray summaryOfBranchArray = jsonObject.getJSONArray("orgUnitSales");
        ArrayList<VoucherDataForVan> voucherDataForVanList = new ArrayList<>();
        for (int i = 0; i < summaryOfBranchArray.length(); i++) {

            JSONArray vouchers = summaryOfBranchArray.getJSONObject(i).getJSONArray("vouchers");
            String org = summaryOfBranchArray.getJSONObject(i).getString("org");
            double grandTotal = summaryOfBranchArray.getJSONObject(i).getDouble("grandTotal");
            int countS = summaryOfBranchArray.getJSONObject(i).getInt("countS");

            ArrayList<VoucherData> voucherDataArrayList = new ArrayList<>();
            for (int j = 0; j < vouchers.length(); j++) {
//            for (int j = 0; j < 2; j++) {
                JSONObject voucherObject = vouchers.getJSONObject(j);
                String outlet = (voucherObject.getString("outlates") == null) ? "- - - - -" : voucherObject.getString("outlates");
                VoucherData voucherData = new VoucherData(voucherObject.getString("voucherNo"), outlet, voucherObject.getDouble("grandTotal"),
                        voucherObject.getDouble("latitude"), voucherObject.getDouble("longitude"), voucherObject.getDouble("taxAmount"),
                        voucherObject.getString("tin"), voucherObject.getString("dateAndTime"), voucherObject.getDouble("subTotal"),
                        voucherObject.getString("username"), voucherObject.getInt("itemCount"));
                voucherDataArrayList.add(voucherData);

            }
            VoucherDataForVan voucherDataForVan = new VoucherDataForVan(org, voucherDataArrayList, grandTotal, countS);
            voucherDataForVanList.add(voucherDataForVan);
        }
        return voucherDataForVanList;
    }

//    public static VsmTableForSingleDistributor vsmTransactionForSingleCompanyParser(JSONObject jsonObject) throws JSONException {
//        JSONArray vsmTransactionArray = jsonObject.getJSONArray("getSalesDataToDisplayOnVsmTable");
//        if (vsmTransactionArray.length() > 0) {
//            JSONObject vsmTransactionObject = vsmTransactionArray.getJSONObject(0);
//
////        VsmTableForSingleDistributor dataForAdistributor = new VsmTableForSingleDistributor();
//            JSONArray vsmTables = vsmTransactionObject.getJSONArray("vsmTables");
//            String orgName = vsmTransactionObject.getString("orgName");
//
//            ArrayList<VsmTableDataForSingleVan> allVans = new ArrayList<VsmTableDataForSingleVan>();
//
//            for (int i = 0; i < vsmTables.length(); i++) {
//                JSONObject tableDataObjectForSingleVanInJson = vsmTables.getJSONObject(i);
//
//                JSONArray transactionsOfaVan = tableDataObjectForSingleVanInJson.getJSONArray("tableRows");
//                String vanName = tableDataObjectForSingleVanInJson.getString("van");
//                int salesOutLateCount = tableDataObjectForSingleVanInJson.getInt("salesOutLateCount");
//                String lastActive = tableDataObjectForSingleVanInJson.getString("lastActive");
//                int allLineItemCount = tableDataObjectForSingleVanInJson.getInt("allLineItemCount");
//                double totalPrice = tableDataObjectForSingleVanInJson.getDouble("totalPrice");
//
//                ArrayList<VsmTransactionTableRow> singleTransactionOfaVan = new ArrayList<VsmTransactionTableRow>();
//                for (int j = 0; j < transactionsOfaVan.length(); j++) {
//                    JSONObject singleTransactionObject = transactionsOfaVan.getJSONObject(j);
//
//                    VsmTransactionTableRow vsmTransactionTableRow = new VsmTransactionTableRow(
//                            singleTransactionObject.getString("voucherNo"),
//                            singleTransactionObject.getString("outlates"),
//                            singleTransactionObject.getString("tin"),
//                            singleTransactionObject.getString("dateAndTime"),
//                            (int) singleTransactionObject.getDouble("itemCount"),
//                            singleTransactionObject.getDouble("subTotal"),
//                            singleTransactionObject.getDouble("vat"),
//                            singleTransactionObject.getDouble("grandTotal"),
//                            singleTransactionObject.getDouble("latitude"),
//                            singleTransactionObject.getDouble("longitude"),
//                            singleTransactionObject.getString("username"));
//
//                    singleTransactionOfaVan.add(vsmTransactionTableRow);
//                }
//
//                VsmTableDataForSingleVan vsmTableDataForSingleVan = new VsmTableDataForSingleVan(vanName, singleTransactionOfaVan, salesOutLateCount,
//                        lastActive, allLineItemCount, totalPrice);
//                allVans.add(vsmTableDataForSingleVan);
//
//            }
//
//            VsmTableForSingleDistributor vsmTableForSingleDistributor = new VsmTableForSingleDistributor(allVans, orgName);
//            return vsmTableForSingleDistributor;
//        }
//        return new VsmTableForSingleDistributor(new ArrayList<VsmTableDataForSingleVan>(), "");
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void parseAllDashBoardDataCatching(DashBoardData dashBoardData, JSONObject rootJSON) {
        try {
            dashBoardData.setSummarizedByArticleData(summarizedByArticleParser(rootJSON));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            dashBoardData.setSummarizedByParentArticleData(summarizedByParentArticleParser(rootJSON));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            dashBoardData.setSummarizedByChildArticleData(summarizedByChildArticleParser(rootJSON));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            dashBoardData.setSummaryOfLast6MonthsData(last6MonthsDataParser(rootJSON));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            dashBoardData.setSummaryOfLast30DaysData(last30DaysDataParser(rootJSON));
//            dashBoardData.setSummaryOfLast30DaysData(new SummaryOfLast30DaysData(new BarChartData(new float[0], new float[0], new String[0]), new ArrayList<SummaryOfLast30DaysRow>()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            dashBoardData.setBranchSummaryData(branchSummaryParser(rootJSON));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            dashBoardData.setVoucherDataForVans(voucherParser(rootJSON));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            dashBoardData.setUserReportForEachBranch(userReportForEachOuDataParser(rootJSON));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            dashBoardData.setFigureReportDataforEachBranch(figureReportDataParser(rootJSON));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            dashBoardData.setUserReportForAllBranch(userReportAllTogetherOuDataParser(rootJSON));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            dashBoardData.setFigureReportDataforAllBranch(figureReportAllTogetherOuDataParser(rootJSON));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}




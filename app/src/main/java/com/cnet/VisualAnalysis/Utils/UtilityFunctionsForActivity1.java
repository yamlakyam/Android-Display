package com.cnet.VisualAnalysis.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cnet.VisualAnalysis.Data.DistributorTableRow;
import com.cnet.VisualAnalysis.Data.SummaryTableRow;
import com.cnet.VisualAnalysis.Data.VSMCard;
import com.cnet.VisualAnalysis.Data.VsmTableDataForSingleVan;
import com.cnet.VisualAnalysis.Data.VsmTableForSingleDistributor;
import com.cnet.VisualAnalysis.Data.VsmTransactionTableRow;
import com.cnet.VisualAnalysis.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UtilityFunctionsForActivity1 {

    @SuppressLint("HandlerLeak")
    public static Handler getHandlerForUpdatingDataByRedrawing(VolleyHttp.GetRequest classImplemented, Context context) {
        return new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                Integer index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }

                VolleyHttp http = new VolleyHttp(context);
                http.makeGetRequest("https://jsonplaceholder.typicode.com/todos/" + index, classImplemented);

            }

        };

    }

    public static ArrayList<SummaryTableRow> summaryTableParser(JSONArray tableInJson) throws JSONException {
        ArrayList<SummaryTableRow> tableData = new ArrayList<>();

        for (int i = 0; i < tableInJson.length(); i++) {
            JSONObject table = tableInJson.getJSONObject(i);

            tableData.add(
                    new SummaryTableRow(
                            table.getString("organizationName"),
                            formatTimeToString(table.getString("startTimeStamp")),
                            formatTimeToString(table.getString("endTimeStamp")),
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

        return tableData;
    }

    public static void drawSummaryTable(ArrayList<SummaryTableRow> summaryTableRows, Context context, TableLayout summaryTableLayout, int index) {

        SummaryTableRow row = summaryTableRows.get(index);
        View tableElements = LayoutInflater.from(context).inflate(R.layout.table_row_summary, null, false);

        TextView serialNumberTextView = tableElements.findViewById(R.id.serialNumberTextView);
        TextView distributorNameTextView = tableElements.findViewById(R.id.distributorNameTextView);
        TextView startTimeTextView = tableElements.findViewById(R.id.startTimeTextView);
        TextView lastActivityTextView = tableElements.findViewById(R.id.lastActivityTextView);
        TextView totalVsiTextView = tableElements.findViewById(R.id.totalVsiTextView);
        TextView totalOutlatesTextView = tableElements.findViewById(R.id.totalOutlatesTextView);
        TextView totalSkuTextView = tableElements.findViewById(R.id.totalSkuTextView);
        TextView totalQuantityTextView = tableElements.findViewById(R.id.totalQuantityTextView);
        TextView totalSalesTextView = tableElements.findViewById(R.id.totalSalesTextView);
        TextView activeVansTextView = tableElements.findViewById(R.id.activeVansTextView);
        TextView prospectTextView = tableElements.findViewById(R.id.prospectTextView);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);


        serialNumberTextView.setText(String.valueOf(index + 1));
        distributorNameTextView.setText(row.getOrganizationName());
        startTimeTextView.setText(row.getStartTimeStamp());
        lastActivityTextView.setText(row.getEndTimeStamp());
        totalVsiTextView.setText(numberFormat.format(row.getVsiCount()));
        totalOutlatesTextView.setText(numberFormat.format(row.getSalesOutLateCount()));
        totalSkuTextView.setText(numberFormat.format(row.getSkuCount()));
        totalQuantityTextView.setText(numberFormat.format(row.getQuantityCount()));
        totalSalesTextView.setText(numberFormat.format(row.getTotalSalesAmountAfterTax()));
        activeVansTextView.setText(numberFormat.format(row.getActiveVans()));
        prospectTextView.setText(numberFormat.format(row.getProspect()));

        summaryTableLayout.addView(tableElements);
        animate(summaryTableLayout, tableElements);

    }

    public static ArrayList<DistributorTableRow> distributorTableParser(JSONArray tableInJson, int index) throws JSONException {

        ArrayList<DistributorTableRow> distributorTableData = new ArrayList<>();

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
        return distributorTableData;

    }

    public static void drawDistributorTable(ArrayList<DistributorTableRow> distributorTableRows, Context context, TableLayout distributorTableLayout, int index) {
        if (distributorTableRows != null) {
            DistributorTableRow row = distributorTableRows.get(index);

            View tableElements = null;
            try {
                tableElements = LayoutInflater.from(context).inflate(R.layout.table_row_distributor, null, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Date lastActive = formatTime(row.getEndTimeStamp());
            Date currentTime = Calendar.getInstance().getTime();

            String formattedLastActive = timeElapsed(lastActive, currentTime);

            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(true);

            if (tableElements != null) {
                TextView distributorSerialNumberTV = tableElements.findViewById(R.id.distributorSerialNumberTV);
                TextView distributorVSITV = tableElements.findViewById(R.id.distributorVSITV);
                TextView distributorProspectTV = tableElements.findViewById(R.id.distributorProspectTV);
                TextView distributorEndTimeTV = tableElements.findViewById(R.id.distributorEndTimeTV);
                TextView distributorSalesOutletTV = tableElements.findViewById(R.id.distributorSalesOutletTV);
                TextView distributorSKUcountTV = tableElements.findViewById(R.id.distributorSKUcountTV);
                TextView distributorQuantityCountTV = tableElements.findViewById(R.id.distributorQuantityCountTV);
                TextView distributorTotalSalesTV = tableElements.findViewById(R.id.distributorTotalSalesTV);


                distributorSerialNumberTV.setText(String.valueOf(index + 1));
                distributorVSITV.setText(row.getVsi());
                distributorProspectTV.setText(String.valueOf(row.getProspect()));
                distributorEndTimeTV.setText(formattedLastActive);
                distributorSalesOutletTV.setText(String.valueOf(row.getSalesOutLateCount()));
                distributorSKUcountTV.setText(String.valueOf(row.getSkuCount()));
                distributorQuantityCountTV.setText(String.valueOf(row.getQuantityCount()));
                distributorTotalSalesTV.setText(numberFormat.format(row.getTotalSalesAmountAfterTax()));

                if (distributorTableLayout != null) {
                    distributorTableLayout.addView(tableElements);
                }
                animate(distributorTableLayout, tableElements);
            }


        }

    }

    public static void animate(View container, View child) {
        if (container != null) {
            Animation animation = AnimationUtils.loadAnimation(container.getContext(), R.anim.slide_out_bottom);
            child.startAnimation(animation);
        }
    }

    public static void scrollRows(ScrollView scrollView) {

        if (scrollView != null) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        }


    }

    public static ArrayList<ArrayList<VSMCard>> vsmCardParser(JSONArray cardsInJSon) throws JSONException {

        ArrayList<ArrayList<VSMCard>> allVSMcards = new ArrayList<>();

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


        return allVSMcards;
    }

    public static void drawVSMCard(JSONArray cardsInJSon, int index, Context context, GridView VSMcardGridView) throws JSONException {

        VSMCardGVAdapter adapter = new VSMCardGVAdapter(context, vsmCardParser(cardsInJSon).get(index));
        VSMcardGridView.setAdapter(adapter);

    }

    public static ArrayList<VsmTableForSingleDistributor> vsmTransactionParser(JSONArray jsonArray) throws JSONException {

        ArrayList<VsmTableForSingleDistributor> dataForAllDis = new ArrayList<>();

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tableDataObjectForSingleOrgInJson = jsonArray.getJSONObject(i);
                JSONArray tableDataForSingleOrgInJson = tableDataObjectForSingleOrgInJson.getJSONArray("vsmTables");
                String distributorName = tableDataObjectForSingleOrgInJson.getString("orgName");

                ArrayList<VsmTableDataForSingleVan> dataToDisplaySingleDis = new ArrayList<>();
                for (int j = 0; j < tableDataForSingleOrgInJson.length(); j++) {
                    JSONObject tableDataObjectForSingleVanInJson = tableDataForSingleOrgInJson.getJSONObject(j);
                    VsmTableDataForSingleVan dataToDisplaySingleVanTable = getSingleVanData(tableDataObjectForSingleVanInJson);
                    dataToDisplaySingleDis.add(dataToDisplaySingleVanTable);
                }

                VsmTableForSingleDistributor vsmTableForSingleDistributor = new VsmTableForSingleDistributor(
                        dataToDisplaySingleDis,
                        distributorName
                );

                dataForAllDis.add(vsmTableForSingleDistributor);
            }
        }

        return dataForAllDis;
    }

    public static VsmTableDataForSingleVan getSingleVanData(JSONObject tableDataObjectForSingleVanInJson) throws JSONException {
        JSONArray tableRowsForSingleVan = tableDataObjectForSingleVanInJson.getJSONArray("tableRows");
        String nameOfVan = tableDataObjectForSingleVanInJson.getString("van");


        ArrayList<VsmTransactionTableRow> vsmTableForSingleVan = new ArrayList<>();
        for (int k = 0; k < tableRowsForSingleVan.length(); k++) {
            JSONObject tableRowForSingleVan = tableRowsForSingleVan.getJSONObject(k);

            String formattedOutlet;
            String formattedTIN;
            if (tableRowForSingleVan.getString("outlates").length() > 15) {
                formattedOutlet = tableRowForSingleVan.getString("outlates").substring(0, 12) + "...";
            } else
                formattedOutlet = tableRowForSingleVan.getString("outlates");

            if (tableRowForSingleVan.getString("tin").length() == 0) {
                formattedTIN = "- - - - - - - - - - -";
            } else {
                formattedTIN = tableRowForSingleVan.getString("tin");
            }


            vsmTableForSingleVan.add(new VsmTransactionTableRow(
                    tableRowForSingleVan.getString("voucherNo"),
                    formattedOutlet,
                    formattedTIN,
                    tableRowForSingleVan.getString("dateAndTime"),
                    tableRowForSingleVan.getInt("itemCount"),
                    tableRowForSingleVan.getDouble("subTotal"),
                    tableRowForSingleVan.getString("vat"),
                    tableRowForSingleVan.getDouble("grandTotal")
            ));
        }

        return new VsmTableDataForSingleVan(nameOfVan, vsmTableForSingleVan);
    }

    public static void drawVsmTransactionTable(ArrayList<VsmTableForSingleDistributor> vsmTableDataForAll, Context context, TableLayout vsmTransactionTableLayout, int distributorIndex, int dataIndex, int animationIndex) {

        ArrayList<VsmTableDataForSingleVan> vsmTableDataForSingleDis = vsmTableDataForAll.get(distributorIndex).getAllVansData();
        VsmTableDataForSingleVan vsmTableDataForSingleVan = vsmTableDataForSingleDis.get(dataIndex);
        ArrayList<VsmTransactionTableRow> rows = vsmTableDataForSingleVan.getTableRows();

        if (animationIndex < rows.size()) {
            VsmTransactionTableRow row = rows.get(animationIndex);

            View tableElements = LayoutInflater.from(context).inflate(R.layout.table_row_vsm_transaction, null, false);
            TextView snTextView = tableElements.findViewById(R.id.vsmTransSNtextView);
            TextView voucherNoTextView = tableElements.findViewById(R.id.vsmTransVoucherNtxtView);
            TextView outletTextView = tableElements.findViewById(R.id.vsmTransOutletTextView);
            TextView TINtextView = tableElements.findViewById(R.id.vsmTransTINtextView);
            TextView dateNtimeTextView = tableElements.findViewById(R.id.vsmTransDateNtimeTextV);
            TextView itemCountTextview = tableElements.findViewById(R.id.vsmTransItemCountTxtV);
            TextView subTotalTextView = tableElements.findViewById(R.id.vsmTransSubTotalTxtv);
            TextView VATtextView = tableElements.findViewById(R.id.vsmTransVATtextView);
            TextView totalSalesTextView = tableElements.findViewById(R.id.vsmTransGrandTotalTextView);

            NumberFormat numberFormat = NumberFormat.getInstance();

            numberFormat.setGroupingUsed(true);


            snTextView.setText(String.valueOf(animationIndex + 1));
            voucherNoTextView.setText(row.getVoucherNo());
            outletTextView.setText(row.getOutlet());
            TINtextView.setText(row.getTIN());
            dateNtimeTextView.setText(formatTimeToString(row.getDateNtime()));
            itemCountTextview.setText(String.valueOf(row.getItemCount()));
            subTotalTextView.setText(numberFormat.format(Math.round(row.getSubTotal() * 100.0) / 100.0));
            VATtextView.setText(row.getVAT());
            totalSalesTextView.setText(numberFormat.format(Math.round(row.getTotalSales() * 100.0) / 100.0));

            if (vsmTransactionTableLayout != null) {
                vsmTransactionTableLayout.addView(tableElements);
            }
            animate(vsmTransactionTableLayout, tableElements);
        }


    }

    public static String formatTimeToString(String lastActive) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("HH:mm:ss");

        Date parsed = null;
        String formattedTime = null;
        try {
            parsed = input.parse(lastActive);
            formattedTime = output.format(parsed);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

    public static String formatDateTimeToString(String lastActive) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

        Date parsed = null;
        String formattedTime = null;
        try {
            parsed = input.parse(lastActive);
            formattedTime = output.format(parsed);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

    static Date formatTime(String lastActive) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date parsed = null;
        try {
            parsed = input.parse(lastActive);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsed;
    }

    public static String timeElapsed(Date startDate, Date endDate) {
        long difference = endDate.getTime() - startDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long monthsInMilli = daysInMilli * 30;

        long elapsedMonths = difference / monthsInMilli;
        difference = difference % monthsInMilli;
        long elapsedDays = difference / daysInMilli;
        difference = difference % daysInMilli;
        long elapsedHours = difference / hoursInMilli;
        difference = difference % hoursInMilli;
        long elapsedMinutes = difference / minutesInMilli;
        difference = difference % minutesInMilli;

        long elapsedSeconds = difference / secondsInMilli;
        if (elapsedMonths > 7) {
            return " long ago";
        } else if (elapsedMonths > 0) {
            return elapsedMonths + " months ago";
        } else if (elapsedMonths == 0 && elapsedDays > 0) {
            return elapsedDays + " days ago";
        } else if (elapsedDays == 0 && elapsedHours > 0) {
            return elapsedHours + " hours ago";
        } else if (elapsedDays == 0 && elapsedHours == 0 & elapsedMinutes > 0) {
            return elapsedMinutes + " minutes ago";
        } else if (elapsedDays == 0 && elapsedHours == 0 & elapsedMinutes == 0 && elapsedSeconds > 0) {
            return elapsedSeconds + " seconds ago";
        } else
//            return elapsedDays + " days, " + elapsedHours + " hours, " + elapsedMinutes + " minutes, " + elapsedSeconds + " seconds ago";
            return elapsedSeconds + "";
    }

}

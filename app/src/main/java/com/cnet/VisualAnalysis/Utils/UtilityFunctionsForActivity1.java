package com.cnet.VisualAnalysis.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.cnet.VisualAnalysis.Data.DistributorTableRow;
import com.cnet.VisualAnalysis.Data.FigureReportDataElements;
import com.cnet.VisualAnalysis.Data.SummaryTableRow;
import com.cnet.VisualAnalysis.Data.UserReportTableRow;
import com.cnet.VisualAnalysis.Data.VsmTableDataForSingleVan;
import com.cnet.VisualAnalysis.Data.VsmTableForSingleDistributor;
import com.cnet.VisualAnalysis.Data.VsmTransactionTableRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SplashScreenActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UtilityFunctionsForActivity1 {

    public ArrayList<SummaryTableRow> summaryTableParser(JSONArray tableInJson) throws JSONException {
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

    public void drawSummaryTable(ArrayList<SummaryTableRow> summaryTableRows, Context context, TableLayout summaryTableLayout, int index) {

        SummaryTableRow row = summaryTableRows.get(index);

        if (context != null) {
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
            totalSalesTextView.setText(numberFormat.format(Math.round(row.getTotalSalesAmountAfterTax() * 100) / 100));
            activeVansTextView.setText(numberFormat.format(row.getActiveVans()));
            prospectTextView.setText(numberFormat.format(row.getProspect()));

            summaryTableLayout.addView(tableElements);
            animate(summaryTableLayout, tableElements);
        }

    }

    public void drawDistributorTable(ArrayList<DistributorTableRow> distributorTableRows, Context context, TableLayout distributorTableLayout, int index) {
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
                distributorTotalSalesTV.setText(numberFormat.format(Math.round(row.getTotalSalesAmountAfterTax() * 100) / 100));

                if (distributorTableLayout != null) {
                    distributorTableLayout.addView(tableElements);
                }
                animate(distributorTableLayout, tableElements);
            }

        }

    }

    public void drawUserReportForEachOu(ArrayList<UserReportTableRow> userReportTableRows, Context context, TableLayout userReportTableLayout, int index) {
        if (userReportTableRows != null) {
            UserReportTableRow row = userReportTableRows.get(index);

            View tableElements = null;
            try {
                tableElements = LayoutInflater.from(context).inflate(R.layout.table_row_summary_by_parent_article, null, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (tableElements != null) {
                TextView userReportSN = tableElements.findViewById(R.id.tableRowParentArtProperty1);
                TextView userReportSummaryType = tableElements.findViewById(R.id.tableRowParentArtProperty2);
                TextView userReportGrandTotal = tableElements.findViewById(R.id.tableRowParentArtProperty3);
                TextView userReportPercentage = tableElements.findViewById(R.id.tableRowParentArtProperty4);

                double grandTotalForAll = 0;
                for (int i = 0; i < userReportTableRows.size(); i++) {
                    double grandTotalForI = userReportTableRows.get(i).getGrandTotal();
                    grandTotalForAll = grandTotalForAll + grandTotalForI;
                }
                double percentage = (row.grandTotal / grandTotalForAll) * 100;

                NumberFormat numberFormat = NumberFormat.getInstance();

                userReportSN.setText(String.valueOf(index + 1));
                userReportSummaryType.setText(row.getSummaryType());
                userReportGrandTotal.setText(String.valueOf(Math.round(row.grandTotal * 100.0) / 100.0));
                userReportPercentage.setText(numberFormat.format(percentage) + "%");


                if (userReportTableLayout != null) {
                    userReportTableLayout.addView(tableElements);
                }
                animate(userReportTableLayout, tableElements);
            }

        }
    }

    public void drawPeakHourReportForEachOu(ArrayList<FigureReportDataElements> figureReportDataElements, Context context, TableLayout peakHourReportTableLayout, int index) {
        if (figureReportDataElements != null) {
            FigureReportDataElements row = figureReportDataElements.get(index);

            View tableElements = null;
            try {
                tableElements = LayoutInflater.from(context).inflate(R.layout.table_row_summary_by_parent_article, null, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (tableElements != null) {
                TextView peakHourReportSN = tableElements.findViewById(R.id.tableRowParentArtProperty1);
                TextView peakHourReportSummaryType = tableElements.findViewById(R.id.tableRowParentArtProperty2);
                TextView peakHourReportTotalCount = tableElements.findViewById(R.id.tableRowParentArtProperty3);
                TextView peakHourReportGrandTotal = tableElements.findViewById(R.id.tableRowParentArtProperty4);

                double grandTotalForAll = 0;
                for (int i = 0; i < figureReportDataElements.size(); i++) {
                    double grandTotalForI = figureReportDataElements.get(i).getGrandTotal();
                    grandTotalForAll = grandTotalForAll + grandTotalForI;
                }
                double percentage = (row.grandTotal / grandTotalForAll) * 100;

                NumberFormat numberFormat = NumberFormat.getInstance();

                peakHourReportSN.setText(String.valueOf(index + 1));
                peakHourReportSummaryType.setText(formatHourNmin(row.dateNTime));
                peakHourReportTotalCount.setText(String.valueOf(row.totalCount));
                peakHourReportGrandTotal.setText(numberFormat.format(Math.round(row.getGrandTotal() * 100.0) / 100.0));

                if (peakHourReportTableLayout != null) {
                    peakHourReportTableLayout.addView(tableElements);
                }
                animate(peakHourReportTableLayout, tableElements);
            }
        }
    }

    public void drawUserReportForAllOu(ArrayList<UserReportTableRow> userReportTableRows, Context context, TableLayout userReportTableLayout, int index) {
        if (userReportTableRows != null) {
            UserReportTableRow row = userReportTableRows.get(index);

            View tableElements = null;
            try {
                tableElements = LayoutInflater.from(context).inflate(R.layout.user_report_for_all_table_row, null, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (tableElements != null) {
                TextView userReportForAllSN = tableElements.findViewById(R.id.tableRowAllUserReportProperty1);
                TextView userReportForAllUserName = tableElements.findViewById(R.id.tableRowAllUserReportProperty2);
                TextView userReportForAllGrandTotal = tableElements.findViewById(R.id.tableRowAllUserReportProperty3);
                TextView userReportForAllBranchName = tableElements.findViewById(R.id.tableRowAllUserReportProperty4);
                TextView userReportForAllPercentage = tableElements.findViewById(R.id.tableRowAllUserReportProperty5);

                double grandTotalForAll = 0;
                for (int i = 0; i < userReportTableRows.size(); i++) {
                    double grandTotalForI = userReportTableRows.get(i).getGrandTotal();
                    grandTotalForAll = grandTotalForAll + grandTotalForI;
                }
                double percentage = (row.grandTotal / grandTotalForAll) * 100;

                NumberFormat numberFormat = NumberFormat.getInstance();

                userReportForAllSN.setText(String.valueOf(index + 1));
                userReportForAllUserName.setText(row.getSummaryType());
                userReportForAllGrandTotal.setText(String.valueOf(row.grandTotal));
                userReportForAllBranchName.setText(row.getOrg());
                userReportForAllPercentage.setText(numberFormat.format(percentage) + "%");

                if (userReportTableLayout != null) {
                    userReportTableLayout.addView(tableElements);
                }
                animate(userReportTableLayout, tableElements);
            }

        }

    }

    public void drawPeakHourReportForAllOus(ArrayList<FigureReportDataElements> figureReportDataElements, Context context, TableLayout peakHourReportTableLayout, int index) {
        if (figureReportDataElements != null) {
            FigureReportDataElements row = figureReportDataElements.get(index);

            View tableElements = null;
            try {
                tableElements = LayoutInflater.from(context).inflate(R.layout.figure_report_for_all_table_row, null, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (tableElements != null) {
                TextView peakHourReportForAllSN = tableElements.findViewById(R.id.tableRowPeakHourReportForAllProperty1);
                TextView peakHourReportForAllTime = tableElements.findViewById(R.id.tableRowPeakHourReportForAllProperty2);
                TextView peakHourReportForAllTotalCount = tableElements.findViewById(R.id.tableRowPeakHourReportForAllProperty3);
                TextView peakHourReportForAllGrandTotal = tableElements.findViewById(R.id.tableRowPeakHourReportForAllProperty5);

                NumberFormat numberFormat = NumberFormat.getInstance();
                peakHourReportForAllSN.setText(String.valueOf(index + 1));
                peakHourReportForAllTime.setText(formatHourNmin(row.getDateNTime()));
                peakHourReportForAllTotalCount.setText(String.valueOf(row.totalCount));

                peakHourReportForAllGrandTotal.setText(numberFormat.format(Math.round(row.grandTotal * 100.0) / 100.0));

                if (peakHourReportTableLayout != null) {
                    peakHourReportTableLayout.addView(tableElements);
                }
                animate(peakHourReportTableLayout, tableElements);
            }

        }

    }

//    public void drawVansOfSingleOrgTable(ArrayList<VsmTableDataForSingleVan> vsmTableDataForSingleVanArrayList, Context context, TableLayout vanListTableLayout, int index) {
//        if (vsmTableDataForSingleVanArrayList != null) {
//            VsmTableDataForSingleVan row = vsmTableDataForSingleVanArrayList.get(index);
//
//            View tableElements = null;
//            try {
//                tableElements = LayoutInflater.from(context).inflate(R.layout.table_row_vans_of_single_org, null, false);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (tableElements != null) {
//                TextView singleOrgVansSerialNumberTV = tableElements.findViewById(R.id.singleOrgVansSerialNumberTV);
//                TextView singleOrgVansVSITV = tableElements.findViewById(R.id.singleOrgVansVSITV);
//                TextView singleOrgVansProspectTV = tableElements.findViewById(R.id.singleOrgVansProspectTV);
//                TextView singleOrgVansEndTimeTV = tableElements.findViewById(R.id.singleOrgVansEndTimeTV);
//                TextView singleOrgVansSalesOutletTV = tableElements.findViewById(R.id.singleOrgVansSalesOutletTV);
//                TextView singleOrgVansQuantityCountTV = tableElements.findViewById(R.id.singleOrgVansQuantityCountTV);
//                TextView singleOrgVansTotalSalesTV = tableElements.findViewById(R.id.singleOrgVansTotalSalesTV);
//
//
//                NumberFormat numberFormat = NumberFormat.getInstance();
//                singleOrgVansSerialNumberTV.setText(String.valueOf(index + 1));
//                singleOrgVansVSITV.setText(row.nameOfVan);
//                singleOrgVansProspectTV.setText(String.valueOf(1));
//                singleOrgVansEndTimeTV.setText(formatDateTimeToString(row.getLastActive()));
//                singleOrgVansSalesOutletTV.setText(numberFormat.format(row.salesOutLateCount));
//                singleOrgVansQuantityCountTV.setText(numberFormat.format(row.allLineItemCount));
//                singleOrgVansTotalSalesTV.setText(numberFormat.format(Math.round(row.totalPrice * 100.0) / 100.0));
//
//                if (vanListTableLayout != null) {
//                    vanListTableLayout.addView(tableElements);
//                }
//                animate(vanListTableLayout, tableElements);
//            }
//
//        }
//
//    }

    public void animate(View container, View child) {
        if (container != null) {
            Animation animation = AnimationUtils.loadAnimation(container.getContext(), R.anim.slide_out_bottom);
            child.startAnimation(animation);
        }
    }

    public void scrollRows(ScrollView scrollView) {

        if (scrollView != null) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        }

    }

    public void drawVSMCard(int index, Context context, GridView VSMcardGridView) throws JSONException {

        VSMCardGVAdapter adapter = new VSMCardGVAdapter(context, SplashScreenActivity.allData.getFmcgData().getVsmCards().get(index));
        VSMcardGridView.setAdapter(adapter);
    }

//    public VsmTableDataForSingleVan getSingleVanData(JSONObject tableDataObjectForSingleVanInJson) throws JSONException {
//        JSONArray tableRowsForSingleVan = tableDataObjectForSingleVanInJson.getJSONArray("tableRows");
//        String nameOfVan = tableDataObjectForSingleVanInJson.getString("van");
//        int salesOutLateCount = tableDataObjectForSingleVanInJson.getInt("salesOutLateCount");
//        String lastActive = tableDataObjectForSingleVanInJson.getString("lastActive");
//        int allLineItemCount = tableDataObjectForSingleVanInJson.getInt("allLineItemCount");
//        double totalPrice = tableDataObjectForSingleVanInJson.getDouble("totalPrice");
//        ArrayList<VsmTransactionTableRow> vsmTableForSingleVan = new ArrayList<>();
//        for (int k = 0; k < tableRowsForSingleVan.length(); k++) {
//            JSONObject tableRowForSingleVan = tableRowsForSingleVan.getJSONObject(k);
//
//            String formattedOutlet;
//            String formattedTIN;
//            if (tableRowForSingleVan.getString("outlates").length() > 15) {
//                formattedOutlet = tableRowForSingleVan.getString("outlates").substring(0, 12) + "...";
//            } else
//                formattedOutlet = tableRowForSingleVan.getString("outlates");
//
//            if (tableRowForSingleVan.getString("tin").length() == 0) {
//                formattedTIN = "- - - - - - - - - - -";
//            } else {
//                formattedTIN = tableRowForSingleVan.getString("tin");
//            }
//            vsmTableForSingleVan.add(new VsmTransactionTableRow(
//                    tableRowForSingleVan.getString("voucherNo"),
//                    formattedOutlet,
//                    formattedTIN,
//                    tableRowForSingleVan.getString("dateAndTime"),
//                    tableRowForSingleVan.getInt("itemCount"),
//                    tableRowForSingleVan.getDouble("subTotal"),
//                    tableRowForSingleVan.getDouble("vat"),
//                    tableRowForSingleVan.getDouble("grandTotal"),
//                    tableRowForSingleVan.getDouble("latitude"),
//                    tableRowForSingleVan.getDouble("longitude"), tableRowForSingleVan.getString("username")
//            ));
//        }
//        return new VsmTableDataForSingleVan(nameOfVan, vsmTableForSingleVan, salesOutLateCount,
//                lastActive, allLineItemCount, totalPrice);
//    }

    public void drawVsmTransactionTable(ArrayList<VsmTableForSingleDistributor> vsmTableDataForAll, Context context, TableLayout vsmTransactionTableLayout, int distributorIndex, int dataIndex, int animationIndex) {
        ArrayList<VsmTableDataForSingleVan> vsmTableDataForSingleDis = vsmTableDataForAll.get(distributorIndex).getAllVansData();
        VsmTableDataForSingleVan vsmTableDataForSingleVan = vsmTableDataForSingleDis.get(dataIndex);
        ArrayList<VsmTransactionTableRow> rows = vsmTableDataForSingleVan.getTableRows();

        if (animationIndex < rows.size()) {
            VsmTransactionTableRow row = rows.get(animationIndex);
            if (context != null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View tableElements = inflater.inflate(R.layout.table_row_vsm_transaction, null);
//            View tableElements = LayoutInflater.from(context).inflate(R.layout.table_row_vsm_transaction, null, false);
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
                VATtextView.setText((int) row.getVAT());
                totalSalesTextView.setText(numberFormat.format(Math.round(row.getTotalSales() * 100.0) / 100.0));
                if (vsmTransactionTableLayout != null) {
                    vsmTransactionTableLayout.addView(tableElements);
                }
                animate(vsmTransactionTableLayout, tableElements);
            }
        }
    }

    public String formatTimeToString(String lastActive) {
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

    public String formatDateTimeToString(String lastActive) {
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

    public String formatDateToString(String lastActive) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");

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

    public String formatHourNmin(String lastActive) {
        SimpleDateFormat input = new SimpleDateFormat("MMM dd yyyy hh:mmaa", Locale.ENGLISH);
        SimpleDateFormat output = new SimpleDateFormat("hh:mmaa");

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

    public Date formatTime(String lastActive) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date parsed = null;
        try {
            parsed = input.parse(lastActive);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsed;
    }


    public Date peakHourFormatter(String dateTime) {
        SimpleDateFormat input = new SimpleDateFormat("MMM dd yyyy hh:mmaa", Locale.ENGLISH);

        Date parsed = null;
        try {
            parsed = input.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsed;
    }

    public String timeElapsed(Date startDate, Date endDate) {
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

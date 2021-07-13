package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.DistributorTableRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.Threads.HandleDataChangeThread;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.NumberFormat;
import java.util.ArrayList;

public class DistributorTableFragment extends Fragment implements VolleyHttp.GetRequest {

    public static Handler animationHandler;
    public static Handler changeDataHandler;
    private final String URL = "http://192.168.1.248:8001/api/ChartData/GetSalesDataForSingleOrganization";
    private ArrayList<DistributorTableRow> tablesToDisplay;
    TableLayout distributorTableLayout;
    ProgressBar distributorTableProgressBar;
    ScrollView scrollDistributorTable;
    TextView distributorHeaderTextView;
    int respSize;


    int sumofProspect, sumofOutlet, sumofSKU, sumofQuantity = 0;
    double sumofSales = 0;


    ////////
    ArrayList<DistributorTableRow> distributorTableRows;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        VolleyHttp http = new VolleyHttp(getContext());
        http.makeGetRequest(URL, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_distributor_table, container, false);
        distributorTableLayout = view.findViewById(R.id.distributorTableLayout);
        distributorTableProgressBar = view.findViewById(R.id.distributorTableProgressBar);
        scrollDistributorTable = view.findViewById(R.id.scrollDistributorTable);
        distributorHeaderTextView = view.findViewById(R.id.distributorHeaderTextView);

        return view;
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(JSONArray jsonArray, int index) {

        distributorTableLayout.removeAllViews();
        try {
            tablesToDisplay = UtilityFunctionsForActivity1.distributorTableParser(jsonArray, index);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }

                if (index == tablesToDisplay.size()) {
//                    drawLastRow();
                }
                else {
                    lastRowSummation(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity1.drawDistributorTable(tablesToDisplay, getContext(), distributorTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(scrollDistributorTable);
                }



            }

        };

        HandleRowAnimationThread handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), DistributorTableFragment.animationHandler);
        handleRowAnimationThread.start();

    }


    @SuppressLint("HandlerLeak")
    private void inflateAllTables(JSONArray jsonArray) {

        changeDataHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }


                inflateTable(jsonArray, index);
                setDistributorHeader(jsonArray, index);


            }
        };

        HandleDataChangeThread handleDataChangeThread = new HandleDataChangeThread(DistributorTableFragment.changeDataHandler, jsonArray.length(), 30);
        handleDataChangeThread.start();

    }

    public void setDistributorHeader(JSONArray jsonArray, int index) {
        try {
            String distributorName = jsonArray.getJSONObject(index).getString("nameOfOrg");
            distributorHeaderTextView.setText(distributorName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void lastRowSummation(DistributorTableRow distributorTableRow) {
        sumofProspect = sumofProspect + distributorTableRow.getProspect();
        sumofOutlet = sumofOutlet + distributorTableRow.getSalesOutLateCount();
        sumofSKU = sumofSKU + distributorTableRow.getSkuCount();
        sumofQuantity = sumofQuantity + distributorTableRow.getQuantityCount();
        sumofSales = sumofSales + distributorTableRow.getTotalSalesAmountAfterTax();
    }

    public void drawLastRow() {

        View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.table_row_distributor, null, false);
        TextView distributorSerialNumberTV = tableElements.findViewById(R.id.distributorSerialNumberTV);
        TextView distributorVSITV = tableElements.findViewById(R.id.distributorVSITV);
        TextView distributorProspectTV = tableElements.findViewById(R.id.distributorProspectTV);
        TextView distributorEndTimeTV = tableElements.findViewById(R.id.distributorEndTimeTV);
        TextView distributorSalesOutletTV = tableElements.findViewById(R.id.distributorSalesOutletTV);
        TextView distributorSKUcountTV = tableElements.findViewById(R.id.distributorSKUcountTV);
        TextView distributorQuantityCountTV = tableElements.findViewById(R.id.distributorQuantityCountTV);
        TextView distributorTotalSalesTV = tableElements.findViewById(R.id.distributorTotalSalesTV);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        distributorEndTimeTV.setText("");
        distributorSerialNumberTV.setText("");
        distributorVSITV.setText("");
        distributorProspectTV.setText(String.valueOf(sumofProspect));
        distributorSalesOutletTV.setText(String.valueOf(sumofOutlet));
        distributorSKUcountTV.setText(String.valueOf(sumofSKU));
        distributorQuantityCountTV.setText(String.valueOf(sumofQuantity));
        distributorTotalSalesTV.setText(numberFormat.format(sumofSales));

        distributorTableLayout.addView(tableElements);
        UtilityFunctionsForActivity2.animate(distributorTableLayout, tableElements);

    }


    @Override
    public void onSuccess(JSONArray jsonArray) {
        distributorTableProgressBar.setVisibility(View.GONE);
        try {
//            inflateTable(jsonArray,0);
            inflateAllTables(jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(VolleyError error) {

    }

}
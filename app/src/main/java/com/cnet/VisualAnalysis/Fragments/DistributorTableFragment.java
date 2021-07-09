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
import com.cnet.VisualAnalysis.Utils.UtilityFunctions;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;

import org.json.JSONArray;
import org.json.JSONException;

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

    int sumofProspect;
    int sumofOutlet;
    int sumofSKU;
    int sumofQuantity;
    double sumofSales;

    ////////
    ArrayList<DistributorTableRow> distributorTableRows;

    public DistributorTableFragment() {

    }

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
//        sumofProspect = 0;
//        sumofOutlet = 0;
//        sumofSKU = 0;
//        sumofQuantity = 0;
//        sumofSales = 0;

        distributorTableLayout.removeAllViews();

        try {
            tablesToDisplay = UtilityFunctions.distributorTableParser(jsonArray, index);
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


                /////
                UtilityFunctions.drawDistributorTable(tablesToDisplay, getContext(), distributorTableLayout, index);


//                Summations(distributorTableRows.get(index).getProspect(),
//                        distributorTableRows.get(index).getSalesOutLateCount(),
//                        distributorTableRows.get(index).getSkuCount(),
//                        distributorTableRows.get(index).getQuantityCount(),
//                        distributorTableRows.get(index).getTotalSalesAmountAfterTax(), index);
//                Log.i("sum", sumofProspect + "");

                UtilityFunctions.scrollRows(scrollDistributorTable);


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

        HandleDataChangeThread handleDataChangeThread = new HandleDataChangeThread(DistributorTableFragment.changeDataHandler, jsonArray.length());
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

    public void Summations(int totalProspect, int totalOutlet, int totalSKU, int totalQty, double totalSale, int index) {

        sumofProspect = sumofProspect + totalProspect;
        sumofOutlet = sumofOutlet + totalOutlet;
        sumofSKU = sumofSKU + totalSKU;
        sumofQuantity = sumofQuantity + totalQty;
        sumofSales = sumofSales + totalSale;
        if (index == distributorTableRows.size() - 1) {
            ArrayList<DistributorTableRow> TotaldistributorTableRows = new ArrayList<>();
//            TotaldistributorTableRows.add("")


            UtilityFunctions.drawDistributorTable(distributorTableRows, getContext(), distributorTableLayout, index);

        }
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
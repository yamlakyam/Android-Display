package com.cnet.VisualAnalysis.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.SummaryTableRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.NumberFormat;
import java.util.ArrayList;


public class SummaryTableFragment extends Fragment implements VolleyHttp.GetRequest {

    private TableLayout summaryTableLayout;
    public static Handler changeTodoHandler;
    private final String URL = "http://192.168.1.248:8001/api/ChartData/GetSalesDataForAllOrganizations";
    private ArrayList<SummaryTableRow> tablesToDisplay;
    private ProgressBar summaryTableProgressBar;
    ScrollView scrollSummaryTable;
    Fragment navhostFragment;


    int sumOfVSICount, sumOfSalesCount, sumOfSKUCount, sumOfQuantity, sumOfActiveVans, sumOfProspects = 0;
    double sumOfTotalSales = 0;

    int respSize;

    public SummaryTableFragment() {

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

        View view = inflater.inflate(R.layout.fragment_summary_table, container, false);
        summaryTableLayout = view.findViewById(R.id.summaryTableLayout);
        summaryTableProgressBar = view.findViewById(R.id.summaryTableprogressBar);
        scrollSummaryTable = view.findViewById(R.id.scrollSummaryTable);

        View navhost = getActivity().findViewById(R.id.nav_host_fragment);
        navhostFragment = FragmentManager.findFragment(navhost);

        return view;
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(JSONArray jsonArray) {
        changeTodoHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }

                try {
                    tablesToDisplay = UtilityFunctionsForActivity1.summaryTableParser(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (index == tablesToDisplay.size()) {
                    drawSumOfLastRow();
                    NavHostFragment.findNavController(navhostFragment).navigate(R.id.distributorTableFragment);
                }
                else if(index == tablesToDisplay.size()+1){

                }else {
                    sumofLastRow(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity1.drawSummaryTable(tablesToDisplay, getContext(), summaryTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(scrollSummaryTable);
                }

            }

        };

        HandleRowAnimationThread changeTodoTitleThread = new HandleRowAnimationThread(respSize, SummaryTableFragment.changeTodoHandler);
        changeTodoTitleThread.start();

    }

    @Override
    public void onSuccess(JSONArray jsonArray) {
        try {
            Log.i("TAG", jsonArray.toString());
            summaryTableProgressBar.setVisibility(View.GONE);
            respSize = jsonArray.length();
            inflateTable(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(VolleyError error) {
        error.printStackTrace();
    }


    public void sumofLastRow(SummaryTableRow summaryTableRow) {
        sumOfVSICount = sumOfVSICount + summaryTableRow.getVsiCount();
        sumOfSalesCount = sumOfSalesCount + summaryTableRow.getSalesOutLateCount();
        sumOfSKUCount = sumOfSKUCount + summaryTableRow.getSkuCount();
        sumOfQuantity = sumOfQuantity + summaryTableRow.getQuantityCount();
        sumOfTotalSales = sumOfTotalSales + summaryTableRow.getTotalSalesAmountAfterTax();
        sumOfActiveVans = sumOfActiveVans + summaryTableRow.getActiveVans();
        sumOfProspects = sumOfProspects + summaryTableRow.getProspect();

    }

    public void drawSumOfLastRow() {
        View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.table_row_summary, null, false);

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

        serialNumberTextView.setText("");
        distributorNameTextView.setText("");
        startTimeTextView.setText("");
        lastActivityTextView.setText("");
        totalVsiTextView.setText(numberFormat.format(sumOfVSICount));
        totalOutlatesTextView.setText(String.valueOf(sumOfSalesCount));
        totalSkuTextView.setText(String.valueOf(sumOfSKUCount));
        totalQuantityTextView.setText(String.valueOf(sumOfQuantity));
        totalSalesTextView.setText(String.valueOf(sumOfTotalSales));
        activeVansTextView.setText(String.valueOf(sumOfActiveVans));
        prospectTextView.setText(String.valueOf(sumOfProspects));

        summaryTableLayout.addView(tableElements);
        UtilityFunctionsForActivity1.animate(summaryTableLayout, tableElements);
    }


}





















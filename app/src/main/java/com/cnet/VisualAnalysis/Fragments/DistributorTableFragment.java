package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.DistributorTableRow;
import com.cnet.VisualAnalysis.MainActivity;
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
    Fragment fragment;
    HandleRowAnimationThread handleRowAnimationThread;
    HandleDataChangeThread handleDataChangeThread;

    int sumofProspect, sumofOutlet, sumofSKU, sumofQuantity = 0;
    double sumofSales = 0;


    ////////
    ArrayList<DistributorTableRow> distributorTableRows;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MainActivity.distributorTableJSONArray == null) {
            VolleyHttp http = new VolleyHttp(getContext());
            http.makeGetRequest(URL, this);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_distributor_table, container, false);
        distributorTableLayout = view.findViewById(R.id.distributorTableLayout);
        distributorTableProgressBar = view.findViewById(R.id.distributorTableProgressBar);
        scrollDistributorTable = view.findViewById(R.id.scrollDistributorTable);
        distributorHeaderTextView = view.findViewById(R.id.distributorHeaderTextView);
        fragment = this;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.distributorTableJSONArray != null) {
            distributorTableProgressBar.setVisibility(View.GONE);
            inflateAllTables(MainActivity.distributorTableJSONArray);
        }

        backTraverse(fragment,R.id.summaryTableFragment);
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(JSONArray jsonArray, int dataIndex) {
        resetLastRowSummation();
        distributorTableLayout.removeAllViews();
        try {
            tablesToDisplay = UtilityFunctionsForActivity1.distributorTableParser(jsonArray, dataIndex);
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
                    drawLastRow();
                    UtilityFunctionsForActivity1.scrollRows(scrollDistributorTable);
                } else if (index == tablesToDisplay.size() + 1 && dataIndex ==jsonArray.length()-1) {
                    Log.i("from distributor", "navigating from distributor: ");
                    NavController navController = NavHostFragment.findNavController(fragment);
                    navController.navigate(R.id.vsmCardFragment);
                } else if(index<tablesToDisplay.size()){
                    lastRowSummation(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity1.drawDistributorTable(tablesToDisplay, getContext(), distributorTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(scrollDistributorTable);
                }
            }
        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), DistributorTableFragment.animationHandler, 200);
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

        handleDataChangeThread = new HandleDataChangeThread(DistributorTableFragment.changeDataHandler, jsonArray.length(), 30);
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

    public void resetLastRowSummation() {
        sumofProspect = 0;
        sumofOutlet = 0;
        sumofSKU = 0;
        sumofQuantity = 0;
        sumofSales = 0;
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
        distributorVSITV.setText("Total Amount");
        distributorVSITV.setTypeface(Typeface.DEFAULT_BOLD);
        distributorVSITV.setTextSize(25f);

        distributorProspectTV.setText(String.valueOf(sumofProspect));
        distributorProspectTV.setTypeface(Typeface.DEFAULT_BOLD);
        distributorProspectTV.setTextSize(25f);

        distributorSalesOutletTV.setText(String.valueOf(sumofOutlet));
        distributorSalesOutletTV.setTypeface(Typeface.DEFAULT_BOLD);
        distributorSalesOutletTV.setTextSize(25f);

        distributorSKUcountTV.setText(String.valueOf(sumofSKU));
        distributorSKUcountTV.setTypeface(Typeface.DEFAULT_BOLD);
        distributorSKUcountTV.setTextSize(25f);

        distributorQuantityCountTV.setText(String.valueOf(sumofQuantity));
        distributorQuantityCountTV.setTypeface(Typeface.DEFAULT_BOLD);
        distributorQuantityCountTV.setTextSize(25f);

        distributorTotalSalesTV.setText(numberFormat.format(sumofSales));
        distributorTotalSalesTV.setTypeface(Typeface.DEFAULT_BOLD);
        distributorTotalSalesTV.setTextSize(25f);


        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        distributorVSITV.startAnimation(animation);
        distributorProspectTV.startAnimation(animation);
        distributorSalesOutletTV.startAnimation(animation);
        distributorSalesOutletTV.startAnimation(animation);
        distributorSKUcountTV.startAnimation(animation);
        distributorQuantityCountTV.startAnimation(animation);
        distributorTotalSalesTV.startAnimation(animation);

        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));
        distributorTableLayout.addView(tableElements);
        UtilityFunctionsForActivity2.animate(distributorTableLayout, tableElements);

    }


    @Override
    public void onSuccess(JSONArray jsonArray) {
        MainActivity.distributorTableJSONArray = jsonArray;
        distributorTableProgressBar.setVisibility(View.GONE);
        try {
            inflateAllTables(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(VolleyError error) {

    }

    public void backTraverse(Fragment fragment, int id) {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavController navController = NavHostFragment.findNavController(fragment);
                navController.navigate(id);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if(handleRowAnimationThread!=null){
       handleRowAnimationThread.interrupt();
        }
        if(handleDataChangeThread!=null){
            handleDataChangeThread.interrupt();
        }
    }
}
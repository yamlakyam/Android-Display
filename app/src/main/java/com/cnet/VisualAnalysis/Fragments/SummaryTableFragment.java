package com.cnet.VisualAnalysis.Fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
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
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.SummaryTableRow;
import com.cnet.VisualAnalysis.MainActivity;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.StartingActivty;
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
    Fragment fragment;

    HandleRowAnimationThread handleRowAnimationThread;

    int sumOfVSICount, sumOfSalesCount, sumOfSKUCount, sumOfQuantity, sumOfActiveVans, sumOfProspects = 0;
    double sumOfTotalSales = 0;

    int respSize;

    public SummaryTableFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(MainActivity.summaryTableJSONArray==null){
            VolleyHttp http = new VolleyHttp(getContext());
            http.makeGetRequest(URL, this);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_summary_table, container, false);

        fragment=this;
        summaryTableLayout = view.findViewById(R.id.summaryTableLayout);
        summaryTableProgressBar = view.findViewById(R.id.summaryTableprogressBar);
        scrollSummaryTable = view.findViewById(R.id.scrollSummaryTable);

        backTraverse();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MainActivity.summaryTableJSONArray!=null){
            summaryTableProgressBar.setVisibility(View.GONE);
            inflateTable(MainActivity.summaryTableJSONArray);
        }
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(JSONArray jsonArray) {
        resetSumOfLastRow();
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
                    UtilityFunctionsForActivity1.scrollRows(scrollSummaryTable);
                }
                else if(index == tablesToDisplay.size()+1){
                    NavController navController = NavHostFragment.findNavController(fragment);
                    navController.navigate(R.id.distributorTableFragment);
                }else {
                    sumofLastRow(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity1.drawSummaryTable(tablesToDisplay, getContext(), summaryTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(scrollSummaryTable);
                }

            }

        };

        handleRowAnimationThread = new HandleRowAnimationThread(respSize, SummaryTableFragment.changeTodoHandler,200);
        handleRowAnimationThread.start();

    }

    @Override
    public void onSuccess(JSONArray jsonArray) {
        try {
            MainActivity.summaryTableJSONArray=jsonArray;
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

    public void resetSumOfLastRow(){
        sumOfVSICount =0;
        sumOfSalesCount = 0;
        sumOfSKUCount = 0;
        sumOfQuantity = 0;
        sumOfTotalSales =0;
        sumOfProspects = 0;
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
        totalVsiTextView.setTypeface(Typeface.DEFAULT_BOLD);
        totalVsiTextView.setTextSize(25f);

        totalOutlatesTextView.setText(String.valueOf(sumOfSalesCount));
        totalOutlatesTextView.setTypeface(Typeface.DEFAULT_BOLD);
        totalOutlatesTextView.setTextSize(25f);

        totalSkuTextView.setText(String.valueOf(sumOfSKUCount));
        totalSkuTextView.setTypeface(Typeface.DEFAULT_BOLD);
        totalSkuTextView.setTextSize(25f);

        totalQuantityTextView.setText(String.valueOf(sumOfQuantity));
        totalQuantityTextView.setTypeface(Typeface.DEFAULT_BOLD);
        totalQuantityTextView.setTextSize(25f);

        totalSalesTextView.setText(String.valueOf(sumOfTotalSales));
        totalSalesTextView.setTypeface(Typeface.DEFAULT_BOLD);
        totalSalesTextView.setTextSize(25f);

        activeVansTextView.setText(String.valueOf(sumOfActiveVans));
        activeVansTextView.setTypeface(Typeface.DEFAULT_BOLD);
        activeVansTextView.setTextSize(25f);

        prospectTextView.setText(String.valueOf(sumOfProspects));
        prospectTextView.setTypeface(Typeface.DEFAULT_BOLD);
        prospectTextView.setTextSize(25f);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        totalVsiTextView.startAnimation(animation);
        totalOutlatesTextView.startAnimation(animation);
        totalSkuTextView.startAnimation(animation);
        totalQuantityTextView.startAnimation(animation);
        totalSalesTextView.startAnimation(animation);
        activeVansTextView.startAnimation(animation);
        prospectTextView.startAnimation(animation);

        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));
        summaryTableLayout.addView(tableElements);
        UtilityFunctionsForActivity1.animate(summaryTableLayout, tableElements);
    }

    public void backTraverse() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent =new Intent(requireActivity(), StartingActivty.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        handleRowAnimationThread.interrupt();
    }
}





















package com.cnet.VisualAnalysis.Fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.SummaryTableRow;
import com.cnet.VisualAnalysis.MainActivity;
import com.cnet.VisualAnalysis.MapsActivity;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;

import java.text.NumberFormat;
import java.util.ArrayList;


public class SummaryTableFragment extends Fragment implements MainActivity.KeyPress {

    private TableLayout summaryTableLayout;
    public static Handler changeTodoHandler;
    private final String URL = "http://192.168.1.248:8001/api/ChartData/GetSalesDataForAllOrganizations";
    private ArrayList<SummaryTableRow> tablesToDisplay;
    private ProgressBar summaryTableProgressBar;
    ScrollView scrollSummaryTable;
    Fragment fragment;
    NavController navController;

    HandleRowAnimationThread handleRowAnimationThread;

    int sumOfVSICount, sumOfSalesCount, sumOfSKUCount, sumOfQuantity, sumOfActiveVans, sumOfProspects = 0;
    double sumOfTotalSales = 0;

    int respSize;

    public SummaryTableFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_summary_table, container, false);

        fragment = this;
        summaryTableLayout = view.findViewById(R.id.summaryTableLayout);
        summaryTableProgressBar = view.findViewById(R.id.summaryTableprogressBar);
        scrollSummaryTable = view.findViewById(R.id.scrollSummaryTable);

        if (SplashScreenActivity.allData.isEnableNavigation()) {
            backTraverse();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (SplashScreenActivity.allData.getFmcgData() != null) {
            summaryTableProgressBar.setVisibility(View.GONE);
            respSize = SplashScreenActivity.allData.getFmcgData().getSummaryTableRows().size();
            inflateTable(SplashScreenActivity.allData.getFmcgData().getSummaryTableRows());
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("HandlerLeak")
//    private void inflateTable(JSONArray jsonArray) {
    private void inflateTable(ArrayList<SummaryTableRow> tableRows) {
        resetSumOfLastRow();
        changeTodoHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }
                tablesToDisplay = tableRows;
                if (index == tablesToDisplay.size()) {
                    drawSumOfLastRow();
                    UtilityFunctionsForActivity1.scrollRows(scrollSummaryTable);
                } else if (index == tablesToDisplay.size() + 1) {
                    NavController navController = NavHostFragment.findNavController(fragment);

                    if (SplashScreenActivity.allData.getLayoutList().size() > 0) {

                        Log.i("first index", SplashScreenActivity.allData.getLayoutList().get(0) + "");
                        if (SplashScreenActivity.allData.getLayoutList().contains(0)) {
                            navController.navigate(R.id.distributorTableFragment);
                        } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
                            startActivity(new Intent(requireActivity(), MapsActivity.class));
                        } else if (SplashScreenActivity.allData.getLayoutList().contains(2)) {
                            navController.navigate(R.id.vsmTransactionFragment);
                        }
                    }
                } else {
                    sumofLastRow(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity1.drawSummaryTable(tablesToDisplay, getContext(), summaryTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(scrollSummaryTable);
                }

            }

        };

        handleRowAnimationThread = new HandleRowAnimationThread(respSize, SummaryTableFragment.changeTodoHandler, 200, this,0);
        handleRowAnimationThread.start();

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

    public void resetSumOfLastRow() {
        sumOfVSICount = 0;
        sumOfSalesCount = 0;
        sumOfSKUCount = 0;
        sumOfQuantity = 0;
        sumOfTotalSales = 0;
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
        startTimeTextView.setText("");
        lastActivityTextView.setText("");

        distributorNameTextView.setText("Total Amount");
        distributorNameTextView.setTypeface(Typeface.DEFAULT_BOLD);
        distributorNameTextView.setTextSize(25f);

        totalVsiTextView.setText(numberFormat.format(sumOfVSICount));
        totalVsiTextView.setTypeface(Typeface.DEFAULT_BOLD);
        totalVsiTextView.setTextSize(25f);

        totalOutlatesTextView.setText(numberFormat.format(sumOfSalesCount));
        totalOutlatesTextView.setTypeface(Typeface.DEFAULT_BOLD);
        totalOutlatesTextView.setTextSize(25f);

        totalSkuTextView.setText(numberFormat.format(sumOfSKUCount));
        totalSkuTextView.setTypeface(Typeface.DEFAULT_BOLD);
        totalSkuTextView.setTextSize(25f);

        totalQuantityTextView.setText(numberFormat.format(sumOfQuantity));
        totalQuantityTextView.setTypeface(Typeface.DEFAULT_BOLD);
        totalQuantityTextView.setTextSize(25f);

        totalSalesTextView.setText(numberFormat.format(Math.round(sumOfTotalSales * 100.0) / 100.0));
        totalSalesTextView.setTypeface(Typeface.DEFAULT_BOLD);
        totalSalesTextView.setTextSize(25f);

        activeVansTextView.setText(numberFormat.format(sumOfActiveVans));
        activeVansTextView.setTypeface(Typeface.DEFAULT_BOLD);
        activeVansTextView.setTextSize(25f);

        prospectTextView.setText(numberFormat.format(sumOfProspects));
        prospectTextView.setTypeface(Typeface.DEFAULT_BOLD);
        prospectTextView.setTextSize(25f);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        distributorNameTextView.startAnimation(animation);
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
                if (handleRowAnimationThread != null)
                    handleRowAnimationThread.interrupt();
                Intent intent = new Intent(requireActivity(), SplashScreenActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
    }


    @Override
    public void centerKey() {
        Log.i("centerKey", "summary");
        handleRowAnimationThread.interrupt();
        MainActivity.secondCenterKeyPause = !MainActivity.secondCenterKeyPause;


        if(!MainActivity.secondCenterKeyPause){
            navController = NavHostFragment.findNavController(fragment);
            navController.navigate(R.id.distributorTableFragment);
        }

    }

    @Override
    public void leftKey() {
        Log.i("leftKey", "summary");

        handleRowAnimationThread.interrupt();

        if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
            startActivity(new Intent(requireActivity(), MapsActivity.class));
        } else if (SplashScreenActivity.allData.getLayoutList().contains(2)) {
            navController = NavHostFragment.findNavController(fragment);
            navController.navigate(R.id.vsmTransactionFragment);
        } else {
            navController.navigate(R.id.vsmCardFragment);
        }
    }

    @Override
    public void rightKey() {

        Log.i("rightKey", "summary");

        handleRowAnimationThread.interrupt();
        navController = NavHostFragment.findNavController(fragment);
        navController.navigate(R.id.distributorTableFragment);
    }


}





















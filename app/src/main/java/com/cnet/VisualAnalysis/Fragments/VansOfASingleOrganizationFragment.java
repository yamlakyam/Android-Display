package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cnet.VisualAnalysis.Data.VsmTableDataForSingleVan;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;

import java.text.NumberFormat;
import java.util.ArrayList;


public class VansOfASingleOrganizationFragment extends Fragment {

    ScrollView scrollVanListTable;
    TableLayout vanListTableLayout;
    ArrayList<VsmTableDataForSingleVan> tablesToDisplay;
    public static Handler animationHandler;
    HandleRowAnimationThread handleRowAnimationThread;
    Fragment fragment;
    TextView OrgHeaderTextView;

    int salesOutLateCountSum, allLineItemCountSum = 0;
    double totalPriceSum = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vans_of_a_single_organization, container, false);
        scrollVanListTable = view.findViewById(R.id.scrollVanListTable);
        vanListTableLayout = view.findViewById(R.id.vanListTableLayout);
        OrgHeaderTextView = view.findViewById(R.id.OrgHeaderTextView);
        fragment = this;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor() != null) {
            inflateTable();
        }
    }

    @SuppressLint("HandlerLeak")
    public void inflateTable() {
        resetSumOfLastRow();
        if (vanListTableLayout != null) {
            vanListTableLayout.removeAllViews();
        }
        tablesToDisplay = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData();
        OrgHeaderTextView.setText(SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getNameOfDistributor());
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
                    drawSumOfLastRow();
                    UtilityFunctionsForActivity1.scrollRows(scrollVanListTable);
                } else if (index == tablesToDisplay.size() + 1) {
                    if (fragment != null) {
//                        NavController navController = NavHostFragment.findNavController(fragment);
//                        navController.navigate(R.id.vsmCardFragment);

//                        if (peakHourForAllPaused) {
//                            handleRowAnimationThread.interrupt();
//                        } else {
//                            navigate(fragment);
//                        }
                    }

                } else if (index < tablesToDisplay.size()) {
                    sumofLastRow(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity1.drawVansOfSingleOrgTable(tablesToDisplay, getContext(), vanListTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(scrollVanListTable);
                }
            }
        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, 200, this, 0);
        handleRowAnimationThread.start();
    }

    private void sumofLastRow(VsmTableDataForSingleVan vsmTableDataForSingleVan) {
        salesOutLateCountSum = salesOutLateCountSum + vsmTableDataForSingleVan.getSalesOutLateCount();
        allLineItemCountSum = allLineItemCountSum + vsmTableDataForSingleVan.getAllLineItemCount();
        totalPriceSum = totalPriceSum + vsmTableDataForSingleVan.getTotalPrice();
    }

    private void drawSumOfLastRow() {
        View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.table_row_vans_of_single_org, null, false);

        if (tableElements != null) {
            TextView singleOrgVansSerialNumberTV = tableElements.findViewById(R.id.singleOrgVansSerialNumberTV);
            TextView singleOrgVansVSITV = tableElements.findViewById(R.id.singleOrgVansVSITV);
            TextView singleOrgVansProspectTV = tableElements.findViewById(R.id.singleOrgVansProspectTV);
            TextView singleOrgVansEndTimeTV = tableElements.findViewById(R.id.singleOrgVansEndTimeTV);
            TextView singleOrgVansSalesOutletTV = tableElements.findViewById(R.id.singleOrgVansSalesOutletTV);
            TextView singleOrgVansQuantityCountTV = tableElements.findViewById(R.id.singleOrgVansQuantityCountTV);
            TextView singleOrgVansTotalSalesTV = tableElements.findViewById(R.id.singleOrgVansTotalSalesTV);


            NumberFormat numberFormat = NumberFormat.getInstance();
            singleOrgVansSerialNumberTV.setText("");
            singleOrgVansVSITV.setText("Total Amount");
            singleOrgVansVSITV.setTypeface(Typeface.DEFAULT_BOLD);
            singleOrgVansVSITV.setTextSize(25f);
            singleOrgVansProspectTV.setText(String.valueOf(1));
            singleOrgVansProspectTV.setTypeface(Typeface.DEFAULT_BOLD);
            singleOrgVansProspectTV.setTextSize(25f);
            singleOrgVansEndTimeTV.setText("");
            singleOrgVansSalesOutletTV.setText(numberFormat.format(salesOutLateCountSum));
            singleOrgVansSalesOutletTV.setTypeface(Typeface.DEFAULT_BOLD);
            singleOrgVansSalesOutletTV.setTextSize(25f);
            singleOrgVansQuantityCountTV.setText(numberFormat.format(allLineItemCountSum));
            singleOrgVansQuantityCountTV.setTypeface(Typeface.DEFAULT_BOLD);
            singleOrgVansQuantityCountTV.setTextSize(25f);
            singleOrgVansTotalSalesTV.setText(numberFormat.format(Math.round(totalPriceSum * 100.0) / 100.0));
            singleOrgVansTotalSalesTV.setTypeface(Typeface.DEFAULT_BOLD);
            singleOrgVansTotalSalesTV.setTextSize(25f);

            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
            singleOrgVansVSITV.startAnimation(animation);
            singleOrgVansProspectTV.startAnimation(animation);
            singleOrgVansSalesOutletTV.startAnimation(animation);
            singleOrgVansQuantityCountTV.startAnimation(animation);
            singleOrgVansTotalSalesTV.startAnimation(animation);

            tableElements.setBackgroundColor(Color.parseColor("#3f4152"));

            if (vanListTableLayout != null) {
                vanListTableLayout.addView(tableElements);
            }
            UtilityFunctionsForActivity1.animate(vanListTableLayout, tableElements);
        }

    }

    public void resetSumOfLastRow() {
        salesOutLateCountSum = 0;
        allLineItemCountSum = 0;
        totalPriceSum = 0;

    }

}
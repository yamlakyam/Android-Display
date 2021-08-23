package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.VsmTableDataForSingleVan;
import com.cnet.VisualAnalysis.MapsActivity;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class
VansOfASingleOrganizationFragment extends Fragment implements SecondActivity.KeyPress {

    ScrollView scrollVanListTable;
    TableLayout vanListTableLayout;
    ArrayList<VsmTableDataForSingleVan> tablesToDisplay;
    public Handler animationHandler;
    HandleRowAnimationThread handleRowAnimationThread;
    Fragment fragment;
    TextView OrgHeaderTextView;

    LinearLayout vanListKeyPad;
    ImageView vanListleftArrow;
    ImageView vanListplayPause;
    ImageView vanListrightArrow;

    int salesOutLateCountSum, allLineItemCountSum = 0;
    double totalPriceSum = 0;
    public static boolean vanListPaused;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!SecondActivity.pausedstate()) {
            vanListPaused = false;
        } else {
            vanListPaused = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vans_of_a_single_organization, container, false);
        scrollVanListTable = view.findViewById(R.id.scrollVanListTable);
        vanListTableLayout = view.findViewById(R.id.vanListTableLayout);
        OrgHeaderTextView = view.findViewById(R.id.OrgHeaderTextView);
        fragment = this;
        vanListKeyPad = view.findViewById(R.id.vanListKeyPad);
        vanListleftArrow = view.findViewById(R.id.vanListleftArrow);
        vanListplayPause = view.findViewById(R.id.vanListplayPause);
        vanListrightArrow = view.findViewById(R.id.vanListrightArrow);
        keyPadControl(vanListPaused);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SplashScreenActivity.allData != null) {
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

        try {
            Collections.sort(tablesToDisplay, new Comparator<VsmTableDataForSingleVan>() {
                @Override
                public int compare(VsmTableDataForSingleVan o1, VsmTableDataForSingleVan o2) {
                    return Integer.parseInt(o1.nameOfVan.substring(3)) < Integer.parseInt(o2.nameOfVan.substring(3)) ? -1 : 0;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                    drawSumOfLastRow();
                    new UtilityFunctionsForActivity1().scrollRows(scrollVanListTable);
                } else if (index == tablesToDisplay.size() + 1) {
                    if (fragment != null) {
                        if (vanListPaused) {
                            if (handleRowAnimationThread != null) {
                                handleRowAnimationThread.interrupt();
                            }
                        } else {
                            navigate();
                        }
                    }

                } else if (index < tablesToDisplay.size()) {
                    sumofLastRow(tablesToDisplay.get(index));
                    new UtilityFunctionsForActivity1().drawVansOfSingleOrgTable(tablesToDisplay, getContext(), vanListTableLayout, index);
                    new UtilityFunctionsForActivity1().scrollRows(scrollVanListTable);
                }
            }
        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, 200, this, 0);
        handleRowAnimationThread.start();
    }

    public void navigate() {
        if (getActivity() != null && fragment.isAdded()) {
            try {
                startActivity(new Intent(requireActivity(), MapsActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void navigateLeft(Fragment fragment) {

        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(12))
            navController.navigate(R.id.peakHourReportFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(11))
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
            navController.navigate(R.id.userReportForEachOusFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
            navController.navigate(R.id.userReportForAllOusFragment2);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
            navController.navigate(R.id.branchSummaryFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(7))
            navController.navigate(R.id.summaryOfLastMonthFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(6))
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(5))
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(3))
            navController.navigate(R.id.summarizedByArticleFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(1))
            startActivity(new Intent(requireActivity(), MapsActivity.class));


    }


    private void sumofLastRow(VsmTableDataForSingleVan vsmTableDataForSingleVan) {
        salesOutLateCountSum = salesOutLateCountSum + vsmTableDataForSingleVan.getSalesOutLateCount();
        allLineItemCountSum = allLineItemCountSum + vsmTableDataForSingleVan.getAllLineItemCount();
        totalPriceSum = totalPriceSum + vsmTableDataForSingleVan.getTotalPrice();
    }

    private void drawSumOfLastRow() {
        if (getContext() != null) {
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
                new UtilityFunctionsForActivity1().animate(vanListTableLayout, tableElements);
            }
        }
    }

    public void resetSumOfLastRow() {
        salesOutLateCountSum = 0;
        allLineItemCountSum = 0;
        totalPriceSum = 0;

    }

    @Override
    public void centerKey() {
        vanListPaused = !vanListPaused;

        if (!vanListPaused) {
            navigate();

            SecondActivity.playAll();
        } else {
            SecondActivity.pauseAll();
        }
        keyPadControl(vanListPaused);
    }

    @Override
    public void leftKey() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        navigateLeft(fragment);
    }

    @Override
    public void rightKey() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        navigate();

    }

    public void keyPadControl(boolean paused) {
        if (paused) {
            vanListplayPause.setImageResource(R.drawable.ic_play_button__2_);
            vanListplayPause.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireActivity(), R.color.playbacksForeground)));
            vanListKeyPad.setVisibility(View.VISIBLE);

        } else {
            vanListKeyPad.setVisibility(View.GONE);
        }
    }

}
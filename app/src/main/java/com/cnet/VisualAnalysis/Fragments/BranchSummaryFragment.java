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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.BranchSummaryTableRow;
import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.StartingActivty;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.DashBoardDataParser;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;

import org.json.JSONArray;

import java.text.NumberFormat;
import java.util.ArrayList;

public class BranchSummaryFragment extends Fragment implements VolleyHttp.GetRequest {

    TableLayout branchSummaryTableLayout;
    ProgressBar branchSummaryProgressBar;
    ScrollView scrollBranchSummaryTable;
    Handler animationHandler;
    Fragment fragment;

    public static HandleRowAnimationThread handleRowAnimationThread;

    public static boolean isInflatingTable;
    int totalQuantity = 0;
    double grandTotal = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SecondActivity.interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                SummarizedByArticleChildCategFragment.handleRowAnimationThread,
                SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                SummaryOfLastMonthFragment.handleRowAnimationThread);

        if (SecondActivity.dashBoardData == null) {
            VolleyHttp http = new VolleyHttp(getContext());
            http.makeGetRequest(Constants.DashboardURL, this);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_branch_summary, container, false);
        branchSummaryTableLayout = view.findViewById(R.id.branchSummaryTableLayout);
        branchSummaryProgressBar = view.findViewById(R.id.branchSummaryProgressBar);
        scrollBranchSummaryTable = view.findViewById(R.id.scrollBranchSummaryTable);
        fragment = this;

        backTraverse(fragment, R.id.summaryOfLastMonthFragment);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SecondActivity.dashBoardData != null && !isInflatingTable) {
            initFragment(SecondActivity.dashBoardData,200);
        }
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<BranchSummaryTableRow> tablesToDisplay, int seconds) {
        grandTotal = 0;
        totalQuantity = 0;

        branchSummaryTableLayout.removeAllViews();
        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {

                Log.i("branch", SecondActivity.summaryOfBranchPause + "");
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }

                if (index == tablesToDisplay.size()) {
                    drawLastBranchSummaryRow();
                    UtilityFunctionsForActivity1.scrollRows(scrollBranchSummaryTable);

                } else if (index == tablesToDisplay.size() + 1 && !SecondActivity.summaryOfBranchPause) {
//                    NavController navController = NavHostFragment.findNavController(fragment);
//                    navController.navigate(R.id.summarizedByArticleFragment2);
//                    SecondActivity secondActivity = new SecondActivity();
//                    secondActivity.navigations(fragment);
                    navigate(fragment);

                } else if (index < tablesToDisplay.size()) {
                    totalLastRow(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity2.drawBranchSummary(tablesToDisplay, getContext(), branchSummaryTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(scrollBranchSummaryTable);
                }

            }

        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, seconds);
        handleRowAnimationThread.start();
    }


    public void initFragment(DashBoardData dashBoardDataParam,int seconds) {
        isInflatingTable = true;
        branchSummaryProgressBar.setVisibility(View.GONE);
        Log.i("success", fragment + "");

        DashBoardData dashBoardData = dashBoardDataParam;
        inflateTable(dashBoardData.getBranchSummaryData().getBranchSummaryTableRows(), seconds);

    }


    public void backTraverse(Fragment fragment, int id) {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override

            public void handleOnBackPressed() {
//                NavController navController = NavHostFragment.findNavController(fragment);
//                navController.navigate(id);

                Intent intent = new Intent(getActivity(), StartingActivty.class);
                startActivity(intent);

            }
        });
    }

    public void totalLastRow(BranchSummaryTableRow row) {
        totalQuantity = totalQuantity + row.getQuantity();
        grandTotal = grandTotal + row.getGrandTotal();
    }

    public void drawLastBranchSummaryRow() {
        View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.table_row_branch_summary, null, false);

        TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowBranchSummary1);
        TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowBranchSummary2);
        TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowBranchSummary3);
        TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowBranchSummary4);
        TextView tableRowProperty5 = tableElements.findViewById(R.id.tableRowBranchSummary5);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        tableRowProperty1.setText("");
        tableRowProperty2.setText("Total Amount");
        tableRowProperty2.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty2.setTextSize(25f);
        tableRowProperty3.setText(String.valueOf(totalQuantity));
        tableRowProperty3.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty3.setTextSize(25f);
        tableRowProperty4.setText("");
        tableRowProperty5.setText(numberFormat.format(grandTotal));
        tableRowProperty5.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty5.setTextSize(25f);


        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        tableRowProperty3.startAnimation(animation);
        tableRowProperty5.startAnimation(animation);

        branchSummaryTableLayout.addView(tableElements);
        UtilityFunctionsForActivity2.animate(branchSummaryTableLayout, tableElements);
    }

    public void navigate(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        SecondActivity secondActivity = new SecondActivity();
        if (secondActivity.visibleFragments[0]) {
            navController.navigate(R.id.summarizedByArticleFragment2);
        } else if (secondActivity.visibleFragments[1]) {
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
        } else if (secondActivity.visibleFragments[2]) {
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        } else if (secondActivity.visibleFragments[3]) {
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        } else if (secondActivity.visibleFragments[4]) {
            navController.navigate(R.id.summaryOfLastMonthFragment);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (handleRowAnimationThread != null)
            handleRowAnimationThread.interrupt();
    }

    @Override
    public void onSuccess(JSONArray jsonArray) {
        Log.i("branch success", "onSuccess: from branch ");
        SecondActivity.dashBoardArray = jsonArray;
        DashBoardDataParser dashBoardDataParser = new DashBoardDataParser(jsonArray);
        DashBoardData dashBoardData = dashBoardDataParser.parseDashBoardData();
        SecondActivity.dashBoardData = dashBoardData;
        initFragment(dashBoardData, 200);
    }

    @Override
    public void onFailure(VolleyError error) {

    }
}
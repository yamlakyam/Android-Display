package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.BranchSummaryData;
import com.cnet.VisualAnalysis.Data.BranchSummaryTableRow;
import com.cnet.VisualAnalysis.Data.SummarizedByArticleData;
import com.cnet.VisualAnalysis.Data.SummarizedByArticleTableRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class BranchSummaryFragment extends Fragment  {

    TableLayout branchSummaryTableLayout;
    ProgressBar branchSummaryProgressBar;
    ScrollView scrollBranchSummaryTable;
    Handler animationHandler;
    Fragment fragment;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_branch_summary, container, false);
        branchSummaryTableLayout=view.findViewById(R.id.branchSummaryTableLayout);
        branchSummaryProgressBar=view.findViewById(R.id.branchSummaryProgressBar);
        scrollBranchSummaryTable=view.findViewById(R.id.scrollBranchSummaryTable);
        fragment=this;

        if(SecondActivity.dashBoardArray!=null){
            initFragment(SecondActivity.dashBoardArray);
        }

//        backTraverse(fragment,);

        return view;
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<BranchSummaryTableRow> tablesToDisplay) {
        branchSummaryTableLayout.removeAllViews();
        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }

                if (index == tablesToDisplay.size()) {
//                    drawLast6MonsTotalRow();
//                    UtilityFunctionsForActivity1.scrollRows(summByArticleScrollView);
                } else if (index == tablesToDisplay.size()+1) {

                    NavController navController = NavHostFragment.findNavController(fragment);
                    navController.navigate(R.id.summarizedByArticleFragment2);

                } else {
//                    totalLastRow(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity2.drawBranchSummary(tablesToDisplay, getContext(), branchSummaryTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(scrollBranchSummaryTable);
                }

            }

        };

        HandleRowAnimationThread handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler);
        handleRowAnimationThread.start();
    }


    public void initFragment(JSONArray jsonArray) {
        branchSummaryProgressBar.setVisibility(View.GONE);
        Log.i("success", fragment + "");
        try {
            BranchSummaryData branchSummaryData = UtilityFunctionsForActivity2.branchSummaryParser(jsonArray);
            inflateTable(branchSummaryData.getBranchSummaryTableRows());

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
}
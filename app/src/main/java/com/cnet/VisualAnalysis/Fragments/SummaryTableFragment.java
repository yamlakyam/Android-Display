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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.SummaryTableRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctions;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class SummaryTableFragment extends Fragment implements VolleyHttp.GetRequest {

    private TableLayout summaryTableLayout;
    public static Handler changeTodoHandler;
    private final String URL = "http://192.168.1.248:8001/api/ChartData/GetSalesDataForAllOrganizations";
    private ArrayList<SummaryTableRow> tablesToDisplay;
    private ProgressBar summaryTableProgressBar;
    ScrollView scrollSummaryTable;

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
                    tablesToDisplay = UtilityFunctions.summaryTableParser(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                UtilityFunctions.drawSummaryTable(tablesToDisplay, getContext(), summaryTableLayout, index);
                UtilityFunctions.scrollRows(scrollSummaryTable);

            }

        };

        HandleRowAnimationThread changeTodoTitleThread = new HandleRowAnimationThread(respSize,SummaryTableFragment.changeTodoHandler);
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


}





















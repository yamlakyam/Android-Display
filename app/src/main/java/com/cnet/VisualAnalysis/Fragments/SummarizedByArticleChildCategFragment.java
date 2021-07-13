package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.SummarizedByChildArticleData;
import com.cnet.VisualAnalysis.Data.SummarizedByChildArticleRow;
import com.cnet.VisualAnalysis.Data.SummarizedByParentArticleRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SummarizedByArticleChildCategFragment extends Fragment implements VolleyHttp.GetRequest {

    TableLayout summaryByChildArticleTableLayout;
    ScrollView summarizedByChildArticleScrollView;
    Handler animationHandler;
    BarChart barChart;
    PieChart pieChart;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VolleyHttp http =new VolleyHttp(getContext());
        http.makeGetRequest(Constants.DashboardURL,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_summarized_by_article_child_categ, container, false);
        summaryByChildArticleTableLayout=view.findViewById(R.id.summaryByChildArticleTableLayout);
        summarizedByChildArticleScrollView=view.findViewById(R.id.summarizedByChildArticleScrollView);
        pieChart=view.findViewById(R.id.pchartsumByArticleChild);
        barChart=view.findViewById(R.id.bChartSumByArticleChild);
        return view;
    }

    @Override
    public void onSuccess(JSONArray jsonArray) {
        try {
            SummarizedByChildArticleData summarizedByChildArticleData = UtilityFunctionsForActivity2.summarizedByChildArticleParser(jsonArray);
            inflateTable(summarizedByChildArticleData.getTableData());
            UtilityFunctionsForActivity2.drawBarChart(summarizedByChildArticleData.getBarChartData(), barChart, "Summarized by Article Child Category");
            UtilityFunctionsForActivity2.drawPieChart(summarizedByChildArticleData.getPieChartData(), pieChart,"Summarized by Article Child Category");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(VolleyError error) {

    }
    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummarizedByChildArticleRow> tablesToDisplay) {

        summaryByChildArticleTableLayout.removeAllViews();
        animationHandler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }
                if(index==tablesToDisplay.size()){

                }else{

                    UtilityFunctionsForActivity2.drawSummaryByChildArticleTable(tablesToDisplay, getContext(), summaryByChildArticleTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(summarizedByChildArticleScrollView);
                }

            }

        };

        HandleRowAnimationThread handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler);
        handleRowAnimationThread.start();
    }

}
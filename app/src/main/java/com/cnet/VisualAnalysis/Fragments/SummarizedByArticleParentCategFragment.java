package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.SummarizedByParentArticleData;
import com.cnet.VisualAnalysis.Data.SummarizedByParentArticleRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SummarizedByArticleParentCategFragment extends Fragment implements VolleyHttp.GetRequest {

    private final String URL = "http://192.168.1.248:8001/api/DashBoardData/GetDashBoardData";
    Handler animationHandler;

    TableLayout summarizedByParentArticleTableLayout;
    ScrollView scrollView;
    PieChart pieChart;
    BarChart barChart;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VolleyHttp http = new VolleyHttp(getContext());
        http.makeGetRequest(URL, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summarized_by_article_parent_categ, container, false);
        summarizedByParentArticleTableLayout = view.findViewById(R.id.summaryByChildArticleTableLayout);
        scrollView = view.findViewById(R.id.summarizedByChildArticleScrollView);
        pieChart=view.findViewById(R.id.pchartsumByArticleParent);
        barChart=view.findViewById(R.id.bChartSumByArticleParent);
        return view;
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummarizedByParentArticleRow> tablesToDisplay) {

        summarizedByParentArticleTableLayout.removeAllViews();
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

                    UtilityFunctionsForActivity2.drawSummaryByParentArticleTable(tablesToDisplay, getContext(), summarizedByParentArticleTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(scrollView);
                }

            }

        };

        HandleRowAnimationThread handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler);
        handleRowAnimationThread.start();
    }


    @Override
    public void onSuccess(JSONArray jsonArray) {

        try {
            SummarizedByParentArticleData summarizedByParentArticleData = UtilityFunctionsForActivity2.summarizedByParentArticleParser(jsonArray);
            inflateTable(summarizedByParentArticleData.getTableData());
            UtilityFunctionsForActivity2.drawBarChart(summarizedByParentArticleData.getBarChartData(), barChart, "Summarized by Article parent category");
            UtilityFunctionsForActivity2.drawPieChart(summarizedByParentArticleData.getPieChartData(), pieChart,"Summarized by Article parent category");

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onFailure(VolleyError error) {

    }
}
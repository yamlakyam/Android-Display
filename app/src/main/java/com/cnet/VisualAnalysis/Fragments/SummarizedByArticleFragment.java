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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.BarChartData;
import com.cnet.VisualAnalysis.Data.PieChartData;
import com.cnet.VisualAnalysis.Data.SummarizedByArticleData;
import com.cnet.VisualAnalysis.Data.SummarizedByArticleTableRow;
import com.cnet.VisualAnalysis.Data.SummarizedByParentArticleData;
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

public class SummarizedByArticleFragment extends Fragment implements VolleyHttp.GetRequest {

    BarChart barChartSumByArticle;
    PieChart pieChartSumByArticle;
    TableLayout summarizedByArticleTableLayout;
    private final String URL = "http://192.168.1.248:8001/api/DashBoardData/GetDashBoardData";
    Handler animationHandler;
    TextView scrollingArticleText;
    ScrollView summByArticleScrollView;

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
        View view = inflater.inflate(R.layout.fragment_summarized_by_article, container, false);

        barChartSumByArticle = view.findViewById(R.id.bChartSumByArticle);
        pieChartSumByArticle = view.findViewById(R.id.pchartsumByArticle);
        summarizedByArticleTableLayout = view.findViewById(R.id.summaryByArticleTableLayout);
        scrollingArticleText = view.findViewById(R.id.scrollingArticleText);
        scrollingArticleText.setSelected(true);
        summByArticleScrollView=view.findViewById(R.id.summByArticleScrollView);

        return view;
    }


    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummarizedByArticleTableRow> tablesToDisplay) {
        summarizedByArticleTableLayout.removeAllViews();
        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }

                if(index==tablesToDisplay.size()){

                }
                else{
                    UtilityFunctionsForActivity2.drawSummaryByArticleTable(tablesToDisplay, getContext(), summarizedByArticleTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(summByArticleScrollView);
                }

            }

        };

        HandleRowAnimationThread handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler);
        handleRowAnimationThread.start();
    }


    @Override
    public void onSuccess(JSONArray jsonArray) {
        try {
            SummarizedByArticleData summarizedByArticleData = UtilityFunctionsForActivity2.summarizedByArticleParser(jsonArray);
            inflateTable(summarizedByArticleData.getTableData());
            UtilityFunctionsForActivity2.drawBarChart(summarizedByArticleData.getBarChartData(), barChartSumByArticle,"Summarized by Article");
            UtilityFunctionsForActivity2.drawPieChart(summarizedByArticleData.getPieChartData(), pieChartSumByArticle,"Summarized by Article"); } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onFailure(VolleyError error) {

    }
}
package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.BarChartData;
import com.cnet.VisualAnalysis.Data.PieChartData;
import com.cnet.VisualAnalysis.Data.SummarizedByArticleTableRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.Activity2UtilityFunctions;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import org.json.JSONArray;

import java.util.ArrayList;

public class SummarizedByArticleFragment extends Fragment implements VolleyHttp.GetRequest {

    BarChart barChartSumByArticle;
    PieChart pieChartSumByArticle;
    TableLayout summarizedByArticleTableLayout;
    private final String URL = "http://192.168.1.248:8001/api/ChartData/GetSalesDataForSingleOrganization";
    Handler animationHandler;
    TextView scrollingArticleText;

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


                Activity2UtilityFunctions.drawSummaryByArticleTable(tablesToDisplay, getContext(), summarizedByArticleTableLayout, index);
            }

        };

        HandleRowAnimationThread handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler);
        handleRowAnimationThread.start();
    }


    @Override
    public void onSuccess(JSONArray jsonArray) {
        ArrayList<SummarizedByArticleTableRow> allTableData = new ArrayList<>();
        allTableData.add(new SummarizedByArticleTableRow("High Severity", 3000, 3111.2, 12005892));
        allTableData.add(new SummarizedByArticleTableRow("High Severity", 3000, 3111.2, 12005892));
        allTableData.add(new SummarizedByArticleTableRow("High Severity", 3000, 3111.2, 12005892));

        PieChartData pieChartData = new PieChartData(
                new float[]{2400, 500, 1000},
                new String[]{"Hardware", "Software", "Service"}
        );

        BarChartData barChartData = new BarChartData(
                new float[]{1, 2, 3, 4, 6},
                new float[]{100, 25, 85, 56, 45},
                new String[]{"JAN", "FEB", "MAR", "APR", "MAY"}
        );

        inflateTable(allTableData);
        Activity2UtilityFunctions.drawBarChart(barChartData, barChartSumByArticle);
        Activity2UtilityFunctions.drawPieChart(pieChartData, pieChartSumByArticle);

    }

    @Override
    public void onFailure(VolleyError error) {

    }
}
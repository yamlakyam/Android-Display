package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.SummarizedByArticleData;
import com.cnet.VisualAnalysis.Data.SummarizedByArticleTableRow;
import com.cnet.VisualAnalysis.MainActivity;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;

public class SummarizedByArticleFragment extends Fragment implements VolleyHttp.GetRequest {

    BarChart barChartSumByArticle;
    LineChart lineChartSumByArticle;
    TableLayout summarizedByArticleTableLayout;
    Handler animationHandler;
    TextView scrollingArticleText;
    ScrollView summByArticleScrollView;
    ProgressBar articleSummaryProgressBar;
    ConstraintLayout constraintLayout;

    double totalUnitAmount = 0;
    int totalQuantity = 0;

    Fragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VolleyHttp http = new VolleyHttp(getContext());
        http.makeGetRequest(Constants.DashboardURL, this);
//        if(SecondActivity.dashBoardArray != null){
//            try {
//                SummarizedByArticleData summarizedByArticleData = UtilityFunctionsForActivity2.summarizedByArticleParser(SecondActivity.dashBoardArray);
//                inflateTable(summarizedByArticleData.getTableData());
//                UtilityFunctionsForActivity2.drawBarChart(summarizedByArticleData.getBarChartData(), barChartSumByArticle, "Summarized by Article");
//                UtilityFunctionsForActivity2.drawLineChart(summarizedByArticleData.getLineChartData(),lineChartSumByArticle);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summarized_by_article, container, false);
        fragment = this;

        barChartSumByArticle = view.findViewById(R.id.bChartSumByArticle);
        lineChartSumByArticle = view.findViewById(R.id.lchartsumByArticle);
        summarizedByArticleTableLayout = view.findViewById(R.id.summaryByArticleTableLayout);
        scrollingArticleText = view.findViewById(R.id.scrollingArticleText);
        scrollingArticleText.setSelected(true);
        summByArticleScrollView = view.findViewById(R.id.summByArticleScrollView);
        articleSummaryProgressBar=view.findViewById(R.id.articleSummaryProgressBar);
        constraintLayout=view.findViewById(R.id.constraintLayout);

//        backTraverse(fragment, R.id.summaryOfLastMonthFragment);

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

                if (index == tablesToDisplay.size()) {
                    drawLast6MonsTotalRow();
                    UtilityFunctionsForActivity1.scrollRows(summByArticleScrollView);
                } else if (index == tablesToDisplay.size()+1) {

                    NavController navController = NavHostFragment.findNavController(fragment);
                    navController.navigate(R.id.summarizedByArticleParentCategFragment);

                } else {
                    totalLastRow(tablesToDisplay.get(index));
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
            articleSummaryProgressBar.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.VISIBLE);
            SecondActivity.dashBoardArray = jsonArray;
            Log.i("success", fragment + "");
            SummarizedByArticleData summarizedByArticleData = UtilityFunctionsForActivity2.summarizedByArticleParser(jsonArray);
            inflateTable(summarizedByArticleData.getTableData());
            UtilityFunctionsForActivity2.drawBarChart(summarizedByArticleData.getBarChartData(), barChartSumByArticle, "Summarized by Article");
            UtilityFunctionsForActivity2.drawLineChart(summarizedByArticleData.getLineChartData(),lineChartSumByArticle);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(VolleyError error) {

    }

    public void totalLastRow(SummarizedByArticleTableRow row) {

        totalQuantity = totalQuantity + row.getQuantity();
        totalUnitAmount = totalUnitAmount + row.getAvgAmount();

    }

    public void drawLast6MonsTotalRow() {
        View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.table_row_summary_by_article, null, false);

        TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowArticleProperty1);
        TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowArticleProperty2);
        TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowArticleProperty3);
        TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowArticleProperty4);
        TextView tableRowProperty5 = tableElements.findViewById(R.id.tableRowArticleProperty5);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);


        tableRowProperty1.setText("");
        tableRowProperty2.setText("Total Amount");
        tableRowProperty3.setText(numberFormat.format(totalQuantity));
        tableRowProperty4.setText(numberFormat.format(Math.round(totalUnitAmount * 100.0) / 100.0));
        tableRowProperty5.setText("");
        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        tableRowProperty3.startAnimation(animation);
        tableRowProperty4.startAnimation(animation);

        summarizedByArticleTableLayout.addView(tableElements);
        UtilityFunctionsForActivity1.animate(summarizedByArticleTableLayout, tableElements);
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
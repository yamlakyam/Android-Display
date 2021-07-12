package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.SummarizedByParentArticleRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.Activity2UtilityFunctions;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;

import org.json.JSONArray;

import java.util.ArrayList;

public class SummarizedByArticleParentCategFragment extends Fragment implements VolleyHttp.GetRequest {

    private final String URL = "http://192.168.1.248:8001/api/ChartData/GetSalesDataForSingleOrganization";
    Handler animationHandler;

    TableLayout summarizedByParentArticleTableLayout;


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
        summarizedByParentArticleTableLayout = view.findViewById(R.id.summaryByParentArticleTableLayout);
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


                Activity2UtilityFunctions.drawSummaryByParentArticleTable(tablesToDisplay, getContext(), summarizedByParentArticleTableLayout, index);
            }

        };

        HandleRowAnimationThread handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler);
        handleRowAnimationThread.start();
    }


    @Override
    public void onSuccess(JSONArray jsonArray) {
        ArrayList<SummarizedByParentArticleRow> allTableData = new ArrayList<>();
        allTableData.add(new SummarizedByParentArticleRow("Adminstrative Officer","25.62%",2566.8));
        allTableData.add(new SummarizedByParentArticleRow("Sales","25.62%",2566.8));
        allTableData.add(new SummarizedByParentArticleRow("Waiter","25.62%",2566.8));
        allTableData.add(new SummarizedByParentArticleRow("Adminstrative Officer","25.62%",2566.8));
        allTableData.add(new SummarizedByParentArticleRow("Adminstrative Officer","25.62%",2566.8));

        inflateTable(allTableData);

    }

    @Override
    public void onFailure(VolleyError error) {

    }
}
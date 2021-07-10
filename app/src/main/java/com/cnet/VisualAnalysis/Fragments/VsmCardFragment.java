package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.Threads.HandleDataChangeThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctions;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;

import org.json.JSONArray;
import org.json.JSONException;

public class VsmCardFragment extends Fragment implements VolleyHttp.GetRequest {

    GridView vsmCardGridLayout;
    public static Handler changeDataHandler;
    public static final String URL = "http://192.168.1.248:8001/api/ChartData/GetSalesDataToDisplayVsmCards";
    ProgressBar vsmCardProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VolleyHttp http = new VolleyHttp(getContext());
        http.makeGetRequest(URL, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vsm_card, container, false);
        vsmCardGridLayout = view.findViewById(R.id.vsmCardGridLayout);
        vsmCardProgressBar = view.findViewById(R.id.vsmCardProgressBar);

        return view;
    }

    @SuppressLint("HandlerLeak")
    public void inflateAllCompanyCards(JSONArray jsonArray) {

        changeDataHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String) msg.obj;
                int index = Integer.parseInt(message);

                try {
                    UtilityFunctions.drawVSMCard(jsonArray, index, getContext(), vsmCardGridLayout);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        HandleDataChangeThread handleDataChangeThread = new HandleDataChangeThread(changeDataHandler, jsonArray.length(),30);
        handleDataChangeThread.start();
    }

    @Override
    public void onSuccess(JSONArray jsonArray) {
        try {
            vsmCardProgressBar.setVisibility(View.GONE);
            inflateAllCompanyCards(jsonArray);
        } catch (Exception e) {
            vsmCardProgressBar.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(VolleyError error) {

    }
}
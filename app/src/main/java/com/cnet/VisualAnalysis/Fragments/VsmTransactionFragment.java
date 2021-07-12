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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.VsmTableDataForSingleVan;
import com.cnet.VisualAnalysis.Data.VsmTableForSingleDistributor;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.Threads.HandleDataChangeThread;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctions;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class VsmTransactionFragment extends Fragment implements VolleyHttp.GetRequest {

    public static final String URL = "http://192.168.1.248:8001/api/ChartData/GetSalesDataToDisplayOnVsmTable";
    public static Handler changeVanHandler;
    public static Handler changeDistributorHandler;
    static Handler animationHandler;
    TableLayout vsmTransactionTableLayout;
    ProgressBar VSMtransactioProgressBar;
    ScrollView scrollVSMtable;
    ArrayList<VsmTableForSingleDistributor> tablesToDisplay;
    TextView distributorHeaderVsmTransaction;
    TextView vanHeaderVsmTransaction;

    public int distributors = 3;
    public int vansCount = 2;
    public int allRows = 5;

    public VsmTransactionFragment() {

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

        View view = inflater.inflate(R.layout.fragment_vsm_transaction, container, false);
        vsmTransactionTableLayout = view.findViewById(R.id.VSMtableLayout);
        VSMtransactioProgressBar = view.findViewById(R.id.VSMtransactioProgressBar);
        scrollVSMtable = view.findViewById(R.id.scrollVSMtable);
        tablesToDisplay = new ArrayList<>();
        vanHeaderVsmTransaction = view.findViewById(R.id.vanHeaderVsmTransaction);
        distributorHeaderVsmTransaction = view.findViewById(R.id.distributorHeaderVsmTransaction);

        return view;
    }


    @SuppressLint("HandlerLeak")
    private void inflateAlldistributors(ArrayList<VsmTableForSingleDistributor> allOrgData) {
        distributors = allOrgData.size();
        changeDistributorHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                String message = (String) msg.obj;
                int distributorIndex = 0;
                if (message != null) {
                    distributorIndex = Integer.parseInt(message);
                }

                inflateAllVansInSingleDis(allOrgData, distributorIndex);
            }
        };

        HandleDataChangeThread handleDistDataChangeThread = new HandleDataChangeThread(changeDistributorHandler, distributors, 500);
        handleDistDataChangeThread.start();
    }

    @SuppressLint("HandlerLeak")
    private void inflateAllVansInSingleDis(ArrayList<VsmTableForSingleDistributor> allOrgData, int distributorIndex) {
        vansCount = allOrgData.get(distributorIndex).getAllVansData().size();
        String nameOfOrg = allOrgData.get(distributorIndex).getNameOfDistributor();

        distributorHeaderVsmTransaction.setText(nameOfOrg);
        changeVanHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                String message = (String) msg.obj;
                int vanIndex = 0;
                if (message != null) {
                    vanIndex = Integer.parseInt(message);
                }

                inflateTable(allOrgData, vanIndex, distributorIndex);

            }
        };

        HandleDataChangeThread handleVanDataChangeThread = new HandleDataChangeThread(changeVanHandler, vansCount, 40);
        handleVanDataChangeThread.start();
    }


    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<VsmTableForSingleDistributor> allOrgData, int vanIndex, int distributorIndex) {
        vsmTransactionTableLayout.removeAllViews();
        allRows = allOrgData.get(distributorIndex).getAllVansData().get(vanIndex).getTableRows().size();

        animationHandler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }

                UtilityFunctions.drawVsmTransactionTable(allOrgData, getContext(), vsmTransactionTableLayout, distributorIndex, vanIndex, index, requireView());
                UtilityFunctions.scrollRows(scrollVSMtable);
            }
        };

        HandleRowAnimationThread handleRowAnimationThread = new HandleRowAnimationThread(allRows, VsmTransactionFragment.animationHandler);
        handleRowAnimationThread.start();

    }

    @Override
    public void onSuccess(JSONArray jsonArray) {
        VSMtransactioProgressBar.setVisibility(View.GONE);
        try {

            tablesToDisplay = UtilityFunctions.vsmTransactionParser(jsonArray);
//            inflateAllVansInSingleDis(tablesToDisplay);
            inflateAlldistributors(tablesToDisplay);
            Log.i("Success", "onSuccess: ");



        } catch (JSONException e) {
            VSMtransactioProgressBar.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(VolleyError error) {

    }
}
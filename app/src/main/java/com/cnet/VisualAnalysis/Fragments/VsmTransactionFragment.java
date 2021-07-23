package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
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
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.VsmTableForSingleDistributor;
import com.cnet.VisualAnalysis.MainActivity;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.Threads.HandleDataChangeThread;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.NumberFormat;
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
    Fragment fragment;
    HandleDataChangeThread handleDistDataChangeThread;
    HandleDataChangeThread handleVanDataChangeThread;
    HandleRowAnimationThread handleRowAnimationThread;
    int vanIndex, distributorIndex;
    public int distributors;
    public int vansCount;
    public int allRows;
    public int totalItemCount = 0;
    public double totalSubTotal = 0;
    public double grandTotalSales = 0;
    public ArrayList<Integer> numberOfVansInDistributors = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> numberOfRowsInSingleVan = new ArrayList<>();


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
        fragment = this;
        vsmTransactionTableLayout = view.findViewById(R.id.VSMtableLayout);
        VSMtransactioProgressBar = view.findViewById(R.id.VSMtransactioProgressBar);
        scrollVSMtable = view.findViewById(R.id.scrollVSMtable);
        tablesToDisplay = new ArrayList<>();
        vanHeaderVsmTransaction = view.findViewById(R.id.vanHeaderVsmTransaction);
        distributorHeaderVsmTransaction = view.findViewById(R.id.distributorHeaderVsmTransaction);

        backTraverse(fragment, R.id.vsmCardFragment);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (MainActivity.vsmTransactionJSONArray != null) {
//            try {
//                VSMtransactioProgressBar.setVisibility(View.GONE);
//                tablesToDisplay = UtilityFunctionsForActivity1.vsmTransactionParser(MainActivity.vsmTransactionJSONArray);
//                inflateAlldistributors(tablesToDisplay);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

    }

    @SuppressLint("HandlerLeak")
    private void inflateAlldistributors(ArrayList<VsmTableForSingleDistributor> allOrgData, int distributorStartIndex, int vanStartingIndex) {
        distributors = allOrgData.size();

        changeDistributorHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                String message = (String) msg.obj;
                distributorIndex = 0;
                if (message != null) {
                    distributorIndex = Integer.parseInt(message);
                }
                inflateAllVansInSingleDis(allOrgData, distributorIndex, vanStartingIndex);

                Log.i("DISTRIBUTOR INDEX", distributorIndex + "");
                Log.i("SUM", (int) (vansSum(numberOfRowsInSingleVan.get(distributorIndex))) + "");
            }
        };

        handleDistDataChangeThread = new HandleDataChangeThread(changeDistributorHandler, distributors, (int) (vansSum(numberOfRowsInSingleVan.get(distributorIndex))), distributorStartIndex);
        handleDistDataChangeThread.start();
    }

    @SuppressLint("HandlerLeak")
    private void inflateAllVansInSingleDis(ArrayList<VsmTableForSingleDistributor> allOrgData, int distributorIndex, int startingIndex) {
        vansCount = allOrgData.get(distributorIndex).getAllVansData().size();
        vanIndex = 0;
        changeVanHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                String message = (String) msg.obj;
                vanIndex = 0;
                if (message != null) {
                    vanIndex = Integer.parseInt(message);
                }
                inflateTable(allOrgData, vanIndex, distributorIndex);
            }
        };
        handleVanDataChangeThread = new HandleDataChangeThread(changeVanHandler, vansCount, numberOfRowsInSingleVan.get(distributorIndex).get(vanIndex), startingIndex);
        handleVanDataChangeThread.start();
    }


    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<VsmTableForSingleDistributor> allOrgData, int vanIndex, int distributorIndex) {
        totalItemCount = 0;
        totalSubTotal = 0;
        grandTotalSales = 0;
        if(vanHeaderVsmTransaction!=null){
            vanHeaderVsmTransaction.setText(allOrgData.get(distributorIndex).getAllVansData().get(vanIndex).nameOfVan);
        }
        String nameOfOrg = allOrgData.get(distributorIndex).getNameOfDistributor();
        if(distributorHeaderVsmTransaction!=null){
            distributorHeaderVsmTransaction.setText(nameOfOrg);
        }
        if(vsmTransactionTableLayout!=null){
            vsmTransactionTableLayout.removeAllViews();
        }
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
                if (index == allRows) {
                    drawLastRow();
                    UtilityFunctionsForActivity1.scrollRows(scrollVSMtable);
                } else if (index == allRows + 1 &&
                        vanIndex == allOrgData.get(distributorIndex).getAllVansData().size() - 1 &&
                        distributorIndex == allOrgData.size() - 1) {
                    NavController navController = NavHostFragment.findNavController(fragment);
                    navController.navigate(R.id.summaryTableFragment);
                } else if (index < allRows) {
                    lastRowSummation(allOrgData, vanIndex, distributorIndex, index);
                    UtilityFunctionsForActivity1.drawVsmTransactionTable(allOrgData, getContext(), vsmTransactionTableLayout, distributorIndex, vanIndex, index);
                    UtilityFunctionsForActivity1.scrollRows(scrollVSMtable);
                }

            }
        };

        handleRowAnimationThread = new HandleRowAnimationThread(allRows, VsmTransactionFragment.animationHandler, 100);
        handleRowAnimationThread.start();
    }


    @Override
    public void onSuccess(JSONArray jsonArray) {
        if(VSMtransactioProgressBar!=null){
            VSMtransactioProgressBar.setVisibility(View.GONE);

        }
        try {
            MainActivity.vsmTransactionJSONArray = jsonArray;
            tablesToDisplay = UtilityFunctionsForActivity1.vsmTransactionParser(jsonArray);
            for (int i = 0; i < tablesToDisplay.size(); i++) {
                numberOfVansInDistributors.add(tablesToDisplay.get(i).getAllVansData().size());
                ArrayList<Integer> numbers = new ArrayList<>();
                for (int j = 0; j < tablesToDisplay.get(i).getAllVansData().size(); j++) {
                    numbers.add(tablesToDisplay.get(i).getAllVansData().get(j).tableRows.size());
                }
                numberOfRowsInSingleVan.add(numbers);
            }


            Log.i("VANS COUNT", "NUMBER OF VANS : " + numberOfVansInDistributors.toString());
            Log.i("VANS COUNT", "NUMBER OF VANS : " + numberOfRowsInSingleVan.toString());

            inflateAlldistributors(tablesToDisplay, 0, 0);

        } catch (JSONException e) {
            VSMtransactioProgressBar.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(VolleyError error) {

    }

    public void drawLastRow() {
        View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.table_row_vsm_transaction, null, false);
        TextView snTextView = tableElements.findViewById(R.id.vsmTransSNtextView);
        TextView voucherNoTextView = tableElements.findViewById(R.id.vsmTransVoucherNtxtView);
        TextView outletTextView = tableElements.findViewById(R.id.vsmTransOutletTextView);
        TextView TINtextView = tableElements.findViewById(R.id.vsmTransTINtextView);
        TextView dateNtimeTextView = tableElements.findViewById(R.id.vsmTransDateNtimeTextV);
        TextView itemCountTextview = tableElements.findViewById(R.id.vsmTransItemCountTxtV);
        TextView subTotalTextView = tableElements.findViewById(R.id.vsmTransSubTotalTxtv);
        TextView VATtextView = tableElements.findViewById(R.id.vsmTransVATtextView);
        TextView totalSalesTextView = tableElements.findViewById(R.id.vsmTransGrandTotalTextView);

        NumberFormat numberFormat = NumberFormat.getInstance();

        numberFormat.setGroupingUsed(true);


        snTextView.setText("");

        voucherNoTextView.setText("");
        voucherNoTextView.setTypeface(Typeface.DEFAULT_BOLD);
        voucherNoTextView.setTextSize(25f);

        outletTextView.setText("Total Amount");
        outletTextView.setTypeface(Typeface.DEFAULT_BOLD);
        outletTextView.setTextSize(25f);
        TINtextView.setText("");
        dateNtimeTextView.setText("");
        itemCountTextview.setText(numberFormat.format(totalItemCount));
        itemCountTextview.setTypeface(Typeface.DEFAULT_BOLD);
        itemCountTextview.setTextSize(25f);

        subTotalTextView.setText(numberFormat.format(Math.round(totalSubTotal * 100.0) / 100.0));
        subTotalTextView.setTypeface(Typeface.DEFAULT_BOLD);
        subTotalTextView.setTextSize(25f);

        VATtextView.setText("");
        totalSalesTextView.setText(numberFormat.format(Math.round(grandTotalSales * 100.0) / 100.0));
        totalSalesTextView.setTypeface(Typeface.DEFAULT_BOLD);
        totalSalesTextView.setTextSize(25f);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        outletTextView.startAnimation(animation);
        itemCountTextview.startAnimation(animation);
        subTotalTextView.startAnimation(animation);
        totalSalesTextView.startAnimation(animation);

        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));
        if(vsmTransactionTableLayout!=null){
            vsmTransactionTableLayout.addView(tableElements);
        }
        UtilityFunctionsForActivity1.animate(vsmTransactionTableLayout, tableElements);
    }

    public void lastRowSummation(ArrayList<VsmTableForSingleDistributor> allOrgData, int vanIndex, int distributorIndex, int rowNo) {
        if (rowNo < allOrgData.get(distributorIndex).getAllVansData().get(vanIndex).getTableRows().size()) {
            totalItemCount = totalItemCount + allOrgData.get(distributorIndex).getAllVansData().get(vanIndex).getTableRows().get(rowNo).getItemCount();
            totalSubTotal = totalSubTotal + allOrgData.get(distributorIndex).getAllVansData().get(vanIndex).getTableRows().get(rowNo).getSubTotal();
            grandTotalSales = totalSubTotal + allOrgData.get(distributorIndex).getAllVansData().get(vanIndex).getTableRows().get(rowNo).getTotalSales();
        }
    }

    public void backTraverse(Fragment fragment, int id) {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {


                ArrayList<VsmTableForSingleDistributor> allOrgData = new ArrayList<>();
                try {
                    allOrgData = UtilityFunctionsForActivity1.vsmTransactionParser(MainActivity.vsmTransactionJSONArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (handleDistDataChangeThread != null) {
                    handleDistDataChangeThread.interrupt();
                }
                if (handleVanDataChangeThread != null) {
                    handleVanDataChangeThread.interrupt();
                }

                if (distributorIndex == 0) {
                    if (vanIndex == 0) {
                        NavController navController = NavHostFragment.findNavController(fragment);
                        navController.navigate(id);
                    }

                    if (vanIndex > 0) {
                        inflateAlldistributors(allOrgData, distributorIndex, vanIndex - 1);
                    }
                } else {
                    if (vanIndex == 0) {
                        inflateAlldistributors(allOrgData, distributorIndex - 1, vanIndex);
                    }

                    if (vanIndex > 0) {
                        inflateAlldistributors(allOrgData, distributorIndex, vanIndex - 1);
                    }

                }
            }

        });
    }

    public Integer vansSum(ArrayList<Integer> numbersToAdd){
        Integer sum = 0;

        for (int i = 0; i < numbersToAdd.size(); i++) {
            sum+=numbersToAdd.get(i);
        }
        return sum;
    }


    @Override
    public void onStop() {
        super.onStop();
        if (handleDistDataChangeThread != null) {

            handleDistDataChangeThread.interrupt();
        }
        if (handleVanDataChangeThread != null) {

            handleVanDataChangeThread.interrupt();
        }
        if (handleRowAnimationThread != null) {

            handleRowAnimationThread.interrupt();
        }
    }
}


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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.MainActivity;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.Threads.HandleDataChangeThread;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VsmCardFragment extends Fragment implements VolleyHttp.GetRequest {

    GridView vsmCardGridLayout;
    public static Handler changeDataHandler;
    public static final String URL = "http://192.168.1.248:8001/api/ChartData/GetSalesDataToDisplayVsmCards";
    ProgressBar vsmCardProgressBar;
    Fragment fragment;
    HandleDataChangeThread handleDataChangeThread;
    int distributorIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MainActivity.vsmCardJSONArray == null) {
            VolleyHttp http = new VolleyHttp(getContext());
            http.makeGetRequest(Constants.allDataWithConfigurationURL, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vsm_card, container, false);
        vsmCardGridLayout = view.findViewById(R.id.vsmCardGridLayout);
        vsmCardProgressBar = view.findViewById(R.id.vsmCardProgressBar);
        fragment = this;
        backTraverse(fragment, R.id.distributorTableFragment);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.vsmCardJSONArray != null) {
            vsmCardProgressBar.setVisibility(View.GONE);
            inflateAllCompanyCards(MainActivity.vsmCardJSONArray, 0);
        }
    }

    @SuppressLint("HandlerLeak")
    public void inflateAllCompanyCards(JSONArray jsonArray, int startingIndex) {

        changeDataHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String) msg.obj;
                int index = Integer.parseInt(message);
                distributorIndex = index;

                try {
                    if (index == jsonArray.length()) {
                        NavController navController = NavHostFragment.findNavController(fragment);
                        navController.navigate(R.id.vsmTransactionFragment);
                    } else {
                        UtilityFunctionsForActivity1.drawVSMCard(jsonArray, index, getContext(), vsmCardGridLayout);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        handleDataChangeThread = new HandleDataChangeThread(changeDataHandler, jsonArray.length() + 1, 30, startingIndex);
        handleDataChangeThread.start();
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONObject("consolidationObjectData").getJSONArray("getSalesDataToDisplayVsmCards");

            MainActivity.vsmCardJSONArray = jsonArray;
            vsmCardProgressBar.setVisibility(View.GONE);
            inflateAllCompanyCards(jsonArray, 0);
        } catch (Exception e) {
            if (vsmCardProgressBar != null) {
                vsmCardProgressBar.setVisibility(View.GONE);
            }
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(VolleyError error) {

    }

    public void backTraverse(Fragment fragment, int id) {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (handleDataChangeThread != null) {
                    handleDataChangeThread.interrupt();

                    if (distributorIndex == 0) {
                        NavController navController = NavHostFragment.findNavController(fragment);
                        navController.navigate(id);
                    } else {
                        inflateAllCompanyCards(MainActivity.vsmCardJSONArray, distributorIndex - 1);
                    }
//                    inflateAllTables(MainActivity.distributorTableJSONArray, 0);

                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (handleDataChangeThread != null) {
            handleDataChangeThread.interrupt();
        }
    }
}
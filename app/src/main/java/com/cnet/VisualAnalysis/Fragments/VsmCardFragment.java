package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.cnet.VisualAnalysis.MapsActivity;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleDataChangeThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;

import org.json.JSONException;

public class VsmCardFragment extends Fragment {

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vsm_card, container, false);
        vsmCardGridLayout = view.findViewById(R.id.vsmCardGridLayout);
        vsmCardProgressBar = view.findViewById(R.id.vsmCardProgressBar);
        fragment = this;

        if (SplashScreenActivity.allData.isEnableNavigation()) {
            backTraverse(fragment, R.id.distributorTableFragment);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (SplashScreenActivity.allData.getFmcgData().getVsmCards() != null) {
            vsmCardProgressBar.setVisibility(View.GONE);
            inflateAllCompanyCards(0);

        }
    }

    @SuppressLint("HandlerLeak")
//    public void inflateAllCompanyCards(JSONArray jsonArray, int startingIndex) {
    public void inflateAllCompanyCards(int startingIndex) {

        changeDataHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String) msg.obj;
                int index = Integer.parseInt(message);
                distributorIndex = index;

                try {
//                    if (index == jsonArray.length()) {
                    if (index == SplashScreenActivity.allData.getFmcgData().getVsmCards().size()) {
                        NavController navController = NavHostFragment.findNavController(fragment);
//                        navController.navigate(R.id.vsmTransactionFragment);

                        if (SplashScreenActivity.allData.getLayoutList().size() > 1) {
                            if (SplashScreenActivity.allData.getLayoutList().contains(2)) {
                                navController.navigate(R.id.vsmTransactionFragment);
                            } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
                                startActivity(new Intent(requireActivity(), MapsActivity.class));
                            }
                        }
                    } else {
                        UtilityFunctionsForActivity1.drawVSMCard(index, getContext(), vsmCardGridLayout);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

//        handleDataChangeThread = new HandleDataChangeThread(changeDataHandler, jsonArray.length() + 1, 30, startingIndex);
//        handleDataChangeThread = new HandleDataChangeThread(changeDataHandler, SplashScreenActivity.allData.getFmcgData().getVsmCards().size() + 1, 30, startingIndex);
        if (SplashScreenActivity.allData != null) {
            handleDataChangeThread = new HandleDataChangeThread(changeDataHandler, SplashScreenActivity.allData.getFmcgData().getVsmCards().size() + 1,
                    Integer.parseInt(SplashScreenActivity.allData.getTransitionTimeInMinutes()), startingIndex);
        } else
            handleDataChangeThread = new HandleDataChangeThread(changeDataHandler, SplashScreenActivity.allData.getFmcgData().getVsmCards().size() + 1, 30, startingIndex);
        handleDataChangeThread.start();
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
                        inflateAllCompanyCards(distributorIndex - 1);
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
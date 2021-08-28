package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

import com.cnet.VisualAnalysis.MainActivity;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleDataChangeThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;

import org.json.JSONException;

public class VsmCardFragment extends Fragment implements MainActivity.KeyPress {

    GridView vsmCardGridLayout;
    public Handler changeDataHandler;
    public HandleDataChangeThread handleDataChangeThread;
    ProgressBar vsmCardProgressBar;
    Fragment fragment;
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

//        VolleyHttp http = new VolleyHttp(getContext());
//        http.makeGetRequest(Constants.allDataWithConfigurationURL + "?imei=" + new SplashScreenActivity().getDeviceId(requireContext()),
//                this);

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
                    if (index == SplashScreenActivity.allData.getFmcgData().getVsmCards().size()) {
                        NavController navController = NavHostFragment.findNavController(fragment);

                        if (SplashScreenActivity.allData.getLayoutList().size() > 1) {
                            if (SplashScreenActivity.allData.getLayoutList().contains(2)) {
                                navController.navigate(R.id.vsmTransactionFragment);
                            } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
//                                startActivity(new Intent(requireActivity(), MapsActivity.class));
                            } else {
                                navController.navigate(R.id.summaryTableFragment);
                            }
                        } else {
                            navController.navigate(R.id.summaryTableFragment);
                        }
                    } else {
                        new UtilityFunctionsForActivity1().drawVSMCard(index, getContext(), vsmCardGridLayout);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


        if (SplashScreenActivity.allData != null) {
            handleDataChangeThread = new HandleDataChangeThread(changeDataHandler, SplashScreenActivity.allData.getFmcgData().getVsmCards().size() + 1,
                    Integer.parseInt(SplashScreenActivity.allData.getTransitionTimeInMinutes()), startingIndex);
        } else {
            handleDataChangeThread = new HandleDataChangeThread(changeDataHandler,
                    SplashScreenActivity.allData.getFmcgData().getVsmCards().size() + 1, 30, startingIndex);
        }
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

//    @Override
//    public void onSuccess(JSONObject jsonObject) throws JSONException {
//
//        Log.i("updated", "onSuccess: ");
//        AllDataParser allDataParser = new AllDataParser(jsonObject);
//        SplashScreenActivity.allData = allDataParser.parseAllData();
//    }
//
//    @Override
//    public void onFailure(VolleyError error) {
//        Toast.makeText(getContext(), "Failed updating", Toast.LENGTH_SHORT).show();
//
//    }

    @Override
    public void centerKey() {

        Log.i("vsmcard", "centerKey: ");
        if (handleDataChangeThread != null) {
            handleDataChangeThread.interrupt();
        }
        MainActivity.secondCenterKeyPause = !MainActivity.secondCenterKeyPause;

        if (!MainActivity.secondCenterKeyPause) {
            NavController navController = NavHostFragment.findNavController(fragment);

            if (distributorIndex == SplashScreenActivity.allData.getFmcgData().getVsmCards().size() - 1) {
                if (SplashScreenActivity.allData.getLayoutList().contains(2)) {
                    navController.navigate(R.id.vsmTransactionFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
//                    startActivity(new Intent(requireActivity(), MapsActivity.class));
                } else {
                    navController.navigate(R.id.summaryTableFragment);
                }
            } else {
                inflateAllCompanyCards(distributorIndex + 1);

            }
        }
    }

    @Override
    public void leftKey() {


    }

    @Override
    public void rightKey() {
        Log.i("IAM ", "right");
        if (handleDataChangeThread != null) {
            handleDataChangeThread.interrupt();
            NavController navController = NavHostFragment.findNavController(fragment);

            if (distributorIndex == SplashScreenActivity.allData.getFmcgData().getVsmCards().size() - 1) {
                if (SplashScreenActivity.allData.getLayoutList().contains(2)) {
                    navController.navigate(R.id.vsmTransactionFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
//                    startActivity(new Intent(requireActivity(), MapsActivity.class));
                } else {
                    navController.navigate(R.id.summaryTableFragment);
                }
            } else {
                inflateAllCompanyCards(distributorIndex + 1);

            }
        }
    }

//    @Override
//    public void dispatchKey(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
//            Log.i("center press", "dispatchKey: ");
//            MainActivity.secondCenterKeyPause = !MainActivity.secondCenterKeyPause;
//        } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
//            Log.i("IAM ", "right");
//            if (handleDataChangeThread != null) {
//                handleDataChangeThread.interrupt();
//                NavController navController = NavHostFragment.findNavController(fragment);
//
//                if (distributorIndex == SplashScreenActivity.allData.getFmcgData().getVsmCards().size() - 1) {
//                    if (SplashScreenActivity.allData.getLayoutList().contains(2)) {
//                        navController.navigate(R.id.vsmTransactionFragment);
//                    } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
//                        startActivity(new Intent(requireActivity(), MapsActivity.class));
//                    } else {
//                        navController.navigate(R.id.summaryTableFragment);
//                    }
//                } else {
//                    inflateAllCompanyCards(distributorIndex + 1);
//
//                }
//            }
//        }
//        else if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT){
//            Log.i("leftKey", "vsmCard");
//
//            if (handleDataChangeThread != null) {
//                handleDataChangeThread.interrupt();
//
//                if (distributorIndex == 0) {
//                    NavController navController = NavHostFragment.findNavController(fragment);
//                    navController.navigate(R.id.distributorTableFragment);
//                } else {
//                    inflateAllCompanyCards(distributorIndex - 1);
//                }
//
//            }
//        }
//    }

}
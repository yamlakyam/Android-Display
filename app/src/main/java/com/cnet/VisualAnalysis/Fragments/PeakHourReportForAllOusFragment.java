package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DigitalClock;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.FigureReportDataElements;
import com.cnet.VisualAnalysis.MapsActivity;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;

import java.util.ArrayList;

public class PeakHourReportForAllOusFragment extends Fragment implements SecondActivity.KeyPress {

    TableLayout peakHourReportForAllTableLayout;
    ScrollView peakHourReportScrollView;
    TextView scrollingPeakHourForAllText;
    DigitalClock digitalClock;
    public static Handler animationHandler;
    public HandleRowAnimationThread handleRowAnimationThread;
    private ArrayList<FigureReportDataElements> tablesToDisplay;
    Fragment fragment;

    boolean peakHourForAllPaused = false;

    double grandTotalSum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_peak_hour_report_for_all_ous, container, false);
        peakHourReportForAllTableLayout = view.findViewById(R.id.peakHourReportForAllTableLayout);
        peakHourReportScrollView = view.findViewById(R.id.peakHourReportScrollView);
        scrollingPeakHourForAllText = view.findViewById(R.id.scrollingPeakHourForAllText);
        scrollingPeakHourForAllText.setSelected(true);
        digitalClock = view.findViewById(R.id.digitalClock);
        digitalClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));
        fragment = this;


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch() != null) {
            inflateTable();

        }
    }

    @SuppressLint("HandlerLeak")
    public void inflateTable() {
        grandTotalSum = 0;
        if (peakHourReportForAllTableLayout != null) {
            peakHourReportForAllTableLayout.removeAllViews();
        }
        tablesToDisplay = SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch();
        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }

                if (index == tablesToDisplay.size()) {
//                    drawLastRow();
                    UtilityFunctionsForActivity1.scrollRows(peakHourReportScrollView);
                } else if (index == tablesToDisplay.size() + 1) {
                    if (fragment != null) {
//                        NavController navController = NavHostFragment.findNavController(fragment);
//                        navController.navigate(R.id.vsmCardFragment);
                        navigate(fragment);
                    }

                } else if (index < tablesToDisplay.size()) {
                    grandTotalSum = grandTotalSum + tablesToDisplay.get(index).grandTotal;
                    UtilityFunctionsForActivity1.drawPeakHourReportForAllOus(tablesToDisplay, getContext(), peakHourReportForAllTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(peakHourReportScrollView);
                }
            }
        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, 200, this, 0);
        handleRowAnimationThread.start();
    }


    public void navigate(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
            if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                navController.navigate(R.id.peakHourReportFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
                startActivity(new Intent(requireActivity(), MapsActivity.class));
            } else if (SplashScreenActivity.allData.getLayoutList().contains(3))
                navController.navigate(R.id.summarizedByArticleFragment2);
            else if (SplashScreenActivity.allData.getLayoutList().contains(4))
                navController.navigate(R.id.summarizedByArticleParentCategFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(5))
                navController.navigate(R.id.summarizedByArticleChildCategFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(6))
                navController.navigate(R.id.summaryOfLastSixMonthsFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(7))
                navController.navigate(R.id.summaryOfLastMonthFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(8))
                navController.navigate(R.id.branchSummaryFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(9))
                navController.navigate(R.id.userReportForAllOusFragment2);
            else if (SplashScreenActivity.allData.getLayoutList().contains(10))
                navController.navigate(R.id.userReportForEachOusFragment);
        }
    }

    public void navigateLeft(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
            navController.navigate(R.id.userReportForEachOusFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
            navController.navigate(R.id.userReportForAllOusFragment2);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(8)) {
            navController.navigate(R.id.branchSummaryFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(7))
            navController.navigate(R.id.summaryOfLastMonthFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(6))
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(5))
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(4))
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(3))
            navController.navigate(R.id.summarizedByArticleFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(1))
            startActivity(new Intent(requireActivity(), MapsActivity.class));
        else if (SplashScreenActivity.allData.getLayoutList().contains(12))
            navController.navigate(R.id.peakHourReportFragment);


    }

    @Override
    public void onStop() {
        super.onStop();
        handleRowAnimationThread.interrupt();
    }

    @Override
    public void centerKey() {
        peakHourForAllPaused = !peakHourForAllPaused;

        if (peakHourForAllPaused) {
            if (handleRowAnimationThread != null) {
                handleRowAnimationThread.interrupt();
            }
        } else {
            navigate(fragment);
        }
    }

    @Override
    public void leftKey() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        navigateLeft(fragment);
    }

    @Override
    public void rightKey() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        navigate(fragment);
    }
}
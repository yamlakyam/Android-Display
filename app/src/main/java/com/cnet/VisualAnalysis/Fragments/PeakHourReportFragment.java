package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleDataChangeThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.github.mikephil.charting.charts.LineChart;

import java.util.Calendar;

public class PeakHourReportFragment extends Fragment {

    public Handler changeDataHandler;
    public HandleDataChangeThread handleDataChangeThread;
    public LineChart lineChart;
    Fragment fragment;
    TextView peakHourReportTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_peak_hour_report, container, false);
        lineChart = view.findViewById(R.id.peakHourLineChart);
        peakHourReportTitle = view.findViewById(R.id.peakHourReportTitle);
        fragment = this;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i("formatted time", UtilityFunctionsForActivity1.peakHourFormatter("May  3 2021  8:00PM")+"");
        Log.i("formatted time", UtilityFunctionsForActivity1.peakHourFormatter("May  3 2021  8:00PM").compareTo(Calendar.getInstance().getTime())+"");

        if (SplashScreenActivity.allData.getDashBoardData().getAllFigureReportData() != null) {
            drawAllPeakTimeLineCharts(0);
        }
    }

    @SuppressLint("HandlerLeak")
    public void drawAllPeakTimeLineCharts(int startingIndex) {
        changeDataHandler = new Handler() {
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String) msg.obj;
                int index = Integer.parseInt(message);
//                distributorIndex = index;


                if (index == SplashScreenActivity.allData.getDashBoardData().getAllFigureReportData().size()) {
//                        NavController navController = NavHostFragment.findNavController(fragment);
//
//                        if (SplashScreenActivity.allData.getLayoutList().size() > 1) {
//                            if (SplashScreenActivity.allData.getLayoutList().contains(2)) {
//                                navController.navigate(R.id.vsmTransactionFragment);
//                            } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
//                                startActivity(new Intent(requireActivity(), MapsActivity.class));
//                            } else {
//                                navController.navigate(R.id.summaryTableFragment);
//                            }
//                        } else {
//                            navController.navigate(R.id.summaryTableFragment);
//                        }
                } else {
                    UtilityFunctionsForActivity2.drawLineChart(
                            SplashScreenActivity.allData.getDashBoardData().getAllFigureReportData().get(index).lineChartData,
                            lineChart, "Peak Hour Report");
                    peakHourReportTitle.setText("Figure Report for " + SplashScreenActivity.allData.getDashBoardData().getAllFigureReportData().get(index).org);

                    Log.i("date", SplashScreenActivity.allData.getDashBoardData().getAllFigureReportData().get(index).lineChartData.x_labels + "");
                }
            }
        };

        if (SplashScreenActivity.allData != null) {
            handleDataChangeThread = new HandleDataChangeThread(changeDataHandler, SplashScreenActivity.allData.getDashBoardData().getAllFigureReportData().size() + 1,
                    Integer.parseInt(SplashScreenActivity.allData.getTransitionTimeInMinutes()), startingIndex);
        } else {
            handleDataChangeThread = new HandleDataChangeThread(changeDataHandler,
                    SplashScreenActivity.allData.getDashBoardData().getAllFigureReportData().size() + 1, 30, startingIndex);
        }
        handleDataChangeThread.start();
    }

}
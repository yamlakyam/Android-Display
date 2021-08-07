package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cnet.VisualAnalysis.Data.FigureReportDataElements;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleDataChangeThread;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.github.mikephil.charting.charts.LineChart;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PeakHourReportFragment extends Fragment {

    public static Handler animationHandler;
    public static Handler changeDataHandler;
    public HandleDataChangeThread handleDataChangeThread;
    public HandleRowAnimationThread handleRowAnimationThread;
    public LineChart lineChart;
    Fragment fragment;
    TextView peakHourReportTitle;
    TableLayout peakHourReportTableLayout;
    ScrollView peakHourReportScrollView;

    private ArrayList<FigureReportDataElements> tablesToDisplay;

    double grandTotalSum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_peak_hour_report, container, false);
        lineChart = view.findViewById(R.id.peakHourLineChart);
        peakHourReportTitle = view.findViewById(R.id.peakHourReportTitle);

        peakHourReportTableLayout = view.findViewById(R.id.peakHourReportTableLayout);
        peakHourReportScrollView = view.findViewById(R.id.peakHourReportScrollView);

        fragment = this;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

//        Log.i("formatted time", UtilityFunctionsForActivity1.peakHourFormatter("May  3 2021  8:00PM")+"");
//        Log.i("formatted time", UtilityFunctionsForActivity1.peakHourFormatter("May  3 2021  8:00PM").compareTo(Calendar.getInstance().getTime())+"");

        if (SplashScreenActivity.allData.getDashBoardData().getAllFigureReportData() != null) {
            drawAllPeakTimeLineCharts(0);
        }
        Date parsedDate = convertToTime(SplashScreenActivity.allData.getDashBoardData().getAllFigureReportData().get(0).figureReportDataElementsArrayList.get(0).dateNTime);
        Log.i("time", parsedDate + "");
        Log.i("time-cal", Calendar.getInstance().getTime() + "");
        Log.i("compare", compareDate(parsedDate, Calendar.getInstance().getTime()));
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
                    inflateTable(index);

//                    SplashScreenActivity.allData.getDashBoardData().getAllFigureReportData().get(index).figureReportDataElementsArrayList.get
//                    Log.i("date", SplashScreenActivity.allData.getDashBoardData().getAllFigureReportData().get(index).lineChartData.x_labels + "");
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

    @SuppressLint("HandlerLeak")
    private void inflateTable(int dataIndex) {
        grandTotalSum = 0;
        if (peakHourReportTableLayout != null) {
            peakHourReportTableLayout.removeAllViews();
        }

        tablesToDisplay = SplashScreenActivity.allData.getDashBoardData().getAllFigureReportData().get(dataIndex).figureReportDataElementsArrayList;

        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }

                if (index == tablesToDisplay.size()) {
                    drawLastRow();
                    UtilityFunctionsForActivity1.scrollRows(peakHourReportScrollView);
                } else if (index == tablesToDisplay.size() + 1 && dataIndex == SplashScreenActivity.allData.getDashBoardData().getAllFigureReportData().size() - 1) {
                    if (fragment != null) {
//                        NavController navController = NavHostFragment.findNavController(fragment);
//                        navController.navigate(R.id.vsmCardFragment);
                    }

                } else if (index < tablesToDisplay.size()) {
                    grandTotalSum = grandTotalSum + tablesToDisplay.get(index).grandTotal;
                    UtilityFunctionsForActivity1.drawPeakHourReportForEachOu(tablesToDisplay, getContext(), peakHourReportTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(peakHourReportScrollView);
                }
            }
        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, 200, this);
        handleRowAnimationThread.start();
    }

    private void drawLastRow() {
    }

    public static Date convertToTime(String dateInString) {
        SimpleDateFormat input = new SimpleDateFormat("MMM dd yyyy HH:mmaa");

        Date parsed = null;
        try {
            parsed = input.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsed;
    }

    public String compareDate(Date parsedDateTime, Date currentDate) {
        if (parsedDateTime.getYear() != currentDate.getYear()) {
            return "different year";
        } else if (parsedDateTime.getMonth() != currentDate.getMonth()) {
            return "different month";
        } else if (parsedDateTime.getDate() != currentDate.getDate()) {
            return "different date ";
        }
        return "same date";
    }
}
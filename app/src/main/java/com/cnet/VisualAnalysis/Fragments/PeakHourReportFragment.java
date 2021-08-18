package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.FigureReportData;
import com.cnet.VisualAnalysis.Data.FigureReportDataElements;
import com.cnet.VisualAnalysis.MapsActivity;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleDataChangeThread;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.cnet.VisualAnalysis.VideoActivity;
import com.github.mikephil.charting.charts.LineChart;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PeakHourReportFragment extends Fragment implements SecondActivity.KeyPress {

    public static Handler animationHandler;
    public static Handler changeDataHandler;
    public HandleDataChangeThread handleDataChangeThread;
    public HandleRowAnimationThread handleRowAnimationThread;
    public LineChart lineChart;
    Fragment fragment;
    TextView peakHourReportTitle;
    TextView scrollingPeakText;
    TableLayout peakHourReportTableLayout;
    ScrollView peakHourReportScrollView;
    DigitalClock digitalClock;
    int branchIndex = 0;
    public static boolean peakHourForEachPaused;
    ImageView peakHrEachleftArrow;
    ImageView peakHrEachrightArrow;
    ImageView peakHrEachplaypause;
    LinearLayout peakHrEachKeyPad;


    private ArrayList<FigureReportDataElements> tablesToDisplay;

    double grandTotalSum;
    int totalCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!SecondActivity.pausedstate()) {
            peakHourForEachPaused = false;
        } else {
            peakHourForEachPaused = true;
        }

        int count = getallFigureReportSize(SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch());
        Log.i("non-null_count", count + "");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_peak_hour_report, container, false);


        lineChart = view.findViewById(R.id.peakHourLineChart);
        peakHourReportTitle = view.findViewById(R.id.peakHourReportTitle);

        peakHourReportTableLayout = view.findViewById(R.id.peakHourReportTableLayout);
        peakHourReportScrollView = view.findViewById(R.id.peakHourReportScrollView);
        scrollingPeakText = view.findViewById(R.id.scrollingPeakText);

        peakHrEachleftArrow = view.findViewById(R.id.peakHrEachleftArrow);
        peakHrEachrightArrow = view.findViewById(R.id.peakHrEachrightArrow);
        peakHrEachplaypause = view.findViewById(R.id.peakHrEachplayPause);
        peakHrEachKeyPad = view.findViewById(R.id.peakHrEachKeyPad);

        scrollingPeakText.setSelected(true);
        digitalClock = view.findViewById(R.id.digitalClock);
        digitalClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));

        keyPadControl(peakHourForEachPaused);

        fragment = this;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

//        Log.i("formatted time", UtilityFunctionsForActivity1.peakHourFormatter("May  3 2021  8:00PM")+"");
//        Log.i("formatted time", UtilityFunctionsForActivity1.peakHourFormatter("May  3 2021  8:00PM").compareTo(Calendar.getInstance().getTime())+"");

        if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch() != null) {
            drawAllPeakTimeLineCharts(0, 0);
        }
        Date parsedDate = convertToTime(SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(0).figureReportDataElementsArrayList.get(0).dateNTime);

    }

    @SuppressLint("HandlerLeak")
    public void drawAllPeakTimeLineCharts(int startingIndex, int startingRowIndex) {
        changeDataHandler = new Handler() {
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String message = (String) msg.obj;
                int index = Integer.parseInt(message);
//                distributorIndex = index;


                if (index == SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size()) {

                } else {

                    if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(index).figureReportDataElementsArrayList.size() > 0) {
                        UtilityFunctionsForActivity2.drawLineChart(
                                SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(index).lineChartData,
                                lineChart, "Peak Hour Report");
                        peakHourReportTitle.setText("Peak Hour Report for " + SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(index).org);
                        peakHourReportTitle.append(" from " + new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()));

                        inflateTable(index, startingRowIndex);
                        branchIndex = index;
                    }
                }
            }
        };

        if (SplashScreenActivity.allData != null) {
//            handleDataChangeThread = new HandleDataChangeThread(changeDataHandler, SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size() + 1,
//                    Integer.parseInt(SplashScreenActivity.allData.getTransitionTimeInMinutes()), startingIndex);
            int secondsForEachReport = Integer.parseInt(SplashScreenActivity.allData.getTransitionTimeInMinutes()) / SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size();

            handleDataChangeThread = new HandleDataChangeThread(changeDataHandler, SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size() + 1,
                    secondsForEachReport, startingIndex);
        } else {

            handleDataChangeThread = new HandleDataChangeThread(changeDataHandler,
                    SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size() + 1, totalTime(200), startingIndex);
        }
        handleDataChangeThread.start();
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(int dataIndex, int startingRowIndex) {
        grandTotalSum = 0;
        totalCount = 0;
        if (peakHourReportTableLayout != null) {
            peakHourReportTableLayout.removeAllViews();
        }

        tablesToDisplay = SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(dataIndex).figureReportDataElementsArrayList;

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
//                } else if (index == tablesToDisplay.size() + 1 && dataIndex == SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size() - 1) {
                } else if (index == tablesToDisplay.size() + 1 && dataIndex == getallFigureReportSize(SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch()) - 1) {
                    if (fragment != null) {

                        if (peakHourForEachPaused) {
                            if (handleRowAnimationThread != null) {
                                handleRowAnimationThread.interrupt();
                            }
                        } else {
                            navigate(fragment);
                        }
                    }

                } else if (index < tablesToDisplay.size()) {

                    grandTotalSum = grandTotalSum + tablesToDisplay.get(index).grandTotal;
                    totalCount = totalCount + 1;
                    UtilityFunctionsForActivity1.drawPeakHourReportForEachOu(tablesToDisplay, getContext(), peakHourReportTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(peakHourReportScrollView);
                }
            }
        };
        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, 200, this, startingRowIndex);
        handleRowAnimationThread.start();
    }

    public void navigate(Fragment fragment) {

        Intent intent = new Intent(getActivity(), VideoActivity.class);
        intent.putExtra("from", 12);
        startActivity(intent);

//        NavController navController = NavHostFragment.findNavController(fragment);
//        if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
//            if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
////                startActivity(new Intent(requireActivity(), MapsActivity.class));
//                navController.navigate(R.id.vansOfASingleOrganizationFragment);
//
//            } else if (SplashScreenActivity.allData.getLayoutList().contains(3))
//                navController.navigate(R.id.summarizedByArticleFragment2);
//            else if (SplashScreenActivity.allData.getLayoutList().contains(4))
//                navController.navigate(R.id.summarizedByArticleParentCategFragment);
//            else if (SplashScreenActivity.allData.getLayoutList().contains(5))
//                navController.navigate(R.id.summarizedByArticleChildCategFragment);
//            else if (SplashScreenActivity.allData.getLayoutList().contains(6))
//                navController.navigate(R.id.summaryOfLastSixMonthsFragment);
//            else if (SplashScreenActivity.allData.getLayoutList().contains(7))
//                navController.navigate(R.id.summaryOfLastMonthFragment);
//            else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
//                navController.navigate(R.id.branchSummaryFragment);
//            else if (SplashScreenActivity.allData.getLayoutList().contains(9))
//                navController.navigate(R.id.userReportForAllOusFragment2);
//            else if (SplashScreenActivity.allData.getLayoutList().contains(10))
//                navController.navigate(R.id.userReportForEachOusFragment);
//            else if (SplashScreenActivity.allData.getLayoutList().contains(11))
//                navController.navigate(R.id.peakHourReportForAllOusFragment);
//        }
    }

    public void leftNavigate(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(11))
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(10))
            navController.navigate(R.id.userReportForEachOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(9))
            navController.navigate(R.id.userReportForAllOusFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
            navController.navigate(R.id.branchSummaryFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(7))
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
//            navController.navigate(R.id.vansOfASingleOrganizationFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(12))
            navController.navigate(R.id.peakHourReportFragment);
    }


    private void drawLastRow() {
        View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.table_row_summary_by_parent_article, null, false);

        TextView peakHourReportSN = tableElements.findViewById(R.id.tableRowParentArtProperty1);
        TextView peakHourReportSummaryType = tableElements.findViewById(R.id.tableRowParentArtProperty2);
        TextView peakHourReportTotalCount = tableElements.findViewById(R.id.tableRowParentArtProperty3);
        TextView peakHourReportGrandTotal = tableElements.findViewById(R.id.tableRowParentArtProperty4);

        NumberFormat numberFormat = NumberFormat.getInstance();

        peakHourReportSN.setText("");

        peakHourReportSummaryType.setText("Total Amount");
        peakHourReportSummaryType.setTypeface(Typeface.DEFAULT_BOLD);
        peakHourReportSummaryType.setTextSize(16f);

        peakHourReportTotalCount.setText(String.valueOf(totalCount));
        peakHourReportTotalCount.setTypeface(Typeface.DEFAULT_BOLD);
        peakHourReportTotalCount.setTextSize(16f);

        peakHourReportGrandTotal.setText(numberFormat.format(grandTotalSum));
        peakHourReportGrandTotal.setTypeface(Typeface.DEFAULT_BOLD);
        peakHourReportGrandTotal.setTextSize(16f);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        peakHourReportSummaryType.startAnimation(animation);
        peakHourReportTotalCount.startAnimation(animation);
        peakHourReportGrandTotal.startAnimation(animation);

        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));

        if (peakHourReportTableLayout != null) {
            peakHourReportTableLayout.addView(tableElements);
            UtilityFunctionsForActivity2.animateBottomToTop(peakHourReportTableLayout, tableElements);

        }
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

    @Override
    public void onStop() {
        super.onStop();
        if (handleDataChangeThread != null) {
            handleDataChangeThread.interrupt();
        }
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
    }

    @Override
    public void centerKey() {
        peakHourForEachPaused = !peakHourForEachPaused;
//        SecondActivity.firstCenterKeyPause = peakHourForEachPaused;
        if (peakHourForEachPaused) {
            if (handleDataChangeThread != null) {
                handleDataChangeThread.interrupt();
            }
        }
        if (!peakHourForEachPaused) {
//            if (branchIndex == SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size() - 1) {
            if (branchIndex == getallFigureReportSize(SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch())-1) {
                navigate(fragment);
                SecondActivity.playAll();
            } else {
                drawAllPeakTimeLineCharts(branchIndex + 1, 0);
            }
        } else {
            SecondActivity.pauseAll();
        }
        keyPadControl(peakHourForEachPaused);
    }

    @Override
    public void leftKey() {
        if (handleDataChangeThread != null) {
            handleDataChangeThread.interrupt();
        }
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        if (branchIndex == 0) {
            leftNavigate(fragment);
        } else {
            drawAllPeakTimeLineCharts(branchIndex - 1, 0);
        }

    }

    @Override
    public void rightKey() {
        if (handleDataChangeThread != null) {
            handleDataChangeThread.interrupt();
        }
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        if (branchIndex == SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size() - 1) {
            navigate(fragment);
        } else {
            drawAllPeakTimeLineCharts(branchIndex + 1, 0);
        }
    }

    public void keyPadControl(boolean paused) {
        if (paused) {
            peakHrEachplaypause.setImageResource(R.drawable.ic_play_button__2_);
            peakHrEachplaypause.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireActivity(), R.color.playbacksForeground)));
            peakHrEachKeyPad.setVisibility(View.VISIBLE);

        } else {
            peakHrEachKeyPad.setVisibility(View.GONE);
        }
    }

    public int totalTime(int eachRowDuration) {
        int lastrowsDuration = 11000;
        int allRowsDuration = eachRowDuration * SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(branchIndex).figureReportDataElementsArrayList.size() + 1;
        int totalDuration = lastrowsDuration + allRowsDuration;
        return totalDuration;
    }

    public int getallFigureReportSize(ArrayList<FigureReportData> figureReportDatas) {
        int count = 0;
        for (FigureReportData l : figureReportDatas) {
            if (l.figureReportDataElementsArrayList.size() > 0) {
                count = count + 1;
            }
        }
        return count;
    }
}
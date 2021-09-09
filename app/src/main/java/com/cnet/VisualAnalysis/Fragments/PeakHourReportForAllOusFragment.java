package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.BarChartData;
import com.cnet.VisualAnalysis.Data.FigureReportDataElements;
import com.cnet.VisualAnalysis.Data.LineChartData;
import com.cnet.VisualAnalysis.Data.PieChartData;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.DashBoardDataParser;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PeakHourReportForAllOusFragment extends Fragment implements SecondActivity.KeyPress {

    TableLayout peakHourReportForAllTableLayout;
    ScrollView peakHourReportScrollView;
    TextView scrollingPeakHourForAllText;
    TextClock peakHourAll_textClock;
    public Handler animationHandler;
    public HandleRowAnimationThread handleRowAnimationThread;
    private ArrayList<FigureReportDataElements> tablesToDisplay;
    ArrayList<FigureReportDataElements> mergedFigureData;
    Fragment fragment;
    ImageView peakHrAllleftArrow;
    ImageView peakHrAllrightArrow;
    ImageView peakHrAllplaypause;
    LinearLayout linearLayout;

    MaterialCardView peakHourReportLineCard;

    public static boolean peakHourForAllPaused;
    double grandTotalSum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SecondActivity.pausedstate()) {
            peakHourForAllPaused = false;
        } else {
            peakHourForAllPaused = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_peak_hour_report_for_all_ous, container, false);

        peakHourReportForAllTableLayout = view.findViewById(R.id.peakHourReportForAllTableLayout);
        peakHourReportScrollView = view.findViewById(R.id.peakHourReportScrollView);
        scrollingPeakHourForAllText = view.findViewById(R.id.scrollingPeakHourForAllText);
        peakHrAllleftArrow = view.findViewById(R.id.peakHrAllleftArrow);
        peakHrAllplaypause = view.findViewById(R.id.peakHrAllplayPause);
        peakHrAllrightArrow = view.findViewById(R.id.peakHrAllrightArrow);
        linearLayout = view.findViewById(R.id.peakHrForAllKeypad);
        scrollingPeakHourForAllText.setSelected(true);
        peakHourAll_textClock = view.findViewById(R.id.peakHourAll_textClock);
        peakHourAll_textClock.setFormat12Hour("kk:mm:ss");
        peakHourAll_textClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));
        peakHourReportLineCard = view.findViewById(R.id.peakHourReportLineCard);
        fragment = this;

        keyPadControl(peakHourForAllPaused);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SplashScreenActivity.allData != null) {
            inflateTable();
            drawLineChartForAllPeakHourReport();
        }
    }

    @SuppressLint("HandlerLeak")
    public void inflateTable() {
        grandTotalSum = 0;
        if (peakHourReportForAllTableLayout != null) {
            peakHourReportForAllTableLayout.removeAllViews();
        }

        dataofTheSameHour();
        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }
//                if (index == tablesToDisplay.size()) {
                if (index == mergedFigureData.size()) {
//                    drawLastRow();
                    new UtilityFunctionsForActivity1().scrollRows(peakHourReportScrollView);
//                } else if (index == tablesToDisplay.size() + 1) {
                } else if (index == mergedFigureData.size() + 1) {
                    if (fragment != null) {
//                        NavController navController = NavHostFragment.findNavController(fragment);
//                        navController.navigate(R.id.vsmCardFragment);
                        if (peakHourForAllPaused) {
                            if (handleRowAnimationThread != null) {
                                handleRowAnimationThread.interrupt();
                            }
                        } else {
                            navigate(fragment);
                        }
                    }

//                } else if (index < tablesToDisplay.size()) {
                } else if (index < mergedFigureData.size()) {
//                    grandTotalSum = grandTotalSum + tablesToDisplay.get(index).grandTotal;
                    grandTotalSum = grandTotalSum + mergedFigureData.get(index).grandTotal;
//                    UtilityFunctionsForActivity1.drawPeakHourReportForAllOus(tablesToDisplay, getContext(), peakHourReportForAllTableLayout, index);
                    new UtilityFunctionsForActivity1().drawPeakHourReportForAllOus(mergedFigureData, getContext(), peakHourReportForAllTableLayout, index);
                    new UtilityFunctionsForActivity1().scrollRows(peakHourReportScrollView);
                }
            }
        };

//        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, 200, this, 0);
        handleRowAnimationThread = new HandleRowAnimationThread(mergedFigureData.size(), animationHandler, 200);
        handleRowAnimationThread.start();
    }


    public void drawLineChartForAllPeakHourReport() {

        tablesToDisplay = SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch();
        ArrayList<String> distinctDates = DashBoardDataParser.distinictDates(tablesToDisplay);

        Log.i("distinctDates", distinctDates + "");
        Collections.sort(distinctDates, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return new UtilityFunctionsForActivity1().peakHourFormatter(o1).compareTo(new UtilityFunctionsForActivity1().peakHourFormatter(o2));
            }
        });

        ArrayList<ArrayList<Integer>> indexesForDates = new ArrayList<>();

        Log.i("DISTINCT DATES", distinctDates.toString());
        for (int i = 0; i < distinctDates.size(); i++) {
            ArrayList<Integer> indexForThisDate = new ArrayList<>();
            for (int j = 0; j < tablesToDisplay.size(); j++) {
                if (tablesToDisplay.get(j).dateNTime.equals(distinctDates.get(i))) {
                    indexForThisDate.add(j);
                }
            }
            indexesForDates.add(indexForThisDate);
        }

        Log.i("DATES ", indexesForDates.toString());

        ArrayList<Double> merged = new ArrayList<>();
        ArrayList<FigureReportDataElements> mergedFigureData = new ArrayList<>();

        for (int i = 0; i < indexesForDates.size(); i++) {
            ArrayList<Integer> indexesAtSingleTime = indexesForDates.get(i);
            double grandTotal = 0.0;
            String dateTime = "";
            int count = 0;
            String org = "";
//            FigureReportDataElements figureReportDataElements = new FigureReportDataElements(dateTime, count, grandTotal, org);
            FigureReportDataElements figureReportDataElements = null;
            for (int j = 0; j < indexesAtSingleTime.size(); j++) {

                grandTotal += tablesToDisplay.get(indexesForDates.get(i).get(j)).grandTotal;
                dateTime = tablesToDisplay.get(indexesForDates.get(i).get(j)).dateNTime;
                count += tablesToDisplay.get(indexesForDates.get(i).get(j)).totalCount;
                String time = tablesToDisplay.get(indexesForDates.get(i).get(j)).dateNTime;
                Log.i("time", time + "");
                figureReportDataElements = new FigureReportDataElements(dateTime, count, grandTotal, "");

            }
//            Log.i("dateTime", dateTime + "");
            Log.i("count", count + "");
            Log.i("grandTotal", grandTotal + "");

            merged.add(grandTotal);
            mergedFigureData.add(figureReportDataElements);
        }

        Log.i("GRAND TOTAL", merged.toString());
        Log.i("ALL DATA", mergedFigureData.toString());

//        float[] x = new float[SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch().size()];
        float[] x1 = new float[merged.size()];
        float[] x2 = new float[merged.size()];
//        float[] y = new float[SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch().size()];
        float[] y1 = new float[merged.size()];
        float[] y2 = new float[merged.size()];
//        String[] label = new String[SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch().size()];
        String[] label1 = new String[merged.size()];
        String[] label2 = new String[merged.size()];

        for (int i = 0; i < merged.size(); i++) {

            x2[i] = i;
            y2[i] = (float) mergedFigureData.get(i).getTotalCount();
            String timeAtThisIndex2 = SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch().get(indexesForDates.get(i).get(0)).dateNTime;
            label2[i] = timeAtThisIndex2;

            x1[i] = i;
            y1[i] = Float.parseFloat(merged.get(i).toString());
            String timeAtThisIndex1 = SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch().get(indexesForDates.get(i).get(0)).dateNTime;
            label1[i] = timeAtThisIndex1;
        }
        LineChartData lineChartData1 = new LineChartData(x1, y1, label1);
        LineChartData lineChartData2 = new LineChartData(x2, y2, label2);
        PieChartData pieChartData = new PieChartData(y1, label1);
        BarChartData barChartData1 = new BarChartData(x1, y1, label1);
        BarChartData barChartData2 = new BarChartData(x2, y2, label2);

        int chartTypeIndex = SplashScreenActivity.allData.getLayoutList().indexOf(Constants.ALL_PEAK_HOUR_INDEX);
//        int chartTypeIndex = 13;

        Log.i("chartTypeIndex", chartTypeIndex + "");
        Log.i("size", SplashScreenActivity.allData.getLayoutList().size() + "");

        String chartType = "";
        if (chartTypeIndex < SplashScreenActivity.allData.getChartList().size()) {
            chartType = SplashScreenActivity.allData.getChartList().get(chartTypeIndex);
        } else {
            chartType = "";
        }

        Log.i("chart type", chartType + "");
//        new UtilityFunctionsForActivity2().drawDoubleLineChart(lineChartData1, lineChartData2, peakHourReportForAllLineChart, "peak Hour Report for all", "Total Count");
        new UtilityFunctionsForActivity2().drawChart(getContext(), chartType, peakHourReportLineCard, pieChartData, barChartData1, barChartData2, lineChartData1, lineChartData2, "peak Hour Report for all", "Total Count");
    }

    public void navigate(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(10) && SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch() != null)
            navController.navigate(R.id.userReportForEachOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(12) && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch() != null)
            navController.navigate(R.id.peakHourReportFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(1) && SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans() != null)
            navController.navigate(R.id.mapsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(3) && SplashScreenActivity.allData.getDashBoardData().getSummarizedByArticleData() != null)
            navController.navigate(R.id.summarizedByArticleFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(4) && SplashScreenActivity.allData.getDashBoardData().getSummarizedByParentArticleData() != null)
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(5) && SplashScreenActivity.allData.getDashBoardData().getSummarizedByChildArticleData() != null)
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(6) && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast6MonthsData() != null)
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(7) && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData() != null)
            navController.navigate(R.id.summaryOfLastMonthFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
            navController.navigate(R.id.branchSummaryFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(9) && SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch() != null)
            navController.navigate(R.id.userReportForAllOusFragment2);
        else {
            inflateTable();
            drawLineChartForAllPeakHourReport();
        }

    }

    public void navigateLeft(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(9) && SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch() != null)
            navController.navigate(R.id.userReportForAllOusFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
            navController.navigate(R.id.branchSummaryFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(7) && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData() != null)
            navController.navigate(R.id.summaryOfLastMonthFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(6) && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast6MonthsData() != null)
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(5) && SplashScreenActivity.allData.getDashBoardData().getSummarizedByChildArticleData() != null)
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(4) && SplashScreenActivity.allData.getDashBoardData().getSummarizedByParentArticleData() != null)
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(3) && SplashScreenActivity.allData.getDashBoardData().getSummarizedByArticleData() != null)
            navController.navigate(R.id.summarizedByArticleFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(1) && SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans() != null)
            navController.navigate(R.id.mapsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(12) && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch() != null)
            navController.navigate(R.id.peakHourReportFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(10) && SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch() != null)
            navController.navigate(R.id.userReportForEachOusFragment);
        else {
            inflateTable();
            drawLineChartForAllPeakHourReport();
        }
    }

    public void dataofTheSameHour() {
        tablesToDisplay = SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch();
        ArrayList<String> distinctDates = DashBoardDataParser.distinictDates(tablesToDisplay);
        Collections.sort(distinctDates, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return new UtilityFunctionsForActivity1().peakHourFormatter(o1).compareTo(new UtilityFunctionsForActivity1().peakHourFormatter(o2));
            }
        });

        ArrayList<ArrayList<Integer>> indexesForDates = new ArrayList<>();
        Log.i("DISTINCT DATES", distinctDates.toString());
        for (int i = 0; i < distinctDates.size(); i++) {
            ArrayList<Integer> indexForThisDate = new ArrayList<>();
            for (int j = 0; j < tablesToDisplay.size(); j++) {
                if (tablesToDisplay.get(j).dateNTime.equals(distinctDates.get(i))) {
                    indexForThisDate.add(j);
                }
            }
            indexesForDates.add(indexForThisDate);
        }

        Log.i("DATES ", indexesForDates.toString());

        ArrayList<Double> merged = new ArrayList<>();
        mergedFigureData = new ArrayList<>();

        for (int i = 0; i < indexesForDates.size(); i++) {
            ArrayList<Integer> indexesAtSingleTime = indexesForDates.get(i);
            double grandTotal = 0.0;
            String dateTime = "";
            int count = 0;
            String org = "";
//            FigureReportDataElements figureReportDataElements = new FigureReportDataElements(dateTime, count, grandTotal, org);
            FigureReportDataElements figureReportDataElements = null;
            for (int j = 0; j < indexesAtSingleTime.size(); j++) {
                grandTotal += tablesToDisplay.get(indexesForDates.get(i).get(j)).grandTotal;
                dateTime = tablesToDisplay.get(indexesForDates.get(i).get(j)).dateNTime;
                count += tablesToDisplay.get(indexesForDates.get(i).get(j)).totalCount;

                figureReportDataElements = new FigureReportDataElements(dateTime, count, grandTotal, "");

            }
            merged.add(grandTotal);
            mergedFigureData.add(figureReportDataElements);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
            handleRowAnimationThread = null;
        }
    }

    @Override
    public void centerKey() {
        peakHourForAllPaused = !peakHourForAllPaused;
//        SecondActivity.firstCenterKeyPause = peakHourForAllPaused;
        if (!peakHourForAllPaused) {
            SecondActivity.playAll();
            navigate(fragment);
        } else {
            SecondActivity.pauseAll();
        }
        keyPadControl(peakHourForAllPaused);

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

    public void keyPadControl(boolean paused) {
        if (paused) {
            peakHrAllplaypause.setImageResource(R.drawable.ic_play_button__2_);
            peakHrAllplaypause.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireActivity(), R.color.playbacksForeground)));
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }

}
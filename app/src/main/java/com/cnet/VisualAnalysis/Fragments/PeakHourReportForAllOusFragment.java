package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.cnet.VisualAnalysis.Data.PeakHourReportForAllOus;
import com.cnet.VisualAnalysis.MapsActivity;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.github.mikephil.charting.charts.LineChart;

public class PeakHourReportForAllOusFragment extends Fragment implements SecondActivity.KeyPress {

    TableLayout peakHourReportForAllTableLayout;
    ScrollView peakHourReportScrollView;
    TextView scrollingPeakHourForAllText;
    DigitalClock digitalClock;
    public static Handler animationHandler;
    public HandleRowAnimationThread handleRowAnimationThread;
    //    private ArrayList<FigureReportDataElements> tablesToDisplay;
    private PeakHourReportForAllOus tablesToDisplay;
    Fragment fragment;
    ImageView peakHrAllleftArrow;
    ImageView peakHrAllrightArrow;
    ImageView peakHrAllplaypause;
    LinearLayout linearLayout;
    LineChart peakHourReportForAllLineChart;

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
        digitalClock = view.findViewById(R.id.digitalClock);
        peakHourReportForAllLineChart = view.findViewById(R.id.peakHourReportForAllLineChart);
        digitalClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));
        fragment = this;

        keyPadControl(peakHourForAllPaused);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch() != null) {
        if (SplashScreenActivity.allData.getDashBoardData().getPeakHourReportForAllOus() != null) {
            inflateTable();
        }
    }

    @SuppressLint("HandlerLeak")
    public void inflateTable() {
        grandTotalSum = 0;
        if (peakHourReportForAllTableLayout != null) {
            peakHourReportForAllTableLayout.removeAllViews();
        }
//        tablesToDisplay = SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch();
        tablesToDisplay = SplashScreenActivity.allData.getDashBoardData().getPeakHourReportForAllOus();
        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }

//                if (index == tablesToDisplay.size()) {
                if (index == tablesToDisplay.getFigureReportDataElements().size()) {
//                    drawLastRow();
                    UtilityFunctionsForActivity1.scrollRows(peakHourReportScrollView);
//                } else if (index == tablesToDisplay.size() + 1) {
                } else if (index == tablesToDisplay.getFigureReportDataElements().size() + 1) {
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
                } else if (index < tablesToDisplay.getFigureReportDataElements().size()) {
//                    grandTotalSum = grandTotalSum + tablesToDisplay.get(index).grandTotal;
                    grandTotalSum = grandTotalSum + tablesToDisplay.getFigureReportDataElements().get(index).grandTotal;
//                    UtilityFunctionsForActivity1.drawPeakHourReportForAllOus(tablesToDisplay, getContext(), peakHourReportForAllTableLayout, index);
                    UtilityFunctionsForActivity1.drawPeakHourReportForAllOus(tablesToDisplay.getFigureReportDataElements(), getContext(), peakHourReportForAllTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(peakHourReportScrollView);
                    UtilityFunctionsForActivity2.drawLineChart(tablesToDisplay.lineChartData, peakHourReportForAllLineChart, "peak hour report for all Ous");
                }
            }
        };

//        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, 200, this, 0);
        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.getFigureReportDataElements().size(), animationHandler, 200, this, 0);
        handleRowAnimationThread.start();
    }


    public void navigate(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
            if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                navController.navigate(R.id.peakHourReportFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
//                startActivity(new Intent(requireActivity(), MapsActivity.class));
                navController.navigate(R.id.vansOfASingleOrganizationFragment);
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
            else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
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
        } else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0) {
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
//            navController.navigate(R.id.vansOfASingleOrganizationFragment);

        else if (SplashScreenActivity.allData.getLayoutList().contains(12))
            navController.navigate(R.id.peakHourReportFragment);


    }

    @Override
    public void onStop() {
        super.onStop();
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();

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
//            SecondActivity.pauseAll();
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
package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Data.FigureReportDataElements;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.BackGroundTasks;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.cnet.VisualAnalysis.VideoActivity;
import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PeakHourReportFragment extends Fragment implements SecondActivity.KeyPress{

    public Handler animationHandler;
    public HandleRowAnimationThread handleRowAnimationThread;
    Fragment fragment;
    TextView peakHourReportTitle;
    TextView scrollingPeakText;
    TableLayout peakHourReportTableLayout;
    ScrollView peakHourReportScrollView;
    DigitalClock digitalClock;
    TextClock peakHourEach_textClock;
    public static boolean peakHourForEachPaused;
    ImageView peakHrEachleftArrow;
    ImageView peakHrEachrightArrow;
    ImageView peakHrEachplaypause;
    LinearLayout peakHrEachKeyPad;
    NavController navController;
    MaterialCardView cCardEachPeakHour;

    ArrayList<Integer> layouts;
    DashBoardData dashBoardData;

    private ArrayList<FigureReportDataElements> tablesToDisplay;

    double grandTotalSum;
    int totalCount;
    Context thisContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisContext = requireActivity().getApplicationContext();
//
//        BackGroundTasks backGroundTasks = new BackGroundTasks(thisContext, this);
//        backGroundTasks.execute();
        if (!SecondActivity.pausedstate()) {
            peakHourForEachPaused = false;
        } else {
            peakHourForEachPaused = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_peak_hour_report, container, false);

        peakHourReportTitle = view.findViewById(R.id.peakHourReportTitle);

        peakHourReportTableLayout = view.findViewById(R.id.peakHourReportTableLayout);
        peakHourReportScrollView = view.findViewById(R.id.peakHourReportScrollView);
        scrollingPeakText = view.findViewById(R.id.scrollingPeakText);

        peakHrEachleftArrow = view.findViewById(R.id.peakHrEachleftArrow);
        peakHrEachrightArrow = view.findViewById(R.id.peakHrEachrightArrow);
        peakHrEachplaypause = view.findViewById(R.id.peakHrEachplayPause);
        peakHrEachKeyPad = view.findViewById(R.id.peakHrEachKeyPad);

        scrollingPeakText.setSelected(true);
        peakHourEach_textClock = view.findViewById(R.id.peakHourEach_textClock);
        peakHourEach_textClock.setFormat12Hour("kk:mm:ss");
        peakHourEach_textClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));
        cCardEachPeakHour = view.findViewById(R.id.cCardEachPeakHour);

        keyPadControl(peakHourForEachPaused);

        fragment = this;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//
        if (SplashScreenActivity.allData != null) {
            layouts = SplashScreenActivity.allData.getLayoutList();
            dashBoardData = SplashScreenActivity.allData.getDashBoardData();
            drawAvailableReportFromPeakHour(SecondActivity.vanIndex);
        }
    }

    private void drawAvailableReportFromPeakHour(int vanIndex) {
//        if (SecondActivity.vanIndex < SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size()) {
        if (SecondActivity.vanIndex < dashBoardData.getFigureReportDataforEachBranch().size()) {
//            if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
            if (dashBoardData.getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
                drawAllPeakHourReport(SecondActivity.vanIndex);
            } else if (layouts.contains(1)) {
                Log.i("paused-1-", "drawAvailableReportFromPeakHour: ");
//                if (!peakHourForEachPaused) {
                navigateToMapFromPeakHour(SecondActivity.vanIndex);
//                }
                Log.i("TAG-", "drawAvailableReportFromPeakHour: ");
            } else if (layouts.contains(10)) {
                Log.i("paused-2-", "drawAvailableReportFromPeakHour: ");
//                if (!peakHourForEachPaused) {
                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                navigateToUserReportFromPeakHour(SecondActivity.vanIndex);
//                }
            } else {
                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                if (SecondActivity.vanIndex < dashBoardData.getVoucherDataForVans().size()) {
                    interruptThread();
                    drawAvailableReportFromPeakHour(SecondActivity.vanIndex);
                }
            }
        } else {
            SecondActivity.vanIndex = 0;
            startActivity(new Intent(requireActivity(), VideoActivity.class));
        }
    }


    private void navigateToMapFromPeakHour(int vanIndex) {
        navController = NavHostFragment.findNavController(fragment);
        if (dashBoardData.getVoucherDataForVans().size() > 0) {
//            if (SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).tableRows.size() > 0) {
            if (dashBoardData.getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {

                navController.navigate(R.id.mapsFragment);
            } else {
                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                interruptThread();
                drawAvailableReportFromPeakHour(SecondActivity.vanIndex);
            }
        } else {
            SecondActivity.vanIndex = 0;
            startActivity(new Intent(requireActivity(), VideoActivity.class));
        }
    }

    private void navigateToUserReportFromPeakHour(int vanIndex) {
        if (getView() != null) {
            getView().post(new Runnable() {
                @Override
                public void run() {
                    navController = NavHostFragment.findNavController(fragment);
                    if (SecondActivity.vanIndex < dashBoardData.getUserReportForEachBranch().size()) {
                        if (dashBoardData.getUserReportForEachBranch().size() > 0) {
                            if (dashBoardData.getUserReportForEachBranch().get(SecondActivity.vanIndex).userReportTableRowArrayList.size() > 0) {
                                navController.navigate(R.id.userReportForEachOusFragment);
                            } else {
                                interruptThread();
                                drawAvailableReportFromPeakHour(SecondActivity.vanIndex);
                            }
                        } else {
                            SecondActivity.vanIndex = 0;
                            startActivity(new Intent(requireActivity(), VideoActivity.class));
                        }
                    } else {
                        SecondActivity.vanIndex = 0;
                        startActivity(new Intent(requireActivity(), VideoActivity.class));
                    }
                }
            });
        }


    }

    public void drawAllPeakHourReport(int vanIndex) {
        if (dashBoardData.getFigureReportDataforEachBranch().get(vanIndex) != null) {

            inflateTable(vanIndex);
            String chartType = "";

            DashBoardData dashBoardData = SplashScreenActivity.allData.getDashBoardData();
            if (layouts.contains(Constants.EACH_PEAK_HOUR_INDEX)) {
                int chartTypeIndex = layouts.indexOf(Constants.EACH_PEAK_HOUR_INDEX);
                Log.i("chartTypeIndex", chartTypeIndex + "");
                if (chartTypeIndex < SplashScreenActivity.allData.getChartList().size()) {
                    chartType = SplashScreenActivity.allData.getChartList().get(chartTypeIndex);
                } else {
                    chartType = "";
                }
                Log.i("chart", chartType + "");
            }
            new UtilityFunctionsForActivity2().drawChart(getContext(), chartType, cCardEachPeakHour, dashBoardData.getFigureReportDataforEachBranch().get(vanIndex).getPieChartData(),
                    dashBoardData.getFigureReportDataforEachBranch().get(vanIndex).getBarChartData1(), dashBoardData.getFigureReportDataforEachBranch().get(vanIndex).getBarChartData2(), dashBoardData.getFigureReportDataforEachBranch().get(vanIndex).lineChartData1,
                    dashBoardData.getFigureReportDataforEachBranch().get(vanIndex).lineChartData2, "Grand Total ", "Total Count");

            if (peakHourReportTitle != null) {
                peakHourReportTitle.setText("Peak Hour Report for " + dashBoardData.getFigureReportDataforEachBranch().get(vanIndex).org);
                peakHourReportTitle.append(" on " + new SimpleDateFormat(Constants.dateCriteriaFormat, Locale.getDefault()).format(Calendar.getInstance().getTime()));
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(int dataIndex) {
        grandTotalSum = 0;
        totalCount = 0;
        if (peakHourReportTableLayout != null) {
            peakHourReportTableLayout.removeAllViews();
        }
        if (cCardEachPeakHour != null) {
            cCardEachPeakHour.removeAllViews();
        }
        tablesToDisplay = dashBoardData.getFigureReportDataforEachBranch().get(dataIndex).figureReportDataElementsArrayList;
//        tablesToDisplay = figureReportData.get(dataIndex).figureReportDataElementsArrayList;

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
                    new UtilityFunctionsForActivity1().scrollRows(peakHourReportScrollView);
//                } else if (index == tablesToDisplay.size() + 1 && dataIndex == SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size() - 1) {
//                } else if (index == tablesToDisplay.size() + 1 && dataIndex == getallFigureReportSize(SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch()) - 1) {
                }

                if (index == tablesToDisplay.size() + 1 && dataIndex == dashBoardData.getFigureReportDataforEachBranch().size() - 1) {
                    Log.i("last van, last voucher ", "handleMessage: ");
                    if (fragment != null) {
                        if (peakHourForEachPaused) {
                            if (handleRowAnimationThread != null) {
                                handleRowAnimationThread.interrupt();
                                handleRowAnimationThread = null;

                            }
                        } else {
//                            SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                            navigateToNextReport();
                        }
                    }
                }
                if (index == tablesToDisplay.size() + 1) {
                    Log.i("last van", "handleMessage: ");

                    if (peakHourForEachPaused) {
                        if (handleRowAnimationThread != null) {
                            handleRowAnimationThread.interrupt();
                            handleRowAnimationThread = null;

                        }
                    } else {
//                        navigate(fragment);
                        navigateToNextReport();
                    }
                } else if (index < tablesToDisplay.size()) {
                    grandTotalSum = grandTotalSum + tablesToDisplay.get(index).grandTotal;
                    totalCount = totalCount + tablesToDisplay.get(index).totalCount;
                    new UtilityFunctionsForActivity1().drawPeakHourReportForEachOu(tablesToDisplay, thisContext, peakHourReportTableLayout, index);
                    new UtilityFunctionsForActivity1().scrollRows(peakHourReportScrollView);
                }
            }
        };
        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, 200);
        handleRowAnimationThread.start();
    }

    public void navigateToNextReport() {
        try {
            navController = NavHostFragment.findNavController(fragment);
            Log.i("van Index", SecondActivity.vanIndex + "");
            Log.i("van size", dashBoardData.getVoucherDataForVans().size() + "");
            if (SecondActivity.vanIndex < dashBoardData.getVoucherDataForVans().size()) {
                if (layouts.contains(1)) {
                    Log.i("TAG-1", "navigateToNextReport: ");
                    if (dashBoardData.getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
                        Log.i("TAG-2", "navigateToNextReport: ");
                        navController.navigate(R.id.mapsFragment);
                    } else if (layouts.contains(10)) {
                        SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                        if (dashBoardData.getUserReportForEachBranch().get(SecondActivity.vanIndex).userReportTableRowArrayList.size() > 0) {
                            navController.navigate(R.id.userReportForEachOusFragment);
                        } else {
                            Log.i("Redrawn-2", "drawAvailableReportFromPeakHour: ");
                            interruptThread();
                            drawAvailableReportFromPeakHour(SecondActivity.vanIndex);
                        }
                    } else {
                        Log.i("TAG-3", "navigateToNextReport: ");
                        SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                        Log.i("Redrawn-3", "drawAvailableReportFromPeakHour: ");
                        interruptThread();
                        drawAvailableReportFromPeakHour(SecondActivity.vanIndex);
                    }
                } else if (layouts.contains(10)) {
                    SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                    if (SecondActivity.vanIndex < dashBoardData.getUserReportForEachBranch().size()) {
                        if (dashBoardData.getUserReportForEachBranch().get(SecondActivity.vanIndex).userReportTableRowArrayList.size() > 0) {
                            navController.navigate(R.id.userReportForEachOusFragment);
                        } else {
                            Log.i("Redrawn -5", "navigateToNextReport: ");
                            interruptThread();
                            drawAvailableReportFromPeakHour(SecondActivity.vanIndex);
                        }
                    } else {
                        Log.i("TAG-4", "navigateToNextReport: ");
                        SecondActivity.vanIndex = 0;
//                    drawAvailableReportFromPeakHour(SecondActivity.vanIndex);
                        startActivity(new Intent(requireActivity(), VideoActivity.class));
                    }
                } else {
                    Log.i("TAG-5", "navigateToNextReport: ");
                    SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                    Log.i("Redrawn-4", "drawAvailableReportFromPeakHour: ");
                    interruptThread();
                    drawAvailableReportFromPeakHour(SecondActivity.vanIndex);
                }
            } else {
                Log.i("TAG-6", "navigateToNextReport: ");
                SecondActivity.vanIndex = 0;
                startActivity(new Intent(requireActivity(), VideoActivity.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void navigateToPreviousReport() {
        navController = NavHostFragment.findNavController(fragment);
        Log.i("current_index-1", SecondActivity.vanIndex + "");
        if (layouts.contains(10)) {
            if (dashBoardData.getUserReportForEachBranch().get(SecondActivity.vanIndex).userReportTableRowArrayList.size() > 0) {
                navController.navigate(R.id.userReportForEachOusFragment);
            } else {
                SecondActivity.vanIndex = SecondActivity.vanIndex - 1;
                if (SecondActivity.vanIndex >= 0) {
                    Log.i("van-", SecondActivity.vanIndex + "");
                    if (layouts.contains(1)) {
                        Log.i("TAG-1", "navigateToPreviousReport: ");
                        if (dashBoardData.getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
                            Log.i("TAG-2", "navigateToPreviousReport: ");
                            navController.navigate(R.id.mapsFragment);
                        } else if (dashBoardData.getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
                            Log.i("TAG-3", "navigateToPreviousReport: ");
                            interruptThread();
                            drawAvailableReportFromPeakHour(SecondActivity.vanIndex);
                        } else {
                            Log.i("TAG-4", "navigateToPreviousReport: ");
                            navigateToPreviousReport();
                        }
                    } else {
                        if (dashBoardData.getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
                            interruptThread();
                            drawAvailableReportFromPeakHour(SecondActivity.vanIndex);
                        } else {
                            navigateToPreviousReport();
                        }
                    }
                } else {
                    Log.i("TAG-6", "navigateToPreviousReport: ");
                    SecondActivity.vanIndex = 0;
                    navigateToLeftFragments();
                }
            }
        } else if (layouts.contains(1)) {
            SecondActivity.vanIndex = SecondActivity.vanIndex - 1;
            if (SecondActivity.vanIndex >= 0) {
                if (dashBoardData.getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
                    navController.navigate(R.id.mapsFragment);
                } else if (dashBoardData.getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
                    interruptThread();
                    drawAvailableReportFromPeakHour(SecondActivity.vanIndex);
                } else {
                    navigateToPreviousReport();
                }
            } else {
                SecondActivity.vanIndex = 0;
                navigateToLeftFragments();
            }
            Log.i("current_index-2", SecondActivity.vanIndex + "");


        } else {
            SecondActivity.vanIndex = SecondActivity.vanIndex - 1;
            if (SecondActivity.vanIndex >= 0) {
                if (dashBoardData.getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
                    interruptThread();
                    drawAvailableReportFromPeakHour(SecondActivity.vanIndex);
                } else {
                    navigateToPreviousReport();
                }
            } else {
                SecondActivity.vanIndex = 0;
                navigateToLeftFragments();
            }
            Log.i("current_index-2", SecondActivity.vanIndex + "");

        }
    }

    public void navigateToLeftFragments() {
        if (SplashScreenActivity.allData.getLayoutList().contains(11) &&
                SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch().size() > 0) {
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(9) &&
                SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().size() > 0) {
            navController.navigate(R.id.userReportForAllOusFragment2);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(8) &&
                SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData() != null
                && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0) {
            navController.navigate(R.id.branchSummaryFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(7) &&
                SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData().tableData.size() > 0) {
            navController.navigate(R.id.summaryOfLastMonthFragment);
        } else if (layouts.contains(6) && dashBoardData.getSummaryOfLast6MonthsData() != null) {
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(5) &&
                SplashScreenActivity.allData.getDashBoardData().getSummarizedByChildArticleData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummarizedByChildArticleData().getTableData().size() > 0) {
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(4) &&
                SplashScreenActivity.allData.getDashBoardData().getSummarizedByParentArticleData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummarizedByParentArticleData().getTableData().size() > 0) {
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(3) &&
                SplashScreenActivity.allData.getDashBoardData().getSummarizedByArticleData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummarizedByArticleData().tableData.size() > 0) {
            navController.navigate(R.id.summarizedByArticleFragment2);
        } else {
//            drawAvailableReportFromPeakHour(SecondActivity.vanIndex);
            startActivity(new Intent(requireActivity(), VideoActivity.class));

        }
    }

    private void drawLastRow() {
        View tableElements = null;
        try {
            if (thisContext != null) {
                LayoutInflater inflater = (LayoutInflater) thisContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            tableElements = LayoutInflater.from(thisContext).inflate(R.layout.table_row_summary_by_parent_article, null, false);
                tableElements = inflater.inflate(R.layout.table_row_summary_by_parent_article, null, false);
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

//            peakHourReportGrandTotal.setText(numberFormat.format(Math.round(grandTotalSum * 100.0) / 100.0));
                peakHourReportGrandTotal.setText((grandTotalSum > 1) ? UtilityFunctionsForActivity2.decimalFormat.format(grandTotalSum) :
                        UtilityFunctionsForActivity2.smallDecimlFormat.format(grandTotalSum));
                peakHourReportGrandTotal.setTypeface(Typeface.DEFAULT_BOLD);
                peakHourReportGrandTotal.setTextSize(16f);

                Animation animation = AnimationUtils.loadAnimation(thisContext, R.anim.blink);
                peakHourReportSummaryType.startAnimation(animation);
                peakHourReportTotalCount.startAnimation(animation);
                peakHourReportGrandTotal.startAnimation(animation);

                tableElements.setBackgroundColor(Color.parseColor("#3f4152"));

                if (peakHourReportTableLayout != null) {
                    peakHourReportTableLayout.addView(tableElements);
                    new UtilityFunctionsForActivity2().animateBottomToTop(peakHourReportTableLayout, tableElements);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void interruptThread() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
            handleRowAnimationThread = null;
        }
    }

    public Date convertToTime(String dateInString) {
        SimpleDateFormat input = new SimpleDateFormat("MMM dd yyyy HH:mmaa");

        Date parsed = null;
        try {
            parsed = input.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsed;
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
        peakHourForEachPaused = !peakHourForEachPaused;
        if (!peakHourForEachPaused) {
            SecondActivity.playAll();
            navigateToNextReport();
        } else {
            SecondActivity.pauseAll();
        }
        keyPadControl(peakHourForEachPaused);
    }

    @Override
    public void leftKey() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
            handleRowAnimationThread = null;

        }
        navigateToPreviousReport();
    }

    @Override
    public void rightKey() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
            handleRowAnimationThread = null;

        }
        navigateToNextReport();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        thisContext = null;
    }

}

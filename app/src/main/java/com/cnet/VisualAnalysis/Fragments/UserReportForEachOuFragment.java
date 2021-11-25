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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Data.UserReportTableRow;
import com.cnet.VisualAnalysis.MapsActivity;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.cnet.VisualAnalysis.VideoActivity;
import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class UserReportForEachOuFragment extends Fragment implements SecondActivity.KeyPress {

    TableLayout userReportTableLayout;
    ScrollView userReportScrollView;
    TextView userReportTitle;
    TextView scrollingUserReportForEachText;
    TextClock userReportEach_textClock;
    public Handler animationHandler;
    public Handler changeDataHandler;
    public HandleRowAnimationThread handleRowAnimationThread;
    private ArrayList<UserReportTableRow> tablesToDisplay;
    Fragment fragment;

    LinearLayout eachUserReportKeyPad;
    ImageView userRepEachleftArrow;
    ImageView userRepEachplayPause;
    ImageView userRepEachrightArrow;
    NavController navController;

    ConstraintLayout userReportCL;

    TextView userReportSN;
    TextView userReportSummaryType;
    TextView userReportGrandTotal;
    TextView userReportPercentage;

    double grandTotalSum;
    public static boolean userReportForEachPaused;

    Context thisContext;

    MaterialCardView cCardEachUserReport;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SecondActivity.pausedstate()) {
            userReportForEachPaused = false;
        } else {
            userReportForEachPaused = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_report_for_each_ou, container, false);

        userReportTableLayout = view.findViewById(R.id.userReportTableLayout);
        userReportScrollView = view.findViewById(R.id.userReportScrollView);
        userReportTitle = view.findViewById(R.id.userReportTitle);
        scrollingUserReportForEachText = view.findViewById(R.id.scrollingUserReportForEachText);
        scrollingUserReportForEachText.setSelected(true);
        userReportEach_textClock = view.findViewById(R.id.userReportEach_textClock);
        userReportEach_textClock.setFormat12Hour("kk:mm:ss");
        userReportEach_textClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));
        eachUserReportKeyPad = view.findViewById(R.id.eachUserReportKeyPad);
        userRepEachleftArrow = view.findViewById(R.id.userRepEachleftArrow);
        userRepEachplayPause = view.findViewById(R.id.userRepEachplayPause);
        userRepEachrightArrow = view.findViewById(R.id.userRepEachrightArrow);
        cCardEachUserReport = view.findViewById(R.id.cCardEachUserReport);
        userReportCL = view.findViewById(R.id.userReportCL);

        fragment = this;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (SplashScreenActivity.allData != null) {
            navController = NavHostFragment.findNavController(fragment);
            drawAvailableReportFromUserReport();
        }
    }

    private void drawAvailableReportFromUserReport() {
        if (SecondActivity.vanIndex < SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size()) {
            if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(SecondActivity.vanIndex).userReportTableRowArrayList.size() > 0) {

                Log.i("User Report drawn", "drawAvailableReportFromUserReport: ");
                drawAllUserReport(SecondActivity.vanIndex);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                navigateToPeakHourFromUserReport(SecondActivity.vanIndex);

            } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
                Log.i("navigated", "to map");
                navigateToMapFromUserReport();

            } else {
                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                if (SecondActivity.vanIndex < SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size()) {
                    Log.i("iterating", "drawAvailableReportFromUserReport: ");
                    interruptThread();
                    drawAvailableReportFromUserReport();
                } else {
                    SecondActivity.vanIndex = 0;
                    startActivity(new Intent(requireActivity(), VideoActivity.class));
                }
            }
        } else {
            SecondActivity.vanIndex = 0;
            startActivity(new Intent(requireActivity(), VideoActivity.class));
        }
    }

    public void navigateToPeakHourFromUserReport(int vanIndex) {
        navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size() > 0) {

            if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {

                navController.navigate(R.id.peakHourReportFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
                if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size() > 0) {

                    if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
//                        navController.navigate(R.id.mapsFragment);
                        startActivity(new Intent(requireActivity(), MapsActivity.class));

                    } else {
                        SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                        interruptThread();
                        drawAvailableReportFromUserReport();
                    }
                } else {
                    SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                    interruptThread();
                    drawAvailableReportFromUserReport();
                }
            } else {
                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                interruptThread();
                drawAvailableReportFromUserReport();
            }

        }
    }

    private void navigateToMapFromUserReport() {
        navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size() > 0) {
            if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
//                navController.navigate(R.id.mapsFragment);
                startActivity(new Intent(requireActivity(), MapsActivity.class));
            } else {

                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                interruptThread();
                drawAvailableReportFromUserReport();
            }
        } else {//size is 0
            SecondActivity.vanIndex = 0;
            startActivity(new Intent(requireActivity(), VideoActivity.class));
        }
    }

    public void drawAllUserReport(int index) {
        if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(index) != null) {

            inflateTable(index);
            DashBoardData dashBoardData = SplashScreenActivity.allData.getDashBoardData();
            String chartType = "";
            if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                int chartTypeIndex = SplashScreenActivity.allData.getLayoutList().indexOf(Constants.EACH_USER_REPORT_INDEX);

                if (chartTypeIndex < SplashScreenActivity.allData.getChartList().size()) {
                    chartType = SplashScreenActivity.allData.getChartList().get(chartTypeIndex);
                } else {
                    chartType = "";
                }
            } else {
                chartType = "";
            }

            new UtilityFunctionsForActivity2().drawChart(getContext(), chartType, cCardEachUserReport,
                    dashBoardData.getUserReportForEachBranch().get(index).getPieChartData(), dashBoardData.getUserReportForEachBranch().get(index).getBarChartData(),
                    dashBoardData.getUserReportForEachBranch().get(index).getLineChartData(), "User Report");

            userReportTitle.setText("User Report For " + SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(index).org);
            userReportTitle.append(" on " + new SimpleDateFormat(Constants.dateCriteriaFormat, Locale.getDefault()).format(Calendar.getInstance().getTime()));
        }
    }


    @SuppressLint("HandlerLeak")
    public void inflateTable(int dataIndex) {
        grandTotalSum = 0;
        if (userReportTableLayout != null) {
            userReportTableLayout.removeAllViews();
        }
        if (cCardEachUserReport != null) {
            cCardEachUserReport.removeAllViews();
        }
        tablesToDisplay = SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(dataIndex).userReportTableRowArrayList;
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
                    new UtilityFunctionsForActivity1().scrollRows(userReportScrollView);
                }
                if (index == tablesToDisplay.size() + 1 && dataIndex == SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size() - 1) {
                    if (userReportForEachPaused) {
                        if (handleRowAnimationThread != null) {
                            handleRowAnimationThread.interrupt();
                            handleRowAnimationThread = null;

                        }
                    } else {
                        SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                        navigateToNextReport();
                    }
                }
                if (index == tablesToDisplay.size() + 1) {
                    if (userReportForEachPaused) {
                        if (handleRowAnimationThread != null) {
                            handleRowAnimationThread.interrupt();
                            handleRowAnimationThread = null;

                        }
                    } else {
                        navigateToNextReport();
                    }
                } else if (index < tablesToDisplay.size()) {
                    grandTotalSum = grandTotalSum + tablesToDisplay.get(index).grandTotal;
                    new UtilityFunctionsForActivity1().drawUserReportForEachOu(tablesToDisplay, requireActivity().getApplicationContext(), userReportTableLayout, index);
                    new UtilityFunctionsForActivity1().scrollRows(userReportScrollView);
                }
            }
        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, 200);
        handleRowAnimationThread.start();
        Log.i("UserReportThread", handleRowAnimationThread + "");
    }

    public void drawLastRow() {
        thisContext = requireActivity().getApplicationContext();

        View tableElements = LayoutInflater.from(thisContext).inflate(R.layout.table_row_summary_by_parent_article, null, false);
        userReportSN = tableElements.findViewById(R.id.tableRowParentArtProperty1);
        userReportSummaryType = tableElements.findViewById(R.id.tableRowParentArtProperty2);
        userReportGrandTotal = tableElements.findViewById(R.id.tableRowParentArtProperty3);
        userReportPercentage = tableElements.findViewById(R.id.tableRowParentArtProperty4);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        userReportSN.setText("");
        userReportSummaryType.setText("Total Amount");
        userReportSummaryType.setTypeface(Typeface.DEFAULT_BOLD);
        userReportSummaryType.setTextSize(16f);

        userReportGrandTotal.setText(numberFormat.format(grandTotalSum));
        userReportGrandTotal.setTypeface(Typeface.DEFAULT_BOLD);
        userReportGrandTotal.setTextSize(16f);

        userReportPercentage.setText("");
        userReportPercentage.setTypeface(Typeface.DEFAULT_BOLD);
        userReportPercentage.setTextSize(16f);

        Animation animation = AnimationUtils.loadAnimation(requireActivity().getApplicationContext(), R.anim.blink);
        userReportSummaryType.startAnimation(animation);
        userReportGrandTotal.startAnimation(animation);
        userReportPercentage.startAnimation(animation);

        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));

        if (userReportTableLayout != null) {
            userReportTableLayout.addView(tableElements);
            new UtilityFunctionsForActivity2().animateBottomToTop(userReportTableLayout, tableElements);
        }
    }

    public void navigateToNextReport() {
        navController = NavHostFragment.findNavController(fragment);
        if (SecondActivity.vanIndex < SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size()) {
            if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
                    navController.navigate(R.id.peakHourReportFragment);
                } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {

                    if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
//                        navController.navigate(R.id.mapsFragment);
                        startActivity(new Intent(requireActivity(), MapsActivity.class));
                    } else {
                        SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                        interruptThread();
                        drawAvailableReportFromUserReport();
                    }
                } else {
                    SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                    interruptThread();
                    drawAvailableReportFromUserReport();
                }
            } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
                if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
//                    navController.navigate(R.id.mapsFragment);
                    startActivity(new Intent(requireActivity(), MapsActivity.class));

                } else {
                    SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                    interruptThread();
                    drawAvailableReportFromUserReport();
                }

            } else {
                Log.i("TAG-1", "navigateToNextReport: ");
                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                if (SecondActivity.vanIndex < SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size()) {
                    Log.i("TAG-2", "navigateToNextReport: ");
                    interruptThread();
                    drawAvailableReportFromUserReport();
                } else {
                    Log.i("TAG-3", "navigateToNextReport: ");
                    navigateToNextReport();
                }
            }
        } else {
            SecondActivity.vanIndex = 0;
            startActivity(new Intent(requireActivity(), VideoActivity.class));
        }

    }

    public void navigateToPreviousReport() {
        navController = NavHostFragment.findNavController(fragment);

        SecondActivity.vanIndex = SecondActivity.vanIndex - 1;
        if (SecondActivity.vanIndex >= 0) {
            if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
                if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
//                    navController.navigate(R.id.mapsFragment);
                    startActivity(new Intent(requireActivity(), MapsActivity.class));

                } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                    if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
                        navController.navigate(R.id.peakHourReportFragment);
                    } else {
                        Log.i("TAG-1", "navigateToPreviousReport: ");
                        navigateToPreviousReport();
                    }
                } else {
                    Log.i("TAG-2", "navigateToPreviousReport: ");
                    navigateToPreviousReport();
                }

            } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
                    navController.navigate(R.id.peakHourReportFragment);
                } else {
                    Log.i("TAG-3", "navigateToPreviousReport: ");
                    navigateToPreviousReport();
                }
            } else {
                Log.i("TAG-4", "navigateToPreviousReport: ");
                if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
                    Log.i("TAG-5", "navigateToPreviousReport: ");
                    interruptThread();
                    drawAvailableReportFromUserReport();
                } else {
                    Log.i("TAG-6", "navigateToPreviousReport: ");
                    Log.i("index", SecondActivity.vanIndex + "");
                    navigateToPreviousReport();
                }
            }
        } else {
            SecondActivity.vanIndex = 0;
            startActivity(new Intent(requireActivity(), VideoActivity.class));
        }
    }

    public void navigate(Fragment fragment) {
        try {
            navController = NavHostFragment.findNavController(fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (SplashScreenActivity.allData.getLayoutList().contains(12) &&
                SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size() > 0)
            navController.navigate(R.id.peakHourReportFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(1) &&
                SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans() != null
                && SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size() > 0)
//            navController.navigate(R.id.mapsFragment);
            startActivity(new Intent(requireActivity(), MapsActivity.class));

        else {
            startActivity(new Intent(requireActivity(), VideoActivity.class));
        }
    }

    public void leftNavigate(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(11) &&
                SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch().size() > 0)
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(9) &&
                SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().size() > 0)
            navController.navigate(R.id.userReportForAllOusFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(8) &&
                SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData() != null
                && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
            navController.navigate(R.id.branchSummaryFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(7) &&
                SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData().tableData.size() > 0)
            navController.navigate(R.id.summaryOfLastMonthFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(6) &&
                SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast6MonthsData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast6MonthsData().getTableData().size() > 0)
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(5) &&
                SplashScreenActivity.allData.getDashBoardData().getSummarizedByChildArticleData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummarizedByChildArticleData().getTableData().size() > 0)
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(4) &&
                SplashScreenActivity.allData.getDashBoardData().getSummarizedByParentArticleData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummarizedByParentArticleData().getTableData().size() > 0)
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(3) &&
                SplashScreenActivity.allData.getDashBoardData().getSummarizedByArticleData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummarizedByArticleData().tableData.size() > 0)
            navController.navigate(R.id.summarizedByArticleFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(1) &&
                SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans() != null
                && SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size() > 0)
//            navController.navigate(R.id.mapsFragment);
            startActivity(new Intent(requireActivity(), MapsActivity.class));

        else if (SplashScreenActivity.allData.getLayoutList().contains(10) &&
                SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size() > 0)
            navController.navigate(R.id.userReportForEachOusFragment);
        else {
//            drawAvailableReportFromUserReport(SecondActivity.vanIndex);
            startActivity(new Intent(requireActivity(), VideoActivity.class));

        }
    }

    public void interruptThread() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
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

        userReportForEachPaused = !userReportForEachPaused;
        if (!userReportForEachPaused) {
            SecondActivity.playAll();
            navigateToNextReport();
        } else {
            SecondActivity.pauseAll();
        }
        keyPadControl(userReportForEachPaused);
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
            userRepEachplayPause.setImageResource(R.drawable.ic_play_button__2_);
            userRepEachplayPause.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.playbacksForeground)));
            eachUserReportKeyPad.setVisibility(View.VISIBLE);

        } else {
            eachUserReportKeyPad.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        thisContext = null;
        super.onDestroyView();
        userReportTableLayout = null;
        userReportScrollView = null;
        userReportTitle = null;
        scrollingUserReportForEachText = null;
        userReportEach_textClock = null;
        eachUserReportKeyPad = null;
        userRepEachleftArrow = null;
        userRepEachplayPause = null;
        userRepEachrightArrow = null;
        cCardEachUserReport = null;

        userReportSN = null;
        userReportSummaryType = null;
        userReportGrandTotal = null;
        userReportPercentage = null;
        userReportCL = null;
        if (animationHandler != null)
            animationHandler.removeCallbacksAndMessages(null);

    }
}
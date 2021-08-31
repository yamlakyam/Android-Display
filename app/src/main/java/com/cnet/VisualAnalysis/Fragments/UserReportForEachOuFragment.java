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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Data.UserReportDataForSingleOu;
import com.cnet.VisualAnalysis.Data.UserReportTableRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.cnet.VisualAnalysis.VideoActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class UserReportForEachOuFragment extends Fragment implements SecondActivity.KeyPress {

    TableLayout userReportTableLayout;
    PieChart pieChart;
    ScrollView userReportScrollView;
    TextView userReportTitle;
    TextView scrollingUserReportForEachText;
    DigitalClock digitalClock;
    public Handler animationHandler;
    public Handler changeDataHandler;
    public HandleRowAnimationThread handleRowAnimationThread;
    private ArrayList<UserReportTableRow> tablesToDisplay;
    Fragment fragment;
    int branchIndex;

    LinearLayout eachUserReportKeyPad;
    ImageView userRepEachleftArrow;
    ImageView userRepEachplayPause;
    ImageView userRepEachrightArrow;
    NavController navController;

    double grandTotalSum;
    public static boolean userReportForEachPaused;

    ArrayList<UserReportDataForSingleOu> userReportDataForSingleOus;

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
        digitalClock = view.findViewById(R.id.userReportEach_digitalClock);
        digitalClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));

        eachUserReportKeyPad = view.findViewById(R.id.eachUserReportKeyPad);
        userRepEachleftArrow = view.findViewById(R.id.userRepEachleftArrow);
        userRepEachplayPause = view.findViewById(R.id.userRepEachplayPause);
        userRepEachrightArrow = view.findViewById(R.id.userRepEachrightArrow);
        cCardEachUserReport = view.findViewById(R.id.cCardEachUserReport);
//        keyPadControl(userReportForEachPaused);

        fragment = this;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (SplashScreenActivity.allData != null) {
            navController = NavHostFragment.findNavController(fragment);
            drawAvailableReportFromUserReport(SecondActivity.vanIndex);
        }
    }

    private void drawAvailableReportFromUserReport(int vanIndex) {
        if (vanIndex < SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size()) {
            if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(vanIndex).userReportTableRowArrayList.size() > 0) {
//                drawUserReport(SecondActivity.vanIndex);
                Log.i("User Report drawn", "drawAvailableReportFromUserReport: ");
                drawAllUserReport(vanIndex);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
//                navController.navigate(R.id.peakHourReportFragment);
                if (!userReportForEachPaused) {
                    navigateToPeakHourFromUserReport(vanIndex);
                }
            } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
//                navController.navigate(R.id.mapsFragment);
                Log.i("navigated", "to map");
                if (!userReportForEachPaused) {
                    navigateToMapFromUserReport(vanIndex);
                }

            } else {
                vanIndex++;
                if (vanIndex < SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size()) {
                    Log.i("iterating", "drawAvailableReportFromUserReport: ");
                    drawAvailableReportFromUserReport(vanIndex);
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

            if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(vanIndex).figureReportDataElementsArrayList.size() > 0) {

                navController.navigate(R.id.peakHourReportFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
                if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size() > 0) {

                    if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size() > 0) {
                        navController.navigate(R.id.mapsFragment);
                    } else {
                        vanIndex = vanIndex + 1;
                        SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                        drawAvailableReportFromUserReport(vanIndex);
                    }
                } else {
                    vanIndex = vanIndex + 1;
                    SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                    drawAvailableReportFromUserReport(vanIndex);
                }
            } else {
                vanIndex = vanIndex + 1;
                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                drawAvailableReportFromUserReport(vanIndex);
            }

        }
    }

    private void navigateToMapFromUserReport(int vanIndex) {
        navController = NavHostFragment.findNavController(fragment);
//        if (SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().size() > 0) {
        if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size() > 0) {
//            if (SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).tableRows.size() > 0) {
            if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size() > 0) {
                navController.navigate(R.id.mapsFragment);
            } else {
                vanIndex = vanIndex + 1;
                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                drawAvailableReportFromUserReport(SecondActivity.vanIndex);
            }
        } else {//size is 0
            SecondActivity.vanIndex = 0;
            startActivity(new Intent(requireActivity(), VideoActivity.class));
        }
    }

    public void drawAllUserReport(int index) {
        if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(index) != null) {

            inflateTable(index);
//            new UtilityFunctionsForActivity2().drawPieChart(SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(index).pieChartData, pieChart, "User Report");

            DashBoardData dashBoardData = SplashScreenActivity.allData.getDashBoardData();


            int chartTypeIndex = SplashScreenActivity.allData.getLayoutList().indexOf(Constants.EACH_USER_REPORT_INDEX);
            String chartType = "";
            if (chartTypeIndex < SplashScreenActivity.allData.getChartList().size()) {
                chartType = SplashScreenActivity.allData.getChartList().get(chartTypeIndex);
            } else {
                chartType = "";
            }

//                new UtilityFunctionsForActivity2().drawBarChart(dashBoardData.getSummarizedByParentArticleData().getBarChartData(), barChart, "Summarized by Article parent category");
//                new UtilityFunctionsForActivity2().drawPieChart(dashBoardData.getSummarizedByParentArticleData().getPieChartData(), pieChart, "Summarized by Article parent category");
            new UtilityFunctionsForActivity2().drawChart(getContext(), chartType, cCardEachUserReport,
                    dashBoardData.getUserReportForEachBranch().get(index).getPieChartData(), dashBoardData.getUserReportForEachBranch().get(index).getBarChartData(),
                    dashBoardData.getUserReportForEachBranch().get(index).getLineChartData(), "User Report");

            userReportTitle.setText("User Report For " + SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(index).org);
            userReportTitle.append(" from " + new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()));
        }
    }


    @SuppressLint("HandlerLeak")
    public void inflateTable(int dataIndex) {
        grandTotalSum = 0;
        if (userReportTableLayout != null) {
            userReportTableLayout.removeAllViews();
        }
        tablesToDisplay = SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(dataIndex).userReportTableRowArrayList;
//        tablesToDisplay = userReportDataForSingleOus.get(dataIndex).userReportTableRowArrayList;
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
//                } else if (index == tablesToDisplay.size() + 1 && dataIndex == SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size() - 1) {
//                } else if (index == tablesToDisplay.size() + 1 && dataIndex == getallUserReportSize(SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch()) - 1) {
                }
                if (index == tablesToDisplay.size() + 1 && dataIndex == SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size() - 1) {
                    if (userReportForEachPaused) {
                        if (handleRowAnimationThread != null) {
                            handleRowAnimationThread.interrupt();
                        }
                    } else {
                        SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                        navigate(fragment);
                    }
                }
                if (index == tablesToDisplay.size() + 1) {
//                    SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                    if (userReportForEachPaused) {
                        if (handleRowAnimationThread != null) {
                            handleRowAnimationThread.interrupt();
                        }
                    } else {
                        navigate(fragment);
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
    }

    public void drawLastRow() {
        thisContext = requireActivity().getApplicationContext();

        View tableElements = LayoutInflater.from(thisContext).inflate(R.layout.table_row_summary_by_parent_article, null, false);
        TextView userReportSN = tableElements.findViewById(R.id.tableRowParentArtProperty1);
        TextView userReportSummaryType = tableElements.findViewById(R.id.tableRowParentArtProperty2);
        TextView userReportGrandTotal = tableElements.findViewById(R.id.tableRowParentArtProperty3);
        TextView userReportPercentage = tableElements.findViewById(R.id.tableRowParentArtProperty4);

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

    public void navigate(Fragment fragment) {
        try {
            navController = NavHostFragment.findNavController(fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (SplashScreenActivity.allData.getLayoutList().contains(12))
            navController.navigate(R.id.peakHourReportFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(1))
            navController.navigate(R.id.mapsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(3))
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
        else if (SplashScreenActivity.allData.getLayoutList().contains(11))
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        else {
            drawAvailableReportFromUserReport(SecondActivity.vanIndex);
        }
    }

    public void leftNavigate(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(11))
            navController.navigate(R.id.peakHourReportForAllOusFragment);
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
            navController.navigate(R.id.mapsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(10))
            navController.navigate(R.id.peakHourReportFragment);
        else {
            drawAvailableReportFromUserReport(SecondActivity.vanIndex);
        }
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

        userReportForEachPaused = !userReportForEachPaused;
//        SecondActivity.firstCenterKeyPause = userReportForEachPaused;
        if (!userReportForEachPaused) {
            SecondActivity.playAll();
            navigateToPeakHourFromUserReport(SecondActivity.vanIndex);
        } else {
            SecondActivity.pauseAll();
        }
        keyPadControl(userReportForEachPaused);

    }

    @Override
    public void leftKey() {
        /*
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        if (branchIndex == 0) {
            leftNavigate(fragment);
        } else {
            inflateAllTables(branchIndex - 1);
        }

         */
    }

    @Override
    public void rightKey() {
        /*
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }

//        if (branchIndex == SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size() - 1) {
//        if (branchIndex == getallUserReportSize(SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch()) - 1) {
        if (branchIndex == getallUserReportSize(userReportDataForSingleOus) - 1) {
            navigate(fragment);
        } else {
            inflateAllTables(branchIndex + 1);
        }

         */
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
    }
}
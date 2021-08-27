package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.BranchSummaryTableRow;
import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.github.mikephil.charting.charts.PieChart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class BranchSummaryFragment extends Fragment implements SecondActivity.KeyPress {

    TableLayout branchSummaryTableLayout;
    //    ProgressBar branchSummaryProgressBar;
    ScrollView scrollBranchSummaryTable;
    PieChart pChartBranchSummary;
    Handler animationHandler;
    Fragment fragment;
    TextView scrollingBranchText;
    DigitalClock digitalClock;
    TextView branchSummaryHeaderTextView;
    public static boolean branchSummaryPaused;
    ImageView brancplayPause;
    ImageView brancleft;
    ImageView brancright;
    LinearLayout linearLayout;

    public HandleRowAnimationThread handleRowAnimationThread;

    int totalQuantity = 0;
    double grandTotal = 0;

    Activity activity;
    int rowIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SecondActivity.pausedstate()) {
            branchSummaryPaused = false;
        } else {
            branchSummaryPaused = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = getActivity();

        View view = inflater.inflate(R.layout.fragment_branch_summary, container, false);


        branchSummaryTableLayout = view.findViewById(R.id.branchSummaryTableLayout);
//        branchSummaryProgressBar = view.findViewById(R.id.branchSummaryProgressBar);
        scrollBranchSummaryTable = view.findViewById(R.id.scrollBranchSummaryTable);
        pChartBranchSummary = view.findViewById(R.id.pChartBranchSummary);
        scrollingBranchText = view.findViewById(R.id.scrollingBranchText);

        brancplayPause = view.findViewById(R.id.brancplayPause);

        brancleft = view.findViewById(R.id.branchleftArrow);
        brancright = view.findViewById(R.id.brancrightArrow);
        linearLayout = view.findViewById(R.id.branchKeypad);

        scrollingBranchText.setSelected(true);
        digitalClock = view.findViewById(R.id.branchSummary_digitalClock);
        branchSummaryHeaderTextView = view.findViewById(R.id.branchSummaryHeaderTextView);
        branchSummaryHeaderTextView.append(" from " + new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()));
        digitalClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));
        fragment = this;

        backTraverse(fragment, R.id.summaryOfLastMonthFragment);

        keyPadControl(branchSummaryPaused);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SplashScreenActivity.allData != null) {
            initFragment(SplashScreenActivity.allData.getDashBoardData(), 200, 0);
//            Log.i("TAG", SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() + "");
        }
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<BranchSummaryTableRow> tablesToDisplay, int seconds, int startingRowIndex) {
        grandTotal = 0;
        totalQuantity = 0;

        branchSummaryTableLayout.removeAllViews();
        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                    rowIndex = index;
                }

                if (index == tablesToDisplay.size()) {
                    drawLastBranchSummaryRow();
                    new UtilityFunctionsForActivity1().scrollRows(scrollBranchSummaryTable);

//                } else if (index == tablesToDisplay.size() + 1 && !SecondActivity.summaryOfBranchPause) {
                } else if (index == tablesToDisplay.size() + 1) {
//                    startActivity(new Intent(requireActivity(), MapsActivity.class));
                    if (branchSummaryPaused) {
                        if (handleRowAnimationThread != null) {
                            handleRowAnimationThread.interrupt();
                        }
                    } else {
                        navigate(fragment);
                    }

                } else if (index < tablesToDisplay.size()) {
                    totalLastRow(tablesToDisplay.get(index));
                    new UtilityFunctionsForActivity2().drawBranchSummary(tablesToDisplay, getContext(), branchSummaryTableLayout, index);
                    new UtilityFunctionsForActivity1().scrollRows(scrollBranchSummaryTable);
                }

            }

        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, seconds);
        handleRowAnimationThread.start();
    }


    public void initFragment(DashBoardData dashBoardDataParam, int seconds, int startingRowIndex) {
//        branchSummaryProgressBar.setVisibility(View.GONE);

        DashBoardData dashBoardData = dashBoardDataParam;

        ArrayList<BranchSummaryTableRow> branchSummaryTableRows = dashBoardData.getBranchSummaryData().getBranchSummaryTableRows();
        try {
            Collections.sort(branchSummaryTableRows, new Comparator<BranchSummaryTableRow>() {
                @Override
                public int compare(BranchSummaryTableRow o1, BranchSummaryTableRow o2) {
//                    return Integer.parseInt(o1.getBranch().substring(3)) < Integer.parseInt(o2.getBranch().substring(3)) ? -1 : 0;
                    return o1.getGrandTotal() > o2.getGrandTotal() ? 1 : 0;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        inflateTable(dashBoardData.getBranchSummaryData().getBranchSummaryTableRows(), seconds, startingRowIndex);
        new UtilityFunctionsForActivity2().drawPieChart(dashBoardData.getBranchSummaryData().getPieChartData(), pChartBranchSummary, "Branch summary");

    }

    public void backTraverse(Fragment fragment, int id) {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override

            public void handleOnBackPressed() {

                Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
                startActivity(intent);

            }
        });
    }

    public void totalLastRow(BranchSummaryTableRow row) {
        totalQuantity = totalQuantity + row.getQuantity();
        grandTotal = grandTotal + row.getGrandTotal();
    }

    public void drawLastBranchSummaryRow() {
        View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.table_row_branch_summary, null, false);

        TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowBranchSummary1);
        TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowBranchSummary2);
        TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowBranchSummary3);
        TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowBranchSummary4);
        TextView tableRowProperty5 = tableElements.findViewById(R.id.tableRowBranchSummary5);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        tableRowProperty1.setText("");
        tableRowProperty2.setText("Total Amount");
        tableRowProperty2.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty2.setTextSize(16f);
        tableRowProperty3.setText(String.valueOf(totalQuantity));
        tableRowProperty3.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty3.setTextSize(16f);
        tableRowProperty4.setText("");
        tableRowProperty5.setText(numberFormat.format(Math.round(grandTotal * 100.0) / 100.0));
        tableRowProperty5.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty5.setTextSize(16f);


        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        tableRowProperty2.startAnimation(animation);
        tableRowProperty3.startAnimation(animation);
        tableRowProperty5.startAnimation(animation);

        branchSummaryTableLayout.addView(tableElements);
        new UtilityFunctionsForActivity2().animateBottomToTop(branchSummaryTableLayout, tableElements);
    }

    public void navigate(Fragment fragment) {

        requireActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

        NavController navController = NavHostFragment.findNavController(fragment);

        if (SplashScreenActivity.allData.getLayoutList().contains(9))
            navController.navigate(R.id.userReportForAllOusFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(11))
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(1))
//                startActivity(new Intent(requireActivity(), MapsActivity.class));
            navController.navigate(R.id.vansOfASingleOrganizationFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(10))
            navController.navigate(R.id.userReportForEachOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(12))
            navController.navigate(R.id.peakHourReportFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(1))
//                startActivity(new Intent(requireActivity(), MapsActivity.class));
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
        else if (SplashScreenActivity.allData.getLayoutList().contains(8))
            navController.navigate(R.id.branchSummaryFragment);


    }

    public void leftNavigate(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(7))
            navController.navigate(R.id.summaryOfLastMonthFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(6))
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(5))
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(4))
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(3))
            navController.navigate(R.id.summarizedByArticleFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(10))
            navController.navigate(R.id.userReportForEachOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(12))
            navController.navigate(R.id.peakHourReportFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(1))
//            navController.navigate(R.id.vansOfASingleOrganizationFragment);
            navController.navigate(R.id.mapsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(1))
            navController.navigate(R.id.vansOfASingleOrganizationFragment);
//            navController.navigate(R.id.mapsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(11))
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
            navController.navigate(R.id.userReportForAllOusFragment2);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (handleRowAnimationThread != null)
            handleRowAnimationThread.interrupt();
    }

    @Override
    public void centerKey() {
        branchSummaryPaused = !branchSummaryPaused;
//        SecondActivity.firstCenterKeyPause = branchSummaryPaused;

        if (!branchSummaryPaused) {
            SecondActivity.playAll();
            navigate(fragment);
        } else {
            SecondActivity.pauseAll();
        }
        keyPadControl(branchSummaryPaused);

    }

    @Override
    public void leftKey() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        leftNavigate(fragment);
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
            brancplayPause.setImageResource(R.drawable.ic_play_button__2_);
            brancplayPause.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireActivity(), R.color.playbacksForeground)));
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }
}
package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
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
import android.widget.FrameLayout;
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

import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Data.SummarizedByChildArticleRow;
import com.cnet.VisualAnalysis.MapsActivity;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SummarizedByArticleChildCategFragment extends Fragment implements SecondActivity.KeyPress {

    TableLayout summaryByChildArticleTableLayout;
    ScrollView summarizedByChildArticleScrollView;
    Handler animationHandler;
    BarChart barChart;
    PieChart pieChart;
    Fragment fragment;
    FrameLayout summarizedByChildArticleFrameLayout;
    TextView scrollingChildText;
    DigitalClock digitalClock;
    TextView articleChildSummaryTitle;
    LinearLayout sumByChildKeyPad;
    ImageView sumChildArticleleftArrow;
    ImageView sumChildArticleplayPause;
    ImageView sumChildArticlerightArrow;

    public static HandleRowAnimationThread handleRowAnimationThread;

    double grandTotal = 0;
    public static boolean isInflatingTable;
    public static boolean summByChildArticlePaused;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SecondActivity.pausedstate()) {
            summByChildArticlePaused = false;
        } else {
            summByChildArticlePaused = true;

        }

        SecondActivity.interrupThreads(SummarizedByArticleFragment.handleRowAnimationThread,
                SummarizedByArticleParentCategFragment.handleRowAnimationThread,
                SummaryOfLastSixMonthsFragment.handleRowAnimationThread,
                SummaryOfLastMonthFragment.handleRowAnimationThread,
                BranchSummaryFragment.handleRowAnimationThread);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summarized_by_article_child_categ, container, false);


        fragment = this;
        summaryByChildArticleTableLayout = view.findViewById(R.id.summaryByChildArticleTableLayout);
        summarizedByChildArticleScrollView = view.findViewById(R.id.summarizedByChildArticleScrollView);
        pieChart = view.findViewById(R.id.pchartsumByArticleChild);
        barChart = view.findViewById(R.id.bChartSumByArticleChild);
        summarizedByChildArticleFrameLayout = view.findViewById(R.id.summarizedByChildArticleFrameLayout);
        scrollingChildText = view.findViewById(R.id.scrollingChildText);
        scrollingChildText.setSelected(true);
        digitalClock = view.findViewById(R.id.digitalClock);
        digitalClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));
        articleChildSummaryTitle = view.findViewById(R.id.articleChildSummaryTitle);
        articleChildSummaryTitle.append(" from " + new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()));

        sumChildArticleleftArrow = view.findViewById(R.id.sumChildArticleleftArrow);
        sumChildArticlerightArrow = view.findViewById(R.id.sumChildArticlerightArrow);
        sumChildArticleplayPause = view.findViewById(R.id.sumChildArticleplayPause);
        sumByChildKeyPad = view.findViewById(R.id.sumByChildKeyPad);

        backTraverse(fragment, R.id.summarizedByArticleParentCategFragment);
        keyPadControl(summByChildArticlePaused);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (SplashScreenActivity.allData != null && !isInflatingTable) {
            summarizedByChildArticleFrameLayout.setVisibility(View.GONE);
            initFragment(SplashScreenActivity.allData.getDashBoardData(), 200);

        }
    }

    public void initFragment(DashBoardData dashBoardDataParam, int seconds) {
        isInflatingTable = true;

        DashBoardData dashBoardData = dashBoardDataParam;

        if (dashBoardData.getSummarizedByChildArticleData() != null) {
            inflateTable(dashBoardData.getSummarizedByChildArticleData().getTableData(), seconds);
            UtilityFunctionsForActivity2.drawBarChart(dashBoardData.getSummarizedByChildArticleData().getBarChartData(), barChart, "Summarized by Article Child Category");
            UtilityFunctionsForActivity2.drawPieChart(dashBoardData.getSummarizedByChildArticleData().getPieChartData(), pieChart, "Summarized by Article Child Category");
        }

    }


    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummarizedByChildArticleRow> tablesToDisplay, int seconds) {
        grandTotal = 0;
        summaryByChildArticleTableLayout.removeAllViews();
        animationHandler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }
                if (index == tablesToDisplay.size()) {
                    drawLastArticleChildRow();
//                } else if (index == tablesToDisplay.size() + 1 && !SecondActivity.summaryByChildArticlePause) {
                } else if (index == tablesToDisplay.size() + 1) {

                    if (summByChildArticlePaused) {
                        handleRowAnimationThread.interrupt();
                    } else {
                        navigate(fragment);
                    }
                } else if (index < tablesToDisplay.size()) {
                    totalLastRow(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity2.drawSummaryByChildArticleTable(tablesToDisplay, getContext(), summaryByChildArticleTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(summarizedByChildArticleScrollView);
                }

            }

        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, seconds, this, 0);
        handleRowAnimationThread.start();
    }

    public void totalLastRow(SummarizedByChildArticleRow row) {

        grandTotal = grandTotal + row.getTotalAmount() + row.getTaxAmount() + row.getTotalServCharge();


    }

    public void drawLastArticleChildRow() {
        View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.table_row_summary_by_parent_article, null, false);

        TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowParentArtProperty1);
        TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowParentArtProperty2);
        TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowParentArtProperty3);
        TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowParentArtProperty4);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);


        tableRowProperty1.setText("");
        tableRowProperty2.setText("Total Amount");
        tableRowProperty2.setTextSize(16f);
        tableRowProperty2.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty3.setText(numberFormat.format(Math.round(grandTotal * 100.0) / 100.0));
        tableRowProperty3.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty3.setTextSize(16f);
        tableRowProperty4.setText("");

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        tableRowProperty2.startAnimation(animation);
        tableRowProperty3.startAnimation(animation);
        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));
        summaryByChildArticleTableLayout.addView(tableElements);
        UtilityFunctionsForActivity1.animate(summaryByChildArticleTableLayout, tableElements);
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

    public void navigate(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(5)) {

            if (SplashScreenActivity.allData.getLayoutList().contains(6))
                navController.navigate(R.id.summaryOfLastSixMonthsFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(7))
                navController.navigate(R.id.summaryOfLastMonthFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                navController.navigate(R.id.branchSummaryFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                navController.navigate(R.id.userReportForAllOusFragment2);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                navController.navigate(R.id.userReportForEachOusFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
                navController.navigate(R.id.peakHourReportForAllOusFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                navController.navigate(R.id.peakHourReportFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(1))
//                startActivity(new Intent(requireActivity(), MapsActivity.class));
                navController.navigate(R.id.vansOfASingleOrganizationFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(3))
                navController.navigate(R.id.summarizedByArticleFragment2);
            else if (SplashScreenActivity.allData.getLayoutList().contains(4))
                navController.navigate(R.id.summarizedByArticleParentCategFragment);
        }
    }

    public void navigateLeft(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(5)) {
            if (SplashScreenActivity.allData.getLayoutList().contains(4))
                navController.navigate(R.id.summarizedByArticleParentCategFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(3))
                navController.navigate(R.id.summarizedByArticleFragment2);
            else if (SplashScreenActivity.allData.getLayoutList().contains(1))
                startActivity(new Intent(requireActivity(), MapsActivity.class));
//                navController.navigate(R.id.vansOfASingleOrganizationFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(12))
                navController.navigate(R.id.peakHourReportFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(11))
                navController.navigate(R.id.peakHourReportForAllOusFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                navController.navigate(R.id.userReportForEachOusFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
                navController.navigate(R.id.userReportForAllOusFragment2);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                navController.navigate(R.id.branchSummaryFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(7))
                navController.navigate(R.id.summaryOfLastMonthFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(6))
                navController.navigate(R.id.summaryOfLastSixMonthsFragment);

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
        summByChildArticlePaused = !summByChildArticlePaused;
//        SecondActivity.firstCenterKeyPause = summByChildArticlePaused;

        if (!summByChildArticlePaused) {
            SecondActivity.playAll();

            navigate(fragment);
        } else {
            SecondActivity.pauseAll();
        }
        keyPadControl(summByChildArticlePaused);

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
            sumChildArticleplayPause.setImageResource(R.drawable.ic_play_button__2_);
            sumChildArticleplayPause.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireActivity(), R.color.playbacksForeground)));
            sumByChildKeyPad.setVisibility(View.VISIBLE);

        } else {
            sumByChildKeyPad.setVisibility(View.GONE);
        }
    }
}
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
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Data.SummarizedByArticleTableRow;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SummarizedByArticleFragment extends Fragment implements SecondActivity.KeyPress {

    TableLayout summarizedByArticleTableLayout;
    Handler animationHandler;
    TextView scrollingArticleText;
    TextView summarizedByArticleTextView;
    ScrollView summByArticleScrollView;
    LinearLayout sumByArticleKeyPad;
    ImageView summarticleleftArrow;
    ImageView summarticleplayPause;
    ImageView summarticlerightArrow;

    TextClock articleSummary_textClock;
    public HandleRowAnimationThread handleRowAnimationThread;
    public static boolean summByarticlePaused;

    TextView tableRowProperty1;
    TextView tableRowProperty2;
    TextView tableRowProperty3;
    TextView tableRowProperty4;
    TextView tableRowProperty5;

    double totalUnitAmount = 0;
    int totalQuantity = 0;
    double totalAvgAmount = 0;
    double totalGrand = 0;

    Fragment fragment;
    MaterialCardView pCardSummByArticle;
    ConstraintLayout articleCL;

    ////////////
    ArrayList<SummarizedByArticleTableRow> tablesToDisplay;
    ArrayList<Integer> layoutList;
    DashBoardData dashBoardData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SecondActivity.pausedstate()) {
            summByarticlePaused = false;
        } else {
            summByarticlePaused = true;
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_summarized_by_article, container, false);
        fragment = this;

        summarizedByArticleTableLayout = view.findViewById(R.id.summaryByArticleTableLayout);
        scrollingArticleText = view.findViewById(R.id.scrollingArticleText);
        scrollingArticleText.setSelected(true);
        summByArticleScrollView = view.findViewById(R.id.summByArticleScrollView);


        articleSummary_textClock = view.findViewById(R.id.articleSummary_textClock);
        articleSummary_textClock.setFormat12Hour("kk:mm:ss");
        articleSummary_textClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));

        summarizedByArticleTextView = view.findViewById(R.id.summarizedByArticleTextView);
        summarizedByArticleTextView.append(" on " + new SimpleDateFormat(Constants.dateCriteriaFormat, Locale.getDefault()).format(Calendar.getInstance().getTime()));

        summarticlerightArrow = view.findViewById(R.id.summarticlerightArrow);
        summarticleplayPause = view.findViewById(R.id.summarticleplayPause);
        summarticleleftArrow = view.findViewById(R.id.summarticleleftArrow);
        sumByArticleKeyPad = view.findViewById(R.id.sumByArticleKeyPad);

        pCardSummByArticle = view.findViewById(R.id.pCardSummByArticle);
        articleCL = view.findViewById(R.id.articleCL);

        keyPadControl(summByarticlePaused);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

//        BackGroundTasks backGroundTasks = new BackGroundTasks(fragment.getContext(), this);
//        backGroundTasks.execute();

        if (SplashScreenActivity.allData != null) {

            if (SplashScreenActivity.allData.getDashBoardData().getSummarizedByArticleData() != null) {

                layoutList = SplashScreenActivity.allData.getLayoutList();
                dashBoardData = SplashScreenActivity.allData.getDashBoardData();
                totalLastRow();
                Log.i("totalUnitAmount", totalUnitAmount + "");
                initFragment(SplashScreenActivity.allData.getDashBoardData(), 100);
            } else {
                navigate(fragment);
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummarizedByArticleTableRow> tablesToDisplay, int seconds) {
        summarizedByArticleTableLayout.removeAllViews();
        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }
                if (index == tablesToDisplay.size()) {
                    drawLastArticleSummaryTotalRow();
                    new UtilityFunctionsForActivity1().scrollRows(summByArticleScrollView);
//                } else if (index == tablesToDisplay.size() + 1 && !SecondActivity.summaryByArticlePause) {
                } else if (index == tablesToDisplay.size() + 1) {
                    if (getContext() != null) {
                        if (summByarticlePaused) {
                            if (handleRowAnimationThread != null) {
                                handleRowAnimationThread.interrupt();
                                handleRowAnimationThread = null;

                            }
                        } else {
                            resetLastRow();
                            navigate(fragment);
                        }
                    }

                } else if (index < tablesToDisplay.size()) {
//                    totalLastRow(tablesToDisplay.get(index));
                    new UtilityFunctionsForActivity2().drawSummaryByArticleTable(tablesToDisplay, getContext(), summarizedByArticleTableLayout, index);
                    new UtilityFunctionsForActivity1().scrollRows(summByArticleScrollView);
                }


            }

        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, seconds);
        handleRowAnimationThread.start();
        Log.i("Article - Thread", handleRowAnimationThread + "");
    }


    public void navigate(Fragment fragment) {

        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(4) &&
                SplashScreenActivity.allData.getDashBoardData().getSummarizedByParentArticleData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummarizedByParentArticleData().getTableData().size() > 0) {
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(5) &&
                SplashScreenActivity.allData.getDashBoardData().getSummarizedByChildArticleData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummarizedByChildArticleData().getTableData().size() > 0) {
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(6) &&
                SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast6MonthsData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast6MonthsData().getTableData().size() > 0) {
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(7) &&
                SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData().tableData.size() > 0) {
            navController.navigate(R.id.summaryOfLastMonthFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(8) &&
                SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData() != null
                && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0) {
            navController.navigate(R.id.branchSummaryFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(9) &&
                SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().size() > 0) {
            navController.navigate(R.id.userReportForAllOusFragment2);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(11) &&
                SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch().size() > 0) {
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(10) &&
                SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size() > 0) {
            navController.navigate(R.id.userReportForEachOusFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(12) &&
                SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size() > 0) {
            navController.navigate(R.id.peakHourReportFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(1) &&
                SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans() != null
                && SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size() > 0) {
            navController.navigate(R.id.mapsFragment);
        } else {
//            initFragment(dashBoardData, 100);
            startActivity(new Intent(requireActivity(), VideoActivity.class));

        }
    }

    public void naviagteLeft(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(3)) {
            if (layoutList != null) {
                if (SplashScreenActivity.allData.getLayoutList().contains(1) &&
                        SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans() != null
                        && SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size() > 0)
                    navController.navigate(R.id.mapsFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(12) &&
                        SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch() != null
                        && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size() > 0)
                    navController.navigate(R.id.peakHourReportFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(10) &&
                        SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch() != null
                        && SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size() > 0)
                    navController.navigate(R.id.userReportForEachOusFragment);
                else if (SplashScreenActivity.allData.getLayoutList().contains(11) &&
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
                else
                    startActivity(new Intent(requireActivity(), VideoActivity.class));

//                    initFragment(dashBoardData, 100);

            }
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

    public void totalLastRow() {
        tablesToDisplay = SplashScreenActivity.allData.getDashBoardData().getSummarizedByArticleData().getTableData();
        for (int i = 0; i < tablesToDisplay.size(); i++) {
            totalUnitAmount = totalUnitAmount + tablesToDisplay.get(i).getAvgAmount();
            totalQuantity = totalQuantity + tablesToDisplay.get(i).getQuantity();
            totalAvgAmount = totalAvgAmount + ((tablesToDisplay.get(i).getTotalAmount() + tablesToDisplay.get(i).getTaxAmount()) / tablesToDisplay.get(i).getQuantity());
            totalGrand = totalGrand + tablesToDisplay.get(i).getTotalAmount() + tablesToDisplay.get(i).getTaxAmount();
        }
    }

    public void resetLastRow() {
        totalUnitAmount = 0;
        totalQuantity = 0;
        totalAvgAmount = 0;
        totalGrand = 0;
    }

    public void drawLastArticleSummaryTotalRow() {
        if (getContext() != null) {
            View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.table_row_summary_by_article, null, false);

            tableRowProperty1 = tableElements.findViewById(R.id.tableRowArticleProperty1);
            tableRowProperty2 = tableElements.findViewById(R.id.tableRowArticleProperty2);
            tableRowProperty3 = tableElements.findViewById(R.id.tableRowArticleProperty3);
            tableRowProperty4 = tableElements.findViewById(R.id.tableRowArticleProperty4);
            tableRowProperty5 = tableElements.findViewById(R.id.tableRowArticleProperty5);

            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(true);

            tableRowProperty1.setText("");
            tableRowProperty2.setText("Total Amount");
            tableRowProperty2.setTypeface(Typeface.DEFAULT_BOLD);
            tableRowProperty2.setTextSize(16f);

            tableRowProperty3.setText(numberFormat.format(totalQuantity));
            tableRowProperty3.setTypeface(Typeface.DEFAULT_BOLD);
            tableRowProperty3.setTextSize(16f);

//            tableRowProperty4.setText(numberFormat.format(Math.round(totalUnitAmount * 100.0) / 100.0));
            tableRowProperty4.setText("");
            tableRowProperty4.setTypeface(Typeface.DEFAULT_BOLD);
            tableRowProperty4.setTextSize(16f);

//            tableRowProperty5.setText(numberFormat.format(Math.round(totalAmount * 100.0) / 100.0));
            tableRowProperty5.setText(UtilityFunctionsForActivity2.decimalFormat.format(totalGrand));
            tableRowProperty5.setTypeface(Typeface.DEFAULT_BOLD);
            tableRowProperty5.setTextSize(16f);

            tableElements.setBackgroundColor(Color.parseColor("#3f4152"));

            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
            tableRowProperty3.startAnimation(animation);
            tableRowProperty4.startAnimation(animation);
            tableRowProperty5.startAnimation(animation);

            summarizedByArticleTableLayout.addView(tableElements);
            new UtilityFunctionsForActivity1().animate(summarizedByArticleTableLayout, tableElements);
        }

    }


    private void initFragment(DashBoardData dashBoardDataParam, int seconds) {

        DashBoardData dashBoardData = dashBoardDataParam;

        inflateTable(dashBoardData.getSummarizedByArticleData().getTableData(), seconds);

//        int chartTypeIndex = SplashScreenActivity.allData.getLayoutList().indexOf(Constants.SUMMARY_OF_ARTICLE_INDEX);
        int chartTypeIndex = layoutList.indexOf(Constants.SUMMARY_OF_ARTICLE_INDEX);
        Log.i("TAG", chartTypeIndex + "");
        String chartType = "";
        if (chartTypeIndex < SplashScreenActivity.allData.getChartList().size()) {
            chartType = SplashScreenActivity.allData.getChartList().get(chartTypeIndex);
        } else {
            chartType = "";
        }
        Log.i("TAG", chartType + "");

        new UtilityFunctionsForActivity2().drawChart(getContext(), chartType, pCardSummByArticle,
                dashBoardData.getSummarizedByArticleData().pieChartData, dashBoardData.getSummarizedByArticleData().barChartData,
                dashBoardData.getSummarizedByArticleData().lineChartData, "Summarized by Article");

    }


    @Override
    public void centerKey() {
        summByarticlePaused = !summByarticlePaused;
//        SecondActivity.firstCenterKeyPause = summByarticlePaused;
        if (!summByarticlePaused) {
            SecondActivity.playAll();
            navigate(fragment);
        } else {
            SecondActivity.pauseAll();
        }
        keyPadControl(summByarticlePaused);

    }

    @Override
    public void leftKey() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
            handleRowAnimationThread = null;
            handleRowAnimationThread.destroy();
        }
        naviagteLeft(fragment);
    }

    @Override
    public void rightKey() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
            handleRowAnimationThread = null;

        }
        navigate(fragment);
    }

    public void keyPadControl(boolean paused) {
        if (paused) {
            summarticleplayPause.setImageResource(R.drawable.ic_play_button__2_);
            summarticleplayPause.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireActivity(), R.color.playbacksForeground)));
            sumByArticleKeyPad.setVisibility(View.VISIBLE);
        } else {
            sumByArticleKeyPad.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        summarizedByArticleTableLayout = null;
        scrollingArticleText = null;
        summByArticleScrollView = null;

        articleSummary_textClock = null;
        summarizedByArticleTextView = null;
        summarticlerightArrow = null;
        summarticleplayPause = null;
        summarticleleftArrow = null;
        sumByArticleKeyPad = null;
        pCardSummByArticle = null;

        tableRowProperty1 = null;
        tableRowProperty2 = null;
        tableRowProperty3 = null;
        tableRowProperty4 = null;
        tableRowProperty5 = null;
        articleCL = null;
        if (animationHandler != null)
            animationHandler.removeCallbacksAndMessages(null);

    }
}
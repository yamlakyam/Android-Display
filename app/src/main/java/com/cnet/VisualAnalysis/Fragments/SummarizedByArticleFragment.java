package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
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
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
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
import com.cnet.VisualAnalysis.Utils.BackGroundTasks;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SummarizedByArticleFragment extends Fragment implements SecondActivity.KeyPress, BackGroundTasks.CalculateInBackground {

    TableLayout summarizedByArticleTableLayout;
    Handler animationHandler;
    TextView scrollingArticleText;
    TextView summarizedByArticleTextView;
    ScrollView summByArticleScrollView;
    ProgressBar articleSummaryProgressBar;
    ConstraintLayout constraintLayout;

    LinearLayout sumByArticleKeyPad;
    ImageView summarticleleftArrow;
    ImageView summarticleplayPause;
    ImageView summarticlerightArrow;

    DigitalClock digitalClock;
    public HandleRowAnimationThread handleRowAnimationThread;
    public static boolean summByarticlePaused;

    double totalUnitAmount = 0;
    int totalQuantity = 0;
    double totalAmount = 0;

    Fragment fragment;
    MaterialCardView pCardSummByArticle;

    ////////////
    ArrayList<SummarizedByArticleTableRow> tablesToDisplay;

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
        articleSummaryProgressBar = view.findViewById(R.id.articleSummaryProgressBar);
        constraintLayout = view.findViewById(R.id.constraintLayout);
        digitalClock = view.findViewById(R.id.articleSummary_digitalClock);
        digitalClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));
        summarizedByArticleTextView = view.findViewById(R.id.summarizedByArticleTextView);
        summarizedByArticleTextView.append(" on " + new SimpleDateFormat(Constants.dateCriteriaFormat, Locale.getDefault()).format(Calendar.getInstance().getTime()));

        summarticlerightArrow = view.findViewById(R.id.summarticlerightArrow);
        summarticleplayPause = view.findViewById(R.id.summarticleplayPause);
        summarticleleftArrow = view.findViewById(R.id.summarticleleftArrow);
        sumByArticleKeyPad = view.findViewById(R.id.sumByArticleKeyPad);

        pCardSummByArticle = view.findViewById(R.id.pCardSummByArticle);

        keyPadControl(summByarticlePaused);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

//        BackGroundTasks backGroundTasks = new BackGroundTasks(fragment.getContext(), this);
//        backGroundTasks.execute();

        if (SplashScreenActivity.allData != null) {
            articleSummaryProgressBar.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.VISIBLE);

            if (SplashScreenActivity.allData.getDashBoardData().getSummarizedByArticleData() != null) {
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
    }


    public void navigate(Fragment fragment) {

        NavController navController = NavHostFragment.findNavController(fragment);

        if (SplashScreenActivity.allData.getLayoutList().contains(4) && SplashScreenActivity.allData.getDashBoardData().getSummarizedByParentArticleData() != null) {
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(5) && SplashScreenActivity.allData.getDashBoardData().getSummarizedByChildArticleData() != null) {
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(6) && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast6MonthsData() != null) {
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(7) && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData() != null) {
            navController.navigate(R.id.summaryOfLastMonthFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0) {
            navController.navigate(R.id.branchSummaryFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(9) && SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch() != null) {
            navController.navigate(R.id.userReportForAllOusFragment2);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(11) && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch() != null) {
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(10) && SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch() != null) {
            navController.navigate(R.id.userReportForEachOusFragment);

        } else if (SplashScreenActivity.allData.getLayoutList().contains(12) && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch() != null) {
            navController.navigate(R.id.peakHourReportFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(1) && SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans() != null) {
            navController.navigate(R.id.mapsFragment);
        } else {
            initFragment(SplashScreenActivity.allData.getDashBoardData(), 100);
        }

    }

    public void naviagteLeft(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(3)) {
            if (SplashScreenActivity.allData.getLayoutList().contains(1) && SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans() != null)
                navController.navigate(R.id.mapsFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(12) && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch() != null)
                navController.navigate(R.id.peakHourReportFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(10) && SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch() != null)
                navController.navigate(R.id.userReportForEachOusFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(11) && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch() != null)
                navController.navigate(R.id.peakHourReportForAllOusFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(9) && SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch() != null)
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
            else
                initFragment(SplashScreenActivity.allData.getDashBoardData(), 100);

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
            totalAmount = totalAmount + tablesToDisplay.get(i).getTotalAmount();
        }
    }

    public void resetLastRow() {
        totalUnitAmount = 0;
        totalQuantity = 0;
        totalAmount = 0;
    }

    public void drawLastArticleSummaryTotalRow() {
        if (getContext() != null) {
            View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.table_row_summary_by_article, null, false);

            TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowArticleProperty1);
            TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowArticleProperty2);
            TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowArticleProperty3);
            TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowArticleProperty4);
            TextView tableRowProperty5 = tableElements.findViewById(R.id.tableRowArticleProperty5);

            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(true);

            tableRowProperty1.setText("");
            tableRowProperty2.setText("Grand Total");
            tableRowProperty2.setTypeface(Typeface.DEFAULT_BOLD);
            tableRowProperty2.setTextSize(16f);

            tableRowProperty3.setText(numberFormat.format(totalQuantity));
            tableRowProperty3.setTypeface(Typeface.DEFAULT_BOLD);
            tableRowProperty3.setTextSize(16f);

//            tableRowProperty4.setText(numberFormat.format(Math.round(totalUnitAmount * 100.0) / 100.0));
            tableRowProperty4.setText(UtilityFunctionsForActivity2.decimalFormat.format(totalUnitAmount));
            tableRowProperty4.setTypeface(Typeface.DEFAULT_BOLD);
            tableRowProperty4.setTextSize(16f);

//            tableRowProperty5.setText(numberFormat.format(Math.round(totalAmount * 100.0) / 100.0));
            tableRowProperty5.setText(UtilityFunctionsForActivity2.decimalFormat.format(totalAmount));
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

        int chartTypeIndex = SplashScreenActivity.allData.getLayoutList().indexOf(Constants.SUMMARY_OF_ARTICLE_INDEX);
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
        }
        naviagteLeft(fragment);
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
            summarticleplayPause.setImageResource(R.drawable.ic_play_button__2_);
            summarticleplayPause.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireActivity(), R.color.playbacksForeground)));
            sumByArticleKeyPad.setVisibility(View.VISIBLE);
        } else {
            sumByArticleKeyPad.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPreExecute() {
        Log.i("TAG", "onPreExecute: ");
    }

    @Override
    public void doInBackground() {
        Log.i("TAG", "doInBackground: ");
        totalLastRow();
    }

    @Override
    public void onPostExecute() {
        Log.i("TAG", "onPostExecute: ");
        initFragment(SplashScreenActivity.allData.getDashBoardData(), 100);
    }
}
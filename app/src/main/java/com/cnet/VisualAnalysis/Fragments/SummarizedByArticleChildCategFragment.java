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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Data.SummarizedByChildArticleRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SummarizedByArticleChildCategFragment extends Fragment implements SecondActivity.KeyPress {

    TableLayout summaryByChildArticleTableLayout;
    ScrollView summarizedByChildArticleScrollView;
    Handler animationHandler;
    Fragment fragment;
    FrameLayout summarizedByChildArticleFrameLayout;
    TextView scrollingChildText;
    TextClock childArticle_textClock;
    TextView articleChildSummaryTitle;
    LinearLayout sumByChildKeyPad;
    ImageView sumChildArticleleftArrow;
    ImageView sumChildArticleplayPause;
    ImageView sumChildArticlerightArrow;

    MaterialCardView pCardSummByArticleChild;

    public HandleRowAnimationThread handleRowAnimationThread;


    double grandTotal = 0;
    double totalPercentage = 0;
    public static boolean summByChildArticlePaused;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SecondActivity.pausedstate()) {
            summByChildArticlePaused = false;
        } else {
            summByChildArticlePaused = true;

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summarized_by_article_child_categ, container, false);

        fragment = this;
        summaryByChildArticleTableLayout = view.findViewById(R.id.summaryByChildArticleTableLayout);
        summarizedByChildArticleScrollView = view.findViewById(R.id.summarizedByChildArticleScrollView);
        summarizedByChildArticleFrameLayout = view.findViewById(R.id.summarizedByChildArticleFrameLayout);
        scrollingChildText = view.findViewById(R.id.scrollingChildText);
        scrollingChildText.setSelected(true);

        childArticle_textClock = view.findViewById(R.id.childArticle_textClock);
        childArticle_textClock.setFormat12Hour("kk:mm:ss");
        childArticle_textClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));

        articleChildSummaryTitle = view.findViewById(R.id.articleChildSummaryTitle);
        articleChildSummaryTitle.append(" on " + new SimpleDateFormat(Constants.dateCriteriaFormat, Locale.getDefault()).format(Calendar.getInstance().getTime()));

        sumChildArticleleftArrow = view.findViewById(R.id.sumChildArticleleftArrow);
        sumChildArticlerightArrow = view.findViewById(R.id.sumChildArticlerightArrow);
        sumChildArticleplayPause = view.findViewById(R.id.sumChildArticleplayPause);
        sumByChildKeyPad = view.findViewById(R.id.sumByChildKeyPad);
        pCardSummByArticleChild = view.findViewById(R.id.pCardSummByArticleChild);

//        backTraverse(fragment, R.id.summarizedByArticleParentCategFragment);
        keyPadControl(summByChildArticlePaused);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (SplashScreenActivity.allData != null) {
            summarizedByChildArticleFrameLayout.setVisibility(View.GONE);
            if (SplashScreenActivity.allData.getDashBoardData().getSummarizedByChildArticleData() != null) {

                initFragment(SplashScreenActivity.allData.getDashBoardData(), 200);
            } else {
                Log.i("Child article", "onResume: ");
                navigate(fragment);
            }

        }
    }

    public void initFragment(DashBoardData dashBoardDataParam, int seconds) {

        DashBoardData dashBoardData = dashBoardDataParam;

        if (dashBoardData.getSummarizedByChildArticleData() != null) {
            inflateTable(dashBoardData.getSummarizedByChildArticleData().getTableData(), seconds);

            int chartTypeIndex = SplashScreenActivity.allData.getLayoutList().indexOf(Constants.SUMMARY_OF_CHILD_ARTICLE_INDEX);
            String chartType = "";
            if (chartTypeIndex < SplashScreenActivity.allData.getChartList().size()) {
                chartType = SplashScreenActivity.allData.getChartList().get(chartTypeIndex);
            } else {
                chartType = "";
            }

            new UtilityFunctionsForActivity2().drawChart(getContext(), chartType, pCardSummByArticleChild,
                    dashBoardData.getSummarizedByChildArticleData().getPieChartData(), dashBoardData.getSummarizedByChildArticleData().getBarChartData(),
                    dashBoardData.getSummarizedByChildArticleData().getLineChartData(), "Summarized by Article Child Category");

//            new UtilityFunctionsForActivity2().drawBarChart(dashBoardData.getSummarizedByChildArticleData().getBarChartData(), barChart, "Summarized by Article Child Category");
//            new UtilityFunctionsForActivity2().drawPieChart(dashBoardData.getSummarizedByChildArticleData().getPieChartData(), pieChart, "Summarized by Article Child Category");
        }

    }


    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummarizedByChildArticleRow> tablesToDisplay, int seconds) {
        grandTotal = 0;
        totalPercentage = 0;
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
                        if (handleRowAnimationThread != null) {
                            handleRowAnimationThread.interrupt();
                        }
                    } else {
                        navigate(fragment);
                    }
                } else if (index < tablesToDisplay.size()) {
                    totalLastRow(tablesToDisplay.get(index));
                    new UtilityFunctionsForActivity2().drawSummaryByChildArticleTable(tablesToDisplay, getContext(), summaryByChildArticleTableLayout, index);
                    new UtilityFunctionsForActivity1().scrollRows(summarizedByChildArticleScrollView);
                }

            }

        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, seconds);
        handleRowAnimationThread.start();
    }

    public void totalLastRow(SummarizedByChildArticleRow row) {

        ArrayList<SummarizedByChildArticleRow> summarizedByChildArticleRows = SplashScreenActivity.allData.getDashBoardData().getSummarizedByChildArticleData().getTableData();


        double grandTotalForAll = 0;
        for (int i = 0; i < summarizedByChildArticleRows.size(); i++) {
            double grandTotalForI = summarizedByChildArticleRows.get(i).getTotalAmount() +
                    summarizedByChildArticleRows.get(i).getTotalServCharge() +
                    summarizedByChildArticleRows.get(i).getTaxAmount();
            grandTotalForAll = grandTotalForAll + grandTotalForI;
        }

        grandTotal = grandTotal + row.getTotalAmount() + row.getTaxAmount() + row.getTotalServCharge();
        totalPercentage = totalPercentage + ((row.getTotalAmount() + row.getTotalServCharge() + row.getTaxAmount()) / grandTotalForAll) * 100;

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
//        tableRowProperty4.setText(numberFormat.format(Math.round(grandTotal * 100.0) / 100.0));
        tableRowProperty4.setText(UtilityFunctionsForActivity2.decimalFormat.format(grandTotal));
        tableRowProperty4.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty4.setTextSize(16f);

        tableRowProperty3.setText(UtilityFunctionsForActivity2.decimalFormat.format(totalPercentage) + "%");
        tableRowProperty3.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty3.setTextSize(16f);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        tableRowProperty2.startAnimation(animation);
        tableRowProperty3.startAnimation(animation);
        tableRowProperty4.startAnimation(animation);

        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));
        summaryByChildArticleTableLayout.addView(tableElements);
        new UtilityFunctionsForActivity1().animate(summaryByChildArticleTableLayout, tableElements);
    }

//    public void backTraverse(Fragment fragment, int id) {
//        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
//                startActivity(intent);
//            }
//        });
//    }

    public void navigate(Fragment fragment) {
        requireActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(6) &&
                SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast6MonthsData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast6MonthsData().getTableData().size() > 0)
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(7) &&
                SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData().tableData.size() > 0)
            navController.navigate(R.id.summaryOfLastMonthFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(8) &&
                SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData() != null
                && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
            navController.navigate(R.id.branchSummaryFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(9) &&
                SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().size() > 0)
            navController.navigate(R.id.userReportForAllOusFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(11) &&
                SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch().size() > 0)
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(10) &&
                SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size() > 0)
            navController.navigate(R.id.userReportForEachOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(12) &&
                SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch() != null
                && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size() > 0)
            navController.navigate(R.id.peakHourReportFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(1) &&
                SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans() != null
                && SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size() > 0)
            navController.navigate(R.id.mapsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(3) &&
                SplashScreenActivity.allData.getDashBoardData().getSummarizedByArticleData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummarizedByArticleData().tableData.size() > 0)
            navController.navigate(R.id.summarizedByArticleFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(4) &&
                SplashScreenActivity.allData.getDashBoardData().getSummarizedByParentArticleData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummarizedByParentArticleData().getTableData().size() > 0)
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
        else
            initFragment(SplashScreenActivity.allData.getDashBoardData(), 200);
    }

    public void navigateLeft(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(5)) {
            if (SplashScreenActivity.allData.getLayoutList().contains(4) &&
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
                navController.navigate(R.id.mapsFragment);
//                startActivity(new Intent(requireActivity(), MapsActivity.class));
//                navController.navigate(R.id.vansOfASingleOrganizationFragment);
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
            else
                initFragment(SplashScreenActivity.allData.getDashBoardData(), 200);
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
package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.cnet.VisualAnalysis.Data.BarChartData;
import com.cnet.VisualAnalysis.Data.LineChartData;
import com.cnet.VisualAnalysis.Data.PieChartData;
import com.cnet.VisualAnalysis.Data.UserReportTableRow;
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

public class UserReportForAllOusFragment extends Fragment implements SecondActivity.KeyPress {

    TableLayout userReportForAllTableLayout;
    TextView scrollingUserReportForAllText;
    TextView userReportForAllTitle;
    ScrollView userReportForAllScrollView;
    TextClock userReportAll_textClock;
    public Handler animationHandler;
    public HandleRowAnimationThread handleRowAnimationThread;

    private ArrayList<UserReportTableRow> tablesToDisplay;
    Fragment fragment;

    LinearLayout allUserRepKeyPad;
    ImageView userRepoAllleftArrow;
    ImageView userRepoAllplayPause;
    ImageView userRepoAllrightArrow;

    TextView userReportForAllSN;
    TextView userReportForAllUserName;
    TextView userReportForAllGrandTotal;
    TextView userReportForAllBranchName;
    TextView userReportForAllPercentage;

    ConstraintLayout userReportAllCL;

    double grandTotalSum;
    public static boolean userReportForAllPaused;

    MaterialCardView cCardAllUserReport;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SecondActivity.pausedstate()) {
            userReportForAllPaused = false;
        } else {
            userReportForAllPaused = true;

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_report_for_all_ous, container, false);

        userReportForAllTableLayout = view.findViewById(R.id.userReportForAllTableLayout);
        userReportForAllScrollView = view.findViewById(R.id.userReportForAllScrollView);
        scrollingUserReportForAllText = view.findViewById(R.id.scrollingUserReportForAllText);
        scrollingUserReportForAllText.setSelected(true);

        userReportAll_textClock = view.findViewById(R.id.userReportAll_textClock);
        userReportAll_textClock.setFormat12Hour("kk:mm:ss");
        userReportAll_textClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));

        userReportForAllTitle = view.findViewById(R.id.userReportForAllTitle);
        userReportForAllTitle.append(" on " + new SimpleDateFormat(Constants.dateCriteriaFormat, Locale.getDefault()).format(Calendar.getInstance().getTime()));
        allUserRepKeyPad = view.findViewById(R.id.allUserRepKeyPad);
        userRepoAllleftArrow = view.findViewById(R.id.userRepoAllleftArrow);
        userRepoAllplayPause = view.findViewById(R.id.userRepoAllplayPause);
        userRepoAllrightArrow = view.findViewById(R.id.userRepoAllrightArrow);
        cCardAllUserReport = view.findViewById(R.id.cCardAllUserReport);
        userReportAllCL = view.findViewById(R.id.userReportAllCL);

        fragment = this;

        keyPadControl(userReportForAllPaused);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (SplashScreenActivity.allData != null) {
            inflateTable();
            drawPieChartForAllUsers();
        }
    }

    @SuppressLint("HandlerLeak")
    public void inflateTable() {
        grandTotalSum = 0;
        if (userReportForAllTableLayout != null) {
            userReportForAllTableLayout.removeAllViews();
        }
        tablesToDisplay = SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch();
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
                    new UtilityFunctionsForActivity1().scrollRows(userReportForAllScrollView);
                } else if (index == tablesToDisplay.size() + 1) {
                    if (fragment != null) {
//                        NavController navController = NavHostFragment.findNavController(fragment);
//                        navController.navigate(R.id.vsmCardFragment);
                        if (userReportForAllPaused) {
                            if (handleRowAnimationThread != null) {
                                handleRowAnimationThread.interrupt();
                                handleRowAnimationThread = null;

                            }
                        } else {
                            navigate(fragment);
                        }
                    }

                } else if (index < tablesToDisplay.size()) {
                    grandTotalSum = grandTotalSum + tablesToDisplay.get(index).grandTotal;
                    new UtilityFunctionsForActivity1().drawUserReportForAllOu(tablesToDisplay, getContext(), userReportForAllTableLayout, index);
                    new UtilityFunctionsForActivity1().scrollRows(userReportForAllScrollView);
                }
            }
        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, 200);
        handleRowAnimationThread.start();
    }

    private void drawLastRow() {

        @SuppressLint("InflateParams") View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.user_report_for_all_table_row, null, false);
        userReportForAllSN = tableElements.findViewById(R.id.tableRowAllUserReportProperty1);
        userReportForAllUserName = tableElements.findViewById(R.id.tableRowAllUserReportProperty2);
        userReportForAllGrandTotal = tableElements.findViewById(R.id.tableRowAllUserReportProperty3);
        userReportForAllBranchName = tableElements.findViewById(R.id.tableRowAllUserReportProperty4);
        userReportForAllPercentage = tableElements.findViewById(R.id.tableRowAllUserReportProperty5);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        userReportForAllSN.setText("");
        userReportForAllUserName.setText("Total Amount");
        userReportForAllUserName.setTypeface(Typeface.DEFAULT_BOLD);
        userReportForAllUserName.setTextSize(16f);

        userReportForAllGrandTotal.setText(numberFormat.format(grandTotalSum));
        userReportForAllGrandTotal.setTypeface(Typeface.DEFAULT_BOLD);
        userReportForAllGrandTotal.setTextSize(16f);

        userReportForAllBranchName.setText("");
        userReportForAllBranchName.setTypeface(Typeface.DEFAULT_BOLD);
        userReportForAllBranchName.setTextSize(16f);

        userReportForAllPercentage.setText("");
        userReportForAllPercentage.setTypeface(Typeface.DEFAULT_BOLD);
        userReportForAllPercentage.setTextSize(16f);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        userReportForAllUserName.startAnimation(animation);
        userReportForAllGrandTotal.startAnimation(animation);
        userReportForAllPercentage.startAnimation(animation);

        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));

        if (userReportForAllTableLayout != null) {
            userReportForAllTableLayout.addView(tableElements);
            new UtilityFunctionsForActivity2().animateBottomToTop(userReportForAllTableLayout, tableElements);
        }
    }


    private void drawPieChartForAllUsers() {
        float[] x = new float[SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().size()];
        float[] y = new float[SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().size()];
        String[] label = new String[SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().size()];
        for (int i = 0; i < SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().size(); i++) {
            y[i] = (float) SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().get(i).grandTotal;
            x[i] = i;
            label[i] = SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().get(i).summaryType;
        }
        PieChartData pieChartData = new PieChartData(y, label);
        BarChartData barChartData = new BarChartData(x, y, label);
        LineChartData lineChartData = new LineChartData(x, y, label);

//        new UtilityFunctionsForActivity2().drawPieChart(pieChartData, pieChart, "User Report For All Organizations");

        int chartTypeIndex = SplashScreenActivity.allData.getLayoutList().indexOf(Constants.ALL_USER_REPORT_INDEX);
        String chartType;
        if (chartTypeIndex < SplashScreenActivity.allData.getChartList().size()) {
            chartType = SplashScreenActivity.allData.getChartList().get(chartTypeIndex);
        } else {
            chartType = "";
        }

        new UtilityFunctionsForActivity2().drawChart(getContext(), chartType, cCardAllUserReport,
                pieChartData, barChartData, lineChartData, "User Report For All Organizations");

    }

    public void navigate(Fragment fragment) {

//        Intent intent = new Intent(getActivity(), VideoActivity.class);
//        intent.putExtra("from", 9);
//        startActivity(intent);

        NavController navController = NavHostFragment.findNavController(fragment);

        if (SplashScreenActivity.allData.getLayoutList().contains(11) &&
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
        else if (SplashScreenActivity.allData.getLayoutList().contains(5) &&
                SplashScreenActivity.allData.getDashBoardData().getSummarizedByChildArticleData() != null
                && SplashScreenActivity.allData.getDashBoardData().getSummarizedByChildArticleData().getTableData().size() > 0)
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(6) &&
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
        else {
//            inflateTable();
//            drawPieChartForAllUsers();
            startActivity(new Intent(requireActivity(), VideoActivity.class));

        }

    }

    public void navigateLeft(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(8) &&
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
        else {
//            inflateTable();
//            drawPieChartForAllUsers();
            startActivity(new Intent(requireActivity(), VideoActivity.class));

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
        userReportForAllPaused = !userReportForAllPaused;
//        SecondActivity.firstCenterKeyPause = userReportForAllPaused;
        if (!userReportForAllPaused) {
            SecondActivity.playAll();
            navigate(fragment);
        } else {
            SecondActivity.pauseAll();
        }
        keyPadControl(userReportForAllPaused);

    }

    @Override
    public void leftKey() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
            handleRowAnimationThread = null;

        }
        navigateLeft(fragment);
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
            userRepoAllplayPause.setImageResource(R.drawable.ic_play_button__2_);
            userRepoAllplayPause.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireActivity(), R.color.playbacksForeground)));
            allUserRepKeyPad.setVisibility(View.VISIBLE);

        } else {
            allUserRepKeyPad.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        userReportForAllTableLayout = null;
        userReportForAllScrollView = null;
        scrollingUserReportForAllText = null;
        userReportAll_textClock = null;
        userReportForAllTitle = null;
        allUserRepKeyPad = null;
        userRepoAllleftArrow = null;
        userRepoAllplayPause = null;
        userRepoAllrightArrow = null;
        cCardAllUserReport = null;

        userReportForAllSN = null;
        userReportForAllUserName = null;
        userReportForAllGrandTotal = null;
        userReportForAllBranchName = null;
        userReportForAllPercentage = null;
        userReportAllCL = null;
        if (animationHandler != null)
            animationHandler.removeCallbacksAndMessages(null);

    }
}
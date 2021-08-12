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

import com.cnet.VisualAnalysis.Data.PieChartData;
import com.cnet.VisualAnalysis.Data.UserReportTableRow;
import com.cnet.VisualAnalysis.MapsActivity;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.github.mikephil.charting.charts.PieChart;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class UserReportForAllOusFragment extends Fragment implements SecondActivity.KeyPress {

    TableLayout userReportForAllTableLayout;
    TextView scrollingUserReportForAllText;
    TextView userReportForAllTitle;
    PieChart pieChart;
    ScrollView userReportForAllScrollView;
    DigitalClock digitalClock;
    public static Handler animationHandler;
    public HandleRowAnimationThread handleRowAnimationThread;

    private ArrayList<UserReportTableRow> tablesToDisplay;
    Fragment fragment;

    LinearLayout allUserRepKeyPad;
    ImageView userRepoAllleftArrow;
    ImageView userRepoAllplayPause;
    ImageView userRepoAllrightArrow;

    double grandTotalSum;
    public static boolean userReportForAllPaused;

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
        pieChart = view.findViewById(R.id.pchartUserReportForAll);
        userReportForAllScrollView = view.findViewById(R.id.userReportForAllScrollView);
        scrollingUserReportForAllText = view.findViewById(R.id.scrollingUserReportForAllText);
        scrollingUserReportForAllText.setSelected(true);
        digitalClock = view.findViewById(R.id.digitalClock);
        digitalClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));
        userReportForAllTitle = view.findViewById(R.id.userReportForAllTitle);
        userReportForAllTitle.append(" from " + new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()));

        allUserRepKeyPad = view.findViewById(R.id.allUserRepKeyPad);
        userRepoAllleftArrow = view.findViewById(R.id.userRepoAllleftArrow);
        userRepoAllplayPause = view.findViewById(R.id.userRepoAllplayPause);
        userRepoAllrightArrow = view.findViewById(R.id.userRepoAllrightArrow);

        fragment = this;

        keyPadControl(userReportForAllPaused);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch() != null) {
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
                    UtilityFunctionsForActivity1.scrollRows(userReportForAllScrollView);
                } else if (index == tablesToDisplay.size() + 1) {
                    if (fragment != null) {
//                        NavController navController = NavHostFragment.findNavController(fragment);
//                        navController.navigate(R.id.vsmCardFragment);
                        if (userReportForAllPaused) {
                            handleRowAnimationThread.interrupt();
                        } else {
                            navigate(fragment);
                        }
                    }

                } else if (index < tablesToDisplay.size()) {
                    grandTotalSum = grandTotalSum + tablesToDisplay.get(index).grandTotal;
                    UtilityFunctionsForActivity1.drawUserReportForAllOu(tablesToDisplay, getContext(), userReportForAllTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(userReportForAllScrollView);
                }
            }
        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, 200, this, 0);
        handleRowAnimationThread.start();
    }

    private void drawLastRow() {

        View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.user_report_for_all_table_row, null, false);
        TextView userReportForAllSN = tableElements.findViewById(R.id.tableRowAllUserReportProperty1);
        TextView userReportForAllUserName = tableElements.findViewById(R.id.tableRowAllUserReportProperty2);
        TextView userReportForAllGrandTotal = tableElements.findViewById(R.id.tableRowAllUserReportProperty3);
        TextView userReportForAllBranchName = tableElements.findViewById(R.id.tableRowAllUserReportProperty4);
        TextView userReportForAllPercentage = tableElements.findViewById(R.id.tableRowAllUserReportProperty5);

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
            UtilityFunctionsForActivity2.animateBottomToTop(userReportForAllTableLayout, tableElements);

        }

    }

    private void drawPieChartForAllUsers() {

        float[] x = new float[SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().size()];
        String[] label = new String[SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().size()];
        for (int i = 0; i < SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().size(); i++) {
            x[i] = (float) SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().get(i).grandTotal;
            label[i] = SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch().get(i).summaryType;
        }
        PieChartData pieChartData = new PieChartData(x, label);
        UtilityFunctionsForActivity2.drawPieChart(pieChartData, pieChart, "User Report For All Organizations");

    }

    public void navigate(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
            if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                navController.navigate(R.id.userReportForEachOusFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
                navController.navigate(R.id.peakHourReportForAllOusFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
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
            else if (SplashScreenActivity.allData.getLayoutList().contains(8)&& SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
                navController.navigate(R.id.branchSummaryFragment);
        }
    }

    public void navigateLeft(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(8)&& SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0)
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
            startActivity(new Intent(requireActivity(), MapsActivity.class));
//            navController.navigate(R.id.vansOfASingleOrganizationFragment);

        else if (SplashScreenActivity.allData.getLayoutList().contains(12))
            navController.navigate(R.id.peakHourReportFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(11))
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(10))
            navController.navigate(R.id.userReportForEachOusFragment);
    }

    @Override
    public void onStop() {
        super.onStop();
        handleRowAnimationThread.interrupt();
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
            userRepoAllplayPause.setImageResource(R.drawable.ic_play_button__2_);
            userRepoAllplayPause.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireActivity(), R.color.playbacksForeground)));
            allUserRepKeyPad.setVisibility(View.VISIBLE);

        } else {
            allUserRepKeyPad.setVisibility(View.GONE);
        }
    }
}
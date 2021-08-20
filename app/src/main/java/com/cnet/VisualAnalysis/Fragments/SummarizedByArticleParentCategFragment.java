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
import com.cnet.VisualAnalysis.Data.SummarizedByParentArticleRow;
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

public class SummarizedByArticleParentCategFragment extends Fragment implements SecondActivity.KeyPress {

    Handler animationHandler;

    TableLayout summarizedByParentArticleTableLayout;
    ScrollView scrollView;
    PieChart pieChart;
    BarChart barChart;
    Fragment fragment;
    FrameLayout summarizedByParentArticleFrameLayout;
    TextView scrollingParentText;
    DigitalClock digitalClock;
    TextView articleParentSummaryTitle;

    LinearLayout sumByParentKeyPad;
    ImageView sumParentArticleleftArrow;
    ImageView sumParentArticleplayPause;
    ImageView sumParentArticlerightArrow;

    public HandleRowAnimationThread handleRowAnimationThread;

    double grandTotal = 0;
    public static boolean summByParentArticlePaused;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SecondActivity.pausedstate()) {
            summByParentArticlePaused = false;
        } else {
            summByParentArticlePaused = true;
        }


//        if (SecondActivity.dashBoardData == null) {
//            VolleyHttp http = new VolleyHttp(getContext());
//            http.makeGetRequest(Constants.allDataWithConfigurationURL, this);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_summarized_by_article_parent_categ, container, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragment = this;


        summarizedByParentArticleTableLayout = view.findViewById(R.id.summaryByChildArticleTableLayout);
        scrollView = view.findViewById(R.id.summarizedByChildArticleScrollView);
        pieChart = view.findViewById(R.id.pchartsumByArticleParent);
        barChart = view.findViewById(R.id.bChartSumByArticleParent);
        summarizedByParentArticleFrameLayout = view.findViewById(R.id.summarizedByParentArticleFrameLayout);
        scrollingParentText = view.findViewById(R.id.scrollingParentText);
        scrollingParentText.setSelected(true);
        digitalClock = view.findViewById(R.id.digitalClock);
        digitalClock.setTypeface(ResourcesCompat.getFont(requireActivity(), R.font.digital_7));
        articleParentSummaryTitle = view.findViewById(R.id.articleParentSummaryTitle);
        articleParentSummaryTitle.append(" from " + new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()));
        sumByParentKeyPad = view.findViewById(R.id.sumByParentKeyPad);
        sumParentArticleleftArrow = view.findViewById(R.id.sumParentArticleleftArrow);
        sumParentArticleplayPause = view.findViewById(R.id.sumParentArticleplayPause);
        sumParentArticlerightArrow = view.findViewById(R.id.sumParentArticlerightArrow);

        backTraverse(fragment, R.id.summarizedByArticleFragment2);

        keyPadControl(summByParentArticlePaused);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (SplashScreenActivity.allData != null) {
            summarizedByParentArticleFrameLayout.setVisibility(View.GONE);
            initFragment(SplashScreenActivity.allData.getDashBoardData(), 200);
        }
    }

    @SuppressLint("HandlerLeak")
    private void inflateTable(ArrayList<SummarizedByParentArticleRow> tablesToDisplay, int seconds) {

        grandTotal = 0;
        if (summarizedByParentArticleTableLayout != null) {
            summarizedByParentArticleTableLayout.removeAllViews();

        }
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
                    drawLastArticleParentRow();
                    UtilityFunctionsForActivity1.scrollRows(scrollView);

//                } else if (index == tablesToDisplay.size() + 1 && !SecondActivity.summaryByParentArticlePause) {
                } else if (index == tablesToDisplay.size() + 1) {

                    if (summByParentArticlePaused) {
                        if (handleRowAnimationThread != null) {
                            handleRowAnimationThread.interrupt();
                        }
                    } else {
                        navigate(fragment);
                    }

                } else if (index < tablesToDisplay.size()) {
                    totalLastRow(tablesToDisplay.get(index));
                    UtilityFunctionsForActivity2.drawSummaryByParentArticleTable(tablesToDisplay, getContext(), summarizedByParentArticleTableLayout, index);
                    UtilityFunctionsForActivity1.scrollRows(scrollView);
                }
            }
        };

        handleRowAnimationThread = new HandleRowAnimationThread(tablesToDisplay.size(), animationHandler, seconds, this, 0);
        handleRowAnimationThread.start();

    }


    private void initFragment(DashBoardData dashBoardDataP, int seconds) {


        DashBoardData dashBoardData = dashBoardDataP;

        if (dashBoardData != null) {
            if (dashBoardData.getSummarizedByParentArticleData() != null) {
                inflateTable(dashBoardData.getSummarizedByParentArticleData().getTableData(), seconds);

                UtilityFunctionsForActivity2.drawBarChart(dashBoardData.getSummarizedByParentArticleData().getBarChartData(), barChart, "Summarized by Article parent category");
                UtilityFunctionsForActivity2.drawPieChart(dashBoardData.getSummarizedByParentArticleData().getPieChartData(), pieChart, "Summarized by Article parent category");
            }
        }
    }

    public void totalLastRow(SummarizedByParentArticleRow row) {

        grandTotal = grandTotal + row.getTotalAmount() + row.getTaxAmount() + row.getTotalServCharge();


    }

    public void drawLastArticleParentRow() {
        View tableElements = LayoutInflater.from(getContext()).inflate(R.layout.table_row_summary_by_parent_article, null, false);
        TextView tableRowProperty1 = tableElements.findViewById(R.id.tableRowParentArtProperty1);
        TextView tableRowProperty2 = tableElements.findViewById(R.id.tableRowParentArtProperty2);
        TextView tableRowProperty3 = tableElements.findViewById(R.id.tableRowParentArtProperty3);
        TextView tableRowProperty4 = tableElements.findViewById(R.id.tableRowParentArtProperty4);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);


        tableRowProperty1.setText("");
        tableRowProperty2.setText("Total Amount");
        tableRowProperty2.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty2.setTextSize(16f);


        tableRowProperty3.setText(numberFormat.format(Math.round(grandTotal * 100.0) / 100.0));
        tableRowProperty3.setTypeface(Typeface.DEFAULT_BOLD);
        tableRowProperty3.setTextSize(16f);


        tableRowProperty4.setText("");

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        tableRowProperty3.startAnimation(animation);
        tableElements.setBackgroundColor(Color.parseColor("#3f4152"));
        summarizedByParentArticleTableLayout.addView(tableElements);
        UtilityFunctionsForActivity1.animate(summarizedByParentArticleTableLayout, tableElements);

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

//        Intent intent = new Intent(getActivity(), VideoActivity.class);
//        intent.putExtra("from", 4);
//        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(requireContext(), R.anim.slide_in_bottom, R.anim.slide_out_bottom).toBundle();
//
//        startActivity(intent,bundle);
        requireActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(4)) {
            if (SplashScreenActivity.allData.getLayoutList().contains(5))
                navController.navigate(R.id.summarizedByArticleChildCategFragment);
            else if (SplashScreenActivity.allData.getLayoutList().contains(6))
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
        }

    }

    public void navigateLeft(Fragment fragment) {

        NavController navController = NavHostFragment.findNavController(fragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(4)) {
            if (SplashScreenActivity.allData.getLayoutList().contains(3))
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
            else if (SplashScreenActivity.allData.getLayoutList().contains(5))
                navController.navigate(R.id.summarizedByArticleChildCategFragment);
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
        summByParentArticlePaused = !summByParentArticlePaused;
//        SecondActivity.firstCenterKeyPause = summByParentArticlePaused;

        if (!summByParentArticlePaused) {
            SecondActivity.playAll();
            navigate(fragment);
        } else {
            SecondActivity.pauseAll();
        }
        keyPadControl(summByParentArticlePaused);

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
            sumParentArticleplayPause.setImageResource(R.drawable.ic_play_button__2_);
            sumParentArticleplayPause.setImageTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireActivity(), R.color.playbacksForeground)));
            sumByParentKeyPad.setVisibility(View.VISIBLE);

        } else {
            sumByParentKeyPad.setVisibility(View.GONE);
        }
    }
}
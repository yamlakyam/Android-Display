package com.cnet.VisualAnalysis.Threads;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.cnet.VisualAnalysis.Fragments.BranchSummaryFragment;
import com.cnet.VisualAnalysis.Fragments.SummarizedByArticleChildCategFragment;
import com.cnet.VisualAnalysis.Fragments.SummarizedByArticleFragment;
import com.cnet.VisualAnalysis.Fragments.SummarizedByArticleParentCategFragment;
import com.cnet.VisualAnalysis.Fragments.SummaryOfLastMonthFragment;
import com.cnet.VisualAnalysis.Fragments.SummaryOfLastSixMonthsFragment;
import com.cnet.VisualAnalysis.Fragments.SummaryTableFragment;
import com.cnet.VisualAnalysis.SplashScreenActivity;


public class HandleRowAnimationThread extends Thread {

    int rows;
    Handler changeDataHandler;
    int numberOfSeconds;
    Fragment fragment;
    int startingIndex;

    public HandleRowAnimationThread(int rows, Handler changeDataHandler, int numberOfSeconds, Fragment fragment, int startingIndex) {
        this.changeDataHandler = changeDataHandler;
        this.rows = rows;
        this.numberOfSeconds = numberOfSeconds;
        this.fragment = fragment;
        this.startingIndex = startingIndex;

    }

    @Override
    public void run() {
        super.run();
        for (int i = startingIndex; i <= rows + 1; i++) {

            Message message = changeDataHandler.obtainMessage();
            message.obj = String.valueOf(i);
            changeDataHandler.sendMessage(message);
            try {
                if (i == rows + 1) {
                    Thread.sleep(3000);

                } else if (i == rows) {
                    lastRowAnimationSleep();
                } else {
                    Thread.sleep(numberOfSeconds);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void lastRowAnimationSleep() throws InterruptedException {
        SummarizedByArticleFragment.isInflatingTable = false;
        SummarizedByArticleChildCategFragment.isInflatingTable = false;
        SummarizedByArticleParentCategFragment.isInflatingTable = false;
        SummaryOfLastSixMonthsFragment.isInflatingTable = false;
        SummaryOfLastMonthFragment.isInflatingTable = false;
        BranchSummaryFragment.isInflatingTable = false;
        if (SplashScreenActivity.allData != null) {

            if (fragment instanceof SummaryTableFragment) {
                int secondsToWait = Integer.parseInt(SplashScreenActivity.allData.getTransitionTimeInMinutes()) - (rows * (numberOfSeconds / 1000));
                Log.i("SECONDS ", secondsToWait + "");
                if (secondsToWait < 0) {
                    secondsToWait = rows * (numberOfSeconds / 1000) + 5000;
                }
                Thread.sleep(secondsToWait * 1000);
            } else {
                Thread.sleep(5000);
            }

        } else {
            Thread.sleep(5000);
        }
    }
}
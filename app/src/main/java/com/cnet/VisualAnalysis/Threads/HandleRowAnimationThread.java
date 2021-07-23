package com.cnet.VisualAnalysis.Threads;

import android.os.Handler;
import android.os.Message;
import android.provider.Browser;

import androidx.fragment.app.Fragment;

import com.cnet.VisualAnalysis.Data.SummarizedByArticleData;
import com.cnet.VisualAnalysis.Data.SummarizedByChildArticleRow;
import com.cnet.VisualAnalysis.Fragments.BranchSummaryFragment;
import com.cnet.VisualAnalysis.Fragments.DistributorTableFragment;
import com.cnet.VisualAnalysis.Fragments.SummarizedByArticleChildCategFragment;
import com.cnet.VisualAnalysis.Fragments.SummarizedByArticleFragment;
import com.cnet.VisualAnalysis.Fragments.SummarizedByArticleParentCategFragment;
import com.cnet.VisualAnalysis.Fragments.SummaryOfLastMonthFragment;
import com.cnet.VisualAnalysis.Fragments.SummaryOfLastSixMonthsFragment;
import com.cnet.VisualAnalysis.Fragments.SummaryTableFragment;


public class HandleRowAnimationThread extends Thread {

    int rows;
    Handler changeDataHandler;
    int numberOfSeconds;

    public HandleRowAnimationThread(int rows, Handler changeDataHandler, int numberOfSeconds ){
        this.changeDataHandler = changeDataHandler;
        this.rows=rows;
        this.numberOfSeconds=numberOfSeconds;
    }
    @Override
    public void run() {
        super.run();
        for (int i = 0; i <= rows+1; i++) {

            Message message = changeDataHandler.obtainMessage();
            message.obj = String.valueOf(i);
            changeDataHandler.sendMessage(message);
            try {
                if(i==rows+1){
                    Thread.sleep(3000);
                }
                else if(i==rows){
                    SummarizedByArticleFragment.isInflatingTable = false;
                    SummarizedByArticleChildCategFragment.isInflatingTable = false;
                    SummarizedByArticleParentCategFragment.isInflatingTable = false;
                    SummaryOfLastSixMonthsFragment.isInflatingTable = false;
                    SummaryOfLastMonthFragment.isInflatingTable = false;
                    BranchSummaryFragment.isInflatingTable = false;

                    Thread.sleep(5000);
                }
                else{
                    Thread.sleep(numberOfSeconds);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
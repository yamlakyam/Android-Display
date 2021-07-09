package com.cnet.VisualAnalysis.Threads;

import android.os.Handler;
import android.os.Message;
import android.provider.Browser;

import androidx.fragment.app.Fragment;

import com.cnet.VisualAnalysis.Fragments.DistributorTableFragment;
import com.cnet.VisualAnalysis.Fragments.SummaryTableFragment;


public class HandleRowAnimationThread extends Thread {

    int rows;
    Handler changeDataHandler;

    public HandleRowAnimationThread(int rows, Handler changeDataHandler){
        this.changeDataHandler = changeDataHandler;
        this.rows=rows;
    }
    @Override
    public void run() {
        super.run();
        for (int i = 0; i < rows; i++) {

            Message message = changeDataHandler.obtainMessage();
            message.obj = String.valueOf(i);
            changeDataHandler.sendMessage(message);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}
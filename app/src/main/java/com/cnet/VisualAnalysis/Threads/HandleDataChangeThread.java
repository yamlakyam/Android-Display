package com.cnet.VisualAnalysis.Threads;

import android.os.Handler;
import android.os.Message;

import com.cnet.VisualAnalysis.SplashScreenActivity;

public class HandleDataChangeThread extends Thread {
    Handler changeDataHandler;
    int numberOfDistributors;
    int numberOfSeconds;
    int startingIndex;

    public HandleDataChangeThread(Handler changeDataHandler, int numberOfDistributors, int numberOfSeconds, int startingIndex) {
        this.changeDataHandler = changeDataHandler;
        this.numberOfDistributors = numberOfDistributors;
        this.numberOfSeconds = numberOfSeconds;
        this.startingIndex = startingIndex;
    }

//    VsmTransactionFragment vsmTransactionFragment = new VsmTransactionFragment();
//    Integer vansSum =vsmTransactionFragment.vansSum(SplashScreenActivity.allData.getFmcgData().getVsmTableForSingleDistributors().get(1).getAllVansData().get(0)

    public void run() {
        super.run();

        for (int i = startingIndex; i < numberOfDistributors; i++) {
            Message message = changeDataHandler.obtainMessage();
            message.obj = String.valueOf(i);
            changeDataHandler.sendMessage(message);
            try {

                Thread.sleep(numberOfSeconds * 1000);

            } catch (InterruptedException e) {

                e.printStackTrace();
                return;
            }
        }
    }
}


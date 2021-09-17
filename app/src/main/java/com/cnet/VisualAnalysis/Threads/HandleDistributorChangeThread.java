package com.cnet.VisualAnalysis.Threads;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cnet.VisualAnalysis.SplashScreenActivity;

public class HandleDistributorChangeThread extends Thread {
    Handler changeDataHandler;
    int numberOfDistributors;
    int numberOfSeconds;
    int startingIndex;

    public HandleDistributorChangeThread(Handler changeDataHandler, int numberOfDistributors, int numberOfSeconds, int startingIndex) {
        this.changeDataHandler = changeDataHandler;
        this.numberOfDistributors = numberOfDistributors;
        this.numberOfSeconds = numberOfSeconds;
        this.startingIndex = startingIndex;
    }

    public void run() {
        super.run();

        Log.i("HandleDistributorChange", "Called");
        for (int i = startingIndex; i < numberOfDistributors; i++) {
            Message message = changeDataHandler.obtainMessage();
            message.obj = String.valueOf(i);
            changeDataHandler.sendMessage(message);
            try {
                int wait = Integer.parseInt(SplashScreenActivity.allData.getTransitionTimeInMinutes());
                Log.i("wait", SplashScreenActivity.allData.getFmcgData().getDistributorTableRows().get(i).getTableData().size() * wait + " ");
                Thread.sleep((int) ((SplashScreenActivity.allData.getFmcgData().getDistributorTableRows().get(i).getTableData().size() * wait * 1000) * 1.2));
            } catch (InterruptedException e) {

                e.printStackTrace();
                return;
            }
        }
    }
}

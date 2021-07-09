package com.cnet.VisualAnalysis.Threads;

import android.os.Handler;
import android.os.Message;

import com.cnet.VisualAnalysis.Utils.VolleyHttp;

public class HandleDataChangeThread extends Thread {
    Handler changeDataHandler;
    int numberOfDistributors;

    public HandleDataChangeThread( Handler changeDataHandler, int numberOfDistributors){
        this.changeDataHandler = changeDataHandler;
        this.numberOfDistributors = numberOfDistributors;
    }

    public void run() {
        super.run();
        for (int i = 0; i < numberOfDistributors; i++) {
            Message message = changeDataHandler.obtainMessage();
            message.obj = String.valueOf(i);
            changeDataHandler.sendMessage(message);
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

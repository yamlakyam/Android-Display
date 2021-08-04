package com.cnet.VisualAnalysis.Threads;

import android.os.Handler;
import android.os.Message;

public class HandleDataRefreshThread extends Thread {

    Handler changeDataHandler;
    int refreshSec;

    public HandleDataRefreshThread(Handler changeDataHandler, int refreshSec) {
        this.changeDataHandler = changeDataHandler;
        this.refreshSec = refreshSec;
    }


    @Override
    public void run() {
        super.run();

        for (int i = 0; i <24*60*60/refreshSec ; i++) {
            Message message = changeDataHandler.obtainMessage();
            message.obj = String.valueOf(refreshSec);
            try {
                Thread.sleep(refreshSec * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



    }
}

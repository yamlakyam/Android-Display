package com.cnet.VisualAnalysis.Threads;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cnet.VisualAnalysis.Data.VsmTableDataForSingleVan;

import java.util.ArrayList;

public class HandleVanChangeThread extends Thread {
    Handler changeDataHandler;
    int numberOfVans;
    int startingIndex;
    ArrayList<VsmTableDataForSingleVan> vsmTableDataForSingleVan;


    public HandleVanChangeThread(Handler changeDataHandler, int numberOfDistributors, ArrayList<VsmTableDataForSingleVan> vsmTableDataForSingleVan, int startingIndex) {
        this.changeDataHandler = changeDataHandler;
        this.numberOfVans = numberOfDistributors;
        this.startingIndex = startingIndex;
        this.vsmTableDataForSingleVan = vsmTableDataForSingleVan;
    }

    public void run() {

        Log.i("HandleVanChangeThread", "called ");

        for (int i = startingIndex; i < numberOfVans; i++) {
            Message message = changeDataHandler.obtainMessage();
            message.obj = String.valueOf(i);
            changeDataHandler.sendMessage(message);
            try {
                Thread.sleep(vsmTableDataForSingleVan.get(i).tableRows.size() * 1000 + 2000);
            } catch (InterruptedException e) {

                e.printStackTrace();
                return;
            }
        }
    }
}

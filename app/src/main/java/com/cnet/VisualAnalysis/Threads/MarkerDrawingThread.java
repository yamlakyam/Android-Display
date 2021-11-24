package com.cnet.VisualAnalysis.Threads;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MarkerDrawingThread extends Thread {

    int rows;
    Handler changeDataHandler;
    int numberOfSeconds;
    private boolean isInterrupted = false;

    public MarkerDrawingThread(int rows, Handler changeDataHandler, int numberOfSeconds) {
        this.changeDataHandler = changeDataHandler;
        this.rows = rows;
        this.numberOfSeconds = numberOfSeconds;
    }

    @Override
    public void run() {
        super.run();

        Log.i("MarkerDrawingThread", Thread.currentThread() + "");

        if (!Thread.currentThread().isInterrupted() && !isInterrupted) {
            for (int i = 0; i <= rows + 1; i++) {
                Message message = changeDataHandler.obtainMessage();
                message.obj = String.valueOf(i);
                changeDataHandler.sendMessage(message);
                try {
                    if (i == rows + 1) {
                        Log.i("ROW+1", "run: ");
                        Thread.sleep(3000);

                    } else if (i == rows) {
                        Log.i("ROW", "run: ");
//                    lastRowAnimationSleep();
                        Thread.sleep(3000);
                    } else if (i == rows - 1) {
                        Log.i("ROW-1", "run: ");
                        Thread.sleep(5000);
                    } else {
                        Thread.sleep(numberOfSeconds);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                    isInterrupted = true;
                    return;
                }
            }
        }
    }

    public void interruptMarker() {
        isInterrupted = true;
        Thread.currentThread().interrupt();

    }

}





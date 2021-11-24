package com.cnet.VisualAnalysis.Threads;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.cnet.VisualAnalysis.SplashScreenActivity;


public class HandleRowAnimationThread extends Thread {

    int rows;
    Handler changeDataHandler;
    int numberOfSeconds;


    public HandleRowAnimationThread(int rows, Handler changeDataHandler, int numberOfSeconds) {
        this.changeDataHandler = changeDataHandler;
        this.rows = rows;
        this.numberOfSeconds = numberOfSeconds;

    }

    @Override
    public void run() {
        super.run();
        Log.i("ThreadCalled", Thread.currentThread() + "");

        if (Thread.currentThread() != null)
            if (!Thread.currentThread().isInterrupted()) {
                for (int i = 0; i <= rows + 1; i++) {
                    Message message = changeDataHandler.obtainMessage();
                    message.obj = String.valueOf(i);
                    changeDataHandler.sendMessage(message);
                    try {
                        if (i == rows + 1) {
                            Thread.sleep(3000);

                        } else if (i == rows) {
                            Log.i("last row-animation", "run: ");
//                    lastRowAnimationSleep();
                            Thread.sleep(3000);
                        } else if (i == rows - 1) {
                            Thread.sleep(5000);

                        } else {
                            Thread.sleep(numberOfSeconds);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        boolean IsMainThread = changeDataHandler.getLooper() == Looper.getMainLooper();
                        Log.i("IsMainThread", IsMainThread + "");
                        return;
                    }
                }
            }

    }


    public void lastRowAnimationSleep() throws InterruptedException {
        if (SplashScreenActivity.allData != null) {
//
//            if (fragment instanceof SupportMapFragment) {
//                Thread.sleep(5000);
//            } else {
            int secondsToWait = Integer.parseInt(SplashScreenActivity.allData.getTransitionTimeInMinutes()) - (rows * (numberOfSeconds / 1000));
            Log.i("SECONDS ", secondsToWait + "");
            if (secondsToWait < 0) {
                secondsToWait = rows * (numberOfSeconds / 1000) + 5000;
            }
            Thread.sleep(secondsToWait * 1000);
//            }

        } else {
            Thread.sleep(5000);
        }
    }

//    @Override
//    protected void finalize() throws Throwable {
//        super.finalize();
//        Log.i("Finalize Called", this + " " + Thread.currentThread() + " finalized");
//        Log.i("Cores", Runtime.getRuntime().availableProcessors() + "");
//    }

}
package com.cnet.VisualAnalysis.Threads;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.google.android.gms.maps.SupportMapFragment;


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
                    Log.i("last row", "run: ");
//                    lastRowAnimationSleep();
                    Thread.sleep(5000);
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
        if (SplashScreenActivity.allData != null) {

            if (fragment instanceof SupportMapFragment) {
                Thread.sleep(5000);
            } else {
                int secondsToWait = Integer.parseInt(SplashScreenActivity.allData.getTransitionTimeInMinutes()) - (rows * (numberOfSeconds / 1000));
                Log.i("SECONDS ", secondsToWait + "");
                if (secondsToWait < 0) {
                    secondsToWait = rows * (numberOfSeconds / 1000) + 5000;
                }
                Thread.sleep(secondsToWait * 1000);
            }

        } else {
            Thread.sleep(5000);
        }
    }
}
package com.cnet.VisualAnalysis.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class BackGroundTasks extends AsyncTask<String, Void, String> {

    Context context;

    public interface CalculateInBackground {

        void onPreExecute();

        void doInBackground();

        void onPostExecute();

    }
    CalculateInBackground calculateInBackground;

    public BackGroundTasks(Context context, CalculateInBackground calculateInBackground) {
        this.context = context;
        this.calculateInBackground = calculateInBackground;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        calculateInBackground.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        calculateInBackground.doInBackground();
        return null;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        calculateInBackground.onPostExecute();
    }
}

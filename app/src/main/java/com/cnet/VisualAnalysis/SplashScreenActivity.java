package com.cnet.VisualAnalysis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.AllData;
import com.cnet.VisualAnalysis.Utils.AllDataParser;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;


public class SplashScreenActivity extends AppCompatActivity implements VolleyHttp.GetRequest {

    public static AllData allData;
    public static String myAndroidDeviceId;

    ProgressBar progressBarCircular;
    TextView loadingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBarCircular = findViewById(R.id.progressBarCircular);
        loadingTextView = findViewById(R.id.loadingTextView);

        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                VolleyHttp http = new VolleyHttp(getApplicationContext());
                http.makeGetRequest(Constants.allDataWithConfigurationURL + "?imei=" + getDeviceId(getApplicationContext()),
                        SplashScreenActivity.this);
                handler.postDelayed(this, 600000);
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {

        Log.i("TAG", "onSuccess: ");
        progressBarCircular.setVisibility(View.GONE);
        loadingTextView.setVisibility(View.GONE);

        AllDataParser allDataParser = new AllDataParser(jsonObject);

        try {
            allData = allDataParser.parseAllData();

            if (jsonObject.has("dashBoardData") && !jsonObject.isNull("dashBoardData") && jsonObject.getJSONArray("dashBoardData").length() > 0) {
                startActivity(new Intent(SplashScreenActivity.this, SecondActivity.class));
            } else if (jsonObject.has("consolidationObjectData") && !jsonObject.isNull("consolidationObjectData") && jsonObject.getJSONArray("consolidationObjectData").length() > 0) {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(VolleyError error) {
        progressBarCircular.setVisibility(View.GONE);
        loadingTextView.setText(R.string.volley_error);
        Log.i("error", error + "");
    }

    @SuppressLint("HardwareIds")
    public String getDeviceId(Context context) {
//        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        String imei = telephonyManager.getDeviceId();

//        myAndroidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        myAndroidDeviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return myAndroidDeviceId;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);

    }

}
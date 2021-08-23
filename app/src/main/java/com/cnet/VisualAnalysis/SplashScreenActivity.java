package com.cnet.VisualAnalysis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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
    public Handler handler;
    TextView loadingStatusText;
    String connFailMessage = "Connection not available, Restart the app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash_screen);
        setContentView(R.layout.splash_screen);

        loadingStatusText = findViewById(R.id.loadingStatusText);

        VolleyHttp http = new VolleyHttp(getApplicationContext());
        http.makeGetRequest(Constants.allDataWithConfigurationURL + "?imei=" + getDeviceId(getApplicationContext()),
                SplashScreenActivity.this);
//        http.makeGetRequest(Constants.allDataWithConfigurationURL + "?imei=" + "cc70a81e8233444a",
//                SplashScreenActivity.this);


       /* Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.logo_animation);
        videoView.setVideoURI(uri);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.start();
            }
        });

        */
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSuccess(JSONObject jsonObject) {


        try {
            AllDataParser allDataParser = new AllDataParser(jsonObject);
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
//        progressBarCircular.setVisibility(View.GONE);
//        loadingTextView.setText(R.string.volley_error);
        Log.i("error", error + "");
        Toast.makeText(this, "Connection not available, Restart the app", Toast.LENGTH_LONG).show();
        loadingStatusText.setText(connFailMessage);
    }

    @SuppressLint("HardwareIds")
    public String getDeviceId(Context context) {
        myAndroidDeviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return myAndroidDeviceId;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
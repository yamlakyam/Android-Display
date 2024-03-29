package com.cnet.VisualAnalysis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
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
    //
    ProgressBar progressBarCircular;
    TextView loadingTextView;
    public Handler handler;
    boolean isFirstRefresh;
    VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//        videoView = findViewById(R.id.videoView);
//        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.animation_vid;
//        Uri uri = Uri.parse(videoPath);
//        videoView.setVideoURI(uri);
//
//        videoView.start();
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//            }
//        });


//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                videoView.start();
//            }
//        });


        progressBarCircular = findViewById(R.id.progressBarCircular);
        loadingTextView = findViewById(R.id.loadingTextView);

        VolleyHttp http = new VolleyHttp(getApplicationContext());
//        http.makeGetRequest(Constants.allDataWithConfigurationURL + "?imei=" + getDeviceId(getApplicationContext()),
//                SplashScreenActivity.this);
        http.makeGetRequest(Constants.allDataWithConfigurationURL + "?imei=" + "cc70a81e8233444a",
                SplashScreenActivity.this);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
//        videoView.stopPlayback();
//        videoView.setVisibility(View.GONE);

        progressBarCircular.setVisibility(View.GONE);
        loadingTextView.setVisibility(View.GONE);
        Log.i("Splash Screen", "onSuccess: ");

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
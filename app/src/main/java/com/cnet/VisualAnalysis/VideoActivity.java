package com.cnet.VisualAnalysis;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Utils.AllDataParser;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

public class VideoActivity extends AppCompatActivity implements VolleyHttp.GetRequest {

    VideoView videoView;

    String deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Context context = VideoActivity.this;
        deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        videoView = findViewById(R.id.lastVideo);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.logo_animation);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                navigate();
            }
        });

        VolleyHttp http = new VolleyHttp(getApplicationContext());
        http.makeGetRequest(Constants.allDataWithConfigurationURL + "?imei=" + deviceID,
                VideoActivity.this);
    }

    public void navigate() {
        Intent intentToSecondActivity = new Intent(VideoActivity.this, SecondActivity.class);
        intentToSecondActivity.putExtra("prev-activity", "Video");
        startActivity(intentToSecondActivity);
    }

    public void triggerAppRestart(Context context) {
        Log.i("RESTARTED", "triggerRebirth: ");
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(VideoActivity.this, SplashScreenActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();

        Runtime runtime = Runtime.getRuntime();
        long usedMemInMB = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        long maxHeapSizeInMB = runtime.maxMemory() / 1048576L;
        long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

        if (availHeapSizeInMB < 200) {
            triggerAppRestart(getApplicationContext());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSuccess(JSONObject jsonObject) throws JSONException {

        AllDataParser allDataParser = new AllDataParser(jsonObject);
        try {
            SplashScreenActivity.allData = null;
            SplashScreenActivity.allData = allDataParser.parseAllData();
            Log.i("Refreshed_Data_Parsed", "run: ");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(VolleyError error) {

    }
}
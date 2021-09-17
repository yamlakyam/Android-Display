package com.cnet.VisualAnalysis;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

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

    }

    public void navigate() {
        Intent intentToSecondActivity = new Intent(VideoActivity.this, SecondActivity.class);
        intentToSecondActivity.putExtra("prev-activity", "Video");
        startActivity(intentToSecondActivity);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(VideoActivity.this, SplashScreenActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

}
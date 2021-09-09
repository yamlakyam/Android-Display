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
//                startActivity(new Intent(VideoActivity.this, SecondActivity.class));
                navigate();
            }
        });
    }

    public void navigate() {
        Intent intent = getIntent();
        int fragNo = intent.getIntExtra("from", 0);
        Intent intentToSecondActivity = new Intent(VideoActivity.this, SecondActivity.class);
        intentToSecondActivity.putExtra("fromVideo", fragNo);
        startActivity(intentToSecondActivity);

    }


    //        if (SplashScreenActivity.allData.getLayoutList().contains(3)) {
//            if (SplashScreenActivity.allData.getLayoutList().contains(4)) {
//                navController.navigate(R.id.summarizedByArticleParentCategFragment);
//            } else if (SplashScreenActivity.allData.getLayoutList().contains(5)) {
//                navController.navigate(R.id.summarizedByArticleChildCategFragment);
//            } else if (SplashScreenActivity.allData.getLayoutList().contains(6)) {
//                navController.navigate(R.id.summaryOfLastSixMonthsFragment);
//            } else if (SplashScreenActivity.allData.getLayoutList().contains(7)) {
//                navController.navigate(R.id.summaryOfLastMonthFragment);
//            } else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0) {
//                navController.navigate(R.id.branchSummaryFragment);
//            } else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
//                navController.navigate(R.id.userReportForAllOusFragment2);
//            } else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
//                navController.navigate(R.id.userReportForEachOusFragment);
//            } else if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
//                navController.navigate(R.id.peakHourReportForAllOusFragment);
//            } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
//                navController.navigate(R.id.peakHourReportFragment);
//            } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
////                startActivity(new Intent(requireActivity(), MapsActivity.class));
//                navController.navigate(R.id.vansOfASingleOrganizationFragment);
//            }
//
//        }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(VideoActivity.this, SplashScreenActivity.class));
    }
}
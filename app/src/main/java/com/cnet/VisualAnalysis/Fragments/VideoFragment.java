package com.cnet.VisualAnalysis.Fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SplashScreenActivity;

public class VideoFragment extends Fragment {


    VideoView videoView;
    Fragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        fragment = this;

        videoView = view.findViewById(R.id.lastAnimVideo);

        Uri uri = Uri.parse("android.resource://" + requireActivity().getPackageName() + "/" + R.raw.logo_animation);
        videoView.setVideoURI(uri);
        playVideo();


        return view;
    }

    public void playVideo() {
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                startActivity(new Intent(VideoActivity.this, SecondActivity.class));
                navigate();
            }
        });
    }

    private void navigate() {
        NavController navController = NavHostFragment.findNavController(fragment);

        if (SplashScreenActivity.allData.getLayoutList().contains(3) && SplashScreenActivity.allData.getDashBoardData().getSummarizedByArticleData() != null) {
            navController.navigate(R.id.summarizedByArticleFragment2);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(4) && SplashScreenActivity.allData.getDashBoardData().getSummarizedByParentArticleData() != null) {
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(5) && SplashScreenActivity.allData.getDashBoardData().getSummarizedByChildArticleData() != null) {
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(6) && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast6MonthsData() != null) {
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(7) && SplashScreenActivity.allData.getDashBoardData().getSummaryOfLast30DaysData() != null) {
            navController.navigate(R.id.summaryOfLastMonthFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(8) && SplashScreenActivity.allData.getDashBoardData().getBranchSummaryData().getBranchSummaryTableRows().size() > 0) {
            navController.navigate(R.id.branchSummaryFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(9) && SplashScreenActivity.allData.getDashBoardData().getUserReportForAllBranch() != null) {
            navController.navigate(R.id.userReportForAllOusFragment2);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(11) && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforAllBranch() != null) {
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(10) && SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch() != null) {
            navController.navigate(R.id.userReportForEachOusFragment);

        } else if (SplashScreenActivity.allData.getLayoutList().contains(12) && SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch() != null) {
            navController.navigate(R.id.peakHourReportFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(1) && SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans() != null) {
            navController.navigate(R.id.mapsFragment);
        } else {
            playVideo();
        }

    }
}
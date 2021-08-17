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

public class AnimationVideoFragment extends Fragment {
    Fragment fragment;
    VideoView videoView;

    public AnimationVideoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animation_video, container, false);
        fragment = this;

        NavController navController = NavHostFragment.findNavController(fragment);

        videoView = view.findViewById(R.id.videoAnim);
        Uri uri = Uri.parse("android.resource://" + requireActivity().getPackageName() + "/" + R.raw.anim_video);
        videoView.setVideoURI(uri);
        videoView.start();

        Bundle bundle = getArguments();
        int myInt = bundle.getInt("nav_from");

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (myInt == 3) {
                    navController.navigate(R.id.summarizedByArticleParentCategFragment);
                }
            }
        });

        return view;
    }
}
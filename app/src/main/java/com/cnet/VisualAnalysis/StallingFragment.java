package com.cnet.VisualAnalysis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

public class StallingFragment extends Fragment {

    FrameLayout frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stalling, container, false);
        frameLayout = view.findViewById(R.id.frameLayout);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        frameLayout = null;
    }
}
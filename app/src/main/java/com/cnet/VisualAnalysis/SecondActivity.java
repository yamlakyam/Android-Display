package com.cnet.VisualAnalysis;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;

import org.json.JSONArray;

public class SecondActivity extends AppCompatActivity   {
    public static JSONArray dashBoardArray;
    public static DashBoardData dashBoardData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {

            case KeyEvent.KEYCODE_DPAD_CENTER:
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                Log.i("TAG", "onKeyDown:up ");
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                Log.i("TAG", "onKeyDown:down ");
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                Log.i("key", "onKeyDown: left");
                leftNavigation();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Log.i("key", "onKeyDown: right");

                rightNavigation();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Fragment getCurrentFragment() {
//        Log.i("current Frag", getCurrentFragment().getClass().getName());
        Fragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
        Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);

        return fragment;
    }

    public void leftNavigation() {
        if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.SummarizedByArticleFragment")) {
            NavController navController = NavHostFragment.findNavController(getCurrentFragment());
            navController.navigate(R.id.branchSummaryFragment);
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.SummarizedByArticleParentCategFragment")) {
            NavController navController = NavHostFragment.findNavController(getCurrentFragment());
            navController.navigate(R.id.summarizedByArticleFragment2);
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.SummarizedByArticleChildCategFragment")) {
            NavController navController = NavHostFragment.findNavController(getCurrentFragment());
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.SummaryOfLastSixMonthsFragment")) {
            NavController navController = NavHostFragment.findNavController(getCurrentFragment());
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.SummaryOfLastMonthFragment")) {
            NavController navController = NavHostFragment.findNavController(getCurrentFragment());
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        }else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.BranchSummaryFragment")) {
            NavController navController = NavHostFragment.findNavController(getCurrentFragment());
            navController.navigate(R.id.summaryOfLastMonthFragment);
        }

    }

    public void rightNavigation() {
        if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.SummarizedByArticleFragment")) {
            NavController navController = NavHostFragment.findNavController(getCurrentFragment());
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.SummarizedByArticleParentCategFragment")) {
            NavController navController = NavHostFragment.findNavController(getCurrentFragment());
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.SummarizedByArticleChildCategFragment")) {
            NavController navController = NavHostFragment.findNavController(getCurrentFragment());
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        } else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.SummaryOfLastSixMonthsFragment")) {
            NavController navController = NavHostFragment.findNavController(getCurrentFragment());
            navController.navigate(R.id.summaryOfLastMonthFragment);
        }
        else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.SummaryOfLastMonthFragment")) {
            NavController navController = NavHostFragment.findNavController(getCurrentFragment());
            navController.navigate(R.id.branchSummaryFragment);
        }else if (getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.BranchSummaryFragment")) {
            NavController navController = NavHostFragment.findNavController(getCurrentFragment());
            navController.navigate(R.id.summarizedByArticleFragment2);
        }

    }

}
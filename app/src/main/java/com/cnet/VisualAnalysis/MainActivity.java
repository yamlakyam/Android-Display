package com.cnet.VisualAnalysis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.cnet.VisualAnalysis.Data.DashBoardData;
import com.cnet.VisualAnalysis.Fragments.DistributorTableFragment;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    public static JSONArray summaryTableJSONArray;
    public static JSONArray vsmCardJSONArray;
    public static JSONArray distributorTableJSONArray;
    public static JSONArray vsmTransactionJSONArray;

    NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public Fragment getCurrentFragment() {
        Fragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
        Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);

        return fragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        navController = NavHostFragment.findNavController(getCurrentFragment());

        switch(keyCode){
            case KeyEvent.KEYCODE_DPAD_CENTER:

            case KeyEvent.KEYCODE_DPAD_LEFT:
                leftNavigation();
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                rightNavigation();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void rightNavigation() {
        if(getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummaryTableFragment")){
            navController.navigate(R.id.distributorTableFragment);
        }
        else if(getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.DistributorTableFragment")){
            navController.navigate(R.id.vsmCardFragment);
        }
        else if(getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.VsmCardFragment")){
            navController.navigate(R.id.vsmTransactionFragment);
        }
        else if(getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.VsmTransactionFragment")){
            navController.navigate(R.id.summaryTableFragment);
        }

    }

    private void leftNavigation() {
        if(getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.SummaryTableFragment")){

            navController.navigate(R.id.vsmTransactionFragment);
        }
        else if(getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.DistributorTableFragment")){
            navController.navigate(R.id.summaryTableFragment);
        }
        else if(getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.VsmCardFragment")){
            navController.navigate(R.id.distributorTableFragment);
        }
        else if(getCurrentFragment().getClass().getName().equals("com.cnet.VisualAnalysis.Fragments.VsmTransactionFragment")){
            navController.navigate(R.id.vsmCardFragment);
        }
    }
}
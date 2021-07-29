package com.cnet.VisualAnalysis;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

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

}
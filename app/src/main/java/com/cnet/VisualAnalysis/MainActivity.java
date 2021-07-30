package com.cnet.VisualAnalysis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;

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

        Intent intent = getIntent();
        String name = intent.getStringExtra("back");

        if (name != null) {
            Log.i("Message", name);
            if (name.equals("pressed")) {
                if (SplashScreenActivity.allData.getLayoutList().contains(2)) {
                    setHomeFragment(R.id.vsmTransactionFragment);
                } else {
                    setHomeFragment(R.id.vsmCardFragment);
                }
            }
        }

    }

    public void setHomeFragment(int fragmentId) {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavInflater inflater = navHostFragment.getNavController().getNavInflater();
        NavGraph graph = inflater.inflate(R.navigation.main_nav);
        graph.setStartDestination(fragmentId);
        NavController navController = navHostFragment.getNavController();
        navController.setGraph(graph);
    }

}
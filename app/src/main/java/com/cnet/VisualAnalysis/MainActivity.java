package com.cnet.VisualAnalysis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;

public class MainActivity extends AppCompatActivity {

    private Context context;

    public interface keyPress {
        void centerKey(int keyCode, KeyEvent event);

        void leftKey(int keyCode, KeyEvent event);

        void rightKey(int keyCode, KeyEvent event);
    }

    keyPress keyPress;

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
                keyPress.centerKey(keyCode, event);
            case KeyEvent.KEYCODE_DPAD_LEFT:
                keyPress.leftKey(keyCode, event);
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                keyPress.rightKey(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

//    private void rightNavigation() {
//        if (getCurrentFragment() instanceof SummaryTableFragment) {
//            navController.navigate(R.id.distributorTableFragment);
//        } else if (getCurrentFragment() instanceof DistributorTableFragment) {
//            navController.navigate(R.id.vsmCardFragment);
//        } else if (getCurrentFragment() instanceof VsmCardFragment) {
//            Log.i("key", "right key from vsm card ");
//            VsmCardFragment.handleDataChangeThread.interrupt();
//            if (SplashScreenActivity.allData.getLayoutList().contains(2)) {
//                navController.navigate(R.id.vsmTransactionFragment);
//            } else if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
//                startActivity(new Intent(MainActivity.this, MapsActivity.class));
//            } else {
//                navController.navigate(R.id.summaryTableFragment);
//            }
//        } else if (getCurrentFragment() instanceof VsmTransactionFragment) {
//            if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
//                startActivity(new Intent(MainActivity.this, MapsActivity.class));
//            } else {
//                navController.navigate(R.id.summaryTableFragment);
//            }
//        }
//    }
//
//    private void leftNavigation() {
//
//        if (getCurrentFragment() instanceof SummaryTableFragment) {
//            if (SplashScreenActivity.allData.getLayoutList().contains(1)) {
//                startActivity(new Intent(MainActivity.this, MapsActivity.class));
//            } else if (SplashScreenActivity.allData.getLayoutList().contains(2)) {
//                navController.navigate(R.id.vsmTransactionFragment);
//            } else {
//                navController.navigate(R.id.vsmCardFragment);
//
//            }
//        } else if (getCurrentFragment() instanceof DistributorTableFragment) {
//            navController.navigate(R.id.summaryTableFragment);
//        } else if (getCurrentFragment() instanceof VsmCardFragment) {
//            Log.i("key", "left key from vsm card ");
//            VsmCardFragment.handleDataChangeThread.interrupt();
//            navController.navigate(R.id.distributorTableFragment);
//        } else if (getCurrentFragment() instanceof VsmTransactionFragment) {
//            navController.navigate(R.id.vsmCardFragment);
//        }
//    }


    public Fragment getCurrentFragment() {
        Fragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
        return navHostFragment.getChildFragmentManager().getFragments().get(0);
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
package com.cnet.VisualAnalysis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public MarkerThread markerThread;
    int width;

    TextView vanNameText;

    LatLng loc1 = new LatLng(9.016947, 38.764635);
    LatLng loc2 = new LatLng(9.016677, 38.766920);
    LatLng loc3 = new LatLng(9.016210, 38.770046);
    LatLng loc4 = new LatLng(9.017683, 38.770271);
    LatLng loc5 = new LatLng(9.007278, 38.776499);
    LatLng loc6 = new LatLng(9.030900, 38.848000);
    LatLng loc7 = new LatLng(9.051921, 38.738136);
    LatLng loc8 = new LatLng(9.005130, 38.696251);
    //LatLng loc6 = new LatLng(11.512322,37.402954);
    public static ArrayList<LatLng> locations = new ArrayList<LatLng>();

    public static Handler transactionsInVanHandler;
    public static Handler vanHandler;

    public static ArrayList<String> place_names = new ArrayList<>();
    public static ArrayList<String> time_list = new ArrayList<>();

    public static ArrayList<Double> grandTotal_list = new ArrayList<>();
    public static ArrayList<Integer> itemCount_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        vanNameText = findViewById(R.id.vanNameText);


        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this::onMapReady);

        }
//        DashBoardData dashBoardData = new DashBoardData();
        for (int i = 0; i < SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().size(); i++) {

//            vanNameText.setText(SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(0).nameOfVan);

            for (int j = 0; j < SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getTableRows().size(); j++) {
                double latitude = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getTableRows().get(j).getLatitude();
                double longitude = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getTableRows().get(j).getLongitude();
                LatLng loc = new LatLng(latitude, longitude);
                locations.add(loc);

                String outlate = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getTableRows().get(j).getOutlet();
                place_names.add(outlate);

                String saleTime = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getTableRows().get(j).getDateNtime();
                time_list.add(saleTime);

                double grandTotal = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getTableRows().get(j).getTotalSales();
                grandTotal_list.add(grandTotal);

                int items = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getTableRows().get(j).getItemCount();
                itemCount_list.add(items);

            }

        }

//        locations.add(loc1);
//        locations.add(loc2);
//        locations.add(loc3);
//        locations.add(loc4);
//        locations.add(loc5);
//        locations.add(loc6);
//        locations.add(loc7);
//        locations.add(loc8);
//
//        place_names.add("Van 1");
//        place_names.add("Van 2");
//        place_names.add("Van 3");
//        place_names.add("Van 4");
//        place_names.add("Van 5");
//        place_names.add("Van 6");
//        place_names.add("Van 7");
//        place_names.add("Van 8");
    }

    @Override
    protected void onStop() {
        super.onStop();
        markerThread.interrupt();
    }

    @SuppressLint("HandlerLeak")

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        transactionsInVanHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = (String) msg.obj;
                int index = Integer.parseInt(message);
                if (index == MapsActivity.locations.size()) {
//                    markerThread.interrupt();
                    place_names.clear();
                    locations.clear();
//                    startActivity(new Intent(MapsActivity.this, MainActivity.class));
                    startActivity(new Intent(MapsActivity.this, SecondActivity.class));
                } else {
                    drawMarkerWithInfo(googleMap, builder, index);

                }
            }
        };

        markerThread = new MarkerThread();
        markerThread.start();

        vanHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = Integer.parseInt(message);

                vanNameText.setText(SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(index).nameOfVan);
            }
        };

        VansThread vansThread = new VansThread();
        vansThread.start();
    }

    private void drawMarkerWithInfo(GoogleMap googleMap, LatLngBounds.Builder builder, int index) {

        LatLng loc = locations.get(index);
        MarkerOptions marker = new MarkerOptions().position(loc);
        Marker mMarker = googleMap.addMarker(marker);
        builder.include(marker.getPosition());
        LatLngBounds bounds = builder.build();
        width = getResources().getDisplayMetrics().widthPixels;
        int padding = (int) (width * 0.15); // offset from edges of the map 10% of screen
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);//to draw markers at a time including all the previous ones
//        googleMap.animateCamera(cu, 1000, null);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                View view = getLayoutInflater().inflate(R.layout.custom_pop_up, null);
                TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
                nameTextView.setText(place_names.get(index));
                TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
                timeTextView.setText(UtilityFunctionsForActivity1.timeElapsed(UtilityFunctionsForActivity1.formatTime(time_list.get(index)), Calendar.getInstance().getTime()));
                TextView grandTotalText = (TextView) view.findViewById(R.id.grandTotalText);
                grandTotalText.setText(String.valueOf(grandTotal_list.get(index)));
                TextView itemCountText = (TextView) view.findViewById(R.id.itemCountText);
                itemCountText.setText(String.valueOf(itemCount_list.get(index)));

                return view;
            }
        });
        mMarker.showInfoWindow();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        markerThread.interrupt();
//        Intent intent = new Intent(MapsActivity.this, MainActivity.class);
//        intent.putExtra("back", "pressed");
//        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        markerThread.interrupt();

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
//                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
//                intent.putExtra("back", "pressed");
//                startActivity(intent);


                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
//                startActivity(new Intent(MapsActivity.this, MainActivity.class));
                Intent intent = new Intent(MapsActivity.this, SecondActivity.class);
                startActivity(intent);
                break;
        }

        return super.onKeyDown(keyCode, event);
    }
}

class MarkerThread extends Thread {

    public MarkerThread() {
    }

    @Override
    public void run() {
        for (int i = 0; i <= MapsActivity.locations.size(); i++) {
            Log.v("MapIndex", "" + i);
            if (MapsActivity.transactionsInVanHandler != null) {
                Message msg = MapsActivity.transactionsInVanHandler.obtainMessage();
                msg.obj = "" + i;
                MapsActivity.transactionsInVanHandler.sendMessage(msg);

                if (i == MapsActivity.locations.size()) {
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}

class VansThread extends Thread {

    @Override
    public void run() {
        super.run();

        for (int i = 0; i < SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().size(); i++) {
            if (MapsActivity.vanHandler != null) {
                Message msg = MapsActivity.vanHandler.obtainMessage();
                msg.obj = "" + i;
                MapsActivity.vanHandler.sendMessage(msg);
                int salesInAvan = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).tableRows.size();
                try {
                    Thread.sleep(salesInAvan * 5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
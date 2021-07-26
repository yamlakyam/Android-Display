package com.cnet.VisualAnalysis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    int width;

    LatLng loc1 = new LatLng(9.016947, 38.764635);
    LatLng loc2 = new LatLng(9.016677, 38.766920);
    LatLng loc3 = new LatLng(9.016210, 38.770046);
    LatLng loc4 = new LatLng(9.017683, 38.770271);
    LatLng loc5 = new LatLng(9.007278, 38.776499);
    LatLng loc6 = new LatLng(9.030900, 38.848000);
    LatLng loc7 = new LatLng(9.051921, 38.738136);
    LatLng loc8 = new LatLng(9.005130, 38.696251);

    public static ArrayList<LatLng> locations = new ArrayList<LatLng>();

    public static Handler handler;

    public static ArrayList<String> place_names = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        WindowManager.LayoutParams layoutParams;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            Log.i("wm", "1");
//            layoutParams = new WindowManager.LayoutParams
//                    (200, 200, WindowManager.LayoutParams.TYPE_PHONE,
//                            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, PixelFormat.TRANSLUCENT);
//        } else {
            Log.i("wm", "2");
            layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
//        }




        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);

            View map_overlay = LayoutInflater.from(mapFragment.getContext()).inflate(R.layout.map_overlay, null, false);
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            windowManager.addView(map_overlay, layoutParams);
        }

        locations.add(loc1);
        locations.add(loc2);
        locations.add(loc3);
        locations.add(loc4);
        locations.add(loc5);
        locations.add(loc6);
        locations.add(loc7);
        locations.add(loc8);

        place_names.add("Van 1");
        place_names.add("Van 2");
        place_names.add("Van 3");
        place_names.add("Van 4");
        place_names.add("Van 5");
        place_names.add("Van 6");
        place_names.add("Van 7");
        place_names.add("Van 8");


        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                intent.putExtra("fragmentNumber", 1);
                startActivity(intent);
                finish();
            }
        }, 15000);

    }

    @SuppressLint("HandlerLeak")

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MarkerThread markerThread = new MarkerThread();
        markerThread.start();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = (String) msg.obj;
                int index = Integer.parseInt(message);
                LatLng loc = locations.get(index);

                MarkerOptions marker = new MarkerOptions().position(loc);

                Marker mMarker = googleMap.addMarker(marker);
                builder.include(marker.getPosition());
                LatLngBounds bounds = builder.build();
                width = getResources().getDisplayMetrics().widthPixels;
                int padding = (int) (width * 0.15); // offset from edges of the map 10% of screen

                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                googleMap.animateCamera(cu, 1000, null);

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
                        TextView descTextView = (TextView) view.findViewById(R.id.descTextView);
                        descTextView.setText(loc.toString());

                        return view;
                    }
                });
                mMarker.showInfoWindow();

            }
        };
    }

};

class MarkerThread extends Thread {

    //    MapActivity mapActivity = new MapActivity();
    public MarkerThread() {

    }

    @Override
    public void run() {
        for (int i = 0; i < MapsActivity.locations.size(); i++) {
            Log.v("MapIndex", "" + i);
            if (MapsActivity.handler != null) {
                Message msg = MapsActivity.handler.obtainMessage();
                msg.obj = "" + i;
                MapsActivity.handler.sendMessage(msg);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
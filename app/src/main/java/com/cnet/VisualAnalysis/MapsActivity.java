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

import com.cnet.VisualAnalysis.Data.VsmTableDataForSingleVan;
import com.cnet.VisualAnalysis.Data.VsmTransactionTableRow;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Threads.HandleVanChangeThread;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    int width;
    TextView vanNameText;
    TextView driverNameText;
    TextView parameter1;
    TextView parameter2;
    TextView parameter3;
    int currentVanIndex;
    int currentLocationIndex = 0;

    boolean mapPaused = false;
    public ArrayList<VsmTableDataForSingleVan> vansToDisplay;

    GoogleMap gmap;
    public static ArrayList<VsmTransactionTableRow> eachTransactionsInaVan;
    public static Handler animationHandler;
    public static Handler changeDataHandler;
    public HandleRowAnimationThread handleRowAnimationThread;
    public HandleVanChangeThread handleDataChangeThread;


    public ArrayList<Double> grand_total = new ArrayList<>();

    NumberFormat numberFormat = NumberFormat.getInstance();

    public SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        vanNameText = findViewById(R.id.vanNameText);
        driverNameText = findViewById(R.id.driverNameText);
        parameter1 = findViewById(R.id.parameter1);
        parameter2 = findViewById(R.id.parameter2);
        parameter3 = findViewById(R.id.parameter3);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this::onMapReady);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        if (handleDataChangeThread != null) {
            handleDataChangeThread.interrupt();
        }
    }

    @SuppressLint("HandlerLeak")

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        if (!mapPaused) {
            drawAllVansMarkers(0, googleMap, 0);
        }
    }

    private void drawMarkerWithInfo(GoogleMap googleMap, LatLngBounds.Builder builder, LatLng loc, ArrayList<VsmTransactionTableRow> vsmTransactionTableRows, int index) {
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

                String place_name = vsmTransactionTableRows.get(index).getOutlet();
                if (place_name.length() > 21) {
                    place_name = vsmTransactionTableRows.get(index).getOutlet().substring(0, 21) + "...";
                }
                nameTextView.setText(place_name);
                TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
                timeTextView.setText(new UtilityFunctionsForActivity1().timeElapsed(new UtilityFunctionsForActivity1().formatTime(vsmTransactionTableRows.get(index).getDateNtime()), Calendar.getInstance().getTime()));
                TextView grandTotalText = (TextView) view.findViewById(R.id.grandTotalText);
                grandTotalText.setText(numberFormat.format(Math.round(vsmTransactionTableRows.get(index).getTotalSales() * 100.0) / 100.0));
                TextView itemCountText = (TextView) view.findViewById(R.id.itemCountText);
                itemCountText.setText(numberFormat.format(vsmTransactionTableRows.get(index).getItemCount()));
                TextView voucherTextView = (TextView) view.findViewById(R.id.voucherTextView);
                voucherTextView.setText(vsmTransactionTableRows.get(index).getVoucherNo());
                driverNameText.setText(vsmTransactionTableRows.get(index).getUsername());
                return view;
            }
        });
        mMarker.showInfoWindow();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MapsActivity.this, SplashScreenActivity.class));
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() != KeyEvent.ACTION_UP) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_CENTER:

                    if (handleRowAnimationThread != null) {
                        handleRowAnimationThread.interrupt();
                    }
                    if (handleDataChangeThread != null) {
                        handleDataChangeThread.interrupt();
                    }
                    mapPaused = !mapPaused;
                    if (!mapPaused) {
                        currentLocationIndex = currentLocationIndex + 1;
                        drawAllVansMarkers(currentVanIndex, gmap, currentLocationIndex);
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    leftKeyMapNavigation();
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    rightKeyMapNavigation();
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void leftKeyMapNavigation() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        if (handleDataChangeThread != null) {
            handleDataChangeThread.interrupt();
        }

        if (currentVanIndex == 0) {
            Intent intent = new Intent(MapsActivity.this, SecondActivity.class);
            intent.putExtra("left", "pressed");
            startActivity(intent);
        } else {
//                drawMarkerWithInfo(mapFragment.getMap(), new LatLngBounds.Builder(), transactionIndex - 1);
            currentLocationIndex = 0;
            currentVanIndex = currentVanIndex - 1;
            drawAllVansMarkers(currentVanIndex, gmap, 0);
        }
    }

    private void rightKeyMapNavigation() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        if (handleDataChangeThread != null) {
            handleDataChangeThread.interrupt();
        }

        if (currentVanIndex == SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().size() - 1) {
            startActivity(new Intent(MapsActivity.this, SecondActivity.class));
        } else {
            currentLocationIndex = 0;
            currentVanIndex = currentVanIndex + 1;
            drawAllVansMarkers(currentVanIndex, gmap, 0);
        }

    }

    @SuppressLint("HandlerLeak")
    public void drawMarkerInVan(int vanIndex, GoogleMap googleMap, int locIndex) {
        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                    currentLocationIndex = index;
                }

                if (index == SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).getTableRows().size() - 1) {
                    currentLocationIndex = 0;
                }
                if (index == SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).getTableRows().size()) {
                    currentLocationIndex = 0;
                }
                if (index == SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).getTableRows().size() && vanIndex == SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().size() - 1) {
//                    startActivity(new Intent(MapsActivity.this, SecondActivity.class));
                    startActivity(new Intent(MapsActivity.this, VideoActivity.class));
                } else if (index < SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).getTableRows().size()) {

                    double latitude = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).tableRows.get(index).getLatitude();
                    double longitude = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).tableRows.get(index).getLongitude();
                    LatLng loc = new LatLng(latitude, longitude);

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    ArrayList<VsmTransactionTableRow> vsmTransactionTableRows = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).tableRows;
                    drawMarkerWithInfo(googleMap, builder, loc, vsmTransactionTableRows, index);

                }
            }
        };
        handleRowAnimationThread = new HandleRowAnimationThread(SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).getTableRows().size(),
                animationHandler, 1000, mapFragment, locIndex);
        handleRowAnimationThread.start();
    }

    @SuppressLint("HandlerLeak")
    public void drawAllVansMarkers(int startingIndex, GoogleMap googleMap, int locIndex) {

        changeDataHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                    currentVanIndex = index;
                }
                vansToDisplay = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData();

                try {
                    Collections.sort(vansToDisplay, new Comparator<VsmTableDataForSingleVan>() {
                        @Override
                        public int compare(VsmTableDataForSingleVan o1, VsmTableDataForSingleVan o2) {
                            return Integer.parseInt(o1.nameOfVan.substring(3)) < Integer.parseInt(o2.nameOfVan.substring(3)) ? -1 : 0;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                drawMarkerInVan(index, googleMap, locIndex);
                vanNameText.setText(vansToDisplay.get(index).nameOfVan);
                parameter1.setText(numberFormat.format(vansToDisplay.get(index).salesOutLateCount));
                parameter2.setText(numberFormat.format(vansToDisplay.get(index).allLineItemCount));
                parameter3.setText(numberFormat.format(Math.round(vansToDisplay.get(index).totalPrice * 100.0) / 100.0));

                Log.i("currentloc", currentLocationIndex + "");
                if (currentLocationIndex == 0) {
                    googleMap.clear();
                }
            }
        };
        ArrayList<VsmTableDataForSingleVan> vsmTableDatasForSingleVan = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData();
        handleDataChangeThread = new HandleVanChangeThread(changeDataHandler, SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().size(), vsmTableDatasForSingleVan, startingIndex);
        handleDataChangeThread.start();

    }
}

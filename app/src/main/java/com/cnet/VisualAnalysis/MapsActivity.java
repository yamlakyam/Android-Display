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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    int width;
    TextView vanNameText;
    TextView parameter1;
    TextView parameter2;
    TextView parameter3;
    int currentVanIndex;

    boolean mapPaused = false;

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
        parameter1 = findViewById(R.id.parameter1);
        parameter2 = findViewById(R.id.parameter2);
        parameter3 = findViewById(R.id.parameter3);


        mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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
//        addLocations();
        gmap = googleMap;
        if (!mapPaused) {
            Log.i("map", "not paused");
//            drawMap(googleMap);
            drawAllVansMarkers(0, googleMap);
        }

    }

    //    private void drawMarkerWithInfo(GoogleMap googleMap, LatLngBounds.Builder builder, int index) {
    private void drawMarkerWithInfo(GoogleMap googleMap, LatLngBounds.Builder builder, LatLng loc, ArrayList<VsmTransactionTableRow> vsmTransactionTableRows, int index) {
//        LatLng loc = locations.get(index);
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
                timeTextView.setText(UtilityFunctionsForActivity1.timeElapsed(UtilityFunctionsForActivity1.formatTime(vsmTransactionTableRows.get(index).getDateNtime()), Calendar.getInstance().getTime()));
                TextView grandTotalText = (TextView) view.findViewById(R.id.grandTotalText);
                grandTotalText.setText(numberFormat.format(Math.round(vsmTransactionTableRows.get(index).getTotalSales() * 100.0) / 100.0));
                TextView itemCountText = (TextView) view.findViewById(R.id.itemCountText);
                itemCountText.setText(numberFormat.format(vsmTransactionTableRows.get(index).getItemCount()));
                TextView voucherTextView = (TextView) view.findViewById(R.id.voucherTextView);
                voucherTextView.setText(vsmTransactionTableRows.get(index).getVoucherNo());
                return view;
            }
        });
        mMarker.showInfoWindow();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() != KeyEvent.ACTION_UP) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                    Log.i("center", "onKeyDown: ");

                    if (handleRowAnimationThread != null) {
                        handleRowAnimationThread.interrupt();
                    }
                    if (handleDataChangeThread != null) {
                        handleDataChangeThread.interrupt();
                    }
                    mapPaused = !mapPaused;

                    if (!mapPaused) {
                        drawAllVansMarkers(currentVanIndex, gmap);
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
            currentVanIndex = currentVanIndex - 1;
            drawAllVansMarkers(currentVanIndex, gmap);
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
            currentVanIndex = currentVanIndex + 1;
            drawAllVansMarkers(currentVanIndex, gmap);
        }

    }

    @SuppressLint("HandlerLeak")
    public void drawMarkerInVan(int vanIndex, GoogleMap googleMap) {


        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {

                Log.i("TAG", vanIndex + "");
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }

                if (index == SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).tableRows.size() + 1) {

                } else if (index == SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).getTableRows().size() && vanIndex == SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().size() - 1) {
                    startActivity(new Intent(MapsActivity.this, SecondActivity.class));

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
                animationHandler, 1000, mapFragment, 0);
        handleRowAnimationThread.start();
    }

    @SuppressLint("HandlerLeak")
    public void drawAllVansMarkers(int startingIndex, GoogleMap googleMap) {
        googleMap.clear();
        changeDataHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                    currentVanIndex = index;
//                    distributorIndex = index;
                }
//                inflateTable(index);
//                setDistributorHeader(index);
                drawMarkerInVan(index, googleMap);
                vanNameText.setText(SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(index).nameOfVan);
                parameter1.setText(numberFormat.format(SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(index).salesOutLateCount));
                parameter2.setText(numberFormat.format(SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(index).allLineItemCount));
                parameter3.setText(numberFormat.format(Math.round(SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(index).totalPrice * 100.0) / 100.0));
            }
        };

        ArrayList<VsmTableDataForSingleVan> vsmTableDatasForSingleVan = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData();
        handleDataChangeThread = new HandleVanChangeThread(changeDataHandler, SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().size(), vsmTableDatasForSingleVan, startingIndex);
        handleDataChangeThread.start();

    }
}

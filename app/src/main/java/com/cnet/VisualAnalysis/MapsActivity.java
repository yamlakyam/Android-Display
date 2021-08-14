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

import com.cnet.VisualAnalysis.Data.VsmTransactionTableRow;
import com.cnet.VisualAnalysis.Fragments.UserReportForEachOuFragment;
import com.cnet.VisualAnalysis.Threads.HandleDataChangeThread;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
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

    public MarkerThread markerThread;
    int width;
    TextView vanNameText;
    TextView parameter1;
    TextView parameter2;
    TextView parameter3;
    int transactionIndex;

    boolean mapPaused = false;

    LatLng loc1 = new LatLng(9.016947, 38.764635);
    LatLng loc2 = new LatLng(9.016677, 38.766920);
    LatLng loc3 = new LatLng(9.016210, 38.770046);
    LatLng loc4 = new LatLng(9.017683, 38.770271);
    LatLng loc5 = new LatLng(9.007278, 38.776499);
    LatLng loc6 = new LatLng(9.030900, 38.848000);
    LatLng loc7 = new LatLng(9.051921, 38.738136);
    LatLng loc8 = new LatLng(9.005130, 38.696251);
    //LatLng loc6 = new LatLng(11.512322,37.402954);

    public static ArrayList<VsmTransactionTableRow> eachTransactionsInaVan;
    public static Handler animationHandler;
    public static Handler changeDataHandler;
    public HandleRowAnimationThread handleRowAnimationThread;
    public HandleDataChangeThread handleDataChangeThread;


    public static ArrayList<LatLng> locations = new ArrayList<LatLng>();


    public static Handler transactionsInVanHandler;
    public static Handler vanHandler;


    public ArrayList<String> place_names = new ArrayList<>();
    public ArrayList<String> time_list = new ArrayList<>();

    public ArrayList<Double> grandTotal_list = new ArrayList<>();
    public ArrayList<Integer> itemCount_list = new ArrayList<>();

    public ArrayList<String> currentVan_list = new ArrayList<>();
    public ArrayList<String> last_active = new ArrayList<>();
    public ArrayList<Integer> sales_outlet_count = new ArrayList<>();
    public ArrayList<Integer> line_item_count = new ArrayList<>();
    public ArrayList<Double> grand_total = new ArrayList<>();
    public ArrayList<String> voucher_No = new ArrayList<>();

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

//        addLocations();

//        locations.add(loc1);
//        locations.add(loc2);

//        place_names.add("Van 1");
//        place_names.add("Van 2");

    }

    private void addLocations() {
        if (SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor() != null) {
            for (int i = 0; i < SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().size(); i++) {

                for (int j = 0; j < SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getTableRows().size(); j++) {
                    double latitude = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getTableRows().get(j).getLatitude();
                    double longitude = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getTableRows().get(j).getLongitude();
                    LatLng loc = new LatLng(latitude, longitude);
                    locations.add(loc);

                    String outlate = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getTableRows().get(j).getOutlet();
                    place_names.add(outlate);

                    String saleTime = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getTableRows().get(j).getDateNtime();
                    time_list.add(saleTime);

                    double grandTotalEach = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getTableRows().get(j).getTotalSales();
                    grandTotal_list.add(grandTotalEach);

                    int items = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getTableRows().get(j).getItemCount();
                    itemCount_list.add(items);

                    String currentVan = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getNameOfVan();
                    currentVan_list.add(currentVan);

                    String lastActive = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getLastActive();
                    last_active.add(lastActive);

                    int outletCount = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getSalesOutLateCount();
                    sales_outlet_count.add(outletCount);

                    int lineItemCount = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getAllLineItemCount();
                    line_item_count.add(lineItemCount);

                    Double grandTotal = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getTotalPrice();
                    grand_total.add(grandTotal);

                    String voucherNo = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).getTableRows().get(j).getVoucherNo();
                    voucher_No.add(voucherNo);
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (markerThread != null) {
            markerThread.interrupt();
        }
        place_names.clear();
        locations.clear();
    }

    @SuppressLint("HandlerLeak")

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        addLocations();

        if (!mapPaused) {
            Log.i("map", "not paused");
//            drawMap(googleMap);
            drawAllVansMarkers(0, googleMap);
        }

    }

    @SuppressLint("HandlerLeak")
    private void drawMap(GoogleMap googleMap) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        transactionsInVanHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = (String) msg.obj;
                int index = Integer.parseInt(message);

                transactionIndex = index;
//                startingTransactionIndex=transactionIndex;

//                Log.i("all locations", locations.size() + "");

                if (index == MapsActivity.locations.size() - 1) {
//                    markerThread.interrupt();
                    place_names.clear();
                    locations.clear();
                    startActivity(new Intent(MapsActivity.this, SecondActivity.class));
                } else if (index < MapsActivity.locations.size() - 1) {
//                    drawMarkerWithInfo(googleMap, builder, index);
                    vanNameText.setText(currentVan_list.get(index));
                    parameter1.setText(numberFormat.format(sales_outlet_count.get(index)));
                    parameter2.setText(numberFormat.format(line_item_count.get(index)));
                    parameter3.setText(numberFormat.format(Math.round(grand_total.get(index) * 100.0) / 100.0));
                }
            }
        };

        markerThread = new MarkerThread(transactionIndex);
        markerThread.start();


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
//                String place_name = place_names.get(index);
                String place_name = vsmTransactionTableRows.get(index).getOutlet();
                if (place_name.length() > 21) {
                    place_name = place_names.get(index).substring(0, 21) + "...";
                }
                nameTextView.setText(place_name);
                TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
//                timeTextView.setText(UtilityFunctionsForActivity1.timeElapsed(UtilityFunctionsForActivity1.formatTime(time_list.get(index)), Calendar.getInstance().getTime()));
                timeTextView.setText(UtilityFunctionsForActivity1.timeElapsed(UtilityFunctionsForActivity1.formatTime(vsmTransactionTableRows.get(index).getDateNtime()), Calendar.getInstance().getTime()));
                TextView grandTotalText = (TextView) view.findViewById(R.id.grandTotalText);
//                grandTotalText.setText(numberFormat.format(Math.round(grandTotal_list.get(index) * 100.0) / 100.0));
                grandTotalText.setText(numberFormat.format(Math.round(vsmTransactionTableRows.get(index).getTotalSales() * 100.0) / 100.0));
                TextView itemCountText = (TextView) view.findViewById(R.id.itemCountText);
//                itemCountText.setText(numberFormat.format(itemCount_list.get(index)));
                itemCountText.setText(numberFormat.format(vsmTransactionTableRows.get(index).getItemCount()));
                TextView voucherTextView = (TextView) view.findViewById(R.id.voucherTextView);
//                voucherTextView.setText(voucher_No.get(index));
                voucherTextView.setText(vsmTransactionTableRows.get(index).getVoucherNo());
                return view;
            }
        });
        mMarker.showInfoWindow();
    }

    private void clearMarker(GoogleMap googleMap, int index) {

        LatLng loc = locations.get(index);
        MarkerOptions marker = new MarkerOptions().position(loc);
        Marker mMarker = googleMap.addMarker(marker);
        mMarker.remove();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (markerThread != null) {
            markerThread.interrupt();
        }

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        Log.i("TAG", event.getKeyCode() + "");
        Log.i("TAG", event.getAction() + "");

        if (event.getAction() != KeyEvent.ACTION_UP) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                    Log.i("center", "onKeyDown: ");

                    markerThread.interrupt();
                    mapPaused = !mapPaused;

                    if (mapPaused == false) {
                        drawMap(mapFragment.getMap());
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
        if (markerThread != null) {
            markerThread.interrupt();
        }

        Intent intent = new Intent(MapsActivity.this, SecondActivity.class);
        intent.putExtra("left", "pressed");
        startActivity(intent);


//        if (transactionIndex == 0) {
//            Intent intent = new Intent(MapsActivity.this, SecondActivity.class);
//            intent.putExtra("left", "pressed");
//            startActivity(intent);
//        } else {
////                drawMarkerWithInfo(mapFragment.getMap(), new LatLngBounds.Builder(), transactionIndex - 1);
//            transactionIndex = transactionIndex - 1;
//            drawMap(mapFragment.getMap());
//        }

    }

    private void rightKeyMapNavigation() {
        if (markerThread != null) {
            markerThread.interrupt();
        }

        startActivity(new Intent(MapsActivity.this, SecondActivity.class));

//        if (transactionIndex == locations.size() - 1) {
//            startActivity(new Intent(MapsActivity.this, SecondActivity.class));
//        } else {
//            transactionIndex = transactionIndex + 1;
//            drawMap(mapFragment.getMap());
//        }

    }

    @SuppressLint("HandlerLeak")
    public void drawMarkerInVan(int vanIndex, GoogleMap googleMap) {

//        if (userReportTableLayout != null) {
//            userReportTableLayout.removeAllViews();
//        }
        eachTransactionsInaVan = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).getTableRows();

        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                }

                if (index == eachTransactionsInaVan.size()) {
//                    drawLastRow();
//                    UtilityFunctionsForActivity1.scrollRows(userReportScrollView);
                } else if (index == eachTransactionsInaVan.size() + 1 && vanIndex == SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().size() - 1) {

//                    if (userReportForEachPaused) {
//                        handleRowAnimationThread.interrupt();
//                    } else {
//                        navigate(fragment);
//                    }

                } else if (index < eachTransactionsInaVan.size()) {
//                    grandTotalSum = grandTotalSum + tablesToDisplay.get(index).grandTotal;
//                    UtilityFunctionsForActivity1.drawUserReportForEachOu(tablesToDisplay, getContext(), userReportTableLayout, index);
//                    UtilityFunctionsForActivity1.scrollRows(userReportScrollView);
                    double latitude = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).tableRows.get(index).getLatitude();
                    double longitude = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).tableRows.get(index).getLongitude();
                    LatLng loc = new LatLng(latitude, longitude);

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    ArrayList<VsmTransactionTableRow> vsmTransactionTableRows = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).tableRows;
                    drawMarkerWithInfo(googleMap, builder, loc, vsmTransactionTableRows, index);

                }
            }
        };
        handleRowAnimationThread = new HandleRowAnimationThread(eachTransactionsInaVan.size(), UserReportForEachOuFragment.animationHandler, 200, mapFragment, 0);
        handleRowAnimationThread.start();
    }

    @SuppressLint("HandlerLeak")
    public void drawAllVansMarkers(int startingIndex, GoogleMap googleMap) {
        changeDataHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
//                    distributorIndex = index;
                }
//                inflateTable(index);
//                setDistributorHeader(index);
                drawMarkerInVan(index, googleMap);
                vanNameText.setText(SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(index).nameOfVan);
                parameter1.setText(SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(index).salesOutLateCount);
                parameter2.setText(SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(index).allLineItemCount);
                parameter3.setText(numberFormat.format(Math.round(SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(index).totalPrice * 100.0) / 100.0));
            }
        };

        float secondsToWait = Float.parseFloat(SplashScreenActivity.allData.getTransitionTimeInMinutes());
        handleDataChangeThread = new HandleDataChangeThread(changeDataHandler, SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().size(), (int) secondsToWait, startingIndex);
        handleDataChangeThread.start();

    }
}


class MarkerThread extends Thread {

    int startingTransactionIndex;

    public MarkerThread(int startingTransactionIndex) {
        this.startingTransactionIndex = startingTransactionIndex;
    }

    @Override
    public void run() {
        for (int i = startingTransactionIndex; i <= MapsActivity.locations.size(); i++) {
//            Log.v("MapIndex", "" + i);
            Handler transactionsInVanHandler = MapsActivity.transactionsInVanHandler;
//            Handler transactionsInVanHandler = new MapsActivity().transactionsInVanHandler;
            if (transactionsInVanHandler != null) {
                Message msg = transactionsInVanHandler.obtainMessage();
                msg.obj = "" + i;
                transactionsInVanHandler.sendMessage(msg);

                if (i == MapsActivity.locations.size()) {
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}

class VansThread extends Thread {

    int startingVanIndex;

    VansThread(int startingVanIndex) {
        this.startingVanIndex = startingVanIndex;
    }

    @Override
    public void run() {
        super.run();

        for (int i = startingVanIndex; i < SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().size(); i++) {
            if (MapsActivity.vanHandler != null) {
                Message msg = MapsActivity.vanHandler.obtainMessage();
                msg.obj = "" + i;
                MapsActivity.vanHandler.sendMessage(msg);

                int salesInAvan = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(i).tableRows.size();
                try {
                    Thread.sleep(salesInAvan * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
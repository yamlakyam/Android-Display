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

import com.cnet.VisualAnalysis.Data.VoucherData;
import com.cnet.VisualAnalysis.Data.VoucherDataForVan;
import com.cnet.VisualAnalysis.Data.VsmTransactionTableRow;
import com.cnet.VisualAnalysis.Threads.MarkerDrawingThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
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
import java.util.Set;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView vanNameText;
    TextView driverNameText;
    TextView driverStatusText;
    TextView parameter1;
    TextView parameter2;
    TextView parameter3;

    public static boolean mapPaused;
    public ArrayList<VoucherDataForVan> vansToDisplay;

    GoogleMap gmap;
    public static ArrayList<VsmTransactionTableRow> eachTransactionsInaVan;
    public static Handler animationHandler;
    public MarkerDrawingThread handleRowAnimationThread;

    public ArrayList<Double> grand_total = new ArrayList<>();
    ArrayList<Integer> layoutList = new ArrayList<>();
    ArrayList<VoucherDataForVan> voucherDataForVans = new ArrayList<>();

    NumberFormat numberFormat = NumberFormat.getInstance();

    public SupportMapFragment mapFragment;

    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    int padding, width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        vanNameText = findViewById(R.id.vanNameText);
        driverNameText = findViewById(R.id.driverNameText);
        driverStatusText = findViewById(R.id.driverStatusText);
        parameter1 = findViewById(R.id.parameter1);
        parameter2 = findViewById(R.id.parameter2);
        parameter3 = findViewById(R.id.parameter3);

        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;
        padding = (int) (width * 0.15); // offset from edges of the map 10% of screen

        layoutList = SplashScreenActivity.allData.getLayoutList();
        voucherDataForVans = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this::onMapReady);
        }

        if (!SecondActivity.pausedstate()) {
            mapPaused = false;
        } else {
            mapPaused = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
    }

    @SuppressLint("HandlerLeak")

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        drawAvailableReportFromMap(googleMap);
    }

    private void drawAvailableReportFromMap(GoogleMap googleMap) {
        if (SecondActivity.vanIndex < SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size()) {
            if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
                Log.i("drawAll called", "drawAvailableReportFromMap: ");
                drawAll(SecondActivity.vanIndex, googleMap);

            } else if (layoutList.contains(10)) {
                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                Log.i("navigate", "to user report");
                navigateToUserReportFromMap();

            } else if (layoutList.contains(12)) {
                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                Log.i("navigate", "to peakHr report");
                navigateToPeakHourFromMap();
            } else {
                SecondActivity.vanIndex++;
                if (SecondActivity.vanIndex < voucherDataForVans.size()) {

                    Log.i("interrupted on -", "drawAvailableReportFromMap: ");
                    interruptThread();
                    drawAvailableReportFromMap(googleMap);
                } else {
                    SecondActivity.vanIndex = 0;
                    Log.i("interrupted on --", "drawAvailableReportFromMap: ");
                    interruptThread();
                    startActivity(new Intent(MapsActivity.this, VideoActivity.class));
                }
            }
        } else {
            SecondActivity.vanIndex = 0;

            Log.i("interrupted on ---", "drawAvailableReportFromMap: ");
            interruptThread();

            startActivity(new Intent(MapsActivity.this, VideoActivity.class));
        }
    }

    public void drawAll(int vanIndex, GoogleMap googleMap) {
        if (voucherDataForVans != null) {

            drawMarkerInVan(vanIndex, googleMap);
            drawLeftPane(vanIndex);
        }
    }

    private void drawMarkerWithInfo(GoogleMap googleMap, LatLng loc, ArrayList<VoucherData> vsmTransactionTableRows, int index) {
        MarkerOptions marker = new MarkerOptions().position(loc);
        Marker mMarker = googleMap.addMarker(marker);
        builder.include(marker.getPosition());
        LatLngBounds bounds = builder.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);//to draw markers at a time including all the previous ones

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

                String place_name = vsmTransactionTableRows.get(index).getOutlates();
                if (place_name.length() > 21) {
                    place_name = vsmTransactionTableRows.get(index).getOutlates().substring(0, 21) + "...";
                }
                nameTextView.setText(place_name);
                TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
                timeTextView.setText(new UtilityFunctionsForActivity1().timeElapsed(
                        new UtilityFunctionsForActivity1().formatTime(vsmTransactionTableRows.get(index).getDateAndTime()), Calendar.getInstance().getTime()));
                TextView grandTotalText = (TextView) view.findViewById(R.id.grandTotalText);
                grandTotalText.setText(UtilityFunctionsForActivity2.decimalFormat.format(vsmTransactionTableRows.get(index).getGrandTotal()));
                TextView itemCountText = (TextView) view.findViewById(R.id.itemCountText);
                itemCountText.setText(numberFormat.format(vsmTransactionTableRows.get(index).getItemCount()));
                TextView voucherTextView = (TextView) view.findViewById(R.id.voucherTextView);
                voucherTextView.setText(vsmTransactionTableRows.get(index).getVoucherNo());
                driverNameText.setText(vsmTransactionTableRows.get(index).getUsername());
                justLogsAboutThreads();

                return view;
            }

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
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

                    mapPaused = !mapPaused;
                    if (!mapPaused) {
                        SecondActivity.playAll();
                        SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                        navigateToNextReport();
                    } else {
                        SecondActivity.pauseAll();
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    Log.i("interrupted on", "leftKey: ");
                    interruptThread();
                    navigateToPreviousReport();
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    Log.i("Interrupted on", "rightKey: ");
                    interruptThread();
                    SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                    navigateToNextReport();
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @SuppressLint("HandlerLeak")
    public void drawMarkerInVan(int vanIndex, GoogleMap googleMap) {
        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (handleRowAnimationThread.isAlive()) {
                    String message = (String) msg.obj;
                    int index = 0;
                    if (message != null) {
                        index = Integer.parseInt(message);
                    }
                    if (index == SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size()) {
                        if (builder != null) {
                            LatLngBounds bounds = builder.build();
                            gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
                            Log.i("ZOOM OUT", "handleMessage: ");
                        }
                    }
                    if (index == SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size() &&
                            vanIndex == SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size() + 1) {
                        Log.i("LAST-VAN LAST-VOUCHER", "handleMessage: ");

                        if (mapPaused) {
                            if (handleRowAnimationThread != null) {
                                Log.i("PAUSED LAST VAN VOUCHER", "handleMessage: ");
                                handleRowAnimationThread.interrupt();
                            }
                        } else {
                            Log.i("NAVIGATING TO ANIMATION", "handleMessage: ");
                            SecondActivity.vanIndex = 0;

                            interruptThread();
                            startActivity(new Intent(MapsActivity.this, VideoActivity.class));
                        }

                    } else if (index == SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size() + 1) {

                        Log.i("NAVIGATING 2 NXT REPORT", "handleMessage: ");
                        if (mapPaused) {
                            Log.i("interrupted on", "map paused");
                            interruptThread();

                        } else {
                            Log.i("NAVIGATE_TO_NEXT_REP", "handleMessage: ");
                            SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                            if (gmap != null) {
                                gmap.clear();
                            }
                            navigateToNextReport();
                        }

                    } else if (index < SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size()) {

                        double latitude = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.get(index).getLatitude();
                        double longitude = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.get(index).getLongitude();

                        Log.i("LATLNG", latitude + "," + longitude);
                        LatLng loc = new LatLng(latitude, longitude);


                        if (builder != null) {
                            builder.include(loc);
                        }
                        ArrayList<VoucherData> vsmTransactionTableRows = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList;


                        drawMarkerWithInfo(googleMap, loc, vsmTransactionTableRows, index);
                    }
                }
            }
        };
        handleRowAnimationThread = new MarkerDrawingThread(SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size(), animationHandler, 1000);
        handleRowAnimationThread.start();
    }

    public void drawLeftPane(int vanIndex) {
        if (SplashScreenActivity.allData != null) {
            vansToDisplay = voucherDataForVans;
        }

        vanNameText.setText(vansToDisplay.get(vanIndex).nameOfVan);
        parameter1.setText(numberFormat.format(vansToDisplay.get(vanIndex).transactionCount));
        parameter2.setText(numberFormat.format(vansToDisplay.get(vanIndex).lineItems));
        parameter3.setText(UtilityFunctionsForActivity2.decimalFormat.format(vansToDisplay.get(vanIndex).grandTotal));
        int lastIndexInaVan = vansToDisplay.get(vanIndex).voucherDataArrayList.size() - 1;
        driverStatusText.setText(new UtilityFunctionsForActivity1().timeElapsed(
                new UtilityFunctionsForActivity1().formatTime(vansToDisplay.get(vanIndex).voucherDataArrayList.get(lastIndexInaVan).dateAndTime), Calendar.getInstance().getTime()));

    }

    private void navigateToUserReportFromMap() {

        Log.i("interrupted on", "navigateToUserReportFromMap: ");
        interruptThread();

        if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size() > 0) {
            if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(SecondActivity.vanIndex).userReportTableRowArrayList.size() > 0) {
                navigatePoppingcurrentFragment(R.id.userReportForEachOusFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
                    navigatePoppingcurrentFragment(R.id.peakHourReportFragment);
//                    navController.navigate(R.id.peakHourReportFragment);
                } else {
                    SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                    drawAvailableReportFromMap(gmap);
                }
            } else {
                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                drawAvailableReportFromMap(gmap);
            }
        }
    }

    public void interruptThread() {
        if (handleRowAnimationThread != null) {

            handleRowAnimationThread.interrupt();
            Log.i("INTERRUPTED--", "interruptThread: ");
        }

    }

    public void navigatePoppingcurrentFragment(int fragmentId) {

        Intent intent = new Intent(MapsActivity.this, SecondActivity.class);
        intent.putExtra("From Map", fragmentId);
        startActivity(intent);

    }

    private void navigateToPeakHourFromMap() {

        Log.i("interrupted on", "navigateToPeakHourFromMap: ");
        interruptThread();

//        navController = NavHostFragment.findNavController(mapFragment);
        if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size() > 0) {
            if (SecondActivity.vanIndex < SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size()) {
                if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
                    navigatePoppingcurrentFragment(R.id.peakHourReportFragment);
                } else {
                    SecondActivity.vanIndex = SecondActivity.vanIndex + 1;

                    drawAvailableReportFromMap(gmap);
                }
            } else {
                SecondActivity.vanIndex = 0;
                startActivity(new Intent(MapsActivity.this, VideoActivity.class));
            }
        }
    }

    public void navigateToNextReport() {

        Log.i("interrupted on", "navigateToNextReport: ");
        interruptThread();

        if (handleRowAnimationThread == null) {
            Log.i("NULL AnimationThread", "navigateToNextReport: ");
        }

        if (SecondActivity.vanIndex < SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size()) {


            if (layoutList.contains(10)) {
                if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(SecondActivity.vanIndex).userReportTableRowArrayList.size() > 0) {
                    Log.i("TO USER REPORT", "navigateToNextReport: ");
                    navigatePoppingcurrentFragment(R.id.userReportForEachOusFragment);
                } else if (layoutList.contains(12)) {

                    Log.i("LAYOUTLIST CONTAINS 12", "navigateToNextReport: ");
                    if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
                        Log.i("NAV_TO_PEAK_HR", "navigateToNextReport: ");
                        navigatePoppingcurrentFragment(R.id.peakHourReportFragment);
                    } else if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {

                        Log.i("REDRAW MAP", "navigateToNextReport: ");
                        drawAvailableReportFromMap(gmap);
                    } else {
                        SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                        Log.i("NAVIGATE TO NEXT REP", "navigateToNextReport: ");
                        navigateToNextReport();
                    }
                } else {

                    if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
                        Log.i("drawAvailable-1", "navigateToNextReport: ");
                        drawAvailableReportFromMap(gmap);
                    } else {
                        Log.i("navigate2NxtRprt Called", "navigateToNextReport: ");
                        SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                        navigateToNextReport();
                    }
                }
            } else if (layoutList.contains(12)) {


                if (SecondActivity.vanIndex < SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size()) {
                    if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
                        Log.i("GO TO", "12");
                        navigatePoppingcurrentFragment(R.id.peakHourReportFragment);

                    } else if (SecondActivity.vanIndex < SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size()) {
                        drawAvailableReportFromMap(gmap);

                    } else {
                        SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                        navigateToNextReport();
                    }
                } else {
                    SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                    navigateToNextReport();
                }
            } else {
                drawAvailableReportFromMap(gmap);

            }
        } else {
            SecondActivity.vanIndex = 0;

            try {
                startActivity(new Intent(MapsActivity.this, VideoActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void navigateToPreviousReport() {
//        if (handleRowAnimationThread != null) {
//            handleRowAnimationThread.interrupt();
//        }
        Log.i("interrupted on", "navigateToPreviousReport: ");
        interruptThread();

        if (layoutList.contains(12)) {
            if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {

                navigatePoppingcurrentFragment(R.id.peakHourReportFragment);

            } else if (layoutList.contains(10)) {
                if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(SecondActivity.vanIndex).userReportTableRowArrayList.size() > 0) {
//                    navController.navigate(R.id.userReportForEachOusFragment);
                    navigatePoppingcurrentFragment(R.id.userReportForEachOusFragment);

                } else {
                    SecondActivity.vanIndex = SecondActivity.vanIndex - 1;
                    if (SecondActivity.vanIndex >= 0) {
                        if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
//                            interruptThread();
                            drawAvailableReportFromMap(gmap);
                        } else {
                            navigateToPreviousReport();
                        }
                    } else {
                        SecondActivity.vanIndex = 0;
                        navigateToLeftFragments();
                    }
                }
            } else {
                SecondActivity.vanIndex = SecondActivity.vanIndex - 1;
                if (SecondActivity.vanIndex >= 0) {
                    if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
//                        interruptThread();
                        drawAvailableReportFromMap(gmap);
                    } else {
                        navigateToPreviousReport();
                    }
                } else {
                    SecondActivity.vanIndex = 0;
                    navigateToLeftFragments();
                }
            }


        } else if (layoutList.contains(10)) {
            if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(SecondActivity.vanIndex).userReportTableRowArrayList.size() > 0) {
//                navController.navigate(R.id.userReportForEachOusFragment);
                navigatePoppingcurrentFragment(R.id.userReportForEachOusFragment);

            } else {
                SecondActivity.vanIndex = SecondActivity.vanIndex - 1;
                if (SecondActivity.vanIndex >= 0) {
                    if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
//                        interruptThread();
                        drawAvailableReportFromMap(gmap);
                    } else {
                        navigateToPreviousReport();
                    }
                } else {
                    SecondActivity.vanIndex = 0;
                    navigateToLeftFragments();
                }
            }

        } else {
            SecondActivity.vanIndex = SecondActivity.vanIndex - 1;
            if (SecondActivity.vanIndex >= 0) {
                if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
//                    interruptThread();
                    drawAvailableReportFromMap(gmap);
                } else {
                    navigateToPreviousReport();
                }
            } else {
                SecondActivity.vanIndex = 0;
                navigateToLeftFragments();
            }
        }
    }

    public void navigateToLeftFragments() {

//        if (handleRowAnimationThread != null) {
////            handleRowAnimationThread.interrupt();
//        }
        Log.i("interrupted on", "navigateToLeftFragments: ");
        interruptThread();

        if (layoutList.contains(11)) {
            navigatePoppingcurrentFragment(R.id.peakHourReportForAllOusFragment);
//            navController.navigate(R.id.peakHourReportForAllOusFragment);

        } else if (layoutList.contains(9)) {
            navigatePoppingcurrentFragment(R.id.userReportForAllOusFragment2);
//            navController.navigate(R.id.userReportForAllOusFragment2);

        } else if (layoutList.contains(8)) {
            navigatePoppingcurrentFragment(R.id.branchSummaryFragment);
//            navController.navigate(R.id.branchSummaryFragment);

        } else if (layoutList.contains(7)) {
            navigatePoppingcurrentFragment(R.id.summaryOfLastMonthFragment);
//            navController.navigate(R.id.summaryOfLastMonthFragment);

        } else if (layoutList.contains(6)) {
            navigatePoppingcurrentFragment(R.id.summaryOfLastSixMonthsFragment);
//            navController.navigate(R.id.summaryOfLastSixMonthsFragment);

        } else if (layoutList.contains(5)) {
            navigatePoppingcurrentFragment(R.id.summarizedByArticleChildCategFragment);
//            navController.navigate(R.id.summarizedByArticleChildCategFragment);

        } else if (layoutList.contains(4)) {
            navigatePoppingcurrentFragment(R.id.summarizedByArticleParentCategFragment);
//            navController.navigate(R.id.summarizedByArticleParentCategFragment);

        } else if (layoutList.contains(3)) {
            navigatePoppingcurrentFragment(R.id.summarizedByArticleFragment2);
//            navController.navigate(R.id.summarizedByArticleFragment2);
        } else {
            drawAvailableReportFromMap(gmap);
        }
    }

    public void justLogsAboutThreads() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();

        Log.i("ThreadSets-whileOnMAp", threadSet.toString());
        Log.i("ThreadSetCount-whileOnMAp", threadSet.size() + "");
        Log.i("Active Count-whileOnMAp", Thread.activeCount() + "");

        Runtime runtime = Runtime.getRuntime();
        long usedMemInMB = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        long maxHeapSizeInMB = runtime.maxMemory() / 1048576L;
        long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;
        Log.i("availHeapSizeInMB-whileOnMAp", availHeapSizeInMB + "");
    }

}

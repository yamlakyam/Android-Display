package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.VoucherData;
import com.cnet.VisualAnalysis.Data.VoucherDataForVan;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.MarkerDrawingThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.cnet.VisualAnalysis.VideoActivity;
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

public class MapsFragment extends Fragment implements SecondActivity.KeyPress {
    Marker marker;

    TextView vanNameText;
    TextView driverNameText;
    TextView driverStatusText;
    TextView parameter1;
    TextView parameter2;
    TextView parameter3;
    public static boolean mapPaused;

    TextView grandTotalText;

    public ArrayList<VoucherDataForVan> vansToDisplay;

    GoogleMap gmap;

    public Handler animationHandler;
    public Handler changeDataHandler;
    public MarkerDrawingThread handleRowAnimationThread;

    NavController navController;
    NumberFormat numberFormat = NumberFormat.getInstance();

    public SupportMapFragment mapFragment;

    View view;

    ArrayList<VoucherDataForVan> voucherDataForVans = new ArrayList<>();
    ArrayList<Integer> layoutList = new ArrayList<>();

    LatLngBounds bounds;
    int padding, width, height;

    LatLngBounds.Builder builder = new LatLngBounds.Builder();

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            Log.i("onMapReady", "onMapReady: ");
            Log.i("AsyncTaskCalled", "onMapReady: ");
            gmap = googleMap;
            drawAvailableReportFromMap(SecondActivity.vanIndex, googleMap);
        }
    };


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    public void drawAll(int vanIndex, GoogleMap googleMap) {
//        if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans() != null) {
        if (voucherDataForVans != null) {
            Log.i("TAG", "drawAll: ");
            drawMarkerInVan(vanIndex, googleMap);
            drawLeftPane(googleMap, vanIndex);
        }
    }

    private void drawAvailableReportFromMap(int vanIndex, GoogleMap googleMap) {

        if (SecondActivity.vanIndex < SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size()) {
            if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {

                Log.i("drawAll called", "drawAvailableReportFromMap: ");
                Log.i("vanIndex", SecondActivity.vanIndex + "");
                drawAll(SecondActivity.vanIndex, googleMap);

            } else if (layoutList.contains(10)) {
                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                Log.i("navigate", "to user report");
                navigateToUserReportFromMap(SecondActivity.vanIndex);

            } else if (layoutList.contains(12)) {
                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                Log.i("navigate", "to peakHr report");
                navigateToPeakHourFromMap(SecondActivity.vanIndex);
            } else {
                SecondActivity.vanIndex++;
                if (SecondActivity.vanIndex < voucherDataForVans.size()) {
                    drawAvailableReportFromMap(SecondActivity.vanIndex, googleMap);
                } else {
                    SecondActivity.vanIndex = 0;
                    if (handleRowAnimationThread != null) {
                        handleRowAnimationThread.interrupt();
                    }
                    startActivity(new Intent(requireActivity(), VideoActivity.class));
                }
            }
        } else {
            SecondActivity.vanIndex = 0;
            if (handleRowAnimationThread != null) {
                handleRowAnimationThread.interrupt();
            }
            startActivity(new Intent(requireActivity(), VideoActivity.class));

        }
    }

    private void navigateToUserReportFromMap(int vanIndex) {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size() > 0) {
            if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(SecondActivity.vanIndex).userReportTableRowArrayList.size() > 0) {
                navController.navigate(R.id.userReportForEachOusFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
                    navController.navigate(R.id.peakHourReportFragment);
                } else {
                    SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                    drawAvailableReportFromMap(SecondActivity.vanIndex, gmap);
                }
            } else {
                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                drawAvailableReportFromMap(SecondActivity.vanIndex, gmap);
            }
        }
    }

    private void navigateToPeakHourFromMap(int vanIndex) {
        handleRowAnimationThread.interrupt();
        navController = NavHostFragment.findNavController(mapFragment);
        if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size() > 0) {
            if (SecondActivity.vanIndex < SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size()) {
                if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
                    navController.navigate(R.id.peakHourReportFragment);
                } else {
                    SecondActivity.vanIndex = SecondActivity.vanIndex + 1;

                    drawAvailableReportFromMap(SecondActivity.vanIndex, gmap);

                }
            } else {
                SecondActivity.vanIndex = 0;
                startActivity(new Intent(requireActivity(), VideoActivity.class));

            }


        }
    }

    public void drawLeftPane(GoogleMap googleMap, int vanIndex) {
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
                        LatLngBounds bounds = builder.build();
                        gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));

                    }
                    if (index == SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size() &&
                            vanIndex == SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size() + 1) {

                        Log.i("NAVIGATE_TO_ANIMATION", "handleMessage: ");
                        if (mapPaused) {
                            if (handleRowAnimationThread != null) {
                                handleRowAnimationThread.interrupt();
                            }
                        } else {
                            SecondActivity.vanIndex = 0;
                            if (handleRowAnimationThread != null) {
                                handleRowAnimationThread.interrupt();
                            }
                            startActivity(new Intent(requireActivity(), VideoActivity.class));
                        }

                    } else if (index == SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size() + 1) {
                        Log.i("TAG-LAST VAN", "handleMessage: ");
                        Log.i("van index", SecondActivity.vanIndex + "");
                        Log.i("index", index + "");

                        if (mapPaused) {
                            if (handleRowAnimationThread != null) {
                                handleRowAnimationThread.interrupt();
                            }
                        } else {
                            Log.i("NAVIGATE_TO_NEXT_REP", "handleMessage: ");
                            SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                            navigateToNextReport(navController);
                        }

                    } else if (index < SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size()) {
                        if (handleRowAnimationThread != null) {
                            Log.i("Alive", handleRowAnimationThread.isAlive() + "");
                            Log.i("interrupted", handleRowAnimationThread.isInterrupted() + "");

                        }
                        double latitude = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.get(index).getLatitude();
                        double longitude = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.get(index).getLongitude();
                        LatLng loc = new LatLng(latitude, longitude);

                        builder.include(loc);
                        ArrayList<VoucherData> vsmTransactionTableRows = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList;
                        drawMarkerWithInfo(googleMap, loc, vsmTransactionTableRows, index);
                    }
                }

            }
        };
        handleRowAnimationThread = new MarkerDrawingThread(SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size(), animationHandler, 1000);
        Log.i("handleThread", "handleRowAnimationThread: ");
        handleRowAnimationThread.start();

    }

    private void drawMarkerWithInfo(GoogleMap googleMap, LatLng
            loc, ArrayList<VoucherData> vsmTransactionTableRows, int index) {
        MarkerOptions marker = new MarkerOptions().position(loc);
        Marker mMarker = googleMap.addMarker(marker);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View view = null;
                try {
                    if (mapFragment.isAdded()) {
                        view = getLayoutInflater().inflate(R.layout.custom_pop_up, null, false);
                        TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);

                        String place_name = vsmTransactionTableRows.get(index).getOutlates();
                        if (place_name.length() > 21) {
                            place_name = vsmTransactionTableRows.get(index).getOutlates().substring(0, 21) + "...";
                        }
                        nameTextView.setText(place_name);


                        TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
                        timeTextView.setText(new UtilityFunctionsForActivity1().timeElapsed(
                                new UtilityFunctionsForActivity1().formatTime(vsmTransactionTableRows.get(index).getDateAndTime()), Calendar.getInstance().getTime()));


                        grandTotalText = (TextView) view.findViewById(R.id.grandTotalText);
                        grandTotalText.setText(UtilityFunctionsForActivity2.decimalFormat.format(vsmTransactionTableRows.get(index).getGrandTotal()));


                        TextView itemCountText = (TextView) view.findViewById(R.id.itemCountText);
                        itemCountText.setText(numberFormat.format(vsmTransactionTableRows.get(index).getItemCount()));


                        TextView voucherTextView = (TextView) view.findViewById(R.id.voucherTextView);
                        voucherTextView.setText(vsmTransactionTableRows.get(index).getVoucherNo());


                        if (driverNameText != null) {
                            driverNameText.setText(vsmTransactionTableRows.get(index).getUsername());

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return view;
            }
        });
        mMarker.showInfoWindow();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        BackGroundTasks backGroundTasks = new BackGroundTasks(getContext(), MapsFragment.this);
//        backGroundTasks.execute();
        if (!SecondActivity.pausedstate()) {
            mapPaused = false;
        } else {
            mapPaused = true;
        }

        voucherDataForVans = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans();
        layoutList = SplashScreenActivity.allData.getLayoutList();

        width = getContext().getResources().getDisplayMetrics().widthPixels;
        padding = (int) (width * 0.15);
        height = getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_maps, container, false);
        vanNameText = view.findViewById(R.id.vanNameText);
        driverNameText = view.findViewById(R.id.driverNameText);
        driverStatusText = view.findViewById(R.id.driverStatusText);
        parameter1 = view.findViewById(R.id.parameter1);
        parameter2 = view.findViewById(R.id.parameter2);
        parameter3 = view.findViewById(R.id.parameter3);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
            navController = NavHostFragment.findNavController(mapFragment);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (handleRowAnimationThread != null) {
            Log.i("On stop", "onStop: ");
            handleRowAnimationThread.interrupt();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handleRowAnimationThread != null) {
            Log.i("onPause", "onPause: ");
            handleRowAnimationThread.interrupt();
            Log.i("interrupted", handleRowAnimationThread.isInterrupted() + "");
        }
    }

    @Override
    public void onDestroyView() {
        if (gmap != null) {
            gmap.clear();
        }
        mapFragment.onDestroyView();
        super.onDestroyView();

    }

    public void navigateToNextReport(NavController navController) {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        if (SecondActivity.vanIndex < SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size()) {
            Log.i("vanIndex", SecondActivity.vanIndex + "");
            Log.i("voucherDataForVansSize", voucherDataForVans.size() + "");

            if (layoutList.contains(10)) {
                if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(SecondActivity.vanIndex).userReportTableRowArrayList.size() > 0) {
//                if (voucherData.size() > 0) {
                    Log.i("To-User-Report", "navigateToNextReport: ");
                    navController.navigate(R.id.userReportForEachOusFragment);

                } else if (layoutList.contains(12)) {

                    if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {

                        Log.i("To-Peak-Hr-Report", "navigateToNextReport: ");
                        if (handleRowAnimationThread != null) {
                            Log.i("Dead-or_Alive", handleRowAnimationThread.isAlive() + "");
                        }
//                        navController.navigate(R.id.peakHourReportFragment);
                        navController.navigate(R.id.peakHourReportFragment, null, new NavOptions.Builder()
                                .setPopUpTo(R.id.mapsFragment, true)
                                .build());


                    } else if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
//                    } else if (voucherData.size() > 0) {
//                        if (!mapPaused)
                        Log.i("drawAvailable", "navigateToNextReport: ");
                        drawAvailableReportFromMap(SecondActivity.vanIndex, gmap);
                    } else {
                        SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                        navigateToNextReport(navController);
                    }
                } else {

                    if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
                        Log.i("drawAvailable-1", "navigateToNextReport: ");

                        drawAvailableReportFromMap(SecondActivity.vanIndex, gmap);
                    } else {
                        SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                        navigateToNextReport(navController);
                    }
                }
            } else if (layoutList.contains(12)) {
                ;
                if (SecondActivity.vanIndex < SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size()) {
//                if (SecondActivity.vanIndex < voucherDataForVans.size()) {
                    if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
//                    if (voucherData.size() > 0) {
                        Log.i("TAG-MAP_2", "navigateToNextReport: ");
                        navController.navigate(R.id.peakHourReportFragment);


                    } else if (SecondActivity.vanIndex < SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size()) {
//                    } else if (SecondActivity.vanIndex < voucherDataForVans.size()) {
                        Log.i("TAG-MAP_3", "navigateToNextReport: ");

                        drawAvailableReportFromMap(SecondActivity.vanIndex, gmap);

                    } else {
                        Log.i("TAG-MAP_4", "navigateToNextReport: ");
                        SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                        navigateToNextReport(navController);
                    }
                } else {
                    Log.i("TAG-MAP_5", "navigateToNextReport: ");
                    SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                    navigateToNextReport(navController);
                }
            } else {
                Log.i("TAG-MAP_6", "navigateToNextReport: ");
                drawAvailableReportFromMap(SecondActivity.vanIndex, gmap);
            }
        } else {
            Log.i("TAG-MAP_7", "navigateToNextReport: ");
            SecondActivity.vanIndex = 0;
            startActivity(new Intent(requireActivity(), VideoActivity.class));
        }
    }

    public void navigateToPreviousReport() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }


        if (layoutList.contains(12)) {
            if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {

                navController.navigate(R.id.peakHourReportFragment);

            } else if (layoutList.contains(10)) {
                if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(SecondActivity.vanIndex).userReportTableRowArrayList.size() > 0) {

                    navController.navigate(R.id.userReportForEachOusFragment);
                } else {
                    SecondActivity.vanIndex = SecondActivity.vanIndex - 1;
                    if (SecondActivity.vanIndex >= 0) {
                        if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {

                            drawAvailableReportFromMap(SecondActivity.vanIndex, gmap);
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
                        drawAvailableReportFromMap(SecondActivity.vanIndex, gmap);
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
                navController.navigate(R.id.userReportForEachOusFragment);
            } else {
                SecondActivity.vanIndex = SecondActivity.vanIndex - 1;
                if (SecondActivity.vanIndex >= 0) {
                    if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {

                        drawAvailableReportFromMap(SecondActivity.vanIndex, gmap);
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
                    drawAvailableReportFromMap(SecondActivity.vanIndex, gmap);
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

        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        if (layoutList.contains(11)) {
            navController.navigate(R.id.peakHourReportForAllOusFragment);

        } else if (layoutList.contains(9)) {
            navController.navigate(R.id.userReportForAllOusFragment2);

        } else if (layoutList.contains(8)) {
            navController.navigate(R.id.branchSummaryFragment);

        } else if (layoutList.contains(7)) {
            navController.navigate(R.id.summaryOfLastMonthFragment);

        } else if (layoutList.contains(6)) {
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);

        } else if (layoutList.contains(5)) {
            navController.navigate(R.id.summarizedByArticleChildCategFragment);

        } else if (layoutList.contains(4)) {
            navController.navigate(R.id.summarizedByArticleParentCategFragment);

        } else if (layoutList.contains(3)) {
            navController.navigate(R.id.summarizedByArticleFragment2);
        } else {
            drawAvailableReportFromMap(SecondActivity.vanIndex, gmap);
        }
    }

    @Override
    public void centerKey() {
        mapPaused = !mapPaused;
        Log.i("pause|play", mapPaused + "");
        if (!mapPaused) {
            SecondActivity.playAll();
            SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
            navController = NavHostFragment.findNavController(mapFragment);
            navigateToNextReport(navController);
        } else {
            SecondActivity.pauseAll();
        }
    }

    @Override
    public void leftKey() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
        navigateToPreviousReport();
    }

    @Override
    public void rightKey() {
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
            Log.i("thread-interrupted", "rightKey: ");
        }
        SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
        navController = NavHostFragment.findNavController(mapFragment);
        navigateToNextReport(navController);
    }

//    @Override
//    public void onPreExecute() {
//
//    }
//
//    @Override
//    public void doInBackground() {
//
//        voucherDataForVans = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans();
//        layoutList = SplashScreenActivity.allData.getLayoutList();
//
//        width = getContext().getResources().getDisplayMetrics().widthPixels;
//        padding = (int) (width * 0.15);
//        height = getResources().getDisplayMetrics().heightPixels;
//    }
//
//    @Override
//    public void onPostExecute() {
//        Log.i("onPostExecute-started", "onPostExecute: ");
//
//
//    }


}
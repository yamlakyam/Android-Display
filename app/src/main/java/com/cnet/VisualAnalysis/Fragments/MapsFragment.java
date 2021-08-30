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
import androidx.navigation.fragment.NavHostFragment;

import com.cnet.VisualAnalysis.Data.VoucherData;
import com.cnet.VisualAnalysis.Data.VoucherDataForVan;
import com.cnet.VisualAnalysis.Data.VsmTransactionTableRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SecondActivity;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.VideoActivity;
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

public class MapsFragment extends Fragment {
    int width;
    TextView vanNameText;
    TextView driverNameText;
    TextView parameter1;
    TextView parameter2;
    TextView parameter3;
    int currentVanIndex;
    int currentLocationIndex = 0;
    boolean mapPaused = false;
    int vanIndex = 0;

    public ArrayList<VoucherDataForVan> vansToDisplay;

    GoogleMap gmap;
    public ArrayList<VsmTransactionTableRow> eachTransactionsInaVan;
    public Handler animationHandler;
    public Handler changeDataHandler;
    public HandleRowAnimationThread handleRowAnimationThread;
//    public HandleVanChangeThread handleDataChangeThread;

    NavController navController;
    NumberFormat numberFormat = NumberFormat.getInstance();

    public SupportMapFragment mapFragment;

    View view;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            gmap = googleMap;
//            if (!mapPaused) {
//            drawAll(googleMap);
//                drawAllVansMarkers(0, googleMap, 0);
            drawAvailableReportFromMap(SecondActivity.vanIndex, googleMap);
//            }
        }
    };

    public void drawAll(int vanIndex, GoogleMap googleMap) {
        if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans() != null) {
            drawMarkerInVan(vanIndex, googleMap);
            drawLeftPane(googleMap, vanIndex);
        }
    }

    private void drawAvailableReportFromMap(int vanIndex, GoogleMap googleMap) {
        if (vanIndex < SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size()) {
            if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size() > 0) {
                drawAll(vanIndex, googleMap);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                navigateToUserReportFromMap(vanIndex);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                navigateToPeakHourFromMap(vanIndex);
            } else {
                vanIndex++;
                if (vanIndex < SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size()) {
                    drawAvailableReportFromMap(vanIndex, googleMap);
                } else {
                    SecondActivity.vanIndex = 0;
                    startActivity(new Intent(requireActivity(), VideoActivity.class));
                }
            }
        } else {
            SecondActivity.vanIndex = 0;
            startActivity(new Intent(requireActivity(), VideoActivity.class));
        }
    }

    private void navigateToUserReportFromMap(int vanIndex) {
        navController = NavHostFragment.findNavController(mapFragment);
        if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size() > 0) {
            if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(vanIndex).userReportTableRowArrayList.size() > 0) {
                Log.i("TAG-1", "navigateToUserReportFromMap: ");
                navController.navigate(R.id.userReportForEachOusFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                Log.i("TAG-2", "navigateToUserReportFromMap: ");
                if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(vanIndex).figureReportDataElementsArrayList.size() > 0) {
                    Log.i("TAG-3", "navigateToUserReportFromMap: ");
                    navController.navigate(R.id.peakHourReportFragment);
                } else {
                    Log.i("TAG-4", "navigateToUserReportFromMap: ");

                    SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                    vanIndex = vanIndex + 1;
                    drawAvailableReportFromMap(vanIndex, gmap);
                }
            } else {
                Log.i("TAG-5", "navigateToUserReportFromMap: ");

                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                vanIndex = vanIndex + 1;
                drawAvailableReportFromMap(vanIndex, gmap);
            }
        }
    }

    private void navigateToPeakHourFromMap(int vanIndex) {
        navController = NavHostFragment.findNavController(mapFragment);
        if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().size() > 0) {
            if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(vanIndex).figureReportDataElementsArrayList.size() > 0) {
                navController.navigate(R.id.peakHourReportFragment);
            } else {
                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                vanIndex = vanIndex + 1;
                drawAvailableReportFromMap(vanIndex, gmap);
            }
        }
    }

    public void drawLeftPane(GoogleMap googleMap, int vanIndex) {
        if (SplashScreenActivity.allData != null) {
            vansToDisplay = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans();
        }

        vanNameText.setText(vansToDisplay.get(vanIndex).nameOfVan);
//        parameter1.setText(numberFormat.format(vansToDisplay.get(vanIndex).salesOutLateCount));
//        parameter1.setText(numberFormat.format(vansToDisplay.get(vanIndex).));
//        parameter2.setText(numberFormat.format(vansToDisplay.get(vanIndex).allLineItemCount));
//        parameter3.setText(numberFormat.format(Math.round(vansToDisplay.get(vanIndex).totalPrice * 100.0) / 100.0));

        if (currentLocationIndex == 0) {
            googleMap.clear();
        }
    }

    @SuppressLint("HandlerLeak")
    public void drawMarkerInVan(int vanIndex, GoogleMap googleMap) {
        animationHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                String message = (String) msg.obj;
                int index = 0;
                if (message != null) {
                    index = Integer.parseInt(message);
                    currentLocationIndex = index;
                }
//                if (index == SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).getTableRows().size() - 1) {
                if (index == SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size() - 1) {
                    currentLocationIndex = 0;
                    Log.i("second from last", "handleMessage: ");
                }
//                if (index == SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).getTableRows().size() && vanIndex == SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().size() - 1) {
                if (index == SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size() && vanIndex == SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size() - 1) {
                    SecondActivity.vanIndex = 0;
                    startActivity(new Intent(requireActivity(), VideoActivity.class));
                }
//                if (index == SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).getTableRows().size()) {
                if (index == SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size()) {
                    SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                    navigateToNextReport();
//                } else if (index < SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).getTableRows().size()) {
                } else if (index < SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size()) {
//                    double latitude = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).tableRows.get(index).getLatitude();
                    double latitude = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.get(index).getLatitude();
//                    double longitude = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).tableRows.get(index).getLongitude();
                    double longitude = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.get(index).getLongitude();
                    LatLng loc = new LatLng(latitude, longitude);

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    ArrayList<VoucherData> vsmTransactionTableRows = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList;
//                    ArrayList<VsmTransactionTableRow> vsmTransactionTableRows = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).tableRows;
                    drawMarkerWithInfo(googleMap, builder, loc, vsmTransactionTableRows, index);
                }
            }
        };
//        handleRowAnimationThread = new HandleRowAnimationThread(SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).getTableRows().size(),
        handleRowAnimationThread = new HandleRowAnimationThread(SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size(),
                animationHandler, 1000);
        handleRowAnimationThread.start();
    }

    //    private void drawMarkerWithInfo(GoogleMap googleMap, LatLngBounds.Builder builder, LatLng loc, ArrayList<VsmTransactionTableRow> vsmTransactionTableRows, int index) {
    private void drawMarkerWithInfo(GoogleMap googleMap, LatLngBounds.Builder builder, LatLng
            loc, ArrayList<VoucherData> vsmTransactionTableRows, int index) {
        MarkerOptions marker = new MarkerOptions().position(loc);
        Marker mMarker = googleMap.addMarker(marker);
        builder.include(marker.getPosition());
        LatLngBounds bounds = builder.build();
        width = mapFragment.getResources().getDisplayMetrics().widthPixels;
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

                String place_name = vsmTransactionTableRows.get(index).getOutlates();
                if (place_name.length() > 21) {
                    place_name = vsmTransactionTableRows.get(index).getOutlates().substring(0, 21) + "...";
                }
                nameTextView.setText(place_name);
                TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
                timeTextView.setText(new UtilityFunctionsForActivity1().timeElapsed(new UtilityFunctionsForActivity1().formatTime(vsmTransactionTableRows.get(index).getDateAndTime()), Calendar.getInstance().getTime()));
                TextView grandTotalText = (TextView) view.findViewById(R.id.grandTotalText);
                grandTotalText.setText(numberFormat.format(Math.round(vsmTransactionTableRows.get(index).getGrandTotal() * 100.0) / 100.0));
                TextView itemCountText = (TextView) view.findViewById(R.id.itemCountText);
                itemCountText.setText(numberFormat.format(vsmTransactionTableRows.get(index).getItemCount()));
                TextView voucherTextView = (TextView) view.findViewById(R.id.voucherTextView);
                voucherTextView.setText(vsmTransactionTableRows.get(index).getVoucherNo());
                if (driverNameText != null) {
                    driverNameText.setText(vsmTransactionTableRows.get(index).getUsername());
                }
                return view;
            }
        });
        mMarker.showInfoWindow();
    }

    public void navigate() {
        NavController navController = NavHostFragment.findNavController(mapFragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(3))
            navController.navigate(R.id.summarizedByArticleFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(4))
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(5))
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(6))
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(7))
            navController.navigate(R.id.summaryOfLastMonthFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(8))
            navController.navigate(R.id.branchSummaryFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(9))
            navController.navigate(R.id.userReportForAllOusFragment2);
        else if (SplashScreenActivity.allData.getLayoutList().contains(11))
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(10))
            navController.navigate(R.id.userReportForEachOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(12))
            navController.navigate(R.id.peakHourReportFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(1))
            navController.navigate(R.id.mapsFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_maps, container, false);
        vanNameText = view.findViewById(R.id.vanNameText);
        driverNameText = view.findViewById(R.id.driverNameText);
        parameter1 = view.findViewById(R.id.parameter1);
        parameter2 = view.findViewById(R.id.parameter2);
        parameter3 = view.findViewById(R.id.parameter3);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (handleRowAnimationThread != null) {
            handleRowAnimationThread.interrupt();
        }
    }

    @Override
    public void onDestroyView() {
        if (gmap != null) {
            gmap.clear();
        }

        mapFragment.onDestroyView();
        super.onDestroyView();
        driverNameText = null;
        vanNameText = null;
        parameter1 = null;
        parameter2 = null;
        parameter3 = null;
        view = null;
        if (handleRowAnimationThread != null) {
            Log.i("isThreadAlive", handleRowAnimationThread.isAlive() + "");
        }
    }

    public void navigateToNextReport() {
        Log.i("TAG", "navigateToNextReport: ");
        NavController navController = NavHostFragment.findNavController(mapFragment);
        if (SplashScreenActivity.allData.getLayoutList().contains(10))
            navController.navigate(R.id.userReportForEachOusFragment);
        else if (SplashScreenActivity.allData.getLayoutList().contains(12))
            navController.navigate(R.id.peakHourReportFragment);
    }
}
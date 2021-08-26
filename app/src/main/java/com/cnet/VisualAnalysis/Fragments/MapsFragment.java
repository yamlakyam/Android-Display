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

import com.cnet.VisualAnalysis.Data.VsmTableDataForSingleVan;
import com.cnet.VisualAnalysis.Data.VsmTransactionTableRow;
import com.cnet.VisualAnalysis.R;
import com.cnet.VisualAnalysis.SplashScreenActivity;
import com.cnet.VisualAnalysis.Threads.HandleRowAnimationThread;
import com.cnet.VisualAnalysis.Threads.HandleVanChangeThread;
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

    public ArrayList<VsmTableDataForSingleVan> vansToDisplay;

    GoogleMap gmap;
    public ArrayList<VsmTransactionTableRow> eachTransactionsInaVan;
    public Handler animationHandler;
    public Handler changeDataHandler;
    public HandleRowAnimationThread handleRowAnimationThread;
    public HandleVanChangeThread handleDataChangeThread;

    NavController navController;

    NumberFormat numberFormat = NumberFormat.getInstance();

    public SupportMapFragment mapFragment;

    View view;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            gmap = googleMap;
            if (!mapPaused) {
                drawAll(googleMap);
//                drawAllVansMarkers(0, googleMap, 0);

            }
        }
    };

//    @SuppressLint("HandlerLeak")
//    public void drawAllVansMarkers(int startingIndex, GoogleMap googleMap, int locIndex) {
//        changeDataHandler = new Handler() {
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                String message = (String) msg.obj;
//                int index = 0;
//                if (message != null) {
//                    index = Integer.parseInt(message);
//                    currentVanIndex = index;
//                }
//                vansToDisplay = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData();
//
//                try {
//                    Collections.sort(vansToDisplay, new Comparator<VsmTableDataForSingleVan>() {
//                        @Override
//                        public int compare(VsmTableDataForSingleVan o1, VsmTableDataForSingleVan o2) {
//                            return Integer.parseInt(o1.nameOfVan.substring(3)) < Integer.parseInt(o2.nameOfVan.substring(3)) ? -1 : 0;
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                drawMarkerInVan(index, googleMap, locIndex);
//                vanNameText.setText(vansToDisplay.get(index).nameOfVan);
//                parameter1.setText(numberFormat.format(vansToDisplay.get(index).salesOutLateCount));
//                parameter2.setText(numberFormat.format(vansToDisplay.get(index).allLineItemCount));
//                parameter3.setText(numberFormat.format(Math.round(vansToDisplay.get(index).totalPrice * 100.0) / 100.0));
//
//                Log.i("currentloc", currentLocationIndex + "");
//                if (currentLocationIndex == 0) {
//                    googleMap.clear();
//                }
//            }
//        };
//        ArrayList<VsmTableDataForSingleVan> vsmTableDatasForSingleVan = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData();
//        handleDataChangeThread = new HandleVanChangeThread(changeDataHandler, SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().size(), vsmTableDatasForSingleVan, startingIndex);
//        handleDataChangeThread.start();
//
//    }

    public void drawAll(GoogleMap googleMap) {


        Bundle bundleRecieved = getArguments();
        /*if (bundleRecieved != null) {
            int nextVanNo = bundleRecieved.getInt("nextVanNo", 0);
            Log.i("next van", nextVanNo + "");
            if (nextVanNo == SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().size()) {
                Log.i("last van", "time to naviagte");
                NavController navController = NavHostFragment.findNavController(mapFragment);
                navController.navigate(R.id.peakHourReportFragment);
            } else if (nextVanNo < SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().size()) {
                drawLeftPane(googleMap, nextVanNo);
                drawMarkerInVan(nextVanNo, googleMap, locIndex);
            }
        } else {
            drawLeftPane(googleMap, 0);
            drawMarkerInVan(0, googleMap, locIndex);
        }
         */
        if (bundleRecieved != null) {

            ArrayList<VsmTableDataForSingleVan> allVans = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData();

            int indexOfThisVan = 0;
            String vanName = bundleRecieved.getString("currentVan");
            Log.i("vanName", vanName);
            for (int i = 0; i < allVans.size(); i++) {
                if (allVans.get(i).nameOfVan.equals(vanName)) {
                    indexOfThisVan = i;
                }
            }
            Log.i("indexOfThisVan", indexOfThisVan + "");
            if (indexOfThisVan == allVans.size()) {
                Log.i("last van", "time to naviagte");

//                NavController navController = NavHostFragment.findNavController(mapFragment);
//                navController.navigate(R.id.peakHourReportFragment, nextIndexBundl
                startActivity(new Intent(requireActivity(), VideoActivity.class));

            } else if (indexOfThisVan < allVans.size()) {
                Log.i("indexOfThisVan-2", indexOfThisVan + "");

                drawMarkerInVan(indexOfThisVan, googleMap);
                drawLeftPane(googleMap, indexOfThisVan);
            }
        } else {

            drawMarkerInVan(0, googleMap);
            drawLeftPane(googleMap, 0);
        }
    }

    public void drawLeftPane(GoogleMap googleMap, int vanIndex) {
        if (SplashScreenActivity.allData != null) {
            vansToDisplay = SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData();
        }
//        try {
//            Collections.sort(vansToDisplay, new Comparator<VsmTableDataForSingleVan>() {
//                @Override
//                public int compare(VsmTableDataForSingleVan o1, VsmTableDataForSingleVan o2) {
//                    return Integer.parseInt(o1.nameOfVan.substring(3)) < Integer.parseInt(o2.nameOfVan.substring(3)) ? -1 : 0;
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        vanNameText.setText(vansToDisplay.get(vanIndex).nameOfVan);
        parameter1.setText(numberFormat.format(vansToDisplay.get(vanIndex).salesOutLateCount));
        parameter2.setText(numberFormat.format(vansToDisplay.get(vanIndex).allLineItemCount));
        parameter3.setText(numberFormat.format(Math.round(vansToDisplay.get(vanIndex).totalPrice * 100.0) / 100.0));

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
                if (index == SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).getTableRows().size() - 1) {
                    currentLocationIndex = 0;
                    Log.i("second from last", "handleMessage: ");
                }
                if (index == SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).getTableRows().size()) {
                    currentLocationIndex = 0;
                    Log.i("first from last", "handleMessage: ");

//                    Bundle bundle = new Bundle();
////                    bundle.putInt("currentVan", vanIndex);
//                    bundle.putString("currentVan", SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).nameOfVan);
//                    bundle.putInt("VanNo", vanIndex);
                    Bundle receivedBundle = getArguments();
                    int vanIndex = receivedBundle.getInt("vanIndex", 0);
                    vanIndex = vanIndex + 1;
                    Bundle nextIndexBundle = new Bundle();
                    nextIndexBundle.putInt("vanIndex", vanIndex);

                    navController = NavHostFragment.findNavController(mapFragment);
                    navController.navigate(R.id.userReportForEachOusFragment, nextIndexBundle);
                }
                if (index == SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).getTableRows().size() && vanIndex == SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().size() - 1) {
//                    startActivity(new Intent(MapsActivity.this, SecondActivity.class));
//                    startActivity(new Intent(requireActivity(), SecondActivity.class));
                    startActivity(new Intent(requireActivity(), VideoActivity.class));
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
                animationHandler, 1000);
        handleRowAnimationThread.start();
    }

    private void drawMarkerWithInfo(GoogleMap googleMap, LatLngBounds.Builder builder, LatLng loc, ArrayList<VsmTransactionTableRow> vsmTransactionTableRows, int index) {
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
    }
}
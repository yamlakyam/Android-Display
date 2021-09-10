package com.cnet.VisualAnalysis.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
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
import com.cnet.VisualAnalysis.Threads.MarkerDrawingThread;
import com.cnet.VisualAnalysis.Utils.BackGroundTasks;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity1;
import com.cnet.VisualAnalysis.Utils.UtilityFunctionsForActivity2;
import com.cnet.VisualAnalysis.VideoActivity;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.internal.zzf;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MapsFragment extends Fragment implements SecondActivity.KeyPress, BackGroundTasks.CalculateInBackground {
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
    public ArrayList<VsmTransactionTableRow> eachTransactionsInaVan;
    public Handler animationHandler;
    public Handler changeDataHandler;
    public MarkerDrawingThread handleRowAnimationThread;

    NavController navController;
    NumberFormat numberFormat = NumberFormat.getInstance();

    public SupportMapFragment mapFragment;

    View view;

    ArrayList<String> place_names = new ArrayList<>();
    ArrayList<String> time_elapsed_values = new ArrayList<>();
    ArrayList<String> grand_total_values = new ArrayList<>();
    ArrayList<String> item_count_values = new ArrayList<>();
    ArrayList<String> voucher_no_list = new ArrayList<>();
    ArrayList<String> user_name_list = new ArrayList<>();
    CameraUpdate cu;
    ArrayList<VoucherData> voucherData = new ArrayList<>();
    ArrayList<VoucherDataForVan> voucherDataForVans = new ArrayList<>();

    LatLngBounds bounds;
    int padding, width, height;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            Log.i("onMapReady", "onMapReady: ");
//            BackGroundTasks backGroundTasks = new BackGroundTasks(mapFragment.getContext(), MapsFragment.this);
//            backGroundTasks.execute();
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

//        if (SecondActivity.vanIndex < SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size()) {
        if (SecondActivity.vanIndex < voucherDataForVans.size()) {
//            if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
            if (voucherData.size() > 0) {
                Log.i("drawAll called", "drawAvailableReportFromMap: ");
                drawAll(SecondActivity.vanIndex, googleMap);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                Log.i("navigate", "to user report");
                navigateToUserReportFromMap(SecondActivity.vanIndex);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                Log.i("navigate", "to peakHr report");
                navigateToPeakHourFromMap(SecondActivity.vanIndex);
            } else {
                SecondActivity.vanIndex++;
                if (SecondActivity.vanIndex < voucherDataForVans.size()) {

                    drawAvailableReportFromMap(SecondActivity.vanIndex, googleMap);

                } else {
                    SecondActivity.vanIndex = 0;
                    startActivity(new Intent(requireActivity(), VideoActivity.class));
//                    navController.navigate(R.id.videoFragment);
                }
            }
        } else {
            SecondActivity.vanIndex = 0;
            startActivity(new Intent(requireActivity(), VideoActivity.class));
//            navController.navigate(R.id.videoFragment);
        }
    }

    private void navigateToUserReportFromMap(int vanIndex) {
//        navController = NavHostFragment.findNavController(mapFragment);
        if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().size() > 0) {
//            if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(SecondActivity.vanIndex).userReportTableRowArrayList.size() > 0) {
            if (voucherData.size() > 0) {
//                if (!mapPaused)
                navController.navigate(R.id.userReportForEachOusFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
//                if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
                if (voucherData.size() > 0) {
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
//                navController.navigate(R.id.videoFragment);
            }


        }
    }

    public void drawLeftPane(GoogleMap googleMap, int vanIndex) {
        if (SplashScreenActivity.allData != null) {
//            vansToDisplay = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans();
            vansToDisplay = voucherDataForVans;
        }

        vanNameText.setText(vansToDisplay.get(vanIndex).nameOfVan);
        parameter1.setText(numberFormat.format(vansToDisplay.get(vanIndex).transactionCount));
        parameter2.setText(numberFormat.format(vansToDisplay.get(vanIndex).lineItems));
//        parameter3.setText(numberFormat.format(Math.round(vansToDisplay.get(vanIndex).grandTotal * 100.0) / 100.0));
        parameter3.setText(UtilityFunctionsForActivity2.decimalFormat.format(vansToDisplay.get(vanIndex).grandTotal));
        int lastIndexInaVan = vansToDisplay.get(vanIndex).voucherDataArrayList.size() - 1;
//        driverStatusText.setText(new UtilityFunctionsForActivity1().driverStatus(
////                                new UtilityFunctionsForActivity1().formatTime(vsmTransactionTableRows.get(index).getDateAndTime()), Calendar.getInstance().getTime()));
//                new UtilityFunctionsForActivity1().formatTime(vansToDisplay.get(vanIndex).voucherDataArrayList.get(lastIndexInaVan).dateAndTime), Calendar.getInstance().getTime()));

        driverStatusText.setText(time_elapsed_values.get(lastIndexInaVan));
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
                }
//                if (index == SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size()) {
                if (index == voucherData.size()) {
                    Log.i("SHOW_ALL_VAN", "handleMessage: ");
                    gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
//                    gmap.animateCamera(cu);
                }
//                if (index == SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size() &&
//                        vanIndex == SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size() + 1) {

                if (index == voucherData.size() &&
//                        vanIndex == SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size() + 1) {
                        vanIndex == voucherDataForVans.size() + 1) {

                    Log.i("NAVIGATE_TO_ANIMATION", "handleMessage: ");
                    if (mapPaused) {
                        if (handleRowAnimationThread != null) {
                            handleRowAnimationThread.interrupt();
                        }
                    } else {
                        SecondActivity.vanIndex = 0;
//                        System.gc();
                        startActivity(new Intent(requireActivity(), VideoActivity.class));
//                        navController.navigate(R.id.videoFragment);
                    }

//                } else if (index == SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size() + 1) {
                } else if (index == voucherData.size() + 1) {
                    Log.i("TAG-LAST VAN", "handleMessage: ");

                    if (mapPaused) {
                        if (handleRowAnimationThread != null) {
                            handleRowAnimationThread.interrupt();
                        }
                    } else {
                        Log.i("NAVIGATE_TO_NEXT_REP", "handleMessage: ");
                        SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                        navigateToNextReport(navController);
                    }

//                } else if (index < SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size()) {
                } else if (index < voucherData.size()) {
                    Log.i("DRAW_EACH_MARKER", "handleMessage: ");
//                    double latitude = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.get(index).getLatitude();
                    double latitude = voucherData.get(index).getLatitude();
//                    double longitude = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.get(index).getLongitude();
                    double longitude = voucherData.get(index).getLongitude();
                    LatLng loc = new LatLng(latitude, longitude);
//                    ArrayList<VoucherData> vsmTransactionTableRows = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList;
//                    drawMarkerWithInfo(googleMap, builder, loc, vsmTransactionTableRows, index);
                    drawMarkerWithInfo(googleMap, loc, index);
                }
            }
        };
//        handleRowAnimationThread = new HandleRowAnimationThread(SplashScreenActivity.allData.getDashBoardData().getVsmTableForSingleDistributor().getAllVansData().get(vanIndex).getTableRows().size(),
//        handleRowAnimationThread = new HandleRowAnimationThread(SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(vanIndex).voucherDataArrayList.size(), animationHandler, 1000);
        handleRowAnimationThread = new MarkerDrawingThread(voucherData.size(), animationHandler, 1000);
        handleRowAnimationThread.start();
    }

    private void drawMarkerWithInfo(GoogleMap googleMap, LatLng
            loc, int index) {
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
                    view = getLayoutInflater().inflate(R.layout.custom_pop_up, null, false);
                    TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);

//                    String place_name = vsmTransactionTableRows.get(index).getOutlates();
//                    if (place_name.length() > 21) {
//                        place_name = vsmTransactionTableRows.get(index).getOutlates().substring(0, 21) + "...";
//                    }
//                    nameTextView.setText(place_name);
                    nameTextView.setText(place_names.get(index));

                    TextView timeTextView = (TextView) view.findViewById(R.id.timeTextView);
//                    timeTextView.setText(new UtilityFunctionsForActivity1().timeElapsed(
//                            new UtilityFunctionsForActivity1().formatTime(vsmTransactionTableRows.get(index).getDateAndTime()), Calendar.getInstance().getTime()));
                    timeTextView.setText(time_elapsed_values.get(index));

                    grandTotalText = (TextView) view.findViewById(R.id.grandTotalText);
//                    grandTotalText.setText(UtilityFunctionsForActivity2.decimalFormat.format(vsmTransactionTableRows.get(index).getGrandTotal()));
                    grandTotalText.setText(grand_total_values.get(index));

                    TextView itemCountText = (TextView) view.findViewById(R.id.itemCountText);
//                    itemCountText.setText(numberFormat.format(vsmTransactionTableRows.get(index).getItemCount()));
                    itemCountText.setText(item_count_values.get(index));

                    TextView voucherTextView = (TextView) view.findViewById(R.id.voucherTextView);
//                    voucherTextView.setText(vsmTransactionTableRows.get(index).getVoucherNo());
                    voucherTextView.setText(voucher_no_list.get(index));

                    if (driverNameText != null) {
//                        driverNameText.setText(vsmTransactionTableRows.get(index).getUsername());
                        driverNameText.setText(user_name_list.get(index));
                    }
//                    if (driverStatusText != null) {
//                        driverStatusText.setText(new UtilityFunctionsForActivity1().driverStatus(
//                                new UtilityFunctionsForActivity1().formatTime(vsmTransactionTableRows.get(index).getDateAndTime()), Calendar.getInstance().getTime()));
//                    }
                    Log.i("AFTER_SNIPPET_DRAWN", "getInfoContents: ");
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
//        BackGroundTasks backGroundTasks = new BackGroundTasks(mapFragment.getContext(), MapsFragment.this);
        BackGroundTasks backGroundTasks = new BackGroundTasks(getContext(), MapsFragment.this);
        backGroundTasks.execute();
        if (!SecondActivity.pausedstate()) {
            mapPaused = false;
        } else {
            mapPaused = true;
        }
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
            handleRowAnimationThread.interrupt();
            handleRowAnimationThread = null;
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

//        if (SecondActivity.vanIndex < SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size()) {
        if (SecondActivity.vanIndex < voucherDataForVans.size()) {
            if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
//                if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(SecondActivity.vanIndex).userReportTableRowArrayList.size() > 0) {
                if (voucherData.size() > 0) {
//                    if (!mapPaused) {
                    navController.navigate(R.id.userReportForEachOusFragment);
//                    }
                } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {

//                    if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
                    if (voucherData.size() > 0) {
//                        if (!mapPaused) {
                        navController.navigate(R.id.peakHourReportFragment);
//                        }

//                    } else if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
                    } else if (voucherData.size() > 0) {
//                        if (!mapPaused)
                        drawAvailableReportFromMap(SecondActivity.vanIndex, gmap);
                    } else {
                        SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                        navigateToNextReport(navController);
                    }
                } else {

//                    if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
                    if (voucherData.size() > 0) {
//                        if (!mapPaused) {
                        drawAvailableReportFromMap(SecondActivity.vanIndex, gmap);
//                        }
                    } else {
                        SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
                        navigateToNextReport(navController);
                    }
                }
            } else if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
                ;
                if (SecondActivity.vanIndex < SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size()) {
//                    if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
                    if (voucherData.size() > 0) {
                        Log.i("TAG-MAP_2", "navigateToNextReport: ");
//                        if (!mapPaused) {
                        navController.navigate(R.id.peakHourReportFragment);
//                        }

//                    } else if (SecondActivity.vanIndex < SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().size()) {
                    } else if (SecondActivity.vanIndex < voucherDataForVans.size()) {
                        Log.i("TAG-MAP_3", "navigateToNextReport: ");
//                        if (!mapPaused) {
                        drawAvailableReportFromMap(SecondActivity.vanIndex, gmap);
//                        }
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
//            navController.navigate(R.id.videoFragment);
        }
    }

    public void navigateToPreviousReport() {

        if (SplashScreenActivity.allData.getLayoutList().contains(12)) {
//            if (SplashScreenActivity.allData.getDashBoardData().getFigureReportDataforEachBranch().get(SecondActivity.vanIndex).figureReportDataElementsArrayList.size() > 0) {
            if (voucherData.size() > 0) {
                navController.navigate(R.id.peakHourReportFragment);
            } else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
//                if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(SecondActivity.vanIndex).userReportTableRowArrayList.size() > 0) {
                if (voucherData.size() > 0) {
                    navController.navigate(R.id.userReportForEachOusFragment);
                } else {
                    SecondActivity.vanIndex = SecondActivity.vanIndex - 1;
                    if (SecondActivity.vanIndex >= 0) {
//                        if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
                        if (voucherData.size() > 0) {
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
//                    if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
                    if (voucherData.size() > 0) {
                        drawAvailableReportFromMap(SecondActivity.vanIndex, gmap);
                    } else {
                        navigateToPreviousReport();
                    }
                } else {
                    SecondActivity.vanIndex = 0;
                    navigateToLeftFragments();
                }
            }

        } else if (SplashScreenActivity.allData.getLayoutList().contains(10)) {
//            if (SplashScreenActivity.allData.getDashBoardData().getUserReportForEachBranch().get(SecondActivity.vanIndex).userReportTableRowArrayList.size() > 0) {
            if (voucherData.size() > 0) {
                navController.navigate(R.id.userReportForEachOusFragment);
            } else {
                SecondActivity.vanIndex = SecondActivity.vanIndex - 1;
                if (SecondActivity.vanIndex >= 0) {
//                    if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
                    if (voucherData.size() > 0) {
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
//                if (SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList.size() > 0) {
                if (voucherData.size() > 0) {
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
        if (SplashScreenActivity.allData.getLayoutList().contains(11)) {
            navController.navigate(R.id.peakHourReportForAllOusFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(9)) {
            navController.navigate(R.id.userReportForAllOusFragment2);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(8)) {
            navController.navigate(R.id.branchSummaryFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(7)) {
            navController.navigate(R.id.summaryOfLastMonthFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(6)) {
            navController.navigate(R.id.summaryOfLastSixMonthsFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(5)) {
            navController.navigate(R.id.summarizedByArticleChildCategFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(4)) {
            navController.navigate(R.id.summarizedByArticleParentCategFragment);
        } else if (SplashScreenActivity.allData.getLayoutList().contains(3)) {
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
        }
        SecondActivity.vanIndex = SecondActivity.vanIndex + 1;
        navController = NavHostFragment.findNavController(mapFragment);
        navigateToNextReport(navController);
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void doInBackground() {

        Log.i("doInBackground-started", "doInBackground: ");
        voucherDataForVans = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans();
        voucherData = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList;

        for (int i = 0; i < voucherData.size(); i++) {
            String time_eplased = new UtilityFunctionsForActivity1().timeElapsed(
                    new UtilityFunctionsForActivity1().formatTime(voucherData.get(i).getDateAndTime()), Calendar.getInstance().getTime());
            String grand_total = UtilityFunctionsForActivity2.decimalFormat.format(voucherData.get(i).getGrandTotal());
            String place_name = voucherData.get(i).getOutlates();
            if (place_name.length() > 21) {
                place_name = voucherData.get(i).getOutlates().substring(0, 21) + "...";
            } else {
                place_name = place_name;
            }
            String item_count = numberFormat.format(voucherData.get(i).itemCount);
            String voucher_no = voucherData.get(i).voucherNo;
            String user_name = voucherData.get(i).username;

            place_names.add(place_name);
            time_elapsed_values.add(time_eplased);
            grand_total_values.add(grand_total);
            item_count_values.add(item_count);
            voucher_no_list.add(voucher_no);
            user_name_list.add(user_name);
        }


        ArrayList<VoucherData> voucherData = SplashScreenActivity.allData.getDashBoardData().getVoucherDataForVans().get(SecondActivity.vanIndex).voucherDataArrayList;
        ArrayList<Marker> markers = new ArrayList<>();
        for (int i = 0; i < voucherData.size(); i++) {
            double latitude = voucherData.get(i).latitude;
            double longitude = voucherData.get(i).longitude;
            LatLng loc = new LatLng(latitude, longitude);
            Marker marker = new Marker(new zzf() {
                @Override
                public void remove() throws RemoteException {

                }

                @Override
                public String getId() throws RemoteException {
                    return null;
                }

                @Override
                public void setPosition(LatLng latLng) throws RemoteException {
                    latLng = loc;

                }

                @Override
                public LatLng getPosition() throws RemoteException {
//                    return null;
                    return loc;
                }

                @Override
                public void setTitle(String s) throws RemoteException {

                }

                @Override
                public String getTitle() throws RemoteException {
                    return null;
                }

                @Override
                public void setSnippet(String s) throws RemoteException {

                }

                @Override
                public String getSnippet() throws RemoteException {
                    return null;
                }

                @Override
                public void setDraggable(boolean b) throws RemoteException {

                }

                @Override
                public boolean isDraggable() throws RemoteException {
                    return false;
                }

                @Override
                public void showInfoWindow() throws RemoteException {

                }

                @Override
                public void hideInfoWindow() throws RemoteException {

                }

                @Override
                public boolean isInfoWindowShown() throws RemoteException {
                    return false;
                }

                @Override
                public void setVisible(boolean b) throws RemoteException {

                }

                @Override
                public boolean isVisible() throws RemoteException {
                    return false;
                }

                @Override
                public boolean zzj(zzf zzf) throws RemoteException {
                    return false;
                }

                @Override
                public int hashCodeRemote() throws RemoteException {
                    return 0;
                }

                @Override
                public void zzw(zzd zzd) throws RemoteException {

                }

                @Override
                public void setAnchor(float v, float v1) throws RemoteException {

                }

                @Override
                public void setFlat(boolean b) throws RemoteException {

                }

                @Override
                public boolean isFlat() throws RemoteException {
                    return false;
                }

                @Override
                public void setRotation(float v) throws RemoteException {

                }

                @Override
                public float getRotation() throws RemoteException {
                    return 0;
                }

                @Override
                public void setInfoWindowAnchor(float v, float v1) throws RemoteException {

                }

                @Override
                public void setAlpha(float v) throws RemoteException {

                }

                @Override
                public float getAlpha() throws RemoteException {
                    return 0;
                }

                @Override
                public IBinder asBinder() {
                    return null;
                }
            });
            markers.add(marker);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }

        bounds = builder.build();
//        width = mapFragment.getResources().getDisplayMetrics().widthPixels;
        width = getContext().getResources().getDisplayMetrics().widthPixels;
        padding = (int) (width * 0.15);
        height = getResources().getDisplayMetrics().heightPixels;

//        cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        Log.i("doInBackground-finished", "doInBackground: ");
    }

    @Override
    public void onPostExecute() {
        Log.i("onPostExecute-started", "onPostExecute: ");
//        drawAvailableReportFromMap(SecondActivity.vanIndex, gmap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }
}
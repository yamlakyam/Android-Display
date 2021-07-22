package com.cnet.VisualAnalysis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cnet.VisualAnalysis.Data.DashBoardData;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    public static JSONArray summaryTableJSONArray;
    public static JSONArray vsmCardJSONArray;
    public static JSONArray distributorTableJSONArray;
    public static JSONArray vsmTransactionJSONArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
package com.cnet.VisualAnalysis;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;

import org.json.JSONArray;

public class SecondActivity extends AppCompatActivity {
    public static JSONArray dashBoardArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
}
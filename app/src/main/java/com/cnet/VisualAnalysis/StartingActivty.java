package com.cnet.VisualAnalysis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.cnet.VisualAnalysis.Utils.Constants;
import com.cnet.VisualAnalysis.Utils.VolleyHttp;

import org.json.JSONArray;

public class StartingActivty extends AppCompatActivity {

    Button heinekenSummaryButton;
    Button dashboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_activty);

        heinekenSummaryButton = findViewById(R.id.app1Btn);
        dashboardButton = findViewById(R.id.app2Btn);
        heinekenSummaryButton.requestFocus();
        dashboardButton.requestFocus();
        heinekenSummaryButton.requestFocus();
        dashboardButton.requestFocus();


        heinekenSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartingActivty.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartingActivty.this, SecondActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

        Log.i("back", "back pressed");
        System.exit(0);
        Log.i("back", "back pressed");

    }

}
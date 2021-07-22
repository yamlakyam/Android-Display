package com.cnet.VisualAnalysis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartingActivty extends AppCompatActivity {

    Button heinekenSummaryButton;
    Button dashboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_activty);

        heinekenSummaryButton=findViewById(R.id.app1Btn);
        dashboardButton=findViewById(R.id.app2Btn);


        heinekenSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(StartingActivty.this, MainActivity.class);
                startActivity(intent);
            }
        });

        dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(StartingActivty.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }


}
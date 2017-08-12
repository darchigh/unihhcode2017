package com.liisa.chatbotapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * Created by liisa_000 on 14/05/2017.
 */

public class StatisticsActivity extends AppCompatActivity {

    ImageButton BarChart1;
    ImageButton BarChart2;
    ImageButton BarChart3;
    ImageButton BarChart4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_activity);
        BarChart1 = (ImageButton) findViewById(R.id.Barchart1);
        BarChart2 = (ImageButton) findViewById(R.id.Barchart2);
        BarChart3 = (ImageButton) findViewById(R.id.Barchart3);
        BarChart4 = (ImageButton) findViewById(R.id.Barchart4);

        BarChart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(StatisticsActivity.this, BarChartActivity.class);
                StatisticsActivity.this.startActivity(myIntent);
            }
        });
        BarChart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(StatisticsActivity.this, BarChartActivity.class);
                StatisticsActivity.this.startActivity(myIntent);
            }
        });
        BarChart3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(StatisticsActivity.this, BarChartActivity.class);
                StatisticsActivity.this.startActivity(myIntent);
            }
        });
        BarChart4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(StatisticsActivity.this, BarChartActivity.class);
                StatisticsActivity.this.startActivity(myIntent);
            }
        });
    } }
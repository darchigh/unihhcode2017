package com.liisa.chatbotapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liisa_000 on 14/05/2017.
 */

public class StatisticsActivity  extends AppCompatActivity {
    private PieChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_activity);
        mChart = (PieChart) findViewById(R.id.PieChart);


        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        float range = (float) 2.2;
        entries.add(1,new PieEntry(range, "la"));
        entries.add(2,new PieEntry(range, "la"));
        entries.add(3,new PieEntry(range, "la"));

        PieDataSet dataSet = new PieDataSet(entries, "introduction");

        PieData data = new PieData(dataSet);




    }


}

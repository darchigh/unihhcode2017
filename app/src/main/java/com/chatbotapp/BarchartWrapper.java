package com.chatbotapp;

import com.github.mikephil.charting.data.BarDataSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aossa on 28.10.2017.
 */

public class BarchartWrapper implements Serializable {


    private final ArrayList<String> xAxisCaption = new ArrayList<String>();

    private final Map<String, WrapperBarSet> barsets = new HashMap<String, WrapperBarSet>();

    private final Map<String, List<WrapperBarValue>> data = new HashMap<String, List<WrapperBarValue>>();

    public BarchartWrapper() {
    }


    public void addXAxisCaption(String caption) {
        this.xAxisCaption.add(caption);
    }


    public void addBarset(String dataSet, int color) {
        WrapperBarSet bar = barsets.get(dataSet);

        if (bar == null) {
            bar = new WrapperBarSet();
            bar.setName(dataSet);
            bar.setColor(color);

            barsets.put(dataSet, bar);
        }
    }


    public void addData(String xCaption, String dataSet, float value) {
        WrapperBarValue newBarEntry = new WrapperBarValue();
        newBarEntry.value = value;
        newBarEntry.xAxis = xAxisCaption.indexOf(xCaption);

        List<WrapperBarValue> values = data.get(dataSet);

        if (values == null) {
            values = new ArrayList<>();
            data.put(dataSet, values);
        }

        values.add(newBarEntry);
    }


    public ArrayList<String> getxAxisCaption() {
        return xAxisCaption;
    }

    public Map<String, WrapperBarSet> getBarsets() {
        return barsets;
    }

    public Map<String, List<WrapperBarValue>> getData() {
        return data;
    }


    public class WrapperBarSet implements Serializable {

        private String name;

        private int color;

        private List<WrapperBarValue> data = new ArrayList<WrapperBarValue>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public List<WrapperBarValue> getData() {
            return data;
        }

        public void setData(List<WrapperBarValue> data) {
            this.data = data;
        }
    }


    public class WrapperBarValue implements Serializable {

        private float value;

        private int xAxis;

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public int getxAxis() {
            return xAxis;
        }

        public void setxAxis(int xAxis) {
            this.xAxis = xAxis;
        }
    }

}

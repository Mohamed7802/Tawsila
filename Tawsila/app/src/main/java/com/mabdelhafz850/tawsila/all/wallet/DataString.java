package com.mabdelhafz850.tawsila.all.wallet;

import com.jjoe64.graphview.series.DataPointInterface;

import java.io.Serializable;

public class DataString implements DataPointInterface, Serializable {

    private String x;
    private double y;

    public DataString(String x, double y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public double getX() {
        return 0;
    }

    @Override
    public double getY() {
        return 0;
    }
}

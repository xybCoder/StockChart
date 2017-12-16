package com.mobile.xybcoder.stockchartdemo.mychart;

import android.util.SparseArray;

import com.github.mikephil.charting.components.XAxis;

/**
 * Created by xybcoder on 2016/5/6.
 */
public class MyXAxis extends XAxis {
    private SparseArray<String> labels;
    public SparseArray<String> getXLabels() {
        return labels;
    }
    public void setXLabels(SparseArray<String> labels) {
        this.labels = labels;
    }
}

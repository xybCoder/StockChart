package com.mobile.xybcoder.stockchartdemo.mychart;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.mobile.xybcoder.stockchartdemo.bean.DataParse;


/**
 * @author xybcoder
 * Created by xybcoder on 2016/5/6.
 */
public class MyLineChart extends LineChart {
    private MyLeftMarkerView myLeftMarkerView;
    private MyRightMarkerView myRightMarkerView;
    private MyBottomMarkerView myBottomMarkerView;
    private DataParse minuteHelper;

    public MyLineChart(Context context) {
        super(context);
    }

    public MyLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        /*此两处不能重新示例*/
        mXAxis = new MyXAxis();
        mAxisLeft = new MyYAxis(YAxis.AxisDependency.LEFT);
        mXAxisRenderer = new MyXAxisRenderer(mViewPortHandler, (MyXAxis) mXAxis, mLeftAxisTransformer, this);
        mAxisRendererLeft = new MyYAxisRenderer(mViewPortHandler, (MyYAxis) mAxisLeft, mLeftAxisTransformer);

        mAxisRight = new MyYAxis(YAxis.AxisDependency.RIGHT);
        mAxisRendererRight = new MyYAxisRenderer(mViewPortHandler, (MyYAxis) mAxisRight, mRightAxisTransformer);

    }

    /*返回转型后的左右轴*/
    @Override
    public MyYAxis getAxisLeft() {
        return (MyYAxis) super.getAxisLeft();
    }

    @Override
    public MyXAxis getXAxis() {
        return (MyXAxis) super.getXAxis();
    }


    @Override
    public MyYAxis getAxisRight() {
        return (MyYAxis) super.getAxisRight();
    }

    public void setMarker(MyLeftMarkerView markerLeft, MyRightMarkerView markerRight,MyBottomMarkerView markerBottom, DataParse minuteHelper) {
        this.myLeftMarkerView = markerLeft;
        this.myRightMarkerView = markerRight;
        this.myBottomMarkerView=markerBottom;
        this.minuteHelper = minuteHelper;
    }

    public void setHighlightValue(Highlight h) {
        if (mData == null)
            mIndicesToHighlight = null;
        else {
            mIndicesToHighlight = new Highlight[]{
                    h};
        }
        invalidate();
    }





    @Override
    protected void drawMarkers(Canvas canvas) {

        // if there is no marker view or drawing marker is disabled
        if (myLeftMarkerView == null || myRightMarkerView == null
                || myBottomMarkerView == null || !isDrawMarkersEnabled()
                || !valuesToHighlight())
            return;

        for (int i = 0; i < mIndicesToHighlight.length; i++) {

            Highlight highlight = mIndicesToHighlight[i];

            IDataSet set = mData.getDataSetByIndex(highlight.getDataSetIndex());

            Entry e = mData.getEntryForHighlight(mIndicesToHighlight[i]);
            int entryIndex = set.getEntryIndex(e);

            // make sure entry not null
            if (e == null || entryIndex > set.getEntryCount() * mAnimator.getPhaseX())
                continue;

            float[] pos = getMarkerPosition(highlight);

            // check bounds
            if (!mViewPortHandler.isInBounds(pos[0], pos[1]))
                continue;

            float yValForXIndex1 = minuteHelper.getDatas().get(entryIndex).cjprice;
            float yValForXIndex2 = minuteHelper.getDatas().get(entryIndex).per;
            String time = minuteHelper.getDatas().get(entryIndex).time;
            myLeftMarkerView.setData(yValForXIndex1);
            myRightMarkerView.setData(yValForXIndex2);
            myBottomMarkerView.setData(time);
            myLeftMarkerView.refreshContent(e, mIndicesToHighlight[i]);
            myRightMarkerView.refreshContent(e, mIndicesToHighlight[i]);
            myBottomMarkerView.refreshContent(e, mIndicesToHighlight[i]);
                /*修复bug*/
            // invalidate();
                /*重新计算大小*/
            myLeftMarkerView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            myLeftMarkerView.layout(0, 0, myLeftMarkerView.getMeasuredWidth(),
                    myLeftMarkerView.getMeasuredHeight());
            myRightMarkerView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            myRightMarkerView.layout(0, 0, myRightMarkerView.getMeasuredWidth(),
                    myRightMarkerView.getMeasuredHeight());
            myBottomMarkerView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            myBottomMarkerView.layout(0, 0, myBottomMarkerView.getMeasuredWidth(),
                    myBottomMarkerView.getMeasuredHeight());

            myBottomMarkerView.draw(canvas, pos[0] - myBottomMarkerView.getWidth() / 2, mViewPortHandler.contentBottom());
            myLeftMarkerView.draw(canvas, mViewPortHandler.contentLeft() - myLeftMarkerView.getWidth(), pos[1] - myLeftMarkerView.getHeight() / 2);
            myRightMarkerView.draw(canvas, mViewPortHandler.contentRight(), pos[1] - myRightMarkerView.getHeight() / 2);


        }

    }
}

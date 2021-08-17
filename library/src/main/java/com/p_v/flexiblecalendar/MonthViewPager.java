package com.p_v.flexiblecalendar;

import ohos.app.Context;
import ohos.agp.components.Component;
import ohos.agp.components.AttrSet;

import com.antonyt.infiniteviewpager.InfiniteViewPager;

/**
 * @author p-v
 */
class MonthViewPager extends InfiniteViewPager implements Component.EstimateSizeListener {

    private int rowHeight = 0;
    private int numOfRows;

    public MonthViewPager(Context context) {
        super(context);
    }

    public MonthViewPager(Context context, AttrSet attrs) {
        super(context, attrs);
    }

    void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }

    @Override
    public boolean onEstimateSize(int widthScreen, int heightScreen) {
        boolean wrapHeight = Component.EstimateSpec.getMode(heightScreen) == Component.EstimateSpec.PRECISE;
        int height = getHeight();
        if (wrapHeight && rowHeight == 0) {
            int width = getWidth();
            widthScreen = MeasureSpec.getMeasureSpec(width,EstimateSpec.PRECISE);
            if (getChildCount() > 0) {
                Component firstChild = getComponentAt(0);
                firstChild.estimateSize(widthScreen, MeasureSpec.getMeasureSpec(height,EstimateSpec.NOT_EXCEED));
                height = firstChild.getHeight();
                rowHeight = numOfRows == 6 ? height : (int) Math.ceil(((float) height * 6) / 5);
            }
        }
        heightScreen = MeasureSpec.getMeasureSpec(rowHeight, EstimateSpec.PRECISE);
        this.onEstimateSize(widthScreen,heightScreen);
        System.out.println("VIJAY onEstimateSize");
        return false;
    }
}

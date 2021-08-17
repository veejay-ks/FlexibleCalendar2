package com.p_v.flexiblecalendar.view;

import ohos.agp.components.Component;
import ohos.app.Context;
import ohos.agp.components.AttrSet;

/**
 * Created by p-v on 15/07/15.
 */
public class SquareCellView extends CircularEventCellView implements Component.EstimateSizeListener{

    public SquareCellView(Context context) {
        super(context);
        setListeners();
    }

    public SquareCellView(Context context, AttrSet attrs) {
        super(context, attrs);
        setListeners();
    }

    public SquareCellView(Context context, AttrSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setListeners();
    }

    private void setListeners() {
        setEstimateSizeListener(this);
    }


    public boolean onEstimateSize(int widthMeasureSpec, int heightMeasureSpec) {
        //making sure the cell view is a square
        super.setEstimatedSize(widthMeasureSpec, widthMeasureSpec);
        return true;
    }

}

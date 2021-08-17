package com.p_v.flexiblecalendarexample.slice.widget;

//import android.content.Context;
//import android.util.AttributeSet;
import ohos.agp.components.Component;
import ohos.app.Context;
import ohos.agp.components.AttrSet;

import com.p_v.flexiblecalendar.view.CircularEventCellView;

/**
 * @author p-v
 */
public class ExampleCellView extends CircularEventCellView implements Component.EstimateSizeListener {


    private static final int EXACTLY = 0x40000000;


    public ExampleCellView(Context context) {
        super(context);
    }

    public ExampleCellView(Context context, AttrSet attrs) {
        super(context, attrs);
    }

    public ExampleCellView(Context context, AttrSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
     public boolean onEstimateSize(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getEstimatedWidth();
        int height = (7 * width) / 8;
        heightMeasureSpec = EstimateSpec.getSizeWithMode(height, EstimateSpec.PRECISE);
        super.onEstimateSize(widthMeasureSpec, heightMeasureSpec);
        return true;
    }
}

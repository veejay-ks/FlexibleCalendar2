package com.p_v.flexiblecalendar.view;

import com.p_v.flexiblecalendar.entity.Event;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Rect;
import ohos.app.Context;


import java.util.List;

/**
 * Cell view with the event count
 *
 * @author p-v
 */
public class EventCountCellView extends BaseCellView implements Component.DrawTask, Component.EstimateSizeListener {

    private Paint mPaint;
    private Paint mTextPaint;
    private int mEventCount;
    private int eventCircleY;
    private int eventCircleX;
    private int mTextY;
    public static final String EVENT_RADIUS = "event_count_radius";
    public static final String EVENT_BACKGROUND = "event_background";
    public static final String EVENT_TEXTCOLOR = "event_count_text_color";
    public static final String EVENT_TEXTSIZE = "event_text_size";
    private int eventRadius;
    private Color eventTextColor;
    private Color eventBackground;
    private int eventTextSize;
    public static int DEFAULT_VAL_RADIUS = 15;
    public static Color DEFAULT_VAL_BACKGROUND = Color.BLACK;
    public static Color DEFAULT_VAL_TEXTCOLOR = Color.WHITE;
    public static int DEFAULT_VAL_TEXTSIZE = -1;

    public EventCountCellView(Context context) {
        super(context);
        setListeners();
    }

    public EventCountCellView(Context context, AttrSet attrs) {
        super(context, attrs);
        init(attrs);
        setListeners();
    }

    public EventCountCellView(Context context, AttrSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        setListeners();
    }

    private void setListeners() {
        addDrawTask(this);
        setEstimateSizeListener(this);
    }

    public void init(AttrSet attrs) {
        boolean isRadius = attrs.getAttr(EVENT_RADIUS).isPresent();
        if (isRadius) {
            eventRadius = attrs.getAttr(EVENT_RADIUS).get().getIntegerValue();
        } else {
            eventRadius = DEFAULT_VAL_RADIUS;
        }
        boolean isBG = attrs.getAttr(EVENT_BACKGROUND).isPresent();
        if (isBG) {
            eventBackground = attrs.getAttr(EVENT_BACKGROUND).get().getColorValue();
        } else {
            eventBackground = DEFAULT_VAL_BACKGROUND;
        }
        boolean isTextColor = attrs.getAttr(EVENT_TEXTCOLOR).isPresent();
        if (isTextColor) {
            eventTextColor = attrs.getAttr(EVENT_TEXTCOLOR).get().getColorValue();
        } else {
            eventTextColor = DEFAULT_VAL_TEXTCOLOR;
        }
        boolean isTextSize = attrs.getAttr(EVENT_TEXTSIZE).isPresent();
        if (isTextSize) {
            eventTextSize = attrs.getAttr(EVENT_TEXTSIZE).get().getIntegerValue();
        } else {
            eventTextSize = DEFAULT_VAL_TEXTSIZE;
        }
    }

    @Override
    public void setEvents(List<? extends Event> colorList) {
        if (colorList != null && !colorList.isEmpty()) {
            mEventCount = colorList.size();
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL_STYLE);
            mPaint.setColor(eventBackground);
            invalidate();
            postLayout();//requestLayout();
        }
    }

    @Override
    public boolean onEstimateSize(int i, int i1) {
        if (mEventCount > 0) {
            Paint p = new Paint();
            p.setTextSize(getTextSize());

            Rect rect = new Rect();
            p.getTextBounds("31"); // measuring using fake text

            eventCircleY = (getHeight() - rect.getHeight()) / 4;
            eventCircleX = (3 * getWidth() + rect.getWidth()) / 4;

            mTextPaint = new Paint();
            mTextPaint.setStyle(Paint.Style.FILL_STYLE);
            mTextPaint.setTextSize(eventTextSize == -1 ? getTextSize() / 2 : eventTextSize);
            mTextPaint.setColor(eventTextColor);
            mTextPaint.setTextAlign(2);

            mTextY = eventCircleY + eventRadius / 2;
        }
        return false;
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        if (mEventCount > 0 && mPaint != null && mTextPaint != null) {
            canvas.drawCircle(eventCircleX, eventCircleY, eventRadius, mPaint);
            canvas.drawText(mTextPaint,String.valueOf(mEventCount),eventCircleX,mTextY);
        }
    }
}

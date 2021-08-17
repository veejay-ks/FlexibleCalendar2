package com.p_v.flexiblecalendar.view;

import com.p_v.flexiblecalendar.entity.Event;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.utils.Color;
import ohos.agp.utils.Rect;
import ohos.app.Context;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import ohos.agp.components.Component.DrawTask;

/**
 * @author p-v
 */
public class CircularEventCellView extends BaseCellView implements DrawTask, Component.EstimateSizeListener {

    private int eventCircleY;
    private int radius;
    private int padding;
    private int leftMostPosition = Integer.MIN_VALUE;
    private List<Paint> paintList;
    private int w;
    private int h;
    private int oldw;
    private int oldh;
    public static int CircularEventCellView_event_radius;
    public static int CircularEventCellView_event_circle_padding;
    public static final String CircularEventCellView_event_radius_attr = "state_date_today";
    public static final String CircularEventCellView_event_circle_padding_attr = "state_date_regular";
    public static int DEFAULT_VAL_RAD = 5;
    public static int DEFAULT_VAL_CIR = 1;

    public CircularEventCellView(Context context) {
        super(context);
        setListeners();
    }

    public CircularEventCellView(Context context, AttrSet attrs) {
        super(context, attrs);
        init(attrs);
        setListeners();
    }

    public CircularEventCellView(Context context, AttrSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        setListeners();
    }

    private void setListeners() {
        addDrawTask(this);
        setEstimateSizeListener(this);
    }

    public void init(AttrSet attrs) {
        boolean isEventRadius = attrs.getAttr(CircularEventCellView_event_radius_attr).isPresent();
        if (isEventRadius) {
            CircularEventCellView_event_radius = attrs.getAttr(CircularEventCellView_event_radius_attr).get().getIntegerValue();
        } else {
            CircularEventCellView_event_radius = DEFAULT_VAL_RAD;
        }
        boolean isEventCircle = attrs.getAttr(CircularEventCellView_event_circle_padding_attr).isPresent();
        if (isEventCircle) {
            CircularEventCellView_event_circle_padding = attrs.getAttr(CircularEventCellView_event_circle_padding_attr).get().getIntegerValue();
        } else {
            CircularEventCellView_event_circle_padding = DEFAULT_VAL_CIR;
        }
    }

    private int calculateStartPoint(int offset) {
        return leftMostPosition + offset * (2 * (radius + padding));
    }

    @Override
    public void setEvents(List<? extends Event> colorList) {
        if (colorList != null) {
            paintList = new ArrayList<>(colorList.size());
            for (Event e : colorList) {
                Paint eventPaint = new Paint();
                eventPaint.setStyle(Paint.Style.FILL_STYLE);//FILL is replaced as FILL_STYLE
//                eventPaint.setColor(getContext().getResourceManager().getColor(e.getColor()));
                eventPaint.setColor(Color.GREEN);
                paintList.add(eventPaint);
            }
            invalidate();
            postLayout();//requestLayout();
        }
    }


    @Override
    public void onDraw(Component component, Canvas canvas) {

        Set<Integer> stateSet = getStateSet();

        // draw only if there is no state or just one state i.e. the regular day state
        if ((stateSet == null || stateSet.isEmpty() || (stateSet.size() == 1
                && stateSet.contains(STATE_REGULAR))) && paintList != null) {
            int num = paintList.size();
            for (int i = 0; i < num; i++) {
                canvas.drawCircle(calculateStartPoint(i), eventCircleY, radius, paintList.get(i));
            }
        }
    }

    @Override
    public boolean onEstimateSize(int i, int i1) {
        Set<Integer> stateSet = getStateSet();

        //initialize paint objects only if there is no state or just one state i.e. the regular day state
        if ((stateSet == null || stateSet.isEmpty()
                || (stateSet.size() == 1 && stateSet.contains(STATE_REGULAR))) && paintList != null) {
            int num = paintList.size();

            Paint p = new Paint();
            p.setTextSize(getTextSize());
            Rect rect = new Rect();
            p.getTextBounds("31");
//            p.getTextBounds("31", 0, 1, rect); // measuring using fake text

            eventCircleY = (3 * getHeight() + rect.getHeight()) / 4;

            //calculate left most position for the circle
            if (leftMostPosition == Integer.MIN_VALUE) {
                leftMostPosition = (getWidth() / 2) - (num / 2) * 2 * (padding + radius);
                if (num % 2 == 0) {
                    leftMostPosition = leftMostPosition + radius + padding;
                }
            }

        }

        return false;
    }
}

package com.p_v.flexiblecalendarexample.slice.widget;

//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.util.AttributeSet;

import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.p_v.flexiblecalendarexample.ResourceTable;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Rect;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author p-v
 */
public class ExampleCellView2 extends BaseCellView implements Component.DrawTask, Component.EstimateSizeListener {
    private int eventCircleY;
    private int radius;
    private int padding;
    private int leftMostPosition = Integer.MIN_VALUE;
    private List<Paint> paintList;

    public ExampleCellView2(Context context) {
        super(context);
        init();
    }

    public ExampleCellView2(Context context, AttrSet attrs) {
        super(context, attrs);
        init();
    }

    public ExampleCellView2(Context context, AttrSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /*private void init() {
        radius = (int) getResourceManager().getElement().getDimensionValue(ResourceTable.Dimen.example_cell_view_event_radius);
        padding = (int) getResourceManager().getElement().getDimensionValue(ResourceTable.Dimen.example_cell_view_event_spacing);
    }*/
    private void init()  {
        try {
            radius = (int) getResourceManager().getElement(ResourceTable.Float_example_cell_view_event_radius).getFloat();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotExistException e) {
            e.printStackTrace();
        } catch (WrongTypeException e) {
            e.printStackTrace();
        }
        try {
            padding = (int) getResourceManager().getElement(ResourceTable.Float_example_cell_view_event_spacing).getFloat();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotExistException e) {
            e.printStackTrace();
        } catch (WrongTypeException e) {
            e.printStackTrace();
        }
    }


    //@Override
    protected void onEstimateSize(int w, int h, int oldw, int oldh) {
       // super.onEstimateSize(w, h, oldw, oldh);

        if (paintList != null) {
            int num = paintList.size();

            Paint p = new Paint();
            p.setTextSize(getTextSize());

            Rect rect = new Rect();
            //p.getTextBounds("31", 0, 1, rect); // measuring using fake text

            eventCircleY = (3 * getHeight() + rect.getHeight()) / 4;

            //calculate left most position for the circle
            if (leftMostPosition == Integer.MIN_VALUE) {
                leftMostPosition = (getWidth() / 2) - (num / 2) * 2 * (padding + radius);
                if (num % 2 == 0) {
                    leftMostPosition = leftMostPosition + radius + padding;
                }
            }

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
                //Paint eventPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                eventPaint.setAntiAlias(true);
                eventPaint.setStyle(Paint.Style.FILL_STYLE);
                //eventPaint.setColor(getContext().getResourceManager().getColor(e.getColor()));
                paintList.add(eventPaint);
            }
            invalidate();
            postLayout();
        }
    }

    @Override
    public boolean onEstimateSize(int widthMeasureSpec, int heightMeasureSpec) {
        //for a square cell
//        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        return true;
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        // super.onDraw(canvas);
        if (paintList != null) {
            int num = paintList.size();
            for (int i = 0; i < num; i++) {
                canvas.drawCircle(calculateStartPoint(i), eventCircleY, radius, paintList.get(i));
            }
        }
        
    }
}

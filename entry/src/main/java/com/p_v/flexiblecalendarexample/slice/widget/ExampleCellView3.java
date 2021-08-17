package com.p_v.flexiblecalendarexample.slice.widget;

import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendar.view.BaseCellView;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Component;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ShapeElement;
import ohos.app.Context;
import ohos.agp.components.AttrSet;
import ohos.agp.render.Canvas;

import java.util.List;

/**
 * @author p-v
 */
public class ExampleCellView3 extends BaseCellView implements Component.EstimateSizeListener,Component.DrawTask {

    private boolean hasEvents;

    public ExampleCellView3(Context context) {
        super(context);
        setListeners();
        System.out.println("ExampleCellView3 constructor 1 ");
    }

    public ExampleCellView3(Context context, AttrSet attrs) {
        super(context, attrs);
        setListeners();
        System.out.println("ExampleCellView3 constructor 2 ");
    }

    public ExampleCellView3(Context context, AttrSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setListeners();
        System.out.println("ExampleCellView3 constructor 3 ");
    }

    private void setListeners() {
        addDrawTask(this);
        setEstimateSizeListener(this);
    }

    @Override
    public void setEvents(List<? extends Event> colorList) {
        this.hasEvents = colorList != null && !colorList.isEmpty();
        System.out.println("VEEJAY EXCV3 SETEVENTS "+hasEvents);
        invalidate();
        postLayout();
    }

    @Override
    public boolean onEstimateSize(int i, int i1) {
        super.onEstimateSize(i,i1);
        return true;
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {

        System.out.println("VEEJAY EXCV3 GETSTATE "+getStateSet()+" "+STATE_SELECTED+" "+SELECTED_TODAY+" "+STATE_REGULAR);
        System.out.println("VEEJAY EXCV3 ONDRAW "+!getStateSet().contains(STATE_SELECTED)+" "+!getStateSet().contains(SELECTED_TODAY)+" "+getStateSet().contains(STATE_REGULAR)+" "+hasEvents);
        System.out.println("VEJAY ondraw "+ getWidth());
        if (!getStateSet().contains(STATE_SELECTED) && !getStateSet().contains(SELECTED_TODAY) &&
                getStateSet().contains(STATE_REGULAR) && hasEvents) {
            this.setBackground(getElementFromResourceIdBlue());
        }
        if (getStateSet().contains(STATE_SELECTED) && hasEvents) {
            this.setBackground(this.getElementFromResourceIdRed());
        }
    }

    public Element getElementFromResourceIdBlue() {
        RgbColor rgbColor = new RgbColor(3,36  ,252);
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setRgbColor(rgbColor);
        return shapeElement;
    }

    public Element getElementFromResourceIdRed() {
        RgbColor rgbColor = new RgbColor(230,7  ,29);
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setRgbColor(rgbColor);
        return shapeElement;
    }

}

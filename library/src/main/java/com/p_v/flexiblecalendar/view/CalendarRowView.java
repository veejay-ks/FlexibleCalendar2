package com.p_v.flexiblecalendar.view;

import com.p_v.flexiblecalendar.WeekdayNameDisplayAdapter;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.element.ShapeElement;
import ohos.app.Context;

public class CalendarRowView extends ComponentContainer implements Component.ClickedListener {
    private boolean isHeaderRow;
    private WeekdayNameDisplayAdapter mWeekdayNameDisplayAdapter;

    public CalendarRowView(Context context) {
        super(context);
    }

    public CalendarRowView(Context context, AttrSet attrSet) {
        super(context, attrSet);
    }

    public CalendarRowView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
    }

    @Override
    public void addComponent(Component childComponent) {
        childComponent.setClickedListener(this);
        super.addComponent(childComponent);
    }

    @Override
    public void onClick(Component component) {

    }

    protected void onLayout(int left, int top, int right, int bottom) {
        int cellHeight = bottom - top;
        int cellWidth = right - left;
        int childrenCount = getChildCount();
        for (int i = 0; i < childrenCount; i++) {
            Component child = getComponentAt(i);
            int leftMargin = (i * cellWidth) / 7;
            int rightMargin = ((i + 1) * cellWidth) / 7;
            LayoutConfig layoutConfig = new LayoutConfig();
            layoutConfig.setMargins(leftMargin, 0, rightMargin, cellHeight);
        }
    }

    public void setHeaderRow(boolean headerRow) {
        isHeaderRow = headerRow;
    }

    public void setBackgroundResourceId(int resourceId) {
        for (int i = 0; i < getChildCount(); i++) {
            ShapeElement shapeElement = new ShapeElement();
            RgbColor rgbColor = new RgbColor(resourceId);
            shapeElement.setRgbColor(rgbColor);
            getComponentAt(i).setBackground(shapeElement);
        }
    }

    public void setWeekDayAdapter(WeekdayNameDisplayAdapter weekDayAdapter) {
        mWeekdayNameDisplayAdapter = weekDayAdapter;
    }
}

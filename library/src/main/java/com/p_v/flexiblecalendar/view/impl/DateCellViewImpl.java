package com.p_v.flexiblecalendar.view.impl;

import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.p_v.flexiblecalendar.view.IDateCellViewDrawer;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;

/**
 * Default date cell view drawer
 *
 * @author p-v
 */
public class DateCellViewImpl implements IDateCellViewDrawer {

    private FlexibleCalendarView.CalendarView calendarView;

    public DateCellViewImpl(FlexibleCalendarView.CalendarView calendarView) {
        this.calendarView = calendarView;
    }

    @Override
    public void setCalendarView(FlexibleCalendarView.CalendarView calendarView) {
        this.calendarView = calendarView;
    }

    @Override
    public BaseCellView getCellView(int position, Component convertView, ComponentContainer parent, @BaseCellView.CellType int cellType) {
        System.out.println(" FlexibleCalendarGridAdapter calen "+calendarView);
        return calendarView.getCellView(position, convertView, parent, cellType);
    }
}

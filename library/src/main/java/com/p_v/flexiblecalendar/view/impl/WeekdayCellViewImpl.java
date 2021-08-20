package com.p_v.flexiblecalendar.view.impl;

import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.p_v.flexiblecalendar.view.IWeekCellViewDrawer;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;

/**
 * Default week cell view drawer.
 *
 * @author p-v
 */
public class WeekdayCellViewImpl implements IWeekCellViewDrawer {

    /**
     * calendarview.
     */
    private FlexibleCalendarView.CalendarView calendarView;
    /**
     * arg constructor.
     * @param calendarView calendarview
     */
    public WeekdayCellViewImpl(final FlexibleCalendarView.CalendarView calendarView) {
        this.calendarView = calendarView;
    }
    /**
     * set calendarview calendarview.
     * @param calendarView calendarview
     */
    @Override
    public void setCalendarView(final FlexibleCalendarView.CalendarView calendarView) {
        this.calendarView = calendarView;
    }
    /**
     * get cell view.
     * @return basecellview
     */
    @Override
    public BaseCellView getCellView(final int position, final Component convertView, final ComponentContainer parent) {
        return calendarView.getWeekdayCellView(position, convertView, parent);
    }
    /**
     * get week day name.
     * @param dayOfWeek dayofweek
     * @param defaultValue defaultval
     * @return weekdayname
     */
    @Override
    public String getWeekDayName(final int dayOfWeek, final String defaultValue) {
        return calendarView.getDayOfWeekDisplayValue(dayOfWeek, defaultValue);
    }
}

package com.p_v.flexiblecalendar.view.impl;

import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.p_v.flexiblecalendar.view.IDateCellViewDrawer;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * Default date cell view drawer.
 *
 * @author p-v
 */
public class DateCellViewImpl implements IDateCellViewDrawer {
    /**
     * TYPE.
     */
    private static final int HILOG_TYPE = 3;
    /**
     * DOMAIN.
     */
    private static final int HILOG_DOMAIN = 0xD000F00;
    /**
     * LABEL.
     */
    private static final HiLogLabel LABEL = new HiLogLabel(HILOG_TYPE, HILOG_DOMAIN, "FlexibleCalendar");

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
        HiLog.info(LABEL, " FlexibleCalendarGridAdapter calen " + calendarView);
        return calendarView.getCellView(position, convertView, parent, cellType);
    }
}

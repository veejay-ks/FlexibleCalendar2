package com.p_v.flexiblecalendar.view.impl;

import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.p_v.flexiblecalendar.view.ImplDateCellViewDrawer;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * Default date cell view drawer.
 *
 * @author p-v
 */
public class DateCellViewImpl implements ImplDateCellViewDrawer {
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
    /**
     * calendarview.
     */
    private FlexibleCalendarView.CalendarView calendarView;
    /**
     * arg constructor.
     *
     * @param calendarView calendarview
     */
    public DateCellViewImpl(final FlexibleCalendarView.CalendarView calendarView) {
        this.calendarView = calendarView;
    }

    /**
     * set calendarview.
     *
     * @param calendarView calendarview
     */
    @Override
    public void setCalendarView(final FlexibleCalendarView.CalendarView calendarView) {
        this.calendarView = calendarView;
    }

    /**
     * get cell view.
     * @param position position
     * @param convertView convertview
     * @param parent parent
     * @param cellType celltype
     */
    @Override
    public BaseCellView getCellView(final int position, final Component convertView, final ComponentContainer parent, final @BaseCellView.CellType int cellType) {
        HiLog.info(LABEL, " FlexibleCalendarGridAdapter calen " + calendarView);
        return calendarView.getCellView(position, convertView, parent, cellType);
    }
}

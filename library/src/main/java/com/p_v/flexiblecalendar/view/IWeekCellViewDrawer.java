package com.p_v.flexiblecalendar.view;

import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;

/**
 * @author p-v
 */
public interface IWeekCellViewDrawer extends ICellViewDrawer {
    /**
     * Week Cell view
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    BaseCellView getCellView(int position, Component convertView, ComponentContainer parent);

    /**
     * Display value for the day of week
     *
     * @param dayOfWeek
     * @param defaultValue
     * @return
     */
    String getWeekDayName(int dayOfWeek, String defaultValue);
}

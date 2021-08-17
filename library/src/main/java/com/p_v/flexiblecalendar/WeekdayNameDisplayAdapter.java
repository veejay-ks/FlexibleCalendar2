package com.p_v.flexiblecalendar;

import com.p_v.flexiblecalendar.view.BaseCellView;
import com.p_v.flexiblecalendar.view.IWeekCellViewDrawer;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.render.layoutboost.LayoutBoost;
import ohos.agp.utils.Color;
import ohos.app.Context;

import java.text.DateFormatSymbols;

/**
 * @author p-v
 */
public class WeekdayNameDisplayAdapter extends BaseItemProvider {

    private IWeekCellViewDrawer cellViewDrawer;
    private WeekDay[] weekDayArray;
    private Context mContext;
    private int viewWidth;

    public WeekdayNameDisplayAdapter(Context context, int startDayOfTheWeek, int viewWidth) {
        super();
        mContext = context;
        initializeWeekDays(startDayOfTheWeek);
        this.viewWidth = viewWidth;
    }

    @Override
    public int getCount() {
        return weekDayArray.length;
    }

    @Override
    public Object getItem(int position) {
        return weekDayArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
        System.out.println("VIJAY weekDayList 1 -- "+position);
        BaseCellView cellView = cellViewDrawer.getCellView(position, component, componentContainer);
        System.out.println("VIJAY weekDayList CELLVIEW -- "+cellView);
        if (cellView == null) {
            cellView = (BaseCellView) LayoutBoost.inflate(mContext,ResourceTable.Layout_square_cell_layout,null,false);
        }
        System.out.println("VIJAY weekDayList 2 CELLVIEW -- "+cellView);
        WeekDay weekDay = (WeekDay) getItem(position);
        String weekdayName = cellViewDrawer.getWeekDayName(weekDay.index, weekDay.displayValue); //adding 1 as week days starts from 1 in Calendar
        System.out.println("VIJAY weekDayList100 -- "+weekdayName+" "+weekdayName);
        if ((weekdayName == null) || weekdayName.equals("")) {
            weekdayName = weekDay.displayValue;
        }
        System.out.println("VEJAY cellView.getWidth() -- "+ cellView.getWidth());
        cellView.setTextSize(50);
        cellView.setTextColor(Color.WHITE);
        cellView.setWidth(this.viewWidth);
        cellView.setText(weekdayName);
        return cellView;
    }

    public class WeekDay {
        int index;
        String displayValue;
    }

    private void initializeWeekDays(int startDayOfTheWeek) {
        DateFormatSymbols symbols = new DateFormatSymbols(FlexibleCalendarHelper.getLocale(mContext));
        String[] weekDayList = symbols.getShortWeekdays(); // weekday list has 8 elements
        weekDayArray = new WeekDay[7];
        System.out.println("VIJAY weekDayList "+weekDayList.length);
        //reordering array based on the start day of the week
        for (int i = 1; i < weekDayList.length; i++) {
            WeekDay weekDay = new WeekDay();
            weekDay.index = i;
            weekDay.displayValue = weekDayList[i];
            int tempVal = i - startDayOfTheWeek;
            weekDayArray[tempVal < 0 ? 7 + tempVal : tempVal] = weekDay;
        }
    }

    public boolean isEnabled(int position) {
        return false;
    }

    public void setCellView(IWeekCellViewDrawer cellView) {
        this.cellViewDrawer = cellView;
    }

    public IWeekCellViewDrawer getCellViewDrawer() {
        return cellViewDrawer;
    }

    public void setStartDayOfTheWeek(int startDayOfTheWeek) {
        initializeWeekDays(startDayOfTheWeek);
        this.notifyDataChanged();
    }
}

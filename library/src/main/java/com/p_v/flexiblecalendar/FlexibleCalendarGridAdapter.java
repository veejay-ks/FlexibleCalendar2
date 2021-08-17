package com.p_v.flexiblecalendar;

import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendar.entity.SelectedDateItem;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.p_v.flexiblecalendar.view.IDateCellViewDrawer;

import com.p_v.flexiblecalendar.view.MonthDisplayUtil;
import com.p_v.flexiblecalendar.view.ResourceUtil;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.app.Context;

import ohos.agp.components.Component;


import java.util.Calendar;
import java.util.List;


/**
 * @author p-v
 */
class FlexibleCalendarGridAdapter extends BaseItemProvider {

    private static final int SIX_WEEK_DAY_COUNT = 42;

    private int year;
    private int month;
    private Context context;
    private MonthDisplayUtil monthDisplayUtil;
    private Calendar calendar;
    private OnDateCellItemClickListener onDateCellItemClickListener;
    private SelectedDateItem selectedItem;
    private SelectedDateItem userSelectedDateItem;
    private MonthEventFetcher monthEventFetcher;
    private IDateCellViewDrawer cellViewDrawer;
    private boolean showDatesOutsideMonth;
    private boolean decorateDatesOutsideMonth;
    private boolean disableAutoDateSelection;


    public FlexibleCalendarGridAdapter(Context context, int year, int month,
                                       boolean showDatesOutsideMonth, boolean decorateDatesOutsideMonth, int startDayOfTheWeek,
                                       boolean disableAutoDateSelection) {
        /*super(context);*/
        this.context = context;
        this.showDatesOutsideMonth = showDatesOutsideMonth;
        this.decorateDatesOutsideMonth = decorateDatesOutsideMonth;
        this.disableAutoDateSelection = disableAutoDateSelection;
        initialize(year, month, startDayOfTheWeek);
    }

    /*public FlexibleCalendarGridAdapter(Context context) {
        super(context);
    }*/

    public void initialize(int year, int month, int startDayOfTheWeek) {
        this.year = year;
        this.month = month;
        this.monthDisplayUtil = new MonthDisplayUtil(year, month, startDayOfTheWeek);
        this.calendar = FlexibleCalendarHelper.getLocalizedCalendar(context);
    }

    @Override
    public int getCount() {
        int weekStartDay = monthDisplayUtil.getWeekStartDay();
        int firstDayOfWeek = monthDisplayUtil.getFirstDayOfMonth();
        int diff = firstDayOfWeek - weekStartDay;
        int toAdd = diff < 0 ? (diff + 7) : diff;
        return showDatesOutsideMonth ? SIX_WEEK_DAY_COUNT
                : monthDisplayUtil.getNumberOfDaysInMonth()
                + toAdd;
    }

    @Override
    public Object getItem(int position) {
        //TODO implement
        int row = position / 7;
        int col = position % 7;
        return monthDisplayUtil.getDayAt(row, col);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public BaseCellView getComponent(int position, Component convertView, ComponentContainer parent) {
        int row = position / 7;
        int col = position % 7;
        System.out.println(" FlexibleCalendarGridAdapter row&col "+row+" "+col);
        //checking if is within current month
        boolean isWithinCurrentMonth = monthDisplayUtil.isWithinCurrentMonth(row, col);
        System.out.println(" FlexibleCalendarGridAdapter isWithinCurrentMonth "+isWithinCurrentMonth);
        //compute cell type
        int cellType = BaseCellView.OUTSIDE_MONTH;
        System.out.println(" FlexibleCalendarGridAdapter cellType "+cellType);
        //day at the current row and col
        int day = monthDisplayUtil.getDayAt(row, col);
        int month = monthDisplayUtil.getMonth();
        System.out.println(" FlexibleCalendarGridAdapter day "+day+" "+month);

        if (isWithinCurrentMonth) {
            //set to REGULAR if is within current month
            cellType = BaseCellView.REGULAR;
            System.out.println(" FlexibleCalendarGridAdapter cellType "+cellType);
            System.out.println(" FlexibleCalendarGridAdapter disableAuto "+disableAutoDateSelection);
            if (disableAutoDateSelection) {
                if (userSelectedDateItem != null && userSelectedDateItem.getYear() == year
                        && userSelectedDateItem.getMonth() == month
                        && userSelectedDateItem.getDay() == day) {
                    //selected
                    cellType = BaseCellView.SELECTED;
                }
            } else {
                if (selectedItem != null && selectedItem.getYear() == year
                        && selectedItem.getMonth() == month
                        && selectedItem.getDay() == day) {
                    //selected
                    cellType = BaseCellView.SELECTED;
                }
            }
            System.out.println("getcompo inside FCGA 2.2");
            if (calendar.get(Calendar.YEAR) == year
                    && calendar.get(Calendar.MONTH) == month
                    && calendar.get(Calendar.DAY_OF_MONTH) == day) {
                if (cellType == BaseCellView.SELECTED) {
                    //today and selected
                    cellType = BaseCellView.SELECTED_TODAY;
                } else {
                    //today
                    cellType = BaseCellView.TODAY;
                }
            }
        }
        System.out.println("getcompo inside FCGA 3 empty ");

        BaseCellView cellView = cellViewDrawer.getCellView(position, convertView, parent, cellType);
//        BaseCellView cellView = null;
        System.out.println("getcompo inside FCGA 4");
        if (cellView == null) {
            System.out.println("getcompo inside FCGA 4.1");
            cellView = (BaseCellView) convertView;
            System.out.println("getcompo inside FCGA 4.2 "+cellView);
            if (cellView == null) {
                LayoutScatter inflate = LayoutScatter.getInstance(context);// LayoutInflater inflate = LayoutInflater.from(context);
                cellView = (BaseCellView) inflate.parse(ResourceTable.Layout_square_cell_layout, parent,false);
            }
            System.out.println("getcompo inside FCGA 4.3 "+cellView);
        }
        System.out.println("getcompo inside FCGA 5");
        drawDateCell(cellView, day, cellType);
        System.out.println("getcompo inside FCGA 6 "+cellView);
        return cellView;
    }

    private void drawDateCell(BaseCellView cellView, int day, int cellType) {
        cellView.clearAllStates();
        System.out.println("drawDateCell inside FCGA 1-- "+cellType+" ");
        if (cellType != BaseCellView.OUTSIDE_MONTH) {
            cellView.setText(String.valueOf(day));
            cellView.setTextSize(50);
            cellView.setTextColor(Color.WHITE);
            cellView.setWidth(200);
            /*cellView.setOnClickListener(new DateClickListener(day, month, year));*/
            cellView.setClickedListener(new DateClickListener(day, month, year));

            // add events
            if (monthEventFetcher != null) {
                cellView.setEvents(monthEventFetcher.getEventsForTheDay(year, month, day));
            }
            switch (cellType) {
                case BaseCellView.SELECTED_TODAY:
                    System.out.println("VEEJAY EXCV3 FCGA GETSTATE "+BaseCellView.STATE_TODAY);
                    cellView.addState(BaseCellView.STATE_TODAY);
                    cellView.addState(BaseCellView.STATE_SELECTED);
                    break;
                case BaseCellView.TODAY:
                    System.out.println("VEEJAY EXCV3 FCGA GETSTATE "+BaseCellView.STATE_TODAY);
                    cellView.addState(BaseCellView.STATE_TODAY);
//                    cellView.setBackground(getElementFromResourceIdBlue());
                    break;
                case BaseCellView.SELECTED:
                    cellView.addState(BaseCellView.STATE_SELECTED);
                    break;
                default:
                    cellView.addState(BaseCellView.STATE_REGULAR);
            }
        } else {
            System.out.println("drawDateCell inside FCGA 2 -- "+showDatesOutsideMonth+" ");
            if (showDatesOutsideMonth) {
                cellView.setText(String.valueOf(day));
                cellView.setTextSize(50);
                cellView.setTextColor(Color.WHITE);
                cellView.setWidth(200);
                int[] temp = new int[2];
                //date outside month and less than equal to 12 means it belongs to next month otherwise previous

                if (day <= 12) {
                    FlexibleCalendarHelper.nextMonth(year, month, temp);
                } else {
                    FlexibleCalendarHelper.previousMonth(year, month, temp);

                }
                cellView.setClickedListener(new DateClickListener(day, temp[1], temp[0]));

                if (decorateDatesOutsideMonth && monthEventFetcher != null) {
                    cellView.setEvents(monthEventFetcher.getEventsForTheDay(temp[0], temp[1], day));
                }

                cellView.addState(BaseCellView.STATE_OUTSIDE_MONTH);
            } else {
                System.out.println("drawDateCell inside FCGA inside null ");
                Element element = ResourceUtil.getElementFromResourceId(ohos.global.systemres.ResourceTable.Color_id_color_activated_transparent);
//                cellView.setBackground(element);//cellView.setBackgroundResource(ohos.R.color.transparent);
                cellView.setText(null);
                cellView.setTextSize(50);
                cellView.setTextColor(Color.WHITE);
                cellView.setWidth(200);
                cellView.setClickedListener(null);//cellView.setOnClickListener(null);

            }
        }
//        cellView.refreshDrawableState();
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public void setOnDateClickListener(OnDateCellItemClickListener onDateCellItemClickListener) {
        this.onDateCellItemClickListener = onDateCellItemClickListener;
    }

    public void setSelectedItem(SelectedDateItem selectedItem, boolean notify, boolean isUserSelected) {
        this.selectedItem = selectedItem;
        if (disableAutoDateSelection && isUserSelected) {
            this.userSelectedDateItem = selectedItem;
        }
        if (notify) notifyDataChanged();//if (notify) notifyDataSetChanged();
    }

    public SelectedDateItem getSelectedItem() {
        return selectedItem;
    }

    void setMonthEventFetcher(MonthEventFetcher monthEventFetcher) {
        this.monthEventFetcher = monthEventFetcher;
    }

    public void setCellViewDrawer(IDateCellViewDrawer cellViewDrawer) {
        this.cellViewDrawer = cellViewDrawer;
    }

    public void setShowDatesOutsideMonth(boolean showDatesOutsideMonth) {
        this.showDatesOutsideMonth = showDatesOutsideMonth;
        this.notifyDataChanged();//this.notifyDataSetChanged();
    }

    public void setDecorateDatesOutsideMonth(boolean decorateDatesOutsideMonth) {
        this.decorateDatesOutsideMonth = decorateDatesOutsideMonth;
        this.notifyDataChanged();//this.notifyDataSetChanged();
    }

    public void setDisableAutoDateSelection(boolean disableAutoDateSelection) {
        this.disableAutoDateSelection = disableAutoDateSelection;
        this.notifyDataChanged();//this.notifyDataSetChanged();
    }

    public void setFirstDayOfTheWeek(int firstDayOfTheWeek) {
        monthDisplayUtil = new MonthDisplayUtil(year, month, firstDayOfTheWeek);
        this.notifyDataChanged();//this.notifyDataSetChanged();
    }

    public SelectedDateItem getUserSelectedItem() {
        return userSelectedDateItem;
    }

    public void setUserSelectedDateItem(SelectedDateItem selectedItem) {
        this.userSelectedDateItem = selectedItem;
        notifyDataChanged();//notifyDataSetChanged();
    }

    public interface OnDateCellItemClickListener {
        void onDateClick(SelectedDateItem selectedItem);
    }

    interface MonthEventFetcher {
        List<? extends Event> getEventsForTheDay(int year, int month, int day);
    }
    //    private class DateClickListener implements Component.OnClickListener {
    private class DateClickListener implements Component.ClickedListener {

        private int iDay;
        private int iMonth;
        private int iYear;

        public DateClickListener(int day, int month, int year) {
            this.iDay = day;
            this.iMonth = month;
            this.iYear = year;
        }

        /*@Override*/
        public void onClick(final Component v) {
            selectedItem = new SelectedDateItem(iYear, iMonth, iDay);

            if (disableAutoDateSelection) {
                userSelectedDateItem = selectedItem;
            }

            notifyDataChanged();//notifyDataSetChanged();

            if (onDateCellItemClickListener != null) {
                onDateCellItemClickListener.onDateClick(selectedItem);
            }

        }
    }

}

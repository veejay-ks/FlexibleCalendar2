package com.p_v.flexiblecalendar;

import com.p_v.flexiblecalendar.view.CalendarRowView;
import com.p_v.flexiblecalendar.view.ResourceUtil;
import ohos.agp.components.*;
import ohos.agp.utils.Color;
import ohos.app.Context;

import com.antonyt.infiniteviewpager.InfinitePagerAdapter;
import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendar.entity.SelectedDateItem;
import com.p_v.flexiblecalendar.exception.HighValueException;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.p_v.flexiblecalendar.view.impl.DateCellViewImpl;
import com.p_v.flexiblecalendar.view.impl.WeekdayCellViewImpl;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A Flexible calendar view
 *
 * @author p-v
 */
public class FlexibleCalendarView extends DirectionalLayout implements
        FlexibleCalendarGridAdapter.OnDateCellItemClickListener,
        FlexibleCalendarGridAdapter.MonthEventFetcher, Component.EstimateSizeListener {


    /*
     * Direction Constants
     */
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    private static final int HIGH_VALUE = 20000;
    private InfinitePagerAdapter monthInfPagerAdapter;
    private WeekdayNameDisplayAdapter weekdayDisplayAdapter;
    private MonthViewPagerAdapter monthViewPagerAdapter;
    private Context context;
    /**
     * View pager for the month view
     */
    private MonthViewPager monthViewPager;
    private ListContainer weekDisplayView;
    private OnMonthChangeListener onMonthChangeListener;
    private OnDateClickListener onDateClickListener;
    private EventDataProvider eventDataProvider;
    private CalendarView calendarView;
    private int displayYear;
    private int displayMonth;
    private int startDisplayDay;
    private int weekdayHorizontalSpacing;
    private int weekdayVerticalSpacing;
    private int monthDayHorizontalSpacing;
    private int monthDayVerticalSpacing;
    private int monthViewBackground;
    private int weekViewBackground;
    int rowHeight = 0;
    int numOfRows = 0;
    private boolean showDatesOutsideMonth;
    private boolean decorateDatesOutsideMonth;
    private boolean disableAutoDateSelection;
    /**
     * Reset adapters flag used internally
     * for tracking go to current month
     */
    private boolean resetAdapters;
    /**
     * Currently selected date item
     */
    private SelectedDateItem selectedDateItem;
    private SelectedDateItem userSelectedItem;
    /**
     * Internal flag to override the computed date on month change
     */
    private boolean shouldOverrideComputedDate;
    /**
     * First day of the week in the calendar
     */
    private int startDayOfTheWeek;
    private int lastPosition;

    public FlexibleCalendarView(Context context) {
        super(context);
        this.context = context;
        System.out.println("FSC: constructor1");
    }

    public FlexibleCalendarView(Context context, AttrSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
        System.out.println("FSC: constructor2");
    }

    public FlexibleCalendarView(Context context, AttrSet attrs, String defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
        System.out.println("FSC: constructor3");
    }

    private void init(AttrSet attrs) {
        setAttributes(attrs);
        setOrientation(VERTICAL);
        setEstimateSizeListener(this);

        //initialize the default calendar view
        calendarView = new DefaultCalendarView();

        //create week view header
        weekDisplayView = new ListContainer(context);
        TableLayoutManager tableLayoutManager = new TableLayoutManager();
        tableLayoutManager.setColumnCount(7);
        tableLayoutManager.setRowCount(7);
        weekDisplayView.setLayoutManager(tableLayoutManager);
        weekDisplayView.verifyLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT));
//        weekDisplayView.setBackgroundResourceId(weekViewBackground);
        weekdayDisplayAdapter = new WeekdayNameDisplayAdapter(getContext(), startDayOfTheWeek, 200);
        //setting default week cell view
        weekdayDisplayAdapter.setCellView(new WeekdayCellViewImpl(calendarView));
        weekDisplayView.setItemProvider(weekdayDisplayAdapter);
        System.out.println("FSC: weekDisplayView.setWeekDayAdapter");
        this.addComponent(weekDisplayView);
        System.out.println("FSC: addComponent");

        //setup month view
        monthViewPager = new MonthViewPager(context);

//        monthViewPager.setBackground(ResourceUtil.getElementFromResourceId(monthViewBackground));

        numOfRows = showDatesOutsideMonth ? 6 : FlexibleCalendarHelper.getNumOfRowsForTheMonth(displayYear, displayMonth, startDayOfTheWeek);
        System.out.println(" FlexibleCalendarGridAdapter displaymonth "+displayMonth+" "+displayYear+" "+startDayOfTheWeek+" "+startDisplayDay);
        monthViewPager.setNumOfRows(showDatesOutsideMonth ? 6 : FlexibleCalendarHelper.getNumOfRowsForTheMonth(displayYear, displayMonth, startDayOfTheWeek));
        monthViewPagerAdapter = new MonthViewPagerAdapter(context, displayYear, displayMonth, this,
                showDatesOutsideMonth, decorateDatesOutsideMonth, startDayOfTheWeek, disableAutoDateSelection);
        monthViewPagerAdapter.setMonthEventFetcher(this);
        monthViewPagerAdapter.setSpacing(monthDayHorizontalSpacing, monthDayVerticalSpacing);

        //set the default cell view
        monthViewPagerAdapter.setCellViewDrawer(new DateCellViewImpl(calendarView));

        monthInfPagerAdapter = new InfinitePagerAdapter(monthViewPagerAdapter, context);
        //Initializing with the offset value
        lastPosition = monthInfPagerAdapter.getRealCount() * 100;
        monthViewPager.setProvider(monthInfPagerAdapter);
        monthViewPager.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT,
                ComponentContainer.LayoutConfig.MATCH_CONTENT));
        monthViewPager.addPageChangedListener(new MonthChangeListener());
        monthViewPager.setWidth(ComponentContainer.LayoutConfig.MATCH_PARENT);
        monthViewPager.setHeight(ComponentContainer.LayoutConfig.MATCH_PARENT);
        System.out.println("FSC: monthViewPager w&h -- "+monthViewPager.getWidth()+" "+monthViewPager.getHeight());
        //initialize with the current selected item
        selectedDateItem = new SelectedDateItem(displayYear, displayMonth, startDisplayDay);
        monthViewPagerAdapter.setSelectedItem(selectedDateItem);
        System.out.println(" FlexibleCalendarGridAdapter displaymonth second "+displayMonth+" "+displayYear+" "+startDayOfTheWeek+" "+startDisplayDay);
        this.addComponent(monthViewPager);
    }


    private void setAttributes(AttrSet attrs) {
        Calendar cal = Calendar.getInstance(FlexibleCalendarHelper.getLocale(context));
//        displayMonth = attrs.getAttr("startDisplayMonth").isPresent() ?
//                attrs.getAttr("startDisplayMonth").get().getIntegerValue() : 1;
        System.out.println(" FlexibleCalendarGridAdapter displaymonth "+cal.get(Calendar.MONTH));
        displayMonth = cal.get(Calendar.MONTH);
//        displayYear = attrs.getAttr("startDisplayYear").isPresent() ?
//                attrs.getAttr("startDisplayYear").get().getIntegerValue() : 1;
        displayYear = cal.get(Calendar.YEAR);
        startDisplayDay = cal.get(Calendar.DAY_OF_MONTH);

        weekdayHorizontalSpacing = attrs.getAttr("weekDayHorizontalSpacing").isPresent() ?
                attrs.getAttr("weekDayHorizontalSpacing").get().getDimensionValue() : 5;
        weekdayVerticalSpacing = attrs.getAttr("weekDayVerticalSpacing").isPresent() ?
                attrs.getAttr("weekDayVerticalSpacing").get().getDimensionValue() : 5;
        monthDayHorizontalSpacing = attrs.getAttr("monthDayHorizontalSpacing").isPresent() ?
                attrs.getAttr("monthDayHorizontalSpacing").get().getDimensionValue() : 5;
        monthDayVerticalSpacing = attrs.getAttr("monthDayVerticalSpacing").isPresent() ?
                attrs.getAttr("monthDayVerticalSpacing").get().getDimensionValue() : 5;

        monthViewBackground = attrs.getAttr("monthViewBackground").isPresent() ?
                attrs.getAttr("monthViewBackground").get().getIntegerValue() : 0;
        weekViewBackground = attrs.getAttr("weekViewBackground").isPresent() ?
                attrs.getAttr("weekViewBackground").get().getIntegerValue() : 0;

        showDatesOutsideMonth = attrs.getAttr("showDatesOutsideMonth").isPresent() ?
                attrs.getAttr("showDatesOutsideMonth").get().getBoolValue() : false;
        decorateDatesOutsideMonth = attrs.getAttr("decorateDatesOutsideMonth").isPresent() ?
                attrs.getAttr("decorateDatesOutsideMonth").get().getBoolValue() : false;
        disableAutoDateSelection = attrs.getAttr("disableAutoDateSelection").isPresent() ?
                attrs.getAttr("disableAutoDateSelection").get().getBoolValue() : false;

        startDayOfTheWeek = attrs.getAttr("startDayOfTheWeek").isPresent() ?
                attrs.getAttr("startDayOfTheWeek").get().getIntegerValue() : 1;
        if (startDayOfTheWeek < 1 || startDayOfTheWeek > 7) {
            // setting the start day to sunday in case of invalid input
            startDayOfTheWeek = Calendar.SUNDAY;
        }
    }

    /**
     * Expand the view with animation
     */
    public void expand() {
        estimateSize(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);
        final int targetHeight = getEstimatedHeight();

        getLayoutConfig().height = 0;
        setVisibility(Component.VISIBLE);
        /*Animator a = new Animator() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                getLayoutConfig().height = interpolatedTime == 1
                        ? ComponentContainer.LayoutConfig.MATCH_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                postLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(((int) (targetHeight / getContext().getResourceManager().getDisplayMetrics().density)));
        startAnimation(a);*/
    }

    /**
     * Collapse the view with animation
     */
    public void collapse() {
        final int initialHeight = this.getEstimatedHeight();
        /*Animator a = new Animator() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    setVisibility(Component.GONE);
                } else {
                    getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(((int) (initialHeight / getContext().getResources().getDisplayMetrics().density)));
        startAnimation(a);*/
    }

    public void setOnMonthChangeListener(OnMonthChangeListener onMonthChangeListener) {
        this.onMonthChangeListener = onMonthChangeListener;
    }

    public void setOnDateClickListener(OnDateClickListener onDateClickListener) {
        this.onDateClickListener = onDateClickListener;
    }

    public void setEventDataProvider(EventDataProvider eventDataProvider) {
        this.eventDataProvider = eventDataProvider;
    }

    /* /**
      * Set the start display year and month
      * @param year  start year to display
      * @param month  start month to display
      *//*
    public void setStartDisplay(int year,int month){
        //TODO revisit there is something wrong going here things are not selected
        this.displayYear = year;
        this.displayMonth = month;
        invalidate();
        requestLayout();
    }
*/
    @Override
    public void onDateClick(SelectedDateItem selectedItem) {
        if (selectedDateItem.getYear() != selectedItem.getYear() || selectedDateItem.getMonth() != selectedItem.getMonth()) {
            shouldOverrideComputedDate = true;
            //different month
            int monthDifference = FlexibleCalendarHelper.getMonthDifference(selectedItem.getYear(), selectedItem.getMonth(),
                    selectedDateItem.getYear(), selectedDateItem.getMonth());
            this.selectedDateItem = selectedItem;
            //move back or forth based on the monthDifference
            if (monthDifference > 0) {
                moveToPreviousMonth();
            } else {
                moveToNextMonth();
            }
        } else {
            //do nothing if same month
            this.selectedDateItem = selectedItem;
        }

        // redraw current month grid as the events were getting disappeared for selected day
        redrawMonthGrid(lastPosition % MonthViewPagerAdapter.VIEWS_IN_PAGER);

        // set user selected date item
        if (disableAutoDateSelection) {
            this.userSelectedItem = selectedItem.clone();
        }

        if (onDateClickListener != null) {
            onDateClickListener.onDateClick(selectedItem.getYear(), selectedItem.getMonth(), selectedItem.getDay());
        }
    }

    private void redrawMonthGrid(int position) {
        if (position == -1) {
            //redraw all
            for (int i = 0; i <= 3; i++) {
                Component view = monthViewPager.findComponentById(Integer.parseInt(MonthViewPagerAdapter.GRID_TAG_PREFIX + i));
                reAddAdapter(view);
            }
        } else {
            Component view = monthViewPager.findComponentById(Integer.parseInt(MonthViewPagerAdapter.GRID_TAG_PREFIX + position));
            reAddAdapter(view);
        }
    }

    private void reAddAdapter(Component view) {
        if (view != null) {
            BaseItemProvider adapter = ((ListContainer) view).getItemProvider();
            ((ListContainer) view).setItemProvider(adapter);
        }
    }

    /**
     * @return currently selected date
     */
    public SelectedDateItem getSelectedDateItem() {
        if (disableAutoDateSelection) {
            return userSelectedItem == null ? null : userSelectedItem.clone();
        }
        return selectedDateItem.clone();
    }

    public int getCurrentMonth() {
        return selectedDateItem.getMonth();
    }

    public int getCurrentYear() {
        return selectedDateItem.getYear();
    }

    /**
     * Move the selection to the next day
     */
    public void moveToPreviousDate() {
        // in case when auto selection is disabled
        // do nothing if there is nothing selected by the user
        if (disableAutoDateSelection && userSelectedItem == null) return;

        if (selectedDateItem != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(selectedDateItem.getYear(), selectedDateItem.getMonth(), selectedDateItem.getDay());
            cal.add(Calendar.DATE, -1);

            if (selectedDateItem.getMonth() != cal.get(Calendar.MONTH)) {
                //update selected date item
                selectedDateItem.setDay(cal.get(Calendar.DAY_OF_MONTH));
                selectedDateItem.setMonth(cal.get(Calendar.MONTH));
                selectedDateItem.setYear(cal.get(Calendar.YEAR));

                //set true to override the computed date in onPageSelected method
                shouldOverrideComputedDate = true;

                //scroll to previous month
                moveToPreviousMonth();
            } else {
                selectedDateItem.setDay(cal.get(Calendar.DAY_OF_MONTH));
                selectedDateItem.setMonth(cal.get(Calendar.MONTH));
                selectedDateItem.setYear(cal.get(Calendar.YEAR));
                monthViewPagerAdapter.setSelectedItem(selectedDateItem);
            }
        }
    }

    /**
     * Move the selection to the previous day
     */
    public void moveToNextDate() {
        // in case when auto selection is disabled
        // do nothing if there is nothing selected by the user
        if (disableAutoDateSelection && userSelectedItem == null) return;

        if (selectedDateItem != null) {

            Calendar cal = Calendar.getInstance();
            cal.set(selectedDateItem.getYear(), selectedDateItem.getMonth(), selectedDateItem.getDay());
            cal.add(Calendar.DATE, 1);

            if (selectedDateItem.getMonth() != cal.get(Calendar.MONTH)) {
                moveToNextMonth();
            } else {
                selectedDateItem.setDay(cal.get(Calendar.DAY_OF_MONTH));
                selectedDateItem.setMonth(cal.get(Calendar.MONTH));
                selectedDateItem.setYear(cal.get(Calendar.YEAR));
                monthViewPagerAdapter.setSelectedItem(selectedDateItem);
            }
        }
    }

    @Override
    public List<? extends Event> getEventsForTheDay(int year, int month, int day) {
        return eventDataProvider == null ?
                null : eventDataProvider.getEventsForTheDay(year, month, day);
    }

    /**
     * Set the customized calendar view for the calendar for customizing cells
     * and layout
     *
     * @param calendar
     */
    public void setCalendarView(CalendarView calendar) {
        this.calendarView = calendar;
        monthViewPagerAdapter.getCellViewDrawer().setCalendarView(calendarView);
        weekdayDisplayAdapter.getCellViewDrawer().setCalendarView(calendarView);
    }

    /**
     * Set the background resource for week view
     *
     * @param resourceId
     */
//    public void setWeekViewBackgroundResource( int resourceId) {
//        this.weekViewBackground = resourceId;
//        weekDisplayView.setBackgroundResourceId(resourceId);
//    }

    /**
     * Set background resource for the month view
     *
     * @param resourceId
     */
    public void setMonthViewBackgroundResource( int resourceId) {
        this.monthViewBackground = resourceId;
        monthViewPager.setBackground(ResourceUtil.getElementFromResourceId(resourceId));
    }

    /**
     * sets weekview header horizontal spacing between weekdays
     *
     * @param spacing
     */
    public void setWeekViewHorizontalSpacing(int spacing) {
        this.weekdayHorizontalSpacing = spacing;
//        weekDisplayView.setHorizontalSpacing(weekdayHorizontalSpacing);
    }

    /**
     * Sets the weekview header vertical spacing between weekdays
     *
     * @param spacing
     */
    public void setWeekViewVerticalSpacing(int spacing) {
        this.weekdayVerticalSpacing = spacing;
//        weekDisplayView.setVerticalSpacing(weekdayVerticalSpacing);
    }

    /**
     * Sets the month view cells horizontal spacing
     *
     * @param spacing
     */
    public void setMonthViewHorizontalSpacing(int spacing) {
        this.monthDayHorizontalSpacing = spacing;
        monthViewPagerAdapter.setSpacing(monthDayHorizontalSpacing, monthDayVerticalSpacing);
    }

    /**
     * Sets the month view cells vertical spacing
     *
     * @param spacing
     */
    public void setMonthViewVerticalSpacing(int spacing) {
        this.monthDayVerticalSpacing = spacing;
        monthViewPagerAdapter.setSpacing(monthDayHorizontalSpacing, monthDayVerticalSpacing);
    }

    /**
     * move to next month
     */
    public void moveToNextMonth() {
        moveToPosition(1);
    }

    /**
     * move to position with respect to current position
     * for internal use
     */
    private void moveToPosition(int position) {
        monthViewPager.setCurrentPage(lastPosition + position - monthInfPagerAdapter.getRealCount() * 100, true);
    }

    /**
     * move to previous month
     */
    public void moveToPreviousMonth() {
        moveToPosition(-1);
    }

    /**
     * move the position to the current month
     */
    public void goToCurrentMonth() {
        //check has to go left side or right
        System.out.println("monthDifference "+displayYear+" "+displayMonth);
        int monthDifference = FlexibleCalendarHelper
                .getMonthDifference(displayYear, displayMonth);
        System.out.println("monthDifference "+monthDifference+" "+lastPosition);
        if (monthDifference != 0) {
            resetAdapters = true;
            if (monthDifference < 0) {
                //set fake count to avoid freezing in InfiniteViewPager as it iterates to Integer.MAX_VALUE
//                monthInfPagerAdapter.setFakeCount(lastPosition);
                monthInfPagerAdapter.notifyDataChanged();
            }
            moveToPosition(monthDifference);
        }
    }

    /**
     * move the position to today's date
     */
    public void goToCurrentDay() {
        //check has to go left side or right
        int monthDifference = FlexibleCalendarHelper
                .getMonthDifference(displayYear, displayMonth);

        //current date
        Calendar cal = Calendar.getInstance();
        //update selected date item
        selectedDateItem.setDay(cal.get(Calendar.DAY_OF_MONTH));
        selectedDateItem.setMonth(cal.get(Calendar.MONTH));
        selectedDateItem.setYear(cal.get(Calendar.YEAR));

        if (disableAutoDateSelection) {
            this.userSelectedItem = selectedDateItem.clone();
        }

        if (monthDifference != 0) {
            resetAdapters = true;
            if (monthDifference < 0) {
                //set fake count to avoid freezing in InfiniteViewPager as it iterates to Integer.MAX_VALUE
                monthInfPagerAdapter.setFakeCount(lastPosition);
                monthInfPagerAdapter.notifyDataChanged();
            }
            //set true to override the computed date in onPageSelected method
            shouldOverrideComputedDate = true;
            moveToPosition(monthDifference);
        } else {
            FlexibleCalendarGridAdapter currentlyVisibleAdapter = monthViewPagerAdapter
                    .getMonthAdapterAtPosition(lastPosition % MonthViewPagerAdapter.VIEWS_IN_PAGER);
            currentlyVisibleAdapter.notifyDataChanged();
        }
    }

    /**
     * Get the show dates outside month flag
     *
     * @return true if showDatesOutsideMonth is enable, false otherwise
     */
    public boolean getShowDatesOutsideMonth() {
        return showDatesOutsideMonth;
    }

    /**
     * Flag to show dates outside the month. Default value is false which will show only the dates
     * within the month
     *
     * @param showDatesOutsideMonth set true to show the dates outside month
     */
    public void setShowDatesOutsideMonth(boolean showDatesOutsideMonth) {
        this.showDatesOutsideMonth = showDatesOutsideMonth;
        monthViewPager.setNumOfRows(showDatesOutsideMonth ? 6 : FlexibleCalendarHelper.getNumOfRowsForTheMonth(displayYear, displayMonth, startDayOfTheWeek));
        monthViewPager.invalidate();
        monthViewPagerAdapter.setShowDatesOutsideMonth(showDatesOutsideMonth);
    }

    /**
     * Get the decorate dates outside month flag
     *
     * @return true if the decorateDatesOutsideMonth is enabled, false otherwise
     */
    public boolean getDecorateDatesOutsideMonth() {
        return decorateDatesOutsideMonth;
    }

    /**
     * Flag to decorate dates outside the month. Default value is false which will only decorate
     * dates within the month
     *
     * @param decorateDatesOutsideMonth set true to decorate the dates outside month
     */
    public void setDecorateDatesOutsideMonth(boolean decorateDatesOutsideMonth) {
        this.decorateDatesOutsideMonth = decorateDatesOutsideMonth;
        monthViewPager.invalidate();
        monthViewPagerAdapter.setDecorateDatesOutsideMonth(decorateDatesOutsideMonth);
    }

    /**
     * Get the disable auto date selection flag
     *
     * @return true if disableAutoDateSelection is enabled
     */
    public boolean isDisableAutoDateSelection() {
        return disableAutoDateSelection;
    }

    /**
     * Disable auto selection of the first day of the month
     *
     * @param disableAutoDateSelection true to disable the auto selection
     */
    public void setDisableAutoDateSelection(boolean disableAutoDateSelection) {
        this.disableAutoDateSelection = disableAutoDateSelection;
        monthViewPager.invalidate();
        monthViewPagerAdapter.setDisableAutoDateSelection(disableAutoDateSelection);
    }

    /**
     * Refresh the calendar view. Invalidate and redraw all the cells
     */
    public void refresh() {
        redrawMonthGrid(-1);
    }

    /**
     * @return start day of the week
     */
    public int getStartDayOfTheWeek() {
        return startDayOfTheWeek;
    }

    /**
     * <p>Set the start day of week.</p>
     * <p>
     * SUNDAY = 1,
     * MONDAY = 2,
     * TUESDAY = 3,
     * WEDNESDAY = 4,
     * THURSDAY = 5,
     * FRIDAY = 6,
     * SATURDAY = 7
     *
     * @param startDayOfTheWeek Add values between 1 to 7. Defaults to 1 if entered outside boundary
     */
    public void setStartDayOfTheWeek(int startDayOfTheWeek) {
        this.startDayOfTheWeek = startDayOfTheWeek;
        if (startDayOfTheWeek < 1 || startDayOfTheWeek > 7) {
            startDayOfTheWeek = 1;
        }
        monthViewPagerAdapter.setStartDayOfTheWeek(startDayOfTheWeek);
        weekdayDisplayAdapter.setStartDayOfTheWeek(startDayOfTheWeek);
    }

    /**
     * Select the date in the FlexibleCalendar
     *
     * @param date
     */
    public void selectDate(Date date) {
        if (date == null) return;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        selectDate(calendar);
    }

    /**
     * Select the date in the FlexibleCalendar
     *
     * @param calendar
     */
    public void selectDate(Calendar calendar) {
        if (calendar == null) return;

        selectDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

    }

    /**
     * Select the date in the FlexibleCalendar
     *
     * @param newYear
     * @param newMonth
     * @param newDay
     */
    public void selectDate(int newYear, int newMonth, int newDay) {
        int monthDifference = FlexibleCalendarHelper
                .getMonthDifference(selectedDateItem.getYear(), selectedDateItem.getMonth(),
                        newYear, newMonth);

        if (Math.abs(monthDifference) > HIGH_VALUE) {
            //throw exception for high values
            throw new HighValueException("Difference too high to make the change");
        }

        selectedDateItem.setDay(newDay);
        selectedDateItem.setMonth(newMonth);
        selectedDateItem.setYear(newYear);

        if (disableAutoDateSelection) {
            this.userSelectedItem = selectedDateItem.clone();
        }

        if (monthDifference != 0) {
            //different month
            resetAdapters = true;
            if (monthDifference < 0) {
                //set fake count to avoid freezing in InfiniteViewPager as it iterates to Integer.MAX_VALUE
                monthInfPagerAdapter.setFakeCount(lastPosition);
                monthInfPagerAdapter.notifyDataChanged();
            }
            //set true to override the computed date in onPageSelected method
            shouldOverrideComputedDate = true;
            moveToPosition(monthDifference);
            // select the user selected date item
            if (disableAutoDateSelection) {
                monthViewPagerAdapter
                        .getMonthAdapterAtPosition(lastPosition % MonthViewPagerAdapter.VIEWS_IN_PAGER)
                        .setSelectedItem(selectedDateItem, true, true);
            }
        } else {
            monthViewPagerAdapter
                    .getMonthAdapterAtPosition(lastPosition % MonthViewPagerAdapter.VIEWS_IN_PAGER)
                    .setSelectedItem(selectedDateItem, true, true);
        }

    }

    /**
     * Customize Calendar using this interface
     */
    public interface CalendarView {
        /**
         * Cell view for the month
         *
         * @param position
         * @param convertView
         * @param parent
         * @param cellType
         * @return
         */
        BaseCellView getCellView(int position, Component convertView, ComponentContainer parent, @BaseCellView.CellType int cellType);

        /**
         * Cell view for the weekday in the header
         *
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        BaseCellView getWeekdayCellView(int position, Component convertView, ComponentContainer parent);

        /**
         * Get display value for the day of week
         *
         * @param dayOfWeek    the value of day of week where 1 is SUNDAY, 2 is MONDAY ... 7 is SATURDAY
         * @param defaultValue the default value for the day of week
         * @return
         */
        String getDayOfWeekDisplayValue(int dayOfWeek, String defaultValue);
    }

    /**
     * Event Data Provider used for displaying events for a particular date
     */
    public interface EventDataProvider {
        List<? extends Event> getEventsForTheDay(int year, int month, int day);
    }

    /**
     * Listener for month change.
     */
    public interface OnMonthChangeListener {
        /**
         * Called whenever there is a month change
         *
         * @param year      the selected month's year
         * @param month     the selected month
         * @param direction LEFT or RIGHT
         */
        void onMonthChange(int year, int month, @Direction int direction);
    }

    /**
     * Click listener for date cell
     */
    public interface OnDateClickListener {
        /**
         * Called whenever a date cell is clicked
         *
         * @param day   selected day
         * @param month selected month
         * @param year  selected year
         */
        void onDateClick(int year, int month, int day);
    }

    /**
     * Direction for movement of FlexibleCalendarView left or right
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface Direction {
    }

    /**
     * Default calendar view for internal usage
     */
    private class DefaultCalendarView implements CalendarView {

        @Override
        public BaseCellView getCellView(int position, Component convertView, ComponentContainer parent,
                                        int cellType) {
            BaseCellView cellView = (BaseCellView) convertView;
            System.out.println("getcompo inside FCGA 3 cont "+cellView);
            if (cellView == null) {
                LayoutScatter scatter = LayoutScatter.getInstance(context);
                cellView = (BaseCellView) scatter.parse(ResourceTable.Layout_square_cell_layout, parent, false);
            }
            System.out.println("getcompo inside FCGA 3 cont 2 "+cellView);
            return cellView;
        }

        @Override
        public BaseCellView getWeekdayCellView(int position, Component convertView, ComponentContainer parent) {
            BaseCellView cellView = (BaseCellView) convertView;
            if (cellView == null) {
                LayoutScatter scatter = LayoutScatter.getInstance(context);//LayoutInflater inflater = LayoutInflater.from(context);
                cellView = (BaseCellView) scatter.parse(ResourceTable.Layout_square_cell_layout, parent, false);//                cellView = (BaseCellView) inflater.inflate(ResourceTable.layout.square_cell_layout, null);
            }
            return cellView;
        }

        @Override
        public String getDayOfWeekDisplayValue(int dayOfWeek, String defaultValue) {
            return "";
        }
    }

    private class MonthChangeListener implements PageSlider.PageChangedListener {

        private SelectedDateItem computeNewSelectedDateItem(int difference) {

            Calendar cal = Calendar.getInstance();
            cal.set(displayYear, displayMonth, 1);
            cal.add(Calendar.MONTH, -difference);

            return new SelectedDateItem(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH), 1);

        }

        @Override
        public void onPageSliding(int i, float v, int i1) {

        }

        @Override
        public void onPageSlideStateChanged(int i) {

        }

        @Override
        public void onPageChosen(int position) {
            int direction = position > lastPosition ? RIGHT : LEFT;

            //refresh the previous adapter and deselect the item
            monthViewPagerAdapter.getMonthAdapterAtPosition(lastPosition % MonthViewPagerAdapter.VIEWS_IN_PAGER).setSelectedItem(null, true, false);
            if (disableAutoDateSelection) {
                monthViewPagerAdapter.refreshUserSelectedItem(userSelectedItem);
            }

            SelectedDateItem newDateItem;
            if (shouldOverrideComputedDate) {
                //set the selectedDateItem as the newDateItem
                newDateItem = selectedDateItem;
                shouldOverrideComputedDate = false;
            } else {
                //compute the new SelectedDateItem based on the difference in position
                newDateItem = computeNewSelectedDateItem(lastPosition - position);
            }


            //the month view pager adater will update here again
            monthViewPagerAdapter.refreshDateAdapters(position % MonthViewPagerAdapter.VIEWS_IN_PAGER, newDateItem, resetAdapters);

            //update last position
            lastPosition = position;

            //update the currently selected date item
            FlexibleCalendarGridAdapter adapter = monthViewPagerAdapter.getMonthAdapterAtPosition(position % MonthViewPagerAdapter.VIEWS_IN_PAGER);
            selectedDateItem = adapter.getSelectedItem();

            displayYear = adapter.getYear();
            displayMonth = adapter.getMonth();
            if (onMonthChangeListener != null) {
                //fire on month change event
                onMonthChangeListener.onMonthChange(displayYear, displayMonth, direction);
            }

            if (resetAdapters) {
                resetAdapters = false;
                monthViewPager.postLayout();
                /*monthViewPager.postLayout(new Runnable() {
                    @Override
                    public void run() {
                        //resetting fake count
                        monthInfPagerAdapter.setFakeCount(-1);
                        monthInfPagerAdapter.notifyDataChanged();
                    }
                });*/
            }
        }
    }

    @Override
    public boolean onEstimateSize(int widthScreen, int heightScreen) {
        boolean wrapHeight = Component.EstimateSpec.getMode(heightScreen) == Component.EstimateSpec.PRECISE;
        int height = getHeight();
        if (wrapHeight && rowHeight == 0) {
            int width = getWidth();
            widthScreen = MeasureSpec.getMeasureSpec(width,EstimateSpec.PRECISE);
            if (getChildCount() > 0) {
                Component firstChild = getComponentAt(0);
                firstChild.estimateSize(widthScreen, MeasureSpec.getMeasureSpec(height,EstimateSpec.NOT_EXCEED));
                height = firstChild.getHeight();
                rowHeight = numOfRows == 6 ? height : (int) Math.ceil(((float) height * 6) / 5);
            }
        }
        heightScreen = MeasureSpec.getMeasureSpec(rowHeight, EstimateSpec.PRECISE);
//        this.onEstimateSize(widthScreen,heightScreen);
        System.out.println("VIJAY onEstimateSize "+heightScreen+" "+widthScreen);
        return false;
    }
}

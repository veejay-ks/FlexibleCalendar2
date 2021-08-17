package com.p_v.flexiblecalendarexample.slice;

import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.entity.CalendarEvent;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.p_v.flexiblecalendar.view.SquareCellView;
import com.p_v.flexiblecalendarexample.ResourceTable;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.render.layoutboost.LayoutBoost;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarActivityFragment extends Fraction implements FlexibleCalendarView.OnMonthChangeListener, FlexibleCalendarView.OnDateClickListener {
    private ComponentContainer mContainer;
    private Context mContext;
    private FlexibleCalendarView calendarView;
    private Text someTextView;
    private Calendar calendar = Calendar.getInstance();

    public CalendarActivityFragment(Context context, ComponentContainer componentContainer) {
        this.mContext = context;
        this.mContainer = componentContainer;
    }

    public CalendarActivityFragment(Context mContext) {
        calendarView.setOnMonthChangeListener((FlexibleCalendarView.OnMonthChangeListener) mContext);
        calendarView.setOnDateClickListener((FlexibleCalendarView.OnDateClickListener) mContext);
    }

    @Override
    protected Component onComponentAttached(LayoutScatter scatter, ComponentContainer container, Intent intent) {
        scatter.parse(ResourceTable.Layout_fragment_calendar, container, false);
        updateTitle(calendarView.getSelectedDateItem().getYear(), calendarView.getSelectedDateItem().getMonth());
        return super.onComponentAttached(scatter, container, intent);
    }

    @Override
    public Component getComponent() {
        Component component = LayoutBoost.inflate(this.mContext, ResourceTable.Layout_fragment_calendar, this.mContainer, false);
        initView(component);
        return super.getComponent();
    }

    private void initView(Component component) {
        calendarView = (FlexibleCalendarView) component.findComponentById(ResourceTable.Id_calendar_view);
        calendarView.setCalendarView(new FlexibleCalendarView.CalendarView() {

            @Override
            public BaseCellView getCellView(int position, Component convertView, ComponentContainer parent, int cellType) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutScatter scatter = LayoutScatter.getInstance(getContext());
                    cellView = (BaseCellView) scatter.parse(ResourceTable.Layout_calendar1_date_cell_view, parent, false);
                }
                if (cellType == BaseCellView.OUTSIDE_MONTH) {
                    try {
                        cellView.setTextColor(new Color(getResourceManager().getElement(ResourceTable.Color_date_outside_month_text_color_activity_1).getColor()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NotExistException e) {
                        e.printStackTrace();
                    } catch (WrongTypeException e) {
                        e.printStackTrace();
                    }
                }
                return cellView;
            }

            @Override
            public BaseCellView getWeekdayCellView(int position, Component convertView, ComponentContainer parent) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutScatter scatter = LayoutScatter.getInstance(getContext());
                    cellView = (SquareCellView) scatter.parse(ResourceTable.Layout_calendar1_week_cell_view, parent, false);
                }
                return cellView;
            }

            @Override
            public String getDayOfWeekDisplayValue(int dayOfWeek, String defaultValue) {
                return String.valueOf(defaultValue.charAt(0));
            }
        });
        calendarView.setOnMonthChangeListener((FlexibleCalendarView.OnMonthChangeListener) mContext);
        calendarView.setOnDateClickListener((FlexibleCalendarView.OnDateClickListener) mContext);

        fillEvents();

        Button nextDateBtn = (Button) component.findComponentById(ResourceTable.Id_move_to_next_date);
        Button prevDateBtn = (Button) component.findComponentById(ResourceTable.Id_move_to_previous_date);
        Button nextMonthBtn = (Button) component.findComponentById(ResourceTable.Id_move_to_next_month);
        Button prevMonthBtn = (Button) component.findComponentById(ResourceTable.Id_move_to_previous_month);
        Button goToCurrentDayBtn = (Button) component.findComponentById(ResourceTable.Id_go_to_current_day);
        Button showDatesOutSideMonthBtn = (Button) component.findComponentById(ResourceTable.Id_show_dates_outside_month);

        nextDateBtn.setClickedListener((Component view) -> {
            calendarView.moveToNextDate();
        });

        prevDateBtn.setClickedListener((Component view) -> {
            calendarView.moveToPreviousDate();
        });

        nextMonthBtn.setClickedListener((Component view) -> {
            calendarView.moveToNextMonth();
        });

        prevMonthBtn.setClickedListener((Component view) -> {
            calendarView.moveToPreviousMonth();
        });

        goToCurrentDayBtn.setClickedListener((Component view) -> {
            calendarView.goToCurrentDay();
        });

        showDatesOutSideMonthBtn.setClickedListener((Component view) -> {
            if (calendarView.getShowDatesOutsideMonth()) {
                calendarView.setShowDatesOutsideMonth(false);
                ((Button) view).setText("Show dates outside month");
            } else {
                ((Button) view).setText("Hide dates outside month");
                calendarView.setShowDatesOutsideMonth(true);
            }
        });
//        setupToolBar(component);
    }

    private void fillEvents() {
        calendarView.setEventDataProvider(new FlexibleCalendarView.EventDataProvider() {
            @Override
            public List<CalendarEvent> getEventsForTheDay(int year, int month, int day) {
                if (year == calendar.get(Calendar.YEAR) && month == calendar.get(Calendar.MONTH) && day == calendar.get(Calendar.DAY_OF_MONTH)) {
                    List<CalendarEvent> eventColors = new ArrayList<>(2);
                    eventColors.add(new CalendarEvent(ResourceTable.Color_holo_blue_light));
                    eventColors.add(new CalendarEvent(ResourceTable.Color_holo_purple));
                    return eventColors;
                }

                if (year == 2016 && month == 10 && day == 12) {
                    List<CalendarEvent> eventColors = new ArrayList<>(2);
                    eventColors.add(new CalendarEvent(ResourceTable.Color_holo_blue_light));
                    eventColors.add(new CalendarEvent(ResourceTable.Color_holo_purple));
                    return eventColors;
                }

                if (year == 2016 && month == 10 && day == 7 ||
                        year == 2016 && month == 10 && day == 29 ||
                        year == 2016 && month == 10 && day == 5 ||
                        year == 2016 && month == 10 && day == 9) {
                    List<CalendarEvent> eventColors = new ArrayList<>(1);
                    eventColors.add(new CalendarEvent(ResourceTable.Color_holo_blue_light));
                    return eventColors;
                }

                if (year == 2016 && month == 10 && day == 31 ||
                        year == 2016 && month == 10 && day == 22 ||
                        year == 2016 && month == 10 && day == 18 ||
                        year == 2016 && month == 10 && day == 11) {
                    List<CalendarEvent> eventColors = new ArrayList<>(3);
                    eventColors.add(new CalendarEvent(ResourceTable.Color_holo_red_dark));
                    eventColors.add(new CalendarEvent(ResourceTable.Color_holo_orange_light));
                    eventColors.add(new CalendarEvent(ResourceTable.Color_holo_purple));
                    return eventColors;
                }
                return null;
            }
        });
    }

    /*public void setupToolBar(Component mainView) {
        Toolbar toolbar = (Toolbar) mainView.findComponentById(ResourceTable.Id_toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
        bar.setDisplayShowCustomEnabled(true);

        someTextView = new Text(getActivity());
        someTextView.setTextColor(getActivity().getResources().getColor(ResourceTable.Color_title_text_color_activity_1));
        someTextView.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component v) {
                if (calendarView.isComponentDisplayed()) {// if (calendarView.isShown()) {
                    calendarView.collapse();
                } else {
                    calendarView.expand();
                }
            }
        });
        bar.setCustomView(someTextView);

        bar.setBackgroundDrawable(new ColorDrawable(getActivity().getResources()
                .getColor(ResourceTable.Color_action_bar_color_activity_1)));

        //back button color
//        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//        upArrow.setColorFilter(getResources().getColor(R.color.title_text_color_activity_1), PorterDuff.Mode.SRC_ATOP);
//        bar.setHomeAsUpIndicator(upArrow);
    }*/

    private void updateTitle(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        someTextView.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG,
                this.getResourceManager().getConfiguration().getFirstLocale()) + " " + year);
    }
    @Override
    public void onMonthChange(int year, int month, int direction) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        updateTitle(year, month);
    }
    @Override
    public void onDateClick(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        ToastDialog toastDialog = new ToastDialog(getContext());
        toastDialog.setText(cal.getTime().toString() + " Clicked");
        toastDialog.show();
    }
}


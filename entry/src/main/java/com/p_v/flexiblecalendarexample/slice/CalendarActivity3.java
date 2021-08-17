package com.p_v.flexiblecalendarexample.slice;

//import android.app.DatePickerDialog;
import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.exception.HighValueException;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.p_v.flexiblecalendarexample.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DatePicker;
import ohos.agp.components.LayoutScatter;
import ohos.agp.window.dialog.ToastDialog;
import ohos.agp.components.Button;

import java.util.*;

public class CalendarActivity3 extends AbilitySlice {
    private Map<Integer, List<CustomEvent>> eventMap;
    private FlexibleCalendarView calendarView;
    private DatePicker datePicker;

    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_activity_calendar_activity3);
        initView();
        initializeEvents();
    }
    private void initializeEvents() {
        eventMap = new HashMap<>();
        List<CustomEvent> colorLst = new ArrayList<>();
        colorLst.add(new CustomEvent(ResourceTable.Color_holo_red_dark));
        eventMap.put(25, colorLst);

        List<CustomEvent> colorLst1 = new ArrayList<>();
        colorLst1.add(new CustomEvent(ResourceTable.Color_holo_red_dark));
        colorLst1.add(new CustomEvent(ResourceTable.Color_holo_blue_light));
        colorLst1.add(new CustomEvent(ResourceTable.Color_holo_purple));
        eventMap.put(22, colorLst1);

        List<CustomEvent> colorLst2 = new ArrayList<>();
        colorLst2.add(new CustomEvent(ResourceTable.Color_holo_red_dark));
        colorLst2.add(new CustomEvent(ResourceTable.Color_holo_blue_light));
        colorLst2.add(new CustomEvent(ResourceTable.Color_holo_purple));
        eventMap.put(28, colorLst1);

        List<CustomEvent> colorLst3 = new ArrayList<>();
        colorLst3.add(new CustomEvent(ResourceTable.Color_holo_red_dark));
        colorLst3.add(new CustomEvent(ResourceTable.Color_holo_blue_light));
        eventMap.put(29, colorLst1);
    }


    private void initView() {
        calendarView = (FlexibleCalendarView) findComponentById(ResourceTable.Id_calendar_view);
        calendarView.setMonthViewHorizontalSpacing(10);
        calendarView.setMonthViewVerticalSpacing(10);
        calendarView.setOnMonthChangeListener(new FlexibleCalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month, @FlexibleCalendarView.Direction int direction) {
                ToastDialog toastDialog = new ToastDialog(getContext());
                toastDialog.setText(year+""+month+1);
                toastDialog.show();
            }
        });

        calendarView.setCalendarView(new FlexibleCalendarView.CalendarView() {
            @Override
            public BaseCellView getCellView(int position, Component convertView, ComponentContainer parent, int cellType) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutScatter inflater = LayoutScatter.getInstance(CalendarActivity3.this);
                    cellView = (BaseCellView) inflater.parse(ResourceTable.Layout_calendar3_date_cell_view,parent, false);
                }
                return cellView;
            }

            @Override
            public BaseCellView getWeekdayCellView(int position, Component convertView, ComponentContainer parent) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutScatter inflater = LayoutScatter.getInstance(CalendarActivity3.this);
                    cellView = (BaseCellView) inflater.parse(ResourceTable.Layout_calendar3_week_cell_view,parent, false);
                }
                return cellView;
            }

            @Override
            public String getDayOfWeekDisplayValue(int dayOfWeek, String defaultValue) {
                return null;
            }
        });

        calendarView.setEventDataProvider(new FlexibleCalendarView.EventDataProvider() {
            @Override
            public List<CustomEvent> getEventsForTheDay(int year, int month, int day) {
                return getEvents(year, month, day);
            }
        });

        findComponentById(ResourceTable.Id_update_events_button).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component v) {
                List<CustomEvent> colorLst1 = new ArrayList<>();
                colorLst1.add(new CustomEvent(ResourceTable.Color_holo_red_dark));
                colorLst1.add(new CustomEvent(ResourceTable.Color_holo_blue_light));
                colorLst1.add(new CustomEvent(ResourceTable.Color_holo_purple));
                eventMap.put(2, colorLst1);
                calendarView.refresh();
            }
        });

        datePicker = (DatePicker) findComponentById(ResourceTable.Id_date_picker);
        datePicker.setValueChangedListener(new DatePicker.ValueChangedListener() {
            @Override
            public void onValueChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendarView.selectDate(year, monthOfYear, dayOfMonth);
            }
        });
    }

    public List<CustomEvent> getEvents(int year, int month, int day) {
        return eventMap.get(day);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(ResourceTable.menu.menu_calendar_activity3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == ResourceTable.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}




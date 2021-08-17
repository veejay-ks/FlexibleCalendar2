package com.p_v.flexiblecalendarexample.slice;

//import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.Toast;

import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendar.view.BaseCellView;
import com.p_v.flexiblecalendarexample.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.ToastDialog;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

   /* public class CalendarActivity2 extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);*/

public class CalendarActivity2 extends AbilitySlice {
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_activity_main);
        initView();
    }

    private void initView() {
        final FlexibleCalendarView calendarView = (FlexibleCalendarView) findComponentById(ResourceTable.Id_month_view);
        System.out.println("FSC: calendarView - " + calendarView);
        calendarView.setOnMonthChangeListener(new FlexibleCalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange ( int year, int month, int direction){
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, 1);
                ToastDialog toastDialog = new ToastDialog(getContext());
                toastDialog.setText(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH) + " " + year);
                toastDialog.show();
            }
        });
        calendarView.setCalendarView(new FlexibleCalendarView.CalendarView() {
            @Override
            public BaseCellView getCellView ( int position, Component convertView, ComponentContainer parent,int cellType){
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutScatter scatter = LayoutScatter.getInstance(CalendarActivity2.this);
                    cellView = (BaseCellView) scatter.parse(ResourceTable.Layout_calendar2_date_cell_view, parent, false);
                }
                try {
                    if (cellType == BaseCellView.TODAY) {
                        cellView.setTextColor(new Color(getResourceManager().getElement(ResourceTable.Color_holo_red_dark).getColor()));
                        cellView.setTextSize(15);
                    } else {
                        cellView.setTextColor(new Color(getResourceManager().getElement(ResourceTable.Color_color_white).getColor()));
                        cellView.setTextSize(12);
                    }
                } catch (IOException | NotExistException | WrongTypeException e) {

                }
                return cellView;
            }

            @Override
            public BaseCellView getWeekdayCellView(int position, Component convertView, ComponentContainer parent) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutScatter inflater = LayoutScatter.getInstance(CalendarActivity2.this);
                    cellView = (BaseCellView) inflater.parse(ResourceTable.Layout_calendar2_week_cell_view,parent, false);
                }
                return cellView;
            }

            @Override
            public String getDayOfWeekDisplayValue ( int dayOfWeek, String defaultValue){
                return null;
            }
        });

            calendarView.setEventDataProvider(new FlexibleCalendarView.EventDataProvider() {
                @Override
                public List<? extends Event> getEventsForTheDay(int year, int month, int day) {
                    if (year == 2015 && month == 7 && day == 25) {
                        List<CustomEvent> colorLst1 = new ArrayList<>();
                        colorLst1.add(new CustomEvent(ResourceTable.Color_holo_green_dark));
                        colorLst1.add(new CustomEvent(ResourceTable.Color_holo_blue_light));
                        colorLst1.add(new CustomEvent(ResourceTable.Color_holo_purple));
                        return (List<? extends Event>) colorLst1;
                    }
                    if (year == 2015 && month == 7 && day == 8) {
                        List<CustomEvent> colorLst1 = new ArrayList<>();
                        colorLst1.add(new CustomEvent(ResourceTable.Color_holo_green_dark));
                        colorLst1.add(new CustomEvent(ResourceTable.Color_holo_blue_light));
                        colorLst1.add(new CustomEvent(ResourceTable.Color_holo_purple));
                        return (List<? extends Event>) colorLst1;
                    }
                    if (year == 2015 && month == 7 && day == 5) {
                        List<CustomEvent> colorLst1 = new ArrayList<>();
                        colorLst1.add(new CustomEvent(ResourceTable.Color_holo_purple));
                        return (List<? extends Event>) colorLst1;
                    }
                    return null;
                }
            });
            Button button = (Button) findComponentById(ResourceTable.Id_button);
            button.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    if (calendarView.isComponentDisplayed()) {
                        calendarView.setVisibility(Component.HIDE);
                        calendarView.collapse();
                    } else {
//                        calendarView.setVisibility(Component.HIDE);
                        calendarView.expand();
                    }
                }
            });

    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(ResourceTable.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == ResourceTable.Id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}


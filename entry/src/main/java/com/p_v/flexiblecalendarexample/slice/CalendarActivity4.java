package com.p_v.flexiblecalendarexample.slice;
//import android.os.Bundle;
//import android.support.v7.app.ActionBarActivity;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;

import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.view.BaseCellView;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import com.p_v.flexiblecalendarexample.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;

public class CalendarActivity4 extends AbilitySlice {
    private Text monthTextView;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_activity_calendary_activity4);
        initView();
    }

    private void initView() {
        final FlexibleCalendarView calendarView = (FlexibleCalendarView) findComponentById(ResourceTable.Id_calendar_view);
        calendarView.setStartDayOfTheWeek(Calendar.MONDAY);

        Image leftArrow = (Image) findComponentById(ResourceTable.Id_left_arrow);
        Image rightArrow = (Image) findComponentById(ResourceTable.Id_right_arrow);

        monthTextView = (Text) findComponentById(ResourceTable.Id_month_text_view);

        Calendar cal = Calendar.getInstance();
        cal.set(calendarView.getSelectedDateItem().getYear(), calendarView.getSelectedDateItem().getMonth(), 1);
        monthTextView.setText(cal.getDisplayName(Calendar.MONTH,
                Calendar.LONG, Locale.ENGLISH) + " " + calendarView.getSelectedDateItem().getYear());

        leftArrow.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component v) {
                calendarView.moveToPreviousMonth();
            }
        });

        rightArrow.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component v) {
                calendarView.moveToNextMonth();
            }
        });
        calendarView.setOnMonthChangeListener(new FlexibleCalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month, @FlexibleCalendarView.Direction int direction) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, 1);
                monthTextView.setText(cal.getDisplayName(Calendar.MONTH,
                        Calendar.LONG, Locale.ENGLISH) + " " + year);

            }
        });
        calendarView.setShowDatesOutsideMonth(true);

        calendarView.setCalendarView(new FlexibleCalendarView.CalendarView() {
            /*@Override
            public BaseCellView getCellView(int position, View convertView, ViewGroup parent, int cellType) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutInflater inflater = LayoutInflater.from(CalendarActivity4.this);
                    cellView = (BaseCellView) inflater.inflate(R.layout.calendar3_date_cell_view, null);
                }
                return cellView;
            }

            @Override
            public BaseCellView getWeekdayCellView(int position, View convertView, ViewGroup parent) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutInflater inflater = LayoutInflater.from(CalendarActivity4.this);
                    cellView = (BaseCellView) inflater.inflate(R.layout.calendar3_week_cell_view, null);
                    cellView.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
                    cellView.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
                    cellView.setTextSize(18);
                }
                return cellView;
            }*/

            @Override
            public BaseCellView getCellView(int position, Component convertView, ComponentContainer parent, int cellType) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutScatter inflater = LayoutScatter.getInstance(CalendarActivity4.this);
                    cellView = (BaseCellView) inflater.parse(ResourceTable.Layout_calendar3_date_cell_view,parent, false);
                }
                return cellView;
            }


            @Override
            public BaseCellView getWeekdayCellView(int position, Component convertView, ComponentContainer parent) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutScatter inflater = LayoutScatter.getInstance(CalendarActivity4.this);
                    cellView = (BaseCellView) inflater.parse(ResourceTable.Layout_calendar3_week_cell_view,parent, false);
                    try {
                        ShapeElement shapeElement = new ShapeElement();
                        RgbColor rgbColor = new RgbColor(getResourceManager().getElement(ResourceTable.Color_holo_purple).getColor());
                        shapeElement.setRgbColor(rgbColor);
                        cellView.setBackground(shapeElement);
                        cellView.setTextColor(new Color(getResourceManager().getElement(ResourceTable.Color_holo_orange_light).getColor()));
                    } catch (IOException | NotExistException | WrongTypeException e) {
                        e.printStackTrace();
                    }
                    cellView.setTextSize(18);
                }
                return cellView;
            }

            @Override
            public String getDayOfWeekDisplayValue(int dayOfWeek, String defaultValue) {
                return null;
            }
        });

        Button resetButton = (Button) findComponentById(ResourceTable.Id_reset_button);
        resetButton.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component v) {
                calendarView.goToCurrentMonth();
            }
        });
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(ResourceTable.menu.menu_calendary_activity4, menu);
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


package com.p_v.flexiblecalendarexample.slice;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;

import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendar.view.BaseCellView;

import java.util.ArrayList;
import java.util.List;

import com.p_v.flexiblecalendarexample.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.render.layoutboost.LayoutBoost;

public class CalendarActivity5 extends AbilitySlice {
    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_activity_calendar5);
        initView();
    }

    private void initView() {
        FlexibleCalendarView view = (FlexibleCalendarView) findComponentById(ResourceTable.Id_calendar_view);
        view.setCalendarView(new FlexibleCalendarView.CalendarView() {

            @Override
            public BaseCellView getCellView(int position, Component convertView, ComponentContainer parent, int cellType) {
                System.out.println("ExampleCellView3 getcellView 1 "+convertView);

                BaseCellView cellView = null;

                if(convertView!=null) {
                    cellView = (BaseCellView) convertView;
                }


                System.out.println("ExampleCellView3 getcellView 2 "+cellView);
                if (cellView == null) {
//                    LayoutScatter inflater = LayoutScatter.getInstance(CalendarActivity5.this);
//                    cellView = (BaseCellView) inflater.parse(ResourceTable.Layout_calendar5_date_cell_view, parent,false);
                    cellView = (BaseCellView) LayoutBoost.inflate(CalendarActivity5.this,ResourceTable.Layout_calendar5_date_cell_view,null,false);
                }
                System.out.println("ExampleCellView3 getcellView 3 "+cellView);
                return cellView;
            }

            @Override
            public BaseCellView getWeekdayCellView(int position, Component convertView, ComponentContainer parent) {
                return null;
            }

            @Override
            public String getDayOfWeekDisplayValue(int dayOfWeek, String defaultValue) {
                return null;
            }
        });

        view.setEventDataProvider(new FlexibleCalendarView.EventDataProvider() {
            @Override
            public List<? extends Event> getEventsForTheDay(int year, int month, int day) {

                if (day % 7 == 0) {
                    List<EventW> eventList = new ArrayList<>();
                    eventList.add(new EventW());
                    return eventList;
                }
                return null;
            }
        });

    }

    public static class EventW implements Event {
        public EventW() {

        }

        @Override
        public int getColor() {
            return 0;
        }
    }
}



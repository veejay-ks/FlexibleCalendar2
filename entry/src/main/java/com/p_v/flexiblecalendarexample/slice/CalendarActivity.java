package com.p_v.flexiblecalendarexample.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import com.p_v.flexiblecalendarexample.ResourceTable;

/**
 * CalendarActivity class to display the Calendar Contents.
 */
public class CalendarActivity extends AbilitySlice {

    /**
     * onStart.
     *
     * @param intent intent
     */
    @Override
    protected void onStart(final Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_fragment_calendar);
    }
}

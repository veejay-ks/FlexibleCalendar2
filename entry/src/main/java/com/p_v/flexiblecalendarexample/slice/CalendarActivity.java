package com.p_v.flexiblecalendarexample.slice;

import com.p_v.flexiblecalendarexample.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

/**
 * CalendarActivity.
 */
public class CalendarActivity extends AbilitySlice {

    /**
     * onStart.
     * @param intent intent
     */
    @Override
    protected void onStart(final Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_fragment_calendar);
    }
}

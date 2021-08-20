package com.p_v.flexiblecalendarexample;

import com.p_v.flexiblecalendarexample.slice.CalendarListActivity;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

/**
 * MainAbility.
 */
public class MainAbility extends Ability {
    /**
     * onStart.
     * @param intent intent
     */
    @Override
    public void onStart(final Intent intent) {
        super.onStart(intent);
        super.setMainRoute(CalendarListActivity.class.getName());
    }
}

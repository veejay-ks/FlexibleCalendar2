package com.p_v.flexiblecalendarexample;

import com.p_v.flexiblecalendarexample.slice.CalendarListActivity;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(CalendarListActivity.class.getName());
    }

}

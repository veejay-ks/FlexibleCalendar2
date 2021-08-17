package com.p_v.flexiblecalendarexample.slice;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;

public class AbilityNavigateUtil {

    public static void startAbility(Ability currentAbility, Class targetAbility) {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(currentAbility.getBundleName())
                .withAbilityName(targetAbility.getSimpleName())
                .build();
        intent.setOperation(operation);
        currentAbility.startAbility(intent);
    }
}

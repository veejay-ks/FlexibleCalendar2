package com.p_v.flexiblecalendar.view;

import com.p_v.flexiblecalendar.entity.Event;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.utils.Color;
import ohos.app.Context;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author p-v
 */
public abstract class BaseCellView extends Text implements Component.EstimateSizeListener {

    public static final int TODAY = 0;
    public static final int SELECTED = 1;
    public static final int REGULAR = 3;
    public static final int SELECTED_TODAY = 4;
    public static final int OUTSIDE_MONTH = 5;
    public static int DEFAULT_VAL_TODAY = 100;
    public static int DEFAULT_VAL_REGULAR = 200;
    public static int DEFAULT_VAL_SELECTED = 300;
    public static int DEFAULT_VAL_OUTSIDE_MONTH = 400;
    public static int STATE_TODAY;
    public static int STATE_REGULAR;
    public static int STATE_SELECTED;
    public static int STATE_OUTSIDE_MONTH;
    public static final String STATE_TODAY_ATTR = "state_date_today";
    public static final String STATE_REGULAR_ATTR = "state_date_regular";
    public static final String STATE_SELECTED_ATTR = "state_date_selected";
    public static final String STATE_OUTSIDE_MONTH_ATTR = "state_date_outside_month";


    private Set<Integer> stateSet;

    public BaseCellView(Context context) {
        super(context);
        stateSet = new HashSet<>(3);
    }

    public BaseCellView(Context context, AttrSet attrs) {
        super(context, attrs);
        init(attrs);
        stateSet = new HashSet<>(3);
    }

    public BaseCellView(Context context, AttrSet attrs, int defStyleAttr) {
        super(context, attrs, "");
        init(attrs);
        stateSet = new HashSet<>(3);
    }

    public void init(AttrSet attrSet) {
        boolean isStateToday = attrSet.getAttr(STATE_TODAY_ATTR).isPresent();
        System.out.println("KUMAR isStateToday--"+isStateToday);
        if (isStateToday) {
            STATE_TODAY = attrSet.getAttr(STATE_TODAY_ATTR).get().getIntegerValue();
        } else {
            STATE_TODAY = DEFAULT_VAL_TODAY;
        }
        System.out.println("KUMAR STATE_TODAY--"+STATE_TODAY);

        boolean isStateSelected = attrSet.getAttr(STATE_SELECTED_ATTR).isPresent();
        System.out.println("KUMAR isStateToday2--"+isStateSelected);
        if (isStateSelected) {
            STATE_SELECTED = attrSet.getAttr(STATE_SELECTED_ATTR).get().getIntegerValue();
        } else {
            STATE_SELECTED = DEFAULT_VAL_SELECTED;
        }
        System.out.println("KUMAR STATE_SELECTED--"+STATE_SELECTED);

        boolean isStateRegular = attrSet.getAttr(STATE_REGULAR_ATTR).isPresent();
        System.out.println("KUMAR isStateToday3--"+isStateRegular);
        if (isStateRegular) {
            STATE_REGULAR = attrSet.getAttr(STATE_REGULAR_ATTR).get().getIntegerValue();
        } else {
            STATE_REGULAR = DEFAULT_VAL_REGULAR;
        }
        System.out.println("KUMAR STATE_REGULAR--"+STATE_REGULAR);

        boolean isStateOutside = attrSet.getAttr(STATE_OUTSIDE_MONTH_ATTR).isPresent();
        System.out.println("KUMAR isStateToday4--"+isStateOutside);
        if (isStateOutside) {
            STATE_OUTSIDE_MONTH= attrSet.getAttr(STATE_OUTSIDE_MONTH_ATTR).get().getIntegerValue();
        } else {
            STATE_OUTSIDE_MONTH = DEFAULT_VAL_OUTSIDE_MONTH;
        }
        System.out.println("KUMAR STATE_OUTSIDE_MONTH--"+STATE_OUTSIDE_MONTH);
    }

    public void addState(int state) {
        stateSet.add(state);
    }

    public void clearAllStates() {
        stateSet.clear();
    }

    public abstract void setEvents(List<? extends Event> colorList);

    public Set<Integer> getStateSet() {
        return stateSet;
    }

//    @IntDef({TODAY, SELECTED, REGULAR, SELECTED_TODAY, OUTSIDE_MONTH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CellType {
    }

    @Override
    public boolean onEstimateSize(int i, int i1) {
        return false;
    }
}

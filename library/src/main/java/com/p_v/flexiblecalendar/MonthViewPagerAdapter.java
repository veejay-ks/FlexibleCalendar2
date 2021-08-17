package com.p_v.flexiblecalendar;

import com.p_v.flexiblecalendar.entity.SelectedDateItem;
import com.p_v.flexiblecalendar.view.ICellViewDrawer;
import com.p_v.flexiblecalendar.view.IDateCellViewDrawer;
//import com.p_v.fliexiblecalendar.R;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.database.DataSetSubscriber;
import ohos.agp.render.layoutboost.LayoutBoost;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author p-v
 */
public class MonthViewPagerAdapter extends PageSliderProvider {

    public static final int VIEWS_IN_PAGER = 4;
    static final String GRID_TAG_PREFIX = "111";

    private Context context;
    private List<FlexibleCalendarGridAdapter> dateAdapters;
    private FlexibleCalendarGridAdapter.OnDateCellItemClickListener onDateCellItemClickListener;
    private FlexibleCalendarGridAdapter.MonthEventFetcher monthEventFetcher;
    private IDateCellViewDrawer cellViewDrawer;
    private int gridViewHorizontalSpacing;
    private int gridViewVerticalSpacing;
    private boolean showDatesOutsideMonth;
    private boolean refreshMonthViewAdpater;
    private int startDayOfTheWeek;
    private boolean decorateDatesOutsideMonth;
    private boolean disableAutoDateSelection;
    ArrayList<String> monthList = new ArrayList<>();

    public MonthViewPagerAdapter(Context context, int year, int month,
                                 FlexibleCalendarGridAdapter.OnDateCellItemClickListener onDateCellItemClickListener,
                                 boolean showDatesOutsideMonth, boolean decorateDatesOutsideMonth, int startDayOfTheWeek,
                                 boolean disableAutoDateSelection) {
        this.context = context;
        this.dateAdapters = new ArrayList<>(VIEWS_IN_PAGER);
        this.onDateCellItemClickListener = onDateCellItemClickListener;
        this.showDatesOutsideMonth = showDatesOutsideMonth;
        this.decorateDatesOutsideMonth = decorateDatesOutsideMonth;
        this.startDayOfTheWeek = startDayOfTheWeek;
        this.disableAutoDateSelection = disableAutoDateSelection;
        initializeDateAdapters(year, month);
    }

    private void initializeDateAdapters(int year, int month) {
        int pYear;
        int pMonth;
        if (month == 0) {
            pYear = year - 1;
            pMonth = 11;
        } else {
            pYear = year;
            pMonth = month - 1;
        }
        System.out.println(" FlexibleCalendarGridAdapter month--- "+pMonth+" "+month);
        for (int i = 0; i < VIEWS_IN_PAGER - 1; i++) {
            System.out.println(" FlexibleCalendarGridAdapter month---2 "+pMonth+" "+month+" "+pYear+" "+year);
            dateAdapters.add(new FlexibleCalendarGridAdapter(context, year, month, showDatesOutsideMonth, decorateDatesOutsideMonth, startDayOfTheWeek, disableAutoDateSelection));
            if (month == 11) {
                year++;
                month = 0;
            } else {
                month++;
            }
        }
        System.out.println(" FlexibleCalendarGridAdapter month---3 "+pMonth+" "+month+" "+pYear+" "+year);
        dateAdapters.add(new FlexibleCalendarGridAdapter(context, pYear, pMonth, showDatesOutsideMonth, decorateDatesOutsideMonth, startDayOfTheWeek, disableAutoDateSelection));
    }

    @Override
    public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {
        componentContainer.removeAllComponents();
    }

    @Override
    public boolean isPageMatchToObject(Component component, Object o) {
        return component == o;
    }

    public void refreshDateAdapters(int position, SelectedDateItem selectedDateItem, boolean refreshAll) {
        FlexibleCalendarGridAdapter currentAdapter = dateAdapters.get(position);
        if (refreshAll) {
            //refresh all used when go to current month is called to refresh all the adapters
            currentAdapter.initialize(selectedDateItem.getYear(), selectedDateItem.getMonth(), startDayOfTheWeek);
        }
        //selecting the first date of the month
        currentAdapter.setSelectedItem(selectedDateItem, true, false);

        int[] nextDate = new int[2];
        FlexibleCalendarHelper.nextMonth(currentAdapter.getYear(), currentAdapter.getMonth(), nextDate);

        dateAdapters.get((position + 1) % VIEWS_IN_PAGER).initialize(nextDate[0], nextDate[1], startDayOfTheWeek);

        FlexibleCalendarHelper.nextMonth(nextDate[0], nextDate[1], nextDate);
        dateAdapters.get((position + 2) % VIEWS_IN_PAGER).initialize(nextDate[0], nextDate[1], startDayOfTheWeek);

        FlexibleCalendarHelper.previousMonth(currentAdapter.getYear(), currentAdapter.getMonth(), nextDate);
        dateAdapters.get((position + 3) % VIEWS_IN_PAGER).initialize(nextDate[0], nextDate[1], startDayOfTheWeek);

    }

    public FlexibleCalendarGridAdapter getMonthAdapterAtPosition(int position) {
        FlexibleCalendarGridAdapter gridAdapter = null;
        if (dateAdapters != null && position >= 0 && position < dateAdapters.size()) {
            gridAdapter = dateAdapters.get(position);
        }
        return gridAdapter;
    }


    @Override
    public int getCount() {
        return VIEWS_IN_PAGER;
    }

    public void setSelectedItem(SelectedDateItem selectedItem) {
        for (FlexibleCalendarGridAdapter f : dateAdapters) {
            f.setSelectedItem(selectedItem, true, false);
        }
        this.notifyDataChanged();
    }

    public void setMonthEventFetcher(FlexibleCalendarGridAdapter.MonthEventFetcher monthEventFetcher) {
        this.monthEventFetcher = monthEventFetcher;
    }

    public ICellViewDrawer getCellViewDrawer() {
        return cellViewDrawer;
    }

    public void setCellViewDrawer(IDateCellViewDrawer cellViewDrawer) {
        this.cellViewDrawer = cellViewDrawer;
    }

    public void setSpacing(int horizontalSpacing, int verticalSpacing) {
        this.gridViewHorizontalSpacing = horizontalSpacing;
        this.gridViewVerticalSpacing = verticalSpacing;
    }

    public void setShowDatesOutsideMonth(boolean showDatesOutsideMonth) {
        this.showDatesOutsideMonth = showDatesOutsideMonth;
        for (FlexibleCalendarGridAdapter adapter : dateAdapters) {
            adapter.setShowDatesOutsideMonth(showDatesOutsideMonth);
        }
    }

    public void setDecorateDatesOutsideMonth(boolean decorateDatesOutsideMonth) {
        this.decorateDatesOutsideMonth = decorateDatesOutsideMonth;
        for (FlexibleCalendarGridAdapter adapter : dateAdapters) {
            adapter.setDecorateDatesOutsideMonth(decorateDatesOutsideMonth);
        }
    }

    public void setDisableAutoDateSelection(boolean disableAutoDateSelection) {
        this.disableAutoDateSelection = disableAutoDateSelection;
        for (FlexibleCalendarGridAdapter adapter : dateAdapters) {
            adapter.setDisableAutoDateSelection(disableAutoDateSelection);
        }
    }

    @Override
    public int getPageIndex(Object object) {
        if (refreshMonthViewAdpater) {
            return POSITION_INVALID;
        }
        return POSITION_REMAIN;
    }

    public void setStartDayOfTheWeek(int startDayOfTheWeek) {
        this.startDayOfTheWeek = startDayOfTheWeek;
        for (FlexibleCalendarGridAdapter adapter : dateAdapters) {
            adapter.setFirstDayOfTheWeek(startDayOfTheWeek);
        }
    }

    public void refreshUserSelectedItem(SelectedDateItem selectedDateItem) {
        for (FlexibleCalendarGridAdapter adapter : dateAdapters) {
            if (adapter.getUserSelectedItem() != null
                    && !selectedDateItem.equals(adapter.getUserSelectedItem())) {
                adapter.setUserSelectedDateItem(selectedDateItem);
            }
        }

    }

    protected class MonthViewPagerDataSetObserver extends DataSetSubscriber {
        @Override
        public void onChanged() {
            for (FlexibleCalendarGridAdapter adapter : dateAdapters) {
                adapter.notifyDataChanged();
            }
        }

        @Override
        public void onInvalidated() {
            for (FlexibleCalendarGridAdapter adapter : dateAdapters) {
                adapter.notifyDataInvalidated();
            }
        }
    }


    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int pos) {

//        LayoutScatter layoutScatter = LayoutScatter.getInstance(context);
//        DirectionalLayout layout = new DirectionalLayout(context);
//        layout.setOrientation(1);
//        layout.verifyLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT));
//
        Component component = LayoutBoost.inflate(context, ResourceTable.Layout_month_grid_layout, null, false);
        ListContainer gridList = (ListContainer) component.findComponentById(ResourceTable.Id_grid_list);

        TableLayoutManager tableLayoutManager = new TableLayoutManager();
        tableLayoutManager.setColumnCount(7);
        gridList.setLayoutManager(tableLayoutManager);

        FlexibleCalendarGridAdapter adapter = dateAdapters.get(pos);
        adapter.setOnDateClickListener(onDateCellItemClickListener);
        adapter.setMonthEventFetcher(monthEventFetcher);
        adapter.setCellViewDrawer(cellViewDrawer);

        gridList.setItemProvider(adapter);
        gridList.setTag(GRID_TAG_PREFIX + pos);
        componentContainer.addComponent(component);

        return componentContainer;
    }
}

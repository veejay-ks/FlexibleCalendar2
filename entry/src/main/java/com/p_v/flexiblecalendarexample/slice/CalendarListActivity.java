package com.p_v.flexiblecalendarexample.slice;

import com.p_v.flexiblecalendarexample.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.render.layoutboost.LayoutBoost;
import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;

public class CalendarListActivity extends AbilitySlice {
    private List<String> calendarList;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_activity_calendar_list);
        initView();
    }

    private void initView() {
        ListContainer listContainer = (ListContainer) findComponentById(ResourceTable.Id_calendar_list_view);
        calendarList = new ArrayList<>();
        calendarList.add("Calendar 1");
        calendarList.add("Calendar 2");
        calendarList.add("Calendar 3");
        calendarList.add("Calendar 4");
        calendarList.add("Calendar 5");
        final CustomAdapter customAdapter = new CustomAdapter(this, calendarList);
        listContainer.setItemProvider(customAdapter);
    }

    public class CustomAdapter extends BaseItemProvider {
        private Context adapterContext;
        private List<String> calendarListAdapter;

        public CustomAdapter(Context context, List<String> data) {
            this.adapterContext = context;
            this.calendarListAdapter = data;
        }

        @Override
        public int getCount() {
            return calendarList.size();
        }

        @Override
        public String getItem(int i) {
            return calendarList.get(i);
        }

        @Override
        public long getItemId(int getItemId) {
            return Long.parseLong(calendarList.get(getItemId));
        }

        @Override
        public Component getComponent(int pos, Component component, ComponentContainer componentContainer) {
            final Component calList = LayoutBoost.inflate(this.adapterContext, ResourceTable.Layout_mylist, componentContainer, false);
            final Text text = (Text) calList.findComponentById(ResourceTable.Id_text_view);
            text.setText(calendarListAdapter.get(pos));
            text.setId(pos);
            text.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    int id = component.getId();
                    checkIntent(id);
                }
            });
            return calList;
        }
    }

    private void checkIntent(int id) {
        switch (id) {
            case 0:
                present(new CalendarActivity(),new Intent());
                //AbilityNavigateUtil.startAbility(getAbility(), CalendarActivity.class);
                break;
            case 1:
                present(new CalendarActivity2(),new Intent());
                // AbilityNavigateUtil.startAbility(getAbility(),CalendarActivity2.class);
                break;
            case 2:
                present(new CalendarActivity3(),new Intent());
                //AbilityNavigateUtil.startAbility(getAbility(),CalendarActivity3.class);
                break;
            case 3:
                present(new CalendarActivity4(),new Intent());
                //AbilityNavigateUtil.startAbility(getAbility(),CalendarActivity4.class);
                break;
            case 4:
                present(new CalendarActivity5(),new Intent());
                //AbilityNavigateUtil.startAbility(getAbility(),CalendarActivity5.class);
                break;
            default:
                break;
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(ResourceTable.menu.menu_calendar_list, menu);
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



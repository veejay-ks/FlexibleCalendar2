package com.antonyt.infiniteviewpager;

import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.PageSliderProvider;
import ohos.agp.database.DataSetSubscriber;
import ohos.app.Context;

/**
 * A PagerAdapter that wraps around another PagerAdapter to handle paging wrap-around.
 */
public class InfinitePagerAdapter extends PageSliderProvider {

    private static final String TAG = "InfinitePagerAdapter";
    private static final boolean DEBUG = true;
    private static final int MAX_VAL = 10000;

    private PageSliderProvider adapter;

    private int fakeCount;
    private Context context;

    public InfinitePagerAdapter(PageSliderProvider adapter, Context context) {
        this.adapter = adapter;
        this.fakeCount = -1;
        this.context = context;
    }

    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int position) {
        int virtualPosition = position % getRealCount();
        debug("instantiateItem: real position: " + position);
        debug("instantiateItem: virtual position: " + virtualPosition);

        // only expose virtual position to the inner adapter
        return adapter.createPageInContainer(componentContainer, virtualPosition);
    }

    @Override
    public void destroyPageFromContainer(ComponentContainer componentContainer, int position, Object object) {
        int virtualPosition = position % getRealCount();
        debug("destroyItem: real position: " + position);
        debug("destroyItem: virtual position: " + virtualPosition);

        // only expose virtual position to the inner adapter
        adapter.destroyPageFromContainer(componentContainer, virtualPosition, object);
    }

    @Override
    public boolean isPageMatchToObject(Component component, Object object) {
        return adapter.isPageMatchToObject(component, object);
    }

    @Override
    public int getCount() {
        if (getRealCount() == 0) {
            return 0;
        }
        if (fakeCount != -1) {
            return fakeCount;
        }
        return MAX_VAL;
    }

    /**
     * @return the {@link #getCount()} result of the wrapped adapter
     */
    public int getRealCount() {
        return adapter.getCount();
    }

    @Override
    public void onUpdateFinished(ComponentContainer componentContainer) {
        adapter.onUpdateFinished(componentContainer);
    }

    @Override
    public void startUpdate(ComponentContainer container) {
        adapter.startUpdate(container);
    }

    @Override
    public String getPageTitle(int position) {
        int virtualPosition = position % getRealCount();
        return adapter.getPageTitle(virtualPosition);
    }

    @Override
    public int getPageIndex(Object object) {
        return adapter.getPageIndex(object);
    }

    @Override
    public void notifyDataChanged() {
        adapter.notifyDataChanged();
        super.notifyDataChanged();
    }

    /*
     * End delegation
     */

    private void debug(String message) {
        if (DEBUG) {
            System.out.println(TAG+" "+message);
        }
    }

    /**
     * Set the count for the adapter. <br/>
     * A fake count to set limit the number of pages in the adapter
     *
     * @param fakeCount count
     */
    public void setFakeCount(int fakeCount) {
        this.fakeCount = fakeCount;
    }

    public void unregisterDataSetObserver(DataSetSubscriber observer) {
        super.addDataSubscriber(observer);
    }

    public void registerDataSetObserver(DataSetSubscriber observer) {
        super.removeDataSubscriber(observer);
    }

//
//    @Override
//    public void restoreState(Parcel bundle, ClassLoader classLoader) {
//    }
//
//    @Override
//    public Parcel saveState() {
//        return adapter.saveState();
//    }

//    @Override
//    public float getPageWidth(int position) {
//        return adapter.getPageWidth(position);
//    }

    //    @Override
//    public void setPrimaryItem(ComponentContainer container, int position, Object object) {
//        adapter.setPrimaryItem(container, position, object);
//    }

}
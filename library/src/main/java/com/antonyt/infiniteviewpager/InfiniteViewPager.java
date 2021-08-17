package com.antonyt.infiniteviewpager;

import ohos.agp.components.AttrSet;
import ohos.agp.components.PageSlider;
import ohos.agp.components.PageSliderProvider;
import ohos.app.Context;

/**
 * A {@link PageSlider} that allows pseudo-infinite paging with a wrap-around effect. Should be used with an {@link
 * InfinitePagerAdapter}.
 */
public class InfiniteViewPager extends PageSlider {

    public InfiniteViewPager(Context context) {
        super(context);
    }

    public InfiniteViewPager(Context context, AttrSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setProvider(PageSliderProvider provider) {
        super.setProvider(provider);
        setCurrentPage(0);
    }

    public void setProvider(PageSliderProvider provider, int lastPosition) {
        super.setProvider(provider);
        super.setCurrentPage(lastPosition,false);
    }

    @Override
    public void setCurrentPage(int itemPos) {
        setCurrentPage(itemPos,false);
    }

    @Override
    public void setCurrentPage(int itemPos, boolean smoothScroll) {
        if (getProvider().getCount() == 0) {
            super.getCurrentPage();
            return;
        }
        itemPos = getOffsetAmount() + (itemPos % getProvider().getCount());
        super.setCurrentPage(itemPos, smoothScroll);
    }

    @Override
    public int getCurrentPage() {
        if (getProvider().getCount() == 0) {
            return super.getCurrentPage();
        }
        int position = super.getCurrentPage();
        if (getProvider() instanceof InfinitePagerAdapter) {
            InfinitePagerAdapter infAdapter = (InfinitePagerAdapter) getProvider();
            // Return the actual item position in the data backing InfinitePagerAdapter
            return (position % infAdapter.getRealCount());
        } else {
            return super.getCurrentPage();
        }
    }


    public int getOffsetAmount() {
        if (getProvider().getCount() == 0) {
            return 0;
        }
        if (getProvider() instanceof InfinitePagerAdapter) {
            InfinitePagerAdapter infAdapter = (InfinitePagerAdapter) getProvider();
            // allow for 100 back cycles from the beginning
            // should be enough to create an illusion of infinity
            // warning: scrolling to very high values (1,000,000+) results in
            // strange drawing behaviour
            return infAdapter.getRealCount() * 100;
        } else {
            return 0;
        }
    }
}
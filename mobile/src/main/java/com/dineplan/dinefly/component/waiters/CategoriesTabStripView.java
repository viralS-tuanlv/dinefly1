package com.dineplan.dinefly.component.waiters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuCategory;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 09/10/2017
 */
public class CategoriesTabStripView extends SmartTabLayout
{

    List<WaiterMenuCategory> categories = new ArrayList<>();
    Callback callback;
    boolean silentMode;

    public CategoriesTabStripView(final Context context)
    {
        super(context);
        configure();
    }

    public CategoriesTabStripView(final Context context, @Nullable final AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public CategoriesTabStripView(final Context context, @Nullable final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);
        configure();
    }

    public Callback getCallback()
    {
        return callback;
    }

    public void setCallback(final Callback callback)
    {
        this.callback = callback;
    }

    private void configure()
    {
        setBackgroundColor(getContext().getResources().getColor(R.color.color_primary));
    }

    public void setData(MenuCategoryListingPager pager)
    {
        setViewPager(pager.getViewPager());
    }

    public interface Callback
    {

        void onCategorySelected(WaiterMenuCategory cat);
    }
}

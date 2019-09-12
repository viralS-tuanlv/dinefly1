package com.dineplan.dinefly.component.waiters;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuCategory;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuItem;
import me.relex.circleindicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 13/10/2017
 */
public class MenuCategoryListingPager extends FrameLayout implements ViewPager.OnPageChangeListener, MenuGrid.Callback
{

    @BindView(R.id.waitersCatsPager)
    ViewPager pager;

    @BindView(R.id.waitersCatsPagerIndicator)
    CircleIndicator indicator;

    Callback callback;
    private SwipeRefreshLayout swipeToRefreshComponent;

    public MenuCategoryListingPager(final Context context)
    {
        super(context);
        configure();
    }

    public MenuCategoryListingPager(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public MenuCategoryListingPager(final Context context, final AttributeSet attrs, final int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configure();
    }

    @TargetApi(21)
    public MenuCategoryListingPager(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
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

    public void openCategory(@NonNull WaiterMenuCategory category)
    {
        openCategory(category.getCategoryId());
    }

    public void openCategory(int categoryId)
    {
        if (pager.getAdapter() != null)
        {
            final int index = ((CategoryListingsPagerAdapter) pager.getAdapter()).getCategoryIndex(categoryId);

            if (index >= 0 && index < pager.getAdapter().getCount())
            {
                pager.setCurrentItem(index);
            }
        }
    }

    private void configure()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.view_foodcatslisting_pager, this, true);
        ButterKnife.bind(this);
        pager.addOnPageChangeListener(this);
    }

    public void setData(final List<WaiterMenuCategory> cats)
    {
        pager.setAdapter(new CategoryListingsPagerAdapter(cats));
        indicator.setViewPager(pager);

        if (cats.size() > 0)
        {
            onPageSelected(0);
        }
    }

    public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels)
    {

    }

    public void onPageSelected(final int position)
    {
        if (callback != null)
        {
            callback.onCategoryOpened(position, ((CategoryListingsPagerAdapter) pager.getAdapter()).cats.get(position));
        }
    }

    public void onPageScrollStateChanged(final int state)
    {
        if (swipeToRefreshComponent != null && !swipeToRefreshComponent.isRefreshing())
        {
            swipeToRefreshComponent.setEnabled(state == ViewPager.SCROLL_STATE_IDLE);
        }
    }

    public void onTableSelected(final WaiterMenuItem item, List<WaiterMenuItem> tagData)
    {
        if (callback != null)
        {
            callback.onMenuItemSelected(item, tagData);
        }
    }

    public void setSwipeToRefreshComponent(final SwipeRefreshLayout swipeToRefreshComponent)
    {
        this.swipeToRefreshComponent = swipeToRefreshComponent;
    }

    public ViewPager getViewPager()
    {
        return pager;
    }

    class CategoryListingsPagerAdapter extends PagerAdapter
    {

        public List<WaiterMenuCategory> cats = new ArrayList<>();

        public CategoryListingsPagerAdapter(final List<WaiterMenuCategory> cats)
        {
            this.cats.addAll(cats);
        }

        public int getCount()
        {
            return cats.size();
        }

        public boolean isViewFromObject(final View view, final Object object)
        {
            return view == object;
        }

        public Object instantiateItem(final ViewGroup container, final int position)
        {
            MenuGrid menu = new MenuGrid(container.getContext());
            menu.setData(cats.get(position).getMenuItems(), false);
            menu.setCallback(MenuCategoryListingPager.this);
            container.addView(menu);
            return menu;
        }

        public CharSequence getPageTitle(final int position)
        {
            return cats.get(position).getName();
        }

        public void destroyItem(final ViewGroup container, final int position, final Object object)
        {
            ((MenuGrid) object).destroy();
            container.removeView((View) object);
        }

        public int getCategoryIndex(final int categoryId)
        {
            for (WaiterMenuCategory cat : cats)
            {
                if (cat.getCategoryId() == categoryId)
                {
                    return cats.indexOf(cat);
                }
            }

            return -1;
        }
    }

    public interface Callback
    {

        void onCategoryOpened(int index, WaiterMenuCategory category);

        void onMenuItemSelected(WaiterMenuItem item, List<WaiterMenuItem> tagData);
    }
}

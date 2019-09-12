package com.dineplan.dinefly.component.waiters;

import android.annotation.TargetApi;
import android.content.Context;
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
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.data.model.waiters.WaiterRoom;
import com.dineplan.dinefly.data.model.waiters.WaiterTable;
import me.relex.circleindicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 13/10/2017
 */
public class RestaurantTablesPager extends FrameLayout implements ViewPager.OnPageChangeListener, RestaurantRoomView.Callback
{
    @BindView(R.id.waitersTablesPager)
    ViewPager pager;

    @BindView(R.id.waitersTablesPagerIndicator)
    CircleIndicator indicator;

    @BindView(R.id.root)
    View root;

    SwipeRefreshLayout swipeToRefreshComponent;

    Callback callback;


    public RestaurantTablesPager(final Context context)
    {
        super(context);
        configure();
    }

    public RestaurantTablesPager(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public RestaurantTablesPager(final Context context, final AttributeSet attrs, final int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configure();
    }

    @TargetApi(21)
    public RestaurantTablesPager(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes)
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

    public void setSwipeToRefreshComponent(final SwipeRefreshLayout swipeToRefreshComponent)
    {
        this.swipeToRefreshComponent = swipeToRefreshComponent;
    }

    private void configure()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.view_tables_pager, this, true);
        ButterKnife.bind(this);
        applySettings();
        pager.addOnPageChangeListener(this);
    }

    public void setData(final List<WaiterRoom> rooms)
    {
        pager.setAdapter(new TablesPagerAdapter(rooms));
        indicator.setViewPager(pager);

        if (rooms.size()>0)
        {
            onPageSelected(0);
        }
    }

    public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels)
    {

    }

    public void onPageSelected(final int position)
    {
        if (callback!=null)
        {
            callback.onRestaurantHallOpened(position, ((TablesPagerAdapter)pager.getAdapter()).rooms.get(position));
        }
    }

    public void onPageScrollStateChanged(final int state)
    {
        if (swipeToRefreshComponent != null && !swipeToRefreshComponent.isRefreshing())
        {
            swipeToRefreshComponent.setEnabled(state == ViewPager.SCROLL_STATE_IDLE);
        }
    }

    public void onRoomTableSelected(final WaiterRoom room, final WaiterTable table, final boolean viaLongTap)
    {
        if (callback!=null)
        {
            callback.onRestaurantTableSelected(table, viaLongTap);
        }
    }

    public void applySettings()
    {
        if (App.getSettings().shouldShowTablesGridBackground())
        {
            root.setBackgroundResource(R.drawable.bg_restaurant);
        } else
        {
            root.setBackgroundColor(getContext().getResources().getColor(R.color.material_amber_50));
        }

        if (pager.getAdapter()!=null)
        {
            pager.getAdapter().notifyDataSetChanged();
        }
    }

    class TablesPagerAdapter extends PagerAdapter
    {

        public List<WaiterRoom> rooms = new ArrayList<>();

        public TablesPagerAdapter(final List<WaiterRoom> rooms)
        {
            this.rooms.addAll(rooms);
        }

        public int getCount()
        {
            return rooms.size();
        }

        public boolean isViewFromObject(final View view, final Object object)
        {
            return view == object;
        }

        public Object instantiateItem(final ViewGroup container, final int position)
        {
            RestaurantRoomView room = new RestaurantRoomView(container.getContext());
            room.setData(rooms.get(position));
            room.setCallback(RestaurantTablesPager.this);
            container.addView(room);
            return room;
        }

        public void destroyItem(final ViewGroup container, final int position, final Object object)
        {
            ((RestaurantRoomView)object).destroy();
            container.removeView((View)object);
        }
    }

    public interface Callback
    {
        void onRestaurantHallOpened(int index, WaiterRoom hall);

        void onRestaurantTableSelected(WaiterTable table, final boolean viaLongTap);
    }
}

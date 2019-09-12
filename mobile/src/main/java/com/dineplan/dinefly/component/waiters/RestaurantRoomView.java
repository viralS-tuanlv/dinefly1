package com.dineplan.dinefly.component.waiters;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.data.model.waiters.WaiterRoom;
import com.dineplan.dinefly.data.model.waiters.WaiterTable;

import java.util.ArrayList;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 13/10/2017
 */
public class RestaurantRoomView extends FrameLayout implements TablesGrid.Callback
{

    @BindView(R.id.waitersRoomGrid)
    TablesGrid grid;

    WaiterRoom room;
    Callback callback;

    public RestaurantRoomView(final Context context)
    {
        super(context);
        configure();
    }

    public RestaurantRoomView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public RestaurantRoomView(final Context context, final AttributeSet attrs, final int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configure();
    }

    @TargetApi(21)
    public RestaurantRoomView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes)
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

    private void configure()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.waiters_view_room, this, true);
        ButterKnife.bind(this);
        grid.setCallback(this);
    }

    public void setData(final WaiterRoom room)
    {
        this.room = room;
        grid.setData(room.getTables());
    }

    public void onTableSelected(final WaiterTable table, boolean viaLongTap)
    {
        if (callback != null)
        {
            callback.onRoomTableSelected(room, table, viaLongTap);
        }
    }

    public void destroy()
    {
        grid.setData(new ArrayList<WaiterTable>());
        room = null;
        callback = null;
    }

    public interface Callback
    {

        void onRoomTableSelected(WaiterRoom room, WaiterTable table, final boolean viaLongTap);
    }
}

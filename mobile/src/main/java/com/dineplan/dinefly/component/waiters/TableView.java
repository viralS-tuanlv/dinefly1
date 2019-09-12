package com.dineplan.dinefly.component.waiters;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import com.dineplan.dinefly.component.SquareFrameLayout;
import com.dineplan.dinefly.data.model.waiters.WaiterTable;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
public abstract class TableView extends SquareFrameLayout
{
    WaiterTable payload;

    public TableView(final Context context)
    {
        super(context);
        configure();
    }

    public TableView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public TableView(final Context context, final AttributeSet attrs, final int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configure();
    }

    @TargetApi(21)
    public TableView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        configure();
    }

    public void setPayload(WaiterTable table)
    {
        payload = table;
        displayTable();
    }

    public WaiterTable getPayload()
    {
        return payload;
    }

    protected abstract void displayTable();

    private void configure()
    {
        LayoutInflater.from(getContext()).inflate(getTableLayoutResource(), this, true);
        ButterKnife.bind(this);
    }

    @LayoutRes
    protected abstract int getTableLayoutResource();
}

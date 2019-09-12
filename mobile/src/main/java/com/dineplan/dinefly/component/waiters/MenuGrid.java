package com.dineplan.dinefly.component.waiters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 09/10/2017
 */
public class MenuGrid extends RecyclerView
{

    Adapter adapter;
    Callback callback;

    public MenuGrid(final Context context)
    {
        super(context);
        configure();
    }

    public MenuGrid(final Context context, @Nullable final AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public MenuGrid(final Context context, @Nullable final AttributeSet attrs, final int defStyle)
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
        adapter = new Adapter();
        setAdapter(adapter);

        switch (App.getSettings().getMenuDisplayMode())
        {
            case GridRich:
                setLayoutManager(new SmoothScrollGridLayoutManager(getContext(), computeOptimalColumnsCount()));
                break;

            case PlainText:
                setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
                break;

            default:
                setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        }
    }

    private int computeOptimalColumnsCount()
    {
        final int colsSettings = App.getSettings().getMenuDisplayColumns();

        if (colsSettings <= 0)
        {
            if (!isInEditMode() && App.isTablet())
            {
                return App.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 3;
            } else
            {
                return 2;
            }
        } else
        {
            return colsSettings;
        }
    }

    public void setData(List<WaiterMenuItem> items, boolean ignoreSubtags)
    {
        adapter.setData(items, ignoreSubtags);
    }

    public void setData(final Iterator<WaiterMenuItem> items, boolean ignoreSubtags)
    {
        adapter.setData(items, ignoreSubtags);
    }

    public void destroy()
    {
        callback = null;
        adapter.clear();
    }

    class Adapter extends FlexibleAdapter<Item> implements FlexibleAdapter.OnItemClickListener
    {

        public Adapter()
        {
            super(null, null, true);
            addListener(this);
        }

        public void setData(Collection<WaiterMenuItem> menuItems, boolean ignoreSubtags)
        {
            setData(menuItems.iterator(), ignoreSubtags);
        }

        public boolean onItemClick(final int position)
        {
            if (callback != null)
            {
                callback.onTableSelected(getItem(position).menuItem, getItem(position).tagData);
                return true;
            } else
            {
                return false;
            }
        }

        public void setData(final Iterator<WaiterMenuItem> data, boolean ignoreSubtags)
        {
            Map<String, List<WaiterMenuItem>> subtags = new HashMap<>();
            List<Item> items = new ArrayList<>();


            while (data.hasNext())
            {
                WaiterMenuItem wmi = data.next();

                if (TextUtils.isEmpty(wmi.getSubTag()) || ignoreSubtags)
                {
                    items.add(new Item(wmi));
                } else
                {
                    if (!subtags.containsKey(wmi.getSubTag()))
                    {
                        subtags.put(wmi.getSubTag(), new ArrayList<WaiterMenuItem>());
                    }

                    subtags.get(wmi.getSubTag()).add(wmi);
                }
            }

            for (String subtag : subtags.keySet())
            {
                items.add(0, new Item(subtag, subtags.get(subtag)));
            }

            updateDataSet(items);
        }
    }

    class Item extends AbstractFlexibleItem<TableViewHolder>
    {

        final List<WaiterMenuItem> tagData = new ArrayList<>();
        final WaiterMenuItem menuItem;

        public Item(final WaiterMenuItem menuItem)
        {
            super();
            this.menuItem = menuItem;
        }

        public Item(String subtag, List<WaiterMenuItem> tagItems)
        {
            menuItem = WaiterMenuItem.createSubtagRootStub(subtag);
            tagData.addAll(tagItems);
        }

        public boolean equals(final Object o)
        {
            return (o instanceof Item) && menuItem.getItemId() == ((Item) o).menuItem.getItemId();
        }

        public int getLayoutRes()
        {
            switch (App.getSettings().getMenuDisplayMode())
            {
                case GridRich:
                    return R.layout.waiters_grid_mennuitems;

                case PlainText:
                    return R.layout.waiters_grid_mennuitems_plaintext;

                default:
                    return R.layout.waiters_grid_mennuitems_plaintext;
            }
        }

        public TableViewHolder createViewHolder(final View view, final FlexibleAdapter adapter)
        {
            return new TableViewHolder(view, adapter);
        }

        public void bindViewHolder(final FlexibleAdapter adapter, final TableViewHolder holder, final int position, final List payloads)
        {
            holder.set(menuItem);
        }
    }

    class TableViewHolder extends FlexibleViewHolder
    {

        @BindView(R.id.menuView)
        @Nullable
        MenuItemSquareView menuItemView;

        @BindView(R.id.menuViewRow)
        @Nullable
        MenuItemRowView menuItemRowView;

        public TableViewHolder(final View view, final FlexibleAdapter adapter)
        {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }

        public TableViewHolder(final View view, final FlexibleAdapter adapter, final boolean stickyHeader)
        {
            super(view, adapter, stickyHeader);
            ButterKnife.bind(this, view);
        }

        public void set(final WaiterMenuItem menuItem)
        {
            if (menuItemView != null)
            {
                menuItemView.showData(menuItem);
            }

            if (menuItemRowView != null)
            {
                menuItemRowView.showData(menuItem);
            }
        }
    }

    public interface Callback
    {

        void onTableSelected(WaiterMenuItem item, List<WaiterMenuItem> tagData);
    }
}

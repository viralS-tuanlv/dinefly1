package com.dineplan.dinefly.component.waiters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.data.TablesDisplayMode;
import com.dineplan.dinefly.data.model.waiters.WaiterTable;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 09/10/2017
 */
public class TablesGrid extends RecyclerView
{

    Adapter adapter;
    Callback callback;

    public TablesGrid(final Context context)
    {
        super(context);
        configure();
    }

    public TablesGrid(final Context context, @Nullable final AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public TablesGrid(final Context context, @Nullable final AttributeSet attrs, final int defStyle)
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
        setLayoutManager(new SmoothScrollGridLayoutManager(getContext(), computeOptimalColumnsCount()));
        addItemDecoration(new FlexibleItemDecoration(getContext()).addItemViewType(R.layout.waiters_grid_tables).addItemViewType(R.layout.waiters_grid_tables_simple).withEdge(true).withOffset(4));
    }

    private int computeOptimalColumnsCount()
    {
        if (isInEditMode())
        {
            return 2;
        }

        int automaticColumns = 4;

        if (App.isTablet())
        {
            automaticColumns = App.getSettings().getTablesDisplayMode() == TablesDisplayMode.GridRich ? (App.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2) : 5;
        } else
        {
            automaticColumns = App.getSettings().getTablesDisplayMode() == TablesDisplayMode.GridRich ? 2 : 4;
        }

        final int userColumns = App.getSettings().getTablesDisplayColumns();

        return (userColumns <= 0 || userColumns>10) ? automaticColumns : userColumns;
    }

    public void setData(Collection<WaiterTable> tables)
    {
        adapter.setData(tables);
    }

    class Adapter extends FlexibleAdapter<TableItem> implements FlexibleAdapter.OnItemClickListener, FlexibleAdapter.OnItemLongClickListener
    {

        public Adapter()
        {
            super(null, null, true);
            addListener(this);
        }

        public void setData(Collection<WaiterTable> tables)
        {
            List<TableItem> items = new ArrayList<>();

            for (WaiterTable table : tables)
            {
                items.add(new TableItem(table));
            }

            updateDataSet(items);
        }

        public boolean onItemClick(final int position)
        {
            if (callback != null)
            {
                callback.onTableSelected(getItem(position).table, false);
                return true;
            } else
            {
                return false;
            }
        }

        public void onItemLongClick(final int position)
        {
            if (callback != null)
            {
                callback.onTableSelected(getItem(position).table, true);
            }
        }
    }

    class TableItem extends AbstractFlexibleItem<TableViewHolder>
    {

        final WaiterTable table;

        public TableItem(final WaiterTable table)
        {
            super();
            this.table = table;
        }

        public boolean equals(final Object o)
        {
            return (o instanceof TableItem) && table.getTableId() == ((TableItem) o).table.getTableId();
        }

        public int getLayoutRes()
        {
            return App.getSettings().getTablesDisplayMode() == TablesDisplayMode.GridRich ? R.layout.waiters_grid_tables : R.layout.waiters_grid_tables_simple;
        }

        public TableViewHolder createViewHolder(final View view, final FlexibleAdapter adapter)
        {
            return new TableViewHolder(view, adapter);
        }

        public void bindViewHolder(final FlexibleAdapter adapter, final TableViewHolder holder, final int position, final List payloads)
        {
            holder.set(table);
        }
    }

    class TableViewHolder extends FlexibleViewHolder
    {

        @BindView(R.id.tableView)
        @Nullable
        LargeTableView tableView;

        @BindView(R.id.tableViewSimple)
        @Nullable
        SimpleTableView tableViewSimple;

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

        public void set(final WaiterTable table)
        {
            if (tableView != null)
            {
                tableView.setPayload(table);
            }

            if (tableViewSimple != null)
            {
                tableViewSimple.setPayload(table);
            }
        }
    }

    public interface Callback
    {

        void onTableSelected(WaiterTable table, boolean viaLongTap);
    }
}

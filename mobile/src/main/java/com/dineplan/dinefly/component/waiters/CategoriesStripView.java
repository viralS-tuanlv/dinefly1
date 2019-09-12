package com.dineplan.dinefly.component.waiters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuCategory;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 09/10/2017
 */
public class CategoriesStripView extends RecyclerView
{

    Adapter adapter;
    Callback callback;

    public CategoriesStripView(final Context context)
    {
        super(context);
        configure();
    }

    public CategoriesStripView(final Context context, @Nullable final AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public CategoriesStripView(final Context context, @Nullable final AttributeSet attrs, final int defStyle)
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
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    public void setData(Collection<WaiterMenuCategory> cats)
    {
        adapter.setData(cats);
    }

    public void selectCategory(WaiterMenuCategory category) {
        adapter.selectItemSilently(category);
    }

    class Adapter extends FlexibleAdapter<Item> implements FlexibleAdapter.OnItemClickListener
    {

        public Adapter()
        {
            super(null, null, true);
            addListener(this);
        }

        public void setData(Collection<WaiterMenuCategory> data)
        {
            List<Item> items = new ArrayList<>();

            for (WaiterMenuCategory cat : data)
            {
                items.add(new Item(cat, items.size() == 0));
            }

            updateDataSet(items);

            if (items.size() > 0)
            {
                addSelection(0);
            }
        }

        public boolean onItemClick(final int position)
        {
            selectItem(position);

            if (callback != null)
            {
                callback.onCategorySelected(getItem(position).category);
            }

            return false;
        }

        private void selectItem(final int position)
        {
            for (int i = 0; i < getItemCount(); i++)
            {
                final boolean selectionState = position == i;

                if (getItem(i).catSelected!=selectionState)
                {
                    getItem(i).setSelected(selectionState);
                    notifyItemChanged(i);
                }
            }
        }

        public void selectItemSilently(final WaiterMenuCategory category)
        {
            for (int i = 0; i < getItemCount(); i++)
            {
                if (getItem(i).category == category)
                {
                    if (!getItem(i).catSelected)
                    {
                        selectItem(i);
                        break;
                    }
                }
            }
        }
    }

    class Item extends AbstractFlexibleItem<ItemViewHolder>
    {

        final WaiterMenuCategory category;
        private boolean catSelected;

        public Item(final WaiterMenuCategory cat, final boolean selected)
        {
            super();
            this.category = cat;
            this.catSelected = selected;
        }

        public boolean equals(final Object o)
        {
            return (o instanceof Item) && category.getCategoryId() == ((Item) o).category.getCategoryId();
        }

        public int getLayoutRes()
        {
            return R.layout.waiters_strip_categories;
        }

        public ItemViewHolder createViewHolder(final View view, final FlexibleAdapter adapter)
        {
            return new ItemViewHolder(view, adapter);
        }

        public void bindViewHolder(final FlexibleAdapter adapter, final ItemViewHolder holder, final int position, final List payloads)
        {
            holder.catView.setPayload(category);
            holder.catView.setSelected(catSelected);
        }

        public void setSelected(final boolean b)
        {
            catSelected = b;
        }
    }

    class ItemViewHolder extends FlexibleViewHolder
    {

        @BindView(R.id.waiterCatsStripListItem)
        CategoryStripView catView;

        public ItemViewHolder(final View view, final FlexibleAdapter adapter)
        {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }

        public ItemViewHolder(final View view, final FlexibleAdapter adapter, final boolean stickyHeader)
        {
            super(view, adapter, stickyHeader);
            ButterKnife.bind(this, view);
        }
    }

    public interface Callback
    {

        void onCategorySelected(WaiterMenuCategory cat);
    }
}

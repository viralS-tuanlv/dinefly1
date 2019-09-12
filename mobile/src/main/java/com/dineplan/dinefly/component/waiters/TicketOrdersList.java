package com.dineplan.dinefly.component.waiters;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.core.glide.GlideApp;
import com.dineplan.dinefly.data.model.waiters.*;
import com.dineplan.dinefly.util.FormattingUtil;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.flexibleadapter.items.AbstractSectionableItem;
import eu.davidea.viewholders.FlexibleViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 14/10/2017
 */
public class TicketOrdersList extends RecyclerView
{

    public static final String TAG_SERVE_DELAYED = "TAG_SERVE_DELAYED";

    private Adapter adapter;
    Callback callback;
    private boolean quickOrderMode;

    public TicketOrdersList(final Context context)
    {
        super(context);
        configure();
    }

    public TicketOrdersList(final Context context, @Nullable final AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public TicketOrdersList(final Context context, @Nullable final AttributeSet attrs, final int defStyle)
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
        setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter();
        setAdapter(adapter);
    }

    public void setData(WaiterOrder order, boolean showPreorderOnly)
    {
        adapter.setData(order, showPreorderOnly);
    }

    public int getCount()
    {
        return adapter != null ? adapter.getItemCount() : 0;
    }

    public void setQuickOrderMode(final boolean quickOrderMode)
    {
        this.quickOrderMode = quickOrderMode;
    }

    public boolean isQuickOrderMode()
    {
        return quickOrderMode;
    }

    class Adapter extends FlexibleAdapter<TicketOrderItem> implements FlexibleAdapter.OnItemClickListener
    {

        TicketHeaderItem headerNote = new TicketHeaderItem(R.string.menuorder_header_ticket_notes);
        TicketHeaderItem headerPreorderImmediateItems = new TicketHeaderItem(R.string.menuorder_header_pre_immediate, R.string.serve_all_later_btn, R.drawable.ic_servelater_primary_24px, new Object());
        TicketHeaderItem headerPreorderLaterItems = new TicketHeaderItem(R.string.menuorder_header_pre_now);
        TicketHeaderItem headerQuickOrderItems = new TicketHeaderItem(R.string.menuorder_header_quickorder);
        TicketHeaderItem headerCurrentOrder = new TicketHeaderItem("");
        TicketHeaderItem headerCurrentOrderPending = new TicketHeaderItem(R.string.menuorder_header_ordered_delayed_current);
        TicketHeaderItem headerPostedItems = new TicketHeaderItem(R.string.menuorder_header_ordered);
        TicketHeaderItem headerPendingItems = new TicketHeaderItem(R.string.menuorder_header_pending, R.string.serve_now, TAG_SERVE_DELAYED);
        WaiterOrder order;
        boolean preordersMode;

        public Adapter()
        {
            super(null, null, true);
            setDisplayHeadersAtStartUp(true);
            addListener(this);
        }

        public void setData(WaiterOrder order, final boolean showPreorderOnly)
        {
            this.order = order;
            this.preordersMode = showPreorderOnly;

            List<TicketOrderItem> items = new ArrayList<>();

            if (showPreorderOnly)
            {
                headerCurrentOrder.title = App.getContext().getString(R.string.menuorder_header_ordered_current, FormattingUtil.formatMenuPrice(order.findTotal()));

                for (WaiterOrderSaleItem ticketItem : order.getItems())
                {
                    if (ticketItem.hasStatus(WaiterOrderItemStatus.DraftImmediate))
                    {
                        items.add(new TicketOrderItem(quickOrderMode ? headerQuickOrderItems : headerPreorderImmediateItems, ticketItem));
                    }
                }

                for (WaiterOrderSaleItem ticketItem : order.getItems())
                {
                    if (ticketItem.hasStatus(WaiterOrderItemStatus.DraftDelayed))
                    {
                        items.add(new TicketOrderItem(quickOrderMode ? headerQuickOrderItems : headerPreorderLaterItems, ticketItem));
                    }
                }

                for (WaiterOrderSaleItem ticketItem : order.getItems())
                {
                    if (ticketItem.hasStatus(WaiterOrderItemStatus.Ready, WaiterOrderItemStatus.Submitted, WaiterOrderItemStatus.PendingSubmitted, WaiterOrderItemStatus.ToBeServerLater, WaiterOrderItemStatus.PendingToBeServerLater))
                    {
                        items.add(new TicketOrderItem(quickOrderMode ? headerQuickOrderItems : headerCurrentOrder, ticketItem));
                    }
                }

            } else
            {
                items.add(new TicketOrderItem(headerNote, order));

                for (WaiterOrderSaleItem ticketItem : order.getItems())
                {
                    if (ticketItem.hasStatus(WaiterOrderItemStatus.Ready, WaiterOrderItemStatus.Submitted, WaiterOrderItemStatus.PendingSubmitted))
                    {
                        items.add(new TicketOrderItem(showPreorderOnly ? headerCurrentOrder : headerPostedItems, ticketItem));
                    }
                }

                for (WaiterOrderSaleItem ticketItem : order.getItems())
                {
                    if (ticketItem.hasStatus(WaiterOrderItemStatus.ToBeServerLater, WaiterOrderItemStatus.PendingToBeServerLater))
                    {
                        items.add(new TicketOrderItem(showPreorderOnly ? headerCurrentOrderPending : headerPendingItems, ticketItem));
                    }
                }
            }

            updateDataSet(items);
            setSwipeEnabled(true);
        }

        @Override
        public void onItemSwiped(final int position, final int direction)
        {
            super.onItemSwiped(position, direction);
            final WaiterOrderSaleItem src = getItem(position).orderItem;
            removeItem(position);

            if (callback != null)
            {
                callback.onRemovePreorderedItem(src);
            }
        }

        public boolean onItemClick(final int position)
        {
            if (callback != null)
            {
                if (getItem(position) instanceof TicketOrderItem && getItem(position).order != null)
                {
                    callback.onEditTicketNotes(getItem(position).order);
                    return true;
                }

                if (getItem(position) instanceof TicketOrderItem && getItem(position).orderItem != null && getItem(position).orderItem.hasStatus(WaiterOrderItemStatus.DraftImmediate, WaiterOrderItemStatus.DraftDelayed))
                {
                    callback.onEditPreorderedItem(getItem(position).orderItem);
                    return true;
                }
            }

            return false;
        }
    }

    class TicketOrderItem extends AbstractSectionableItem<TicketItemViewHolder, TicketHeaderItem>
    {

        WaiterOrderSaleItem orderItem;
        WaiterOrder order;

        public TicketOrderItem(TicketHeaderItem header, WaiterOrderSaleItem orderItem)
        {
            super(header);
            this.orderItem = orderItem;
            setSwipeable(orderItem.hasStatus(WaiterOrderItemStatus.DraftDelayed, WaiterOrderItemStatus.DraftImmediate));
        }

        public TicketOrderItem(final TicketHeaderItem header, WaiterOrder order)
        {
            super(header);
            this.order = order;
        }

        public boolean equals(final Object o)
        {
            if (o instanceof TicketOrderItem)
            {
                TicketOrderItem oo = (TicketOrderItem) o;

                if (orderItem != null && oo.orderItem != null && orderItem.getId() == oo.orderItem.getId())
                {
                    return true;
                }

                if (order != null && oo.order != null && order.getId() == oo.order.getId())
                {
                    return true;
                }
            }

            return false;
        }

        public int hashCode()
        {
            return Long.valueOf(orderItem != null ? orderItem.getId() : order.getId()).hashCode();
        }

        public int getLayoutRes()
        {
            return order != null ? R.layout.waiters_list_ticketorders_order_notes : ((adapter.preordersMode && !orderItem.hasStatus(WaiterOrderItemStatus.DraftImmediate, WaiterOrderItemStatus.DraftDelayed)) ? R.layout.waiters_list_ticketorders_order_grayedout : R.layout.waiters_list_ticketorders_order);
        }

        public TicketItemViewHolder createViewHolder(final View view, final FlexibleAdapter adapter)
        {
            return new TicketItemViewHolder(view, adapter);
        }

        public void bindViewHolder(final FlexibleAdapter adapter, final TicketItemViewHolder holder, final int position, final List payloads)
        {
            if (order != null)
            {
                holder.set(order);
            } else
            {
                holder.set(orderItem);
            }
        }
    }

    class TicketHeaderItem extends AbstractHeaderItem<TicketHeaderViewHolder>
    {

        String title;
        String buttonText;
        int buttonIcon;
        Object buttonTag;

        public TicketHeaderItem(final String title)
        {
            super();
            this.title = title != null ? title : "";
            setSwipeable(false);
        }

        public TicketHeaderItem(@StringRes final int title)
        {
            super();
            this.title = App.getContext().getString(title);
            setSwipeable(false);
        }

        public TicketHeaderItem(@StringRes final int title, @StringRes final int buttonText, final Object buttonTag)
        {
            super();
            this.title = App.getContext().getString(title);
            this.buttonText = buttonText != 0 ? App.getContext().getString(buttonText) : null;
            this.buttonIcon = 0;
            this.buttonTag = buttonTag;
            setSwipeable(false);
        }

        public TicketHeaderItem(@StringRes final int title, @StringRes final int buttonText, @DrawableRes final int buttonIcon, final Object buttonTag)
        {
            super();
            this.title = App.getContext().getString(title);
            this.buttonText = buttonText != 0 ? App.getContext().getString(buttonText) : null;
            this.buttonIcon = buttonIcon;
            this.buttonTag = buttonTag;
            setSwipeable(false);
        }

        public boolean equals(final Object o)
        {
            return o instanceof TicketHeaderItem && title.equalsIgnoreCase(((TicketHeaderItem) o).title);
        }

        public int hashCode()
        {
            return title.hashCode();
        }

        public int getLayoutRes()
        {
            return R.layout.waiters_list_ticketorders_header;
        }

        public TicketHeaderViewHolder createViewHolder(final View view, final FlexibleAdapter adapter)
        {
            return new TicketHeaderViewHolder(view, adapter);
        }

        public void bindViewHolder(final FlexibleAdapter adapter, final TicketHeaderViewHolder holder, final int position, final List payloads)
        {
            holder.set(this);
        }
    }

    class TicketItemViewHolder extends FlexibleViewHolder
    {

        @BindView(R.id.rear_left_view)
        View rearLeftView;

        @BindView(R.id.rear_right_view)
        View rearRightView;

        @BindView(R.id.master_view)
        View masterView;

        @BindView(R.id.waitersTicketOrderItemIcon)
        @Nullable
        ImageView iconView;

        @BindView(R.id.waitersTicketOrderItemIconText)
        @Nullable
        TextView iconText;

        @BindView(R.id.waitersTicketOrderItemName)
        @Nullable
        TextView nameView;

        @BindView(R.id.waitersTicketOrderItemPrice)
        @Nullable
        TextView priceView;

        @BindView(R.id.waitersTicketOrderItemExtras)
        @Nullable
        TextView extrasView;

        @BindView(R.id.waitersTicketOrderItemNotes)
        @Nullable
        TextView notesView;

        @BindView(R.id.waitersTicketOrderItemQty)
        @Nullable
        TextView qtyView;

        public TicketItemViewHolder(final View view, final FlexibleAdapter adapter)
        {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }

        @Override
        public View getFrontView()
        {
            return masterView;
        }

        @Override
        public View getRearLeftView()
        {
            return rearLeftView;
        }

        @Override
        public View getRearRightView()
        {
            return rearRightView;
        }

        public void set(final WaiterOrderSaleItem orderItem)
        {
            WaiterMenuItem stock = App.getDataManager().getWaiterDataManage().findItem(orderItem.getMenuItemid());

            if (stock != null && !TextUtils.isEmpty(stock.getDineplanPictrue()))
            {
                GlideApp.with(iconView).load(stock.getDineplanPictrue()).into(iconView);
                iconText.setVisibility(INVISIBLE);
            } else
            {
                iconText.setVisibility(VISIBLE);

                if (!TextUtils.isEmpty(stock.getName()))
                {
                    final StringTokenizer tok = new StringTokenizer(stock.getName(), " ", false);

                    if (tok.countTokens() == 1)
                    {
                        final String t = tok.nextToken();
                        iconText.setText(t.length() > 1 ? t.substring(0, 2) : t);
                    } else
                    {
                        final String t1 = tok.nextToken();
                        final String t2 = tok.nextToken();
                        iconText.setText(String.format("%s%s", (t1.length() > 0 ? t1.substring(0, 1) : ""), (t2.length() > 0 ? t2.substring(0, 1) : "")));
                    }
                } else
                {
                    iconText.setText(!TextUtils.isEmpty(stock.getAlias()) ? stock.getAlias() : "?");
                }
            }

            nameView.setText(orderItem.getName());
            priceView.setText(FormattingUtil.formatMenuPrice(orderItem.getFinalPrice()));
            qtyView.setText("" + orderItem.getQty());

            StringBuilder extras = new StringBuilder();
            for (WaiterOrderItemTag tag : orderItem.getTags())
            {
                if (extras.length() > 0)
                {
                    extras.append(", ");
                }

                extras.append(tag.getName());

                if (tag.getQty() > 1)
                {
                    extras.append(String.format(" x%d", tag.getQty()));
                }
            }

            if (extras.length() > 0)
            {
                extrasView.setText(extras.toString());
                extrasView.setVisibility(VISIBLE);
            } else
            {
                extrasView.setVisibility(GONE);
            }

            if (!TextUtils.isEmpty(orderItem.getNotes()))
            {
                notesView.setText(orderItem.getNotes());
                notesView.setVisibility(VISIBLE);
            } else
            {
                notesView.setVisibility(GONE);
            }

            if (orderItem.isVoidOrder())
            {
                qtyView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_void, 0, 0, 0);
            } else if (orderItem.isGiftOrder())
            {
                qtyView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_gift, 0, 0, 0);
            } else
            {
                qtyView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            try
            {
                if (orderItem.isCombo() && orderItem.getComboItems().size() > 0)
                {
                    StringBuilder b = new StringBuilder(priceView.getText().toString()).append("\n\n");

                    for (WaiterOrderComboItem comboItem : orderItem.getComboItems())
                    {
                        if (comboItem.getQty()==1) {
                            b.append("- ").append(comboItem.getName()).append("\n");
                        } else {
                            String strQty = "" + comboItem.getQty();
                            if (strQty.endsWith(".0")) strQty = strQty.replace(".0","");
                            b.append("- ").append("x" + strQty).append(" ").append(comboItem.getName()).append("\n");
                        }
                    }

                    priceView.setText(b.toString());
                }
            } catch (Throwable ignoted)
            {

            }
        }

        public void set(final WaiterOrder order)
        {
            notesView.setVisibility(VISIBLE);

            if (!TextUtils.isEmpty(order.getNotes()))
            {
                notesView.setText(order.getNotes());
            } else
            {
                notesView.setText(R.string.empty_ticket_notes);
            }
        }
    }

    class TicketHeaderViewHolder extends FlexibleViewHolder
    {

        @BindView(R.id.waiterTicketordersHeader)
        TextView headerView;

        @BindView(R.id.waiterTicketordersHeaderBtn)
        Button btnView;

        public TicketHeaderViewHolder(final View view, final FlexibleAdapter adapter)
        {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }

        public TicketHeaderViewHolder(final View view, final FlexibleAdapter adapter, final boolean stickyHeader)
        {
            super(view, adapter, stickyHeader);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.waiterTicketordersHeaderBtn)
        @Optional
        void onHeaderButtonClick()
        {
            if (callback != null)
            {
                callback.onHeaderButtonClick(btnView.getTag());
            }
        }

        public void set(final TicketHeaderItem item)
        {
            if (TextUtils.isEmpty(item.title))
            {
                headerView.setVisibility(View.GONE);
            } else
            {
                headerView.setVisibility(View.VISIBLE);
                headerView.setText(item.title);
            }

            if (TextUtils.isEmpty(item.buttonText))
            {
                btnView.setVisibility(GONE);
            } else
            {
                btnView.setText(item.buttonText);
                btnView.setTag(item.buttonTag);
                btnView.setVisibility(VISIBLE);
            }
        }
    }

    public interface Callback
    {

        void onRemovePreorderedItem(WaiterOrderSaleItem item);

        void onHeaderButtonClick(Object tag);

        void onEditTicketNotes(WaiterOrder ticket);

        void onEditPreorderedItem(WaiterOrderSaleItem item);
    }
}

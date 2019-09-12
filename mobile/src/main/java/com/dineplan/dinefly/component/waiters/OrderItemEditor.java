package com.dineplan.dinefly.component.waiters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dineplan.dinefly.R;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.core.glide.GlideApp;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuCategory;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuItem;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuItemPortion;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuItemTag;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuItemTagGroup;
import com.dineplan.dinefly.data.model.waiters.WaiterOrderItemTag;
import com.dineplan.dinefly.data.model.waiters.WaiterOrderSaleItem;
import com.dineplan.dinefly.util.FormattingUtil;
import com.github.florent37.androidslidr.Slidr;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sbgapps.simplenumberpicker.decimal.DecimalPickerDialog;
import com.sbgapps.simplenumberpicker.decimal.DecimalPickerHandler;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.flexibleadapter.items.AbstractSectionableItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;
import eu.livotov.labs.android.robotools.os.RTKeyboard;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 14/10/2017
 */
public class OrderItemEditor extends RecyclerView
{

    private Adapter adapter;
    private boolean showExtras = true;
    private boolean showMainProperties = true;
    private boolean showStockHeader = true;

    public OrderItemEditor(final Context context)
    {
        super(context);
        configure();
    }

    public OrderItemEditor(final Context context, @Nullable final AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public OrderItemEditor(final Context context, @Nullable final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);
        configure();
    }

    private void configure()
    {
        setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter();
        addItemDecoration(new FlexibleItemDecoration(getContext()).addItemViewType(R.layout.waiters_list_menuitemorder_header).withSectionGapOffset(1));
        setAdapter(adapter);
    }

    public WaiterMenuItem getStock()
    {
        return adapter.getStock();
    }

    public WaiterMenuCategory getCategory()
    {
        return adapter.getCategory();
    }

    public WaiterOrderSaleItem getEditedOrderItem()
    {
        WaiterOrderSaleItem item = adapter.getEditedOrderItem();
        item.computePrice();
        return item;
    }

    public void setData(WaiterOrderSaleItem orderItem, WaiterMenuCategory category, WaiterMenuItem stock)
    {
        adapter.setData(orderItem, category, stock);
    }

    public void setShowExtras(final boolean showExtras)
    {
        this.showExtras = showExtras;
    }

    public void setShowMainProperties(final boolean showMainProperties)
    {
        this.showMainProperties = showMainProperties;
    }

    public void setShowStockHeader(final boolean showStockHeader)
    {
        this.showStockHeader = showStockHeader;
    }

    class Adapter extends FlexibleAdapter<OrderListItem>
    {

        OrderListHeader headerQty = new OrderListHeader(R.string.menuorder_header_main_qty);
        OrderListHeader headerNotes = new OrderListHeader(R.string.menuorder_header_main_notes);
        OrderListHeader headerPortion = new OrderListHeader(R.string.menuorder_header_portion);
        WaiterOrderSaleItem orderItem;
        WaiterMenuItem stock;
        WaiterMenuCategory category;

        public Adapter()
        {
            super(null, null, true);
            setDisplayHeadersAtStartUp(true);
        }

        public WaiterMenuItem getStock()
        {
            return stock;
        }

        public WaiterMenuCategory getCategory()
        {
            return category;
        }

        private void validate() {
            StringBuilder errs = new StringBuilder();
            final Map<String,Integer> counts = new HashMap<>();
            final Map<String,WaiterMenuItemTagGroup> groups = new HashMap<>();

            for (int i = 0; i < getItemCount(); i++)
            {
                if (getItem(i) instanceof OrderListItem)
                {
                    final OrderListItem listItem = getItem(i);
                    switch (listItem.type)
                    {
                        case Tag:
                            if (!groups.containsKey(listItem.group.getName()))
                            {
                                groups.put(listItem.group.getName(), listItem.group);
                            }

                            if (!counts.containsKey(listItem.group.getName()))
                            {
                                counts.put(listItem.group.getName(), 0);
                            }

                            counts.put(listItem.group.getName(), counts.get(listItem.group.getName()) + (listItem.checked ? (listItem.tag.getQty()>0 ? listItem.tag.getQty() : 1) : 0));
                            break;
                    }
                }
            }

            for (String group: counts.keySet())
            {
                int count = counts.get(group);
                WaiterMenuItemTagGroup groupData = groups.get(group);

                if (count<groupData.getMinSelectableCount() && groupData.getMinSelectableCount()>0)
                {
                    errs.append(App.getContext().getString(R.string.tag_min_qty_violation, group, groupData.getMinSelectableCount()));
                } else if (count>groupData.getMaxSelectableCount() && groupData.getMaxSelectableCount()>0)
                {
                    errs.append(App.getContext().getString(R.string.tag_max_qty_violation, group, groupData.getMaxSelectableCount()));
                }
            }

            if (errs.length()>0) throw new RuntimeException(errs.toString());
        }

        public WaiterOrderSaleItem getEditedOrderItem()
        {
            validate();

            if (orderItem != null)
            {
                orderItem.getTags().clear();

                for (int i = 0; i < getItemCount(); i++)
                {
                    if (getItem(i) instanceof OrderListItem)
                    {
                        final OrderListItem listItem = getItem(i);
                        switch (listItem.type)
                        {
                            case Portion:
                                if (listItem.checked)
                                {
                                    orderItem.setPortionId(listItem.portion.getPortionId());

                                    if (listItem.portion.getPrice() > 0)
                                    {
                                        orderItem.setPotionAmount(listItem.portion.getPrice());
                                    }
                                    else
                                    {
                                        orderItem.setPotionAmount(listItem.customPortionPrice);
                                    }

                                    orderItem.setName(String.format("%s %s", stock.getName(), listItem.portion.getName()));
                                }
                                break;

                            case Qty:
                                orderItem.setQty(listItem.qty);
                                break;

                            case Tag:
                                if (listItem.checked)
                                {
                                    orderItem.getTags().add(new WaiterOrderItemTag(listItem.group, listItem.tag));
                                }
                                break;

                            case Notes:
                                orderItem.setNotes(listItem.notes);
                                break;
                        }
                    }
                }

                orderItem.setDateChanged(new Date());
                return orderItem;
            }
            else
            {
                throw new IllegalStateException("You must set order item for editing before requesting the changes back.");
            }
        }

        public void setData(WaiterOrderSaleItem orderItem, final WaiterMenuCategory category, WaiterMenuItem stock)
        {
            this.orderItem = orderItem != null ? orderItem : new WaiterOrderSaleItem(stock);
            this.stock = stock;
            this.category = category;

            List<OrderListItem> items = new ArrayList<>();

            if (!TextUtils.isEmpty(stock.getDineplanPictrue()) && showStockHeader)
            {
                items.add(new OrderListItem(stock));
            }

            if (showMainProperties)
            {
                for (WaiterMenuItemPortion portion : stock.getPortions())
                {
                    items.add(new OrderListItem(portion, orderItem.hasPortion(portion), headerPortion));
                }

                items.add(new OrderListItem(headerQty, orderItem.getQty()));
            }

            if (showExtras)
            {
                for (WaiterMenuItemTagGroup group : category.getTagGroups())
                {
                    OrderListHeader header = new OrderListHeader(group.getName());

                    for (WaiterMenuItemTag tag : group.getTags())
                    {
                        items.add(new OrderListItem(group, tag, orderItem.hasTag(tag), orderItem.getTagQty(tag), header));
                    }
                }

                for (WaiterMenuItemTagGroup group : stock.getTagGroups())
                {
                    OrderListHeader header = new OrderListHeader(group.getName());

                    for (WaiterMenuItemTag tag : group.getTags())
                    {
                        items.add(new OrderListItem(group, tag, orderItem.hasTag(tag), orderItem.getTagQty(tag), header));
                    }
                }
            }

            items.add(new OrderListItem(headerNotes, orderItem.getNotes()));

            updateDataSet(items);
        }
    }

    enum ListItemType
    {
        Header, Qty, Notes, Portion, Tag, Section
    }

    class OrderListItem extends AbstractSectionableItem<OrderListViewHolder, OrderListHeader>
    {

        ListItemType type;

        float qty;
        boolean checked;
        WaiterMenuItemPortion portion;
        WaiterMenuItemTagGroup group;
        WaiterMenuItemTag tag;
        WaiterMenuItem menu;
        String notes;
        float customPortionPrice;

        public OrderListItem(OrderListHeader header, float qty)
        {
            super(header);
            type = ListItemType.Qty;
            this.qty = qty;
        }

        public OrderListItem(WaiterMenuItem menuItem)
        {
            super(new OrderListHeader(null));
            type = ListItemType.Header;
            menu = menuItem;
        }

        public OrderListItem(WaiterMenuItemPortion portion, boolean selected, OrderListHeader header)
        {
            super(header);
            type = ListItemType.Portion;
            this.portion = portion;
            checked = selected;
        }

        public OrderListItem(WaiterMenuItemTagGroup group, WaiterMenuItemTag tag, boolean selected, int qty, OrderListHeader header)
        {
            super(header);
            type = ListItemType.Tag;
            this.group = group;
            this.tag = tag;
            this.checked = selected;
            this.tag.setQty(qty);
        }

        public OrderListItem(final OrderListHeader header, final String notes)
        {
            super(header);
            type = ListItemType.Notes;
            this.notes = notes;
        }

        public boolean equals(final Object o)
        {
            if (o instanceof OrderListItem)
            {
                switch (type)
                {
                    case Header:
                        return menu.getItemId() == ((OrderListItem) o).menu.getItemId();

                    case Qty:
                        return ((OrderListItem) o).type == ListItemType.Qty;

                    case Portion:
                        return portion.getPortionId() == ((OrderListItem) o).portion.getPortionId();

                    case Tag:
                        return tag.getTagId() == ((OrderListItem) o).tag.getTagId() && group.getGroupId() == ((OrderListItem) o).group.getGroupId();

                    case Notes:
                        return ((OrderListItem) o).type == ListItemType.Notes;

                    default:
                        return false;
                }
            }
            else
            {
                return false;
            }
        }

        public int hashCode()
        {
            switch (type)
            {
                case Header:
                    return ("m" + menu.getItemId()).hashCode();

                case Qty:
                    return type.hashCode();

                case Portion:
                    return ("p" + portion.getPortionId()).hashCode();

                case Tag:
                    return String.format("g%st%s", group.getGroupId(), tag.getTagId()).hashCode();

                case Notes:
                    return type.hashCode();

                default:
                    return super.hashCode();
            }
        }

        public int getLayoutRes()
        {
            switch (type)
            {
                case Qty:
                    return R.layout.waiters_list_menuitemorder_qty;

                case Tag:
                    return R.layout.waiters_list_menuitemorder_option;

                case Portion:
                    return R.layout.waiters_list_menuitemorder_portion;

                case Header:
                    return R.layout.waiters_list_menuitemorder_main;

                case Notes:
                    return R.layout.waiters_list_menuitemorder_notes;

                default:
                    return 0;
            }
        }

        public OrderListViewHolder createViewHolder(final View view, final FlexibleAdapter adapter)
        {
            return new OrderListViewHolder(view, adapter, type);
        }

        public void bindViewHolder(final FlexibleAdapter adapter, final OrderListViewHolder holder, final int position, final List payloads)
        {
            holder.set(this);
        }
    }

    class OrderListHeader extends AbstractHeaderItem<HeaderViewHolder>
    {

        String title;

        public OrderListHeader(final String title)
        {
            super();
            this.title = title != null ? title : "";
        }

        public OrderListHeader(@StringRes final int title)
        {
            super();
            this.title = App.getContext().getString(title);
        }

        public boolean equals(final Object o)
        {
            return o instanceof OrderListHeader && title.equalsIgnoreCase(((OrderListHeader) o).title);
        }

        public int hashCode()
        {
            return title.hashCode();
        }

        public int getLayoutRes()
        {
            return R.layout.waiters_list_menuitemorder_header;
        }

        public HeaderViewHolder createViewHolder(final View view, final FlexibleAdapter adapter)
        {
            return new HeaderViewHolder(view, adapter);
        }

        public void bindViewHolder(final FlexibleAdapter adapter, final HeaderViewHolder holder, final int position, final List payloads)
        {
            holder.set(title);
        }
    }

    class OrderListViewHolder extends FlexibleViewHolder implements DiscreteSeekBar.OnProgressChangeListener, TextWatcher, TextView.OnEditorActionListener, Slidr.Listener
    {

        @BindView(R.id.waiterMenuorderName)
        @Nullable
        TextView menuName;

        @BindView(R.id.waiterMenuorderPicture)
        @Nullable
        ImageView menuPicture;

        @BindView(R.id.waiterMenuorderQtyValue)
        @Nullable
        TextView qtyValue;

        @BindView(R.id.waiterMenuorderQtySlider)
        @Nullable
        Slidr qtySlider;

        @BindView(R.id.waiterMenuorderOption)
        @Nullable
        CheckBox tagView;

        @BindView(R.id.btnAddQtyTag)
        @Nullable
        View btnAddQtyTag;

        @BindView(R.id.waiterMenuorderPortion)
        @Nullable
        RadioButton portionView;

        @BindView(R.id.orderCustomPrice)
        @Nullable
        MaterialEditText portionPrice;

        @BindView(R.id.orderNotes)
        @Nullable
        MaterialEditText orderNotesView;

        OrderListItem item;

        public OrderListViewHolder(final View view, final FlexibleAdapter adapter, final ListItemType type)
        {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }

        public void set(final OrderListItem orderListItem)
        {
            item = orderListItem;

            switch (item.type)
            {
                case Qty:
                    setQty();
                    break;

                case Tag:
                    setTag();
                    break;

                case Portion:
                    setPortion();
                    break;

                case Header:
                    setHeader();
                    break;

                case Notes:
                    setNotes();
                    break;
            }
        }

        private void setNotes()
        {
            orderNotesView.removeTextChangedListener(this);

            if (!TextUtils.isEmpty(item.notes))
            {
                orderNotesView.setText(item.notes);
            }
            else
            {
                orderNotesView.setText("");
            }

            orderNotesView.addTextChangedListener(this);
        }

        private void setHeader()
        {
            menuName.setText(item.menu.getName());
            GlideApp.with(menuPicture.getContext()).load(item.menu.getDineplanPictrue()).into(menuPicture);
        }

        void setQty()
        {
            qtyValue.setText("" + item.qty);

            qtySlider.setMin(1);
            qtySlider.setMax(Math.max(item.qty * 2, 10));
            qtySlider.setTextMin("1");
            qtySlider.setTextMax("" + (item.qty * 2));
            qtySlider.setListener(null);
            qtySlider.setCurrentValue(item.qty);
            qtySlider.setListener(this);
            qtySlider.setTextFormatter(new Slidr.TextFormatter()
            {
                @Override
                public String format(float value)
                {
                    return "" + (int)value;
                }
            });

            qtySlider.setRegionTextFormatter(new Slidr.RegionTextFormatter()
            {
                @Override
                public String format(int region, float value)
                {
                    return "" + (int)value;
                }
            });

            qtyValue.setOnClickListener(new OnClickListener()
            {
                public void onClick(final View view)
                {
                    if (App.getActiveActivity() != null)
                    {
                        new DecimalPickerDialog.Builder().reference(2).callback(new DecimalPickerHandler()
                        {
                            public void onDecimalNumberPicked(final int reference, final float number)
                            {
                                int newValue = number > 1 ? (int) number : 1;
                                qtySlider.setMax(newValue < 10 ? 10 : newValue * 2);
                                qtySlider.setCurrentValue(newValue);
                                qtyValue.setText("" + number);
                                item.qty = number;
                            }
                        }).show(App.getActiveActivity().getFragmentManager(), "aec33");
                    }
                }
            });
        }

        void setPortion()
        {
            portionPrice.removeTextChangedListener(this);
            portionPrice.setVisibility(item.portion.getPrice() > 0 ? GONE : VISIBLE);
            portionPrice.setOnEditorActionListener(this);
            portionPrice.addTextChangedListener(this);
            portionView.setText(item.portion.getName());
            portionView.setOnCheckedChangeListener(null);
            portionView.setChecked(item.checked);
            portionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                public void onCheckedChanged(final CompoundButton compoundButton, final boolean checked)
                {
                    item.checked = checked;
                    final Adapter adapter = (Adapter) getAdapter();
                    final int ic = adapter.getItemCount();

                    for (int i = 0; i < ic; i++)
                    {
                        IFlexible itm = adapter.getItem(i);

                        if (itm instanceof OrderListItem)
                        {
                            OrderListItem candidate = (OrderListItem) itm;

                            if (candidate.type == ListItemType.Portion && candidate != item && candidate.checked)
                            {
                                candidate.checked = false;
                                adapter.notifyItemChanged(i);
                            }
                        }
                    }
                }
            });
        }

        void setTag()
        {
            setCheckboxTitleForTag(item);

            tagView.setChecked(item.checked);
            tagView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                public void onCheckedChanged(final CompoundButton compoundButton, final boolean checked)
                {
                    item.checked = checked;

                    if (!checked)
                    {
                        item.tag.setQty(1);
                    }

                    setCheckboxTitleForTag(item);
                }
            });

            btnAddQtyTag.setOnClickListener(new OnClickListener()
            {
                public void onClick(final View view)
                {
                    item.tag.setQty(tagView.isChecked() ? (item.tag.getQty() + 1) : 1);

                    if (!tagView.isChecked())
                    {
                        tagView.setChecked(true);
                    }

                    setCheckboxTitleForTag(item);
                }
            });
        }

        private void setCheckboxTitleForTag(final OrderListItem item)
        {
            if (item.tag.getPrice() > 0)
            {
                if (item.tag.getQty() > 1)
                {
                    tagView.setText(String.format("x%d %s +%s", item.tag.getQty(), item.tag.getName(), FormattingUtil.formatMenuPrice(item.tag.getPrice())));
                }
                else
                {
                    tagView.setText(String.format("%s +%s", item.tag.getName(), FormattingUtil.formatMenuPrice(item.tag.getPrice())));
                }
            }
            else
            {
                if (item.tag.getQty() > 1)
                {
                    tagView.setText(String.format("x%d %s", item.tag.getQty(), item.tag.getName()));
                }
                else
                {
                    tagView.setText(item.tag.getName());
                }
            }
        }

        public void onProgressChanged(final DiscreteSeekBar discreteSeekBar, final int i, final boolean b)
        {
            // unused
        }

        public void onStartTrackingTouch(final DiscreteSeekBar discreteSeekBar)
        {

        }

        public void onStopTrackingTouch(final DiscreteSeekBar discreteSeekBar)
        {

        }

        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2)
        {

        }

        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2)
        {

        }

        public void afterTextChanged(final Editable editable)
        {
            if (item != null)
            {
                if (orderNotesView != null)
                {
                    item.notes = editable.toString();
                }

                if (portionPrice != null)
                {
                    try
                    {
                        item.customPortionPrice = Float.parseFloat(editable.toString());
                    }
                    catch (Throwable err)
                    {
                        item.customPortionPrice = 0;
                    }
                }
            }
        }

        public boolean onEditorAction(final TextView textView, final int i, final KeyEvent keyEvent)
        {
            RTKeyboard.hideKeyboard(textView);
            return true;
        }

        @Override
        public void valueChanged(Slidr slidr, float currentValue)
        {
            qtyValue.setText("" + (int)currentValue);
            item.qty = (int)currentValue;
        }

        @Override
        public void bubbleClicked(Slidr slidr)
        {

        }
    }

    class HeaderViewHolder extends FlexibleViewHolder
    {

        @BindView(R.id.waiterMenuorderHeader)
        TextView headerView;

        public HeaderViewHolder(final View view, final FlexibleAdapter adapter)
        {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }

        public HeaderViewHolder(final View view, final FlexibleAdapter adapter, final boolean stickyHeader)
        {
            super(view, adapter, stickyHeader);
            ButterKnife.bind(this, view);
        }

        public void set(final String title)
        {
            if (TextUtils.isEmpty(title))
            {
                headerView.setVisibility(View.GONE);
            }
            else
            {
                headerView.setVisibility(View.VISIBLE);
                headerView.setText(title);
            }
        }
    }
}

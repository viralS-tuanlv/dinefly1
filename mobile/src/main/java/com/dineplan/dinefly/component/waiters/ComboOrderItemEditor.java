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
import android.util.Log;
import android.view.View;
import android.widget.*;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.dineplan.dinefly.R;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.core.api.model.api.in.ComboGroup;
import com.dineplan.dinefly.core.api.model.api.in.ComboItem;
import com.dineplan.dinefly.core.api.model.api.in.DineplanComboMenuContainer;
import com.dineplan.dinefly.core.glide.GlideApp;
import com.dineplan.dinefly.data.model.waiters.*;
import com.dineplan.dinefly.util.FormattingUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sbgapps.simplenumberpicker.decimal.DecimalPickerDialog;
import com.sbgapps.simplenumberpicker.decimal.DecimalPickerHandler;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.flexibleadapter.items.AbstractSectionableItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 14/10/2017
 */
public class ComboOrderItemEditor extends RecyclerView {
    public interface CallBackAdapterChooser {
        void showMsg(Throwable err);
    }

    private Adapter adapter;
    private CallBackAdapterChooser callBackAdapterChooser;

    public void setCallBack(CallBackAdapterChooser callBackAdapterChooser) {
        this.callBackAdapterChooser = callBackAdapterChooser;
    }

    public ComboOrderItemEditor(final Context context) {
        super(context);
        configure();
    }

    public ComboOrderItemEditor(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        configure();
    }

    public ComboOrderItemEditor(final Context context, @Nullable final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        configure();
    }

    private void configure() {
        setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter();
        addItemDecoration(new FlexibleItemDecoration(getContext()).addItemViewType(R.layout.waiters_list_menuitemorder_header).withSectionGapOffset(1));
        setAdapter(adapter);
    }

    public WaiterMenuItem getComboStock() {
        return adapter.getStock();
    }

    public WaiterOrderSaleItem getEditedOrderItem() {
        WaiterOrderSaleItem item = adapter.getEditedOrderItem();
        item.computePrice();
        return item;
    }

    public void setData(WaiterMenuItem stock, WaiterOrderSaleItem orderItem) {
        adapter.setData(stock, orderItem);
    }

    class Adapter extends FlexibleAdapter<OrderListItem> {

        OrderListHeader headerQty = new OrderListHeader(R.string.menuorder_header_main_qty);
        OrderListHeader headerNotes = new OrderListHeader(R.string.menuorder_header_main_notes);
        OrderListHeader headerPortion = new OrderListHeader(R.string.menuorder_header_portion);
        WaiterOrderSaleItem orderItem;
        WaiterMenuItem stock;
        List<ComboGroup> activeGroups = new ArrayList<>();
        DineplanComboMenuContainer comboMenuContainer;

        public Adapter() {
            super(null, null, true);
            setDisplayHeadersAtStartUp(true);
        }

        public WaiterMenuItem getStock() {
            return stock;
        }

        public int getNumberItems(String name) {
            final StringBuilder builder = new StringBuilder();
            int selectedCount = 0;

            for (ComboGroup group : activeGroups) {

                if (group.getItems().size() > 0 && group.getName().equals(name)) {
                    for (int i = 0; i < getItemCount(); i++) {
                        if (getItem(i) instanceof OrderListItem) {
                            final OrderListItem listItem = getItem(i);

                            switch (listItem.type) {
                                case ComboItem:
                                    if (listItem.comboGroup != null && listItem.comboGroup.equals(group) && listItem.checked) {
                                        selectedCount += listItem.qty;
                                    }
                                    break;
                            }
                        }
                    }
                }
            }

            return selectedCount;
        }

        public void validate() {
            final StringBuilder builder = new StringBuilder();

            for (ComboGroup group : activeGroups) {
                int selectedCount = 0;

                if (group.getItems().size() > 0) {
                    for (int i = 0; i < getItemCount(); i++) {
                        if (getItem(i) instanceof OrderListItem) {
                            final OrderListItem listItem = getItem(i);

                            switch (listItem.type) {
                                case ComboItem:
                                    if (listItem.comboGroup != null && listItem.comboGroup.equals(group) && listItem.checked) {
                                        selectedCount += listItem.qty;
                                    }
                                    break;
                            }
                        }
                    }

                    if (group.getMin() > 0 && selectedCount < group.getMin()) {
                        builder.append(App.getContext().getString(R.string.combo_group_min_fail, group.getName(), group.getMin())).append("\n\n");
                        continue;
                    }

                    if (group.getMax() > 0 && selectedCount > group.getMax()) {
                        builder.append(App.getContext().getString(R.string.combo_group_max_fail, group.getName(), group.getMax())).append("\n\n");
                        continue;
                    }
                }
            }

            if (builder.length() > 0) {
                throw new RuntimeException(builder.toString());
            }
        }

        public WaiterOrderSaleItem getEditedOrderItem() {
            if (orderItem != null) {
                validate();
                orderItem.getTags().clear();
                orderItem.setQty(1);
                orderItem.setDateChanged(new Date());
                orderItem.getComboItems().clear();

                for (int i = 0; i < getItemCount(); i++) {
                    if (getItem(i) instanceof OrderListItem) {
                        final OrderListItem listItem = getItem(i);

                        switch (listItem.type) {
                            case ComboItem:
                                if (listItem.checked) {
                                    orderItem.getComboItems().add(new WaiterOrderComboItem(orderItem, listItem.comboGroup, listItem.conmboItem, listItem.menu, listItem.qty));
                                }
                                break;

                            case Notes:
                                orderItem.setNotes(listItem.notes);
                                break;
                        }
                    }
                }

                return orderItem;
            } else {
                throw new IllegalStateException("You must set order item for editing before requesting the changes back.");
            }
        }

        public void setData(WaiterMenuItem stock, WaiterOrderSaleItem orderItem) {
            if (!stock.checkIfCombo()) {
                throw new IllegalArgumentException("Only combo menu item can be set here!");
            }

            this.orderItem = orderItem != null ? orderItem : new WaiterOrderSaleItem(stock);
            this.stock = stock;

            List<OrderListItem> items = new ArrayList<>();

            comboMenuContainer = App.getDataManager().getWaiterDataManage().getConfiguration().getComboDataFor(stock.getItemId());

            if (comboMenuContainer == null) {
                throw new IllegalStateException("Menu contains combo items but the server did not provide any combo info.");
            }

            for (ComboGroup group : comboMenuContainer.getGroups()) {
                if (group.getItems().size() > 0) {
                    OrderListHeader header = new OrderListHeader(group.buildDisplayName());

                    for (ComboItem item : group.getItems()) {
                        boolean selected = false;
                        int qty = 1;

                        WaiterMenuItem stockItem = App.getDataManager().getWaiterDataManage().findItem(item.getMenuItemId());
                        if (stockItem != null) {
                            if (orderItem.getComboItems().size() > 0) {
                                selected = orderItem.hasSelectedCombo(item.getMenuItemId());
                                qty = orderItem.getComboQty(item.getMenuItemId());
                            } else {
                                selected = item.isAutoselect();
                            }

                            final OrderListItem listItem = new OrderListItem(group, item, stockItem, selected, header);
                            listItem.qty = qty;
                            items.add(listItem);

                            if (!activeGroups.contains(group)) {
                                activeGroups.add(group);
                            }
                        }
                    }
                }
            }

            items.add(new OrderListItem(headerNotes, orderItem.getNotes()));
            updateDataSet(items);
        }
    }


    enum ListItemType {
        Header, Qty, Notes, Portion, Tag, ComboItem, Section
    }

    class OrderListItem extends AbstractSectionableItem<OrderListViewHolder, OrderListHeader> {

        ListItemType type;

        int qty;
        boolean checked;
        WaiterMenuItemPortion portion;
        WaiterMenuItemTagGroup group;
        WaiterMenuItemTag tag;
        WaiterMenuItem menu;
        ComboItem conmboItem;
        ComboGroup comboGroup;
        String notes;

        public OrderListItem(OrderListHeader header, int qty) {
            super(header);
            type = ListItemType.Qty;
            this.qty = qty;
        }

        public OrderListItem(WaiterMenuItem menuItem) {
            super(new OrderListHeader(null));
            type = ListItemType.Header;
            menu = menuItem;
        }

        public OrderListItem(WaiterMenuItemPortion portion, boolean selected, OrderListHeader header) {
            super(header);
            type = ListItemType.Portion;
            this.portion = portion;
            checked = selected;
        }

        public OrderListItem(WaiterMenuItemTagGroup group, WaiterMenuItemTag tag, boolean selected, int qty, OrderListHeader header) {
            super(header);
            type = ListItemType.Tag;
            this.group = group;
            this.tag = tag;
            this.checked = selected;
            this.tag.setQty(qty);
        }

        public OrderListItem(ComboGroup comboGroup, ComboItem comboItem, WaiterMenuItem stockItem, boolean selected, OrderListHeader header) {
            super(header);
            type = ListItemType.ComboItem;
            this.group = null;
            this.tag = null;
            this.checked = selected;
            this.conmboItem = comboItem;
            this.comboGroup = comboGroup;
            this.menu = stockItem;
            this.qty = 1;
        }

        public OrderListItem(final OrderListHeader header, final String notes) {
            super(header);
            type = ListItemType.Notes;
            this.notes = notes;
        }

        public boolean equals(final Object o) {
            if (o instanceof OrderListItem) {
                switch (type) {
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

                    case ComboItem:
                        return ((OrderListItem) o).type == ListItemType.ComboItem && conmboItem.getMenuItemId() == ((OrderListItem) o).conmboItem.getMenuItemId();

                    default:
                        return false;
                }
            } else {
                return false;
            }
        }

        public int hashCode() {
            switch (type) {
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

                case ComboItem:
                    return conmboItem.hashCode();

                default:
                    return super.hashCode();
            }
        }

        public int getLayoutRes() {
            switch (type) {
                case Qty:
                    return R.layout.waiters_list_menuitemorder_qty;

                case Tag:
                    return R.layout.waiters_list_menuitemorder_option;

                case ComboItem:
                    return R.layout.waiters_list_menuitemorder_comboitem;

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

        public OrderListViewHolder createViewHolder(final View view, final FlexibleAdapter adapter) {
            return new OrderListViewHolder(view, adapter, type);
        }

        public void bindViewHolder(final FlexibleAdapter adapter, final OrderListViewHolder holder, final int position, final List payloads) {
            holder.set(this);
        }
    }

    class OrderListHeader extends AbstractHeaderItem<HeaderViewHolder> {

        String title;

        public OrderListHeader(final String title) {
            super();
            this.title = title != null ? title : "";
        }

        public OrderListHeader(@StringRes final int title) {
            super();
            this.title = App.getContext().getString(title);
        }

        public boolean equals(final Object o) {
            return o instanceof OrderListHeader && title.equalsIgnoreCase(((OrderListHeader) o).title);
        }

        public int hashCode() {
            return title.hashCode();
        }

        public int getLayoutRes() {
            return R.layout.waiters_list_menuitemorder_header;
        }

        public HeaderViewHolder createViewHolder(final View view, final FlexibleAdapter adapter) {
            return new HeaderViewHolder(view, adapter);
        }

        public void bindViewHolder(final FlexibleAdapter adapter, final HeaderViewHolder holder, final int position, final List payloads) {
            holder.set(title);
        }
    }

    class OrderListViewHolder extends FlexibleViewHolder implements DiscreteSeekBar.OnProgressChangeListener, TextWatcher {

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
        DiscreteSeekBar qtySlider;

        @BindView(R.id.waiterMenuorderOption)
        @Nullable
        CheckBox tagView;

        @BindView(R.id.btnAddQtyTag)
        @Nullable
        View btnAddQtyTag;

        @BindView(R.id.btnAddQtyComboItem)
        @Nullable
        View btnAddQtyComboItem;

        @BindView(R.id.waiterMenuorderPortion)
        @Nullable
        RadioButton portionView;

        @BindView(R.id.orderNotes)
        @Nullable
        MaterialEditText orderNotesView;

        OrderListItem item;

        public OrderListViewHolder(final View view, final FlexibleAdapter adapter, final ListItemType type) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }

        public void set(final OrderListItem orderListItem) {
            item = orderListItem;

            switch (item.type) {
                case Qty:
                    setQty();
                    break;

                case Tag:
                    setTag();
                    break;

                case ComboItem:
                    setCombo();
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

        private void setNotes() {
            orderNotesView.removeTextChangedListener(this);

            if (!TextUtils.isEmpty(item.notes)) {
                orderNotesView.setText(item.notes);
            } else {
                orderNotesView.setText("");
            }

            orderNotesView.addTextChangedListener(this);
        }

        private void setHeader() {
            menuName.setText(item.menu.getName());
            GlideApp.with(menuPicture.getContext()).load(item.menu.getDineplanPictrue()).into(menuPicture);
        }

        void setQty() {
            qtyValue.setText("" + item.qty);
            qtySlider.setOnProgressChangeListener(null);
            qtySlider.setProgress(item.qty);
            qtySlider.setOnProgressChangeListener(this);
            qtyValue.setOnClickListener(new OnClickListener() {
                public void onClick(final View view) {
                    if (App.getActiveActivity() != null) {
                        new DecimalPickerDialog.Builder().natural().callback(new DecimalPickerHandler() {
                            public void onDecimalNumberPicked(final int reference, final float number) {
                                int newValue = number > 1 ? (int) number : 1;
                                qtySlider.setMax(newValue < 10 ? 10 : newValue * 2);
                                qtySlider.setProgress(newValue);
                            }
                        }).show(App.getActiveActivity().getFragmentManager(), "aec33");
                    }
                }
            });
        }

        void setPortion() {
            portionView.setText(item.portion.getName());
            portionView.setOnCheckedChangeListener(null);
            portionView.setChecked(item.checked);
            portionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(final CompoundButton compoundButton, final boolean checked) {
                    item.checked = checked;
                    final Adapter adapter = (Adapter) getAdapter();
                    final int ic = adapter.getItemCount();

                    for (int i = 0; i < ic; i++) {
                        IFlexible itm = adapter.getItem(i);

                        if (itm instanceof OrderListItem) {
                            OrderListItem candidate = (OrderListItem) itm;

                            if (candidate.type == ListItemType.Portion && candidate != item && candidate.checked) {
                                candidate.checked = false;
                                adapter.notifyItemChanged(i);
                            }
                        }
                    }
                }
            });
        }

        void setTag() {
            setCheckboxTitleForTag(item);

            tagView.setChecked(item.checked);
            tagView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(final CompoundButton compoundButton, final boolean checked) {
                    item.checked = checked;

                    if (!checked) {
                        item.tag.setQty(1);
                    }

                    setCheckboxTitleForTag(item);
                }
            });

            btnAddQtyTag.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View view) {
                    item.tag.setQty(tagView.isChecked() ? (item.tag.getQty() + 1) : 1);

                    if (!tagView.isChecked()) {
                        tagView.setChecked(true);
                    }

                    setCheckboxTitleForTag(item);
                }
            });
        }

        void setCombo() {
            tagView.setChecked(item.checked);
            setCheckboxTitleForComboItem(item);

            tagView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(final CompoundButton compoundButton, final boolean checked) {
                    item.checked = checked;
                    item.qty = 1;
                    setCheckboxTitleForComboItem(item);
                }
            });

            btnAddQtyComboItem.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View view) {
                    try {
                        validateQty();
                    } catch (Throwable err) {
                        if (callBackAdapterChooser != null) {
                            callBackAdapterChooser.showMsg(err);
                        }
                    }
                }
            });
        }

        private void validateQty() {
            if (item.checked) {
                if (item.qty >= item.conmboItem.getMaxQuantity()) {
                    throw new RuntimeException(App.getContext().getString(R.string.combo_group_max_fail, item.menu.getName(), item.conmboItem.getMaxQuantity()));
                }
                item.qty++;
            } else item.qty = 1;
            if (!tagView.isChecked()) {
                tagView.setChecked(true);
            }

            setCheckboxTitleForComboItem(item);
        }

        private void setCheckboxTitleForComboItem(final OrderListItem item) {
            if (item.qty > 1 && tagView.isChecked()) {
                tagView.setText(String.format("x%d %s", item.qty, item.conmboItem.getName()));
            } else {
                tagView.setText(item.conmboItem.getName());
            }
        }

        private void setCheckboxTitleForTag(final OrderListItem item) {
            if (item.tag.getPrice() > 0) {
                if (item.tag.getQty() > 1) {
                    tagView.setText(String.format("x%d %s +%s", item.tag.getQty(), item.tag.getName(), FormattingUtil.formatMenuPrice(item.tag.getPrice())));
                } else {
                    tagView.setText(String.format("%s +%s", item.tag.getName(), FormattingUtil.formatMenuPrice(item.tag.getPrice())));
                }
            } else {
                if (item.tag.getQty() > 1) {
                    tagView.setText(String.format("x%d %s", item.tag.getQty(), item.tag.getName()));
                } else {
                    tagView.setText(item.tag.getName());
                }
            }
        }

        public void onProgressChanged(final DiscreteSeekBar discreteSeekBar, final int i, final boolean b) {
            if (item != null) {
                item.qty = i;
                qtyValue.setText("" + i);
            }
        }

        public void onStartTrackingTouch(final DiscreteSeekBar discreteSeekBar) {

        }

        public void onStopTrackingTouch(final DiscreteSeekBar discreteSeekBar) {

        }

        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {

        }

        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {

        }

        public void afterTextChanged(final Editable editable) {
            if (item != null) {
                item.notes = editable.toString();
            }
        }
    }

    class HeaderViewHolder extends FlexibleViewHolder {

        @BindView(R.id.waiterMenuorderHeader)
        TextView headerView;

        public HeaderViewHolder(final View view, final FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }

        public HeaderViewHolder(final View view, final FlexibleAdapter adapter, final boolean stickyHeader) {
            super(view, adapter, stickyHeader);
            ButterKnife.bind(this, view);
        }

        public void set(final String title) {
            if (TextUtils.isEmpty(title)) {
                headerView.setVisibility(View.GONE);
            } else {
                headerView.setVisibility(View.VISIBLE);
                headerView.setText(title);
            }
        }
    }

}


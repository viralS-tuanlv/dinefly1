package com.dineplan.dinefly.component.waiters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.data.model.waiters.*;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 14/10/2017
 */
public class OrderItemEditorTwoColumn extends FrameLayout
{
    @BindView(R.id.waitersOrderItemEditorLeft)
    OrderItemEditor mainEditor;

    @BindView(R.id.waitersOrderItemEditorRight)
    OrderItemEditor tagsEditor;

    WaiterMenuCategory category;
    WaiterMenuItem stock;

    public OrderItemEditorTwoColumn(final Context context)
    {
        super(context);
        configure();
    }

    public OrderItemEditorTwoColumn(final Context context, @Nullable final AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public OrderItemEditorTwoColumn(final Context context, @Nullable final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);
        configure();
    }

    private void configure()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.waiters_view_orderitemeditor_twocolumns,this, true);
        ButterKnife.bind(this);
        mainEditor.setShowExtras(false);
        tagsEditor.setShowMainProperties(false);
        mainEditor.setShowStockHeader(false);
        tagsEditor.setShowStockHeader(false);
    }

    public WaiterMenuItem getStock()
    {
        return stock;
    }

    public WaiterMenuCategory getCategory()
    {
        return category;
    }

    public WaiterOrderSaleItem getEditedOrderItem()
    {
        final WaiterOrderSaleItem editedItem = mainEditor.getEditedOrderItem();
        editedItem.getTags().addAll(tagsEditor.getEditedOrderItem().getTags());
        editedItem.computePrice();
        return editedItem;
    }

    public void setData(WaiterOrderSaleItem orderItem, WaiterMenuCategory category, WaiterMenuItem stock)
    {
        this.category =category;
        this.stock = stock;
        mainEditor.setData(orderItem, category, stock);
        tagsEditor.setData(orderItem, category, stock);
    }

}

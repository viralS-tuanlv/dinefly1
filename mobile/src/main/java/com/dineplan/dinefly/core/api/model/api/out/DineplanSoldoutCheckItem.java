package com.dineplan.dinefly.core.api.model.api.out;

import com.dineplan.dinefly.data.model.waiters.WaiterMenuItem;
import com.dineplan.dinefly.data.model.waiters.WaiterOrderSaleItem;
import com.google.gson.annotations.SerializedName;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 27/11/2017
 */
public class DineplanSoldoutCheckItem
{

    @SerializedName("menuItemId")
    int menuItem;

    @SerializedName("portionName")
    String portionName;

    @SerializedName("menuItemName")
    String menuItemName;

    @SerializedName("quantity")
    int qty;

    public DineplanSoldoutCheckItem()
    {
    }

    public DineplanSoldoutCheckItem(final WaiterMenuItem menuItem, final WaiterOrderSaleItem item)
    {
        this.menuItem = menuItem.getItemId();
        menuItemName = menuItem.getName();
        portionName = menuItem.findPortion(item.getPortionId()).getName();
        qty = (int)item.getQty();
    }

    public int getMenuItem()
    {
        return menuItem;
    }

    public void setMenuItem(final int menuItem)
    {
        this.menuItem = menuItem;
    }

    public String getPortionName()
    {
        return portionName;
    }

    public void setPortionName(final String portionName)
    {
        this.portionName = portionName;
    }

    public String getMenuItemName()
    {
        return menuItemName;
    }

    public void setMenuItemName(final String menuItemName)
    {
        this.menuItemName = menuItemName;
    }

    public int getQty()
    {
        return qty;
    }

    public void setQty(final int qty)
    {
        this.qty = qty;
    }
}

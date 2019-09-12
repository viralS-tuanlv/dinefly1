package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 20/01/2018
 */
public class ComboItem
{
    @SerializedName("Name")
    String name;

    @SerializedName("SortOrder")
    int weight;

    @SerializedName("MenuItemId")
    int menuItemId;

    @SerializedName("AutoSelect")
    boolean autoselect;

    @SerializedName("Price")
    double price;

    @SerializedName("MaxQuantity")
    int maxQuantity;

    public ComboItem()
    {
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public int getWeight()
    {
        return weight;
    }

    public void setWeight(final int weight)
    {
        this.weight = weight;
    }

    public int getMenuItemId()
    {
        return menuItemId;
    }

    public void setMenuItemId(final int menuItemId)
    {
        this.menuItemId = menuItemId;
    }

    public boolean isAutoselect()
    {
        return autoselect;
    }

    public void setAutoselect(final boolean autoselect)
    {
        this.autoselect = autoselect;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(final double price)
    {
        this.price = price;
    }

    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final ComboItem item = (ComboItem) o;

        return menuItemId == item.menuItemId;
    }

    public int hashCode()
    {
        return menuItemId;
    }
}

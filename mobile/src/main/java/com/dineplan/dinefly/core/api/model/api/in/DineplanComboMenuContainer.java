package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 09/10/2017
 */
public class DineplanComboMenuContainer
{

    @SerializedName("MenuItemId")
    int menuItemId;

    @SerializedName("AddPrice")
    boolean addPrice;

    @SerializedName("ComboGroups")
    List<ComboGroup> groups = new ArrayList<>();

    public DineplanComboMenuContainer()
    {
    }

    public int getMenuItemId()
    {
        return menuItemId;
    }

    public void setMenuItemId(final int menuItemId)
    {
        this.menuItemId = menuItemId;
    }

    public boolean isAddPrice()
    {
        return addPrice;
    }

    public void setAddPrice(final boolean addPrice)
    {
        this.addPrice = addPrice;
    }

    public List<ComboGroup> getGroups()
    {
        return groups;
    }

    public void setGroups(final List<ComboGroup> groups)
    {
        this.groups = groups;
    }

    public ComboGroup findComboGroup(final int menuItemId)
    {
        for (ComboGroup group : groups)
        {
            for (ComboItem comboItem : group.getItems())
            {
                if (comboItem.getMenuItemId() == menuItemId)
                {
                    return group;
                }
            }
        }

        return null;
    }

    public ComboItem findComboItem(final int menuItemId)
    {
        for (ComboGroup group : groups)
        {
            for (ComboItem comboItem : group.getItems())
            {
                if (comboItem.getMenuItemId() == menuItemId)
                {
                    return comboItem;
                }
            }
        }

        return null;
    }
}

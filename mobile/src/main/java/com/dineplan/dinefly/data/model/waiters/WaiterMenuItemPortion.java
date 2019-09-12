package com.dineplan.dinefly.data.model.waiters;

import com.dineplan.dinefly.core.api.model.api.in.DineplanMenuPortion;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
@Entity
public class WaiterMenuItemPortion
{
    @Id
    long pk;

    int portionId;

    String name;

    float price;

    public WaiterMenuItemPortion()
    {
    }

    public int getPortionId()
    {
        return portionId;
    }

    public void setPortionId(final int portionId)
    {
        this.portionId = portionId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public float getPrice()
    {
        return price;
    }

    public void setPrice(final float price)
    {
        this.price = price;
    }

    public static WaiterMenuItemPortion createFromDineplanPortion(final DineplanMenuPortion dineplanMenuPortion)
    {
        WaiterMenuItemPortion portion = new WaiterMenuItemPortion();

        portion.setPortionId(dineplanMenuPortion.getId());
        portion.setName(dineplanMenuPortion.getName());
        portion.setPrice(dineplanMenuPortion.getPrice());

        return portion;
    }
}

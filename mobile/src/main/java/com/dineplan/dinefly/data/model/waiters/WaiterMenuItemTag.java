package com.dineplan.dinefly.data.model.waiters;

import com.dineplan.dinefly.core.api.model.api.in.DineplanOrderTag;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */

@Entity
public class WaiterMenuItemTag
{
    @Id
    long pk;

    int tagId;

    String name;

    float price;

    int weight;

    int qty;

    public WaiterMenuItemTag()
    {
    }

    public int getTagId()
    {
        return tagId;
    }

    public void setTagId(final int tagId)
    {
        this.tagId = tagId;
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

    public int getWeight()
    {
        return weight;
    }

    public void setWeight(final int weight)
    {
        this.weight = weight;
    }

    public int getQty()
    {
        return qty;
    }

    public void setQty(final int qty)
    {
        this.qty = qty;
    }

    public static WaiterMenuItemTag createFromDineplanTag(final DineplanOrderTag dineplanOrderTag)
    {
        WaiterMenuItemTag tag = new WaiterMenuItemTag();

        tag.setTagId(dineplanOrderTag.getId());
        tag.setName(dineplanOrderTag.getName());
        tag.setPrice(dineplanOrderTag.getPrice());
        tag.setWeight(dineplanOrderTag.getWeight());

        return tag;
    }
}

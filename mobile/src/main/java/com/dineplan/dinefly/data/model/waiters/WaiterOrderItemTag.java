package com.dineplan.dinefly.data.model.waiters;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 13/10/2017
 */

@Entity
public class WaiterOrderItemTag
{
    @Id
    long id;

    long tagId;

    long groupId;

    float amount;

    String name;

    int qty;


    public WaiterOrderItemTag()
    {
        super();
        qty = 1;
    }

    public WaiterOrderItemTag(final WaiterMenuItemTagGroup group, final WaiterMenuItemTag tag)
    {
        tagId = tag.tagId;
        groupId = group.groupId;
        amount = tag.price;
        name = tag.name;
        qty = tag.getQty();
    }

    public long getId()
    {
        return id;
    }

    public void setId(final long id)
    {
        this.id = id;
    }

    public long getTagId()
    {
        return tagId;
    }

    public void setTagId(final long tagId)
    {
        this.tagId = tagId;
    }

    public long getGroupId()
    {
        return groupId;
    }

    public void setGroupId(final long groupId)
    {
        this.groupId = groupId;
    }

    public float getAmount()
    {
        return amount;
    }

    public void setAmount(final float amount)
    {
        this.amount = amount;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
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

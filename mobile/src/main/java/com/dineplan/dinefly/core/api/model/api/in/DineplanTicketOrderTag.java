package com.dineplan.dinefly.core.api.model.api.in;

import com.dineplan.dinefly.data.model.waiters.WaiterMenuItemTag;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuItemTagGroup;
import com.google.gson.annotations.SerializedName;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 21/10/2017
 */
public class DineplanTicketOrderTag
{
    @SerializedName("orderTagGroupId")
    int groupId;

    @SerializedName("orderTagId")
    int tagId;

    @SerializedName("tagName")
    String name;

    @SerializedName("tagValue")
    String value;

    @SerializedName("Quantity")
    int qty = 1;

    public DineplanTicketOrderTag()
    {
    }

    public DineplanTicketOrderTag(final WaiterMenuItemTagGroup tagGroup, final WaiterMenuItemTag tag)
    {
        groupId = tagGroup.getGroupId();
        tagId = tag.getTagId();
        name = tagGroup.getName();
        value = tag.getName();
        qty = tag.getQty()>0 ? tag.getQty() : 1;
    }

    public int getGroupId()
    {
        return groupId;
    }

    public void setGroupId(final int groupId)
    {
        this.groupId = groupId;
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

    public String getValue()
    {
        return value;
    }

    public void setValue(final String value)
    {
        this.value = value;
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

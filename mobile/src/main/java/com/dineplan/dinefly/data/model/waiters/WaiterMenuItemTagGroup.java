package com.dineplan.dinefly.data.model.waiters;

import com.dineplan.dinefly.core.api.model.api.in.DineplanOrderTag;
import com.dineplan.dinefly.core.api.model.api.in.DineplanTagGroup;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
@Entity
public class WaiterMenuItemTagGroup
{
    @Id
    long pk;

    int groupId;

    String name;

    int minSelectableCount;

    int maxSelectableCount;

    ToMany<WaiterMenuItemTag> tags;

    public WaiterMenuItemTagGroup()
    {
    }

    public int getGroupId()
    {
        return groupId;
    }

    public void setGroupId(final int groupId)
    {
        this.groupId = groupId;
    }

    public int getMinSelectableCount()
    {
        return minSelectableCount;
    }

    public void setMinSelectableCount(final int minSelectableCount)
    {
        this.minSelectableCount = minSelectableCount;
    }

    public int getMaxSelectableCount()
    {
        return maxSelectableCount;
    }

    public void setMaxSelectableCount(final int maxSelectableCount)
    {
        this.maxSelectableCount = maxSelectableCount;
    }

    public ToMany<WaiterMenuItemTag> getTags()
    {
        return tags;
    }

    public void setTags(final ToMany<WaiterMenuItemTag> tags)
    {
        this.tags = tags;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public static WaiterMenuItemTagGroup createFromDineplanTagGroup(final DineplanTagGroup dineplanTagGroup)
    {
        WaiterMenuItemTagGroup group = new WaiterMenuItemTagGroup();

        group.setGroupId(dineplanTagGroup.getId());
        group.setMinSelectableCount(dineplanTagGroup.getMinSelectableCount());
        group.setMaxSelectableCount(dineplanTagGroup.getMaxSelectableCount());
        group.setName(dineplanTagGroup.getName());

        for (DineplanOrderTag dineplanOrderTag : dineplanTagGroup.getTags())
        {
            group.tags.add(WaiterMenuItemTag.createFromDineplanTag(dineplanOrderTag));
        }

        return group;
    }
}

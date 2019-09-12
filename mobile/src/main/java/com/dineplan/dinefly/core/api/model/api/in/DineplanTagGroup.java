package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
public class DineplanTagGroup
{

    @SerializedName("Id")
    int id;

    @SerializedName("UserString")
    String name;

    @SerializedName("MinSelectItems")
    int minSelectableCount;

    @SerializedName("MaxSelectItems")
    int maxSelectableCount;

    @SerializedName("OrderTags")
    List<DineplanOrderTag> tags = new ArrayList<>();

    public DineplanTagGroup()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(final int id)
    {
        this.id = id;
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

    public List<DineplanOrderTag> getTags()
    {
        return tags;
    }

    public void setTags(final List<DineplanOrderTag> tags)
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
}

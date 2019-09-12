package com.dineplan.dinefly.core.api.model.api.in;

import com.dineplan.dinefly.R;
import com.dineplan.dinefly.core.App;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 20/01/2018
 */
public class ComboGroup
{
    @SerializedName("Name")
    String name;

    @SerializedName("SortOrder")
    int weight;

    @SerializedName("Minimum")
    int min;

    @SerializedName("Maximum")
    int max;

    @SerializedName("ComboItems")
    List<ComboItem> items = new ArrayList<>();

    public ComboGroup()
    {
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

    public int getMin()
    {
        return min;
    }

    public void setMin(final int min)
    {
        this.min = min;
    }

    public int getMax()
    {
        return max;
    }

    public void setMax(final int max)
    {
        this.max = max;
    }

    public List<ComboItem> getItems()
    {
        return items;
    }

    public void setItems(final List<ComboItem> items)
    {
        this.items = items;
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

        final ComboGroup group = (ComboGroup) o;

        if (min != group.min)
        {
            return false;
        }
        if (max != group.max)
        {
            return false;
        }
        return name != null ? name.equals(group.name) : group.name == null;
    }

    public int hashCode()
    {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + min;
        result = 31 * result + max;
        return result;
    }

    public String buildDisplayName()
    {
        if (min ==  0 && max == 0)
        {
            return name;
        }

        if (min>0 && max==0)
        {
            return App.getContext().getString(R.string.combo_group_name_hint_min, name, min);
        }

        if (max>0 && min==0)
        {
            return App.getContext().getString(R.string.combo_group_name_hint_max, name, min);
        }

        return App.getContext().getString(R.string.combo_group_name_hint_minmax, name, min, max);
    }
}

package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 09/10/2017
 */
public class DineplanMenuContainer
{
    @SerializedName("Categories")
    List<DineplanMenuCategory> categories = new ArrayList<>();

    @SerializedName("OrderTagGroups")
    List<DineplanTagGroup> tagGroups = new ArrayList<>();

    @SerializedName("NonMenuItems")
    List<DineplanMenuItem> nonMenuItems = new ArrayList<>();

    public DineplanMenuContainer()
    {
    }

    public List<DineplanMenuCategory> getCategories()
    {
        return categories;
    }

    public void setMenu(final List<DineplanMenuCategory> categories)
    {
        this.categories = categories;
    }

    public void setCategories(final List<DineplanMenuCategory> categories)
    {
        this.categories = categories;
    }

    public List<DineplanTagGroup> getTagGroups()
    {
        return tagGroups;
    }

    public void setTagGroups(final List<DineplanTagGroup> tagGroups)
    {
        this.tagGroups = tagGroups;
    }

    public List<DineplanMenuItem> getNonMenuItems() {
        return nonMenuItems;
    }

    public void setNonMenuItems(List<DineplanMenuItem> nonMenuItems) {
        this.nonMenuItems = nonMenuItems;
    }
}

package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
public class DineplanMenuCategory
{

    @SerializedName("Id")
    int id;

    @SerializedName("MenuItems")
    List<DineplanMenuItem> items = new ArrayList<>();

    @SerializedName("UserString")
    String name;

    @SerializedName("SortOrder")
    int weight;

    @SerializedName("ImagePath")
    String picture;

    @SerializedName("Description")
    String description;

    @SerializedName("OrderTagGroups")
    List<DineplanTagGroup> tagGroups = new ArrayList<>();

    public DineplanMenuCategory()
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

    public List<DineplanMenuItem> getItems()
    {
        return items;
    }

    public void setItems(final List<DineplanMenuItem> items)
    {
        this.items = items;
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

    public String getPicture()
    {
        return picture;
    }

    public void setPicture(final String picture)
    {
        this.picture = picture;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public List<DineplanTagGroup> getTagGroups()
    {
        return tagGroups;
    }

    public void setTagGroups(final List<DineplanTagGroup> tagGroups)
    {
        this.tagGroups = tagGroups;
    }
}

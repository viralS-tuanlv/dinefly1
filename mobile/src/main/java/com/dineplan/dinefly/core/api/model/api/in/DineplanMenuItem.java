package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
public class DineplanMenuItem
{

    @SerializedName("Id")
    int id;

    @SerializedName("UserString")
    String name;

    @SerializedName("AliasCode")
    String alias;

    @SerializedName("ImagePath")
    String pictrue;

    @SerializedName("Description")
    String description;

    @SerializedName("IsFavorite")
    boolean favorite;

    @SerializedName("SubTag")
    String subTag;

    @SerializedName("SortOrder")
    int weight;

    @SerializedName(value = "ItemType", alternate = "itemType")
    int itemType;

    @SerializedName("MenuPortions")
    List<DineplanMenuPortion> portions = new ArrayList<>();

    @SerializedName("OrderTagGroups")
    List<DineplanTagGroup> tagGroups = new ArrayList<>();

    public DineplanMenuItem()
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

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getAlias()
    {
        return alias;
    }

    public void setAlias(final String alias)
    {
        this.alias = alias;
    }

    public String getPictrue()
    {
        return pictrue;
    }

    public void setPictrue(final String pictrue)
    {
        this.pictrue = pictrue;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public boolean isFavorite()
    {
        return favorite;
    }

    public void setFavorite(final boolean favorite)
    {
        this.favorite = favorite;
    }

    public int getWeight()
    {
        return weight;
    }

    public void setWeight(final int weight)
    {
        this.weight = weight;
    }

    public List<DineplanMenuPortion> getPortions()
    {
        return portions;
    }

    public void setPortions(final List<DineplanMenuPortion> portions)
    {
        this.portions = portions;
    }

    public List<DineplanTagGroup> getTagGroups()
    {
        return tagGroups;
    }

    public void setTagGroups(final List<DineplanTagGroup> tagGroups)
    {
        this.tagGroups = tagGroups;
    }

    public int getItemType()
    {
        return itemType;
    }

    public void setItemType(final int itemType)
    {
        this.itemType = itemType;
    }

    public String getSubTag()
    {
        return subTag;
    }

    public void setSubTag(String subTag)
    {
        this.subTag = subTag;
    }
}

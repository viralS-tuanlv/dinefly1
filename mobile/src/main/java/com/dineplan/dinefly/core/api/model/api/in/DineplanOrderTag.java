package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
public class DineplanOrderTag
{
    @SerializedName("Id")
    int id;

    @SerializedName("Price")
    float price;

    @SerializedName("UserString")
    String name;

    @SerializedName("SortOrder")
    int weight;

    public DineplanOrderTag()
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

    public float getPrice()
    {
        return price;
    }

    public void setPrice(final float price)
    {
        this.price = price;
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
}

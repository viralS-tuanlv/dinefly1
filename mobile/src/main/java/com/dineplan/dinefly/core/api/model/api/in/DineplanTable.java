package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
public class DineplanTable
{
    @SerializedName("EntityId")
    int id;

    @SerializedName("EntityName")
    String name;

    @SerializedName("EntityScreenId")
    int hallId;

    @SerializedName("EntityScreenName")
    String hallName;

    @SerializedName("SortOrder")
    int weight;

    @SerializedName("ButtonHeight")
    int buttonHeight;

    @SerializedName("Occupied")
    boolean occupied;

    public DineplanTable()
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

    public int getHallId()
    {
        return hallId;
    }

    public void setHallId(final int hallId)
    {
        this.hallId = hallId;
    }

    public String getHallName()
    {
        return hallName;
    }

    public void setHallName(final String hallName)
    {
        this.hallName = hallName;
    }

    public int getWeight()
    {
        return weight;
    }

    public void setWeight(final int weight)
    {
        this.weight = weight;
    }

    public int getButtonHeight()
    {
        return buttonHeight;
    }

    public void setButtonHeight(final int buttonHeight)
    {
        this.buttonHeight = buttonHeight;
    }

    public boolean isOccupied()
    {
        return occupied;
    }

    public void setOccupied(final boolean occupied)
    {
        this.occupied = occupied;
    }


}

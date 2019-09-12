package com.dineplan.dinefly.core.api.model.api.out;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 27/11/2017
 */
public class DineplanSoldoutCheckResponse
{

    @SerializedName("soldOut")
    List<DineplanSoldoutCheckItem> items = new ArrayList<>();

    public DineplanSoldoutCheckResponse(List<DineplanSoldoutCheckItem> sochecks)
    {
        items.addAll(sochecks);
    }

    public List<DineplanSoldoutCheckItem> getItems()
    {
        return items;
    }

    public void setItems(List<DineplanSoldoutCheckItem> items)
    {
        this.items = items;
    }
}

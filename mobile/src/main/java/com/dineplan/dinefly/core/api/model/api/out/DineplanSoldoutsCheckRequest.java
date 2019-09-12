package com.dineplan.dinefly.core.api.model.api.out;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 21/10/2017
 */
public class DineplanSoldoutsCheckRequest
{

    @SerializedName("orders")
    List<DineplanSoldoutCheckRequest> orders = new ArrayList<>();


    public DineplanSoldoutsCheckRequest()
    {
    }

}

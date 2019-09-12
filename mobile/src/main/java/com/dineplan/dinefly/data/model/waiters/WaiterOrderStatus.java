package com.dineplan.dinefly.data.model.waiters;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 13/10/2017
 */
public enum WaiterOrderStatus
{
    Draft(0), Created(1), Closed(2);

    final int type;

    WaiterOrderStatus(int type)
    {
        this.type = type;
    }
}

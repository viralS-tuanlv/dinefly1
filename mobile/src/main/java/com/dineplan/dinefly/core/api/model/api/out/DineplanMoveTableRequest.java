package com.dineplan.dinefly.core.api.model.api.out;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 21/10/2017
 */
public class DineplanMoveTableRequest
{

    @SerializedName("ticketId")
    long ticketId;

    @SerializedName("entityId")
    long tableId;

    public DineplanMoveTableRequest()
    {
    }

    public DineplanMoveTableRequest(final long ticketId, final long tableId)
    {
        this.ticketId = ticketId;
        this.tableId = tableId;
    }

    public long getTicketId()
    {
        return ticketId;
    }

    public void setTicketId(final long ticketId)
    {
        this.ticketId = ticketId;
    }

    public long getTableId()
    {
        return tableId;
    }

    public void setTableId(final long tableId)
    {
        this.tableId = tableId;
    }
}

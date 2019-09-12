package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
public class DineplanTableSummary
{

    @SerializedName("entityId")
    int entityId;

    @SerializedName("entityName")
    String name;

    @SerializedName("entityScreenId")
    int screenId;

    @SerializedName("entityScreenName")
    String screenName;

    @SerializedName("sortOrder")
    int weight;

    @SerializedName("occupied")
    boolean busy;

    @SerializedName("ticketData")
    List<DineplanTicketInfo> tickets = new ArrayList<>();

    public DineplanTableSummary()
    {
    }

    public int getEntityId()
    {
        return entityId;
    }

    public void setEntityId(final int entityId)
    {
        this.entityId = entityId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public int getScreenId()
    {
        return screenId;
    }

    public void setScreenId(final int screenId)
    {
        this.screenId = screenId;
    }

    public String getScreenName()
    {
        return screenName;
    }

    public void setScreenName(final String screenName)
    {
        this.screenName = screenName;
    }

    public int getWeight()
    {
        return weight;
    }

    public void setWeight(final int weight)
    {
        this.weight = weight;
    }

    public boolean isBusy()
    {
        return busy;
    }

    public void setBusy(final boolean busy)
    {
        this.busy = busy;
    }

    public List<DineplanTicketInfo> getTickets()
    {
        return tickets;
    }

    public void setTickets(final List<DineplanTicketInfo> tickets)
    {
        this.tickets = tickets;
    }

    public boolean containsTicket(final long id)
    {
        if (tickets != null)
        {
            for (DineplanTicketInfo info : tickets)
            {
                if (id == info.getId())
                {
                    return true;
                }
            }
        }

        return false;
    }
}

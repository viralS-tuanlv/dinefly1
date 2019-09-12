package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 21/10/2017
 */
public class DineplanTicket
{
    @SerializedName("id")
    int id;

    @SerializedName("entity")
    int tableId;

    @SerializedName("departmentId")
    int departmentId;

    @SerializedName("terminalId")
    int terminalId;

    @SerializedName("createdTime")
    Date created;

    @SerializedName("lastOrderDate")
    Date lastOrdered;

    @SerializedName("lastPaymentDate")
    Date lastPaid;

    @SerializedName("isLocked")
    boolean locked;

    @SerializedName("isClosed")
    boolean closed;

    @SerializedName("totalAmount")
    float amount;

    @SerializedName("note")
    String notes;

    @SerializedName("flyEntityStatus")
    int entityStatus;

    @SerializedName("flyTicketStatus")
    int ticketStatus;

    @SerializedName("orders")
    List<DineplanTicketOrder> orders = new ArrayList<>();

    @SerializedName("refId")
    int referenceId;

    @SerializedName("isPaid")
    boolean paid;

    @SerializedName("ticketTags")
    String flags;

    @SerializedName("userName")
    String user;

    @SerializedName("pax")
    String pax;

    @SerializedName("waiter")
    String waiter;

    @SerializedName("isDineCall")
    boolean dineCall;

//    "transactions": null
//    "entities": null,
//    "calculations": [],


    public DineplanTicket()
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

    public int getTableId()
    {
        return tableId;
    }

    public void setTableId(final int tableId)
    {
        this.tableId = tableId;
    }

    public int getDepartmentId()
    {
        return departmentId;
    }

    public void setDepartmentId(final int departmentId)
    {
        this.departmentId = departmentId;
    }

    public int getTerminalId()
    {
        return terminalId;
    }

    public void setTerminalId(final int terminalId)
    {
        this.terminalId = terminalId;
    }

    public Date getCreated()
    {
        return created;
    }

    public void setCreated(final Date created)
    {
        this.created = created;
    }

    public Date getLastOrdered()
    {
        return lastOrdered;
    }

    public void setLastOrdered(final Date lastOrdered)
    {
        this.lastOrdered = lastOrdered;
    }

    public Date getLastPaid()
    {
        return lastPaid;
    }

    public void setLastPaid(final Date lastPaid)
    {
        this.lastPaid = lastPaid;
    }

    public boolean isLocked()
    {
        return locked;
    }

    public void setLocked(final boolean locked)
    {
        this.locked = locked;
    }

    public boolean isClosed()
    {
        return closed;
    }

    public void setClosed(final boolean closed)
    {
        this.closed = closed;
    }

    public float getAmount()
    {
        return amount;
    }

    public void setAmount(final float amount)
    {
        this.amount = amount;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(final String notes)
    {
        this.notes = notes;
    }

    public int getEntityStatus()
    {
        return entityStatus;
    }

    public void setEntityStatus(final int entityStatus)
    {
        this.entityStatus = entityStatus;
    }

    public int getTicketStatus()
    {
        return ticketStatus;
    }

    public void setTicketStatus(final int ticketStatus)
    {
        this.ticketStatus = ticketStatus;
    }

    public List<DineplanTicketOrder> getOrders()
    {
        return orders;
    }

    public void setOrders(final List<DineplanTicketOrder> orders)
    {
        this.orders = orders;
    }

    public int getReferenceId()
    {
        return referenceId;
    }

    public void setReferenceId(final int referenceId)
    {
        this.referenceId = referenceId;
    }

    public boolean isPaid()
    {
        return paid;
    }

    public void setPaid(final boolean paid)
    {
        this.paid = paid;
    }

    public String getFlags()
    {
        return flags;
    }

    public void setFlags(final String flags)
    {
        this.flags = flags;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(final String user)
    {
        this.user = user;
    }

    public String getPax()
    {
        return pax;
    }

    public void setPax(final String pax)
    {
        this.pax = pax;
    }

    public String getWaiter()
    {
        return waiter;
    }

    public void setWaiter(final String waiter)
    {
        this.waiter = waiter;
    }

    public boolean isDineCall()
    {
        return dineCall;
    }

    public void setDineCall(final boolean dineCall)
    {
        this.dineCall = dineCall;
    }
}

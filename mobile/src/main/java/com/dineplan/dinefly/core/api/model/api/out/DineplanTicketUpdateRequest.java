package com.dineplan.dinefly.core.api.model.api.out;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 21/10/2017
 */
public class DineplanTicketUpdateRequest
{

    @SerializedName("id")
    int id;

    @SerializedName("EntityId")
    int tableId;

    @SerializedName("note")
    String notes;

    @SerializedName("orders")
    List<DineplanTicketOrderUpdateRequest> orders = new ArrayList<>();

    @SerializedName("totalAmount")
    private float totalAmount;

    @SerializedName("pax")
    String pax;

    @SerializedName("waiter")
    String waiter;

    @SerializedName("DepartmentId")
    int departmentId;

    @SerializedName("closingTag")
    String closingTag;

    public DineplanTicketUpdateRequest()
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

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(final String notes)
    {
        this.notes = notes;
    }

    public List<DineplanTicketOrderUpdateRequest> getOrders()
    {
        return orders;
    }

    public void setOrders(final List<DineplanTicketOrderUpdateRequest> orders)
    {
        this.orders = orders;
    }

    public void setTotalAmount(final float totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public float getTotalAmount()
    {
        return totalAmount;
    }

    public String getWaiter()
    {
        return waiter;
    }

    public void setWaiter(final String waiter)
    {
        this.waiter = waiter;
    }

    public String getPax()
    {
        return pax;
    }

    public void setPax(final String pax)
    {
        this.pax = pax;
    }

    public int getDepartmentId()
    {
        return departmentId;
    }

    public void setDepartmentId(final int departmentId)
    {
        this.departmentId = departmentId;
    }

    public String getClosingTag()
    {
        return closingTag;
    }

    public void setClosingTag(final String closingTag)
    {
        this.closingTag = closingTag;
    }
}

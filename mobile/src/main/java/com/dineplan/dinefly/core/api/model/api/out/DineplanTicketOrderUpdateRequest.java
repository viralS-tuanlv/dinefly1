package com.dineplan.dinefly.core.api.model.api.out;

import com.dineplan.dinefly.core.api.model.api.in.DineplanTicketOrderTag;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 21/10/2017
 */
public class DineplanTicketOrderUpdateRequest
{
    @SerializedName("id")
    int id;

    @SerializedName("orderNumber")
    int orderNumber;

    @SerializedName("orderRef")
    int orderRef;

    @SerializedName("refId")
    int refId;

    @SerializedName("flyOrderStatus")
    String orderStatus;

    @SerializedName("orderStatus")
    List<Integer> orderStatusCodes = new ArrayList<>();

    @SerializedName("menuItemId")
    int menuItemId;

    @SerializedName("menuItemName")
    String menuItemName;

    @SerializedName("portionName")
    String portionName;

    @SerializedName("price")
    float price;

    @SerializedName("Quantity")
    float qty;

    @SerializedName("menuItemType")
    Integer menuItemType;

    @SerializedName("note")
    String notes;

    @SerializedName("orderTagItems")
    List<DineplanTicketOrderTag> tags = new ArrayList<>();

    @SerializedName("userName")
    String user;

    public DineplanTicketOrderUpdateRequest()
    {
    }

    public int getRefId() {
        return refId;
    }

    public void setRefId(int refId) {
        this.refId = refId;
    }

    public int getId()
    {
        return id;
    }

    public void setId(final int id)
    {
        this.id = id;
    }

    public int getOrderNumber()
    {
        return orderNumber;
    }

    public void setOrderNumber(final int orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    public int getMenuItemId()
    {
        return menuItemId;
    }

    public void setMenuItemId(final int menuItemId)
    {
        this.menuItemId = menuItemId;
    }

    public String getMenuItemName()
    {
        return menuItemName;
    }

    public void setMenuItemName(final String menuItemName)
    {
        this.menuItemName = menuItemName;
    }

    public String getPortionName()
    {
        return portionName;
    }

    public void setPortionName(final String portionName)
    {
        this.portionName = portionName;
    }

    public float getPrice()
    {
        return price;
    }

    public void setPrice(final float price)
    {
        this.price = price;
    }

    public float getQty()
    {
        return qty;
    }

    public void setQty(final float qty)
    {
        this.qty = qty;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(final String notes)
    {
        this.notes = notes;
    }

    public String getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public List<Integer> getOrderStatusCodes()
    {
        return orderStatusCodes;
    }

    public void setOrderStatusCodes(final List<Integer> orderStatusCodes)
    {
        this.orderStatusCodes = orderStatusCodes;
    }

    public List<DineplanTicketOrderTag> getTags()
    {
        return tags;
    }

    public void setTags(final List<DineplanTicketOrderTag> tags)
    {
        this.tags = tags;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(final String user)
    {
        this.user = user;
    }

    public Integer getMenuItemType()
    {
        return menuItemType;
    }

    public void setMenuItemType(final Integer menuItemType)
    {
        this.menuItemType = menuItemType;
    }

    public int getOrderRef()
    {
        return orderRef;
    }

    public void setOrderRef(final int orderRef)
    {
        this.orderRef = orderRef;
    }
}

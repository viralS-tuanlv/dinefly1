package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 21/10/2017
 */
public class DineplanTicketOrder
{
    @SerializedName("id")
    int id;

    @SerializedName("orderNumber")
    int orderNumber;

    @SerializedName("menuItemId")
    int menuItemId;

    @SerializedName("menuItemName")
    String menuItemName;

    @SerializedName("portionName")
    String portionName;

    @SerializedName("price")
    float price;

    @SerializedName("tax")
    float tax;

    @SerializedName("quantity")
    float qty;

    @SerializedName("note")
    String notes;

    @SerializedName("flyOrderStatus")
    String orderStatus;

    @SerializedName("orderStatus")
    List<Integer> orderStatusCodes = new ArrayList<>();

    @SerializedName("orderTagItems")
    List<DineplanTicketOrderTag> tags = new ArrayList<>();

    @SerializedName("userName")
    String user;

    @SerializedName("refId")
    int referenceId;

    @SerializedName("orderRef")
    int orderRef;

    @SerializedName("menuItemType")
    int menuItemType;

    public DineplanTicketOrder()
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

    public float getTax()
    {
        return tax;
    }

    public void setTax(final float tax)
    {
        this.tax = tax;
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

    public int getReferenceId()
    {
        return referenceId;
    }

    public void setReferenceId(final int referenceId)
    {
        this.referenceId = referenceId;
    }

    public boolean isVoid()
    {
        return orderStatusCodes!=null && orderStatusCodes.contains(4);
    }

    public boolean isGift()
    {
        return orderStatusCodes!=null && orderStatusCodes.contains(5);
    }

    public int getOrderRef()
    {
        return orderRef;
    }

    public void setOrderRef(final int orderRef)
    {
        this.orderRef = orderRef;
    }

    public int getMenuItemType()
    {
        return menuItemType;
    }

    public void setMenuItemType(final int menuItemType)
    {
        this.menuItemType = menuItemType;
    }
}

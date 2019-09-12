package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 21/10/2017
 */
public class DineplanTicketInfo
{

    @SerializedName("id")
    int id;

    @SerializedName("ticketNumber")
    String number;

    @SerializedName("remainingAmount")
    float amountUnpaid;

    public DineplanTicketInfo()
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

    public String getNumber()
    {
        return number;
    }

    public void setNumber(final String number)
    {
        this.number = number;
    }

    public float getAmountUnpaid()
    {
        return amountUnpaid;
    }

    public void setAmountUnpaid(final float amountUnpaid)
    {
        this.amountUnpaid = amountUnpaid;
    }
}

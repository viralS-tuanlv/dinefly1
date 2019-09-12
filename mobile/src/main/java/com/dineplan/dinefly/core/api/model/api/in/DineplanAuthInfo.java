package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 07/10/2017
 */
public class DineplanAuthInfo
{
    @SerializedName("userId")
    private long userId;

    @SerializedName("isAdmin")
    private boolean isAdmin;

    @SerializedName("name")
    private String name;

    @SerializedName("deviceName")
    private String deviceName;

    @SerializedName("token")
    private String token;

    @SerializedName("saleOpen")
    private Boolean saleOpen;

    @SerializedName("currencyCode")
    private String currencyCode;

    @SerializedName("serviceInterval")
    private long serviceInterval;

    @SerializedName("uIInterval")
    private long uIInterval;

    @SerializedName("printerAddress")
    private String printerAddress;

    @SerializedName("printKotLocal")
    private boolean printKotLocal;

    @SerializedName("printBillLocal")
    private boolean printBillLocal;

    @SerializedName("printerPaperSize")
    private long printerPaperSize;

    @SerializedName("pAX")
    private String pAX;

    @SerializedName("waiter")
    private String waiter;

    @SerializedName("department")
    private long department;

    @SerializedName("ticketMethod")
    private long ticketMethod;

    @SerializedName("stopNewTicket")
    private boolean stopNewTicket;

    @SerializedName("closingTag")
    String closingTag;


    public DineplanAuthInfo()
    {
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(final long userId)
    {
        this.userId = userId;
    }

    public boolean isAdmin()
    {
        return isAdmin;
    }

    public void setAdmin(final boolean admin)
    {
        isAdmin = admin;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getDeviceName()
    {
        return deviceName;
    }

    public void setDeviceName(final String deviceName)
    {
        this.deviceName = deviceName;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(final String token)
    {
        this.token = token;
    }

    public Boolean getSaleOpen()
    {
        return saleOpen;
    }

    public void setSaleOpen(final Boolean saleOpen)
    {
        this.saleOpen = saleOpen;
    }

    public String getCurrencyCode()
    {
        return currencyCode;
    }

    public void setCurrencyCode(final String currencyCode)
    {
        this.currencyCode = currencyCode;
    }

    public long getServiceInterval()
    {
        return serviceInterval;
    }

    public void setServiceInterval(final long serviceInterval)
    {
        this.serviceInterval = serviceInterval;
    }

    public long getuIInterval()
    {
        return uIInterval;
    }

    public void setuIInterval(final long uIInterval)
    {
        this.uIInterval = uIInterval;
    }

    public String getPrinterAddress()
    {
        return printerAddress;
    }

    public void setPrinterAddress(final String printerAddress)
    {
        this.printerAddress = printerAddress;
    }

    public boolean isPrintKotLocal()
    {
        return printKotLocal;
    }

    public void setPrintKotLocal(final boolean printKotLocal)
    {
        this.printKotLocal = printKotLocal;
    }

    public boolean isPrintBillLocal()
    {
        return printBillLocal;
    }

    public void setPrintBillLocal(final boolean printBillLocal)
    {
        this.printBillLocal = printBillLocal;
    }

    public long getPrinterPaperSize()
    {
        return printerPaperSize;
    }

    public void setPrinterPaperSize(final long printerPaperSize)
    {
        this.printerPaperSize = printerPaperSize;
    }

    public String getpAX()
    {
        return pAX;
    }

    public void setpAX(final String pAX)
    {
        this.pAX = pAX;
    }

    public String getWaiter()
    {
        return waiter;
    }

    public void setWaiter(final String waiter)
    {
        this.waiter = waiter;
    }

    public long getDepartment()
    {
        return department;
    }

    public void setDepartment(final long department)
    {
        this.department = department;
    }

    public long getTicketMethod()
    {
        return ticketMethod;
    }

    public void setTicketMethod(final long ticketMethod)
    {
        this.ticketMethod = ticketMethod;
    }

    public boolean isStopNewTicket()
    {
        return stopNewTicket;
    }

    public void setStopNewTicket(final boolean stopNewTicket)
    {
        this.stopNewTicket = stopNewTicket;
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

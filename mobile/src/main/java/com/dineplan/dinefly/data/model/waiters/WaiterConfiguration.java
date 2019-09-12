package com.dineplan.dinefly.data.model.waiters;

import com.crashlytics.android.Crashlytics;
import com.dineplan.dinefly.core.api.model.api.in.DineplanAuthInfo;
import com.dineplan.dinefly.core.api.model.api.in.DineplanComboMenuContainer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.livotov.labs.android.robotools.crypt.RTCryptUtil;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */

@Entity
public class WaiterConfiguration
{

    @Id
    long pk;

    String dineplanEndpoint;

    String deviceId;

    int userId;

    boolean authenticated;

    String userName;

    String terminalName;

    String terminalToken;

    boolean salesOpen;

    String currencySymbol;

    int serviceInterval;

    int uiInternal;

    String printerAddress;

    boolean canPrintLocalKots;

    boolean canPrintLocalBolls;

    int printerPaperWidth;

    String pax;

    String waiter;

    int departmentId;

    String departmentName;

    int ticketingMethod;

    boolean stopNewTicket;

    boolean administrator;

    String authenticatedSecretHash;

    Date lastMenuSync;

    String closingTag;

    String comboData;

    public WaiterConfiguration()
    {
    }


    public String getDineplanEndpoint()
    {
        return dineplanEndpoint;
    }

    public void setDineplanEndpoint(final String dineplanEndpoint)
    {
        this.dineplanEndpoint = dineplanEndpoint;
    }

    public String getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(final String deviceId)
    {
        this.deviceId = deviceId;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(final int userId)
    {
        this.userId = userId;
    }

    public boolean isAuthenticated()
    {
        return authenticated;
    }

    public void setAuthenticated(final boolean authenticated)
    {
        this.authenticated = authenticated;

        if (!authenticated)
        {
            authenticatedSecretHash = null;
        }
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(final String userName)
    {
        this.userName = userName;
    }

    public String getTerminalName()
    {
        return terminalName;
    }

    public void setTerminalName(final String terminalName)
    {
        this.terminalName = terminalName;
    }

    public String getTerminalToken()
    {
        return terminalToken;
    }

    public void setTerminalToken(final String terminalToken)
    {
        this.terminalToken = terminalToken;
    }

    public boolean isSalesOpen()
    {
        return salesOpen;
    }

    public void setSalesOpen(final boolean salesOpen)
    {
        this.salesOpen = salesOpen;
    }

    public String getCurrencySymbol()
    {
        return currencySymbol;
    }

    public void setCurrencySymbol(final String currencySymbol)
    {
        this.currencySymbol = currencySymbol;
    }

    public int getServiceInterval()
    {
        return serviceInterval;
    }

    public void setServiceInterval(final int serviceInterval)
    {
        this.serviceInterval = serviceInterval;
    }

    public int getUiInternal()
    {
        return uiInternal;
    }

    public void setUiInternal(final int uiInternal)
    {
        this.uiInternal = uiInternal;
    }

    public String getPrinterAddress()
    {
        return printerAddress;
    }

    public void setPrinterAddress(final String printerAddress)
    {
        this.printerAddress = printerAddress;
    }

    public boolean isCanPrintLocalKots()
    {
        return canPrintLocalKots;
    }

    public void setCanPrintLocalKots(final boolean canPrintLocalKots)
    {
        this.canPrintLocalKots = canPrintLocalKots;
    }

    public boolean isCanPrintLocalBolls()
    {
        return canPrintLocalBolls;
    }

    public void setCanPrintLocalBolls(final boolean canPrintLocalBolls)
    {
        this.canPrintLocalBolls = canPrintLocalBolls;
    }

    public int getPrinterPaperWidth()
    {
        return printerPaperWidth;
    }

    public void setPrinterPaperWidth(final int printerPaperWidth)
    {
        this.printerPaperWidth = printerPaperWidth;
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

    public int getDepartmentId()
    {
        return departmentId;
    }

    public void setDepartmentId(final int departmentId)
    {
        this.departmentId = departmentId;
    }

    public String getDepartmentName()
    {
        return departmentName;
    }

    public void setDepartmentName(final String departmentName)
    {
        this.departmentName = departmentName;
    }

    public int getTicketingMethod()
    {
        return ticketingMethod;
    }

    public void setTicketingMethod(final int ticketingMethod)
    {
        this.ticketingMethod = ticketingMethod;
    }

    public boolean isStopNewTicket()
    {
        return stopNewTicket;
    }

    public void setStopNewTicket(final boolean stopNewTicket)
    {
        this.stopNewTicket = stopNewTicket;
    }

    public boolean isAdministrator()
    {
        return administrator;
    }

    public void setAdministrator(final boolean administrator)
    {
        this.administrator = administrator;
    }

    public Date getLastMenuSync()
    {
        return lastMenuSync;
    }

    public void setLastMenuSync(final Date lastMenuSync)
    {
        this.lastMenuSync = lastMenuSync;
    }

    public String getClosingTag()
    {
        return closingTag;
    }

    public void setClosingTag(final String closingTag)
    {
        this.closingTag = closingTag;
    }

    public String getAuthenticatedSecretHash()
    {
        return authenticatedSecretHash;
    }

    public List<String> getPaxData()
    {
        Type type = new TypeToken<ArrayList<String>>()
        {
        }.getType();

        try
        {
            List<String> list = new Gson().fromJson(("" + pax), type);
            return list != null ? list : new ArrayList<String>();
        } catch (Throwable ignored)
        {

        }

        return new ArrayList<>();
    }

    public List<String> getWaiterData()
    {
        Type type = new TypeToken<ArrayList<String>>()
        {
        }.getType();

        try
        {
            final List<String> list = new Gson().fromJson(("" + waiter), type);
            return list != null ? list : new ArrayList<String>();
        } catch (Throwable ignored)
        {

        }

        return new ArrayList<>();
    }

    public List<String> getClosingTagData()
    {
        Type type = new TypeToken<ArrayList<String>>()
        {
        }.getType();

        try
        {
            final List<String> list = new Gson().fromJson(("" + closingTag), type);
            return list != null ? list : new ArrayList<String>();
        } catch (Throwable ignored)
        {
        }

        List<String> emptyData = new ArrayList<>();
        emptyData.add("");
        return emptyData;
    }

    public boolean hasPaxData()
    {
        return getPaxData().size() > 0;
    }

    public boolean hasWaiterData()
    {
        return getWaiterData().size() > 0;
    }

    public void setupFromAuthInfo(final DineplanAuthInfo info, final String secret)
    {
        setUserId((int) info.getUserId());
        setAuthenticated(info.getUserId() != 0);
        setUserName(info.getName());
        setTerminalName(info.getName());
        setTerminalToken(info.getToken());
        setSalesOpen(info.getSaleOpen());
        setCurrencySymbol(info.getCurrencyCode());
        setServiceInterval((int) info.getServiceInterval());
        setUiInternal((int) info.getuIInterval());
        setPrinterAddress(info.getPrinterAddress());
        setCanPrintLocalKots(info.isPrintKotLocal());
        setCanPrintLocalBolls(info.isPrintBillLocal());
        setPrinterPaperWidth((int) info.getPrinterPaperSize());
        setPax(info.getpAX());
        setWaiter(info.getWaiter());
        setDepartmentId((int) info.getDepartment());
        setDepartmentName("" + info.getDepartment());
        setTicketingMethod((int) info.getTicketMethod());
        setStopNewTicket(info.isStopNewTicket());
        setAdministrator(info.isAdmin());
        setClosingTag(info.getClosingTag());

        authenticatedSecretHash = RTCryptUtil.md5("" + secret);
    }

    public boolean verifyAuthenticationSecret(final String secret)
    {
        return authenticatedSecretHash == null || RTCryptUtil.md5("" + secret).equalsIgnoreCase(authenticatedSecretHash);
    }

    public void putComboData(final List<DineplanComboMenuContainer> data)
    {
        if (data != null)
        {
            comboData = new Gson().toJson(data);
        } else
        {
            comboData = null;
        }
    }

    public List<DineplanComboMenuContainer> getComboData()
    {
        if (comboData != null)
        {
            Type type = new TypeToken<ArrayList<DineplanComboMenuContainer>>()
            {
            }.getType();

            try
            {
                return new Gson().fromJson(comboData, type);
            } catch (Throwable err)
            {
                Crashlytics.logException(err);
                err.printStackTrace();
            }
        }

        return null;
    }

    public DineplanComboMenuContainer getComboDataFor(int itemId) {
        final List<DineplanComboMenuContainer> comboData = getComboData();
        if (comboData!=null) {
            for (DineplanComboMenuContainer dt : comboData) {
                if (dt.getMenuItemId() == itemId) return dt;
            }
        }
        return null;
    }
}

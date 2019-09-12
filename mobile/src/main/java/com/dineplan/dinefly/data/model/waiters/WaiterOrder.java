package com.dineplan.dinefly.data.model.waiters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.converter.PropertyConverter;
import io.objectbox.relation.ToMany;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 13/10/2017
 */

@Entity
public class WaiterOrder
{

    @Id
    long id;

    @Index
    long dineplanOrderId;

    @Convert(converter = WaiterOrderStatusTypeConverter.class, dbType = Integer.class)
    WaiterOrderStatus status;

    @Index
    long tableId;

    @Backlink
    ToMany<WaiterOrderSaleItem> items;

    String lastSycnedDineplanData;
    Date created;
    String notes;
    String pax;
    String waiter;
    float serverComputedAmount;

    public WaiterOrder()
    {
    }

    public long getId()
    {
        return id;
    }

    public void setId(final long id)
    {
        this.id = id;
    }

    public long getDineplanOrderId()
    {
        return dineplanOrderId;
    }

    public void setDineplanOrderId(final long dineplanOrderId)
    {
        this.dineplanOrderId = dineplanOrderId;
    }

    public WaiterOrderStatus getStatus()
    {
        return status;
    }

    public void setStatus(final WaiterOrderStatus status)
    {
        this.status = status;
    }

    public long getTableId()
    {
        return tableId;
    }

    public void setTableId(final long tableId)
    {
        this.tableId = tableId;
    }

    public ToMany<WaiterOrderSaleItem> getItems()
    {
        return items;
    }

    public void setItems(final ToMany<WaiterOrderSaleItem> items)
    {
        this.items = items;
    }

    public String getLastSycnedDineplanData()
    {
        return lastSycnedDineplanData;
    }

    public void setLastSycnedDineplanData(final String lastSycnedDineplanData)
    {
        this.lastSycnedDineplanData = lastSycnedDineplanData;
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

    public int getPreordersCount()
    {
        int count = 0;

        for (WaiterOrderSaleItem item : items)
        {
            if (item.hasStatus(WaiterOrderItemStatus.DraftDelayed, WaiterOrderItemStatus.DraftImmediate))
            {
                count++;
            }
        }

        return count;
    }

    public int getCurrentOrderCount()
    {
        int count = 0;

        for (WaiterOrderSaleItem item : items)
        {
            if (!item.hasStatus(WaiterOrderItemStatus.DraftDelayed, WaiterOrderItemStatus.DraftImmediate))
            {
                count++;
            }
        }

        return count;
    }

    public float findTotal()
    {
        float total = serverComputedAmount;

        if (total == 0)
        {
            for (WaiterOrderSaleItem item : items)
            {
                total += item.getFinalPrice();

                for (WaiterOrderItemTag tag : item.getTags())
                {
                    total += tag.getAmount();
                }
            }
        }

        return total;
    }

    public void setCreated(final Date created)
    {
        this.created = created;
    }

    public Date getCreated()
    {
        return created;
    }

    public void setNotes(final String notes)
    {
        this.notes = notes;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setServerComputedAmount(final float serverComputedAmount)
    {
        this.serverComputedAmount = serverComputedAmount;
    }

    public float getServerComputedAmount()
    {
        return serverComputedAmount;
    }

    public List<WaiterOrderSaleItem> clearItemsButExtractShoppingCartOnes()
    {
        List<WaiterOrderSaleItem> data = new ArrayList<>();

        for (WaiterOrderSaleItem item : items)
        {
            if (item.hasStatus(WaiterOrderItemStatus.DraftDelayed,WaiterOrderItemStatus.DraftImmediate))
            {
                data.add(item);
            }
        }

        items.clear();

        for (WaiterOrderSaleItem item : data)
        {
            item.resetDatabaseRefLinks();
        }

        return data;
    }

    public static class WaiterOrderStatusTypeConverter implements PropertyConverter<WaiterOrderStatus, Integer>
    {

        @Override
        public WaiterOrderStatus convertToEntityProperty(Integer databaseValue)
        {
            if (databaseValue == null)
            {
                return null;
            }

            for (WaiterOrderStatus entityProperty : WaiterOrderStatus.values())
            {
                if (entityProperty.type == databaseValue)
                {
                    return entityProperty;
                }
            }

            return WaiterOrderStatus.Draft;
        }

        @Override
        public Integer convertToDatabaseValue(WaiterOrderStatus entityProperty)
        {
            return entityProperty == null ? null : entityProperty.type;
        }
    }

}

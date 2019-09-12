package com.dineplan.dinefly.data.model.waiters;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.NameInDb;
import io.objectbox.converter.PropertyConverter;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

import java.util.Date;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 13/10/2017
 */
@Entity
public class WaiterOrderSaleItem
{

    @Id
    long id;

    @Index
    int dineplanOrderId;

    ToOne<WaiterOrder> order;

    Date dateAdded;

    Date dateChanged;

    @Convert(converter = WaiterOrderItemStatusTypeConverter.class, dbType = Integer.class)
    WaiterOrderItemStatus status;

    String name;

    float qty;

    long menuItemid;

    long portionId;

    float potionAmount;

    float finalPrice;

    boolean voidOrder;

    boolean giftOrder;

    boolean combo;

    ToMany<WaiterOrderComboItem> comboItems;

    ToMany<WaiterOrderItemTag> tags;
    private String notes;
    private int dineplanOrderNumber;

    public WaiterOrderSaleItem()
    {
        dateAdded = new Date();
        dateChanged = new Date();
        status = WaiterOrderItemStatus.DraftImmediate;
        qty = 1;
        name = "";
        potionAmount = 0;
    }

    public WaiterOrderSaleItem(final WaiterMenuItem item)
    {
        dateAdded = new Date();
        dateChanged = new Date();
        status = WaiterOrderItemStatus.DraftImmediate;
        qty = 1;
        menuItemid = item.getItemId();
        portionId = item.getPortions().size() > 0 ? item.getPortions().get(0).getPortionId() : 0;
        name = String.format("%s %s", item.getName(), item.getPortions().size() > 0 ? item.getPortions().get(0).getName() : "");
        potionAmount = item.getPortions().size() > 0 ? item.getPortions().get(0).getPrice() : 0;
        combo = item.checkIfCombo();
    }

    public boolean isCombo()
    {
        return combo;
    }

    public ToMany<WaiterOrderComboItem> getComboItems()
    {
        return comboItems;
    }

    public long getId()
    {
        return id;
    }

    public void setId(final long id)
    {
        this.id = id;
    }

    public ToOne<WaiterOrder> getOrder()
    {
        return order;
    }

    public void setOrder(final ToOne<WaiterOrder> order)
    {
        this.order = order;
    }

    public Date getDateAdded()
    {
        return dateAdded;
    }

    public void setDateAdded(final Date dateAdded)
    {
        this.dateAdded = dateAdded;
    }

    public Date getDateChanged()
    {
        return dateChanged;
    }

    public void setDateChanged(final Date dateChanged)
    {
        this.dateChanged = dateChanged;
    }

    public WaiterOrderItemStatus getStatus()
    {
        return status;
    }

    public void setStatus(final WaiterOrderItemStatus status)
    {
        this.status = status;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public float getQty()
    {
        return qty;
    }

    public void setQty(final float qty)
    {
        this.qty = qty;
    }

    public long getMenuItemid()
    {
        return menuItemid;
    }

    public void setMenuItemid(final long menuItemid)
    {
        this.menuItemid = menuItemid;
    }

    public long getPortionId()
    {
        return portionId;
    }

    public void setPortionId(final long portionId)
    {
        this.portionId = portionId;
    }

    public float getPotionAmount()
    {
        return potionAmount;
    }

    public void setPotionAmount(final float potionAmount)
    {
        this.potionAmount = potionAmount;
    }

    public ToMany<WaiterOrderItemTag> getTags()
    {
        return tags;
    }

    public void setTags(final ToMany<WaiterOrderItemTag> tags)
    {
        this.tags = tags;
    }

    public int getDineplanOrderId()
    {
        return dineplanOrderId;
    }

    public void setDineplanOrderId(final int dineplanOrderId)
    {
        this.dineplanOrderId = dineplanOrderId;
    }

    public boolean isVoidOrder()
    {
        return voidOrder;
    }

    public void setVoidOrder(final boolean voidOrder)
    {
        this.voidOrder = voidOrder;
    }

    public boolean isGiftOrder()
    {
        return giftOrder;
    }

    public void setGiftOrder(final boolean giftOrder)
    {
        this.giftOrder = giftOrder;
    }

    public float getFinalPrice()
    {
        if (finalPrice == 0)
        {
            computePrice();
        }

        return finalPrice;
    }

    public void setFinalPrice(final float finalPrice)
    {
        this.finalPrice = finalPrice;
    }

    public boolean hasPortion(final WaiterMenuItemPortion portion)
    {
        if (portion != null)
        {
            return portionId == portion.getPortionId();
        } else
        {
            return portionId == 0;
        }
    }

    public boolean hasTag(final WaiterMenuItemTag tag)
    {
        if (tag != null)
        {
            for (WaiterOrderItemTag tg : tags)
            {
                if (tg.getTagId() == tag.getTagId())
                {
                    return true;
                }
            }

            return false;
        } else
        {
            return false;
        }
    }

    public boolean hasStatus(final WaiterOrderItemStatus... statuses)
    {
        for (WaiterOrderItemStatus st : statuses)
        {
            if (status == st)
            {
                return true;
            }
        }

        return false;
    }

    public void setNotes(final String notes)
    {
        this.notes = notes;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setDineplanOrderNumber(final int dineplanOrderNumber)
    {
        this.dineplanOrderNumber = dineplanOrderNumber;
    }

    public int getDineplanOrderNumber()
    {
        return dineplanOrderNumber;
    }

    public void computePrice()
    {
        if (combo)
        {
            for (WaiterOrderComboItem comboItem : comboItems)
            {
                finalPrice += comboItem.getFinalPrice();
            }
        } else
        {
            finalPrice = potionAmount;

            if (tags != null)
            {
                for (WaiterOrderItemTag orderedTag : tags)
                {
                    finalPrice += orderedTag.getAmount();
                }
            }
        }

        finalPrice = finalPrice * qty;
    }

    public int getTagQty(final WaiterMenuItemTag tag)
    {
        if (tag != null)
        {
            for (WaiterOrderItemTag tg : tags)
            {
                if (tg.getTagId() == tag.getTagId())
                {
                    return tg.qty;
                }
            }

            return 0;
        } else
        {
            return 0;
        }
    }

    public boolean hasSelectedCombo(final int menuItemId)
    {
        try
        {
            for (WaiterOrderComboItem comboItem : comboItems)
            {
                if (comboItem.getMenuItemid() == menuItemId)
                {
                    return true;
                }
            }
        } catch (Throwable err)
        {
            // ignored
        }

        return false;
    }

    public int getComboQty(final int menuItemId)
    {
        try
        {
            for (WaiterOrderComboItem comboItem : comboItems)
            {
                if (comboItem.getMenuItemid() == menuItemId)
                {
                    return (int)comboItem.qty;
                }
            }
        } catch (Throwable err)
        {
            // ignored
        }

        return 1;
    }

    public void resetDatabaseRefLinks()
    {
        order.setTarget(null);
    }

    public static class WaiterOrderItemStatusTypeConverter implements PropertyConverter<WaiterOrderItemStatus, Integer>
    {

        @Override
        public WaiterOrderItemStatus convertToEntityProperty(Integer databaseValue)
        {
            if (databaseValue == null)
            {
                return null;
            }

            for (WaiterOrderItemStatus entityProperty : WaiterOrderItemStatus.values())
            {
                if (entityProperty.type == databaseValue)
                {
                    return entityProperty;
                }
            }

            return WaiterOrderItemStatus.DraftImmediate;
        }

        @Override
        public Integer convertToDatabaseValue(WaiterOrderItemStatus entityProperty)
        {
            return entityProperty == null ? null : entityProperty.type;
        }
    }
}

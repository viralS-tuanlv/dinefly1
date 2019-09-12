package com.dineplan.dinefly.data.model.waiters;

import com.dineplan.dinefly.core.api.model.api.in.DineplanTable;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
@Entity
public class WaiterTable
{

    private static final int VIRTUAL_QUICKORDER_TABLE_ID = Integer.MAX_VALUE;

    @Id
    long pk;

    ToOne<WaiterRoom> room;

    int tableId;

    String name;

    boolean busy;

    public WaiterTable()
    {
    }

    public int getTableId()
    {
        return tableId;
    }

    public void setTableId(final int tableId)
    {
        this.tableId = tableId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public boolean isBusy()
    {
        return busy;
    }

    public void setBusy(final boolean busy)
    {
        this.busy = busy;
    }

    public ToOne<WaiterRoom> getRoom()
    {
        return room;
    }

    public static WaiterTable createFromDineplanTable(final DineplanTable dineplanTable)
    {
        WaiterTable table = new WaiterTable();

        table.setName(dineplanTable.getName());
        table.setTableId(dineplanTable.getId());
        table.setBusy(dineplanTable.isOccupied());

        return table;
    }

    @Override
    public String toString()
    {
        return "" + name;
    }

    public boolean isVirtualTable()
    {
        return pk == Long.MAX_VALUE;
    }

    public boolean isVirtualQuickOrderTable()
    {
        return tableId == VIRTUAL_QUICKORDER_TABLE_ID;
    }

    public static WaiterTable createQuickOrderVirtualTable()
    {
        WaiterTable t = new WaiterTable();
        t.name = "";
        t.busy = false;
        t.pk = Long.MAX_VALUE;
        t.tableId = VIRTUAL_QUICKORDER_TABLE_ID;

        return t;
    }
}

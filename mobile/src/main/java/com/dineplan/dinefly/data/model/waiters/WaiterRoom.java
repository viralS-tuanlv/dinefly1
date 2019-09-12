package com.dineplan.dinefly.data.model.waiters;

import com.dineplan.dinefly.core.api.model.api.in.DineplanTable;
import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
@Entity
public class WaiterRoom
{

    @Id
    long pk;

    int roomId;

    String name;

    @Backlink
    ToMany<WaiterTable> tables;

    public WaiterRoom()
    {
    }

    public int getRoomId()
    {
        return roomId;
    }

    public void setRoomId(final int roomId)
    {
        this.roomId = roomId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public ToMany<WaiterTable> getTables()
    {
        return tables;
    }

    public void setTables(final ToMany<WaiterTable> tables)
    {
        this.tables = tables;
    }

    public static WaiterRoom createFromDineplanTable(final DineplanTable dineplanTable)
    {
        WaiterRoom room = new WaiterRoom();

        room.setRoomId(dineplanTable.getHallId());
        room.setName(dineplanTable.getHallName());

        return room;
    }

    public int getVacantTablesCount()
    {
        int free = 0;

        for (WaiterTable table : tables)
        {
            free += table.isBusy() ? 0 : 1;
        }

        return free;
    }

    public int getBusyTablesCount()
    {
        int busy = 0;

        for (WaiterTable table : tables)
        {
            busy += table.isBusy() ? 1 : 0;
        }

        return busy;
    }
}

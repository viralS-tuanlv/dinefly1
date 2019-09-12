package com.dineplan.dinefly.core;

import com.crashlytics.android.Crashlytics;
import com.dineplan.dinefly.core.api.DineplanApiClient;
import com.dineplan.dinefly.core.api.model.api.in.*;
import com.dineplan.dinefly.core.api.model.api.out.DineplanTicketOrderUpdateRequest;
import com.dineplan.dinefly.core.api.model.api.out.DineplanTicketUpdateRequest;
import com.dineplan.dinefly.core.err.DineflyException;
import com.dineplan.dinefly.core.err.DineflyNetworkException;
import com.dineplan.dinefly.data.model.waiters.*;
import com.google.gson.Gson;
import io.objectbox.BoxStore;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 21/10/2017
 */
public class DineplanSynchronizer
{

    public long sync(final BoxStore store, WaiterConfiguration configuration, final DineplanApiClient server, final WaiterTable table, final WaiterOrder ticket)
    {
        try
        {
            if (table.isVirtualTable())
            {
                return submitQuickOrder(store, configuration, server, ticket);
            } else
            {
                DineplanTableSummary tableSummary = readTableSummary(configuration, server, table);
                DineplanTicket serverTicket = readServerTicket(configuration, server, tableSummary, ticket);

                // Local data was never synced with the server and server has its own ticket data
                if (serverTicket != null && ticket.getDineplanOrderId() == 0)
                {
                    replaceLocalDataWithServer(store, configuration, server, table, ticket, serverTicket);
                    return serverTicket.getId();
                }

                // Ticket was removed from the dineplan side - need to remove it from here too
                if (serverTicket == null && ticket.getDineplanOrderId() != 0)
                {
                    serverTicket = readAvailableServerTicket(configuration, server, tableSummary);

                    if (serverTicket != null)
                    {
                        replaceLocalDataWithServer(store, configuration, server, table, ticket, serverTicket);
                    } else
                    {
                        resetLocalData(store, configuration, server, table, ticket, serverTicket);
                    }

                    return ticket.getDineplanOrderId();
                }

                if (serverTicket == null && ticket.getDineplanOrderId() == 0)
                {
                    serverTicket = readAvailableServerTicket(configuration, server, tableSummary);

                    if (serverTicket != null)
                    {
                        replaceLocalDataWithServer(store, configuration, server, table, ticket, serverTicket);
                    } else
                    {
                        mergeData(store, configuration, server, table, ticket, serverTicket);
                    }

                    return ticket.getDineplanOrderId();
                }

                if ((serverTicket == null || serverTicket.getOrders().size() == 0) && ticket.getItems().size() > 0)
                {
                    replaceServerDataWithLocal(store, configuration, server, table, ticket, serverTicket);
                    return ticket.getDineplanOrderId();
                }

                mergeData(store, configuration, server, table, ticket, serverTicket);
                return ticket.getDineplanOrderId();
            }
        } catch (Throwable err)
        {
            err.printStackTrace();

            if (err instanceof DineflyException)
            {
                throw (DineflyException) err;
            } else
            {
                throw new DineflyException(err);
            }
        }
    }

    private int submitQuickOrder(final BoxStore store, final WaiterConfiguration configuration, final DineplanApiClient server, final WaiterOrder ticket) throws IOException
    {
        DineplanTicketUpdateRequest request = new DineplanTicketUpdateRequest();

        request.setId(0);
        request.setNotes(ticket.getNotes());
        request.setTableId(0);
        request.setTotalAmount(ticket.findTotal());
        request.setPax(null);
        request.setWaiter(null);
        request.setDepartmentId(configuration.getDepartmentId());

        int refIdCounter = generateNextOrderRef(null);

        if (configuration.getClosingTagData().size()>0)
        {
            request.setClosingTag(configuration.getClosingTagData().get(0));
        }

        for (WaiterOrderSaleItem item : ticket.getItems())
        {
            final WaiterMenuItem menuItem = store.boxFor(WaiterMenuItem.class).query().equal(WaiterMenuItem_.itemId, item.getMenuItemid()).build().findFirst();

            DineplanTicketOrderUpdateRequest orderRequest = new DineplanTicketOrderUpdateRequest();
            orderRequest.setId(item.getDineplanOrderId());
            orderRequest.setMenuItemId((int) item.getMenuItemid());
            orderRequest.setOrderNumber(item.getDineplanOrderNumber());
            orderRequest.setMenuItemName(menuItem != null ? menuItem.getName() : item.getName());
            orderRequest.setOrderStatus(item.getStatus().toDineplanStatus());
            orderRequest.setOrderStatusCodes(item.getStatus().toDineplanStatusCodes());
            orderRequest.setPrice(item.getPotionAmount());
            orderRequest.setNotes(item.getNotes());
            orderRequest.setQty(item.getQty());
            orderRequest.setUser(configuration.getUserName());
            orderRequest.setOrderRef(0);

            if (item.isGiftOrder())
            {
                orderRequest.getOrderStatusCodes().add(5);
                orderRequest.setOrderStatus(orderRequest.getOrderStatus() + ",Gift");
            }

            if (item.isVoidOrder())
            {
                orderRequest.getOrderStatusCodes().add(4);
                orderRequest.setOrderStatus(orderRequest.getOrderStatus() + ",Void");
            }

            if (menuItem != null)
            {
                for (WaiterMenuItemPortion portion : menuItem.getPortions())
                {
                    if (item.getPortionId() == portion.getPortionId())
                    {
                        orderRequest.setPortionName(portion.getName());
                    }
                }
            }

            for (WaiterOrderItemTag tag : item.getTags())
            {
                final WaiterMenuItemTagGroup tagGroup = store.boxFor(WaiterMenuItemTagGroup.class).query().equal(WaiterMenuItemTagGroup_.groupId, tag.getGroupId()).build().findFirst();

                for (WaiterMenuItemTag tg : tagGroup.getTags())
                {
                    if (tg.getTagId() == tag.getTagId())
                    {
                        orderRequest.getTags().add(new DineplanTicketOrderTag(tagGroup, tg));
                    }
                }
            }

            request.getOrders().add(orderRequest);

            if (item.isCombo())
            {
                orderRequest.setOrderRef(refIdCounter);
                orderRequest.setMenuItemType(WaiterMenuItem.ITEM_TYPE_COMBO);

                for (WaiterOrderComboItem comboItem : item.getComboItems())
                {
                    final WaiterMenuItem comboMenuItem = store.boxFor(WaiterMenuItem.class).query().equal(WaiterMenuItem_.itemId, comboItem.getMenuItemid()).build().findFirst();
                    DineplanTicketOrderUpdateRequest comboOrder = new DineplanTicketOrderUpdateRequest();

                    comboOrder.setId(comboItem.getDineplanOrderId());
                    comboOrder.setOrderRef(refIdCounter);
                    comboOrder.setMenuItemId((int) comboItem.getMenuItemid());
                    comboOrder.setOrderNumber(item.getDineplanOrderNumber());
                    comboOrder.setMenuItemName(comboMenuItem != null ? comboMenuItem.getName() : item.getName());
                    comboOrder.setOrderStatus(comboItem.getStatus().toDineplanStatus());
                    comboOrder.setOrderStatusCodes(comboItem.getStatus().toDineplanStatusCodes());
                    comboOrder.setPrice(comboItem.getPotionAmount());
                    comboOrder.setNotes(comboItem.getNotes());
                    comboOrder.setQty(comboItem.getQty());
                    comboOrder.setUser(configuration.getUserName());

                    if (comboItem.isGiftOrder())
                    {
                        comboOrder.getOrderStatusCodes().add(5);
                        comboOrder.setOrderStatus(comboOrder.getOrderStatus() + ",Gift");
                    }

                    if (comboItem.isVoidOrder())
                    {
                        comboOrder.getOrderStatusCodes().add(4);
                        comboOrder.setOrderStatus(comboOrder.getOrderStatus() + ",Void");
                    }

                    request.getOrders().add(comboOrder);
                }

                refIdCounter++;
            }
        }

        if (request.getOrders().size() > 0)
        {
            final Response<DinaplanTicketResult> response = server.getService().updateTicket(request).execute();

            if (response.isSuccessful())
            {
                final DinaplanTicketResult ticketResult = response.body();

                if (ticketResult.isError())
                {
                    throw new RuntimeException("Ticket update api error: " + ticketResult.getErrorMessage());
                } else
                {
                    ticket.getItems().clear();
                    store.boxFor(WaiterOrder.class).remove(ticket);
                    return ticketResult.getTicket().getId();
                }
            } else
            {
                throw new DineflyNetworkException(response.code(), response.message());
            }
        }

        return 0;
    }

    private void resetLocalData(final BoxStore store, final WaiterConfiguration configuration, final DineplanApiClient server, final WaiterTable table, final WaiterOrder ticket, final DineplanTicket serverTicket)
    {
        ticket.getItems().clear();
        store.boxFor(WaiterOrder.class).remove(ticket);
    }

    private void replaceLocalDataWithServer(final BoxStore store, final WaiterConfiguration configuration, final DineplanApiClient server, final WaiterTable table, final WaiterOrder ticket, final DineplanTicket serverTicket)
    {
        List<WaiterOrderSaleItem> shoppingCartPendingItems = ticket.clearItemsButExtractShoppingCartOnes();

        store.boxFor(WaiterOrder.class).put(ticket);

        if (serverTicket!=null)
        {
            ticket.setNotes(serverTicket.getNotes());
            ticket.setCreated(serverTicket.getCreated());
            ticket.setDineplanOrderId(serverTicket.getId());
            ticket.setLastSycnedDineplanData(new Gson().toJson(serverTicket));
            ticket.setDineplanOrderId(serverTicket.getId());
            ticket.setPax(serverTicket.getPax());
            ticket.setWaiter(serverTicket.getWaiter());
            ticket.setServerComputedAmount(serverTicket.getAmount());

            for (DineplanTicketOrder order : serverTicket.getOrders())
            {
                final WaiterMenuItem menuItem = store.boxFor(WaiterMenuItem.class).query().equal(WaiterMenuItem_.itemId, order.getMenuItemId()).build().findFirst();

                if (order.getOrderRef() != 0 && order.getMenuItemType() == 0 && menuItem != null && menuItem.checkIfCombo())
                {
                    // to fix server data integrity bug
                    order.setMenuItemType(menuItem.getItemType());
                }

                if (order.getOrderRef() != 0 && order.getMenuItemType() != WaiterMenuItem.ITEM_TYPE_COMBO)
                {
                    // skip combo child items
                    continue;
                }

                if (menuItem != null)
                {
                    final WaiterOrderSaleItem localOrder = new WaiterOrderSaleItem(menuItem);
                    localOrder.setDineplanOrderId(order.getId());
                    localOrder.setDineplanOrderNumber(order.getOrderNumber());
                    localOrder.setStatus(WaiterOrderItemStatus.fromDineplanStatus(order.getOrderStatus()));
                    localOrder.setVoidOrder(order.isVoid());
                    localOrder.setGiftOrder(order.isGift());
                    localOrder.setNotes(order.getNotes());
                    localOrder.setQty(order.getQty() > 0 ? order.getQty() : 1);

                    for (WaiterMenuItemPortion portion : menuItem.getPortions())
                    {
                        if (portion.getName().equalsIgnoreCase(order.getPortionName()))
                        {
                            localOrder.setPortionId(portion.getPortionId());
                            localOrder.setName(String.format("%s %s", menuItem.getName(), portion.getName()));
                            break;
                        }
                    }

                    for (DineplanTicketOrderTag tag : order.getTags())
                    {
                        WaiterOrderItemTag localTag = new WaiterOrderItemTag();
                        localTag.setGroupId(tag.getGroupId());
                        localTag.setTagId(tag.getTagId());
                        localTag.setName(tag.getValue());
                        localTag.setQty(tag.getQty());

                        for (WaiterMenuItemTagGroup tg : menuItem.getTagGroups())
                        {
                            if (tg.getGroupId() == tag.getGroupId())
                            {
                                for (WaiterMenuItemTag tt : tg.getTags())
                                {
                                    if (tt.getTagId() == tag.getTagId())
                                    {
                                        localTag.setAmount(tt.getPrice());
                                    }
                                }
                            }
                        }

                        localOrder.getTags().add(localTag);
                    }

                    localOrder.setFinalPrice(order.getTax());

                    if (order.getMenuItemType() == WaiterMenuItem.ITEM_TYPE_COMBO)
                    {
                        final DineplanComboMenuContainer comboData = App.getDataManager().getWaiterDataManage().getConfiguration().getComboDataFor(order.getMenuItemId());
                        final int refId = order.getOrderRef();
                        localOrder.setDineplanOrderNumber(order.getOrderNumber());
                        ComboGroup comboGroup = comboData.findComboGroup(order.getMenuItemId());

                        for (DineplanTicketOrder comboItem : serverTicket.getOrders())
                        {
                            if (comboItem.getOrderRef() == refId && comboItem.getMenuItemType() != WaiterMenuItem.ITEM_TYPE_COMBO)
                            {
                                final ComboItem comboItemInfo = comboData.findComboItem(comboItem.getMenuItemId());
                                final WaiterMenuItem comboMenuItem = store.boxFor(WaiterMenuItem.class).query().equal(WaiterMenuItem_.itemId, comboItem.getMenuItemId()).build().findFirst();
                                WaiterOrderComboItem combo = new WaiterOrderComboItem(localOrder, comboGroup, comboItemInfo, comboMenuItem, (int)order.getQty());
                                combo.setDineplanOrderId(order.getId());
                                combo.setStatus(WaiterOrderItemStatus.fromDineplanStatus(order.getOrderStatus()));
                                combo.setVoidOrder(order.isVoid());
                                combo.setGiftOrder(order.isGift());
                                combo.setNotes(order.getNotes());
                                combo.setQty(comboItem.getQty() > 0 ? comboItem.getQty() : 1);
                                localOrder.getComboItems().add(combo);
                            }
                        }
                    }

                    ticket.getItems().add(localOrder);
                }
            }

            for (WaiterOrderSaleItem scitem : shoppingCartPendingItems)
            {
                ticket.getItems().add(scitem);
            }

            store.boxFor(WaiterOrder.class).put(ticket);
        }
    }

    private void replaceServerDataWithLocal(final BoxStore store, final WaiterConfiguration configuration, final DineplanApiClient server, final WaiterTable table, final WaiterOrder ticket, final DineplanTicket serverTicket) throws IOException
    {
        DineplanTicketUpdateRequest request = new DineplanTicketUpdateRequest();

        request.setId((int) ticket.getDineplanOrderId());
        request.setNotes(ticket.getNotes());
        request.setTableId(table.getTableId());
        request.setTotalAmount(ticket.findTotal());
        request.setPax(ticket.getPax());
        request.setWaiter(ticket.getWaiter());
        request.setDepartmentId(configuration.getDepartmentId());

        int refIdCounter = 1;

        for (WaiterOrderSaleItem item : ticket.getItems())
        {
            final WaiterMenuItem menuItem = store.boxFor(WaiterMenuItem.class).query().equal(WaiterMenuItem_.itemId, item.getMenuItemid()).build().findFirst();

            DineplanTicketOrderUpdateRequest orderRequest = new DineplanTicketOrderUpdateRequest();
            orderRequest.setId(item.getDineplanOrderId());
            orderRequest.setMenuItemId((int) item.getMenuItemid());
            orderRequest.setOrderNumber(item.getDineplanOrderNumber());
            orderRequest.setMenuItemName(menuItem != null ? menuItem.getName() : item.getName());
            orderRequest.setOrderStatus(item.getStatus().toDineplanStatus());
            orderRequest.setOrderStatusCodes(item.getStatus().toDineplanStatusCodes());
            orderRequest.setPrice(item.getPotionAmount());
            orderRequest.setNotes(item.getNotes());
            orderRequest.setQty(item.getQty());
            orderRequest.setUser(configuration.getUserName());
            orderRequest.setOrderRef(0);

            if (item.isGiftOrder())
            {
                orderRequest.getOrderStatusCodes().add(5);
                orderRequest.setOrderStatus(orderRequest.getOrderStatus() + ",Gift");
            }

            if (item.isVoidOrder())
            {
                orderRequest.getOrderStatusCodes().add(4);
                orderRequest.setOrderStatus(orderRequest.getOrderStatus() + ",Void");
            }

            if (menuItem != null)
            {
                for (WaiterMenuItemPortion portion : menuItem.getPortions())
                {
                    if (item.getPortionId() == portion.getPortionId())
                    {
                        orderRequest.setPortionName(portion.getName());
                    }
                }
            }

            for (WaiterOrderItemTag tag : item.getTags())
            {
                final WaiterMenuItemTagGroup tagGroup = store.boxFor(WaiterMenuItemTagGroup.class).query().equal(WaiterMenuItemTagGroup_.groupId, tag.getGroupId()).build().findFirst();

                for (WaiterMenuItemTag tg : tagGroup.getTags())
                {
                    if (tg.getTagId() == tag.getTagId())
                    {
                        orderRequest.getTags().add(new DineplanTicketOrderTag(tagGroup, tg));
                    }
                }
            }

            request.getOrders().add(orderRequest);

            if (item.isCombo())
            {
                orderRequest.setOrderRef(refIdCounter);
                orderRequest.setMenuItemType(WaiterMenuItem.ITEM_TYPE_COMBO);

                for (WaiterOrderComboItem comboItem : item.getComboItems())
                {
                    final WaiterMenuItem comboMenuItem = store.boxFor(WaiterMenuItem.class).query().equal(WaiterMenuItem_.itemId, comboItem.getMenuItemid()).build().findFirst();
                    DineplanTicketOrderUpdateRequest comboOrder = new DineplanTicketOrderUpdateRequest();

                    comboOrder.setId(comboItem.getDineplanOrderId());
                    comboOrder.setOrderRef(refIdCounter);
                    comboOrder.setOrderNumber(item.getDineplanOrderNumber());
                    comboOrder.setMenuItemId((int) comboItem.getMenuItemid());
                    comboOrder.setMenuItemName(comboMenuItem != null ? comboMenuItem.getName() : item.getName());
                    comboOrder.setOrderStatus(comboItem.getStatus().toDineplanStatus());
                    comboOrder.setOrderStatusCodes(comboItem.getStatus().toDineplanStatusCodes());
                    comboOrder.setPrice(comboItem.getPotionAmount());
                    comboOrder.setNotes(comboItem.getNotes());
                    comboOrder.setQty(comboItem.getQty());
                    comboOrder.setUser(configuration.getUserName());

                    if (comboItem.isGiftOrder())
                    {
                        comboOrder.getOrderStatusCodes().add(5);
                        comboOrder.setOrderStatus(comboOrder.getOrderStatus() + ",Gift");
                    }

                    if (comboItem.isVoidOrder())
                    {
                        comboOrder.getOrderStatusCodes().add(4);
                        comboOrder.setOrderStatus(comboOrder.getOrderStatus() + ",Void");
                    }

                    request.getOrders().add(comboOrder);
                }

                refIdCounter++;
            }
        }

        if (request.getOrders().size() == 0)
        {
            return;
        }

        final Response<DinaplanTicketResult> response = server.getService().updateTicket(request).execute();

        if (response.isSuccessful())
        {
            final DinaplanTicketResult ticketResult = response.body();

            if (!ticketResult.isError())
            {
                replaceLocalDataWithServer(store, configuration, server, table, ticket, ticketResult.getTicket());
            } else
            {
                throw new RuntimeException("Ticket update api error: " + ticketResult.getErrorMessage());
            }
        } else
        {
            throw new DineflyNetworkException(response.code(), response.message());
        }
    }

    private int generateNextOrderRef(final DineplanTicket ticket)
    {
        int i = 1;

        if (ticket!=null && ticket.getOrders()!=null) {
            for (DineplanTicketOrder item : ticket.getOrders()) {
                if (item.getOrderRef() > i) {
                    i = item.getOrderRef();
                }
            }

            i++;
        }

        return i;
    }

    private void mergeData(final BoxStore store, final WaiterConfiguration configuration, final DineplanApiClient server, final WaiterTable table, final WaiterOrder ticket, final DineplanTicket serverTicket) throws IOException
    {
        DineplanTicketUpdateRequest request = new DineplanTicketUpdateRequest();

        request.setId((int) ticket.getDineplanOrderId());
        request.setNotes(ticket.getNotes());
        request.setTableId(table.getTableId());
        request.setPax(ticket.getPax());
        request.setWaiter(ticket.getWaiter());
        request.setDepartmentId(configuration.getDepartmentId());

        int orderRefCounter = generateNextOrderRef(serverTicket);

        for (WaiterOrderSaleItem item : ticket.getItems())
        {
            if (item.hasStatus(WaiterOrderItemStatus.DraftDelayed, WaiterOrderItemStatus.DraftImmediate, WaiterOrderItemStatus.Submitted, WaiterOrderItemStatus.ToBeServerLater))
            {
                continue;
            }

            final WaiterMenuItem menuItem = store.boxFor(WaiterMenuItem.class).query().equal(WaiterMenuItem_.itemId, item.getMenuItemid()).build().findFirst();

            DineplanTicketOrderUpdateRequest orderRequest = new DineplanTicketOrderUpdateRequest();
            orderRequest.setId(item.getDineplanOrderId());
            orderRequest.setMenuItemId((int) item.getMenuItemid());
            orderRequest.setOrderNumber(item.getDineplanOrderNumber());
            orderRequest.setMenuItemName(menuItem != null ? menuItem.getName() : item.getName());
            orderRequest.setOrderStatus(item.getStatus().toDineplanStatus());
            orderRequest.setOrderStatusCodes(item.getStatus().toDineplanStatusCodes());
            orderRequest.setPrice(item.getPotionAmount());
            orderRequest.setNotes(item.getNotes());
            orderRequest.setQty(item.getQty());
            orderRequest.setUser(configuration.getUserName());
            orderRequest.setOrderRef(0);
            orderRequest.setRefId(0);

            if (menuItem != null)
            {
                for (WaiterMenuItemPortion portion : menuItem.getPortions())
                {
                    if (item.getPortionId() == portion.getPortionId())
                    {
                        orderRequest.setPortionName(portion.getName());
                    }
                }
            }

            for (WaiterOrderItemTag tag : item.getTags())
            {
                final WaiterMenuItemTagGroup tagGroup = store.boxFor(WaiterMenuItemTagGroup.class).query().equal(WaiterMenuItemTagGroup_.groupId, tag.getGroupId()).build().findFirst();

                for (WaiterMenuItemTag tg : tagGroup.getTags())
                {
                    if (tg.getTagId() == tag.getTagId())
                    {
                        orderRequest.getTags().add(new DineplanTicketOrderTag(tagGroup, tg));
                    }
                }
            }

            request.getOrders().add(orderRequest);

            if (item.isCombo())
            {
                orderRequest.setOrderRef(orderRefCounter);
                orderRequest.setMenuItemType(WaiterMenuItem.ITEM_TYPE_COMBO);

                try
                {
                    for (WaiterOrderComboItem comboItem : item.getComboItems())
                    {
                        final WaiterMenuItem comboMenuItem = store.boxFor(WaiterMenuItem.class).query().equal(WaiterMenuItem_.itemId, comboItem.getMenuItemid()).build().findFirst();
                        DineplanTicketOrderUpdateRequest comboOrder = new DineplanTicketOrderUpdateRequest();

                        comboOrder.setId(comboItem.getDineplanOrderId());
                        comboOrder.setOrderRef(orderRefCounter);
                        comboOrder.setMenuItemId((int) comboItem.getMenuItemid());
                        comboOrder.setMenuItemName(comboMenuItem != null ? comboMenuItem.getName() : item.getName());
                        comboOrder.setOrderStatus(comboItem.getStatus().toDineplanStatus());
                        comboOrder.setOrderNumber(item.getDineplanOrderNumber());
                        comboOrder.setOrderStatusCodes(comboItem.getStatus().toDineplanStatusCodes());
                        comboOrder.setPrice(comboItem.getPotionAmount());
                        comboOrder.setNotes(comboItem.getNotes());
                        comboOrder.setQty(comboItem.getQty());
                        comboOrder.setUser(configuration.getUserName());

                        if (comboItem.isGiftOrder())
                        {
                            comboOrder.getOrderStatusCodes().add(5);
                            comboOrder.setOrderStatus(comboOrder.getOrderStatus() + ",Gift");
                        }

                        if (comboItem.isVoidOrder())
                        {
                            comboOrder.getOrderStatusCodes().add(4);
                            comboOrder.setOrderStatus(comboOrder.getOrderStatus() + ",Void");
                        }

                        request.getOrders().add(comboOrder);
                    }
                } catch (Throwable err)
                {
                    Crashlytics.logException(err);
                    err.printStackTrace();
                }

                orderRefCounter++;
            }
        }

        if (request.getOrders().size() == 0)
        {
            replaceLocalDataWithServer(store, configuration, server, table, ticket, serverTicket);
            return;
        }

        final Response<DinaplanTicketResult> response = server.getService().updateTicket(request).execute();

        if (response.isSuccessful())
        {
            final DinaplanTicketResult ticketResult = response.body();

            if (!ticketResult.isError())
            {
                replaceLocalDataWithServer(store, configuration, server, table, ticket, ticketResult.getTicket());
            } else
            {
                throw new RuntimeException("Ticket update api error: " + ticketResult.getErrorMessage());
            }
        } else
        {
            throw new DineflyNetworkException(response.code(), response.message());
        }
    }

    private DineplanTicket readServerTicket(final WaiterConfiguration configuration, final DineplanApiClient server, final DineplanTableSummary tableSummary, final WaiterOrder ticket)
    {
        if (tableSummary == null || tableSummary.getTickets().size() == 0)
        {
            return null;
        }

        final long ticketId = (ticket != null && tableSummary.containsTicket(ticket.getDineplanOrderId())) ? ticket.getDineplanOrderId() : 0;

        if (ticketId > 0)
        {
            try
            {
                final Response<DinaplanTicketResult> ticketResponse = server.getService().getTicket(ticketId).execute();

                if (ticketResponse.isSuccessful())
                {
                    final DinaplanTicketResult dtr = ticketResponse.body();

                    if (dtr.isError())
                    {
                        return null;
                    }

                    return dtr.getTicket();
                } else
                {
                    throw new DineflyNetworkException(ticketResponse.code(), ticketResponse.message());
                }
            } catch (Throwable err)
            {
                if (err instanceof DineflyException)
                {
                    throw (DineflyException) err;
                } else
                {
                    throw new DineflyException(err);
                }
            }
        } else
        {
            return null;
        }
    }

    private DineplanTicket readAvailableServerTicket(final WaiterConfiguration configuration, final DineplanApiClient server, final DineplanTableSummary tableSummary)
    {
        if (tableSummary == null || tableSummary.getTickets().size() == 0)
        {
            return null;
        }

        try
        {
            final Response<DinaplanTicketResult> ticketResponse = server.getService().getTicket(tableSummary.getTickets().get(0).getId()).execute();

            if (ticketResponse.isSuccessful())
            {
                final DinaplanTicketResult dtr = ticketResponse.body();

                if (dtr.isError())
                {
                    return null;
                }

                return dtr.getTicket();
            } else
            {
                throw new DineflyNetworkException(ticketResponse.code(), ticketResponse.message());
            }
        } catch (Throwable err)
        {
            if (err instanceof DineflyException)
            {
                throw (DineflyException) err;
            } else
            {
                throw new DineflyException(err);
            }
        }
    }

    private DineplanTableSummary readTableSummary(final WaiterConfiguration configuration, final DineplanApiClient server, final WaiterTable table)
    {
        try
        {
            final Response<DineplanTableSummaryResult> tableSummaryResultResponse = server.getService().getTableOverview(table.getTableId()).execute();

            if (tableSummaryResultResponse.isSuccessful())
            {
                final DineplanTableSummaryResult tsr = tableSummaryResultResponse.body();

                if (tsr.isError())
                {
                    throw new RuntimeException("Table summary api error: " + tsr.getErrorMessage());
                }

                return tsr.getSummary();
            } else
            {
                throw new DineflyNetworkException(tableSummaryResultResponse.code(), tableSummaryResultResponse.message());
            }
        } catch (Throwable err)
        {
            if (err instanceof DineflyException)
            {
                throw (DineflyException) err;
            } else
            {
                throw new DineflyException(err);
            }
        }
    }

}

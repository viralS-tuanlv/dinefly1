package com.dineplan.dinefly.core;

import android.support.annotation.AnyThread;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.dineplan.dinefly.R;
import com.dineplan.dinefly.core.api.DineplanApiClient;
import com.dineplan.dinefly.core.api.model.api.bill.BillData;
import com.dineplan.dinefly.core.api.model.api.in.DinaplanMoveTableResult;
import com.dineplan.dinefly.core.api.model.api.in.DineplanAuthResult;
import com.dineplan.dinefly.core.api.model.api.in.DineplanComboMenuContainer;
import com.dineplan.dinefly.core.api.model.api.in.DineplanComboMenuResult;
import com.dineplan.dinefly.core.api.model.api.in.DineplanGuestQuestionnarie;
import com.dineplan.dinefly.core.api.model.api.in.DineplanGuestQuestionnarieResponse;
import com.dineplan.dinefly.core.api.model.api.in.DineplanMenuCategory;
import com.dineplan.dinefly.core.api.model.api.in.DineplanMenuItem;
import com.dineplan.dinefly.core.api.model.api.in.DineplanMenuResult;
import com.dineplan.dinefly.core.api.model.api.in.DineplanPrintDataResult;
import com.dineplan.dinefly.core.api.model.api.in.DineplanSoldoutCheckResult;
import com.dineplan.dinefly.core.api.model.api.in.DineplanTable;
import com.dineplan.dinefly.core.api.model.api.in.DineplanTablesResult;
import com.dineplan.dinefly.core.api.model.api.in.GuestFeedback;
import com.dineplan.dinefly.core.api.model.api.in.GuestFeedbackEntry;
import com.dineplan.dinefly.core.api.model.api.out.DineplanMoveTableRequest;
import com.dineplan.dinefly.core.api.model.api.out.DineplanSoldoutCheckItem;
import com.dineplan.dinefly.core.api.model.api.out.DineplanSoldoutCheckRequest;
import com.dineplan.dinefly.core.err.DineflyApiException;
import com.dineplan.dinefly.core.err.DineflyException;
import com.dineplan.dinefly.core.err.DineflyNetworkException;
import com.dineplan.dinefly.core.err.ProvisioningError;
import com.dineplan.dinefly.core.err.SoldOutError;
import com.dineplan.dinefly.core.print.BTPOSPrinter;
import com.dineplan.dinefly.data.model.waiters.WaiterConfiguration;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuCategory;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuCategory_;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuItem;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuItemPortion;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuItemTag;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuItemTagGroup;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuItem_;
import com.dineplan.dinefly.data.model.waiters.WaiterOrder;
import com.dineplan.dinefly.data.model.waiters.WaiterOrderItemStatus;
import com.dineplan.dinefly.data.model.waiters.WaiterOrderItemTag;
import com.dineplan.dinefly.data.model.waiters.WaiterOrderSaleItem;
import com.dineplan.dinefly.data.model.waiters.WaiterOrderSaleItem_;
import com.dineplan.dinefly.data.model.waiters.WaiterOrderStatus;
import com.dineplan.dinefly.data.model.waiters.WaiterOrder_;
import com.dineplan.dinefly.data.model.waiters.WaiterRoom;
import com.dineplan.dinefly.data.model.waiters.WaiterRoom_;
import com.dineplan.dinefly.data.model.waiters.WaiterTable;
import com.dineplan.dinefly.data.model.waiters.WaiterTable_;
import com.dineplan.dinefly.event.OrderItemsChangedEvent;
import com.dineplan.dinefly.event.OrdersChangedEvent;
import com.dineplan.dinefly.event.TablesChangedEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.objectbox.BoxStore;
import io.objectbox.query.QueryBuilder;
import io.objectbox.reactive.DataObserver;
import retrofit2.Response;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 06/10/2017
 */
public class WaiterDataManager
{

    private static final long MENU_RESYNC_INTERVAL_MS = 24 * 60 * 60 * 1000; // 24 hr


    private final DataManager dm;
    private DineplanApiClient dineplanServer;
    private DineplanSynchronizer dineplanSynchroniser = new DineplanSynchronizer();

    WaiterDataManager(DataManager dm)
    {
        this.dm = dm;

        dm.db.subscribe(WaiterOrderSaleItem.class).observer(new DataObserver<Class<WaiterOrderSaleItem>>()
        {
            public void onData(final Class<WaiterOrderSaleItem> data)
            {
                App.postEvent(new OrderItemsChangedEvent());
            }
        });

        dm.db.subscribe(WaiterOrder.class).observer(new DataObserver<Class<WaiterOrder>>()
        {
            public void onData(final Class<WaiterOrder> data)
            {
                App.postEvent(new OrdersChangedEvent());
            }
        });

        dm.db.subscribe(WaiterTable.class).observer(new DataObserver<Class<WaiterTable>>()
        {
            public void onData(final Class<WaiterTable> data)
            {
                App.postEvent(new TablesChangedEvent());
            }
        });
    }

    @AnyThread
    public static void provision(final BoxStore db, final Object payload)
    {
        if (payload != null && payload instanceof WaiterConfiguration)
        {
            db.boxFor(WaiterConfiguration.class).removeAll();
            db.boxFor(WaiterConfiguration.class).put((WaiterConfiguration) payload);
        }
        else
        {
            throw new ProvisioningError("Invalid payload: must not be null and instance of WaiterConfiguration, but got: " + payload);
        }
    }

    @AnyThread
    void shutdown()
    {
        logoff();
        clearData();
        dineplanServer = null;
    }

    @AnyThread
    public WaiterConfiguration getConfiguration()
    {
        return dm.db.boxFor(WaiterConfiguration.class).query().build().findUnique();
    }

    @AnyThread
    private synchronized DineplanApiClient getServer()
    {
        if (dineplanServer == null)
        {
            final WaiterConfiguration cfg = getConfiguration();

            if (cfg != null)
            {
                dineplanServer = new DineplanApiClient(cfg.getDineplanEndpoint(), cfg.getDeviceId(), cfg.getUserId() != 0 ? ("" + cfg.getUserId()) : null);
            }
        }

        if (dineplanServer != null)
        {
            return dineplanServer;
        }
        else
        {
            throw new ProvisioningError("Incorrect configuration for waiters mode: no configuration");
        }
    }

    @AnyThread
    public boolean isAuthenticated()
    {
        final WaiterConfiguration configuration = getConfiguration();
        return configuration != null && configuration.isAuthenticated();
    }

    @AnyThread
    public void logoff()
    {
        if (isAuthenticated())
        {
            getServer().setUserId("");
            final WaiterConfiguration configuration = getConfiguration();
            configuration.setUserId(0);
            configuration.setAuthenticated(false);
            dm.db.boxFor(WaiterConfiguration.class).put(configuration);
            dineplanServer = null;
        }
    }

    public boolean verifyAccess(final String password)
    {
        return (isAuthenticated() && getConfiguration().verifyAuthenticationSecret(password));
    }

    private void clearData()
    {
        dm.db.boxFor(WaiterRoom.class).removeAll();
        dm.db.boxFor(WaiterTable.class).removeAll();
        dm.db.boxFor(WaiterOrder.class).removeAll();
        dm.db.boxFor(WaiterOrderSaleItem.class).removeAll();
        dm.db.boxFor(WaiterOrderItemTag.class).removeAll();
        dm.db.boxFor(WaiterMenuCategory.class).removeAll();
        dm.db.boxFor(WaiterMenuItem.class).removeAll();
        dm.db.boxFor(WaiterMenuItemTagGroup.class).removeAll();
        dm.db.boxFor(WaiterMenuItemTag.class).removeAll();
        dm.db.boxFor(WaiterMenuItemPortion.class).removeAll();
    }

    @WorkerThread
    public void authenticate(final String password)
    {
        try
        {
            final Response<DineplanAuthResult> response = getServer().getService().auth(password).execute();

            if (response.isSuccessful())
            {
                final DineplanAuthResult authResult = response.body();

                if (!authResult.isError())
                {
                    getServer().setUserId("" + authResult.getAuthInfo().getUserId());
                    final WaiterConfiguration cfg = getConfiguration();
                    cfg.setupFromAuthInfo(authResult.getAuthInfo(), password);
                    dm.db.boxFor(WaiterConfiguration.class).put(cfg);
                    dineplanServer = null;
                    syncTablesIfEmpty();
                    syncMenu(false);
                }
                else
                {
                    getServer().setUserId("");
                    throw new DineflyApiException(authResult.getErrorMessage());
                }
            }
            else
            {
                getServer().setUserId("");
                throw new DineflyNetworkException(response.code(), response.message());
            }
        }
        catch (IOException err)
        {
            logoff();
            throw new DineflyException(err);
        }
    }


    @WorkerThread
    public void syncMenu(boolean force)
    {
        final WaiterConfiguration configuration = getConfiguration();

        if (configuration == null)
        {
            return;
        }

        if (force || getMenuCatsCount() == 0 || configuration.getLastMenuSync() == null || (System.currentTimeMillis() - configuration.getLastMenuSync().getTime()) > MENU_RESYNC_INTERVAL_MS)
        {
            try
            {
                final Response<DineplanMenuResult> response = getServer().getService().getMenu().execute();

                if (!response.isSuccessful())
                {
                    throw new DineflyNetworkException(response.code(), response.message());
                }
                else
                {
                    final DineplanMenuResult menuResult = response.body();

                    if (menuResult.isError())
                    {
                        throw new DineflyApiException(menuResult.getErrorMessage());
                    }
                    else
                    {
                        List<WaiterMenuCategory> newMenu = new ArrayList<>();

                        for (DineplanMenuCategory dineplanCategory : menuResult.getMenu().getCategories())
                        {
                            dineplanCategory.getTagGroups().addAll(menuResult.getMenu().getTagGroups());
                            newMenu.add(WaiterMenuCategory.createFromDineplanCategory(dineplanCategory, configuration.getDineplanEndpoint()));
                        }

                        if (menuResult.getMenu().getNonMenuItems().size()>0) {
                            WaiterMenuCategory hiddenCategory = new WaiterMenuCategory();
                            hiddenCategory.setCategoryId(9999);
                            hiddenCategory.setDescription("");
                            hiddenCategory.setName("");
                            hiddenCategory.setHidden(true);

                            for (DineplanMenuItem mni : menuResult.getMenu().getNonMenuItems())
                            {
                                hiddenCategory.addMenuItem(WaiterMenuItem.createFromDineplanMennuItem(null, mni, configuration.getDineplanEndpoint()));
                            }

                            newMenu.add(hiddenCategory);
                        }

                        configuration.setLastMenuSync(new Date());

                        dm.db.boxFor(WaiterMenuCategory.class).removeAll();
                        dm.db.boxFor(WaiterMenuItem.class).removeAll();
                        dm.db.boxFor(WaiterMenuItemPortion.class).removeAll();
                        dm.db.boxFor(WaiterMenuItemTagGroup.class).removeAll();
                        dm.db.boxFor(WaiterMenuItemTag.class).removeAll();

                        dm.db.boxFor(WaiterMenuCategory.class).put(newMenu);


                        configuration.putComboData(null);

                        // nested try/catch here is for shitty DP API workaround (one more!) :(
                        try {
                            final Response<DineplanComboMenuResult> comboResponse = getServer().getService().getCombos().execute();
                            if (comboResponse.isSuccessful())
                            {
                                final DineplanComboMenuResult comboBody = comboResponse.body();

                                if (!comboBody.isError())
                                {
                                    configuration.putComboData(comboBody.getMenu());
                                } else
                                {
                                    configuration.putComboData(new ArrayList<DineplanComboMenuContainer>());
                                }
                            }
                        } catch (Throwable errCombo) {
                            configuration.putComboData(new ArrayList<DineplanComboMenuContainer>());
                        }

                        dm.db.boxFor(WaiterConfiguration.class).put(configuration);
                    }
                }
            }
            catch (IOException e)
            {
                throw new DineflyException(e);
            }
        }
    }

    public List<WaiterRoom> getHalls()
    {
        return dm.db.boxFor(WaiterRoom.class).query().order(WaiterRoom_.roomId).build().find();
    }

    public List<WaiterMenuCategory> getMenu()
    {
        return dm.db.boxFor(WaiterMenuCategory.class).query().notEqual(WaiterMenuCategory_.hidden, true).order(WaiterMenuCategory_.weight).build().find();
    }

    public long getMenuCatsCount()
    {
        return dm.db.boxFor(WaiterMenuCategory.class).query().build().count();
    }

    @WorkerThread
    public void syncTablesIfEmpty()
    {
        if (dm.db.boxFor(WaiterTable.class).count() == 0)
        {
            syncTables();
        }
    }
    @WorkerThread
    public BillData getBillFromTicket(long ticketId) {
        final WaiterConfiguration configuration = getConfiguration();
        BillData billData = null;
        if (configuration == null) {
            return null;
        }
        try {
            final Response<BillData> response = getServer().getService().getBillContent(ticketId).execute();
            billData = response.body();

        } catch (Exception e) {

        }
        return billData;
    }
    @WorkerThread
    public void syncTables()
    {
        final WaiterConfiguration configuration = getConfiguration();

        if (configuration == null)
        {
            return;
        }

        try
        {
            final Response<DineplanTablesResult> response = getServer().getService().getTables().execute();

            if (!response.isSuccessful())
            {
                throw new DineflyNetworkException(response.code(), response.message());
            }
            else
            {
                final DineplanTablesResult tablesResult = response.body();

                if (tablesResult.isError())
                {
                    throw new DineflyApiException(tablesResult.getErrorMessage());
                }
                else
                {


                    Map<Integer, WaiterRoom> rooms = new HashMap<>();

                    for (DineplanTable dineplanTable : tablesResult.getTables())
                    {
                        if (!rooms.containsKey(dineplanTable.getHallId()))
                        {
                            rooms.put(dineplanTable.getHallId(), WaiterRoom.createFromDineplanTable(dineplanTable));
                        }

                        rooms.get(dineplanTable.getHallId()).getTables().add(WaiterTable.createFromDineplanTable(dineplanTable));
                    }

                    dm.db.boxFor(WaiterRoom.class).removeAll();
                    dm.db.boxFor(WaiterTable.class).removeAll();
                    dm.db.boxFor(WaiterRoom.class).put(rooms.values());
                }
            }
        }
        catch (IOException e)
        {
            throw new DineflyException(e);
        }
    }

    public WaiterTable findTable(final long id)
    {
        if (id == 0)
        {
            return WaiterTable.createQuickOrderVirtualTable();
        }
        else
        {
            return dm.db.boxFor(WaiterTable.class).query().equal(WaiterTable_.tableId, id).build().findFirst();
        }
    }

    public Collection<WaiterMenuItem> getAllMenuItems()
    {
        return dm.db.boxFor(WaiterMenuItem.class).query().order(WaiterMenuItem_.weight).build().find();
    }

    public WaiterMenuCategory findCategory(final int categoryId)
    {
        return dm.db.boxFor(WaiterMenuCategory.class).query().equal(WaiterMenuCategory_.categoryId, categoryId).build().findFirst();
    }

    public WaiterMenuItem findItem(final long menuItemid)
    {
        return dm.db.boxFor(WaiterMenuItem.class).query().equal(WaiterMenuItem_.itemId, menuItemid).build().findFirst();
    }

    public synchronized WaiterOrder getTableOrder(final long tableId)
    {
        final WaiterTable table = findTable(tableId);

        if (table != null)
        {
            WaiterOrder order = dm.db.boxFor(WaiterOrder.class).query().equal(WaiterOrder_.tableId, tableId).build().findFirst();

            if (order == null)
            {
                order = new WaiterOrder();
                order.setStatus(WaiterOrderStatus.Draft);
                order.setTableId(tableId);
                dm.db.boxFor(WaiterOrder.class).put(order);
                order = dm.db.boxFor(WaiterOrder.class).query().equal(WaiterOrder_.tableId, tableId).build().findFirst();
            }

            return order;
        }
        else
        {
            throw new DineflyException("Invalid table id: " + table);
        }
    }

    public void addOrderItem(final long tableId, final WaiterOrderSaleItem item, boolean cookImmideatelyAfterSubmission)
    {
        WaiterOrder order = getTableOrder(tableId);
        item.setStatus(cookImmideatelyAfterSubmission ? WaiterOrderItemStatus.DraftImmediate : WaiterOrderItemStatus.DraftDelayed);

        if (item.getId() == 0)
        {
            order.getItems().add(item);
            dm.db.boxFor(WaiterOrder.class).put(order);
        }
        else
        {
            dm.db.boxFor(WaiterOrderSaleItem.class).put(item);
        }
    }

    public void updateOrderItem(final WaiterOrderSaleItem order)
    {
        if (order.getId() > 0)
        {
            dm.db.boxFor(WaiterOrderSaleItem.class).put(order);
        }
    }

    public void removePreorderedItem(final long id)
    {
        final WaiterOrderSaleItem item = dm.db.boxFor(WaiterOrderSaleItem.class).get(id);

        if (item != null && item.hasStatus(WaiterOrderItemStatus.DraftDelayed, WaiterOrderItemStatus.DraftImmediate))
        {
            dm.db.boxFor(WaiterOrderSaleItem.class).remove(item);
        }
        else
        {
            if (item == null)
            {
                throw new DineflyException("Invalid item id: " + id);
            }
            else
            {
                throw new DineflyException(String.format("Item is not in pre-order state: %s - %s", id, item.getStatus().name()));
            }
        }
    }

    public void clearPreorderForTable(final long tableId)
    {
        final WaiterOrder order = getTableOrder(tableId);

        if (order != null)
        {
            List<WaiterOrderSaleItem> deleteList = new ArrayList<>();

            for (WaiterOrderSaleItem item : order.getItems())
            {
                if (item.hasStatus(WaiterOrderItemStatus.DraftImmediate, WaiterOrderItemStatus.DraftDelayed))
                {
                    deleteList.add(item);
                }
            }

            if (deleteList.size() > 0)
            {
                dm.db.boxFor(WaiterOrderSaleItem.class).remove(deleteList);
            }
        }
    }

    public List<WaiterMenuItem> searchMenu(final String query)
    {
        return dm.db.boxFor(WaiterMenuItem.class).query().contains(WaiterMenuItem_.name, query, QueryBuilder.StringOrder.CASE_INSENSITIVE).or().contains(WaiterMenuItem_.alias, query, QueryBuilder.StringOrder.CASE_INSENSITIVE).or().contains(WaiterMenuItem_.description, query, QueryBuilder.StringOrder.CASE_INSENSITIVE).build().find();
    }

    public long submitPreorderForTable(final long tableId, final String pax, final String waiter, String comments)
    {
        final boolean quickOrderMode = findTable(tableId).isVirtualTable();
        final String closingTag = getConfiguration().getClosingTag();
        final WaiterOrder tableOrder = getTableOrder(tableId);
        final List<WaiterOrderSaleItem> updateBatch = new ArrayList<>();

        if (!TextUtils.isEmpty(pax))
        {
            tableOrder.setPax(pax);
        }

        if (!TextUtils.isEmpty(waiter))
        {
            tableOrder.setWaiter(waiter);
        }

        if (!TextUtils.isEmpty(comments))
        {
            tableOrder.setNotes(comments);
        }

        List<DineplanSoldoutCheckItem> sochecks = new ArrayList<>();

        for (WaiterOrderSaleItem item : tableOrder.getItems())
        {
            final WaiterMenuItem menuItem = findItem(item.getMenuItemid());

            if (quickOrderMode)
            {
                item.setStatus(WaiterOrderItemStatus.PendingSubmitted);
                updateBatch.add(item);
                sochecks.add(new DineplanSoldoutCheckItem(menuItem, item));
            }
            else
            {
                if (item.hasStatus(WaiterOrderItemStatus.DraftDelayed))
                {
                    item.setStatus(WaiterOrderItemStatus.PendingToBeServerLater);
                    updateBatch.add(item);
                    sochecks.add(new DineplanSoldoutCheckItem(menuItem, item));
                }

                if (item.hasStatus(WaiterOrderItemStatus.DraftImmediate))
                {
                    item.setStatus(WaiterOrderItemStatus.PendingSubmitted);
                    updateBatch.add(item);
                    sochecks.add(new DineplanSoldoutCheckItem(menuItem, item));
                }
            }
        }

        long serverId = 0;

        try
        {
            Response<DineplanSoldoutCheckResult> response = getServer().getService().checkForSoldouts(new DineplanSoldoutCheckRequest(sochecks)).execute();
            if (response.isSuccessful())
            {
                DineplanSoldoutCheckResult body = response.body();

                if (body.getSoldOutInfo().getItems().size() > 0)
                {
                    StringBuilder b = new StringBuilder();
                    b.append(App.getContext().getString(R.string.sold_out_items_title)).append("\n\n");

                    for (DineplanSoldoutCheckItem item : body.getSoldOutInfo().getItems())
                    {
                        b.append("- ").append(item.getMenuItemName()).append("\n");
                    }

                    throw new SoldOutError(b.toString());
                }
            }
        }
        catch (IOException e)
        {
        }

        if (updateBatch.size() > 0)
        {
            dm.db.boxFor(WaiterOrderSaleItem.class).put(updateBatch);
            dm.db.boxFor(WaiterOrder.class).put(tableOrder);
            serverId = syncTableOrders(tableId);
        }

        if (serverId != 0 && getConfiguration().isCanPrintLocalBolls() && !TextUtils.isEmpty(getConfiguration().getPrinterAddress()))
        {
            try
            {
                printBillLocally(serverId);
            }
            catch (Throwable err)
            {

            }
        }

        if (serverId != 0 && getConfiguration().isCanPrintLocalKots() && !TextUtils.isEmpty(getConfiguration().getPrinterAddress()))
        {
            try
            {
                printKotLocally(serverId);
            }
            catch (Throwable err)
            {
                err.printStackTrace();
            }
        }

        return serverId;
    }

    public String checkSoldOutsForTable(final long tableId)
    {
        final boolean quickOrderMode = findTable(tableId).isVirtualTable();
        final WaiterOrder tableOrder = getTableOrder(tableId);
        final List<WaiterOrderSaleItem> updateBatch = new ArrayList<>();
        List<DineplanSoldoutCheckItem> sochecks = new ArrayList<>();

        for (WaiterOrderSaleItem item : tableOrder.getItems())
        {
            final WaiterMenuItem menuItem = findItem(item.getMenuItemid());

            if (quickOrderMode)
            {
                item.setStatus(WaiterOrderItemStatus.PendingSubmitted);
                updateBatch.add(item);
                sochecks.add(new DineplanSoldoutCheckItem(menuItem, item));
            }
            else
            {
                if (item.hasStatus(WaiterOrderItemStatus.DraftDelayed))
                {
                    item.setStatus(WaiterOrderItemStatus.PendingToBeServerLater);
                    updateBatch.add(item);
                    sochecks.add(new DineplanSoldoutCheckItem(menuItem, item));
                }

                if (item.hasStatus(WaiterOrderItemStatus.DraftImmediate))
                {
                    item.setStatus(WaiterOrderItemStatus.PendingSubmitted);
                    updateBatch.add(item);
                    sochecks.add(new DineplanSoldoutCheckItem(menuItem, item));
                }
            }
        }

        long serverId = 0;

        try
        {
            Response<DineplanSoldoutCheckResult> response = getServer().getService().checkForSoldouts(new DineplanSoldoutCheckRequest(sochecks)).execute();
            if (response.isSuccessful())
            {
                DineplanSoldoutCheckResult body = response.body();

                if (body.getSoldOutInfo().getItems().size() > 0)
                {
                    StringBuilder b = new StringBuilder();
                    b.append(App.getContext().getString(R.string.sold_out_items_title)).append("\n\n");

                    for (DineplanSoldoutCheckItem item : body.getSoldOutInfo().getItems())
                    {
                        b.append("- ").append(item.getMenuItemName()).append("\n");
                    }

                    return b.toString();
                }
            }
        }
        catch (IOException e)
        {
        }

        return null;
    }

    public void updateOrderMetadata(final long orderId, final String pax, final String waiter)
    {
        final WaiterOrder tableOrder = dm.db.boxFor(WaiterOrder.class).get(orderId);

        if (tableOrder != null)
        {
            if (!TextUtils.isEmpty(pax))
            {
                tableOrder.setPax(pax);
            }

            if (!TextUtils.isEmpty(waiter))
            {
                tableOrder.setWaiter(waiter);
            }

            dm.db.boxFor(WaiterOrder.class).put(tableOrder);
            syncTableOrders(tableOrder.getTableId());
        }
    }

    public long syncTableOrders(final long tableId)
    {
        final WaiterTable table = findTable(tableId);
        final WaiterOrder tableOrder = getTableOrder(tableId);
        long serverId = 0;

        if (table != null && tableOrder != null)
        {
            serverId = dineplanSynchroniser.sync(dm.db, getConfiguration(), getServer(), table, tableOrder);
            syncTables();
        }

        return serverId;
    }

    public void submitPendingMealsForTable(final long tableId)
    {
        final WaiterTable table = findTable(tableId);
        final WaiterOrder tableOrder = getTableOrder(tableId);

        if (table != null && tableOrder != null)
        {
            List<WaiterOrderSaleItem> updateBatch = new ArrayList<>();

            for (WaiterOrderSaleItem item : tableOrder.getItems())
            {
                if (item.hasStatus(WaiterOrderItemStatus.ToBeServerLater))
                {
                    item.setStatus(WaiterOrderItemStatus.PendingTakeFromWaitlist);
                    updateBatch.add(item);
                }
            }

            if (updateBatch.size() > 0)
            {
                dm.db.boxFor(WaiterOrderSaleItem.class).put(updateBatch);
                dineplanSynchroniser.sync(dm.db, getConfiguration(), getServer(), table, tableOrder);
                syncTables();
            }
        }
    }

    public void printBillForTable(final long tableId)
    {
        final WaiterTable table = findTable(tableId);
        final WaiterOrder tableOrder = getTableOrder(tableId);

        if (table != null && tableOrder != null)
        {
            try
            {
                getServer().getService().printBill(tableOrder.getDineplanOrderId()).execute();
                getServer().getService().getPreparationOrderPrintoutData(tableOrder.getDineplanOrderId()).execute();
                getServer().getService().getTicketBillPrintoutData(tableOrder.getDineplanOrderId()).execute();
            }
            catch (IOException e)
            {
                throw new DineflyException(e);
            }
        }
    }

    private void printBillLocally(final long dineplanTicketId)
    {
        try
        {
            final Response<DineplanPrintDataResult> response = getServer().getService().getTicketBillPrintoutData(dineplanTicketId).execute();

            if (response.isSuccessful())
            {
                final DineplanPrintDataResult body = response.body();

                if (!body.isError())
                {
                    final BTPOSPrinter printer = new BTPOSPrinter(getConfiguration().getPrinterAddress());
                    printer.sendData(response.body().getData().getContent(), 10);
                    printer.close();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new DineflyException(e);
        }
    }

    private void printKotLocally(final long dineplanTicketId)
    {
        try
        {
            final Response<DineplanPrintDataResult> response = getServer().getService().getPreparationOrderPrintoutData(dineplanTicketId).execute();

            if (response.isSuccessful())
            {
                final DineplanPrintDataResult body = response.body();

                if (!body.isError())
                {
                    final BTPOSPrinter printer = new BTPOSPrinter(getConfiguration().getPrinterAddress());
                    printer.sendData(response.body().getData().getContent(), 10);
                    printer.close();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new DineflyException(e);
        }
    }

    public void updateTicketNotes(final long tableId, final long ticketId, final String notes)
    {
        final WaiterTable table = findTable(tableId);
        final WaiterOrder tableOrder = getTableOrder(tableId);

        if (table != null && tableOrder != null)
        {
            tableOrder.setNotes(notes);
            dm.db.boxFor(WaiterOrder.class).put(tableOrder);
            dineplanSynchroniser.sync(dm.db, getConfiguration(), getServer(), table, tableOrder);
            syncTables();
        }
    }

    public WaiterOrderSaleItem findOrderItem(final long id)
    {
        return dm.db.boxFor(WaiterOrderSaleItem.class).query().equal(WaiterOrderSaleItem_.id, id).build().findFirst();
    }

    public void markAllPreordersAsServeLater(final long tableId)
    {
        final WaiterOrder tableOrder = App.getDataManager().getWaiterDataManage().getTableOrder(tableId);

        if (tableOrder != null)
        {
            List<WaiterOrderSaleItem> updateBatch = new ArrayList<>();

            for (WaiterOrderSaleItem item : tableOrder.getItems())
            {
                if (item.hasStatus(WaiterOrderItemStatus.DraftImmediate))
                {
                    item.setStatus(WaiterOrderItemStatus.DraftDelayed);
                    updateBatch.add(item);
                }
            }

            if (updateBatch.size() > 0)
            {
                dm.db.boxFor(WaiterOrderSaleItem.class).put(updateBatch);
            }
        }
    }

    public void moveTable(final long ticketId, final long tableId) throws IOException
    {
        final WaiterOrder order = dm.db.boxFor(WaiterOrder.class).get(ticketId);
        final WaiterTable table = findTable(tableId);

        if (order != null && table != null)
        {
            final Response<DinaplanMoveTableResult> response = getServer().getService().moveTable(new DineplanMoveTableRequest(order.getDineplanOrderId(), tableId)).execute();

            if (response.isSuccessful())
            {
                final DinaplanMoveTableResult body = response.body();

                if (!body.isError())
                {
                    order.setTableId(tableId);
                    dm.db.boxFor(WaiterOrder.class).put(order);
                    syncTables();
                }
                else
                {
                    throw new DineflyException(body.getErrorMessage());
                }
            }
            else
            {
                throw new DineflyNetworkException(response.code(), response.message());
            }
        }
    }

    public WaiterOrder findOrder(final long orderId)
    {
        return dm.db.boxFor(WaiterOrder.class).query().equal(WaiterOrder_.id, orderId).build().findFirst();
    }

    public boolean printOrder(final long dineplanTicketId, final boolean printLocally)
    {
        if (getConfiguration().isCanPrintLocalBolls())
        {
            printBillLocally(dineplanTicketId);
        }

        if (getConfiguration().isCanPrintLocalKots())
        {
            printKotLocally(dineplanTicketId);
        }

        return true;
    }

    public DineplanGuestQuestionnarie loadFeedback()
    {
        try
        {
            Response<DineplanGuestQuestionnarieResponse> execute = getServer().getService().getGuestFeedbackQuestionnarie().execute();
            if (execute.isSuccessful())
            {
                DineplanGuestQuestionnarieResponse body = execute.body();
                return !body.isError() ? body.getQuestionnarie() : null;
            }
            else
            {
                return null;
            }
        }
        catch (IOException err)
        {
            err.printStackTrace();
            return null;
        }
    }

    public void sendFeedback(DineplanGuestQuestionnarie feedback, long ticketId, List<GuestFeedbackEntry> answers) throws IOException
    {
        getServer().getService().submitGuestFeedback(new GuestFeedback(feedback, answers, "" + ticketId)).execute();
    }
}

package com.dineplan.dinefly.logic.waiters;

import android.text.TextUtils;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.data.model.waiters.WaiterOrder;
import com.dineplan.dinefly.data.model.waiters.WaiterOrderSaleItem;
import com.dineplan.dinefly.event.OrderItemsChangedEvent;
import com.dineplan.dinefly.event.OrdersChangedEvent;
import eu.livotov.labs.android.robotools.os.RTAsyncTask;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
@InjectViewState
public class WaiterOrderPresenter extends MvpPresenter<WaiterOrderPresenter.View>
{

    Subscriber subscriber = new Subscriber();
    private boolean quickMode;

    protected void onFirstViewAttach()
    {
        App.subscribe(subscriber);
    }

    public void onDestroy()
    {
        App.unsubscribe(subscriber);
        super.onDestroy();
    }

    public WaiterOrder getOrderForTable(long tableId)
    {
        return App.getDataManager().getWaiterDataManage().getTableOrder(tableId);
    }

    public void updateOrderItem(final WaiterOrderSaleItem order)
    {
        new RTAsyncTask()
        {

            protected void doInBackground() throws Throwable
            {
                App.getDataManager().getWaiterDataManage().updateOrderItem(order);
            }
        }.execPool();
    }

    public void addDraftItem(final long tableId, final WaiterOrderSaleItem order, final boolean cookImmideatelyAfterSubmission)
    {
        new RTAsyncTask()
        {

            protected void doInBackground() throws Throwable
            {
                App.getDataManager().getWaiterDataManage().addOrderItem(tableId, order, cookImmideatelyAfterSubmission);
            }
        }.execPool();
    }

    public void removePreorderedItem(final WaiterOrderSaleItem item)
    {
        new RTAsyncTask()
        {

            protected void doInBackground() throws Throwable
            {
                App.getDataManager().getWaiterDataManage().removePreorderedItem(item.getId());
            }
        }.execPool();
    }

    public void clearPreorder(final long tableId)
    {
        new RTAsyncTask()
        {

            protected void doInBackground() throws Throwable
            {
                App.getDataManager().getWaiterDataManage().clearPreorderForTable(tableId);
            }
        }.execPool();
    }

    public void submitPreorderWithResult(final long tableId, final String pax, final String waiter, final String comments)
    {
        new RTAsyncTask()
        {
            long order;

            protected void doInBackground() throws Throwable
            {
                order = App.getDataManager().getWaiterDataManage().submitPreorderForTable(tableId, pax, waiter, comments);
            }

            protected void onPostExecute()
            {
                getViewState().onPreorderSubmitted(order);
            }

            protected void onError(final Throwable t)
            {
                getViewState().onPreorderSubmitFailed(t);
            }
        }.execPool();
    }

    public void updateOrderMetadata(final long ticketId, final String pax, final String waiter)
    {
        new RTAsyncTask()
        {

            protected void doInBackground() throws Throwable
            {
                App.getDataManager().getWaiterDataManage().updateOrderMetadata(ticketId, pax, waiter);
            }
        }.execPool();
    }

    public void syncTableOrders(final long tableId)
    {
        new RTAsyncTask()
        {

            protected void doInBackground() throws Throwable
            {
                App.getDataManager().getWaiterDataManage().syncTableOrders(tableId);
            }

            protected void onPostExecute()
            {
                getViewState().onTableOrdersSyncCompleted();
            }

            protected void onError(final Throwable t)
            {
                getViewState().onTableOrdersSyncFailed(t);
            }
        }.execPool();
    }

    public void submitPendingMeals(final long tableId)
    {
        new RTAsyncTask()
        {

            protected void doInBackground() throws Throwable
            {
                App.getDataManager().getWaiterDataManage().submitPendingMealsForTable(tableId);
            }

            protected void onPostExecute()
            {
                getViewState().onTableOrdersSyncCompleted();
            }

            protected void onError(final Throwable t)
            {
                getViewState().onTableOrdersSyncFailed(t);
            }
        }.execPool();
    }

    public void printBill(final long tableId)
    {
        new RTAsyncTask()
        {

            protected void doInBackground() throws Throwable
            {
                App.getDataManager().getWaiterDataManage().printBillForTable(tableId);
            }

            protected void onPostExecute()
            {
                getViewState().onBillPrinted();
            }

            protected void onError(final Throwable t)
            {
                getViewState().onBillPrintingError(t);
            }
        }.execPool();
    }

    public void changeOrderNotes(final long tableId, final long ticketId, final String notes)
    {
        new RTAsyncTask()
        {

            protected void doInBackground() throws Throwable
            {
                App.getDataManager().getWaiterDataManage().updateTicketNotes(tableId, ticketId, notes);
            }

            protected void onPostExecute()
            {
                getViewState().onOrdersDataChanged();
            }

            protected void onError(final Throwable t)
            {
                getViewState().onTableOrdersSyncFailed(t);
            }
        }.execPool();
    }

    public boolean needToSetPaxData(final long tableId)
    {
        final WaiterOrder ticket = getOrderForTable(tableId);
        return ticket != null && ((TextUtils.isEmpty(ticket.getWaiter()) && App.getDataManager().getWaiterDataManage().getConfiguration().hasWaiterData()) || (TextUtils.isEmpty(ticket.getPax()) && App.getDataManager().getWaiterDataManage().getConfiguration().hasPaxData()));
    }

    public WaiterOrderSaleItem getOrderItem(final long orderItemId)
    {
        return App.getDataManager().getWaiterDataManage().findOrderItem(orderItemId);
    }

    public void moveAllToSaveLater(final long tableId)
    {
        App.getDataManager().getWaiterDataManage().markAllPreordersAsServeLater(tableId);
    }

    public void setQuickMode(final boolean quickMode)
    {
        this.quickMode = quickMode;
    }

    public boolean isQuickMode()
    {
        return quickMode;
    }

    public void checkSoldOuts(final long tableId)
    {
        new RTAsyncTask()
        {
            String message;

            protected void doInBackground() throws Throwable
            {
                message = App.getDataManager().getWaiterDataManage().checkSoldOutsForTable(tableId);
            }

            protected void onPostExecute()
            {
                getViewState().onSoldOutCheckResult(message);
            }

            protected void onError(final Throwable t)
            {
                getViewState().onSoldOutCheckResult(null);
            }
        }.execPool();
    }

    class Subscriber
    {

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onOrderItemsChanged(OrderItemsChangedEvent event)
        {
            getViewState().onOrdersDataChanged();
        }

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onOrdersChangedEvent(OrdersChangedEvent event)
        {
            getViewState().onOrdersDataChanged();
        }
    }

    @StateStrategyType(AddToEndStrategy.class)
    public interface View extends MvpView
    {

        void onOrdersDataChanged();

        void onTableOrdersSyncFailed(Throwable t);

        void onTableOrdersSyncCompleted();

        void onBillPrintingError(Throwable t);

        void onBillPrinted();

        void onPreorderSubmitFailed(Throwable t);

        void onPreorderSubmitted(final long serverTicketId);

        void onSoldOutCheckResult(String message);
    }
}

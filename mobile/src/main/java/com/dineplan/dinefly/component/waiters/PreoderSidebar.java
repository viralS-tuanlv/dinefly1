package com.dineplan.dinefly.component.waiters;

import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.activity.base.BaseActivity;
import com.dineplan.dinefly.data.model.waiters.WaiterOrder;
import com.dineplan.dinefly.data.model.waiters.WaiterOrderSaleItem;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 13/10/2017
 */
public class PreoderSidebar
{

    @BindView(R.id.preOrderList)
    TicketOrdersList orderList;

    @BindView(R.id.waitersBtnPreorderSubmit)
    View btnSubmit;

    @BindView(R.id.waitersBtnPreorderClear)
    View btnClear;

    Drawer drawer;
    Callback callback;
    boolean hasPreorders;


    public PreoderSidebar(final BaseActivity host, final Callback callback)
    {
        this.callback = callback;
        configure(host);
    }

    private void configure(final BaseActivity host)
    {
        final View ordersListPanel = LayoutInflater.from(host).inflate(R.layout.waiters_view_sidebarpreorderspanel, null, false);
        ButterKnife.bind(this, ordersListPanel);

        orderList.setCallback(new TicketOrdersList.Callback()
        {
            public void onRemovePreorderedItem(final WaiterOrderSaleItem item)
            {
                if (callback != null)
                {
                    callback.onRemovePreorderItemFromDrawer(item);
                }
            }

            public void onHeaderButtonClick(final Object tag)
            {
                if (callback!=null)
                {
                    callback.onMoveAllPreorderedItemsToServeLaterStatus();
                }
            }

            public void onEditTicketNotes(final WaiterOrder ticket)
            {

            }

            public void onEditPreorderedItem(final WaiterOrderSaleItem item)
            {
                if (callback != null)
                {
                    callback.onEditPreorderedItem(item);
                }
            }
        });

        drawer = new DrawerBuilder()
                         .withActivity(host)
                         .withCustomView(ordersListPanel)
                         .withTranslucentStatusBar(false)
                         .withDisplayBelowStatusBar(true)
                         .withDrawerGravity(Gravity.RIGHT)
                         .withFooterDivider(false)
                         .withStickyFooterDivider(false)
                         .withHeaderDivider(false)
                         .withStickyFooterShadow(false)
                         .withGenerateMiniDrawer(true)
                         .build();
    }

    @OnClick(R.id.waitersBtnPreorderSubmit)
    void onOrderNowClick()
    {
        if (callback != null)
        {
            callback.onSubmitPreorder();
        }
    }

    @OnClick(R.id.btnNotes)
    void onNotesEditorClick()
    {
        if (callback!=null)
        {
            callback.onEditPreorderNotes();
        }
    }

    @OnClick(R.id.waitersBtnPreorderClear)
    void onClearClick()
    {
        if (callback != null)
        {
            callback.onClearPreorder();
        }
    }

    public void close()
    {
        if (drawer.isDrawerOpen())
        {
            drawer.closeDrawer();
        }
    }

    public void setData(final WaiterOrder orders)
    {
        hasPreorders = orders.getPreordersCount()>0;

        btnClear.setVisibility(hasPreorders ? View.VISIBLE : View.GONE);
        btnSubmit.setVisibility(hasPreorders ? View.VISIBLE : View.GONE);
        orderList.setData(orders, true);
    }

    public void closeAndLock()
    {
        drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void unlock()
    {
        drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void open()
    {
        drawer.openDrawer();
    }

    public boolean hasPreorders()
    {
        return hasPreorders;
    }

    public boolean isOpen()
    {
        return drawer.isDrawerOpen();
    }

    public void setQuickOrderMode(final boolean quickOrderMode)
    {
        orderList.setQuickOrderMode(quickOrderMode);
    }

    public interface Callback
    {

        void onClearPreorder();

        void onSubmitPreorder();

        void onRemovePreorderItemFromDrawer(WaiterOrderSaleItem item);

        void onEditPreorderedItem(WaiterOrderSaleItem item);

        void onMoveAllPreorderedItemsToServeLaterStatus();

        void onEditPreorderNotes();
    }

}

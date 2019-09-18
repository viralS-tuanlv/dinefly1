package com.dineplan.dinefly.activity.waiters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.activity.waiters.dialogprint.DialogPrint;
import com.dineplan.dinefly.component.waiters.TicketOrdersList;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.data.model.waiters.WaiterOrder;
import com.dineplan.dinefly.data.model.waiters.WaiterOrderSaleItem;
import com.dineplan.dinefly.data.model.waiters.WaiterRoom;
import com.dineplan.dinefly.data.model.waiters.WaiterTable;
import com.dineplan.dinefly.dialog.waiters.PaxDataDialog;
import com.dineplan.dinefly.logic.waiters.WaiterOrderPresenter;
import com.dineplan.dinefly.logic.waiters.WaiterTablePresenter;
import com.dineplan.dinefly.util.FormattingUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.workarounds.bundler.Bundler;
import in.workarounds.bundler.annotations.Arg;
import in.workarounds.bundler.annotations.RequireBundler;
import in.workarounds.bundler.annotations.Required;

/**
 * Created by dlivotov on 07/06/2016.
 */

@RequireBundler
public class WaitersTableActivity extends WaitersBaseActivity implements WaiterTablePresenter.View, WaiterOrderPresenter.View, SwipeRefreshLayout.OnRefreshListener
{

    private static final int REQUEST_CODE_ADDMENU = 100;

    @Arg
    @Required
    long tableId;

    @Arg
    boolean forceStayInTicketDetails;

    @BindView(R.id.waitersToolbar)
    Toolbar toolbar;

    @BindView(R.id.refresher)
    SwipeRefreshLayout refresher;

    @BindView(R.id.waiterTableTitle)
    TextView tableTitle;

    @BindView(R.id.waiterTableGuestsTitle)
    TextView guestsCountTitle;

    @BindView(R.id.waiterTableGuests)
    TextView guestsCount;

    @BindView(R.id.waiterTableDurationTitle)
    TextView guestsDurationTitle;

    @BindView(R.id.waiterTableDuration)
    TextView guestsDuration;

    @BindView(R.id.waiterTableWaiterTitle)
    TextView guestsWaiterTitle;

    @BindView(R.id.waiterTableWaiter)
    TextView guestsWaiter;

    @BindView(R.id.ordersList)
    TicketOrdersList orderList;

    @BindView(R.id.btnPayOrder)
    Button btnFialize;

    @BindView(R.id.btnFeedback)
    Button btnFeedback;

    @InjectPresenter
    WaiterTablePresenter tablePresenter;

    @InjectPresenter
    WaiterOrderPresenter orderPresenter;

    private long ticketId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundler.inject(this);

        if (checkAuthorisation())
        {
            setContentView(R.layout.activity_waiters_table);
            configure();
            showData(true);
        }
    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
    {
        if (requestCode == REQUEST_CODE_ADDMENU && resultCode == RESULT_CANCELED && orderPresenter.getOrderForTable(tableId).getItems().size() == 0)
        {
            finish();
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showData(boolean startup)
    {
        final WaiterTable table = tablePresenter.getTable(tableId);

        if (table == null)
        {
            finish();
        }
        else
        {
            final WaiterRoom room = table.getRoom().getTarget();
            final WaiterOrder ticket = orderPresenter.getOrderForTable(tableId);
            final float orderAmount = ticket.findTotal();

            if (room != null)
            {
                toolbar.setTitle(getString(R.string.table_title, table.getName(), room.getName()));
            }
            else
            {
                toolbar.setTitle(getString(R.string.table_title_simple, table.getName()));
            }

            guestsCount.setText(!TextUtils.isEmpty(ticket.getPax()) ? ticket.getPax() : "-");
            guestsWaiter.setText(!TextUtils.isEmpty(ticket.getWaiter()) ? ticket.getWaiter() : "-");
            guestsDuration.setVisibility(View.INVISIBLE);
            guestsDurationTitle.setVisibility(View.INVISIBLE);

            btnFialize.setText("0.00");
            ticketId = ticket.getDineplanOrderId();
            tableTitle.setText(getTicketNumber(ticket));
            guestsCountTitle.setVisibility(ticket.getDineplanOrderId() != 0 ? View.VISIBLE : View.INVISIBLE);
            guestsCount.setVisibility(ticket.getDineplanOrderId() != 0 ? View.VISIBLE : View.INVISIBLE);
            guestsWaiterTitle.setVisibility(ticket.getDineplanOrderId() != 0 ? View.VISIBLE : View.INVISIBLE);
            guestsWaiter.setVisibility(ticket.getDineplanOrderId() != 0 ? View.VISIBLE : View.INVISIBLE);
            btnFialize.setVisibility(ticket.getDineplanOrderId() != 0 ? View.VISIBLE : View.INVISIBLE);

            orderList.setData(ticket, false);
            btnFialize.setText(FormattingUtil.formatMenuPrice(orderAmount));

            toolbar.getMenu().findItem(R.id.menu_move_table).setVisible(ticket.getDineplanOrderId() != 0);
            toolbar.getMenu().findItem(R.id.menu_bill).setVisible(ticket.getDineplanOrderId() != 0);
            toolbar.invalidate();

            if (startup)
            {
                syncTable();

                if (!forceStayInTicketDetails)
                {
                    onAddMealClick();
                }
            }
        }
    }
    private String getTicketNumber(WaiterOrder ticket) {
        if (ticket.getTicketNumber() != null) {
            return getString(R.string.order_number_title_number, ticket.getTicketNumber());
        }
        return ticket.getDineplanOrderId() != 0 ? getString(R.string.order_number_title, ticket.getDineplanOrderId()) : getString(R.string.new_unsynced_order);
    }

    @OnClick({R.id.waiterTableGuestsTitle, R.id.waiterTableGuests, R.id.waiterTableWaiter, R.id.waiterTableWaiterTitle})
    void onChangePaxDataClick()
    {
        final WaiterOrder ticket = orderPresenter.getOrderForTable(tableId);

        if (ticket != null)
        {
            new PaxDataDialog(this).show(ticket.getWaiter(), ticket.getPax(), new PaxDataDialog.Callback()
            {
                public void onPaxDataSet(final String pax, final String waiter)
                {
                    orderPresenter.updateOrderMetadata(ticket.getId(), pax, waiter);
                }
            });
        }
    }

    private void syncTable()
    {
        refresher.setRefreshing(true);
        orderPresenter.syncTableOrders(tableId);
    }

    private void configure()
    {
        toolbar.inflateMenu(R.menu.waiters_table_menu);

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            public void onClick(final View view)
            {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
        {
            public boolean onMenuItemClick(final MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.menu_move_table:
                        onMoveTable();
                        return true;
                        case R.id.menu_bill:
                        new DialogPrint(WaitersTableActivity.this, ticketId).show();
                        return true;
                }

                return false;
            }
        });

        orderList.setCallback(new TicketOrdersList.Callback()
        {
            public void onRemovePreorderedItem(final WaiterOrderSaleItem item)
            {

            }

            public void onHeaderButtonClick(final Object tag)
            {
                if (tag == TicketOrdersList.TAG_SERVE_DELAYED)
                {
                    refresher.setRefreshing(true);
                    orderPresenter.submitPendingMeals(tableId);
                }
            }

            public void onEditTicketNotes(final WaiterOrder ticket)
            {
                doEditNotes(ticket);
            }

            public void onEditPreorderedItem(final WaiterOrderSaleItem item)
            {
                // unused here
            }
        });

        refresher.setColorSchemeResources(R.color.color_primary, R.color.color_light_blue, R.color.material_green_700);
        refresher.setOnRefreshListener(this);
    }

    private void onMoveTable()
    {
        final List<WaiterTable> tables = tablePresenter.getTablesToMoveTo(tableId);

        if (tables.size() == 0)
        {
            showMessage(R.string.no_vacant_tables_to_move);
            return;
        }

        new MaterialDialog.Builder(this).title(R.string.select_table_to_move).items(tables).itemsCallback(new MaterialDialog.ListCallback()
        {
            public void onSelection(final MaterialDialog dialog, final View itemView, final int position, final CharSequence text)
            {
                showBlockingIndeterminateProgressDialog(false, null, getString(R.string.moving_table));
                tablePresenter.moveToTable(orderPresenter.getOrderForTable(tableId).getId(), tables.get(position).getTableId());
            }
        }).show();
    }

    private void doEditNotes(final WaiterOrder ticket)
    {
        new MaterialDialog.Builder(this).title(R.string.ticket_notes).input(getString(R.string.ticket_notes), ticket.getNotes(), true, new MaterialDialog.InputCallback()
        {
            public void onInput(@NonNull final MaterialDialog dialog, final CharSequence input)
            {
                orderPresenter.changeOrderNotes(tableId, ticket.getId(), input.toString());
                dialog.dismiss();
            }
        }).autoDismiss(false).show();
    }

    @OnClick(R.id.btnAddMeal)
    void onAddMealClick()
    {
        startActivityForResult(Bundler.waitersMenuActivity(tableId).intent(this), REQUEST_CODE_ADDMENU);
    }

    @OnClick(R.id.btnPayOrder)
    void onFinalizeOrderOptionsClick()
    {
        new MaterialDialog.Builder(this).title(R.string.ticket_finalisation).items(getString(R.string.print_bill)).itemsCallback(new MaterialDialog.ListCallback()
        {
            public void onSelection(final MaterialDialog dialog, final View itemView, final int position, final CharSequence text)
            {
                showBlockingIndeterminateProgressDialog(true, null, getString(R.string.printing_bill));
                orderPresenter.printBill(tableId);
            }
        }).show();
    }

    public void onRefresh()
    {
        syncTable();
    }

    public void onOrdersDataChanged()
    {
        refresher.setRefreshing(false);
        showData(false);
    }

    public void onTableOrdersSyncFailed(final Throwable t)
    {
        refresher.setRefreshing(false);
        showToast(R.string.sync_error, false);
    }

    public void onTableOrdersSyncCompleted()
    {
        refresher.setRefreshing(false);
    }

    public void onBillPrintingError(final Throwable t)
    {
        hideBlockingProgressDialog();
        showToast(R.string.error_printing_bill, false);
    }

    @OnClick(R.id.btnFeedback)
    void onFeedbackClick()
    {
        final WaiterOrder order = App.getDataManager().getWaiterDataManage().getTableOrder(tableId);

        if (order != null)
        {
            Bundler.waitersSuccessOrderMessageActivity(order.getId()).start(this);
        }
    }

    public void onBillPrinted()
    {
        hideBlockingProgressDialog();
        showToast(R.string.bill_printed, true);
    }

    public void onPreorderSubmitFailed(final Throwable t)
    {
        hideBlockingProgressDialog();
        showError(t);
    }

    public void onPreorderSubmitted(final long order)
    {
        // unused here
    }

    @Override
    public void onSoldOutCheckResult(String message)
    {

    }

    public void onTableMovementError(final Throwable t)
    {
        hideBlockingProgressDialog();
        showError(t);
    }

    public void onTableMoved(final long ticketId, final long tableId)
    {
        hideBlockingProgressDialog();
        this.tableId = tableId;
        showData(true);
    }

}

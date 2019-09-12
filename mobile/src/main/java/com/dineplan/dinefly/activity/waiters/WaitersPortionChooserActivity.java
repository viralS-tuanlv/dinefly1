package com.dineplan.dinefly.activity.waiters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.OnClick;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.component.waiters.OrderItemEditor;
import com.dineplan.dinefly.component.waiters.OrderItemEditorTwoColumn;
import com.dineplan.dinefly.data.model.waiters.*;
import com.dineplan.dinefly.logic.waiters.WaiterMenuPresenter;
import com.dineplan.dinefly.logic.waiters.WaiterOrderPresenter;
import com.dineplan.dinefly.logic.waiters.WaiterTablePresenter;

import in.workarounds.bundler.Bundler;
import in.workarounds.bundler.annotations.Arg;
import in.workarounds.bundler.annotations.RequireBundler;
import in.workarounds.bundler.annotations.Required;

/**
 * Created by dlivotov on 07/06/2016.
 */

@RequireBundler
public class WaitersPortionChooserActivity extends WaitersBaseActivity implements WaiterMenuPresenter.View, WaiterTablePresenter.View, WaiterOrderPresenter.View {

    @Arg
    @Required
    long tableId;

    @Arg
    @Required
    long menuItemId;

    @Arg
    @Required(false)
    long orderItemId;

    @BindView(R.id.waitersToolbar)
    Toolbar toolbar;

    @BindView(R.id.waitersPortionChooser)
    @Nullable
    OrderItemEditor editor;

    @BindView(R.id.waitersPortionChooserTwoColumn)
    @Nullable
    OrderItemEditorTwoColumn editor2col;

    @BindView(R.id.waitersPortionChooserBtnLater)
    View btnServeLater;

    @InjectPresenter
    WaiterMenuPresenter menuPresenter;

    @InjectPresenter
    WaiterTablePresenter tablePresenter;

    @InjectPresenter
    WaiterOrderPresenter orderPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundler.inject(this);

        if (checkAuthorisation()) {
            setContentView(R.layout.activity_waiters_portionchooser);
            configure();
            showData();
        }
    }

    private void showData() {
        final WaiterTable table = tablePresenter.getTable(tableId);
        final WaiterMenuItem menuItem = menuPresenter.getMenuItem(menuItemId);

        if (table != null && menuItem != null) {
            final WaiterMenuCategory category = menuPresenter.getMenuItemCategory(menuItem.getCategoryId());

            if (table.isVirtualTable()) {
                toolbar.setTitle(getString(R.string.quickorder_portion_chooser_title, category.getName(), menuItem.getName()));
                orderPresenter.setQuickMode(true);
                btnServeLater.setVisibility(View.GONE);
            } else {
                toolbar.setTitle(getString(R.string.table_portion_chooser_title, table.getName(), category.getName(), menuItem.getName()));
            }

            WaiterOrderSaleItem orderItem = null;

            if (orderItemId != 0) {
                orderItem = orderPresenter.getOrderItem(orderItemId);
            }

            if (orderItem == null) {
                orderItem = new WaiterOrderSaleItem(menuItem);
            }

            if (editor != null) {
                editor.setData(orderItem, category, menuItem);
            }

            if (editor2col != null) {
                editor2col.setData(orderItem, category, menuItem);
            }
        } else {
            finish();
        }
    }

    private void configure() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                finish();
            }
        });

        if (editor != null) {
            editor.setShowStockHeader(false);
        }
    }

    @OnClick(R.id.waitersPortionChooserBtnNow)
    void onOrderNowClick() {
        try {
            orderPresenter.addDraftItem(tableId, editor != null ? editor.getEditedOrderItem() : editor2col.getEditedOrderItem(), true);
            finish();
        } catch (Throwable err) {
            showError(err);
        }
    }

    @OnClick(R.id.waitersPortionChooserBtnLater)
    void onServeLaterClick() {
        try {
            orderPresenter.addDraftItem(tableId, editor != null ? editor.getEditedOrderItem() : editor2col.getEditedOrderItem(), false);
            finish();
        } catch (Throwable err) {
            showError(err);
        }
    }

    public void onOrdersDataChanged() {

    }

    public void onTableOrdersSyncFailed(final Throwable t) {

    }

    public void onTableOrdersSyncCompleted() {

    }

    public void onBillPrintingError(final Throwable t) {

    }

    public void onBillPrinted() {

    }

    public void onPreorderSubmitFailed(final Throwable t) {
        hideBlockingProgressDialog();
        showError(t);
    }

    public void onPreorderSubmitted(final long order) {
        // unused here
    }

    @Override
    public void onSoldOutCheckResult(String message) {

    }

    public void onTableMovementError(final Throwable t) {
        // unused here
    }

    public void onTableMoved(final long ticketId, final long tableId) {
        // unused here
    }
}

package com.dineplan.dinefly.component.waiters;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.activity.waiters.WaitersMenuActivity;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuCategory;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuItem;
import com.dineplan.dinefly.data.model.waiters.WaiterOrderSaleItem;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 14/10/2017
 */
public class MenuItemOrderEditorDialog
{

    private final WaiterMenuItem menuItem;
    private final WaiterOrderSaleItem order;
    private final OrderItemEditor editor;
    private final MaterialDialog dialog;
    private Callback callback;

    public MenuItemOrderEditorDialog(final WaitersMenuActivity ctx, final WaiterMenuCategory category, final WaiterMenuItem menuItem, final WaiterOrderSaleItem order)
    {
        this.menuItem = menuItem;
        this.order = order;

        editor = new OrderItemEditor(ctx);
        editor.setData(order, category, menuItem);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(ctx)
                                                 .customView(editor, false)
                                                 .canceledOnTouchOutside(false)
                                                 .cancelable(true);

        if (order == null || order.getId() == 0)
        {
            builder.positiveText(R.string.order_dialog_order);
            builder.neutralText(R.string.order_dialog_orderlater);

            builder.onNeutral(new MaterialDialog.SingleButtonCallback()
            {
                public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which)
                {
                    if (callback != null)
                    {
                        callback.onCreateNewOrderItem(editor.getEditedOrderItem(), editor.getCategory(), editor.getStock(), true);
                    }
                }
            });

            builder.onPositive(new MaterialDialog.SingleButtonCallback()
            {
                public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which)
                {
                    if (callback != null)
                    {
                        callback.onCreateNewOrderItem(editor.getEditedOrderItem(), editor.getCategory(), editor.getStock(), false);
                    }
                }
            });
        } else
        {
            builder.positiveText(R.string.order_dialog_update);

            builder.onPositive(new MaterialDialog.SingleButtonCallback()
            {
                public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which)
                {
                    if (callback != null)
                    {
                        callback.onUpdateOrderItem(editor.getEditedOrderItem(), editor.getCategory(), editor.getStock());
                    }
                }
            });
        }


        if (TextUtils.isEmpty(menuItem.getDineplanPictrue()))
        {
            builder.title(menuItem.getName());
        }

        dialog = builder.build();
    }

    public MenuItemOrderEditorDialog callback(Callback callback)
    {
        this.callback = callback;
        return this;
    }

    public void show()
    {
        dialog.show();
    }

    public interface Callback
    {

        void onUpdateOrderItem(WaiterOrderSaleItem orderItem, WaiterMenuCategory category, WaiterMenuItem stock);

        void onCreateNewOrderItem(WaiterOrderSaleItem orderItem, WaiterMenuCategory category, WaiterMenuItem stock, boolean serveLater);
    }
}

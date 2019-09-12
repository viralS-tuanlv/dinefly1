package com.dineplan.dinefly.component.waiters;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dineplan.dinefly.activity.waiters.WaitersMenuActivity;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 14/10/2017
 */
public class SubtagsSelectionDialog
{

    private final List<WaiterMenuItem> subtags = new ArrayList<>();
    private MaterialDialog dialog;
    private final MaterialDialog.Builder builder;
    private WaiterMenuItem tagRoot;
    private Callback callback;

    public SubtagsSelectionDialog(final WaitersMenuActivity ctx, final WaiterMenuItem root, final List<WaiterMenuItem> subtags, Callback cb)
    {
        this.tagRoot = root;
        this.subtags.addAll(subtags);
        this.callback = cb;

        MenuGrid grid = new MenuGrid(ctx);
        grid.setData(subtags, true);
        grid.setCallback(new MenuGrid.Callback()
        {
            @Override
            public void onTableSelected(WaiterMenuItem item, List<WaiterMenuItem> tagData)
            {
                if (callback!=null)
                {
                    callback.onSubtagSelected(item);
                }
            }
        });

        builder = new MaterialDialog.Builder(ctx).customView(grid, false).canceledOnTouchOutside(false).title(tagRoot.getName()).cancelable(true);
    }

    public void show()
    {
        dialog = builder.show();
    }

    public void dismiss()
    {
        if (dialog!=null)
        {
            dialog.dismiss();
        }
    }

    public interface Callback
    {

        void onSubtagSelected(WaiterMenuItem subtag);
    }
}

package com.dineplan.dinefly.component.waiters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.dineplan.dinefly.R;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
public class SimpleTableView extends TableView
{

    @BindView(R.id.waitersCompTableBase)
    View tableBase;

    @BindView(R.id.waitersCompTableNumberBig)
    TextView tableNumber;

    public SimpleTableView(final Context context)
    {
        super(context);
        configureView();
    }

    public SimpleTableView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        configureView();
    }

    public SimpleTableView(final Context context, final AttributeSet attrs, final int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configureView();
    }

    public SimpleTableView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        configureView();
    }

    protected void displayTable()
    {
        if (payload != null)
        {
            tableNumber.setText(payload.getName());
            tableBase.setBackgroundColor(getContext().getResources().getColor(payload.isBusy() ? R.color.color_primary : R.color.material_grey_500));
            tableNumber.setTextColor(getContext().getResources().getColor(payload.isBusy() ? R.color.material_grey_50 : R.color.material_grey_900));
        }
    }

    private void configureView()
    {

    }

    protected int getTableLayoutResource()
    {
        return R.layout.waiters_view_table_simple;
    }
}

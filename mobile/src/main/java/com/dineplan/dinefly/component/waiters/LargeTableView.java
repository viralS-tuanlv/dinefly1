package com.dineplan.dinefly.component.waiters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.dineplan.dinefly.R;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
public class LargeTableView extends TableView
{

    @BindView(R.id.waitersCompTableBase)
    View tableBase;

    @BindView(R.id.waitersCompTableDivider)
    View tableDivider;

    @BindView(R.id.waitersCompTableNumber)
    TextView tableNumber;

    @BindView(R.id.waitersCompTableNumberBig)
    TextView tableNumberBig;

    @BindView(R.id.waitersCompTableGuests)
    TextView tableGuestsCount;

    @BindView(R.id.waitersCompTableMeals)
    TextView tableMealsCount;

    @BindView(R.id.waitersCompTableIconAlert)
    ImageView iconAlert;

    public LargeTableView(final Context context)
    {
        super(context);
        configureView();
    }

    public LargeTableView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        configureView();
    }

    public LargeTableView(final Context context, final AttributeSet attrs, final int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configureView();
    }

    public LargeTableView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        configureView();
    }

    protected void displayTable()
    {
        if (payload != null)
        {
            if (payload.isBusy())
            {
                tableBase.setBackgroundResource(R.drawable.bg_table_busy_normal);
                tableNumber.setText(payload.getName());
                tableGuestsCount.setText("1");
                tableMealsCount.setText(R.string.symbol_infinite);

                tableDivider.setVisibility(VISIBLE);
                tableNumber.setVisibility(VISIBLE);
                tableNumberBig.setVisibility(INVISIBLE);
                tableGuestsCount.setVisibility(VISIBLE);
                tableMealsCount.setVisibility(VISIBLE);
                iconAlert.setVisibility(INVISIBLE);
            } else
            {
                tableBase.setBackgroundResource(R.drawable.bg_table_vacant);
                tableNumberBig.setText(payload.getName());

                tableDivider.setVisibility(INVISIBLE);
                tableNumber.setVisibility(INVISIBLE);
                tableNumberBig.setVisibility(VISIBLE);
                tableGuestsCount.setVisibility(INVISIBLE);
                tableMealsCount.setVisibility(INVISIBLE);
                iconAlert.setVisibility(INVISIBLE);
            }
        } else
        {
            tableBase.setBackgroundResource(R.drawable.bg_table_vacant);
            tableDivider.setVisibility(INVISIBLE);
            tableNumber.setVisibility(INVISIBLE);
            tableNumberBig.setVisibility(INVISIBLE);
            tableGuestsCount.setVisibility(INVISIBLE);
            tableMealsCount.setVisibility(INVISIBLE);
            iconAlert.setVisibility(INVISIBLE);
        }
    }

    private void configureView()
    {

    }

    protected int getTableLayoutResource()
    {
        return R.layout.waiters_view_table_big;
    }
}

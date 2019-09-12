package com.dineplan.dinefly.component.waiters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuCategory;
import com.dineplan.dinefly.data.model.waiters.WaiterTable;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.extra.Scale;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
public class CategoryStripView extends FrameLayout
{
    WaiterMenuCategory payload;

    @BindView(R.id.waiterCatStripTextNormal)
    TextView titleNormal;

    @BindView(R.id.waiterCatStripTextSelected)
    TextView titleSelected;


    public CategoryStripView(final Context context)
    {
        super(context);
        configure();
    }

    public CategoryStripView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public CategoryStripView(final Context context, final AttributeSet attrs, final int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configure();
    }

    @TargetApi(21)
    public CategoryStripView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        configure();
    }

    public void setPayload(WaiterMenuCategory cat)
    {
        payload = cat;
        displayCategory();
    }

    public void setSelected(final boolean selected)
    {
        super.setSelected(selected);
        displayCategory();
    }

    public WaiterMenuCategory getPayload()
    {
        return payload;
    }

    protected void displayCategory()
    {
        titleNormal.setText(payload.getName());
        titleSelected.setText(payload.getName());

        titleSelected.setVisibility(isSelected() ? VISIBLE : GONE);
        titleNormal.setVisibility(isSelected() ? GONE : VISIBLE);

        setBackgroundColor(isSelected() ? Color.parseColor("#229FEA") : Color.TRANSPARENT);
    }

    private void configure()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.waiter_view_category_strip_view, this, true);
        ButterKnife.bind(this);
    }


}

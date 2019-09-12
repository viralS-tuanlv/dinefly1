package com.dineplan.dinefly.component.commons;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import butterknife.BindView;
import com.dineplan.dinefly.R;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 23/10/2017
 */
public class SecondarySettingsItemView extends SettingsItemView
{
    @BindView(R.id.cfgSwitch)
    SwitchCompat switchView;

    public SecondarySettingsItemView(final Context context)
    {
        super(context);
        initView(null);
    }

    public SecondarySettingsItemView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        initView(attrs);
    }

    public SecondarySettingsItemView(final Context context, final AttributeSet attrs, final int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    @TargetApi(21)
    public SecondarySettingsItemView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
    }

    private void initView(AttributeSet attrs)
    {
        if (attrs != null)
        {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SettingsViewsFramework);

            if (a.hasValue(R.styleable.SettingsViewsFramework_fly_switch))
            {
                showSwitch(a.getBoolean(R.styleable.SettingsViewsFramework_fly_switch, false));
            }

            if (a.hasValue(R.styleable.SettingsViewsFramework_fly_switchValue))
            {
                switchView.setChecked(a.getBoolean(R.styleable.SettingsViewsFramework_fly_switchValue, false));
                showSwitch(true);
            }

            a.recycle();
        }
    }

    public void showSwitch(final boolean show)
    {
        switchView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public SwitchCompat getSwitchView()
    {
        return switchView;
    }

    @LayoutRes
    protected int getItemLayout()
    {
        return R.layout.commons_view_settings_secondary;
    }

}

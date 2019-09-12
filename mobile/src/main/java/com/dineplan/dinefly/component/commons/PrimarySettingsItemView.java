package com.dineplan.dinefly.component.commons;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.dineplan.dinefly.R;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 23/10/2017
 */
public class PrimarySettingsItemView extends SettingsItemView
{

    public PrimarySettingsItemView(final Context context)
    {
        super(context);
    }

    public PrimarySettingsItemView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
    }

    public PrimarySettingsItemView(final Context context, final AttributeSet attrs, final int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public PrimarySettingsItemView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @LayoutRes
    protected int getItemLayout()
    {
        return R.layout.commons_view_settings_main;
    }

}

package com.dineplan.dinefly.component.commons;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.dineplan.dinefly.R;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 23/10/2017
 */
public class SettingsSectionView extends LinearLayout
{

    @BindView(R.id.cfgSectionTitle)
    TextView title;

    public SettingsSectionView(Context context)
    {
        super(context);
        configure(null);
    }

    public SettingsSectionView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        configure(attrs);
    }

    public SettingsSectionView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configure(attrs);
    }

    @TargetApi(21)
    public SettingsSectionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        configure(attrs);
    }

    private void configure(AttributeSet attrs)
    {
        setOrientation(VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.commons_view_settings_section, this, true);
        ButterKnife.bind(this);

        if (attrs != null)
        {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SettingsViewsFramework);

            if (a.hasValue(R.styleable.SettingsViewsFramework_fly_title))
            {
                title.setText(a.getString(R.styleable.SettingsViewsFramework_fly_title));
            }

            a.recycle();
        }
    }

    public void setTitle(@StringRes final int title)
    {
        this.title.setText(title);
    }

    public void setTitle(final CharSequence title)
    {
        this.title.setText(TextUtils.isEmpty(title) ? "" : title);
    }

}
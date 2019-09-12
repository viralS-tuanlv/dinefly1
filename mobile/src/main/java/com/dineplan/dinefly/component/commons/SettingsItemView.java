package com.dineplan.dinefly.component.commons;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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
import com.dineplan.dinefly.core.App;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 23/10/2017
 */
public abstract class SettingsItemView extends FrameLayout
{

    @BindView(R.id.cfgTitle)
    @Nullable
    TextView title;

    @BindView(R.id.cfgDescription)
    @Nullable
    TextView description;

    @BindView(R.id.cfgIcon)
    @Nullable
    ImageView icon;

    @BindView(R.id.cfgAction)
    @Nullable
    ImageView actionIcon;

    public SettingsItemView(final Context context)
    {
        super(context);
        configure(null);
    }

    public SettingsItemView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        configure(attrs);
    }

    public SettingsItemView(final Context context, final AttributeSet attrs, final int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configure(attrs);
    }

    @TargetApi(21)
    public SettingsItemView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        configure(attrs);
    }

    private void configure(AttributeSet attrs)
    {
        LayoutInflater.from(getContext()).inflate(getItemLayout(), this, true);
        ButterKnife.bind(this);

        if (attrs != null)
        {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SettingsViewsFramework);

            if (a.hasValue(R.styleable.SettingsViewsFramework_fly_title))
            {
                setTitle(a.getString(R.styleable.SettingsViewsFramework_fly_title));
            }

            if (a.hasValue(R.styleable.SettingsViewsFramework_fly_subtitle))
            {
                setDescription(a.getString(R.styleable.SettingsViewsFramework_fly_subtitle));
            } else
            {
                setDescription(null);
            }

            if (a.hasValue(R.styleable.SettingsViewsFramework_fly_icon))
            {
                setIcon(a.getResourceId(R.styleable.SettingsViewsFramework_fly_icon, 0));
            } else
            {
                setIcon(0);
            }

            if (a.hasValue(R.styleable.SettingsViewsFramework_fly_actionIcon))
            {
                setActionIcon(a.getResourceId(R.styleable.SettingsViewsFramework_fly_actionIcon, 0));
            } else
            {
                setActionIcon(0);
            }

            a.recycle();
        }
    }

    @LayoutRes
    protected abstract int getItemLayout();

    public void setIcon(final @DrawableRes int icon)
    {
        if (this.icon != null)
        {
            if (icon != 0)
            {
                this.icon.setImageResource(icon);
                this.icon.setVisibility(View.VISIBLE);
            } else
            {
                this.icon.setVisibility(View.GONE);
            }
        }
    }

    public void setActionIcon(final @DrawableRes int icon)
    {
        if (this.actionIcon != null)
        {
            if (icon != 0)
            {
                this.actionIcon.setImageResource(icon);
                this.actionIcon.setVisibility(View.VISIBLE);
            } else
            {
                this.actionIcon.setVisibility(View.GONE);
            }
        }
    }

    public void setTitle(final @StringRes int title)
    {
        setTitle(getContext().getString(title));
    }

    public void setTitle(final CharSequence title)
    {
        if (this.title != null)
        {
            this.title.setText(TextUtils.isEmpty(title) ? "" : title);
        }
    }

    public void setDescription(final @StringRes int description)
    {
        setDescription(getContext().getString(description));
    }

    public void setDescription(final CharSequence description)
    {
        if (this.description != null)
        {
            this.description.setText(TextUtils.isEmpty(description) ? "" : description);
            this.description.setVisibility(TextUtils.isEmpty(description) ? View.GONE : View.VISIBLE);
        }
    }

    public void setActive(final boolean active)
    {
        if (active)
        {
            setBackgroundColor(getContext().getResources().getColor(R.color.color_accent));
            setSelected(true);

            if (icon!=null)
            {
                icon.setColorFilter(Color.WHITE);
            }

            if (actionIcon!=null)
            {
                actionIcon.setColorFilter(Color.WHITE);
            }

            if (title!=null)
            {
                title.setTextColor(Color.WHITE);
            }

            if (description!=null)
            {
                description.setTextColor(Color.WHITE);
            }
        } else
        {
            setBackgroundColor(Color.TRANSPARENT);
            setSelected(false);

            if (icon!=null)
            {
                icon.setColorFilter(Color.TRANSPARENT);
            }

            if (actionIcon!=null)
            {
                actionIcon.setColorFilter(Color.TRANSPARENT);
            }

            if (title!=null)
            {
                title.setTextColor(Color.BLACK);
            }

            if (description!=null)
            {
                description.setTextColor(Color.BLACK);
            }
        }
    }
}

package com.dineplan.dinefly.panels.waiters.settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.component.commons.SecondarySettingsItemView;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.data.TablesDisplayMode;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 23/10/2017
 */
public class UIAppLocksSettingsPanel extends FrameLayout implements CompoundButton.OnCheckedChangeListener
{

    @BindView(R.id.settingsLock)
    SecondarySettingsItemView settingsLock;

    @BindView(R.id.logoutLock)
    SecondarySettingsItemView logoutLock;

    @BindView(R.id.tableLock)
    SecondarySettingsItemView tableLock;

    Callback callback;

    public UIAppLocksSettingsPanel(final Context context)
    {
        super(context);
        configure();
    }

    public UIAppLocksSettingsPanel(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public UIAppLocksSettingsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configure();
    }

    @TargetApi(21)
    public UIAppLocksSettingsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        configure();
    }

    public Callback getCallback()
    {
        return callback;
    }

    public void setCallback(final Callback callback)
    {
        this.callback = callback;
    }

    private void configure()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.commons_panel_settings_applocks, this, true);
        ButterKnife.bind(this);
        showSettings();
    }

    private void showSettings()
    {
        settingsLock.getSwitchView().setOnCheckedChangeListener(null);
        logoutLock.getSwitchView().setOnCheckedChangeListener(null);
        tableLock.getSwitchView().setOnCheckedChangeListener(null);

        settingsLock.getSwitchView().setChecked(App.getSettings().isSettingsLockActive());
        logoutLock.getSwitchView().setChecked(App.getSettings().isLogoutLockActive());
        tableLock.getSwitchView().setChecked(App.getSettings().isTableLockActive());

        settingsLock.getSwitchView().setOnCheckedChangeListener(this);
        logoutLock.getSwitchView().setOnCheckedChangeListener(this);
        tableLock.getSwitchView().setOnCheckedChangeListener(this);
    }

    @OnClick(R.id.settingsLock)
    public void onChangeSettingsLock()
    {
        settingsLock.getSwitchView().setChecked(!settingsLock.getSwitchView().isChecked());
    }

    @OnClick(R.id.logoutLock)
    public void onChangeLogoutLock()
    {
        logoutLock.getSwitchView().setChecked(!logoutLock.getSwitchView().isChecked());
    }

    @OnClick(R.id.tableLock)
    public void onChangeTableLock()
    {
        tableLock.getSwitchView().setChecked(!tableLock.getSwitchView().isChecked());
    }

    public void onCheckedChanged(final CompoundButton compoundButton, final boolean b)
    {
        if (settingsLock.getSwitchView() == compoundButton)
        {
            App.getSettings().setSettingsLockActive(b);
        }

        if (logoutLock.getSwitchView() == compoundButton)
        {
            App.getSettings().setLogoutLockActive(b);
        }

        if (tableLock.getSwitchView() == compoundButton)
        {
            App.getSettings().setTableLockActive(b);
        }

        showSettings();
    }

    public interface Callback
    {

    }
}

package com.dineplan.dinefly.panels.commons.settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.component.commons.PrimarySettingsItemView;
import com.dineplan.dinefly.core.App;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 23/10/2017
 */
public class MainSettingsPanel extends FrameLayout
{

    @BindView(R.id.settingsEntitiesVisulaisation)
    PrimarySettingsItemView itemEntities;

    @BindView(R.id.settingsMenuVisulaisation)
    PrimarySettingsItemView itemMenu;

    @BindView(R.id.settingsAppLocking)
    PrimarySettingsItemView itemAppLocks;

    @BindView(R.id.app_version)
    TextView appVersion;

    boolean stateless = true;

    Callback callback;

    
    public MainSettingsPanel(final Context context)
    {
        super(context);
        configure();
    }

    public MainSettingsPanel(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public MainSettingsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configure();
    }

    @TargetApi(21)
    public MainSettingsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes)
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
        LayoutInflater.from(getContext()).inflate(R.layout.commons_panel_settings_main, this, true);
        ButterKnife.bind(this);
        appVersion.setText(getContext().getString(R.string.app_version_footer, App.getVersionName(), App.getVersionCode()));
    }

    public boolean isStateless()
    {
        return stateless;
    }

    public void setStateless(final boolean stateless)
    {
        this.stateless = stateless;
    }

    @OnClick(R.id.settingsEntitiesVisulaisation)
    public void onEntitiesSettingsClick()
    {
        if (callback != null)
        {
            callback.onOpenEntitiesUISettings();
        }

        toggleSelections(itemEntities);
    }

    @OnClick(R.id.settingsMenuVisulaisation)
    public void onMenuSettingsClick()
    {
        if (callback != null)
        {
            callback.onOpenMenuUISettings();
        }

        toggleSelections(itemMenu);
    }

    @OnClick(R.id.settingsAppLocking)
    public void onAppLocksSettingsClick()
    {
        if (callback != null)
        {
            callback.onAppLocksUISettings();
        }

        toggleSelections(itemAppLocks);
    }

    private void toggleSelections(final PrimarySettingsItemView item)
    {
        itemEntities.setActive(!stateless && itemEntities == item);
        itemMenu.setActive(!stateless && itemMenu == item);
        itemAppLocks.setActive(!stateless && itemAppLocks == item);
    }

    public interface Callback
    {

        void onOpenEntitiesUISettings();

        void onOpenMenuUISettings();

        void onAppLocksUISettings();
    }
}
